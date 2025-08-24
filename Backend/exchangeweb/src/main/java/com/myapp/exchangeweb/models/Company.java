package com.myapp.exchangeweb.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table( name = "companies" )
public class Company {
	
	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	@Column( name = "id" )
	private Integer id ;
	
	@Column( name = "name" )
	private String name ;
	
	@Column( name = "symbol" )
	private String symbol ;
	
	@Column( name = "description" )
	private String description ;
	
	@JoinColumn( name = "quotes_id",
                 referencedColumnName = "id")
	@OneToOne( fetch = FetchType.EAGER )
	private Quote quote ;

	
	// Constructors:
	
	public Company ( Integer inputId,		
			         String inputName,	
			         String inputSymbol,
			         String inputDescription,
			         Quote inputQuote ) {
				
		this.id = inputId ;		
		this.name = inputName ;		
		this.symbol = inputSymbol ;
		this.description = inputDescription ;
		this.quote = inputQuote ;
		
	}
	
	public Company () {
		
    }
	
	
	
	// Getters and Setters:
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	
	public Quote getQuote() {
		return quote;
	}

	public void setQuote(Quote quote) {
		this.quote = quote;
	}

	
	// Method: toString():
	
	public String toString() {
		
		String retString = "" ;
		retString += "%n" ;
		
		retString += "{" + "%n" ;
		retString += "   Class: Company" + ", %n%n" ;
		
		retString += "   id: " + this.id + ", %n" ;
		retString += "   name: " + this.name + ", %n" ;
		retString += "   symbol: " + this.symbol + ", %n" ;
		retString += "   description: " + this.description + ", %n" ;
		retString += "   quote: " + this.quote + "%n" ;
		
		
		retString += "}" ;
		
		retString += "%n" ;
		
		retString = String.format( retString ) ;
		
		return retString ;
		
	}

	

}
