package com.banti.wallet.ums.service;

//import java.util.List;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
//import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.banti.wallet.ums.model.WalletTransaction;
import com.banti.wallet.ums.repository.TransactionRepository;

@Service
@Transactional
public class TransactionService
{
	@Autowired
	private TransactionRepository trepo;
	public Page<WalletTransaction> listAll(int pageNum, String payerNo)
	{
	    int pageSize = 3; 
	    Pageable pageable = PageRequest.of(pageNum-1, pageSize, Sort.Direction.ASC, "amount");
	    return trepo.findAllByPayerNo(payerNo,pageable);
	}
	
	public WalletTransaction getTransaction(Long id)
	{
		return trepo.findById(id).get();
	}
	public WalletTransaction createTransaction(WalletTransaction transaction)
	{
		return trepo.save(transaction);
	}

}
