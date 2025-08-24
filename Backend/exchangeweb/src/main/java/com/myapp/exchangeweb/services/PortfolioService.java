package com.myapp.exchangeweb.services;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import com.myapp.exchangeweb.kafka.services.PriceService;
import com.myapp.exchangeweb.models.Company;
import com.myapp.exchangeweb.models.InvestSlot;
import com.myapp.exchangeweb.models.Portfolio;
import com.myapp.exchangeweb.repositories.PortfolioRepository;



@Service
public class PortfolioService {
	

	@Autowired
	private PortfolioRepository portfolioRepository ;
	
	@Autowired
	private PriceService priceService ;
	@Autowired
	private CompanyService companyService ;	
	@Autowired
	private InvestSlotService investSlotService ;
	
	
	// LOGGER
	private static final Logger LOGGER = LoggerFactory.getLogger( PortfolioService.class ) ;
	
	
	
	public List<Portfolio> findAll () {
		
		LOGGER.debug( "ENTER: findAll ()" ) ;
		LOGGER.debug( "EXIT: findAll ()" ) ;
		
		return this.portfolioRepository.findAll() ;
		
	}

	
	public List<Portfolio> findById ( Integer id ) {
		
		LOGGER.debug( "ENTER: findById ()" ) ;
		LOGGER.debug( "id: {}", id ) ;
		
		List<Integer> id_list = new ArrayList<Integer>() ;
		id_list.add( id ) ;
		
		LOGGER.debug( "EXIT: findById ()" ) ;
		
		return this.portfolioRepository.findAllById( id_list ) ;
		
	}

	
	public List<Portfolio> updateSettings ( Portfolio inputPortfolio ) {
		
		LOGGER.debug( "ENTER: updateSettings ()" ) ;
		LOGGER.debug( "inputPortfolio: {}", inputPortfolio ) ;
		
		
		List<Portfolio> updatedPortfolioList = new ArrayList<Portfolio>() ;
		
		List<Integer> id_list = new ArrayList<Integer>() ;
		id_list.add( inputPortfolio.getId() ) ;
		
		List<Portfolio> matchedPortfolioList =  this.portfolioRepository.findAllById( id_list ) ;
		
		if ( matchedPortfolioList.size() != 1 ) {
			
			LOGGER.error( "No portfolio found with given id: {}", inputPortfolio.getId() );
			LOGGER.debug( "EXIT: updateSettings ()" ) ;
			
			return updatedPortfolioList ;
			
		}
		else {
			
			Portfolio matchedPortfolio = matchedPortfolioList.getFirst() ;
			
			boolean updated = false ;
			
			if ( ( inputPortfolio.getName() != null ) && 
				 ( inputPortfolio.getName().length() != 0 ) ) {
				
				matchedPortfolio.setName( inputPortfolio.getName() ) ;
				
				updated = true ;
				
			}

			
			if ( ( inputPortfolio.getDescription() != null ) && 
				 ( inputPortfolio.getDescription().length() != 0 ) ) {
				
				matchedPortfolio.setDescription( inputPortfolio.getDescription() );
				
				updated = true ;
				
			}
			
			if ( ( inputPortfolio.getInvestStartDay() != null ) && 
				 ( inputPortfolio.getInvestStartDay() <= -1 ) ) {
				
				matchedPortfolio.setInvestStartDay( inputPortfolio.getInvestStartDay() ) ;
				
				updated = true ;
				
			}
			
			if ( updated ) {
				
				matchedPortfolio = this.portfolioRepository.save( matchedPortfolio ) ;
				
				updatedPortfolioList.add( matchedPortfolio ) ;
				
				this.updateInvestmentData( matchedPortfolio.getId() ) ;
				
			}
			
			
		}
		
		
		LOGGER.debug( "EXIT: updateSettings ()" ) ;		
		
		return updatedPortfolioList ;
		
	}
	
	

	
	
	public List<Portfolio> addInvestment ( Integer portfolioId, 
			                               Integer companyId, 
			                               Integer companyQuantity ) {
		
		LOGGER.debug( "ENTER: addInvestment ()" ) ;
		LOGGER.debug( "portfolioId: {}", portfolioId ) ;
		LOGGER.debug( "companyId: {}", companyId ) ;
		LOGGER.debug( "companyQuantity: {}", companyQuantity ) ;
		
		List<Portfolio> updatedPortfolioList = new ArrayList<Portfolio>() ;
		
		List<Integer> id_list = new ArrayList<Integer>() ;
		id_list.add( portfolioId ) ;
		
		List<Portfolio> matchedPortfolioList =  this.portfolioRepository.findAllById( id_list ) ;
		List<Company> matchedCompanyList =  this.companyService.findById( companyId ) ;
			
		if ( matchedPortfolioList.size() != 1 || 
			 matchedCompanyList.size() != 1 ) {
			
			LOGGER.error( "Could not find portfolio with id: {}, or could not find company with id: {}", 
					      portfolioId, 
					      companyId );
			LOGGER.debug( "EXIT: addInvestment ()" ) ;
			
			return updatedPortfolioList ;
			
		}
		else {
				
			Portfolio matchedPortfolio = matchedPortfolioList.getFirst() ;
			Company matchedCompany = matchedCompanyList.getFirst() ;
						
			InvestSlot investSlot = new InvestSlot() ;
			investSlot.setId( null ) ;
			investSlot.setCompany( matchedCompany ) ;
			investSlot.setInvestQuantity( companyQuantity ) ;
			investSlot.setInvestPriceStart( BigDecimal.valueOf( 0.00 ) ) ;
			investSlot.setInvestPriceEnd( BigDecimal.valueOf( 0.00 ) ) ;
			investSlot.setInvestValueStart( BigDecimal.valueOf( 0.00 ) ) ;
			investSlot.setInvestValueEnd( BigDecimal.valueOf( 0.00 ) ) ;
			investSlot.setChangeInvestValueAbsolute( BigDecimal.valueOf( 0.00 ) ) ;
			investSlot.setChangeInvestValuePercent( BigDecimal.valueOf( 0.00 ) ) ;
			
			investSlot = this.investSlotService.save( investSlot ) ;
						
			matchedPortfolio.getInvestSlots().add( investSlot ) ;

			
			matchedPortfolio = this.portfolioRepository.save( matchedPortfolio ) ;
			
			updatedPortfolioList.add( matchedPortfolio ) ;
			
			this.updateInvestmentData( matchedPortfolio.getId() ) ;
			
			
		}
		
		
		LOGGER.debug( "EXIT: addInvestment ()" ) ;
	
		return updatedPortfolioList ;
		
	}
	
	
	public List<Portfolio> removeInvestment ( Integer portfolioId, 
			                                  List<Integer> investSlotIdList ) {
		
		LOGGER.debug( "ENTER: removeInvestment ()" ) ;
		LOGGER.debug( "portfolioId: {}", portfolioId ) ;
		LOGGER.debug( "investSlotIdList: {}", investSlotIdList ) ;
		
		List<Portfolio> updatedPortfolioList = new ArrayList<Portfolio>() ;
		
		List<Integer> id_list = new ArrayList<Integer>() ;
		id_list.add( portfolioId ) ;
		
		List<Portfolio> matchedPortfolioList =  this.portfolioRepository.findAllById( id_list ) ;
			
		if ( matchedPortfolioList.size() != 1 ) {
			
			LOGGER.error( "No portfolios with given id: {}", portfolioId );
			LOGGER.debug( "EXIT: removeInvestment ()" ) ;
			
			return updatedPortfolioList ;
			
		}
		else {
				
			Portfolio matchedPortfolio = matchedPortfolioList.getFirst() ;
			
			Map<Integer, Integer> investSlotIdMap = new HashMap<Integer, Integer>() ;
			
			for ( int i = 0 ; i < investSlotIdList.size() ; i++ ) {
				
				investSlotIdMap.put( investSlotIdList.get( i ), investSlotIdList.get( i ) ) ;
				
			}
			
			List<InvestSlot> matchedPortfolioInvestSlots = matchedPortfolio.getInvestSlots() ;
			List<Integer> removeInvestSlotIdList = new ArrayList<Integer>() ;
			
			for ( int i = 0 ; i < matchedPortfolioInvestSlots.size() ; i++ ) {
				
				InvestSlot currInvestSlot = matchedPortfolioInvestSlots.get( i ) ;
				
				if ( investSlotIdMap.containsKey( currInvestSlot.getId() ) ) {
					
					matchedPortfolioInvestSlots.remove( i ) ;
					removeInvestSlotIdList.add( currInvestSlot.getId() ) ;
					i-- ;
					
				}
				
			}
			
			
			matchedPortfolio = this.portfolioRepository.save( matchedPortfolio ) ;
			
			this.investSlotService.deleteAllByIdList( removeInvestSlotIdList ) ;
			
			
			updatedPortfolioList.add( matchedPortfolio ) ;
			
			this.updateInvestmentData( matchedPortfolio.getId() ) ;
			
			
		}
		
		
		LOGGER.debug( "EXIT: removeInvestment ()" ) ;
	
		return updatedPortfolioList ;
		
	}
	
	
	
	
	public List<Portfolio> updateInvestmentData ( Integer id ) {
		
		LOGGER.debug( "ENTER: updateInvestmentData ()" ) ;
		LOGGER.debug( "id: {}", id ) ;
		
		List<Portfolio> updatedPortfolioList = new ArrayList<Portfolio>() ;
		
		List<Integer> id_list = new ArrayList<Integer>() ;
		id_list.add( id ) ;
		
		List<Portfolio> matchedPortfolioList =  this.portfolioRepository.findAllById( id_list ) ;
		
		if ( matchedPortfolioList.size() != 1 ) {
			
			LOGGER.error( "No portfolio with given id: {}", id ) ;
			LOGGER.debug( "EXIT: updateInvestmentData ()" ) ;
			
			return updatedPortfolioList ;
			
		}
		
		Portfolio matchedPortfolio = matchedPortfolioList.getFirst() ;	

		
		List<BigDecimal> investmentValueStartList = new ArrayList<BigDecimal>() ;
		List<BigDecimal> investmentValueEndList = new ArrayList<BigDecimal>() ;
		
		for ( int i = 0 ; i < matchedPortfolio.getInvestSlots().size() ; i++ ) {
			
			InvestSlot currInvestSlot = matchedPortfolio.getInvestSlots().get( i ) ;
			
			double currQuotePrice = currInvestSlot.getCompany().getQuote().getCurrPrice().doubleValue() ;
			
			
			int fullPriceListSize = ( matchedPortfolio.getInvestStartDay().intValue() * ( -1 ) ) + 1 ;			
			
			List<Double> fullPriceList = new ArrayList<Double>() ;
			
			for ( int j = 0 ; j < fullPriceListSize ; j++ ) {
				
				fullPriceList.add( Double.valueOf( 0.00 ) ) ;
				
			}
			
			fullPriceList.set( fullPriceList.size() - 1 , currQuotePrice ) ;
			
			
			
			List<Double> retrievedPriceList = this.priceService.findByDays( currInvestSlot.getCompany(), 
					                                                        ( -1 ) * matchedPortfolio.getInvestStartDay() ) ;
			
		
			
			for ( int j = retrievedPriceList.size() - 1 ; j >= 0 ; j-- ) {
				
				fullPriceList.set(  ( fullPriceListSize - 2 ) - ( ( retrievedPriceList.size() - 1 ) - j ), 
						            retrievedPriceList.get( j ) ) ;
				
			}
			
			
			List<Double> fullInvestmentValueList = new ArrayList<Double>() ;
			
			for ( int j = 0 ; j < fullPriceList.size() ; j++ ) {
				
				fullInvestmentValueList.add( ( fullPriceList.get( j ).doubleValue() ) * 
						                     ( currInvestSlot.getInvestQuantity().intValue() ) ) ;
				
			}
			
			BigDecimal currInvestPriceStart = BigDecimal.valueOf( fullPriceList.get( 0 ) ) 
					                                    .setScale( 2, RoundingMode.HALF_UP ) ;
			                                            
			BigDecimal currInvestPriceEnd = BigDecimal.valueOf( fullPriceList.get( fullPriceList.size() - 1 ) ) 
					                                  .setScale( 2, RoundingMode.HALF_UP ) ;
			
			BigDecimal currInvestValueStart = BigDecimal.valueOf( fullInvestmentValueList.get( 0 ) ) 
					                                    .setScale( 2, RoundingMode.HALF_UP );
			BigDecimal currInvestValueEnd = BigDecimal.valueOf( fullInvestmentValueList.get( fullInvestmentValueList.size() - 1 ) ) 
					                                  .setScale( 2, RoundingMode.HALF_UP ) ;
			
			BigDecimal currChangeInvestValueAbsolute = currInvestValueEnd.subtract( currInvestValueStart, 
					                                                                MathContext.UNLIMITED )
					                                                     .setScale( 2, RoundingMode.HALF_UP ) ;
			
			BigDecimal currChangeInvestValuePercent = ( currInvestValueStart.compareTo( BigDecimal.valueOf( 0.00 ) ) == 0 ) ?
					                                  currChangeInvestValueAbsolute.divide( BigDecimal.valueOf( 0.01 ),
					                                		                                4, 
					                                		                                RoundingMode.HALF_UP )
					                                                               .multiply( BigDecimal.valueOf( 100 ), 
					                                                            		      MathContext.UNLIMITED )
					                                                               .setScale( 2, RoundingMode.HALF_UP ) :
					                                  currChangeInvestValueAbsolute.divide( currInvestValueStart,
					                                		                                4, 
                      		                                                                RoundingMode.HALF_UP )
						                                                           .multiply( BigDecimal.valueOf( 100 ),
						                                                        		      MathContext.UNLIMITED ) 
						                                                           .setScale( 2, RoundingMode.HALF_UP ) ;
			
			
			currInvestSlot.setInvestPriceStart( currInvestPriceStart ) ;
			currInvestSlot.setInvestPriceEnd( currInvestPriceEnd ) ;
			currInvestSlot.setInvestValueStart( currInvestValueStart ) ;
			currInvestSlot.setInvestValueEnd( currInvestValueEnd ) ;
			currInvestSlot.setChangeInvestValueAbsolute( currChangeInvestValueAbsolute ) ;
			currInvestSlot.setChangeInvestValuePercent( currChangeInvestValuePercent ) ;
			
			this.investSlotService.save( currInvestSlot ) ;
			
			
			investmentValueStartList.add( currInvestValueStart ) ;
			investmentValueEndList.add( currInvestValueEnd ) ;			
			
		}
		
		BigDecimal sumInvestValueStart = BigDecimal.valueOf( 0.00 )
				                                   .setScale( 2, RoundingMode.HALF_UP ) ;
		BigDecimal sumInvestValueEnd = BigDecimal.valueOf( 0.00 )
				                                 .setScale( 2, RoundingMode.HALF_UP ) ;
		
		for ( int i = 0 ; i < matchedPortfolio.getInvestSlots().size() ; i++ ) {
			
			sumInvestValueStart = sumInvestValueStart.add( investmentValueStartList.get( i ), 
					                                       MathContext.UNLIMITED )
			                                         .setScale( 2, RoundingMode.HALF_UP ) ;
			sumInvestValueEnd = sumInvestValueEnd.add( investmentValueEndList.get( i ),
					                                   MathContext.UNLIMITED ) 
			                                     .setScale( 2, RoundingMode.HALF_UP ) ;
			
		}
		
		
		matchedPortfolio.setInvestValueStart( sumInvestValueStart ) ;
		matchedPortfolio.setInvestValueEnd( sumInvestValueEnd ) ;
		
		BigDecimal portfolioChangeInvestValueAbsolute = sumInvestValueEnd.subtract( sumInvestValueStart, 
				                                                                    MathContext.UNLIMITED )
				                                                         .setScale( 2, RoundingMode.HALF_UP ) ;
		
		BigDecimal portfolioChangeInvestValuePercent = ( sumInvestValueStart.compareTo( BigDecimal.valueOf( 0.00 ) ) == 0 ) ?
				                                       portfolioChangeInvestValueAbsolute.divide( BigDecimal.valueOf( 0.01 ),
				                                    		                                      4, 
	                                                                                              RoundingMode.HALF_UP )
				                                                                         .multiply( BigDecimal.valueOf( 100 ),
				                                                                        		    MathContext.UNLIMITED )
				                                                                         .setScale( 2, RoundingMode.HALF_UP ) :
				                                       portfolioChangeInvestValueAbsolute.divide( sumInvestValueStart,
				                                    		                                      4, 
	                                                                                              RoundingMode.HALF_UP )
					                                                                     .multiply( BigDecimal.valueOf( 100 ),
					                                                                    		    MathContext.UNLIMITED )
					                                                                     .setScale( 2, RoundingMode.HALF_UP ) ;
		
		matchedPortfolio.setChangeInvestValueAbsolute( portfolioChangeInvestValueAbsolute ) ;
		
		matchedPortfolio.setChangeInvestValuePercent( portfolioChangeInvestValuePercent ) ;

		
		
		matchedPortfolio = this.portfolioRepository.save( matchedPortfolio ) ;
		updatedPortfolioList.add( matchedPortfolio ) ;
		
		
		LOGGER.debug( "EXIT: updateInvestmentData ()" ) ;
		
		return updatedPortfolioList ;
		
	}
	
	
	
	public List<Portfolio> createPortfolio ( Portfolio inputPortfolio ) {
		
		LOGGER.debug( "ENTER: createPortfolio ()" ) ;
		LOGGER.debug( "inputPortfolio: {}", inputPortfolio ) ;
		
		List<Portfolio> createdPortfolioList = new ArrayList<Portfolio>() ;
		
		
		inputPortfolio.setId( null ) ;
		
		if ( inputPortfolio.getName() == null || 
			 inputPortfolio.getName().length() == 0 ) {
			
			inputPortfolio.setName( " " ) ;
			
		}
		if ( inputPortfolio.getDescription() == null || 
			 inputPortfolio.getDescription().length() == 0 ) {
			
			inputPortfolio.setDescription( " " ) ;
			
		}
		if ( inputPortfolio.getInvestStartDay() == null || 
			 inputPortfolio.getInvestStartDay() >= -1 ) {
			
			inputPortfolio.setInvestStartDay( Integer.valueOf( -1 ) ) ;
			
		}
		if ( inputPortfolio.getInvestValueStart() == null ) {
			
			inputPortfolio.setInvestValueStart( BigDecimal.valueOf( 0.00 ) ) ;
			
		}
		if ( inputPortfolio.getInvestValueEnd() == null ) {
			
			inputPortfolio.setInvestValueEnd( BigDecimal.valueOf( 0.00 ) ) ;
			
		}
		if ( inputPortfolio.getChangeInvestValueAbsolute() == null ) {
			
			inputPortfolio.setChangeInvestValueAbsolute( BigDecimal.valueOf( 0.00 ) ) ;
			
		}
		if ( inputPortfolio.getChangeInvestValuePercent() == null ) {
			
			inputPortfolio.setChangeInvestValuePercent( BigDecimal.valueOf( 0.00 ) ) ;
			
		}
		
		
		
		Portfolio createdPortfolio = this.portfolioRepository.save( inputPortfolio ) ;
		
		createdPortfolioList.add( createdPortfolio ) ;
		
		
		LOGGER.debug( "EXIT: createPortfolio ()" ) ;
		
		return createdPortfolioList ;
		
	}
	
	
	
	public Boolean deletePortfolio ( Portfolio inputPortfolio ) {
		
		LOGGER.debug( "ENTER: deletePortfolio ()" ) ;
		LOGGER.debug( "inputPortfolio: {}", inputPortfolio ) ;
		
		Boolean retDeletePortfolioSuccessful = Boolean.valueOf( false ) ;
		
		List<Integer> investSlotIdList = new ArrayList<Integer>() ;
		
		for ( int i = 0 ; i < inputPortfolio.getInvestSlots().size() ; i++ ) {
			
			investSlotIdList.add( inputPortfolio.getInvestSlots().get( i ).getId() ) ;
			
		}
		
		this.removeInvestment( inputPortfolio.getId() , investSlotIdList ) ;
		
		
		this.portfolioRepository.delete( inputPortfolio ) ;
		
		
		retDeletePortfolioSuccessful = Boolean.valueOf( true ) ;
		
		
		LOGGER.debug( "EXIT: deletePortfolio ()" ) ;
		
		return retDeletePortfolioSuccessful ;
		
	}

	
	
	@EventListener( ApplicationReadyEvent.class )
	public void updateInvestmentDataAllPortfolios () {
		
		LOGGER.debug( "ENTER: updateInvestmentDataAllPortfolios ()" ) ;
		
		this.companyService.updateQuoteDataAllCompanies() ;
		
		List<Portfolio> portfolioList = this.findAll() ;
		
		for ( int i = 0 ; i < portfolioList.size() ; i++ ) {
			
			this.updateInvestmentData( portfolioList.get( i ).getId() ) ;
			
		}
		
		
		LOGGER.debug( "EXIT: updateInvestmentDataAllPortfolios ()" ) ;
		
	}

	
	
	
}
