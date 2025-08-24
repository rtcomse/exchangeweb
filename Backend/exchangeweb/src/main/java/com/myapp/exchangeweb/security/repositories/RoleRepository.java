package com.myapp.exchangeweb.security.repositories;

import java.util.List;

import org.springframework.data.repository.ListCrudRepository;

import com.myapp.exchangeweb.security.models.Role;



public interface RoleRepository extends ListCrudRepository<Role, Integer> {
	
	List<Role> findByAuthoritiesId ( Integer authorityId ) ;

}
