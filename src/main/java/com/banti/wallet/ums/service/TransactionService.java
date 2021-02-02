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
import com.banti.wallet.ums.pagination.PaginationRequest;
import com.banti.wallet.ums.repository.TransactionRepository;

@Service
@Transactional
public class TransactionService
{
	@Autowired
	private TransactionRepository trepo;
	public Page<WalletTransaction> listAll(PaginationRequest paginationRequest , String payerMobileNo)
	{
	    Pageable pageable = PageRequest.of(paginationRequest.getPageNo(), paginationRequest.getPageSize(), Sort.Direction.ASC, "id");
	    return trepo.findAllByPayerNo(payerMobileNo,pageable);
	}
	
	public WalletTransaction getTransaction(Long id)
	{
		return trepo.findById(id).get();
	}
	public WalletTransaction saveTransaction(WalletTransaction transaction)
	{
		return trepo.save(transaction);
	}

}
