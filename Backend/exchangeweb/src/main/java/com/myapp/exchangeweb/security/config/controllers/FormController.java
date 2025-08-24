package com.myapp.exchangeweb.security.config.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.myapp.exchangeweb.project.ProjectData ;


@RestController
public class FormController {
	
	// LOGGER
	private static final Logger LOGGER = LoggerFactory.getLogger( FormController.class ) ;
	
	
	
	@PostMapping( "/login" )
	public ModelAndView loginFormSubmit () {
		
		LOGGER.debug( "ENTER: loginFormSubmit ()" ) ;
		
		ModelAndView retModelAndView = new ModelAndView( "forward:" + 
		                                                 ( new ProjectData() ).getUrlPrefixServer1() + 
		                                                 "/login" ) ;
		
		LOGGER.debug( "EXIT: loginFormSubmit ()" ) ;
		
		return retModelAndView ;
		
	}


}
