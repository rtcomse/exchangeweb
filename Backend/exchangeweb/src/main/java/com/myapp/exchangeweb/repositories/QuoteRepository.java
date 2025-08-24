package com.myapp.exchangeweb.repositories;

import org.springframework.data.repository.ListCrudRepository;

import com.myapp.exchangeweb.models.Quote;



public interface QuoteRepository extends ListCrudRepository<Quote, Integer> {

}
