package com.myapp.exchangeweb.kafka.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myapp.exchangeweb.kafka.services.PriceService;
import com.myapp.exchangeweb.models.Company;
import com.myapp.exchangeweb.services.CompanyService;


@WebMvcTest( PriceController.class )
@WithMockUser
class PriceControllerTest {

	
	@Autowired
	private MockMvc mockMvc ;
	
	@MockBean
	private PriceService priceService ;
	@MockBean
	private CompanyService companyService ;


	@BeforeEach
	void beforeEachTest () {

		Company company1 = new Company() ;
		company1.setId( Integer.valueOf( 1 ) ) ;
		company1.setName( "company1" ) ;
		company1.setSymbol( "AAA" ) ;
		List<Company> company1List = new ArrayList<Company>() ;
		company1List.add( company1 ) ;
		
		List<Double> company1PriceList = new ArrayList<Double>() ;
		company1PriceList.add( Double.valueOf( 1.25 ) ) ;
		company1PriceList.add( Double.valueOf( 2.25 ) ) ;
		company1PriceList.add( Double.valueOf( 3.25 ) ) ;
	
	
		when( this.priceService.findByDays( argThat( (inputCompany) -> inputCompany.getId() == company1.getId() ), anyInt() ) )
		.thenAnswer( new Answer<List<Double>>() {
			
			public List<Double> answer( InvocationOnMock invocation ) {
				
				List<Double> retPriceList = new ArrayList<Double>() ;
				
				Object[] args = invocation.getArguments() ;
				
				Integer argNumDays = (Integer) args[ 1 ] ;

				
				if ( argNumDays >= company1PriceList.size() ) {
					
					retPriceList = company1PriceList ;
					
				}
				else {
					
					for ( int i = ( company1PriceList.size() - argNumDays ) ; i < company1PriceList.size() ; i++ ) {
						
						retPriceList.add( company1PriceList.get( i ) ) ;
						
					}
					
				}

				
				return retPriceList ;
				
			}
			
		} ) ;
		
		
		when( this.priceService.appendByDays( argThat( (inputCompany) -> inputCompany.getId() == company1.getId() ), anyList() ) )
		.thenReturn( Boolean.valueOf( true ) ) ;
		
		when( this.companyService.findById( argThat( ( inputCompanyId ) -> inputCompanyId == company1.getId() ) ) )
		.thenReturn( company1List ) ;
		
		
	}

	
	
	@Test
	void findByDaysShouldReturnAllPricesForProvidedCompanyIdandNumDays() throws Exception {
		
		final Integer PROVIDED_COMPANY_ID = Integer.valueOf( 1 ) ;
		final Integer PROVIDED_NUM_DAYS = Integer.valueOf( 3 ) ;
		
		MvcResult mvcResult = this.mockMvc.perform( get( "/prices" + 
		                                                 "/company/" + PROVIDED_COMPANY_ID + 
				                                         "/days/" + PROVIDED_NUM_DAYS ) )
				                          .andDo( print() )
		                                  .andExpect( status().isOk() )
		                                  .andReturn() ;
		
		String responseJson = mvcResult.getResponse().getContentAsString() ;
		
		ObjectMapper objectMapper = new ObjectMapper() ;
		
		List<Double> responsePriceList = objectMapper.readValue(responseJson, new TypeReference<List<Double>>(){} ) ;

		
		verify( this.priceService, atLeastOnce() ).findByDays( argThat( ( inputCompany ) -> inputCompany.getId() == PROVIDED_COMPANY_ID ), 
				                                               eq( PROVIDED_NUM_DAYS ) ) ;
		
		
		assertNotNull( responsePriceList, "responsePriceList is Null." ) ;
		
		assertTrue( responsePriceList.size() <= PROVIDED_NUM_DAYS, 
				    "responsePriceList.size() is " + responsePriceList.size() + 
				    " (Not <= " + PROVIDED_NUM_DAYS + ")." ) ;
		assertTrue( responsePriceList.get( 0 ).equals( Double.valueOf( 1.25 ) ),
				    "responsePriceList.get( 0 ) is " + 
				    responsePriceList.get( 0 ) + " (not 1.25)." ) ;
		
		assertTrue( responsePriceList.get( 1 ).equals( Double.valueOf( 2.25 ) ),
			        "responsePriceList.get( 1 ) is " + 
			        responsePriceList.get( 1 ) + " (not 2.25)." ) ;
		
		assertTrue( responsePriceList.get( 2 ).equals( Double.valueOf( 3.25 ) ),
			        "responsePriceList.get( 2 ) is " + 
			        responsePriceList.get( 2 ) + " (not 3.25)." ) ;
		
		
	}

	
	@Test
	void appendByDaysShouldReturnTrueAfterSuccessfulAppend() throws Exception {
		
		final Integer PROVIDED_COMPANY_ID = Integer.valueOf( 1 ) ;
		final List<Double> PROVIDED_PRICE_LIST = new ArrayList<Double>() ;
		PROVIDED_PRICE_LIST.add( Double.valueOf( 21.25 ) ) ;
		PROVIDED_PRICE_LIST.add( Double.valueOf( 22.25 ) ) ;
		PROVIDED_PRICE_LIST.add( Double.valueOf( 23.25 ) ) ;

		
		ObjectMapper objectMapper = new ObjectMapper() ;
		
		String requestJson = objectMapper.writeValueAsString( PROVIDED_PRICE_LIST ) ;
		
		
		MvcResult mvcResult = this.mockMvc.perform( post( "/prices" + 
		                                                  "/company/" + 
		                                                  PROVIDED_COMPANY_ID + 
		                                                  "/append" )
				                                    .with( csrf() )
				                                    .contentType( MediaType.APPLICATION_JSON )
				                                    .content( requestJson ) )
				                          .andDo( print() )
		                                  .andExpect( status().isOk() )
		                                  .andReturn() ;

		
		String responseJson = mvcResult.getResponse().getContentAsString() ;
		
		Boolean responseBoolean = objectMapper.readValue(responseJson, new TypeReference<Boolean>(){} ) ;
		
		
		
		verify( this.priceService, atLeastOnce() ).appendByDays( argThat( ( inputCompany ) -> inputCompany.getId() == PROVIDED_COMPANY_ID ), 
				                                                 eq( PROVIDED_PRICE_LIST ) ) ;
		
		
		assertNotNull( responseBoolean, "responseBoolean is Null." ) ;
		
		assertTrue( responseBoolean, 
				    "responseBoolean is " + 
		            responseBoolean + 
				    " (Not True)." ) ;
		
				
	}
	
	
}
