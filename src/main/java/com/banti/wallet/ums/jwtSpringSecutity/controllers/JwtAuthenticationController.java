package com.banti.wallet.ums.jwtSpringSecutity.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.banti.wallet.ums.jwtSpringSecutity.configurations.JwtTokenUtil;
import com.banti.wallet.ums.jwtSpringSecutity.models.JwtRequest;
import com.banti.wallet.ums.jwtSpringSecutity.models.JwtResponse;
import com.banti.wallet.ums.jwtSpringSecutity.services.JwtUserDetailsService;

@RestController
@CrossOrigin(origins={ "http://localhost:3000"})
public class JwtAuthenticationController {
	
    Logger logger = LoggerFactory.getLogger(JwtAuthenticationController.class);
	
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	@Autowired
	private JwtUserDetailsService userDetailsService;
	
	@PostMapping("/authenticate")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
		
		 logger.info("authenticationRequest is received {}",authenticationRequest);
		
		 UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUserName());
		logger.info("userDetails is  received {}",userDetails);
		 
		 authenticate(authenticationRequest.getUserName(), authenticationRequest.getPassword());
		 String token = jwtTokenUtil.generateToken(userDetails);
		 
	   logger.info("token is  received {}",token);
		 
		 return ResponseEntity.ok(new JwtResponse(token));
	}
	
	//it will authenticate user, by username and password receive from database and user 
	private void authenticate(String userName, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}
}