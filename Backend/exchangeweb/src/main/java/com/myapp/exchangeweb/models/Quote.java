package com.myapp.exchangeweb.models;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table( name = "quotes" )
public class Quote {

	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	@Column( name = "id" )
	private Integer id ;
	
	@Column( name = "curr_price" )
	private BigDecimal currPrice ;
	
	@Column( name = "last_close_price" )
	private BigDecimal lastClosePrice ;
	
	@Column( name = "change_price_absolute" )
	private BigDecimal changePriceAbsolute ;
	
	@Column( name = "change_price_percent" )
	private BigDecimal changePricePercent ;
	
	@Column( name = "one_year_high_price" )
	private BigDecimal oneYearHighPrice ;
	
	@Column( name = "one_year_low_price" )
	private BigDecimal oneYearLowPrice ;

	

	// Constructors:
	
	public Quote ( 	Integer inputId,
	                BigDecimal inputCurrPrice,
	                BigDecimal inputLastClosePrice,
	                BigDecimal inputChangePriceAbsolute,
	                BigDecimal inputChangePricePercent,
	                BigDecimal inputOneYearHighPrice,
	                BigDecimal inputOneYearLowPrice ) {
		
		this.id = inputId ;
		this.currPrice = inputCurrPrice ;
		this.lastClosePrice = inputLastClosePrice ;
		this.changePriceAbsolute = inputChangePriceAbsolute ;
		this.changePricePercent = inputChangePricePercent ;
		this.oneYearHighPrice = inputOneYearHighPrice ;
	    this.oneYearLowPrice = inputOneYearLowPrice ;
		
	}
	
	public Quote () {
		
	}
	
	
	
	// Getters and Setters:
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public BigDecimal getCurrPrice() {
		return currPrice;
	}
	public void setCurrPrice(BigDecimal currPrice) {
		this.currPrice = currPrice;
	}
	public BigDecimal getLastClosePrice() {
		return lastClosePrice;
	}
	public void setLastClosePrice(BigDecimal lastClosePrice) {
		this.lastClosePrice = lastClosePrice;
	}
	public BigDecimal getChangePriceAbsolute() {
		return changePriceAbsolute;
	}
	public void setChangePriceAbsolute(BigDecimal changePriceAbsolute) {
		this.changePriceAbsolute = changePriceAbsolute;
	}
	public BigDecimal getChangePricePercent() {
		return changePricePercent;
	}
	public void setChangePricePercent(BigDecimal changePricePercent) {
		this.changePricePercent = changePricePercent;
	}
	public BigDecimal getOneYearHighPrice() {
		return oneYearHighPrice;
	}
	public void setOneYearHighPrice(BigDecimal oneYearHighPrice) {
		this.oneYearHighPrice = oneYearHighPrice;
	}
	public BigDecimal getOneYearLowPrice() {
		return oneYearLowPrice;
	}
	public void setOneYearLowPrice(BigDecimal oneYearLowPrice) {
		this.oneYearLowPrice = oneYearLowPrice;
	}

	
	// Method: toString():
	
	public String toString() {
		
		String retString = "" ;
		retString += "%n" ;
		
		retString += "{" + "%n" ;
		retString += "   Class: Quote" + ", %n%n" ;
		
		retString += "   id: " + this.id + ", %n" ;
		retString += "   currPrice: " + this.currPrice + ", %n" ;		
		retString += "   lastClosePrice: " + this.lastClosePrice + ", %n" ;
		retString += "   changePriceAbsolute: " + this.changePriceAbsolute + ", %n" ;		
		retString += "   changePricePercent: " + this.changePricePercent + ", %n" ;		
		retString += "   oneYearHighPrice: " + this.oneYearHighPrice + ", %n" ;		
		retString += "   oneYearLowPrice: " + this.oneYearLowPrice + "%n" ;
				
		retString += "}" ;
		
		retString += "%n" ;
		
		retString = String.format( retString ) ;
		
		return retString ;
				
	}
	
	
}
