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
import com.myapp.exchangeweb.models.InvestSlot;
import com.myapp.exchangeweb.services.InvestSlotService;


@WebMvcTest( InvestSlotController.class )
@WithMockUser
class InvestSlotControllerTest {

	
	@Autowired
	private MockMvc mockMvc ;
	
	@MockBean
	private InvestSlotService investSlotService ;

	

	@BeforeEach
	void beforeEachTest () {
		
		InvestSlot investSlot1 = new InvestSlot() ;
		investSlot1.setId( Integer.valueOf( 1 ) );
		List<InvestSlot> investSlot1List = new ArrayList<InvestSlot>() ;
		investSlot1List.add( investSlot1 ) ;
		
		InvestSlot investSlot2 = new InvestSlot() ;
		investSlot2.setId( Integer.valueOf( 2 ) );
		List<InvestSlot> investSlot2List = new ArrayList<InvestSlot>() ;
		investSlot2List.add( investSlot2 ) ;
		
		InvestSlot investSlot3 = new InvestSlot() ;
		investSlot3.setId( Integer.valueOf( 3 ) );
		List<InvestSlot> investSlot3List = new ArrayList<InvestSlot>() ;
		investSlot3List.add( investSlot3 ) ;
		
		List<InvestSlot> investSlotList = new ArrayList<InvestSlot>() ;
		investSlotList.add( investSlot1 ) ;
		investSlotList.add( investSlot2 ) ;
		investSlotList.add( investSlot3 ) ;
		
				
		when( this.investSlotService.findAll() ).thenReturn( investSlotList ) ;
		when( this.investSlotService.findById( Integer.valueOf( 2 ) ) ).thenReturn( investSlot2List ) ;
		
		
	}

	
		
	@Test
	void findAllShouldReturnAllInvestSlots() throws Exception {
		
		MvcResult mvcResult = this.mockMvc.perform( get( "/invest_slots" ) )
				                          .andDo( print() )
		                                  .andExpect( status().isOk() )
		                                  .andReturn() ;
		
		String responseJson = mvcResult.getResponse().getContentAsString() ;
		
		ObjectMapper objectMapper = new ObjectMapper() ;
		
		List<InvestSlot> responseInvestSlotList = objectMapper.readValue(responseJson, new TypeReference<List<InvestSlot>>(){} ) ;

		
		verify( this.investSlotService, atLeastOnce() ).findAll() ;
		
		
		assertNotNull( responseInvestSlotList, "responseInvestSlotList is Null." ) ;
		
		assertTrue( responseInvestSlotList.size() == 3, "responseInvestSlotList.size() is " + 
		                                                 responseInvestSlotList.size() + 
				                                         " (Not 3)." ) ;
		assertTrue( responseInvestSlotList.get( 0 ).getId().equals( 1 ), "responseInvestSlotList.get( 0 ) is " + 
				                                                         responseInvestSlotList.get( 0 ).getId() + 
				                                                         " (not 1 for investSlot1)." ) ;
		assertTrue( responseInvestSlotList.get( 1 ).getId().equals( 2 ), "responseInvestSlotList.get( 1 ) is " + 
				                                                         responseInvestSlotList.get( 1 ).getId() + 
				                                                         " (not 2 for investSlot2)." ) ;
		assertTrue( responseInvestSlotList.get( 2 ).getId().equals( 3 ), "responseInvestSlotList.get( 2 ) is " + 
				                                                         responseInvestSlotList.get( 2 ).getId() + 
				                                                         " (not 3 for investSlot3)." ) ;
		
		
	}

	
	
	@Test
	void findByIdShouldReturnAllInvestSlotsHavingProvidedId() throws Exception {
		
		final Integer PROVIDED_INVEST_SLOT_ID = Integer.valueOf( 2 ) ;
		
		MvcResult mvcResult = this.mockMvc.perform( get( "/invest_slots/" + PROVIDED_INVEST_SLOT_ID ) )
				                          .andDo( print() )
		                                  .andExpect( status().isOk() )
		                                  .andReturn() ;

		
		String responseJson = mvcResult.getResponse().getContentAsString() ;
		
		ObjectMapper objectMapper = new ObjectMapper() ;
		
		List<InvestSlot> responseInvestSlotList = objectMapper.readValue(responseJson, new TypeReference<List<InvestSlot>>(){} ) ;

		
		verify( this.investSlotService, atLeastOnce() ).findById( PROVIDED_INVEST_SLOT_ID ) ;
		
		
		assertNotNull( responseInvestSlotList, "responseInvestSlotList is Null." ) ;
		
		assertTrue( responseInvestSlotList.size() == 1, "responseInvestSlotList.size() is " + 
		                                                 responseInvestSlotList.size() + 
				                                         " (Not 1)." ) ;
		assertTrue( responseInvestSlotList.get( 0 ).getId().equals( PROVIDED_INVEST_SLOT_ID ), 
				    "responseInvestSlotList.get( 0 ) is " + 
				    responseInvestSlotList.get( 0 ).getId() + 
				    " (not 2 for investSlot2)." ) ;
		
		
	}

	
}
