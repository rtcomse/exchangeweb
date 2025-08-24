package com.myapp.exchangeweb.security.services;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myapp.exchangeweb.security.models.Authority;
import com.myapp.exchangeweb.security.models.Role;
import com.myapp.exchangeweb.security.repositories.RoleRepository;


@Service
public class RoleService {

	
	@Autowired
	private RoleRepository roleRepository ;
	
	
	// LOGGER
	private static final Logger LOGGER = LoggerFactory.getLogger( RoleService.class ) ;
	
	
	public List<Role> findAll () {
		
		LOGGER.debug( "ENTER: findAll ()" ) ;
		LOGGER.debug( "EXIT: findAll ()" ) ;
		
		return this.roleRepository.findAll() ;
		
	}

	
	public List<Role> findById ( Integer id ) {
		
		LOGGER.debug( "ENTER: findById ()" ) ;
		LOGGER.debug( "id: {}", id ) ;
		
		List<Integer> id_list = new ArrayList<Integer>() ;
		id_list.add( id ) ;
		
		
		LOGGER.debug( "EXIT: findById ()" ) ;
		
		return this.roleRepository.findAllById( id_list ) ;
		
	}

	
	public List<Authority> listAuthorities ( Integer id ) {
		
		LOGGER.debug( "ENTER: listAuthorities ()" ) ;
		LOGGER.debug( "id: {}", id ) ;
		
		
		Role currRole = this.findById( id ).get( 0 ) ;
		
		
		LOGGER.debug( "EXIT: listAuthorities ()" ) ;
		
		return currRole.getAuthorities() ;
		
		
	}
	
	public List<Role> hasAuthority ( Integer authorityId ) {
		
		LOGGER.debug( "ENTER: hasAuthority ()" ) ;
		LOGGER.debug( "authorityId: {}", authorityId ) ;
		LOGGER.debug( "EXIT: hasAuthority ()" ) ;
		
		return this.roleRepository.findByAuthoritiesId( authorityId ) ;
		
		
	}

	
}
