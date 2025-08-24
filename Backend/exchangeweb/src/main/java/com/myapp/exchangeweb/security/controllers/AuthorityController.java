package com.myapp.exchangeweb.security.controllers;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.myapp.exchangeweb.security.models.Authority;
import com.myapp.exchangeweb.security.services.AuthorityService;

@RestController
public class AuthorityController {
	
	@Autowired
	private AuthorityService authorityService ;
	
	
	// LOGGER
	private static final Logger LOGGER = LoggerFactory.getLogger( AuthorityController.class ) ;
		
	
	@GetMapping( "/authorities" )
	public List<Authority> findAll () {
		
		LOGGER.debug( "ENTER: findAll ()" ) ;
		
		List<Authority> retAuthorityList = new ArrayList<Authority>() ;
		
		
		retAuthorityList = this.authorityService.findAll() ;
		
		
		LOGGER.debug( "EXIT: findAll ()" ) ;
		
		return retAuthorityList ;
		
	}
	
	@GetMapping( "/authorities/{authorityId}" )
	public List<Authority> findById ( @PathVariable Integer authorityId ) {
		
		LOGGER.debug( "ENTER: findById ()" ) ;
		LOGGER.debug( "authorityId: {}", authorityId ) ;
		
		List<Authority> retAuthorityList = new ArrayList<Authority>() ;
		
		retAuthorityList =  this.authorityService.findById( authorityId ) ;
		
		
		LOGGER.debug( "EXIT: findById ()" ) ;
		
		return retAuthorityList ;
		
	}

}
