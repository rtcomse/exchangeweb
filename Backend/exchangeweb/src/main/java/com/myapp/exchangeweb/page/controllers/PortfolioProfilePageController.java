package com.myapp.exchangeweb.page.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.myapp.exchangeweb.models.Company;
import com.myapp.exchangeweb.models.Portfolio;
import com.myapp.exchangeweb.project.ProjectData;
import com.myapp.exchangeweb.services.CompanyService;
import com.myapp.exchangeweb.services.PortfolioService;

@RestController
public class PortfolioProfilePageController {

	
	@Autowired
	private PortfolioService portfolioService ;
	@Autowired
	private CompanyService companyService ;
	
	// LOGGER
	private static final Logger LOGGER = LoggerFactory.getLogger( PortfolioProfilePageController.class ) ;
	
	
	@GetMapping( "/portfolio_profile_page/{portfolioId}" )
	public ModelAndView portfolioProfilePage ( @PathVariable Integer portfolioId ) {
		
		LOGGER.debug( "ENTER: portfolioProfilePage ()" ) ;
		LOGGER.debug( "portfolioId: {}", portfolioId ) ;
		
		ModelAndView retModelAndView = new ModelAndView( "pages/portfolio_profile_page/portfolio_profile_page.html" ) ;
		
		List<Portfolio> portfolioList = this.portfolioService.findById( portfolioId ) ;
		
		List<Company> companyList = this.companyService.findAllOrderBySymbolAsc() ;
		
		Map<String, Object> modelMap =  new HashMap<>() ;
				
		modelMap.put( "projectData", ( new ProjectData() ) ) ;
		modelMap.put( "portfolioList", portfolioList ) ;
		modelMap.put( "companyList", companyList ) ;
		
		retModelAndView.addAllObjects( modelMap ) ;
		
		
		LOGGER.debug( "EXIT: portfolioProfilePage ()" ) ;
		
		return retModelAndView ;
		
	}

	
	
}
