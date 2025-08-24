package com.myapp.exchangeweb.page.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.myapp.exchangeweb.project.ProjectData;
import com.myapp.exchangeweb.security.models.Account;
import com.myapp.exchangeweb.security.models.Role;
import com.myapp.exchangeweb.security.services.AccountService;
import com.myapp.exchangeweb.security.services.RoleService;


@RestController
public class PortfolioListingPageController {

	
	@Autowired
	private AccountService accountService ;
	@Autowired
	private RoleService roleService ;
	
	// LOGGER
	private static final Logger LOGGER = LoggerFactory.getLogger( PortfolioListingPageController.class ) ;

	
	
	@GetMapping( "/portfolio_listing_page" )
	public ModelAndView portfolioListingPage () {
		
		LOGGER.debug( "ENTER: portfolioListingPage ()" ) ;
		
		ModelAndView retModelAndView = new ModelAndView( "pages/portfolio_listing_page/portfolio_listing_page.html" ) ;
		
		
		final Integer GUEST_ACCOUNT_ID = Integer.valueOf( 1 ) ;
				
		List<Account> accountList = this.accountService.findById( GUEST_ACCOUNT_ID ) ;
		
		
		List<Role> guestRoleList = this.roleService.findAll() ;
		
		for ( int i = 0 ; i < guestRoleList.size() ; i++ ) {
			
			if ( !( guestRoleList.get( i ).getName().equals( "ROLE_GUEST" ) ) ) {
				
				guestRoleList.remove( i ) ;
				i-- ;
				
			}
			
		}
		
		if ( guestRoleList.size() != 0 ) {
			
			Role currGuestRole = guestRoleList.getFirst() ;
			
			accountList = this.accountService.hasRole( currGuestRole.getId() ) ;
			
		}
		
				
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication() ;
		
		if ( ( authentication != null ) && 
			 ( authentication.isAuthenticated() ) && 
			 ( authentication.getPrincipal() instanceof UserDetails ) ) {
			
			Object principal = authentication.getPrincipal() ;
			UserDetails userDetails = (UserDetails) principal ;
			
			accountList = this.accountService.findByUsername( userDetails.getUsername() ) ;
			
		}

		
		Map<String, Object> modelMap = new HashMap<>() ;
		
		modelMap.put( "projectData", ( new ProjectData() ) ) ;
		modelMap.put( "accountList", accountList ) ;
		
		retModelAndView.addAllObjects( modelMap ) ;
		
		
		LOGGER.debug( "EXIT: portfolioListingPage ()" ) ;
		
		return retModelAndView ;
		
	}
	
	
}
