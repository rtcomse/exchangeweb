package com.myapp.exchangeweb.security.models;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table( name = "roles" )
public class Role {
	
	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	@Column( name = "id" )
	private Integer id ;
	
	@Column( name = "name" )
	private String name ;

	
	@JoinTable( name = "roles_to_authorities",
			    joinColumns = @JoinColumn( name = "roles_id",
			                               referencedColumnName = "id"),
			    inverseJoinColumns = @JoinColumn( name = "authorities_id",
			    		                          referencedColumnName = "id") )
	@ManyToMany( fetch = FetchType.EAGER )
	private List<Authority> authorities ;
	
	
	// Constructors:
	
	public Role ( Integer inputId, 
			      String inputName,
			      List<Authority> inputAuthorities ) {
		
		this.id = inputId ;
		this.name = inputName ;
		this.authorities = inputAuthorities ;
		
	}
	
	public Role () {
		
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

	public List<Authority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(List<Authority> authorities) {
		this.authorities = authorities;
	}

	
	// Method: toString():
	
	public String toString() {
		
		String retString = "" ;
		retString += "%n" ;
		
		retString += "{" + "%n" ;
		retString += "   Class: Role" + ", %n%n" ;
		
		retString += "   id: " + this.id + ", %n" ;
		retString += "   name: " + this.name + ", %n" ;
	
		retString += "   authorities: %n" + this.authorities + "%n" ;
		
		retString += "}" ;
		
		retString += "%n" ;
		
		retString = String.format( retString ) ;
		
		return retString ;
		
	}
	
}
