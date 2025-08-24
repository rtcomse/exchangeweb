


async function setupChart( inputPortfolioId, inputNumDays ) {
	
	const chartGen = await import( window.projectData.urlPrefixServer2 + 
	                               "/dist/pages/portfolio_profile_page/bundle_chart_gen_portfolio_profile_page.js" ) ;
								   
	
	const chartDisplayBlockNode = $( "#chart_block_id_1 .chart_display_block" ) ;

	const portfolioId = inputPortfolioId ;

	const numDays = inputNumDays ;
	
		
	const portfolioValueList = [] ;
	const investSlotValueLists = [] ;
	const investSlotValueStartIndexList = [] ;
	const dataLabels = {
		
		portfolioLabel: "",
		investSlotLabelList: []
		
	} ;
		
	const investSlotPromiseList = [] ;	

				
	$.get( window.projectData.urlPrefixServer1 + 
		   `/portfolios/${portfolioId}`, function ( data, status ) {
		
		const retrievedPortfolio = data[ 0 ] ;
		
		const numDaysDataPortfolio = (retrievedPortfolio.investStartDay * (-1)) + 1 ;
		const numDaysDataInput = numDays + 1 ;
		const numDaysData = ( numDaysDataPortfolio < numDaysDataInput ) ? numDaysDataPortfolio : numDaysDataInput ;
				
		dataLabels.portfolioLabel = retrievedPortfolio.name ;
		
		for ( let i = 0 ; i < retrievedPortfolio.investSlots.length ; i++ ) {
			
			const currInvestSlot = retrievedPortfolio.investSlots[ i ] ;
			
			const currCompany = currInvestSlot.company ;
			const currQuote = currCompany.quote ;
			const currQuantity = currInvestSlot.investQuantity ;
			
			dataLabels.investSlotLabelList[ i ] = currCompany.symbol ;
			
			const investSlotPromise = new Promise( ( resolve, reject ) => {
				
				$.get( window.projectData.urlPrefixServer1 + 
					   `/prices/company/${currCompany.id}/days/${numDaysData - 1}`, function ( data, status ) {
					
					const retrievedPriceList = data ;
					
					retrievedPriceList.push( currQuote.currPrice ) ;

										
					const fullPriceList = retrievedPriceList ;					
					
					investSlotValueStartIndexList[ i ] = numDaysData - fullPriceList.length ;
					
					const currInvestSlotValueList = [] ;
					
					for ( let j = 0 ; j < fullPriceList.length ; j++ ) {
						
						currInvestSlotValueList[ j ] = fullPriceList[ j ] * currQuantity ;
						
					}
					
					investSlotValueLists[ i ] = currInvestSlotValueList ;
					
					
					resolve( currInvestSlotValueList ) ;
					
				} )
				.fail( function () {
					
					reject( this ) ;
					
				} ) ;
				
			} ) ;
			
			investSlotPromiseList[ i ] = investSlotPromise ;
			
		}
		
		Promise.all( investSlotPromiseList )
		       .then( ( values ) => {

				   for ( let i = 0 ; i < numDaysData ; i++ ) {
					
					   let sumPortfolioValue = 0 ;
					
					   for ( let j = 0 ; j < investSlotValueLists.length ; j++ ) {
						
						   const currInvestSlotValueList = investSlotValueLists[ j ] ;
						   
						   
						   if ( i >= investSlotValueStartIndexList[ j ] ) {
							
							   const currValueIndex = i - investSlotValueStartIndexList[ j ] ;
							   
							   sumPortfolioValue += currInvestSlotValueList[ currValueIndex ] ;
							
						   }
						
					   }
					   
					   portfolioValueList[ i ] = sumPortfolioValue ;
					
				   }
				   
				   chartDisplayBlockNode.empty() ;

				   chartDisplayBlockNode.append( chartGen.createChart( dataLabels, 
					                                                   portfolioValueList,
														               investSlotValueLists,
														               investSlotValueStartIndexList ) ) ;

				   $( '[data-bs-toggle|="tooltip"]' ).each( function () {
				   	
				   	    const tooltip = new bootstrap.Tooltip( this ) ;
				   	
				   	    $( this ).siblings().on( "mouseenter", function ( event ) {
				   		
				   		    tooltip.show() ;
				   					
				   	    } ).on( "mouseleave", function ( event ) {
				   		
				   		    tooltip.hide() ;
				   													
				   	    } ) ;
				   					
				   } ) ;

				
			   } )
			   .catch( ( error ) => {
				
				   console.error( error ) ;
				
			   } ) ;
			   
			   
	} ) ;
		
	
}






$( document ).ready( function () {
	
	const portfolioId = Number.parseInt( $( "#script_arguments_1_portfolio_id" ).text() ) ;
		
	$( "#chart_data_range_block_id_1 button" ).removeClass( "pressed" ) ;
	$( "#chart_data_range_block_id_1 #button_1_year" ).addClass( "pressed" ) ;
	
	const chartDisplayBlockNode = $( "#chart_block_id_1 .chart_display_block" ) ;
	const pagePlaceholdersChartNode = $( ".page_placeholders_chart" ).clone( true, true ) ;

	chartDisplayBlockNode.empty() ;
	chartDisplayBlockNode.append( pagePlaceholdersChartNode ) ;

	
	setupChart( portfolioId, 365 ) ;

		
	$( "#chart_data_range_block_id_1 #button_1_year" ).on( "click", function () {
		
		$( "#chart_data_range_block_id_1 button" ).removeClass( "pressed" ) ;
		$( this ).addClass( "pressed" ) ;

		chartDisplayBlockNode.empty() ;
		chartDisplayBlockNode.append( pagePlaceholdersChartNode ) ;
		
		setupChart( portfolioId, 365 ) ;
		
	 } ) ;
	 
	 $( "#chart_data_range_block_id_1 #button_6_months" ).on( "click", function () {
		
		$( "#chart_data_range_block_id_1 button" ).removeClass( "pressed" ) ;
		$( this ).addClass( "pressed" ) ;

		chartDisplayBlockNode.empty() ;
		chartDisplayBlockNode.append( pagePlaceholdersChartNode ) ;
		
		setupChart( portfolioId, 180 ) ;
	 		
	 } ) ;
	 
	 $( "#chart_data_range_block_id_1 #button_3_months" ).on( "click", function () {
		
		$( "#chart_data_range_block_id_1 button" ).removeClass( "pressed" ) ;
		$( this ).addClass( "pressed" ) ;

		chartDisplayBlockNode.empty() ;
		chartDisplayBlockNode.append( pagePlaceholdersChartNode ) ;
		
		setupChart( portfolioId, 90 ) ;
	 	 		
	 } ) ;


	 
	 	 
	 $( ".modal-body form" ).on( "submit", function () {
		
		$( "#grid_left .modal-footer button[type=\"submit\"][form=\"" + this.getAttribute( "id" ) + "\"]" ).each( function () {
			
			const pagePlaceholdersFormSubmitNode = $( ".page_placeholders_form_submit" ).clone( true, true ) ;
			
			$( this ).prepend( pagePlaceholdersFormSubmitNode ) ;
		
		} ) ;
	  	 	 		
	 } ) ;	 
	 
	 
	 
	 
	
} ) ;

