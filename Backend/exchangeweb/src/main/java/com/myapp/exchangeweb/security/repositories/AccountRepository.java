package com.myapp.exchangeweb.security.repositories;

import java.util.List;

import org.springframework.data.repository.ListCrudRepository;

import com.myapp.exchangeweb.security.models.Account;


public interface AccountRepository extends ListCrudRepository<Account, Integer>  {
	
	List<Account> findByUsername ( String username ) ;
	
	List<Account> findByRolesId ( Integer roleId ) ;	
	List<Account> findByAuthoritiesId ( Integer authorityId ) ;
	
}
