


async function setupChart( inputCompanyId, inputNumDays ) {
	
	const chartGen = await import( window.projectData.urlPrefixServer2 + 
	                               "/dist/pages/company_profile_page/bundle_chart_gen_company_profile_page.js" ) ;
	
	
	const chartDisplayBlockNode = $( "#chart_block_id_1 .chart_display_block" ) ;

	const companyId = inputCompanyId ;

	const numDays = inputNumDays ;

		
	$.get( window.projectData.urlPrefixServer1 + 
		   `/prices/company/${companyId}/days/${numDays}`, function ( data, status ) {
		
		const retrievedPriceList = data ;
		
		const quoteId = Number.parseInt( $( "#script_arguments_2_quote_id" ).text() ) ;
		
		
		$.get( window.projectData.urlPrefixServer1 + 
			   `/quotes/${quoteId}`, function ( data, status ) {
						
			const retrievedQuote = data[ 0 ] ;
			
			retrievedPriceList.push( retrievedQuote.currPrice ) ;
			
			const fullPriceList = retrievedPriceList ;
			
			
			chartDisplayBlockNode.empty() ;

			chartDisplayBlockNode.append( chartGen.createChart( fullPriceList ) ) ;
			
			$( '[data-bs-toggle|="tooltip"]' ).each( function () {
				
				const tooltip = new bootstrap.Tooltip( this ) ;
				
				$( this ).siblings().on( "mouseenter", function ( event ) {
					
					tooltip.show() ;
								
				} ).on( "mouseleave", function ( event ) {
					
					tooltip.hide() ;
																
				} ) ;
								
			} ) ;
									
		} ) ;
		
	} ) ;
	
}






$( document ).ready( function () {
	
	const companyId = Number.parseInt( $( "#script_arguments_1_company_id" ).text() ) ;
		
	$( "#chart_data_range_block_id_1 button" ).removeClass( "pressed" ) ;
	$( "#chart_data_range_block_id_1 #button_1_year" ).addClass( "pressed" ) ;

	const chartDisplayBlockNode = $( "#chart_block_id_1 .chart_display_block" ) ;
	const pagePlaceholdersChartNode = $( ".page_placeholders_chart" ).clone( true, true ) ;

	chartDisplayBlockNode.empty() ;
	chartDisplayBlockNode.append( pagePlaceholdersChartNode ) ;

		
	setupChart( companyId, 365 ) ;

		
	$( "#chart_data_range_block_id_1 #button_1_year" ).on( "click", function () {
		
		$( "#chart_data_range_block_id_1 button" ).removeClass( "pressed" ) ;
		$( this ).addClass( "pressed" ) ;

		chartDisplayBlockNode.empty() ;
		chartDisplayBlockNode.append( pagePlaceholdersChartNode ) ;
		
		setupChart( companyId, 365 ) ;
		
	 } ) ;
	 
	 $( "#chart_data_range_block_id_1 #button_6_months" ).on( "click", function () {
		
		$( "#chart_data_range_block_id_1 button" ).removeClass( "pressed" ) ;
		$( this ).addClass( "pressed" ) ;

		chartDisplayBlockNode.empty() ;
		chartDisplayBlockNode.append( pagePlaceholdersChartNode ) ;
		
		setupChart( companyId, 180 ) ;
	 		
	 } ) ;
	 
	 $( "#chart_data_range_block_id_1 #button_3_months" ).on( "click", function () {
		
		$( "#chart_data_range_block_id_1 button" ).removeClass( "pressed" ) ;
		$( this ).addClass( "pressed" ) ;

		chartDisplayBlockNode.empty() ;
		chartDisplayBlockNode.append( pagePlaceholdersChartNode ) ;
		
		setupChart( companyId, 90 ) ;
	 	 		
	 } ) ;
	
} ) ;

