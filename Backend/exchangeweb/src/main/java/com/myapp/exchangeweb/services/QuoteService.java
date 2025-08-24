package com.myapp.exchangeweb.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myapp.exchangeweb.kafka.services.PriceService;
import com.myapp.exchangeweb.models.Company;
import com.myapp.exchangeweb.models.Quote;
import com.myapp.exchangeweb.repositories.QuoteRepository;



@Service
public class QuoteService {
	
		
	@Autowired
	private QuoteRepository quoteRepository ;
	
	@Autowired
	private PriceService priceService ;
	
	
	// LOGGER
	private static final Logger LOGGER = LoggerFactory.getLogger( QuoteService.class ) ;
	
	
	public List<Quote> findAll () {
		
		LOGGER.debug( "ENTER: findAll ()" ) ;
		LOGGER.debug( "EXIT: findAll ()" ) ;
		
		return this.quoteRepository.findAll() ;
		
	}

	
	public List<Quote> findById ( Integer id ) {
		
		LOGGER.debug( "ENTER: findById ()" ) ;
		LOGGER.debug( "id: {}", id ) ;
		
		List<Integer> id_list = new ArrayList<Integer>() ;
		id_list.add( id ) ;
		
		LOGGER.debug( "EXIT: findById ()" ) ;
		
		return this.quoteRepository.findAllById( id_list ) ;
		
	}
	
	
	public List<Quote> createQuote ( Double currPrice ) {
		
		LOGGER.debug( "ENTER: createQuote ()" ) ;
		LOGGER.debug( "currPrice: {}", currPrice ) ;
		
		List<Quote> createdQuoteList = new ArrayList<Quote>() ;
		
		Quote currQuote = new Quote() ;
		currQuote.setId( null ) ;
		currQuote.setCurrPrice( BigDecimal.valueOf( currPrice )
				                          .setScale( 2, RoundingMode.HALF_UP ) );
		
		currQuote.setLastClosePrice( BigDecimal.valueOf( 0.00 )
				                               .setScale( 2, RoundingMode.HALF_UP ) );
		currQuote.setChangePriceAbsolute( BigDecimal.valueOf( 0.00 )
				                                    .setScale( 2, RoundingMode.HALF_UP ) );
		currQuote.setChangePricePercent( BigDecimal.valueOf( 0.00 )
				                                   .setScale( 2, RoundingMode.HALF_UP ) );
		currQuote.setOneYearHighPrice( BigDecimal.valueOf( 0.00 )
				                                 .setScale( 2, RoundingMode.HALF_UP ) );
		currQuote.setOneYearLowPrice( BigDecimal.valueOf( 0.00 )
				                                .setScale( 2, RoundingMode.HALF_UP ) );
		
		Quote createdQuote = this.quoteRepository.save( currQuote ) ;
		
		createdQuoteList.add( createdQuote ) ;
		
		
		LOGGER.debug( "EXIT: createQuote ()" ) ;
		
		return createdQuoteList ;
		
	}
	
	public Boolean deleteQuote ( Quote inputQuote ) {
		
		LOGGER.debug( "ENTER: deleteQuote ()" ) ;
		LOGGER.debug( "inputQuote: {}", inputQuote ) ;
		
		Boolean retDeleteQuoteSuccessful = Boolean.valueOf( false ) ;
		
		this.quoteRepository.delete( inputQuote ) ;
		
		
		retDeleteQuoteSuccessful = Boolean.valueOf( true ) ;
		
		
		LOGGER.debug( "EXIT: deleteQuote ()" ) ;
		
		return retDeleteQuoteSuccessful ;
		
	}
	
	
	public List<Quote> updateQuoteData ( Company inputCompany ) {
		
		LOGGER.debug( "ENTER: updateQuoteData ()" ) ;
		LOGGER.debug( "inputCompany: {}", inputCompany ) ;
		
		List<Quote> updatedQuoteList = new ArrayList<Quote>() ;
		
		
		Quote currQuote = inputCompany.getQuote() ;
		
		List<Double> priceList = this.priceService.findByDays( inputCompany, 
				                                               Integer.valueOf( 365 ) ) ;
		
		Double lastClosePrice = 0.00 ;
		
		if ( priceList.size() > 0 ) {
			
			lastClosePrice = priceList.get( priceList.size() - 1 ) ;
			
		}
		
		currQuote.setLastClosePrice( BigDecimal.valueOf( lastClosePrice )
				                               .setScale( 2, RoundingMode.HALF_UP ) ) ;
		
		
		Double changePriceAbsolute = currQuote.getCurrPrice().doubleValue() - lastClosePrice ;
		
		currQuote.setChangePriceAbsolute( BigDecimal.valueOf( changePriceAbsolute )
				                                    .setScale( 2, RoundingMode.HALF_UP ) ) ;
		
		
		Double changePricePercent = 0.00 ;
		
		if ( lastClosePrice == 0.00 ) {
			
			changePricePercent = ( changePriceAbsolute / 0.01 ) * 100  ;
			
		}
		else {
			
			changePricePercent = ( changePriceAbsolute / lastClosePrice ) * 100  ;
			
		}
				
		currQuote.setChangePricePercent( BigDecimal.valueOf( changePricePercent )
				                         .setScale( 2, RoundingMode.HALF_UP ) ) ;
		
		
		Double oneYearHighPrice = 0.00 ;
		
		for ( int i = 0 ; i < priceList.size() ; i++ ) {
			
			if ( priceList.get( i ) > oneYearHighPrice ) {
				
				oneYearHighPrice = priceList.get( i ) ;
				
			}
			
		}
		
		if ( currQuote.getCurrPrice().doubleValue() > oneYearHighPrice ) {
			
			oneYearHighPrice = currQuote.getCurrPrice().doubleValue() ;
			
		}
		
		currQuote.setOneYearHighPrice( BigDecimal.valueOf( oneYearHighPrice )
				                                 .setScale( 2, RoundingMode.HALF_UP ) ) ;
		
		
		Double oneYearLowPrice = oneYearHighPrice ;
		
		for ( int i = 0 ; i < priceList.size() ; i++ ) {
			
			if ( priceList.get( i ) < oneYearLowPrice ) {
				
				oneYearLowPrice = priceList.get( i ) ;
				
			}
			
		}
		
		if ( currQuote.getCurrPrice().doubleValue() < oneYearLowPrice ) {
			
			oneYearLowPrice = currQuote.getCurrPrice().doubleValue() ;
			
		}
		
		currQuote.setOneYearLowPrice( BigDecimal.valueOf( oneYearLowPrice )
				                                .setScale( 2, RoundingMode.HALF_UP ) ) ;
		
		
		currQuote = this.quoteRepository.save( currQuote ) ;
		
		
		updatedQuoteList.add( currQuote ) ;
		
		
		LOGGER.debug( "EXIT: updateQuoteData ()" ) ;
		
		return updatedQuoteList ;
		
	}


}
