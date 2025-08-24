$( document ).ready( function () {
	
	$( "#alphabet_accordion_block_id_1 button.accordion-button" ).on( "click", function ( event ) {
		
		const currButtonNode = $( this ) ;
		
		if ( $( this ).not( ".collapsed" ).length == 0 ) {
			
			return ;
			
		}
		
		const searchText = $( this ).contents().filter( function () {
			
			return this.nodeType === Node.TEXT_NODE ;
			
		} ).text() ;
		
		
		
		$.get( window.projectData.urlPrefixServer1 + 
			   `/companies/search/symbol/starts_with/${searchText}`, function ( data, status ) {
			
			const retrievedCompaniesList = data ;
			
			
			
			const tableBodyNode = $( currButtonNode ).parent().closest( ".accordion-item" )
			                               .find( ".accordion-collapse .accordion-body .table tbody" ) ;
			
			
			tableBodyNode.empty() ;
			
			for ( let i = 0 ; i < retrievedCompaniesList.length; i++ ) {
				
				let tableRowContentHtml = "" ;
				
				tableRowContentHtml += "<tr>" ;
				
				tableRowContentHtml += "<th scope=\"row\">" + ( i + 1 ) + "</th>" ;
				
				tableRowContentHtml += "<td>" + retrievedCompaniesList[ i ].symbol + "</td>" ;
				tableRowContentHtml += "<td>" + retrievedCompaniesList[ i ].name + "</td>" ;
				
				
				tableRowContentHtml += "</tr>" ;
				
				$( tableRowContentHtml ).appendTo( tableBodyNode ).on( "click", function ( event ) {
					
					const companyProfilePageUrl = window.projectData.urlPrefixServer1 + 
					                              "/company_profile_page/" 
					                              + retrievedCompaniesList[ i ].id ;
												  
					window.location.assign( companyProfilePageUrl ) ;
					
				} )  ;
				
			}						   
										   							   
			
		} ) ;
		
	} ) ;
	
} ) ;



$( document ).ready( function () {
	
	
	
	$( "#create_company_price_list_add_more_button" ).on( "click", function () {
		
		const priceListNode = $( this ).prev().clone( true, true ) ;
		
		$( this ).before( priceListNode ) ;
				
	} ) ;
	
	
	
			
	$( "#grid_left .modal-body form" ).on( "submit", function () {

		$( ".modal-footer button[type=\"submit\"][form=\"" + this.getAttribute( "id" ) + "\"]" ).each( function () {
			
			const pagePlaceholdersFormSubmitNode = $( ".page_placeholders_form_submit" ).clone( true, true ) ;
			
			$( this ).prepend( pagePlaceholdersFormSubmitNode ) ;
			 	 	 		
		} ) ;
		 	 	 		
	} ) ;
	
	 
	
} ) ;






