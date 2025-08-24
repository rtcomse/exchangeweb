package com.myapp.exchangeweb.controllers;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.myapp.exchangeweb.models.Quote;
import com.myapp.exchangeweb.services.QuoteService;


@RestController
public class QuoteController {
	
	@Autowired
	private QuoteService quoteService ;
	
	
	// LOGGER
	private static final Logger LOGGER = LoggerFactory.getLogger( QuoteController.class ) ;
	
	
	
	@GetMapping( "/quotes" )
	public List<Quote> findAll () {
		
		LOGGER.debug( "ENTER: findAll ()" ) ;
		
		List<Quote> retQuoteList = new ArrayList<Quote>() ;
		
		
		retQuoteList = this.quoteService.findAll() ;
		
		
		LOGGER.debug( "EXIT: findAll ()" ) ;
		
		return retQuoteList ;
		
	}
	
	
	@GetMapping( "/quotes/{quoteId}" )
	public List<Quote> findById ( @PathVariable Integer quoteId ) {
		
		LOGGER.debug( "ENTER: findById ()" ) ;
		LOGGER.debug( "quoteId: {}", quoteId ) ;
		
		List<Quote> retQuoteList = new ArrayList<Quote>() ;
		
		retQuoteList = this.quoteService.findById( quoteId ) ;
		
		
		LOGGER.debug( "EXIT: findById ()" ) ;
		
		return retQuoteList ;
		
	}


}
