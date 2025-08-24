package com.myapp.exchangeweb.security.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table( name = "authorities" )
public class Authority {
	
	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	@Column( name = "id" )
	private Integer id ;
	
	@Column( name = "name" )
	private String name ;

	
	// Constructors:
	
	public Authority ( Integer inputId, 
			           String inputName ) {
		
		this.id = inputId ;
		this.name = inputName ;
		
	}
	
	public Authority () {
		
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

	
	// Method: toString():
	
	public String toString() {
		
		String retString = "" ;
		retString += "%n" ;
		
		retString += "{" + "%n" ;
		retString += "   Class: Authority" + ", %n%n" ;
		
		retString += "   id: " + this.id + ", %n" ;
		retString += "   name: " + this.name + "%n" ;
		retString += "}" ;
		
		retString += "%n" ;
		
		retString = String.format( retString ) ;
		
		return retString ;
		
	}
	
}
