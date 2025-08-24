package com.myapp.exchangeweb.security.services;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myapp.exchangeweb.security.models.Authority;
import com.myapp.exchangeweb.security.repositories.AuthorityRepository;


@Service
public class AuthorityService {
	
	@Autowired
	private AuthorityRepository authorityRepository ;
	
	
	// LOGGER
	private static final Logger LOGGER = LoggerFactory.getLogger( AuthorityService.class ) ;
	
	
	public List<Authority> findAll () {
		
		LOGGER.debug( "ENTER: findAll ()" ) ;
		LOGGER.debug( "EXIT: findAll ()" ) ;
		
		return this.authorityRepository.findAll() ;
		
	}
	
	
	public List<Authority> findById ( Integer id ) {
		
		LOGGER.debug( "ENTER: findById ()" ) ;
		LOGGER.debug( "id: {}", id ) ;
		
		List<Integer> id_list = new ArrayList<Integer>() ;
		id_list.add( id ) ;
		
		
		LOGGER.debug( "EXIT: findById ()" ) ;
		
		return this.authorityRepository.findAllById( id_list ) ;
		
	}


}
