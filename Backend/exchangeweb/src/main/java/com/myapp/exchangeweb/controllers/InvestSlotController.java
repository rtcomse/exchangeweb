package com.myapp.exchangeweb.controllers;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.myapp.exchangeweb.models.InvestSlot;
import com.myapp.exchangeweb.services.InvestSlotService;


@RestController
public class InvestSlotController {
	
	
	@Autowired
	private InvestSlotService investSlotService ;
	
	
	// LOGGER
	private static final Logger LOGGER = LoggerFactory.getLogger( InvestSlotController.class ) ;
	
	
	
	@GetMapping( "/invest_slots" )
	public List<InvestSlot> findAll () {
		
		LOGGER.debug( "ENTER: findAll ()" ) ;
		
		List<InvestSlot> retInvestSlotList = new ArrayList<InvestSlot>() ;
		
		
		retInvestSlotList = this.investSlotService.findAll() ;
		
		
		LOGGER.debug( "EXIT: findAll ()" ) ;
		
		return retInvestSlotList ;
		
	}
	
	
	@GetMapping( "/invest_slots/{investSlotId}" )
	public List<InvestSlot> findById ( @PathVariable Integer investSlotId ) {
		
		LOGGER.debug( "ENTER: findById ()" ) ;
		LOGGER.debug( "investSlotId: {}", investSlotId ) ;
		
		List<InvestSlot> retInvestSlotList = new ArrayList<InvestSlot>() ;
		
		retInvestSlotList = this.investSlotService.findById( investSlotId ) ;
		
		
		LOGGER.debug( "EXIT: findById ()" ) ;
		
		return retInvestSlotList ;
		
	}



}
