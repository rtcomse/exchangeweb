package com.myapp.exchangeweb.page.controllers;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.myapp.exchangeweb.project.ProjectData;


@RestController
public class AboutPageController {
	
	// LOGGER
	private static final Logger LOGGER = LoggerFactory.getLogger( AboutPageController.class ) ;
	
	
	
	@GetMapping( "/about_page" )
	public ModelAndView aboutPage () {
		
		LOGGER.debug( "ENTER: aboutPage ()" ) ;
		
		
		ModelAndView retModelAndView = new ModelAndView( "pages/about_page/about_page.html" ) ;
		
		Map<String, Object> modelMap =  new HashMap<>() ;
		
		modelMap.put( "projectData", ( new ProjectData() ) ) ;
		
		retModelAndView.addAllObjects( modelMap ) ;
		
		
		LOGGER.debug( "EXIT: aboutPage ()" ) ;
		
		return retModelAndView ;
		
	}
	
	

}
