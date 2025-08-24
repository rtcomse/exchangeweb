package com.myapp.exchangeweb.models;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table( name = "invest_slots" )
public class InvestSlot {
	
	
	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	@Column( name = "id" )
	private Integer id ;
	
	@JoinColumn( name = "companies_id",
                 referencedColumnName = "id")
	@ManyToOne( fetch = FetchType.EAGER )
	private Company company ;
	
	@Column( name = "invest_quantity" )
	private Integer investQuantity ;
	
	@Column( name = "invest_price_start" )
	private BigDecimal investPriceStart ;
	
	@Column( name = "invest_price_end" )
	private BigDecimal investPriceEnd ;
	
	@Column( name = "invest_value_start" )
	private BigDecimal investValueStart ;
	
	@Column( name = "invest_value_end" )
	private BigDecimal investValueEnd ;
	
	@Column( name = "change_invest_value_absolute" )
	private BigDecimal changeInvestValueAbsolute ;
	
	@Column( name = "change_invest_value_percent" )
	private BigDecimal changeInvestValuePercent ;

	
	
	// Constructors:
	
	public InvestSlot ( Integer inputId,
	                    Company inputCompany,
	                    Integer inputInvestQuantity,
	                    BigDecimal inputInvestPriceStart,
	                    BigDecimal inputInvestPriceEnd,
	                    BigDecimal inputInvestValueStart,
	                    BigDecimal inputInvestValueEnd,
	                    BigDecimal inputChangeInvestValueAbsolute,
	                    BigDecimal inputChangeInvestValuePercent ) {
		
		
		
		this.id = inputId ;
		this.company = inputCompany ;
		this.investQuantity = inputInvestQuantity ;
		this.investPriceStart = inputInvestPriceStart ;
		this.investPriceEnd = inputInvestPriceEnd ;
		this.investValueStart = inputInvestValueStart ;
		this.investValueEnd = inputInvestValueEnd ;
		this.changeInvestValueAbsolute = inputChangeInvestValueAbsolute ;
		this.changeInvestValuePercent = inputChangeInvestValuePercent ;
		
	}
	
    public InvestSlot () {
		
	}
	
	
	// Getters and Setters:
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Integer getInvestQuantity() {
		return investQuantity;
	}

	public void setInvestQuantity(Integer investQuantity) {
		this.investQuantity = investQuantity;
	}

	public BigDecimal getInvestPriceStart() {
		return investPriceStart;
	}

	public void setInvestPriceStart(BigDecimal investPriceStart) {
		this.investPriceStart = investPriceStart;
	}

	public BigDecimal getInvestPriceEnd() {
		return investPriceEnd;
	}

	public void setInvestPriceEnd(BigDecimal investPriceEnd) {
		this.investPriceEnd = investPriceEnd;
	}

	public BigDecimal getInvestValueStart() {
		return investValueStart;
	}

	public void setInvestValueStart(BigDecimal investValueStart) {
		this.investValueStart = investValueStart;
	}

	public BigDecimal getInvestValueEnd() {
		return investValueEnd;
	}

	public void setInvestValueEnd(BigDecimal investValueEnd) {
		this.investValueEnd = investValueEnd;
	}

	public BigDecimal getChangeInvestValueAbsolute() {
		return changeInvestValueAbsolute;
	}

	public void setChangeInvestValueAbsolute(BigDecimal changeInvestValueAbsolute) {
		this.changeInvestValueAbsolute = changeInvestValueAbsolute;
	}

	public BigDecimal getChangeInvestValuePercent() {
		return changeInvestValuePercent;
	}

	public void setChangeInvestValuePercent(BigDecimal changeInvestValuePercent) {
		this.changeInvestValuePercent = changeInvestValuePercent;
	}
	
	
	
	// Method: toString():
	
	public String toString() {
		
		String retString = "" ;
		retString += "%n" ;
		
		retString += "{" + "%n" ;
		retString += "   Class: InvestSlot" + ", %n%n" ;		
		
		retString += "   id: " + this.id + ", %n" ;	
		retString += "   company: " + this.company + ", %n" ;	
		retString += "   investQuantity: " + this.investQuantity + ", %n" ;	
		retString += "   investPriceStart: " + this.investPriceStart + ", %n" ;	
		retString += "   investPriceEnd: " + this.investPriceEnd + ", %n" ;	
		retString += "   investValueStart: " + this.investValueStart + ", %n" ;	
		retString += "   investValueEnd: " + this.investValueEnd + ", %n" ;	
		retString += "   changeInvestValueAbsolute: " + this.changeInvestValueAbsolute + ", %n" ;	
		retString += "   changeInvestValuePercent: " + this.changeInvestValuePercent + "%n" ;	
		
		retString += "}" ;
		
		retString += "%n" ;
		
		retString = String.format( retString ) ;
		
		return retString ;
		
	}


}
