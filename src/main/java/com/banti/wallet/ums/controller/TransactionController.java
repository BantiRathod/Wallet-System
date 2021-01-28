package com.banti.wallet.ums.controller;


import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.banti.wallet.ums.model.Transaction;
import com.banti.wallet.ums.model.Wallet;
import com.banti.wallet.ums.service.TransactionService;
import com.banti.wallet.ums.service.UserService;
import com.banti.wallet.ums.service.WalletService;

@RestController
public class TransactionController {

	@Autowired
	private TransactionService ts;
	@Autowired
	private WalletService ws;
	@Autowired
	private UserService us;
	
	@GetMapping("transaction/summary/{id}")
	public  ResponseEntity<Page<Transaction>> getTrans(@PathVariable Long id)
	{
		try
		{
		   String payerNo = us.get(id).getMobileNo();
		   Page<Transaction> page= ts.listAll(1,payerNo);
		   return new  ResponseEntity<Page<Transaction>>(page,HttpStatus.OK);
		}
		catch(NoSuchElementException e)
		{
			return new ResponseEntity<Page<Transaction>>(HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("transaction/status/{id}")
	public ResponseEntity<String> getStatus(@PathVariable Long id) 
	{
		try
		{
			String status = ts.getTransaction(id).getStatus();
			return new ResponseEntity<String>(status,HttpStatus.OK);
		}
		catch(NoSuchElementException e)
		{
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping("/Transaction")
	public void createTran(@RequestBody Transaction transaction)
	{
		
		Wallet payerWallet= ws.get(transaction.getPayerNo());
		Wallet payeeWallet=ws.get(transaction.getPayeeNo());
		Double balance=payerWallet.getBalance();
		if(balance >= transaction.getAmount())              // amount should not exceed 
		{
		      balance-=transaction.getAmount();                                              //money is being deducted  
		      payerWallet.setBalance(balance); 
		      
 		      balance=payeeWallet.getBalance();
 		      
 		      balance+=transaction.getAmount(); 
 		      
 		      payeeWallet.setBalance(balance);
 		      
 		      ws.update(payerWallet);
 		      ws.update(payeeWallet);
 		     transaction.setStatus("succesful");
		}
		else
		{
			 transaction.setStatus("failed : please check your balance");
		}
		
		ts.createTransaction(transaction); 
		
	}
}
