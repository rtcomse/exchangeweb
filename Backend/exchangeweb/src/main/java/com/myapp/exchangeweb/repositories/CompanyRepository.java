package com.myapp.exchangeweb.repositories;

import java.util.List;

import org.springframework.data.repository.ListCrudRepository;

import com.myapp.exchangeweb.models.Company;



public interface CompanyRepository extends ListCrudRepository<Company, Integer> {

	List<Company> findByOrderBySymbolAsc () ;	
	
	List<Company> findBySymbolIgnoreCaseOrderBySymbolAsc ( String symbol ) ;
	List<Company> findBySymbolStartsWithIgnoreCaseOrderBySymbolAsc ( String symbol ) ;

}
