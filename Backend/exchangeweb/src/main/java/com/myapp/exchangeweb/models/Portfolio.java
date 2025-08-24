package com.myapp.exchangeweb.models;

import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;

import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;


@Entity
@Table( name = "portfolios" )
public class Portfolio {
	
	
	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	@Column( name = "id" )
	private Integer id ;
	
	@Column( name = "name" )
	private String name ;
	
	@Column( name = "description" )
	private String description ;
	
	@Column( name = "invest_start_day" )
	private Integer investStartDay ;
	
	@Column( name = "invest_value_start" )
	private BigDecimal investValueStart ;
	
	@Column( name = "invest_value_end" )
	private BigDecimal investValueEnd ;
	
	@Column( name = "change_invest_value_absolute" )
	private BigDecimal changeInvestValueAbsolute ;
	
	@Column( name = "change_invest_value_percent" )
	private BigDecimal changeInvestValuePercent ;
	

	
	
	
	@JoinTable( name = "portfolios_to_invest_slots",
			    joinColumns = @JoinColumn( name = "portfolios_id",
			                               referencedColumnName = "id"),
			    inverseJoinColumns = @JoinColumn( name = "invest_slots_id",
			    		                          referencedColumnName = "id") )
	@OneToMany( fetch = FetchType.EAGER )
	private List<InvestSlot> investSlots ;
	
	
	
	// Constructors:
	
	public Portfolio ( Integer inputId,
	                   String inputName,
	                   String inputDescription,
	                   Integer inputInvestStartDay,
	                   BigDecimal inputInvestValueStart,
	                   BigDecimal inputInvestValueEnd,
	                   BigDecimal inputChangeInvestValueAbsolute,
	                   BigDecimal inputChangeInvestValuePercent,
	                   List<InvestSlot> inputInvestSlots ) {
		
		
		
		this.id = inputId ;
		this.name = inputName ;
		this.description = inputDescription ;
		this.investStartDay = inputInvestStartDay ;
		this.investValueStart = inputInvestValueStart ;
		this.investValueEnd = inputInvestValueEnd ;
		this.changeInvestValueAbsolute = inputChangeInvestValueAbsolute ;
		this.changeInvestValuePercent = inputChangeInvestValuePercent ;
		this.investSlots = inputInvestSlots ;

	}
	
	public Portfolio () {
		
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getInvestStartDay() {
		return investStartDay;
	}
	public void setInvestStartDay(Integer investStartDay) {
		this.investStartDay = investStartDay;
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
	public List<InvestSlot> getInvestSlots() {
		return investSlots;
	}
	public void setInvestSlots(List<InvestSlot> investSlots) {
		this.investSlots = investSlots;
	}
	
	
	
	// Method: toString():
	
	public String toString() {
		
		String retString = "" ;
		retString += "%n" ;
		
		retString += "{" + "%n" ;
		retString += "   Class: Portfolio" + ", %n%n" ;		
		
		retString += "   id: " + this.id + ", %n" ;		
		retString += "   name: " + this.name + ", %n" ;
		retString += "   description: " + this.description + ", %n" ;
		retString += "   investStartDay: " + this.investStartDay + ", %n" ;
		retString += "   investValueStart: " + this.investValueStart + ", %n" ;
		retString += "   investValueEnd: " + this.investValueEnd + ", %n" ;
		retString += "   changeInvestValueAbsolute: " + this.changeInvestValueAbsolute + ", %n" ;
		retString += "   changeInvestValuePercent: " + this.changeInvestValuePercent + ", %n" ;
		retString += "   investSlots: " + this.investSlots + "%n" ;
				
		retString += "}" ;
		
		retString += "%n" ;
		
		retString = String.format( retString ) ;
		
		return retString ;
		
	}

	
}
