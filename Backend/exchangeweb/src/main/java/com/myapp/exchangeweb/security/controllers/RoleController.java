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
import com.myapp.exchangeweb.security.models.Role;
import com.myapp.exchangeweb.security.services.RoleService;


@RestController
public class RoleController {

	
	@Autowired
	private RoleService roleService ;
	
	
	// LOGGER
	private static final Logger LOGGER = LoggerFactory.getLogger( RoleController.class ) ;

	
	@GetMapping( "/roles" )
	public List<Role> findAll () {
		
		LOGGER.debug( "ENTER: findAll ()" ) ;
		
		List<Role> retRoleList = new ArrayList<Role>() ;
		
		
		retRoleList =  this.roleService.findAll() ;
		
		
		LOGGER.debug( "EXIT: findAll ()" ) ;
		
		return retRoleList ;
		
	}
	
	
	@GetMapping( "/roles/{roleId}" )
	public List<Role> findById ( @PathVariable Integer roleId ) {
		
		LOGGER.debug( "ENTER: findById ()" ) ;
		LOGGER.debug( "roleId: {}", roleId ) ;
		
		List<Role> retRoleList = new ArrayList<Role>() ;
		
		retRoleList =  this.roleService.findById( roleId ) ;
		
		
		LOGGER.debug( "EXIT: findById ()" ) ;
		
		return retRoleList ;
		
	}

	
	@GetMapping( "/roles/{roleId}/list_authorities" )
	public List<Authority> listAuthorities ( @PathVariable Integer roleId ) {
		
		LOGGER.debug( "ENTER: listAuthorities ()" ) ;
		LOGGER.debug( "roleId: {}", roleId ) ;
		
		List<Authority> retAuthorityList = new ArrayList<Authority>() ;
		
		retAuthorityList =  this.roleService.listAuthorities( roleId ) ;
		
		
		LOGGER.debug( "EXIT: listAuthorities ()" ) ;
		
		return retAuthorityList ;
		
	}
	
	@GetMapping( "/roles/has_authority/{authorityId}" )
	public List<Role> hasAuthority ( @PathVariable Integer authorityId ) {
		
		LOGGER.debug( "ENTER: hasAuthority ()" ) ;
		LOGGER.debug( "authorityId: {}", authorityId ) ;
		
		List<Role> retRoleList = new ArrayList<Role>() ;
		
		retRoleList =  this.roleService.hasAuthority( authorityId ) ;
		
		
		LOGGER.debug( "EXIT: hasAuthority ()" ) ;
		
		return retRoleList ;
		
	}

	
}
