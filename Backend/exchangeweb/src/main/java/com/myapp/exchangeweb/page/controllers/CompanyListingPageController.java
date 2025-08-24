package com.myapp.exchangeweb.page.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.myapp.exchangeweb.models.Company;
import com.myapp.exchangeweb.project.ProjectData;
import com.myapp.exchangeweb.services.CompanyService;



@RestController
public class CompanyListingPageController {
	
	
	@Autowired
	private CompanyService companyService ;
	
	// LOGGER
	private static final Logger LOGGER = LoggerFactory.getLogger( CompanyListingPageController.class ) ;

		
	@GetMapping( "/company_listing_page" )
	public ModelAndView companyListingPage () {
		
		LOGGER.debug( "ENTER: companyListingPage ()" ) ;
		
		ModelAndView retModelAndView = new ModelAndView( "pages/company_listing_page/company_listing_page.html" ) ;
		
		
		List<Company> companyList = new ArrayList<Company>() ;
		
		companyList = this.companyService.findAll() ;
		
		
		Map<String, Object> modelMap = new HashMap<>() ;
		
		modelMap.put( "projectData", ( new ProjectData() ) ) ;
		modelMap.put( "companyList", companyList ) ;
		
		retModelAndView.addAllObjects( modelMap ) ;
		
		
		LOGGER.debug( "EXIT: companyListingPage ()" ) ;
		
		return retModelAndView ;
		
	}

	
	
}
