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
import com.myapp.exchangeweb.project.ProjectData;
import com.myapp.exchangeweb.services.CompanyService;

@RestController
public class CompanyProfilePageController {
	
	
	@Autowired
	private CompanyService companyService ;
	
	// LOGGER
	private static final Logger LOGGER = LoggerFactory.getLogger( CompanyProfilePageController.class ) ;

	
	
	@GetMapping( "/company_profile_page/{companyId}" )
	public ModelAndView companyProfilePage ( @PathVariable Integer companyId ) {
		
		LOGGER.debug( "ENTER: companyProfilePage ()" ) ;
		LOGGER.debug( "companyId: {}", companyId ) ;
		
		ModelAndView retModelAndView = new ModelAndView( "pages/company_profile_page/company_profile_page.html" ) ;
		
		List<Company> companyList = this.companyService.findById( companyId ) ;
		
		Map<String, Object> modelMap =  new HashMap<>() ;
		
		modelMap.put( "projectData", ( new ProjectData() ) ) ;
		modelMap.put( "companyList", companyList ) ;
		
		retModelAndView.addAllObjects( modelMap ) ;
		
		
		LOGGER.debug( "EXIT: companyProfilePage ()" ) ;
		
		return retModelAndView ;
		
	}


}
