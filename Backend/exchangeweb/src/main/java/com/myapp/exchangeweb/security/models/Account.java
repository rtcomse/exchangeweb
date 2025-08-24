package com.myapp.exchangeweb.security.models;

import java.util.List;

import com.myapp.exchangeweb.models.Portfolio;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;


@Entity
@Table( name = "accounts" )
public class Account {

	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	@Column( name = "id" )
	private Integer id ;
	
	@Column( name = "username" )
	private String username ;
	
	@Column( name = "password" )
	private String password ;
	
	@Column( name = "is_account_non_expired" )
	private boolean isAccountNonExpired ;
	
	@Column( name = "is_account_non_locked" )
	private boolean isAccountNonLocked ;
	
	@Column( name = "is_credentials_non_expired" )
	private boolean isCredentialsNonExpired ;
	
	@Column( name = "is_enabled" )
	private boolean isEnabled ;
	
	
	@JoinTable( name = "accounts_to_roles",
			    joinColumns = @JoinColumn( name = "accounts_id",
			                               referencedColumnName = "id"),
			    inverseJoinColumns = @JoinColumn( name = "roles_id",
			    		                          referencedColumnName = "id") )
	@ManyToMany( fetch = FetchType.EAGER )	
	private List<Role> roles ;
	
	
	@JoinTable( name = "accounts_to_authorities",
			    joinColumns = @JoinColumn( name = "accounts_id",
			                               referencedColumnName = "id"),
			    inverseJoinColumns = @JoinColumn( name = "authorities_id",
			    		                          referencedColumnName = "id") )
	@ManyToMany( fetch = FetchType.EAGER )
	private List<Authority> authorities ;

	
	@JoinTable( name = "accounts_to_portfolios",
			    joinColumns = @JoinColumn( name = "accounts_id",
			                               referencedColumnName = "id"),
			    inverseJoinColumns = @JoinColumn( name = "portfolios_id",
			    		                          referencedColumnName = "id") )
	@OneToMany( fetch = FetchType.EAGER )	
	private List<Portfolio> portfolios ;
	
	
	
	// Constructors:
	
	public Account ( Integer inputId,
	                 String inputUsername,
	                 String inputPassword,
	                 boolean inputIsAccountNonExpired,
	                 boolean inputIsAccountNonLocked,
	                 boolean inputIsCredentialsNonExpired,
	                 boolean inputIsEnabled,	
	                 List<Role> inputRoles,
	                 List<Authority> inputAuthorities,
	                 List<Portfolio> inputPortfolios ) {
		
		this.id = inputId ;
        this.username = inputUsername ;
        this.password = inputPassword ;
        this.isAccountNonExpired = inputIsAccountNonExpired ;
        this.isAccountNonLocked = inputIsAccountNonLocked ;
        this.isCredentialsNonExpired = inputIsCredentialsNonExpired ;
        this.isEnabled = inputIsEnabled ;	
        this.roles = inputRoles ;
        this.authorities = inputAuthorities ;
        this.portfolios = inputPortfolios ;
		
	}
	
	public Account () {

	}	
 	
	
	// Getters and Setters:
		
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public boolean isAccountNonExpired() {
		return isAccountNonExpired;
	}
	public void setAccountNonExpired(boolean isAccountNonExpired) {
		this.isAccountNonExpired = isAccountNonExpired;
	}
	
	public boolean isAccountNonLocked() {
		return isAccountNonLocked;
	}
	public void setAccountNonLocked(boolean isAccountNonLocked) {
		this.isAccountNonLocked = isAccountNonLocked;
	}
	
	public boolean isCredentialsNonExpired() {
		return isCredentialsNonExpired;
	}
	public void setCredentialsNonExpired(boolean isCredentialsNonExpired) {
		this.isCredentialsNonExpired = isCredentialsNonExpired;
	}
	
	public boolean isEnabled() {
		return isEnabled;
	}
	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}
	
	public List<Role> getRoles() {
		return roles;
	}
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	
	public List<Authority> getAuthorities() {
		return authorities;
	}
	public void setAuthorities(List<Authority> authorities) {
		this.authorities = authorities;
	}
	
	public List<Portfolio> getPortfolios() {
		return portfolios;
	}
	public void setPortfolios(List<Portfolio> portfolios) {
		this.portfolios = portfolios;
	}
	
	
	
	// Method: toString():
	
	public String toString() {
		
		String retString = "" ;
		retString += "%n" ;
		
		retString += "{" + "%n" ;
		retString += "   Class: Account" + ", %n%n" ;
		
		retString += "   id: " + this.id + ", %n" ;
		retString += "   username: " + this.username + ", %n" ;		
		retString += "   password: " + this.password + ", %n" ;
		retString += "   isAccountNonExpired: " + this.isAccountNonExpired + ", %n" ;
		retString += "   isAccountNonLocked: " + this.isAccountNonLocked + ", %n" ;		
		retString += "   isCredentialsNonExpired: " + this.isCredentialsNonExpired + ", %n" ;		
		retString += "   isEnabled: " + this.isEnabled + ", %n" ;

		retString += "   roles: %n" + this.roles + ", %n" ;
		retString += "   authorities: %n" + this.authorities + "%n" ;		
		retString += "   portfolios: %n" + this.portfolios + ", %n" ;
				
		retString += "}" ;
		
		retString += "%n" ;
		
		retString = String.format( retString ) ;
		
		return retString ;
				
	}

	
}
