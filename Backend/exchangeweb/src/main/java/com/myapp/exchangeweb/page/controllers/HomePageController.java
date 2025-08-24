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
public class HomePageController {
	
	// LOGGER
	private static final Logger LOGGER = LoggerFactory.getLogger( HomePageController.class ) ;
	
	
	
	@GetMapping( "/home_page" )
	public ModelAndView homePage () {
		
		LOGGER.debug( "ENTER: homePage ()" ) ;
		
		
		ModelAndView retModelAndView = new ModelAndView( "pages/home_page/home_page.html" ) ;
		
		Map<String, Object> modelMap =  new HashMap<>() ;
		
		modelMap.put( "projectData", ( new ProjectData() ) ) ;
		
		retModelAndView.addAllObjects( modelMap ) ;
		
		
		LOGGER.debug( "EXIT: homePage ()" ) ;
		
		return retModelAndView ;
		
	}
	
	@GetMapping( "/" )
	public ModelAndView emptyPage () {
		
		LOGGER.debug( "ENTER: emptyPage ()" ) ;
		
		
		ModelAndView retModelAndView = new ModelAndView( "redirect:/home_page" ) ;
		
		
		LOGGER.debug( "EXIT: emptyPage ()" ) ;
		
		return retModelAndView ;
		
	}


}
