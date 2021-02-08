package com.banti.wallet.ums.jwtSpringSecutity.services;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.banti.wallet.ums.elasticsearch.models.ElasticPerson;
import com.banti.wallet.ums.service.PersonService;


@Service
public class JwtUserDetailsService implements UserDetailsService {
	
	Logger logger = LoggerFactory.getLogger(JwtUserDetailsService.class);
	@Autowired 
	private PersonService userService;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		
	ElasticPerson user = userService.findUserByUserName(username);
		//String pass = bcryptEncoder.encode(user.getPassword());
	
	  logger.info("user received from database {}", user);
		
		if (user.getUserName().equals(username)) {
		
			return new  User(user.getUserName(), user.getPassword(),new ArrayList<>());
		} else {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
	}
}
	
	

