package com.myapp.exchangeweb.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.myapp.exchangeweb.models.Company;
import com.myapp.exchangeweb.models.InvestSlot;
import com.myapp.exchangeweb.models.Portfolio;
import com.myapp.exchangeweb.services.CompanyService;
import com.myapp.exchangeweb.services.PortfolioService;


@RestController
public class CompanyController {
	
	
	@Autowired
	private CompanyService companyService ;
	@Autowired
	private PortfolioService portfolioService ;
	
	
	// LOGGER
	private static final Logger LOGGER = LoggerFactory.getLogger( CompanyController.class ) ;
	

	
	@GetMapping( "/companies" )
	public List<Company> findAll () {
		
		LOGGER.debug( "ENTER: findAll ()" ) ;
		
		List<Company> retCompanyList = new ArrayList<Company>() ;
		
		
		retCompanyList = this.companyService.findAll() ;
		
		
		LOGGER.debug( "EXIT: findAll ()" ) ;
		
		return retCompanyList ;
		
	}
	
	
	@GetMapping( "/companies/{companyId}" )
	public List<Company> findById ( @PathVariable Integer companyId ) {
		
		LOGGER.debug( "ENTER: findById ()" ) ;
		LOGGER.debug( "companyId: {}", companyId ) ;
		
		List<Company> retCompanyList = new ArrayList<Company>() ;
		
		retCompanyList = this.companyService.findById( companyId ) ;
		
		
		LOGGER.debug( "EXIT: findById ()" ) ;
		
		return retCompanyList ;
		
	}
	
	@GetMapping( "/companies/search/symbol/{companySymbol}" )
	public List<Company> findBySymbol ( @PathVariable String companySymbol ) {
		
		LOGGER.debug( "ENTER: findBySymbol ()" ) ;
		LOGGER.debug( "companySymbol: {}", companySymbol ) ;
		
		List<Company> retCompanyList = new ArrayList<Company>() ;
		
		retCompanyList = this.companyService.findBySymbol( companySymbol ) ;
		
		
		LOGGER.debug( "EXIT: findBySymbol ()" ) ;
		
		return retCompanyList ;
		
	}
	
	@GetMapping( "/companies/search/symbol/starts_with/{companySymbol}" )
	public List<Company> findBySymbolStartsWith ( @PathVariable String companySymbol ) {
		
		LOGGER.debug( "ENTER: findBySymbolStartsWith ()" ) ;
		LOGGER.debug( "companySymbol: {}", companySymbol ) ;
		
		List<Company> retCompanyList = new ArrayList<Company>() ;
		
		retCompanyList = this.companyService.findBySymbolStartsWith( companySymbol ) ;
		
		
		LOGGER.debug( "EXIT: findBySymbolStartsWith ()" ) ;
		
		return retCompanyList ;
		
	}
	
	
	@PostMapping( "/companies/create_company" )
	public ModelAndView createCompany ( @ModelAttribute Company inputCompany, 
			                            @RequestParam( "quote_price" ) Double inputQuotePrice, 
			                            @RequestParam( "price_list" ) List<Double> inputPriceList ) {
		
		LOGGER.debug( "ENTER: createCompany ()" ) ;
		LOGGER.debug( "inputCompany: {}", inputCompany ) ;
		LOGGER.debug( "inputQuotePrice: {}", inputQuotePrice ) ;
		LOGGER.debug( "inputPriceList: {}", inputPriceList ) ;
	
		ModelAndView retModelAndView = new ModelAndView( "redirect:/company_listing_page" ) ;
		
		List<Company> createdCompanyList = this.companyService.createCompany( inputCompany, 
				                                                              inputQuotePrice, 
				                                                              inputPriceList ) ;
		
		if ( createdCompanyList.size() == 0 ) {
			
			LOGGER.error( "Could not create company: {}", inputCompany );
			LOGGER.debug( "EXIT: createCompany ()" ) ;
			
			return retModelAndView ;
			
		}
		
		
		LOGGER.debug( "EXIT: createCompany ()" ) ;
		
		return retModelAndView ;
		
	}
	
	@PostMapping( "/companies/delete_company" )
	public ModelAndView deleteCompany ( @RequestParam( name = "company_id_list", 
													   required = false ,
													   defaultValue = "" ) 
	                                    ArrayList<Integer> companyIdList ) {
		
		LOGGER.debug( "ENTER: deleteCompany ()" ) ;
		LOGGER.debug( "companyIdList: {}", companyIdList ) ;
	
		ModelAndView retModelAndView = new ModelAndView( "redirect:/company_listing_page" ) ;	
		
		Map<Integer, Integer> companyIdMap = new HashMap<Integer, Integer>() ;
		
		for ( int i = 0 ; i < companyIdList.size() ; i++ ) {
			
			companyIdMap.put( companyIdList.get( i ), companyIdList.get( i ) ) ;
			
		}
		
		
		List<Portfolio> portfolioList = this.portfolioService.findAll() ;
		
		for ( int i = 0 ; i < portfolioList.size() ; i++ ) {
			
			Portfolio currPortfolio = portfolioList.get( i ) ;
			List<InvestSlot> currInvestSlotList = currPortfolio.getInvestSlots() ;
			
			List<Integer> removeInvestSlotIdList = new ArrayList<Integer>() ;
			
			for ( int j = 0 ; j < currInvestSlotList.size() ; j++ ) {
				
				InvestSlot currInvestSlot = currInvestSlotList.get( j ) ;
				
				if ( companyIdMap.containsKey( currInvestSlot.getCompany().getId() ) ) {
					
					removeInvestSlotIdList.add( currInvestSlot.getId() ) ;
					
				}
				
			}
			
			this.portfolioService.removeInvestment( currPortfolio.getId(), removeInvestSlotIdList ) ;
			
		}
		
		
		
		this.companyService.deleteCompanyIdList( companyIdList ) ;
		
		
		LOGGER.debug( "EXIT: deleteCompany ()" ) ;
		
		return retModelAndView ;
		
	}




}
