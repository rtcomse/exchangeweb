package com.myapp.exchangeweb.security.config.controllers;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class UserDetailsAccountController {
	
	// LOGGER
	private static final Logger LOGGER = LoggerFactory.getLogger( UserDetailsAccountController.class ) ;
	
	
	@GetMapping( "/user_details/current_user/username" )
	public List<String> findCurrentUserUsername () {
		
		LOGGER.debug( "ENTER: findCurrentUserUsername ()" ) ;
		
		List<String> retUsernameList = new ArrayList<String>() ;
		
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication() ;
		
		if ( ( authentication == null ) || !( authentication.isAuthenticated() ) ) {
			
			LOGGER.error( "Problem with authentication: {}", authentication );
			LOGGER.debug( "EXIT: findCurrentUserUsername ()" ) ;
			
			return retUsernameList ;
			
		}
		
		Object principal = authentication.getPrincipal() ;
		
		if ( !( principal instanceof UserDetails ) ) {
			
			LOGGER.error( "Problem with principal: {}", principal );
			LOGGER.debug( "EXIT: findCurrentUserUsername ()" ) ;
			
			return retUsernameList ;
			
		}
		
		UserDetails userDetails = (UserDetails) principal ;
		
		retUsernameList.add( userDetails.getUsername() ) ;
		
		
		LOGGER.debug( "EXIT: findCurrentUserUsername ()" ) ;
		
		return retUsernameList ;
		
	}
	
	
	

}
