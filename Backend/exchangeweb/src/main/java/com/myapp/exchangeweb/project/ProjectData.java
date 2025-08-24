package com.myapp.exchangeweb.project;

public class ProjectData {
	
	private final String urlPrefix = "/exchangeweb" ;
	private final String urlPrefixServer1 = this.urlPrefix + 
			                                "/server_1" ;
	private final String urlPrefixServer2 = this.urlPrefix + 
                                            "/server_2" ;
	
	
	
	// Getters and Setters:
	
	public String getUrlPrefix() {
		return urlPrefix;
	}
	public String getUrlPrefixServer1() {
		return urlPrefixServer1;
	}
	public String getUrlPrefixServer2() {
		return urlPrefixServer2;
	}
	
	
	

}
