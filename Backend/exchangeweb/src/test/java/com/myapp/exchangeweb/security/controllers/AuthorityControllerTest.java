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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myapp.exchangeweb.security.models.Authority;
import com.myapp.exchangeweb.security.services.AuthorityService;


@WebMvcTest( AuthorityController.class )
@WithMockUser
class AuthorityControllerTest {
	
		
	@Autowired
	private MockMvc mockMvc ;
	
	@MockBean
	private AuthorityService authorityService ;
	
	
	@BeforeEach
	void beforeEachTest () {
		
		Authority authorityRead = new Authority() ;
		authorityRead.setId( Integer.valueOf( 3 ) ) ;
		authorityRead.setName( "AUTHORITY_READ" ) ;
		
		Authority authorityWrite = new Authority() ;
		authorityWrite.setId( Integer.valueOf( 4 ) ) ;
		authorityWrite.setName( "AUTHORITY_WRITE" ) ;
		
		List<Authority> authorityReadList = new ArrayList<Authority>() ;
		authorityReadList.add( authorityRead ) ;
				
		List<Authority> authorityWriteList = new ArrayList<Authority>() ;
		authorityWriteList.add( authorityWrite ) ;
				
		List<Authority> AuthorityList = new ArrayList<Authority>() ;
		AuthorityList.add( authorityRead ) ;
		AuthorityList.add( authorityWrite ) ;

		
		when( this.authorityService.findAll() ).thenReturn( AuthorityList ) ;
		when( this.authorityService.findById( authorityRead.getId() ) ).thenReturn( authorityReadList ) ;
		when( this.authorityService.findById( authorityWrite.getId() ) ).thenReturn( authorityWriteList ) ;
		
		
		
	}
	
	
	@Test
	void findAllShouldReturnAllAuthorities() throws Exception {
		
		MvcResult mvcResult = this.mockMvc.perform( get( "/authorities" ) )
				                          .andDo( print() )
		                                  .andExpect( status().isOk() )
		                                  .andReturn() ;
		
		String responseJson = mvcResult.getResponse().getContentAsString() ;
		
		ObjectMapper objectMapper = new ObjectMapper() ;
		
		List<Authority> responseAuthorityList = objectMapper.readValue(responseJson, new TypeReference<List<Authority>>(){} ) ;

		
		verify( this.authorityService, atLeastOnce() ).findAll() ;
		
		
		assertNotNull( responseAuthorityList, "responseAuthorityList is Null." ) ;
		
		assertTrue( responseAuthorityList.size() == 2, 
				    "responseAuthorityList.size() is " + 
				    responseAuthorityList.size() + 
		            " (Not 2)." ) ;
		
		assertTrue( responseAuthorityList.get( 0 ).getName().equals("AUTHORITY_READ"), 
				    "responseAuthorityList.get( 0 ) is " + 
				    responseAuthorityList.get( 0 ).getName() + 
				    " (not AUTHORITY_READ)." ) ;
		assertTrue( responseAuthorityList.get( 1 ).getName().equals("AUTHORITY_WRITE"), 
				    "responseAuthorityList.get( 1 ) is " + 
				    responseAuthorityList.get( 1 ).getName() + 
				    " (not AUTHORITY_WRITE)." ) ;

		
		
	}
	
	@Test
	void findByIdShouldReturnAllAuthoritiesHavingProvidedId() throws Exception {
		
		final Integer PROVIDED_AUTHORITY_ID = Integer.valueOf( 3 ) ;
		
		MvcResult mvcResult = this.mockMvc.perform( get( "/authorities/" + 
		                                                 PROVIDED_AUTHORITY_ID ) )
				                          .andDo( print() )
		                                  .andExpect( status().isOk() )
		                                  .andReturn() ;
		
		String responseJson = mvcResult.getResponse().getContentAsString() ;
		
		ObjectMapper objectMapper = new ObjectMapper() ;
		
		List<Authority> responseAuthorityList = objectMapper.readValue(responseJson, new TypeReference<List<Authority>>(){} ) ;

		
		verify( this.authorityService, atLeastOnce() ).findById( PROVIDED_AUTHORITY_ID ) ;
		
		
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


}
