package com.myapp.exchangeweb.kafka.controllers;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.myapp.exchangeweb.kafka.services.PriceService;
import com.myapp.exchangeweb.models.Company;
import com.myapp.exchangeweb.services.CompanyService;


@RestController
public class PriceController {
	
	
	@Autowired
	private PriceService priceService ;
	@Autowired
	private CompanyService companyService ;
	
	
	// LOGGER
	private static final Logger LOGGER = LoggerFactory.getLogger( PriceController.class ) ;
	
	
	
	@GetMapping( "/prices/company/{companyId}/days/{numDays}" )
	public List<Double> findByDays ( @PathVariable Integer companyId, 
			                         @PathVariable Integer numDays ) {
		
		LOGGER.debug( "ENTER: findByDays ()" ) ;
		LOGGER.debug( "companyId: {}", companyId ) ;
		LOGGER.debug( "numDays: {}", numDays ) ;
		
		List<Double> retPriceList = new ArrayList<Double>() ;
		
		List<Company> matchedCompanyList = this.companyService.findById( companyId ) ;
		
		if ( matchedCompanyList.size() == 0 ) {
			
			LOGGER.error( "No company found with given id: {}", companyId );
			LOGGER.debug( "EXIT: findByDays ()" ) ;
			
			return retPriceList ;
			
		}
		
		Company matchedCompany = matchedCompanyList.getFirst() ;
		
		retPriceList = this.priceService.findByDays( matchedCompany, numDays ) ;
		
		
		LOGGER.debug( "EXIT: findByDays ()" ) ;
		
		return retPriceList ;
		
	}

	
	@PostMapping( "/prices/company/{companyId}/append" )
	public Boolean appendByDays ( @PathVariable Integer companyId, 
			                      @RequestBody List<Double> priceList ) {
		
		LOGGER.debug( "ENTER: appendByDays ()" ) ;
		LOGGER.debug( "companyId: {}", companyId ) ;
		LOGGER.debug( "priceList: {}", priceList ) ;
		
		Boolean retSuccessful = Boolean.valueOf( false ) ;
		
		List<Company> matchedCompanyList = this.companyService.findById( companyId ) ;
		
		if ( matchedCompanyList.size() == 0 ) {
			
			LOGGER.error( "No company found with given id: {}", companyId );
			LOGGER.debug( "EXIT: appendByDays ()" ) ;
			
			return retSuccessful ;
			
		}
		
		Company matchedCompany = matchedCompanyList.getFirst() ;

		
		
		retSuccessful = this.priceService.appendByDays( matchedCompany, priceList ) ;
		
		
		LOGGER.debug( "EXIT: appendByDays ()" ) ;
		
		return retSuccessful ;
		
	}


}
