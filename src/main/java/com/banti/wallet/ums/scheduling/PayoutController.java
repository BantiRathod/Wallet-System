package com.banti.wallet.ums.scheduling;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PayoutController {

	@Autowired
	private PayoutService payoutService;
	
	@GetMapping("/payouts")
	public Iterable<ElasticPayout> getPayouts()
	{
	   return payoutService.fatchAllPayouts();
	}
	
	@GetMapping("/payout/{payoutId}")
	public ElasticPayout getPayout(@PathVariable Long id)
	{
	   return payoutService.fatchPayout(id);
	}

	/*
	 * public void payoutOfMerchant0123456789(String mobileNo) throws Exception {
	 * payoutService.payoutOfMerchant(mobileNo); }
	 * 
	 * public void payoutOfMerchant0123456788(String mobileNo) throws Exception {
	 * payoutService.payoutOfMerchant(mobileNo);
	 * 
	 * }
	 */
}
