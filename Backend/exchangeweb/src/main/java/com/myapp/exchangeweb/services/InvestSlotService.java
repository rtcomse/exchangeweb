package com.myapp.exchangeweb.services;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myapp.exchangeweb.models.InvestSlot;
import com.myapp.exchangeweb.repositories.InvestSlotRepository;



@Service
public class InvestSlotService {
	
	
	@Autowired
	private InvestSlotRepository investSlotRepository ;
	
	
	// LOGGER
	private static final Logger LOGGER = LoggerFactory.getLogger( InvestSlotService.class ) ;
	
	
	public List<InvestSlot> findAll () {
		
		LOGGER.debug( "ENTER: findAll ()" ) ;
		LOGGER.debug( "EXIT: findAll ()" ) ;
		
		return this.investSlotRepository.findAll() ;
		
	}

	
	public List<InvestSlot> findById ( Integer id ) {
		
		LOGGER.debug( "ENTER: findById ()" ) ;
		LOGGER.debug( "id: {}", id ) ;
		
		List<Integer> id_list = new ArrayList<Integer>() ;
		id_list.add( id ) ;
		
		LOGGER.debug( "EXIT: findById ()" ) ;
		
		return this.investSlotRepository.findAllById( id_list ) ;
		
	}
	
	public InvestSlot save ( InvestSlot investSlot ) {
		
		LOGGER.debug( "ENTER: save ()" ) ;
		LOGGER.debug( "investSlot: {}", investSlot ) ;
		LOGGER.debug( "EXIT: save ()" ) ;
		
		return this.investSlotRepository.save( investSlot ) ;
		
	}
	
	public void deleteAllByIdList ( List<Integer> idList ) {
		
		LOGGER.debug( "ENTER: deleteAllByIdList ()" ) ;
		LOGGER.debug( "idList: {}", idList ) ;
		LOGGER.debug( "EXIT: deleteAllByIdList ()" ) ;
		
		this.investSlotRepository.deleteAllById( idList ) ;
		
	}



}
