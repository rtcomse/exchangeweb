package com.myapp.exchangeweb.security.config.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.myapp.exchangeweb.security.config.models.UserDetailsAccount;
import com.myapp.exchangeweb.security.models.Account;
import com.myapp.exchangeweb.security.services.AccountService;



public class UserDetailsServiceAccount implements UserDetailsService {
	
	
	@Autowired
	private AccountService accountService ;
	
	// LOGGER
	private static final Logger LOGGER = LoggerFactory.getLogger( UserDetailsServiceAccount.class ) ;


	@Override
	public UserDetails loadUserByUsername ( String username ) throws UsernameNotFoundException {
		
		LOGGER.debug( "ENTER: loadUserByUsername ()" ) ;
		LOGGER.debug( "username: {}", username ) ;
	
		UserDetails retUserDetails = null ;
		
		List<Account> matchedAccountList = this.accountService.findByUsername( username ) ;

		
		if ( matchedAccountList.size() == 0 ) {
			
			LOGGER.error( "Could not find account with username: {}", username ) ;
			LOGGER.debug( "EXIT: loadUserByUsername ()" ) ;
				
			throw new UsernameNotFoundException( "No Account with username: " + username ) ;
			
		}

		
		Account matchedAccount = matchedAccountList.getFirst() ;
		
		retUserDetails = new UserDetailsAccount( matchedAccount )  ;

		
		LOGGER.debug( "EXIT: loadUserByUsername ()" ) ;
		
		return retUserDetails ;
		
	}
	
	

}
