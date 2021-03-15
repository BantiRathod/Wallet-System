/*
 * package com.banti.wallet.ums.scheduling;
 * 
 * import java.util.List; import org.slf4j.Logger; import
 * org.slf4j.LoggerFactory; import
 * org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.scheduling.annotation.Scheduled; import
 * org.springframework.stereotype.Service;
 * 
 * import com.banti.wallet.ums.model.MerchantWallet; import
 * com.banti.wallet.ums.service.MerchantWalletService;
 * 
 * @Service public class JobDetails{
 * 
 * Logger logger = LoggerFactory.getLogger(JobDetails.class);
 * 
 * @Autowired private PayoutController payoutController;
 * 
 * @Autowired private MerchantWalletService merchantWalletService ;
 * 
 * @Scheduled(fixedRate = 1*60*1000) public void executeJob() {
 * 
 * List<MerchantWallet> merchantWalletList =
 * merchantWalletService.getMerchantWalletListMysql();
 * 
 * logger.info("job has been executed successfully"); try {
 * payoutController.payoutOfMerchant0123456789("0123456789");
 * 
 * logger.info("0123456789's payout done");
 * 
 * }catch(Exception e) { logger.error("exception occured "+e.getStackTrace()); }
 * try { payoutController.payoutOfMerchant0123456788("0123456788");
 * logger.info("0123456788's payout done"); }catch(Exception e) {
 * logger.error("exception occured "+e.getStackTrace()); }
 * 
 * 
 * } }
 */
