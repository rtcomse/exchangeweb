package com.myapp.exchangeweb.security.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.myapp.exchangeweb.models.Portfolio;
import com.myapp.exchangeweb.security.models.Account;
import com.myapp.exchangeweb.security.models.Authority;
import com.myapp.exchangeweb.security.models.Role;
import com.myapp.exchangeweb.security.repositories.AccountRepository;
import com.myapp.exchangeweb.services.PortfolioService;


@Service
public class AccountService {

	
	@Autowired
	private AccountRepository accountRepository ;
	
	@Autowired
	private PortfolioService portfolioService ;
	
	
	// LOGGER
	private static final Logger LOGGER = LoggerFactory.getLogger( AccountService.class ) ;

	
	public List<Account> findAll () {
		
		LOGGER.debug( "ENTER: findAll ()" ) ;
		LOGGER.debug( "EXIT: findAll ()" ) ;
		
		return this.accountRepository.findAll() ;
		
	}

	
	public List<Account> findById ( Integer id ) {
		
		LOGGER.debug( "ENTER: findById ()" ) ;
		LOGGER.debug( "id: {}", id ) ;
		
		List<Integer> id_list = new ArrayList<Integer>() ;
		id_list.add( id ) ;
		
		LOGGER.debug( "EXIT: findById ()" ) ;
		
		return this.accountRepository.findAllById( id_list ) ;
		
	}
	
	public List<Account> findByUsername ( String username ) {
		
		LOGGER.debug( "ENTER: findByUsername ()" ) ;
		LOGGER.debug( "username: {}", username ) ;
		LOGGER.debug( "EXIT: findByUsername ()" ) ;
		
		return this.accountRepository.findByUsername( username ) ;
		
	}

	public List<Account> findByAuthenticated () {
		
		LOGGER.debug( "ENTER: findByAuthenticated ()" ) ;
		
		List<Account> retAuthenticatedAccountList = new ArrayList<Account>() ;

		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication() ;
		
		if ( ( authentication != null ) && 
			 ( authentication.isAuthenticated() ) && 
			 ( authentication.getPrincipal() instanceof UserDetails ) ) {
			
			Object principal = authentication.getPrincipal() ;
			UserDetails userDetails = (UserDetails) principal ;
			
			retAuthenticatedAccountList = this.findByUsername( userDetails.getUsername() ) ;
			
		}
		
		
		LOGGER.debug( "EXIT: findByAuthenticated ()" ) ;
		
		return retAuthenticatedAccountList ;

		
	}


	
	public List<Role> listRoles ( Integer id ) {
		
		LOGGER.debug( "ENTER: listRoles ()" ) ;
		LOGGER.debug( "id: {}", id ) ;
				
		Account currAccount = this.findById( id ).get( 0 ) ;
		
		LOGGER.debug( "EXIT: listRoles ()" ) ;
		
		return currAccount.getRoles() ;
		
		
	}
	
	public List<Account> hasRole ( Integer roleId ) {
		
		LOGGER.debug( "ENTER: hasRole ()" ) ;
		LOGGER.debug( "roleId: {}", roleId ) ;
		LOGGER.debug( "EXIT: hasRole ()" ) ;
		
		return this.accountRepository.findByRolesId( roleId ) ;
		
		
	}

	
	
	
	
	public List<Authority> listAuthorities ( Integer id ) {
		
		LOGGER.debug( "ENTER: listAuthorities ()" ) ;
		LOGGER.debug( "id: {}", id ) ;
		
		Account currAccount = this.findById( id ).get( 0 ) ;
		
		
		LOGGER.debug( "EXIT: listAuthorities ()" ) ;
		
		return currAccount.getAuthorities() ;
		
		
	}
	
	public List<Account> hasAuthority ( Integer authorityId ) {
		
		LOGGER.debug( "ENTER: hasAuthority ()" ) ;
		LOGGER.debug( "authorityId: {}", authorityId ) ;
		LOGGER.debug( "EXIT: hasAuthority ()" ) ;
		
		return this.accountRepository.findByAuthoritiesId( authorityId ) ;
		
		
	}	

	
	
	
	public List<Portfolio> findByPortfoliosName ( Integer id, String portfolioName ) {
		
		LOGGER.debug( "ENTER: findByPortfoliosName ()" ) ;
		LOGGER.debug( "id: {}", id ) ;
		LOGGER.debug( "portfolioName: {}", portfolioName ) ;
		
		List<Portfolio> retPortfolioList = new ArrayList<Portfolio>() ;
		
		
		Account currAccount = this.findById( id ).get( 0 ) ;
		
		retPortfolioList.addAll( currAccount.getPortfolios() ) ;
				
		retPortfolioList.removeIf( ( portfolio ) -> !( portfolio.getName().equalsIgnoreCase( portfolioName ) ) ) ;
		
		Collections.sort( retPortfolioList, ( portfolio1, portfolio2 ) -> 
		                                      portfolio1.getName().compareToIgnoreCase( portfolio2.getName() ) ) ;
		
		
		LOGGER.debug( "EXIT: findByPortfoliosName ()" ) ;
		
		return retPortfolioList ;
		
		
	}

	public List<Portfolio> findByPortfoliosNameStartsWith ( Integer id, String portfolioName ) {
		
		LOGGER.debug( "ENTER: findByPortfoliosNameStartsWith ()" ) ;
		LOGGER.debug( "id: {}", id ) ;
		LOGGER.debug( "portfolioName: {}", portfolioName ) ;
		
		List<Portfolio> retPortfolioList = new ArrayList<Portfolio>() ;
		
		
		Account currAccount = this.findById( id ).get( 0 ) ;
		
		retPortfolioList.addAll( currAccount.getPortfolios() ) ;
				
		retPortfolioList.removeIf( ( portfolio ) -> !( portfolio.getName().regionMatches( true,
				                                                                          0,
				                                                                          portfolioName,
				                                                                          0,
				                                                                          portfolioName.length() ) ) ) ;
		
		Collections.sort( retPortfolioList, ( portfolio1, portfolio2 ) -> 
		                                      portfolio1.getName().compareToIgnoreCase( portfolio2.getName() ) ) ;
		

		LOGGER.debug( "EXIT: findByPortfoliosNameStartsWith ()" ) ;
		
		return retPortfolioList ;
		
		
	}
	
	
	
	public List<Account> addPortfolio ( Integer accountId, 
			                            Integer portfolioId ) {
		
		LOGGER.debug( "ENTER: addPortfolio ()" ) ;
		LOGGER.debug( "accountId: {}", accountId ) ;
		LOGGER.debug( "portfolioId: {}", portfolioId ) ;
		
		List<Account> updatedAccountList = new ArrayList<Account>() ;
		
		List<Integer> id_list = new ArrayList<Integer>() ;
		id_list.add( accountId ) ;
		
		List<Account> matchedAccountList =  this.accountRepository.findAllById( id_list ) ;
		List<Portfolio> matchedPortfolioList =  this.portfolioService.findById( portfolioId ) ;
		
		if ( matchedAccountList.size() != 1 || 
			 matchedPortfolioList.size() != 1 ) {
			
			LOGGER.error( "Could not find account with id: {}, or could not find portfolio with id: {}", 
					      accountId, 
					      portfolioId ) ;
			LOGGER.debug( "EXIT: addPortfolio ()" ) ;
				
			return updatedAccountList ;
				
		}		
		else {
			
			Account matchedAccount = matchedAccountList.getFirst() ;
			Portfolio matchedPortfolio = matchedPortfolioList.getFirst() ;
			
			matchedAccount.getPortfolios().add( matchedPortfolio ) ;
			matchedAccount = this.accountRepository.save( matchedAccount ) ;
			
			updatedAccountList.add( matchedAccount ) ;
				
		}	
		
		
		LOGGER.debug( "EXIT: addPortfolio ()" ) ;
		
		return updatedAccountList ;
		
	}

	
	public List<Account> removePortfolio ( Integer accountId, 
			                               List<Integer> portfolioIdList ) {
		
		LOGGER.debug( "ENTER: removePortfolio ()" ) ;
		LOGGER.debug( "accountId: {}", accountId ) ;
		LOGGER.debug( "portfolioIdList: {}", portfolioIdList ) ;
		
		List<Account> updatedAccountList = new ArrayList<Account>() ;
		
		List<Integer> id_list = new ArrayList<Integer>() ;
		id_list.add( accountId ) ;
		
		List<Account> matchedAccountList =  this.accountRepository.findAllById( id_list ) ;
		
		if ( matchedAccountList.size() != 1 ) {
			
			LOGGER.error( "Could not find account with id: {}", accountId ) ;
			LOGGER.debug( "EXIT: removePortfolio ()" ) ;
				
			return updatedAccountList ;
				
		}		
		else {
			
			Account matchedAccount = matchedAccountList.getFirst() ;
			
			
			Map<Integer, Integer> portfolioIdMap = new HashMap<Integer, Integer>() ;
			
			for ( int i = 0 ; i < portfolioIdList.size() ; i++ ) {
				
				portfolioIdMap.put( portfolioIdList.get( i ), portfolioIdList.get( i ) ) ;
				
			}
			
			List<Portfolio> matchedAccountPortfolios = matchedAccount.getPortfolios() ;
			List<Portfolio> removePortfolioList = new ArrayList<Portfolio>() ;
			
			for ( int i = 0 ; i < matchedAccountPortfolios.size() ; i++ ) {
				
				Portfolio currPortfolio = matchedAccountPortfolios.get( i ) ;
				
				if ( portfolioIdMap.containsKey( currPortfolio.getId() ) ) {
					
					matchedAccountPortfolios.remove( i ) ;	
					removePortfolioList.add( currPortfolio ) ;
					i-- ;
					
				}
				
			}

			
			matchedAccount = this.accountRepository.save( matchedAccount ) ;
			
			for ( int i = 0 ; i < removePortfolioList.size() ; i++ ) {
				
				Portfolio currPortfolio = removePortfolioList.get( i ) ;
				
				this.portfolioService.deletePortfolio( currPortfolio ) ;
				
			}
			
			
			updatedAccountList.add( matchedAccount ) ;
				
		}	
		
		
		LOGGER.debug( "EXIT: removePortfolio ()" ) ;
		
		return updatedAccountList ;
		
	}

	
	public List<Account> createAccount ( String inputUsername, 
			                             String inputPassword, 
			                             List<Role> inputRoleList, 
			                             List<Authority> inputAuthorityList, 
			                             List<Portfolio> inputPortfolioList ) {
		
		LOGGER.debug( "ENTER: createAccount ()" ) ;
		LOGGER.debug( "inputUsername: {}", inputUsername ) ;
		LOGGER.debug( "inputPassword: {}", inputPassword ) ;
		LOGGER.debug( "inputRoleList: {}", inputRoleList ) ;
		LOGGER.debug( "inputAuthorityList: {}", inputAuthorityList ) ;
		LOGGER.debug( "inputPortfolioList: {}", inputPortfolioList ) ;
		
		List<Account> createdAccountList = new ArrayList<Account>() ;
		
		
		if ( inputUsername == null || 
			 inputUsername.length() == 0 || 
			 this.findByUsername( inputUsername ).size() != 0 ) {
			
			LOGGER.error( "Problem with inputUsername: {}", inputUsername ) ;
			LOGGER.debug( "EXIT: createAccount ()" ) ;
			
			return createdAccountList ;
			
		}		
		if ( inputPassword == null || 
			 inputPassword.length() == 0 ) {
			
			LOGGER.error( "Problem with inputPassword: {}", inputPassword ) ;
			LOGGER.debug( "EXIT: createAccount ()" ) ;
				
			return createdAccountList ;
				
		}
		
		Account currAccount = new Account() ;
		
		currAccount.setId( null ) ;
		currAccount.setUsername( inputUsername ) ;
		currAccount.setPassword( inputPassword ) ;
		currAccount.setAccountNonExpired( true ) ;
		currAccount.setAccountNonLocked( true ) ;
		currAccount.setCredentialsNonExpired( true ) ;
		currAccount.setEnabled( true ) ;
		currAccount.setRoles( inputRoleList ) ;
		currAccount.setAuthorities( inputAuthorityList ) ;
		currAccount.setPortfolios( inputPortfolioList ) ;
		
		
		currAccount = this.accountRepository.save( currAccount ) ;
		
		createdAccountList.add( currAccount ) ;
		
		
		LOGGER.debug( "EXIT: createAccount ()" ) ;
		
		return createdAccountList ;
		
	}
		
	public Boolean deleteAccount ( Integer inputAccountId ) {
		
		LOGGER.debug( "ENTER: deleteAccount ()" ) ;
		LOGGER.debug( "inputAccountId: {}", inputAccountId ) ;
		
		Boolean retSuccessful = Boolean.valueOf( false ) ;
		
		List<Account> matchedAccountList = this.findById( inputAccountId ) ;
		
		if ( matchedAccountList.size() == 0 ) {
			
			LOGGER.error( "Could not find account with id: {}", inputAccountId ) ;
			LOGGER.debug( "EXIT: deleteAccount ()" ) ;
			
			return retSuccessful ;
			
		}
		
		Account matchedAccount = matchedAccountList.getFirst() ;
		List<Portfolio> matchedAccountPortfolioList = matchedAccount.getPortfolios() ;
		
		List<Portfolio> removePortfolioList = new ArrayList<Portfolio>() ;
		
		for ( int i = 0 ; i < matchedAccountPortfolioList.size() ; i++ ) {
			
			removePortfolioList.add( matchedAccountPortfolioList.get( i ) ) ;
			
		}
		
		matchedAccount.getRoles().clear() ;
		matchedAccount.getAuthorities().clear() ;		
		matchedAccount.getPortfolios().clear() ;
		
		matchedAccount = this.accountRepository.save( matchedAccount ) ;
		
		this.accountRepository.delete( matchedAccount ) ;
		
		for ( int i = 0 ; i < removePortfolioList.size() ; i++ ) {
			
			this.portfolioService.deletePortfolio( removePortfolioList.get( i ) ) ;
			
		}
		
		
		retSuccessful = Boolean.valueOf( true ) ;
		
		
		LOGGER.debug( "EXIT: deleteAccount ()" ) ;
		
		return retSuccessful ;

	}

	
	

	
	
}
