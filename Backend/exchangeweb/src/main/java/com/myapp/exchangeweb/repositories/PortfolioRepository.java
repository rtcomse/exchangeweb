package com.myapp.exchangeweb.repositories;


import org.springframework.data.repository.ListCrudRepository;

import com.myapp.exchangeweb.models.Portfolio;


public interface PortfolioRepository extends ListCrudRepository<Portfolio, Integer> {

}
