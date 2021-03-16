package com.banti.wallet.ums.scheduling;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.banti.wallet.ums.elasticsearch.repositories.ElasticPayoutRepository;
import com.banti.wallet.ums.enums.PayoutStatus;
import com.banti.wallet.ums.model.MerchantWallet;
import com.banti.wallet.ums.model.WalletTransaction;
import com.banti.wallet.ums.repository.PayoutRepository;
import com.banti.wallet.ums.service.MerchantWalletService;
import com.banti.wallet.ums.service.TransactionService;

@Service
public class PayoutService {
	
      Logger logger = LoggerFactory.getLogger(PayoutService.class);
  
	  @Autowired
	  private MerchantWalletService merchantWalletService;
	  @Autowired 
	  private PayoutRepository payoutRepository ;
	  @Autowired 
	  private ElasticPayoutRepository  elasticPayoutRepository;
	  @Autowired 
	  private TransactionService transactionService;
	//  @Autowired private PayoutRequestBodyValidator payoutRequestBodyValidator ;
	  
	  public Iterable<ElasticPayout> fatchAllPayouts()
	  {
		  return elasticPayoutRepository.findAll();
	  }
	  
	  public ElasticPayout fatchPayout(Long id)
	  {
		  
		  return elasticPayoutRepository.findById(id).get();
	  }
	  
//@Scheduled(fixedRate = 2*60*1000)
@Scheduled(cron = "0 */1 * ? * *")                  // specified time at which trigger has to fire
public void payoutOfMerchants() throws Exception
 {  	
	List<MerchantWallet> merchantWalletList = merchantWalletService.getEnabledMerchantWalletListMysql();	
	
	logger.info("Extracted Enabled merchant wallet {} ",merchantWalletList);
	
	 Calendar cal1 = Calendar.getInstance();
     cal1.set(Calendar.HOUR_OF_DAY,00);
     cal1.set(Calendar.MINUTE,00);
     cal1.set(Calendar.SECOND,0);
     cal1.set(Calendar.MILLISECOND,00);
     
     Calendar cal2 = Calendar.getInstance();
     cal2.set(Calendar.HOUR_OF_DAY,23);
     cal2.set(Calendar.MINUTE,59);
     cal2.set(Calendar.SECOND,59);
     cal2.set(Calendar.MILLISECOND,999);

     Date startDate = cal1.getTime();
     Date endDate   = cal2.getTime();
     
for(MerchantWallet merchantWallet : merchantWalletList )
 {
	   
	     List<WalletTransaction> merchantTransactionList = transactionService.getMerchantTransactionBetweenTime(merchantWallet.getMobileNo(),startDate,endDate); 
	     
	     logger.info("Extracted merchantTransaction {} ",merchantTransactionList);
	 //ENSURE THAT AT LEAT ONE MERCHANT TRANSACTION SHOULD EXIST 
	    if(merchantTransactionList.isEmpty())
		           continue;
	
	   double totalAmount = 0.0;
	   String merchantMobileNo = merchantWallet.getMobileNo();
	 
	   for(WalletTransaction merchantTransaction :merchantTransactionList)
	     	totalAmount+=merchantTransaction.getAmount();
	 
	  logger.info("total amount received {} of {} nuber ", Double.toString(totalAmount),merchantMobileNo);
		 Payout payout= new Payout();
		
		 payout.setMerchantMobileNo(merchantMobileNo);
		 payout.setSendMoneyDate(new Date());
         payout.setStatus(PayoutStatus.SATTELED.name());
         payout.setAmount(totalAmount);
  
         Payout savedPayout = payoutRepository.save(payout); 
        logger.info("saved {}",savedPayout);
       
     //SAVE DATA IN ELASTIC SEARCH DATA BASE
       ElasticPayout  elasticPayout = new  ElasticPayout(); 
       elasticPayout.setAmount(totalAmount);
       elasticPayout.setMerchantMobileNo(merchantMobileNo);
       elasticPayout.setSendMoneyDate(new Date());
       elasticPayout.setStatus(PayoutStatus.SATTELED.name());
       elasticPayout.setPayloadId(savedPayout.getPayloadId());
       elasticPayoutRepository.save(elasticPayout); 
	}
 }

		/*
		 * public void payoutOfMerchant0123456788(String mobileNo) {
		 * logger.info("before payout object creation"); Payout payout= new Payout();
		 * logger.info("after payout object creation");
		 * payout.setAmount(merchantWalletService.getMerchant_0123456788());
		 * payout.setMerchantMobileNo(mobileNo); payout.setSendMoneyDate(new Date());
		 * payout.setStatus(PayloadStatus.SATTELED.name());
		 * 
		 * Payout savedPayout = payoutRepository.save(payout);
		 * logger.info("saved {}",savedPayout);
		 * logger.info("before reset merchant payload amount is %d"
		 * ,merchantWalletService.getMerchant_0123456788());
		 * 
		 * merchantWalletService.setMerchant_0123456788(0);
		 * 
		 * 
		 * // SAVE DATA IN ELASTIC SEARCH DATA BASE ElasticPayout elasticPayout = new
		 * ElasticPayout();
		 * elasticPayout.setAmount(merchantWalletService.getMerchant_0123456788());
		 * elasticPayout.setMerchantMobileNo(mobileNo);
		 * elasticPayout.setSendMoneyDate(new Date());
		 * elasticPayout.setStatus(PayloadStatus.SATTELED.name());
		 * elasticPayout.setPayloadId(savedPayout.getPayloadId());
		 * elasticPayoutRepository.save(elasticPayout); }
		 */

}
