package com.myapp.exchangeweb.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.* ;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get ;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status ;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myapp.exchangeweb.models.Company;
import com.myapp.exchangeweb.services.CompanyService;
import com.myapp.exchangeweb.services.PortfolioService;


@WebMvcTest( CompanyController.class )
@WithMockUser
class CompanyControllerTest {
	
	@Autowired
	private MockMvc mockMvc ;
	
	@MockBean
	private CompanyService companyService ;
	@MockBean
	private PortfolioService portfolioService ;

	

	@BeforeEach
	void beforeEachTest () {

		Company company1 = new Company() ;
		company1.setId( Integer.valueOf( 1 ) ) ;
		company1.setName( "company1" ) ;
		company1.setSymbol( "AAA" ) ;
		List<Company> company1List = new ArrayList<Company>() ;
		company1List.add( company1 ) ;

		Company company2 = new Company() ;
		company2.setId( Integer.valueOf( 2 ) ) ;
		company2.setName( "company2" ) ;
		company2.setSymbol( "ABB" ) ;
		List<Company> company2List = new ArrayList<Company>() ;
		company2List.add( company2 ) ;
		
		Company company3 = new Company() ;
		company3.setId( Integer.valueOf( 3 ) ) ;
		company3.setName( "company3" ) ;
		company3.setSymbol( "GAA" ) ;
		List<Company> company3List = new ArrayList<Company>() ;
		company3List.add( company3 ) ;
		
		List<Company> companyList = new ArrayList<Company>() ;
		companyList.add( company1 ) ;
		companyList.add( company2 ) ;
		companyList.add( company3 ) ;
		
		
		List<Company> companyListSymbol = new ArrayList<Company>() ;
		companyListSymbol.add( company3 ) ;
		
		List<Company> companyListSymbolStartsWith = new ArrayList<Company>() ;
		companyListSymbolStartsWith.add( company1 ) ;
		companyListSymbolStartsWith.add( company2 ) ;
		
				
		when( this.companyService.findAll() ).thenReturn( companyList ) ;
		when( this.companyService.findById( Integer.valueOf( 1 ) ) ).thenReturn( company1List ) ;
		when( this.companyService.findById( Integer.valueOf( 2 ) ) ).thenReturn( company2List ) ;
		when( this.companyService.findById( Integer.valueOf( 3 ) ) ).thenReturn( company3List ) ;
		
		when( this.companyService.findBySymbol( "GAA" ) ).thenReturn( companyListSymbol ) ;
		when( this.companyService.findBySymbolStartsWith( "A" ) ).thenReturn( companyListSymbolStartsWith ) ;
		
		
	}

	
	
	
	@Test
	void findAllShouldReturnAllCompanies() throws Exception {
		
		MvcResult mvcResult = this.mockMvc.perform( get( "/companies" ) )
				                          .andDo( print() )
		                                  .andExpect( status().isOk() )
		                                  .andReturn() ;
		
		String responseJson = mvcResult.getResponse().getContentAsString() ;
		
		ObjectMapper objectMapper = new ObjectMapper() ;
		
		List<Company> responseCompanyList = objectMapper.readValue(responseJson, new TypeReference<List<Company>>(){} ) ;

		
		verify( this.companyService, atLeastOnce() ).findAll() ;
		
		
		assertNotNull( responseCompanyList, "responseCompanyList is Null." ) ;
		
		assertTrue( responseCompanyList.size() == 3, 
				    "responseCompanyList.size() is " + 
		            responseCompanyList.size() + 
		            " (Not 3)." ) ;
		
		assertTrue( responseCompanyList.get( 0 ).getName().equals("company1"), 
				    "responseCompanyList.get( 0 ) is " + 
				    responseCompanyList.get( 0 ).getName() + 
				    " (not company1)." ) ;
		assertTrue( responseCompanyList.get( 1 ).getName().equals("company2"), 
				    "responseCompanyList.get( 1 ) is " + 
				    responseCompanyList.get( 1 ).getName() + 
				    " (not company2)." ) ;
		assertTrue( responseCompanyList.get( 2 ).getName().equals("company3"), 
				    "responseCompanyList.get( 2 ) is " + 
				    responseCompanyList.get( 2 ).getName() + 
				    " (not company3)." ) ;
		
		
	}

	
	@Test
	void findByIdShouldReturnAllCompaniesHavingProvidedId() throws Exception {
		
		final Integer PROVIDED_COMPANY_ID = Integer.valueOf( 1 ) ;
		
		MvcResult mvcResult = this.mockMvc.perform( get( "/companies/" + PROVIDED_COMPANY_ID ) )
				                          .andDo( print() )
		                                  .andExpect( status().isOk() )
		                                  .andReturn() ;
		
		String responseJson = mvcResult.getResponse().getContentAsString() ;
		
		ObjectMapper objectMapper = new ObjectMapper() ;
		
		List<Company> responseCompanyList = objectMapper.readValue(responseJson, new TypeReference<List<Company>>(){} ) ;

		
		verify( this.companyService, atLeastOnce() ).findById( PROVIDED_COMPANY_ID ) ;
		
		
		assertNotNull( responseCompanyList, "responseCompanyList is Null." ) ;
		
		assertTrue( responseCompanyList.size() == 1, 
				    "responseCompanyList.size() is " + 
		            responseCompanyList.size() + 
		            " (Not 1)." ) ;
		assertTrue( responseCompanyList.get( 0 ).getName().equals("company1"), 
				    "responseCompanyList.get( 0 ) is " + 
				    responseCompanyList.get( 0 ).getName() + 
				    " (not company1)." ) ;
		
		
	}

	
	
	@Test
	void findBySymbolShouldReturnAllCompaniesHavingProvidedSymbol() throws Exception {
		
		final String PROVIDED_SYMBOL = "GAA" ;
		
		MvcResult mvcResult = this.mockMvc.perform( get( "/companies/search/symbol/" + PROVIDED_SYMBOL ) )
				                          .andDo( print() )
		                                  .andExpect( status().isOk() )
		                                  .andReturn() ;
		
		String responseJson = mvcResult.getResponse().getContentAsString() ;
		
		ObjectMapper objectMapper = new ObjectMapper() ;
		
		List<Company> responseCompanyList = objectMapper.readValue(responseJson, new TypeReference<List<Company>>(){} ) ;

		
		verify( this.companyService, atLeastOnce() ).findBySymbol( PROVIDED_SYMBOL ) ;
		
		
		assertNotNull( responseCompanyList, "responseCompanyList is Null." ) ;
		
		assertTrue( responseCompanyList.size() == 1, 
				    "responseCompanyList.size() is " + 
		            responseCompanyList.size() + 
		            " (Not 1)." ) ;
		assertTrue( responseCompanyList.get( 0 ).getName().equals("company3"), 
				    "responseCompanyList.get( 0 ) is " + 
				    responseCompanyList.get( 0 ).getName() + 
				    " (not company3)." ) ;
		
		
	}
	
	
	@Test
	void findBySymbolStartsWithShouldReturnAllCompaniesHavingProvidedSymbolStartsWith() throws Exception {
		
		final String PROVIDED_SYMBOL_STARTS_WITH = "A" ;
		
		MvcResult mvcResult = this.mockMvc.perform( get( "/companies/search/symbol/starts_with/" + PROVIDED_SYMBOL_STARTS_WITH ) )
				                          .andDo( print() )
		                                  .andExpect( status().isOk() )
		                                  .andReturn() ;
		
		String responseJson = mvcResult.getResponse().getContentAsString() ;
		
		ObjectMapper objectMapper = new ObjectMapper() ;
		
		List<Company> responseCompanyList = objectMapper.readValue(responseJson, new TypeReference<List<Company>>(){} ) ;

		
		verify( this.companyService, atLeastOnce() ).findBySymbolStartsWith( PROVIDED_SYMBOL_STARTS_WITH ) ;
		
		
		assertNotNull( responseCompanyList, "responseCompanyList is Null." ) ;
		
		assertTrue( responseCompanyList.size() == 2, 
				    "responseCompanyList.size() is " + 
		            responseCompanyList.size() + 
		            " (Not 2)." ) ;
		assertTrue( responseCompanyList.get( 0 ).getName().equals("company1"), 
				    "responseCompanyList.get( 0 ) is " + 
				    responseCompanyList.get( 0 ).getName() + 
				    " (not company1)." ) ;
		assertTrue( responseCompanyList.get( 1 ).getName().equals("company2"), 
				    "responseCompanyList.get( 1 ) is " + 
                    responseCompanyList.get( 1 ).getName() + 
                    " (not company2)." ) ;
		
		
	}
	
	
	
}
