package com.myapp.exchangeweb.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.myapp.exchangeweb.security.config.services.UserDetailsServiceAccount;

import com.myapp.exchangeweb.project.ProjectData ;



@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Bean
	public SecurityFilterChain securityFilterChain ( HttpSecurity http ) throws Exception {
		
		http.authorizeHttpRequests( ( authorize ) -> authorize.requestMatchers( "/companies" ).permitAll()
															  .requestMatchers( "/companies/search/symbol/starts_with/{companySymbol:^.+$}" ).permitAll()
															  .requestMatchers( "/companies/create_company" ).hasRole( "ADMIN" )
                											  .requestMatchers( "/companies/delete_company" ).hasRole( "ADMIN" )
                											  .requestMatchers( "/companies/**" ).hasRole( "ADMIN" )
                											  
                											  .requestMatchers( "/invest_slots/**" ).hasRole( "ADMIN" )
                											  
															  .requestMatchers( "/portfolios/{portfolioId:^\\d+$}" ).permitAll()
				                                              .requestMatchers( "/portfolios/update_settings" ).hasRole( "USER" )
				                                              .requestMatchers( "/portfolios/add_investment" ).hasRole( "USER" )
				                                              .requestMatchers( "/portfolios/remove_investment" ).hasRole( "USER" )
				                                              .requestMatchers( "/portfolios/create_portfolio" ).hasRole( "USER" )
				                                              .requestMatchers( "/portfolios/delete_portfolio" ).hasRole( "USER" )
				                                              .requestMatchers( "/portfolios/**" ).hasRole( "ADMIN" )
				                                              
				                                              .requestMatchers( "/quotes/{quoteId:^\\d+$}" ).permitAll()
				                                              .requestMatchers( "/quotes/**" ).hasRole( "ADMIN" )
				                                              
				                                              .requestMatchers( "/prices/company/{companyId:^\\d+$}/days/{numDays:^\\d+$}" ).permitAll()
				                                              .requestMatchers( "/prices/**" ).hasRole( "ADMIN" )
				                                              
				                                              .requestMatchers( "/accounts/{accountId:^\\d+$}/search/portfolios/name/starts_with/{portfolioName:^.+$}" ).permitAll()
				                                              .requestMatchers( "/accounts/create_account" ).permitAll()
				                                              .requestMatchers( "/accounts/delete_account/current_user" ).hasRole( "USER" )
				                                              .requestMatchers( "/accounts/**" ).hasRole( "ADMIN" )
				                                              
				                                              .requestMatchers( "/authorities/**" ).hasRole( "ADMIN" )
				                                              
				                                              .requestMatchers( "/roles/**" ).hasRole( "ADMIN" )
				                                              
				                                              .anyRequest().permitAll() )
		    .httpBasic( Customizer.withDefaults() )
		    .formLogin( ( form ) -> form.loginProcessingUrl( ( new ProjectData() ).getUrlPrefixServer1() + 
		    		                                         "/login" ) )
		    .logout( ( form ) -> form.logoutRequestMatcher( new AntPathRequestMatcher( "/custom_logout", "GET" ) ) ) ;
		
		return http.build() ;
		
	}

	
	@Bean
	public UserDetailsService userDetailsService () {
		
		return new UserDetailsServiceAccount() ;
		
	}
	
	@Bean
	public static PasswordEncoder passwordEncoder () {
		
		PasswordEncoder retPasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder() ;
		
		return retPasswordEncoder ;
		
	}
	
	@Bean
	public static RoleHierarchy roleHierarchy () {
		
		return RoleHierarchyImpl.withDefaultRolePrefix()
				                .role( "ADMIN" ).implies( "USER" )
				                .role( "GUEST" ).implies( "USER" )
				                .build() ;
		
	}
	
	
	
}
