package com.myapp.exchangeweb.security.controllers;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.myapp.exchangeweb.models.Portfolio;
import com.myapp.exchangeweb.project.ProjectData;
import com.myapp.exchangeweb.security.models.Account;
import com.myapp.exchangeweb.security.models.Authority;
import com.myapp.exchangeweb.security.models.Role;
import com.myapp.exchangeweb.security.services.AccountService;
import com.myapp.exchangeweb.security.services.RoleService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@RestController
public class AccountController {

	
	@Autowired
	private AccountService accountService ;
	@Autowired
	private RoleService roleService ;
	@Autowired
	private PasswordEncoder passwordEncoder ;
	
	
	// LOGGER
	private static final Logger LOGGER = LoggerFactory.getLogger( AccountController.class ) ;

	
	@GetMapping( "/accounts" )
	public List<Account> findAll () {
		
		LOGGER.debug( "ENTER: findAll ()" ) ;
		
		List<Account> retAccountList = new ArrayList<Account>() ;
		
		
		retAccountList = this.accountService.findAll() ;
		
		
		LOGGER.debug( "EXIT: findAll ()" ) ;
		
		return retAccountList ;
		
	}
	
	@GetMapping( "/accounts/{accountId}" )
	public List<Account> findById ( @PathVariable Integer accountId ) {
		
		LOGGER.debug( "ENTER: findById ()" ) ;
		LOGGER.debug( "accountId: {}", accountId ) ;
		
		List<Account> retAccountList = new ArrayList<Account>() ;
		
		retAccountList =  this.accountService.findById( accountId ) ;
		
		
		LOGGER.debug( "EXIT: findById ()" ) ;
		
		return retAccountList ;
		
	}

	
	
	@GetMapping( "/accounts/{accountId}/list_roles" )
	public List<Role> listRoles ( @PathVariable Integer accountId ) {
		
		LOGGER.debug( "ENTER: listRoles ()" ) ;
		LOGGER.debug( "accountId: {}", accountId ) ;
		
		List<Role> retRoleList = new ArrayList<Role>() ;
		
		retRoleList = this.accountService.listRoles( accountId ) ;
		
		
		LOGGER.debug( "EXIT: listRoles ()" ) ;
		
		return retRoleList ;
		
	}
	
	@GetMapping( "/accounts/has_role/{roleId}" )
	public List<Account> hasRole ( @PathVariable Integer roleId ) {
		
		LOGGER.debug( "ENTER: hasRole ()" ) ;
		LOGGER.debug( "roleId: {}", roleId ) ;
		
		List<Account> retAccountList = new ArrayList<Account>() ;
		
		retAccountList =  this.accountService.hasRole( roleId ) ;
		
		
		LOGGER.debug( "EXIT: hasRole ()" ) ;
		
		return retAccountList ;
		
	}

	
	
	@GetMapping( "/accounts/{accountId}/list_authorities" )
	public List<Authority> listAuthorities ( @PathVariable Integer accountId ) {
		
		LOGGER.debug( "ENTER: listAuthorities ()" ) ;
		LOGGER.debug( "accountId: {}", accountId ) ;
		
		List<Authority> retAuthorityList = new ArrayList<Authority>() ;
		
		retAuthorityList = this.accountService.listAuthorities( accountId ) ;
		
		
		LOGGER.debug( "EXIT: listAuthorities ()" ) ;
		
		return retAuthorityList ;
		
	}
	
	@GetMapping( "/accounts/has_authority/{authorityId}" )
	public List<Account> hasAuthority ( @PathVariable Integer authorityId ) {
		
		LOGGER.debug( "ENTER: hasAuthority ()" ) ;
		LOGGER.debug( "authorityId: {}", authorityId ) ;
		
		List<Account> retAccountList = new ArrayList<Account>() ;
		
		retAccountList =  this.accountService.hasAuthority( authorityId ) ;
		
		
		LOGGER.debug( "EXIT: hasAuthority ()" ) ;
		
		return retAccountList ;
		
	}
	
	
	@GetMapping( "/accounts/{accountId}/search/portfolios/name/{portfolioName}" )
	public List<Portfolio> findByPortfoliosName ( @PathVariable Integer accountId, @PathVariable String portfolioName ) {
		
		LOGGER.debug( "ENTER: findByPortfoliosName ()" ) ;
		LOGGER.debug( "accountId: {}", accountId ) ;
		LOGGER.debug( "portfolioName: {}", portfolioName ) ;
		
		List<Portfolio> retPortfolioList = new ArrayList<Portfolio>() ;
		
		retPortfolioList = this.accountService.findByPortfoliosName( accountId, portfolioName ) ;
		
		
		LOGGER.debug( "EXIT: findByPortfoliosName ()" ) ;
		
		return retPortfolioList ;
		
	}
	
	@GetMapping( "/accounts/{accountId}/search/portfolios/name/starts_with/{portfolioName}" )
	public List<Portfolio> findByPortfoliosNameStartsWith ( @PathVariable Integer accountId, @PathVariable String portfolioName ) {
		
		LOGGER.debug( "ENTER: findByPortfoliosNameStartsWith ()" ) ;
		LOGGER.debug( "accountId: {}", accountId ) ;
		LOGGER.debug( "portfolioName: {}", portfolioName ) ;
		
		List<Portfolio> retPortfolioList = new ArrayList<Portfolio>() ;
		
		retPortfolioList = this.accountService.findByPortfoliosNameStartsWith( accountId, portfolioName ) ;
		
		
		LOGGER.debug( "EXIT: findByPortfoliosNameStartsWith ()" ) ;
		
		return retPortfolioList ;
		
	}
	
	
	@PostMapping( "/accounts/create_account" )
	public ModelAndView createAccount ( @RequestParam( "username" ) String inputUsername, 
			                            @RequestParam( "password" ) String inputPassword ) {
		
		LOGGER.debug( "ENTER: createAccount ()" ) ;
		LOGGER.debug( "inputUsername: {}", inputUsername ) ;
		LOGGER.debug( "inputPassword: {}", inputPassword ) ;
		
		inputPassword = this.passwordEncoder.encode( inputPassword ) ;
		LOGGER.debug( "encoded inputPassword: {}", inputPassword ) ;
		
		ModelAndView retModelAndView = new ModelAndView( "forward:" + 
		                                                 ( new ProjectData() ).getUrlPrefixServer1() +
		                                                 "/login" ) ;	
		
		List<Role> matchedRoleList = new ArrayList<Role>() ;
		List<Authority> matchedAuthorityList = new ArrayList<Authority>() ;
		List<Portfolio> matchedPortfolioList = new ArrayList<Portfolio>() ;
		
		
		List<Role> roleList = this.roleService.findAll() ;
		
		for ( int i = 0 ; i < roleList.size() ; i++ ) {
			
			if ( roleList.get( i ).getName().equals( "ROLE_USER" ) ) {
				
				matchedRoleList.add( roleList.get( i ) ) ;
				
			}
			
		}
		
		
		this.accountService.createAccount( inputUsername, 
				                           inputPassword, 
				                           matchedRoleList, 
				                           matchedAuthorityList, 
				                           matchedPortfolioList ) ;
		
		
		LOGGER.debug( "EXIT: createAccount ()" ) ;
		
		return retModelAndView ;
		
	}

	@GetMapping( "/accounts/delete_account/{accountId}" )
	public Boolean deleteAccount ( @PathVariable Integer accountId ) {
		
		LOGGER.debug( "ENTER: deleteAccount ()" ) ;
		LOGGER.debug( "accountId: {}", accountId ) ;
		
		Boolean retStatus = Boolean.valueOf( false ) ;
		
		retStatus =  this.accountService.deleteAccount( accountId ) ;
		
		
		LOGGER.debug( "EXIT: deleteAccount ()" ) ;
		
		return retStatus ;
		
	}
	
	@PostMapping( "/accounts/delete_account/current_user" )
	public ModelAndView deleteAccountCurrentUser ( HttpServletRequest request, HttpServletResponse response ) {
		
		LOGGER.debug( "ENTER: deleteAccountCurrentUser ()" ) ;
		LOGGER.debug( "request: {}", request ) ;
		LOGGER.debug( "response: {}", response ) ;
		
		ModelAndView retModelAndView = new ModelAndView( "redirect:/home_page" ) ;
		
		
		List<Account> accountList = new ArrayList<Account>() ;
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication() ;
		
		if ( ( authentication != null ) && 
			 ( authentication.isAuthenticated() ) && 
			 ( authentication.getPrincipal() instanceof UserDetails ) ) {
			
			Object principal = authentication.getPrincipal() ;
			UserDetails userDetails = (UserDetails) principal ;
			
			accountList = this.accountService.findByUsername( userDetails.getUsername() ) ;
			
		}
		
		if ( accountList.size() != 0 ) {
			
			Account deleteAccount = accountList.getFirst() ;
			
			SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler() ;
			securityContextLogoutHandler.logout(request, response, authentication) ;
			
			this.accountService.deleteAccount( deleteAccount.getId() ) ;
			
		}
		
		
		LOGGER.debug( "EXIT: deleteAccountCurrentUser ()" ) ;
		
		return retModelAndView ;
		
	}
	


	
}
