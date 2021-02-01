package com.banti.wallet.ums.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.banti.wallet.ums.model.Bank;
import com.banti.wallet.ums.service.BankService;

@RestController
public class BankController
{
    @Autowired 
    private BankService bankService;
    
    @PostMapping("/bank")
    public String createBankAccount(@RequestBody Bank bank)
    {    
    	 bankService.saveBankDetail(bank);
    	 return "account has been opened of this account no. "+ bank.getAccountNo();
    }
}
