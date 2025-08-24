package com.myapp.exchangeweb.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

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
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myapp.exchangeweb.models.Portfolio;
import com.myapp.exchangeweb.security.services.AccountService;
import com.myapp.exchangeweb.services.PortfolioService;


@WebMvcTest( PortfolioController.class )
@WithMockUser
class PortfolioControllerTest {

	
	@Autowired
	private MockMvc mockMvc ;
	
	@MockBean
	private PortfolioService portfolioService ;
	@MockBean
	private AccountService accountService ;

	

	@BeforeEach
	void beforeEachTest () {

		Portfolio portfolio1 = new Portfolio() ;
		portfolio1.setId( Integer.valueOf( 1 ) ) ;
		portfolio1.setName( "portfolio1" ) ;
		List<Portfolio> portfolio1List = new ArrayList<Portfolio>() ;
		portfolio1List.add( portfolio1 ) ;
		
		Portfolio portfolio2 = new Portfolio() ;
		portfolio2.setId( Integer.valueOf( 2 ) ) ;
		portfolio2.setName( "portfolio2" ) ;
		List<Portfolio> portfolio2List = new ArrayList<Portfolio>() ;
		portfolio2List.add( portfolio2 ) ;
		
		Portfolio portfolio3 = new Portfolio() ;
		portfolio3.setId( Integer.valueOf( 3 ) ) ;
		portfolio3.setName( "portfolio3" ) ;
		List<Portfolio> portfolio3List = new ArrayList<Portfolio>() ;
		portfolio3List.add( portfolio3 ) ;

				
		List<Portfolio> portfolioList = new ArrayList<Portfolio>() ;
		portfolioList.add( portfolio1 ) ;
		portfolioList.add( portfolio2 ) ;
		portfolioList.add( portfolio3 ) ;
		
		
				
		when( this.portfolioService.findAll() ).thenReturn( portfolioList ) ;
		when( this.portfolioService.findById( Integer.valueOf( 1 ) ) ).thenReturn( portfolio1List ) ;
		
		when( this.portfolioService.updateSettings( argThat( ( inputPortfolio ) -> inputPortfolio.getId() == portfolio1.getId() ) ) )
		.thenReturn( portfolio1List ) ;
		
		when( this.portfolioService.addInvestment( argThat( ( inputPortfolioId ) -> inputPortfolioId == portfolio1.getId() ), 
                                                   anyInt(), 
                                                   anyInt() ) )
		.thenReturn( portfolio1List ) ;
		
		when( this.portfolioService.removeInvestment( argThat( ( inputPortfolioId ) -> inputPortfolioId == portfolio1.getId() ), 
		                                              anyList() ) )
        .thenReturn( portfolio1List ) ;
		
		
	}
	

	
	@Test
	void findAllShouldReturnAllPortfolios() throws Exception {
		
		MvcResult mvcResult = this.mockMvc.perform( get( "/portfolios" ) )
				                          .andDo( print() )
		                                  .andExpect( status().isOk() )
		                                  .andReturn() ;
		
		String responseJson = mvcResult.getResponse().getContentAsString() ;
		
		ObjectMapper objectMapper = new ObjectMapper() ;
		
		List<Portfolio> responsePortfolioList = objectMapper.readValue(responseJson, new TypeReference<List<Portfolio>>(){} ) ;

		
		verify( this.portfolioService, atLeastOnce() ).findAll() ;
		
		
		assertNotNull( responsePortfolioList, "responsePortfolioList is Null." ) ;
		
		assertTrue( responsePortfolioList.size() == 3, "responsePortfolioList.size() is " + 
				                                       responsePortfolioList.size() + 
		                                               " (Not 3)." ) ;
		assertTrue( responsePortfolioList.get( 0 ).getName().equals("portfolio1"), "responsePortfolioList.get( 0 ) is " + 
				                                                                   responsePortfolioList.get( 0 ).getName() + 
				                                                                 " (not portfolio1)." ) ;
		assertTrue( responsePortfolioList.get( 1 ).getName().equals("portfolio2"), "responsePortfolioList.get( 1 ) is " + 
				                                                                   responsePortfolioList.get( 1 ).getName() + 
				                                                                 " (not portfolio2)." ) ;
		assertTrue( responsePortfolioList.get( 2 ).getName().equals("portfolio3"), "responsePortfolioList.get( 2 ) is " + 
				                                                                   responsePortfolioList.get( 2 ).getName() + 
				                                                                   " (not portfolio3)." ) ;
		
		
	}

	
	
	@Test
	void findByIdShouldReturnAllPortfoliosHavingProvidedId() throws Exception {
		
		final Integer PROVIDED_PORTFOLIO_ID = Integer.valueOf( 1 ) ;
		
		MvcResult mvcResult = this.mockMvc.perform( get( "/portfolios/" + PROVIDED_PORTFOLIO_ID ) )
				                          .andDo( print() )
		                                  .andExpect( status().isOk() )
		                                  .andReturn() ;
		
		String responseJson = mvcResult.getResponse().getContentAsString() ;
		
		ObjectMapper objectMapper = new ObjectMapper() ;
		
		List<Portfolio> responsePortfolioList = objectMapper.readValue(responseJson, new TypeReference<List<Portfolio>>(){} ) ;

		
		verify( this.portfolioService, atLeastOnce() ).findById( PROVIDED_PORTFOLIO_ID ) ;
		
		
		assertNotNull( responsePortfolioList, "responsePortfolioList is Null." ) ;
		
		assertTrue( responsePortfolioList.size() == 1, "responsePortfolioList.size() is " + 
		                                               responsePortfolioList.size() + 
		                                               " (Not 1)." ) ;
		assertTrue( responsePortfolioList.get( 0 ).getName().equals("portfolio1"), "responsePortfolioList.get( 0 ) is " + 
				                                                                   responsePortfolioList.get( 0 ).getName() + 
				                                                                   " (not portfolio1)." ) ;
		
		
	}

	
	
	@Test
	void updateSettingsShouldReturnCorrectView() throws Exception {
		
		final Portfolio PROVIDED_PORTFOLIO = new Portfolio() ;
		PROVIDED_PORTFOLIO.setId( Integer.valueOf( 1 ) ) ;
		
		
		MvcResult mvcResult = this.mockMvc.perform( post( "/portfolios/update_settings" )
				                                    .with( csrf() )
				                                    .param( "id" , PROVIDED_PORTFOLIO.getId().toString() ) )
				                          .andDo( print() )
		                                  .andExpect( status().is3xxRedirection() )
		                                  .andReturn() ;
		
		
		ModelAndView resultModelAndView = mvcResult.getModelAndView() ;

		
		verify( this.portfolioService, atLeastOnce() )
		.updateSettings( argThat( ( inputPortfolio ) -> inputPortfolio.getId() == PROVIDED_PORTFOLIO.getId() ) ) ;
		
		
		assertNotNull( resultModelAndView, "resultModelAndView is Null." ) ;
		
		String resultViewName = resultModelAndView.getViewName() ;
		
		assertNotNull( resultViewName, "resultViewName is Null." ) ;
		
		String expectedResultViewName = "redirect:/portfolio_profile_page/" + PROVIDED_PORTFOLIO.getId() ;
		
		
		
		assertTrue( resultViewName.equals( expectedResultViewName ), "Incorrect value for resultViewName, " + 
		                                                             "Expected: " + 
				                                                     expectedResultViewName + 
				                                                     "Actual: " +
				                                                     resultViewName ) ;
		
	}

	
	@Test
	void addInvestmentShouldReturnCorrectView() throws Exception {
		
		final Portfolio PROVIDED_PORTFOLIO = new Portfolio() ;
		PROVIDED_PORTFOLIO.setId( Integer.valueOf( 1 ) ) ;
		
		
		MvcResult mvcResult = this.mockMvc.perform( post( "/portfolios/add_investment" )
				                                    .with( csrf() )
				                                    .param( "portfolio_id" , PROVIDED_PORTFOLIO.getId().toString() )
				                                    .param( "company_id" , Integer.valueOf( 2 ).toString() )
				                                    .param( "company_quantity" , Integer.valueOf( 60 ).toString() ) )
				                          .andDo( print() )
		                                  .andExpect( status().is3xxRedirection() )
		                                  .andReturn() ;
		
		
		ModelAndView resultModelAndView = mvcResult.getModelAndView() ;

		
		verify( this.portfolioService, atLeastOnce() )
		.addInvestment( argThat( ( inputPortfolioId ) -> inputPortfolioId == PROVIDED_PORTFOLIO.getId() ), 
                anyInt(), 
                anyInt() ) ;
		
		
		assertNotNull( resultModelAndView, "resultModelAndView is Null." ) ;
		
		String resultViewName = resultModelAndView.getViewName() ;
		
		assertNotNull( resultViewName, "resultViewName is Null." ) ;
		
		String expectedResultViewName = "redirect:/portfolio_profile_page/" + PROVIDED_PORTFOLIO.getId() ;
		
		
		
		assertTrue( resultViewName.equals( expectedResultViewName ), "Incorrect value for resultViewName, " + 
		                                                             "Expected: " + 
				                                                     expectedResultViewName + 
				                                                     "Actual: " +
				                                                     resultViewName ) ;
		
	}

	
	@Test
	void removeInvestmentShouldReturnCorrectView() throws Exception {
		
		final Portfolio PROVIDED_PORTFOLIO = new Portfolio() ;
		PROVIDED_PORTFOLIO.setId( Integer.valueOf( 1 ) ) ;
		
		
		MvcResult mvcResult = this.mockMvc.perform( post( "/portfolios/remove_investment" )
				                                    .with( csrf() )
				                                    .param( "portfolio_id" , PROVIDED_PORTFOLIO.getId().toString() )
				                                    .param( "invest_slot_id_list" , "1,2,3" ) )
				                          .andDo( print() )
		                                  .andExpect( status().is3xxRedirection() )
		                                  .andReturn() ;
		
		
		ModelAndView resultModelAndView = mvcResult.getModelAndView() ;

		
		verify( this.portfolioService, atLeastOnce() )
		.removeInvestment( argThat( ( inputPortfolioId ) -> inputPortfolioId == PROVIDED_PORTFOLIO.getId() ), 
				           anyList() ) ;
		
		
		assertNotNull( resultModelAndView, "resultModelAndView is Null." ) ;
		
		String resultViewName = resultModelAndView.getViewName() ;
		
		assertNotNull( resultViewName, "resultViewName is Null." ) ;
		
		String expectedResultViewName = "redirect:/portfolio_profile_page/" + PROVIDED_PORTFOLIO.getId() ;
		
		
		
		assertTrue( resultViewName.equals( expectedResultViewName ), "Incorrect value for resultViewName, " + 
		                                                             "Expected: " + 
				                                                     expectedResultViewName + 
				                                                     "Actual: " +
				                                                     resultViewName ) ;
		
	}
	
	
	
}
