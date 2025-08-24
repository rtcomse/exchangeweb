$( document ).ready( function () {
	
	$( "#alphabet_accordion_block_id_1 button.accordion-button" ).on( "click", function ( event ) {
		
		const currButtonNode = $( this ) ;
		
		if ( $( this ).not( ".collapsed" ).length == 0 ) {
			
			return ;
			
		}
		
		const searchText = $( this ).contents().filter( function () {
			
			return this.nodeType === Node.TEXT_NODE ;
			
		} ).text() ;
		
		
		const accountId = Number.parseInt( $( "#script_arguments_1_account_id" ).text() ) ;
		
		$.get( window.projectData.urlPrefixServer1 + 
			   `/accounts/${accountId}/search/portfolios/name/starts_with/${searchText}`, function ( data, status ) {
			
			const retrievedPortfoliosList = data ;
			
			
			
			const tableBodyNode = $( currButtonNode ).parent().closest( ".accordion-item" )
			                               .find( ".accordion-collapse .accordion-body .table tbody" ) ;
			
			
			tableBodyNode.empty() ;
			
			for ( let i = 0 ; i < retrievedPortfoliosList.length; i++ ) {
				
				let tableRowContentHtml = "" ;
				
				tableRowContentHtml += "<tr>" ;
				
				tableRowContentHtml += "<th scope=\"row\">" + ( i + 1 ) + "</th>" ;
				
				tableRowContentHtml += "<td>" + retrievedPortfoliosList[ i ].name + "</td>" ;
				tableRowContentHtml += "<td>" + retrievedPortfoliosList[ i ].description + "</td>" ;
				
				
				tableRowContentHtml += "</tr>" ;
				
				$( tableRowContentHtml ).appendTo( tableBodyNode ).on( "click", function ( event ) {
					
					const portfolioProfilePageUrl = window.projectData.urlPrefixServer1 
					                              + "/portfolio_profile_page/" 
					                              + retrievedPortfoliosList[ i ].id ;
												  
					window.location.assign( portfolioProfilePageUrl ) ;
					
				} ) ;
				
			}						   
										   							   
			
		} ) ;
		
	} ) ;
	
} ) ;





$( document ).ready( function () {

		
	 $( "#grid_left .modal-body form" ).on( "submit", function () {

		$( ".modal-footer button[type=\"submit\"][form=\"" + this.getAttribute( "id" ) + "\"]" ).each( function () {
			
			const pagePlaceholdersFormSubmitNode = $( ".page_placeholders_form_submit" ).clone( true, true ) ;
			
			$( this ).prepend( pagePlaceholdersFormSubmitNode ) ;
			 	 	 		
		} ) ;
		 	 	 		
	 } ) ;
	
	 
	
} ) ;



