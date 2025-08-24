package com.myapp.exchangeweb.security.repositories;

import org.springframework.data.repository.ListCrudRepository;

import com.myapp.exchangeweb.security.models.Authority;



public interface AuthorityRepository extends ListCrudRepository<Authority, Integer> {

}
