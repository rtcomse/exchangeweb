package com.myapp.exchangeweb.services;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myapp.exchangeweb.kafka.services.PriceService;
import com.myapp.exchangeweb.models.Company;
import com.myapp.exchangeweb.models.Quote;
import com.myapp.exchangeweb.repositories.CompanyRepository;


@Service
public class CompanyService {
	
	
	@Autowired
	private CompanyRepository companyRepository ;
	
	@Autowired
	private QuoteService quoteService ;
	@Autowired
	private PriceService priceService ;
	
	
	// LOGGER
	private static final Logger LOGGER = LoggerFactory.getLogger( CompanyService.class ) ;
	
	
	public List<Company> findAll () {
		
		LOGGER.debug( "ENTER: findAll ()" ) ;
		LOGGER.debug( "EXIT: findAll ()" ) ;
				
		return this.companyRepository.findAll() ;
		
	}

	
	public List<Company> findById ( Integer id ) {
		
		LOGGER.debug( "ENTER: findById ()" ) ;
		LOGGER.debug( "id: {}", id ) ;
		
		List<Integer> id_list = new ArrayList<Integer>() ;
		id_list.add( id ) ;
		
		LOGGER.debug( "EXIT: findById ()" ) ;
		
		return this.companyRepository.findAllById( id_list ) ;
		
	}

	
	public List<Company> findAllOrderBySymbolAsc () {
		
		LOGGER.debug( "ENTER: findAllOrderBySymbolAsc ()" ) ;
		LOGGER.debug( "EXIT: findAllOrderBySymbolAsc ()" ) ;
		
		return this.companyRepository.findByOrderBySymbolAsc() ;
		
	}
	
	public List<Company> findBySymbol ( String symbol ) {
		
		LOGGER.debug( "ENTER: findBySymbol ()" ) ;
		LOGGER.debug( "symbol: {}", symbol ) ;
		LOGGER.debug( "EXIT: findBySymbol ()" ) ;
		
		return this.companyRepository.findBySymbolIgnoreCaseOrderBySymbolAsc( symbol ) ;
		
	}
	
	public List<Company> findBySymbolStartsWith ( String symbol ) {
		
		LOGGER.debug( "ENTER: findBySymbolStartsWith ()" ) ;
		LOGGER.debug( "symbol: {}", symbol ) ;
		LOGGER.debug( "EXIT: findBySymbolStartsWith ()" ) ;
		
		return this.companyRepository.findBySymbolStartsWithIgnoreCaseOrderBySymbolAsc( symbol ) ;
		
	}
	
	public List<Company> createCompany ( Company inputCompany, 
			                             Double inputQuotePrice, 
			                             List<Double> inputPriceList ) {
		
		LOGGER.debug( "ENTER: createCompany ()" ) ;
		LOGGER.debug( "inputCompany: {}", inputCompany ) ;
		LOGGER.debug( "inputQuotePrice: {}", inputQuotePrice ) ;
		LOGGER.debug( "inputPriceList: {}", inputPriceList ) ;
		
		List<Company> createdCompanyList = new ArrayList<Company>() ;
		
		
		inputCompany.setId( null ) ;
		
		if ( inputCompany.getName() == null || 
			 inputCompany.getName().length() == 0 ) {
			
			inputCompany.setName( " " ) ;
			
		}
		if ( inputCompany.getSymbol() == null || 
			 inputCompany.getSymbol().length() == 0 || 
			 this.findBySymbol( inputCompany.getSymbol() ).size() != 0 ) {
			
			LOGGER.error( "Problem with inputCompany.getSymbol(): {}" , 
					      inputCompany.getSymbol() ) ;
			LOGGER.debug( "EXIT: createCompany ()" ) ;
			
			return createdCompanyList ;
			
		}
		if ( inputCompany.getDescription() == null || 
			 inputCompany.getDescription().length() == 0 ) {
			
			inputCompany.setDescription( " " ) ;
			
		}	
		
		List<Quote> createdQuoteList = this.quoteService.createQuote( inputQuotePrice ) ;
		
		if ( createdQuoteList.size() == 0 ) {
			
			LOGGER.error( "Could not create a Quote. Problem with inputQuotePrice: {}" , 
					      inputQuotePrice ) ;
		    LOGGER.debug( "EXIT: createCompany ()" ) ;
			
			return createdCompanyList ;
			
		}
		
		Quote createdQuote = createdQuoteList.getFirst() ;
		
		inputCompany.setQuote( createdQuote ) ;
		
		inputCompany = this.companyRepository.save( inputCompany ) ;
		
		createdCompanyList.add( inputCompany ) ;
		
		
		Boolean topicCreateSuccessful = this.priceService.createTopic( inputCompany ) ;
		
		if ( !topicCreateSuccessful ) {
			
			LOGGER.error( "Could not create a Topic. Problem with inputCompany: {}" , 
						  inputCompany ) ;
			LOGGER.debug( "EXIT: createCompany ()" ) ;
			
			return createdCompanyList ;
			
		}
		
		this.priceService.appendByDays( inputCompany , inputPriceList) ;
		
		this.quoteService.updateQuoteData( inputCompany ) ;
		
		
		LOGGER.debug( "EXIT: createCompany ()" ) ;
				
		return createdCompanyList ;
		
	}
	
	public Boolean deleteCompany ( Company inputCompany ) {
		
		LOGGER.debug( "ENTER: deleteCompany ()" ) ;
		LOGGER.debug( "inputCompany: {}", inputCompany ) ;
		
		Boolean retDeleteCompanySuccessful = Boolean.valueOf( false ) ;
		
		
		this.priceService.deleteTopic( inputCompany ) ;
		
		Quote currQuote = inputCompany.getQuote() ;
		
		this.companyRepository.delete( inputCompany ) ;
		
		
		this.quoteService.deleteQuote( currQuote ) ;
		
		
		retDeleteCompanySuccessful = Boolean.valueOf( true ) ;
		
		
		LOGGER.debug( "EXIT: deleteCompany ()" ) ;
		
		return retDeleteCompanySuccessful ;
		

	}
	
	public Boolean deleteCompanyIdList ( List<Integer> inputCompanyIdList ) {
		
		LOGGER.debug( "ENTER: deleteCompanyIdList ()" ) ;
		LOGGER.debug( "inputCompanyIdList: {}", inputCompanyIdList ) ;
		
		Boolean retDeleteCompanyIdListSuccessful = Boolean.valueOf( false ) ;
		
		
		List<Company> companyList = new ArrayList<Company>() ;
		
		for ( int i = 0 ; i < inputCompanyIdList.size() ; i++ ) {
			
			List<Company> matchedCompanyList = this.findById( inputCompanyIdList.get( i ) ) ;
			
			if ( matchedCompanyList.size() != 0 ) {
				
				companyList.add( matchedCompanyList.getFirst() ) ;
				
			}
			
		}
		
		for ( int i = 0 ; i < companyList.size() ; i++ ) {
			
			this.deleteCompany( companyList.get( i ) ) ;
			
		}
		
		
		retDeleteCompanyIdListSuccessful = Boolean.valueOf( true ) ;
		
		
		LOGGER.debug( "EXIT: deleteCompanyIdList ()" ) ;
		
		return retDeleteCompanyIdListSuccessful ;
		

	}
	
	
	public void updateQuoteDataAllCompanies () {
		
		LOGGER.debug( "ENTER: updateQuoteDataAllCompanies ()" ) ;
		
		List<Company> companyList = this.findAll() ;
		
		for ( int i = 0 ; i < companyList.size() ; i++ ) {
			
			this.quoteService.updateQuoteData( companyList.get( i ) ) ;
			
		}
		
		
		LOGGER.debug( "EXIT: updateQuoteDataAllCompanies ()" ) ;
		
	}



}
