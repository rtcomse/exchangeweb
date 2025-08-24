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
import com.myapp.exchangeweb.security.models.Role;
import com.myapp.exchangeweb.security.services.RoleService;


@WebMvcTest( RoleController.class )
@WithMockUser
class RoleControllerTest {
	
	
	@Autowired
	private MockMvc mockMvc ;
	
	@MockBean
	private RoleService roleService ;
	
	
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
		
		List<Role> roleUserList = new ArrayList<Role>() ;
		roleUserList.add( roleUser ) ;
				
		List<Role> roleAdminList = new ArrayList<Role>() ;
		roleAdminList.add( roleAdmin ) ;
		
		
		List<Authority> roleUserAuthoritiesList = new ArrayList<Authority>() ;
		roleUserAuthoritiesList.add( authorityRead ) ;
		
		List<Authority> roleAdminAuthoritiesList = new ArrayList<Authority>() ;
		roleAdminAuthoritiesList.add( authorityRead ) ;
		roleAdminAuthoritiesList.add( authorityWrite ) ;
		
		roleUser.setAuthorities( roleUserAuthoritiesList ) ;
		roleAdmin.setAuthorities( roleAdminAuthoritiesList ) ;
		
		
		
		List<Role> roleList = new ArrayList<Role>() ;
		roleList.add( roleUser ) ;
		roleList.add( roleAdmin ) ;
		
		when( this.roleService.findAll() ).thenReturn( roleList ) ;
		when( this.roleService.findById( roleUser.getId() ) ).thenReturn( roleUserList ) ;
		when( this.roleService.findById( roleAdmin.getId() ) ).thenReturn( roleAdminList ) ;
		
		when( this.roleService.listAuthorities( roleUser.getId() ) )
		.thenReturn( roleUser.getAuthorities() ) ;		
		when( this.roleService.hasAuthority( authorityWrite.getId() ) )
		.thenReturn( roleAdminList ) ;

		
		
	}

	
	@Test
	void findAllShouldReturnAllRoles() throws Exception {
		
		MvcResult mvcResult = this.mockMvc.perform( get( "/roles" ) )
				                          .andDo( print() )
		                                  .andExpect( status().isOk() )
		                                  .andReturn() ;
		
		String responseJson = mvcResult.getResponse().getContentAsString() ;
		
		ObjectMapper objectMapper = new ObjectMapper() ;
		
		List<Role> responseRoleList = objectMapper.readValue(responseJson, new TypeReference<List<Role>>(){} ) ;

		
		verify( this.roleService, atLeastOnce() ).findAll() ;
		
		
		assertNotNull( responseRoleList, "responseRoleList is Null." ) ;
		
		assertTrue( responseRoleList.size() == 2, 
				    "responseRoleList.size() is " + 
				    responseRoleList.size() + 
		            " (Not 2)." ) ;
		
		assertTrue( responseRoleList.get( 0 ).getName().equals("ROLE_USER"), 
				    "responseRoleList.get( 0 ) is " + 
				    responseRoleList.get( 0 ).getName() + 
				    " (not ROLE_USER)." ) ;
		assertTrue( responseRoleList.get( 1 ).getName().equals("ROLE_ADMIN"), 
				    "responseRoleList.get( 1 ) is " + 
				    responseRoleList.get( 1 ).getName() + 
				    " (not ROLE_ADMIN)." ) ;

		
		
	}
	
	
		
	@Test
	void findByIdShouldReturnAllRolesHavingProvidedId() throws Exception {
		
		final Integer PROVIDED_ROLE_ID = Integer.valueOf( 1 ) ;
		
		MvcResult mvcResult = this.mockMvc.perform( get( "/roles/" + PROVIDED_ROLE_ID ) )
				                          .andDo( print() )
		                                  .andExpect( status().isOk() )
		                                  .andReturn() ;
		
		String responseJson = mvcResult.getResponse().getContentAsString() ;
		
		ObjectMapper objectMapper = new ObjectMapper() ;
		
		List<Role> responseRoleList = objectMapper.readValue(responseJson, new TypeReference<List<Role>>(){} ) ;

		
		verify( this.roleService, atLeastOnce() ).findById( PROVIDED_ROLE_ID ) ;
		
		
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
	void listAuthoritiesShouldReturnAllAuthoritiesForProvidedRoleId() throws Exception {
		
		final Integer PROVIDED_ROLE_ID = Integer.valueOf( 1 ) ;
		
		MvcResult mvcResult = this.mockMvc.perform( get( "/roles/" + 
				                                         PROVIDED_ROLE_ID + 
				                                         "/list_authorities" ) )
				                          .andDo( print() )
		                                  .andExpect( status().isOk() )
		                                  .andReturn() ;
		
		String responseJson = mvcResult.getResponse().getContentAsString() ;
		
		ObjectMapper objectMapper = new ObjectMapper() ;
		
		List<Authority> responseAuthorityList = objectMapper.readValue(responseJson, new TypeReference<List<Authority>>(){} ) ;

		
		verify( this.roleService, atLeastOnce() ).listAuthorities( PROVIDED_ROLE_ID ) ;
		
		
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
	void hasAuthorityShouldReturnAllRolesHavingProvidedAuthorityId() throws Exception {
		
		final Integer PROVIDED_AUTHORITY_ID = Integer.valueOf( 4 ) ;
		
		MvcResult mvcResult = this.mockMvc.perform( get( "/roles/has_authority/" + 
		                                                 PROVIDED_AUTHORITY_ID ) )
				                          .andDo( print() )
		                                  .andExpect( status().isOk() )
		                                  .andReturn() ;
		
		String responseJson = mvcResult.getResponse().getContentAsString() ;
		
		ObjectMapper objectMapper = new ObjectMapper() ;
		
		List<Role> responseRoleList = objectMapper.readValue(responseJson, new TypeReference<List<Role>>(){} ) ;

		
		verify( this.roleService, atLeastOnce() ).hasAuthority( PROVIDED_AUTHORITY_ID ) ;
		
		
		assertNotNull( responseRoleList, "responseRoleList is Null." ) ;
		
		assertTrue( responseRoleList.size() == 1, 
				    "responseRoleList.size() is " + 
				    responseRoleList.size() + 
		            " (Not 1)." ) ;
		assertTrue( responseRoleList.get( 0 ).getName().equals("ROLE_ADMIN"), 
				    "responseRoleList.get( 0 ) is " + 
				    responseRoleList.get( 0 ).getName() + 
				    " (not ROLE_ADMIN)." ) ;
		
		
	}
	
	
	
	
	
	
	
}
