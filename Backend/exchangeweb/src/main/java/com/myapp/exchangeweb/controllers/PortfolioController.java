package com.myapp.exchangeweb.controllers;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.myapp.exchangeweb.models.Portfolio;
import com.myapp.exchangeweb.security.models.Account;
import com.myapp.exchangeweb.security.services.AccountService;
import com.myapp.exchangeweb.services.PortfolioService;


@RestController
public class PortfolioController {
	

	@Autowired
	private PortfolioService portfolioService ;
	@Autowired
	private AccountService accountService ;
	
	
	// LOGGER
	private static final Logger LOGGER = LoggerFactory.getLogger( PortfolioController.class ) ;
	
	
	@GetMapping( "/portfolios" )
	public List<Portfolio> findAll () {
		
		LOGGER.debug( "ENTER: findAll ()" ) ;
		
		List<Portfolio> retPortfolioList = new ArrayList<Portfolio>() ;
		
		
		retPortfolioList = this.portfolioService.findAll() ;
		
		
		LOGGER.debug( "EXIT: findAll ()" ) ;
		
		return retPortfolioList ;
		
	}
	
	
	@GetMapping( "/portfolios/{portfolioId}" )
	public List<Portfolio> findById ( @PathVariable Integer portfolioId ) {
		
		LOGGER.debug( "ENTER: findById ()" ) ;
		LOGGER.debug( "portfolioId: {}", portfolioId ) ;
		
		List<Portfolio> retPortfolioList = new ArrayList<Portfolio>() ;
		
		retPortfolioList = this.portfolioService.findById( portfolioId ) ;
		
		
		LOGGER.debug( "EXIT: findById ()" ) ;
		
		return retPortfolioList ;
		
	}
	
	@PostMapping( "/portfolios/update_settings" )
	public ModelAndView updateSettings ( @ModelAttribute Portfolio inputPortfolio ) {
		
		LOGGER.debug( "ENTER: updateSettings ()" ) ;
		LOGGER.debug( "inputPortfolio: {}", inputPortfolio ) ;
	
		ModelAndView retModelAndView = new ModelAndView( "redirect:/portfolio_profile_page/" + inputPortfolio.getId() ) ;
		
		
		this.portfolioService.updateSettings( inputPortfolio ) ;
		
		
		LOGGER.debug( "EXIT: updateSettings ()" ) ;
		
		return retModelAndView ;
		
	}

	
	@PostMapping( "/portfolios/add_investment" )
	public ModelAndView addInvestment ( @ModelAttribute( "portfolio_id" ) Integer portfolioId,
			                            @ModelAttribute( "company_id" ) Integer companyId,
			                            @ModelAttribute( "company_quantity" ) Integer companyQuantity ) {
		
		LOGGER.debug( "ENTER: addInvestment ()" ) ;
		LOGGER.debug( "portfolioId: {}", portfolioId ) ;
		LOGGER.debug( "companyId: {}", companyId ) ;
		LOGGER.debug( "companyQuantity: {}", companyQuantity ) ;
	
		ModelAndView retModelAndView = new ModelAndView( "redirect:/portfolio_profile_page/" + portfolioId ) ;
		
		
		this.portfolioService.addInvestment( portfolioId, 
				                             companyId, 
				                             companyQuantity ) ;
		
		
		LOGGER.debug( "EXIT: addInvestment ()" ) ;
		
		return retModelAndView ;
		
	}
	
	
	@PostMapping( "/portfolios/remove_investment" )
	public ModelAndView removeInvestment ( @ModelAttribute( "portfolio_id" ) Integer portfolioId,
			                               @RequestParam( name = "invest_slot_id_list", 
			                                              required = false,
			                                              defaultValue = "" ) 
	                                       ArrayList<Integer> investSlotIdList ) {
		
		LOGGER.debug( "ENTER: removeInvestment ()" ) ;
		LOGGER.debug( "portfolioId: {}", portfolioId ) ;
		LOGGER.debug( "investSlotIdList: {}", investSlotIdList ) ;
	
		ModelAndView retModelAndView = new ModelAndView( "redirect:/portfolio_profile_page/" + portfolioId ) ;
		
		
		this.portfolioService.removeInvestment( portfolioId, investSlotIdList ) ;
		
		
		LOGGER.debug( "EXIT: removeInvestment ()" ) ;

		return retModelAndView ;
		
	}
	
	
	
	@PostMapping( "/portfolios/create_portfolio" )
	public ModelAndView createPortfolio ( @ModelAttribute Portfolio inputPortfolio ) {
		
		LOGGER.debug( "ENTER: createPortfolio ()" ) ;
		LOGGER.debug( "inputPortfolio: {}", inputPortfolio ) ;
	
		ModelAndView retModelAndView = new ModelAndView( "redirect:/portfolio_listing_page" ) ;
		
			
		List<Account> authenticatedAccountList = this.accountService.findByAuthenticated() ;
		
		if ( authenticatedAccountList.size() == 0 ) {
			
			LOGGER.error( "No authenticated account." );
			LOGGER.debug( "EXIT: createPortfolio ()" ) ;
			
			return retModelAndView ;
			
		}
		
		
		List<Portfolio> createdPortfolioList = this.portfolioService.createPortfolio( inputPortfolio ) ;
		
		if ( createdPortfolioList.size() == 0 ) {
			
			LOGGER.error( "Could not create portfolio: {}", inputPortfolio );
			LOGGER.debug( "EXIT: createPortfolio ()" ) ;
			
			return retModelAndView ;
			
		}
		
		Integer createdPortfolioId = createdPortfolioList.getFirst().getId() ;
		Integer authenticatedAccountId = authenticatedAccountList.getFirst().getId() ;
		
		this.accountService.addPortfolio( authenticatedAccountId, createdPortfolioId ) ;
		
		
		LOGGER.debug( "EXIT: createPortfolio ()" ) ;
		
		return retModelAndView ;
		
	}
	
	@PostMapping( "/portfolios/delete_portfolio" )
	public ModelAndView deletePortfolio ( @RequestParam( name = "portfolio_id_list", 
	                                                     required = false, 
	                                                     defaultValue = "" ) 
	                                      ArrayList<Integer> portfolioIdList ) {
		
		LOGGER.debug( "ENTER: deletePortfolio ()" ) ;
		LOGGER.debug( "portfolioIdList: {}", portfolioIdList ) ;
	
		ModelAndView retModelAndView = new ModelAndView( "redirect:/portfolio_listing_page" ) ;	
		
		
		List<Account> authenticatedAccountList = this.accountService.findByAuthenticated() ;
		
		if ( authenticatedAccountList.size() == 0 ) {
			
			LOGGER.error( "No authenticated account." );
			LOGGER.debug( "EXIT: deletePortfolio ()" ) ;
			
			return retModelAndView ;
			
		}		
		
		Integer authenticatedAccountId = authenticatedAccountList.getFirst().getId() ;
				
		this.accountService.removePortfolio( authenticatedAccountId, portfolioIdList ) ;
		
		
		LOGGER.debug( "EXIT: deletePortfolio ()" ) ;
		
		return retModelAndView ;
		
	}


	
	

}
