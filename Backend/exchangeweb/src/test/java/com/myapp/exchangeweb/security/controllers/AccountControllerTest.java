package com.myapp.exchangeweb.security.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myapp.exchangeweb.models.Portfolio;
import com.myapp.exchangeweb.security.models.Account;
import com.myapp.exchangeweb.security.models.Authority;
import com.myapp.exchangeweb.security.models.Role;
import com.myapp.exchangeweb.security.services.AccountService;
import com.myapp.exchangeweb.security.services.RoleService;


@WebMvcTest( AccountController.class )
@WithMockUser
class AccountControllerTest {
	
		
	@Autowired
	private MockMvc mockMvc ;
	
	@MockBean
	private AccountService accountService ;
	@MockBean
	private RoleService roleService ;
	@MockBean
	private PasswordEncoder passwordEncoder ;

	

	@BeforeEach
	void beforeEachTest () {
		
		Role roleUser = new Role() ;
		roleUser.setId( Integer.valueOf( 1 ) ) ;
		roleUser.setName( "ROLE_USER" ) ;
		
		Role roleAdmin = new Role() ;
		roleAdmin.setId( Integer.valueOf( 2 ) ) ;
		roleAdmin.setName( "ROLE_ADMIN" ) ;
		
		Authority authorityRead = new Authority() ;
		authorityRead.setId( Integer.valueOf( 3 ) ) ;
		authorityRead.setName( "AUTHORITY_READ" ) ;
		
		Authority authorityWrite = new Authority() ;
		authorityWrite.setId( Integer.valueOf( 4 ) ) ;
		authorityWrite.setName( "AUTHORITY_WRITE" ) ;
		
		Portfolio portfolio1 = new Portfolio() ;
		portfolio1.setId( Integer.valueOf( 5 ) );
		portfolio1.setName( "portfolio1" ) ;
		
		Portfolio portfolio2 = new Portfolio() ;
		portfolio2.setId( Integer.valueOf( 6 ) );
		portfolio2.setName( "portfolio2" ) ;
		
		Portfolio portfolio3 = new Portfolio() ;
		portfolio3.setId( Integer.valueOf( 7 ) );
		portfolio3.setName( "portfolio3" ) ;
		
		
		Account account1 = new Account() ;
		account1.setId( Integer.valueOf( 1 ) ) ;
		account1.setUsername( "account1" ) ;
		
		List<Account> account1List = new ArrayList<Account>() ;
		account1List.add( account1 ) ;
			
		List<Role> account1rolesList = new ArrayList<Role>() ;
		account1rolesList.add( roleUser ) ;
		
		List<Authority> account1authoritiesList = new ArrayList<Authority>() ;
		account1authoritiesList.add( authorityRead ) ;
		
		List<Portfolio> account1portfoliosList = new ArrayList<Portfolio>() ;
		account1portfoliosList.add( portfolio1 ) ;
		account1portfoliosList.add( portfolio3 ) ;
	
		account1.setRoles( account1rolesList ) ;
		account1.setAuthorities( account1authoritiesList ) ;
		account1.setPortfolios( account1portfoliosList ) ;

				
		Account account2 = new Account() ;
		account2.setId( Integer.valueOf( 2 ) ) ;
		account2.setUsername( "account2" ) ;
		
		List<Account> account2List = new ArrayList<Account>() ;
		account2List.add( account2 ) ;
				
		List<Role> account2rolesList = new ArrayList<Role>() ;
		account2rolesList.add( roleAdmin ) ;
		
		List<Authority> account2authoritiesList = new ArrayList<Authority>() ;
		account2authoritiesList.add( authorityWrite ) ;
		
		List<Portfolio> account2portfoliosList = new ArrayList<Portfolio>() ;
		account2portfoliosList.add( portfolio2 ) ;
		
		account2.setRoles( account2rolesList ) ;
		account2.setAuthorities( account2authoritiesList ) ;
		account2.setPortfolios( account2portfoliosList ) ;
		
				
		List<Account> accountList = new ArrayList<Account>() ;
		accountList.add( account1 ) ;
		accountList.add( account2 ) ;
		
		List<Portfolio> portfolioListSymbol = new ArrayList<Portfolio>() ;
		portfolioListSymbol.add( portfolio2 ) ;
		
		List<Portfolio> portfolioListSymbolStartsWith = new ArrayList<Portfolio>() ;
		portfolioListSymbolStartsWith.add( portfolio1 ) ;
		portfolioListSymbolStartsWith.add( portfolio3 ) ;
		
		
		when( this.accountService.findAll() ).thenReturn( accountList ) ;
		when( this.accountService.findById( account1.getId() ) ).thenReturn( account1List ) ;
		when( this.accountService.findById( account2.getId() ) ).thenReturn( account2List ) ;
		
		when( this.accountService.findByPortfoliosName( account2.getId() , portfolio2.getName() ) )
		.thenReturn( portfolioListSymbol ) ;
		when( this.accountService.findByPortfoliosNameStartsWith( account1.getId(), "port" ) )
		.thenReturn( portfolioListSymbolStartsWith ) ;
		
		when( this.accountService.listRoles( account1.getId() ) )
		.thenReturn( account1.getRoles() ) ;		
		when( this.accountService.hasRole( roleUser.getId() ) )
		.thenReturn( account1List ) ;
		
		when( this.accountService.listAuthorities( account1.getId() ) )
		.thenReturn( account1.getAuthorities() ) ;		
		when( this.accountService.hasAuthority( authorityRead.getId() ) )
		.thenReturn( account1List ) ;
		
		
	}

	
	
	@Test
	void findAllShouldReturnAllAccounts() throws Exception {
		
		MvcResult mvcResult = this.mockMvc.perform( get( "/accounts" ) )
				                          .andDo( print() )
		                                  .andExpect( status().isOk() )
		                                  .andReturn() ;
		
		String responseJson = mvcResult.getResponse().getContentAsString() ;
		
		ObjectMapper objectMapper = new ObjectMapper() ;
		
		List<Account> responseAccountList = objectMapper.readValue(responseJson, new TypeReference<List<Account>>(){} ) ;

		
		verify( this.accountService, atLeastOnce() ).findAll() ;
		
		
		assertNotNull( responseAccountList, "responseAccountList is Null." ) ;
		
		assertTrue( responseAccountList.size() == 2, 
				    "responseAccountList.size() is " + 
				    responseAccountList.size() + 
		            " (Not 2)." ) ;
		
		assertTrue( responseAccountList.get( 0 ).getUsername().equals("account1"), 
				    "responseAccountList.get( 0 ) is " + 
				    responseAccountList.get( 0 ).getUsername() + 
				    " (not account1)." ) ;
		assertTrue( responseAccountList.get( 1 ).getUsername().equals("account2"), 
				    "responseAccountList.get( 1 ) is " + 
				    responseAccountList.get( 1 ).getUsername() + 
				    " (not account2)." ) ;

		
		
	}
	
	
	
	@Test
	void findByIdShouldReturnAllAccountsHavingProvidedId() throws Exception {
		
		final Integer PROVIDED_ACCOUNT_ID = Integer.valueOf( 1 ) ;
		
		MvcResult mvcResult = this.mockMvc.perform( get( "/accounts/" + PROVIDED_ACCOUNT_ID ) )
				                          .andDo( print() )
		                                  .andExpect( status().isOk() )
		                                  .andReturn() ;
		
		String responseJson = mvcResult.getResponse().getContentAsString() ;
		
		ObjectMapper objectMapper = new ObjectMapper() ;
		
		List<Account> responseAccountList = objectMapper.readValue(responseJson, new TypeReference<List<Account>>(){} ) ;

		
		verify( this.accountService, atLeastOnce() ).findById( PROVIDED_ACCOUNT_ID ) ;
		
		
		assertNotNull( responseAccountList, "responseAccountList is Null." ) ;
		
		assertTrue( responseAccountList.size() == 1, 
				    "responseAccountList.size() is " + 
				    responseAccountList.size() + 
		            " (Not 1)." ) ;
		assertTrue( responseAccountList.get( 0 ).getUsername().equals("account1"), 
				    "responseAccountList.get( 0 ) is " + 
				    responseAccountList.get( 0 ).getUsername() + 
				    " (not account1)." ) ;
		
		
	}


	
	@Test
	void listRolesShouldReturnAllRolesForProvidedAccountId() throws Exception {
		
		final Integer PROVIDED_ACCOUNT_ID = Integer.valueOf( 1 ) ;
		
		MvcResult mvcResult = this.mockMvc.perform( get( "/accounts/" + 
				                                         PROVIDED_ACCOUNT_ID + 
				                                         "/list_roles" ) )
				                          .andDo( print() )
		                                  .andExpect( status().isOk() )
		                                  .andReturn() ;
		
		String responseJson = mvcResult.getResponse().getContentAsString() ;
		
		ObjectMapper objectMapper = new ObjectMapper() ;
		
		List<Role> responseRoleList = objectMapper.readValue(responseJson, new TypeReference<List<Role>>(){} ) ;

		
		verify( this.accountService, atLeastOnce() ).listRoles( PROVIDED_ACCOUNT_ID ) ;
		
		
		assertNotNull( responseRoleList, "responseRoleList is Null." ) ;
		
		assertTrue( responseRoleList.size() == 1, 
				    "responseRoleList.size() is " + 
				    responseRoleList.size() + 
		            " (Not 1)." ) ;
		
		assertTrue( responseRoleList.get( 0 ).getName().equals("ROLE_USER"), 
				    "responseRoleList.get( 0 ) is " + 
				    responseRoleList.get( 0 ).getName() + 
				    " (not ROLE_USER)." ) ;
		
		
	}
	
	
	
	@Test
	void hasRoleShouldReturnAllAccountsHavingProvidedRoleId() throws Exception {
		
		final Integer PROVIDED_ROLE_ID = Integer.valueOf( 1 ) ;
		
		MvcResult mvcResult = this.mockMvc.perform( get( "/accounts/has_role/" + PROVIDED_ROLE_ID ) )
				                          .andDo( print() )
		                                  .andExpect( status().isOk() )
		                                  .andReturn() ;
		
		String responseJson = mvcResult.getResponse().getContentAsString() ;
		
		ObjectMapper objectMapper = new ObjectMapper() ;
		
		List<Account> responseAccountList = objectMapper.readValue(responseJson, new TypeReference<List<Account>>(){} ) ;

		
		verify( this.accountService, atLeastOnce() ).hasRole( PROVIDED_ROLE_ID ) ;
		
		
		assertNotNull( responseAccountList, "responseAccountList is Null." ) ;
		
		assertTrue( responseAccountList.size() == 1, 
				    "responseAccountList.size() is " + 
				    responseAccountList.size() + 
		            " (Not 1)." ) ;
		assertTrue( responseAccountList.get( 0 ).getUsername().equals("account1"), 
				    "responseAccountList.get( 0 ) is " + 
				    responseAccountList.get( 0 ).getUsername() + 
				    " (not account1)." ) ;
		
		
	}

	
	
	@Test
	void listAuthoritiesShouldReturnAllAuthoritiesForProvidedAccountId() throws Exception {
		
		final Integer PROVIDED_ACCOUNT_ID = Integer.valueOf( 1 ) ;
		
		MvcResult mvcResult = this.mockMvc.perform( get( "/accounts/" + 
				                                         PROVIDED_ACCOUNT_ID + 
				                                         "/list_authorities" ) )
				                          .andDo( print() )
		                                  .andExpect( status().isOk() )
		                                  .andReturn() ;
		
		String responseJson = mvcResult.getResponse().getContentAsString() ;
		
		ObjectMapper objectMapper = new ObjectMapper() ;
		
		List<Authority> responseAuthorityList = objectMapper.readValue(responseJson, new TypeReference<List<Authority>>(){} ) ;

		
		verify( this.accountService, atLeastOnce() ).listAuthorities( PROVIDED_ACCOUNT_ID ) ;
		
		
		assertNotNull( responseAuthorityList, "responseAuthorityList is Null." ) ;
		
		assertTrue( responseAuthorityList.size() == 1, 
				    "responseAuthorityList.size() is " + 
				    responseAuthorityList.size() + 
		            " (Not 1)." ) ;
		
		assertTrue( responseAuthorityList.get( 0 ).getName().equals("AUTHORITY_READ"), 
				    "responseAuthorityList.get( 0 ) is " + 
				    responseAuthorityList.get( 0 ).getName() + 
				    " (not AUTHORITY_READ)." ) ;
		
		
	}
	
	
	
	@Test
	void hasAuthorityShouldReturnAllAccountsHavingProvidedAuthorityId() throws Exception {
		
		final Integer PROVIDED_AUTHORITY_ID = Integer.valueOf( 3 ) ;
		
		MvcResult mvcResult = this.mockMvc.perform( get( "/accounts/has_authority/" + PROVIDED_AUTHORITY_ID ) )
				                          .andDo( print() )
		                                  .andExpect( status().isOk() )
		                                  .andReturn() ;
		
		String responseJson = mvcResult.getResponse().getContentAsString() ;
		
		ObjectMapper objectMapper = new ObjectMapper() ;
		
		List<Account> responseAccountList = objectMapper.readValue(responseJson, new TypeReference<List<Account>>(){} ) ;

		
		verify( this.accountService, atLeastOnce() ).hasAuthority( PROVIDED_AUTHORITY_ID ) ;
		
		
		assertNotNull( responseAccountList, "responseAccountList is Null." ) ;
		
		assertTrue( responseAccountList.size() == 1, 
				    "responseAccountList.size() is " + 
				    responseAccountList.size() + 
		            " (Not 1)." ) ;
		assertTrue( responseAccountList.get( 0 ).getUsername().equals("account1"), 
				    "responseAccountList.get( 0 ) is " + 
				    responseAccountList.get( 0 ).getUsername() + 
				    " (not account1)." ) ;
		
		
	}

	
	
	@Test
	void findByPortfoliosNameShouldReturnAllPortfoliosHavingProvidedName() throws Exception {
		
		final Integer PROVIDED_ACCOUNT_ID = Integer.valueOf( 2 ) ;
		final String PROVIDED_PORTFOLIO_NAME = "portfolio2" ;
		
		MvcResult mvcResult = this.mockMvc.perform( get( "/accounts/" + 
				                                         PROVIDED_ACCOUNT_ID + 
				                                         "/search/portfolios/name/" +
				                                         PROVIDED_PORTFOLIO_NAME ) )
				                          .andDo( print() )
		                                  .andExpect( status().isOk() )
		                                  .andReturn() ;
		
		String responseJson = mvcResult.getResponse().getContentAsString() ;
		
		ObjectMapper objectMapper = new ObjectMapper() ;
		
		List<Portfolio> responsePortfolioList = objectMapper.readValue(responseJson, new TypeReference<List<Portfolio>>(){} ) ;

		
		verify( this.accountService, atLeastOnce() ).findByPortfoliosName( PROVIDED_ACCOUNT_ID, PROVIDED_PORTFOLIO_NAME ) ;
		
		
		assertNotNull( responsePortfolioList, "responsePortfolioList is Null." ) ;
		
		assertTrue( responsePortfolioList.size() == 1, 
				    "responsePortfolioList.size() is " + 
				    responsePortfolioList.size() + 
		            " (Not 1)." ) ;
		
		assertTrue( responsePortfolioList.get( 0 ).getName().equals("portfolio2"), 
				    "responsePortfolioList.get( 0 ) is " + 
				    responsePortfolioList.get( 0 ).getName() + 
				    " (not portfolio2)." ) ;
		
		
	}
	
	
	
	@Test
	void findByPortfoliosNameStartsWithShouldReturnAllPortfoliosHavingProvidedNameStartsWith() throws Exception {
		
		final Integer PROVIDED_ACCOUNT_ID = Integer.valueOf( 1 ) ;
		final String PROVIDED_PORTFOLIO_NAME_STARTS_WITH = "port" ;
		
		MvcResult mvcResult = this.mockMvc.perform( get( "/accounts/" + 
				                                         PROVIDED_ACCOUNT_ID + 
				                                         "/search/portfolios/name/starts_with/" +
				                                         PROVIDED_PORTFOLIO_NAME_STARTS_WITH ) )
				                          .andDo( print() )
		                                  .andExpect( status().isOk() )
		                                  .andReturn() ;
		
		String responseJson = mvcResult.getResponse().getContentAsString() ;
		
		ObjectMapper objectMapper = new ObjectMapper() ;
		
		List<Portfolio> responsePortfolioList = objectMapper.readValue(responseJson, new TypeReference<List<Portfolio>>(){} ) ;

		
		verify( this.accountService, atLeastOnce() )
		.findByPortfoliosNameStartsWith( PROVIDED_ACCOUNT_ID, PROVIDED_PORTFOLIO_NAME_STARTS_WITH ) ;
		
		
		assertNotNull( responsePortfolioList, "responsePortfolioList is Null." ) ;
		
		assertTrue( responsePortfolioList.size() == 2, 
				    "responsePortfolioList.size() is " + 
				    responsePortfolioList.size() + 
		            " (Not 2)." ) ;
		
		assertTrue( responsePortfolioList.get( 0 ).getName().equals("portfolio1"), 
				    "responsePortfolioList.get( 0 ) is " + 
				    responsePortfolioList.get( 0 ).getName() + 
				    " (not portfolio1)." ) ;
		assertTrue( responsePortfolioList.get( 1 ).getName().equals("portfolio3"), 
			        "responsePortfolioList.get( 1 ) is " + 
			        responsePortfolioList.get( 1 ).getName() + 
			        " (not portfolio3)." ) ;
		
		
	}



	
	

}
