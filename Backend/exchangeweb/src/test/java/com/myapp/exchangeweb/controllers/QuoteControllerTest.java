package com.myapp.exchangeweb.controllers;

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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myapp.exchangeweb.models.Quote;
import com.myapp.exchangeweb.services.QuoteService;


@WebMvcTest( QuoteController.class )
@WithMockUser
class QuoteControllerTest {

		
	@Autowired
	private MockMvc mockMvc ;
	
	@MockBean
	private QuoteService quoteService ;

	

	

	@BeforeEach
	void beforeEachTest () {
		

		Quote quote1 = new Quote() ;
		quote1.setId( Integer.valueOf( 1 ) ) ;		
		List<Quote> quote1List = new ArrayList<Quote>() ;
		quote1List.add( quote1 ) ;


		Quote quote2 = new Quote() ;
		quote2.setId( Integer.valueOf( 2 ) ) ;		
		List<Quote> quote2List = new ArrayList<Quote>() ;
		quote2List.add( quote2 ) ;


		Quote quote3 = new Quote() ;
		quote3.setId( Integer.valueOf( 3 ) ) ;		
		List<Quote> quote3List = new ArrayList<Quote>() ;
		quote3List.add( quote3 ) ;

		
		List<Quote> quoteList = new ArrayList<Quote>() ;
		quoteList.add( quote1 ) ;
		quoteList.add( quote2 ) ;
		quoteList.add( quote3 ) ;
		
				
				
		when( this.quoteService.findAll() ).thenReturn( quoteList ) ;
		when( this.quoteService.findById( Integer.valueOf( 1 ) ) ).thenReturn( quote1List ) ;		
		
	}

	
	
	
	@Test
	void findAllShouldReturnAllQuotes() throws Exception {
		
		MvcResult mvcResult = this.mockMvc.perform( get( "/quotes" ) )
				                          .andDo( print() )
		                                  .andExpect( status().isOk() )
		                                  .andReturn() ;
		
		String responseJson = mvcResult.getResponse().getContentAsString() ;
		
		ObjectMapper objectMapper = new ObjectMapper() ;
		
		List<Quote> responseQuoteList = objectMapper.readValue(responseJson, new TypeReference<List<Quote>>(){} ) ;

		
		verify( this.quoteService, atLeastOnce() ).findAll() ;
		
		
		assertNotNull( responseQuoteList, "responseQuoteList is Null." ) ;
		
		assertTrue( responseQuoteList.size() == 3, "responseQuoteList.size() is " + 
				                                   responseQuoteList.size() + 
				                                   " (Not 3)." ) ;
		assertTrue( responseQuoteList.get( 0 ).getId().equals( 1 ), "responseQuoteList.get( 0 ) is " + 
				                                                    responseQuoteList.get( 0 ).getId() + 
				                                                    " (not 1 for quote1)." ) ;
		assertTrue( responseQuoteList.get( 1 ).getId().equals( 2 ), "responseQuoteList.get( 1 ) is " + 
                                                                    responseQuoteList.get( 1 ).getId() + 
                                                                    " (not 2 for quote2)." ) ;
		assertTrue( responseQuoteList.get( 2 ).getId().equals( 3 ), "responseQuoteList.get( 2 ) is " + 
                                                                    responseQuoteList.get( 2 ).getId() + 
                                                                    " (not 3 for quote3)." ) ;

		
		
	}


	
	@Test
	void findByIdShouldReturnAllQuotesHavingProvidedId() throws Exception {
		
		final Integer PROVIDED_QUOTE_ID = Integer.valueOf( 1 ) ;
		
		MvcResult mvcResult = this.mockMvc.perform( get( "/quotes/" + PROVIDED_QUOTE_ID ) )
				                          .andDo( print() )
		                                  .andExpect( status().isOk() )
		                                  .andReturn() ;
		
		String responseJson = mvcResult.getResponse().getContentAsString() ;
		
		ObjectMapper objectMapper = new ObjectMapper() ;
		
		List<Quote> responseQuoteList = objectMapper.readValue(responseJson, new TypeReference<List<Quote>>(){} ) ;

		
		verify( this.quoteService, atLeastOnce() ).findById( PROVIDED_QUOTE_ID ) ;
		
		
		assertNotNull( responseQuoteList, "responseQuoteList is Null." ) ;
		
		assertTrue( responseQuoteList.size() == 1, "responseQuoteList.size() is " + 
		                                           responseQuoteList.size() + 
		                                           " (Not 1)." ) ;
		assertTrue( responseQuoteList.get( 0 ).getId().equals( PROVIDED_QUOTE_ID ), "responseQuoteList.get( 0 ).getId() is " + 
				                                                                    responseQuoteList.get( 0 ).getId() + 
				                                                                    " (not 1 for quote1)." ) ;
		
		
	}


	
	
	
}
