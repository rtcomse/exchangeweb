$( document ).ready( function () {
	
	const portfolioId = Number.parseInt( $( "#script_arguments_1_portfolio_id" ).text() ) ;
	
	
	$.get( window.projectData.urlPrefixServer1 + 
		   `/portfolios/${portfolioId}`, function ( data, status ) {
		
		const retrievedPortfolio = data[ 0 ] ;
		

		const priceFormatter = new Intl.NumberFormat( "en-US", {
			
			style: "decimal" ,
			minimumFractionDigits: 2,
			maximumFractionDigits: 2,
			
		} ) ;

		const percentFormatter = new Intl.NumberFormat( "en-US", {
						
			style: "percent" ,
			minimumFractionDigits: 2,
			maximumFractionDigits: 2,
			signDisplay: "exceptZero"
						
		} ) ;
		
		const priceChangeFormatter = new Intl.NumberFormat( "en-US", {
					
			style: "decimal" ,
			minimumFractionDigits: 2,
			maximumFractionDigits: 2,
			signDisplay: "exceptZero"
					
		} ) ;

		
		const formatted = {
						
			investStartDay: retrievedPortfolio.investStartDay,
			changeInvestValueAbsolute: priceChangeFormatter.format( retrievedPortfolio.changeInvestValueAbsolute ),
			changeInvestValuePercent: percentFormatter.format( retrievedPortfolio.changeInvestValuePercent / 100.0 ),
			investValueStart: priceFormatter.format( retrievedPortfolio.investValueStart ),
			investValueEnd: priceFormatter.format( retrievedPortfolio.investValueEnd )
			
		} ;
		

		let indicatorClass = "" ;
		let iconIndicatorArrow = "" ;

		if ( retrievedPortfolio.changeInvestValuePercent > 0 ) {
			
			indicatorClass = "indicator_positive" ;
			iconIndicatorArrow = "&#9650;" ;
			
		} else if ( retrievedPortfolio.changeInvestValuePercent < 0 ) {
			
			indicatorClass = "indicator_negative" ;
			iconIndicatorArrow = "&#9660;" ;
			
		}

		const markupSpan = "<span " + 
						   "class=\"" + indicatorClass + "\"" +
						   ">" + 
						      iconIndicatorArrow + " " +
						      formatted.changeInvestValuePercent + " " + 
						   "</span>" ;

		
		$( "#portfolio_block_id_1 .portfolio_current_value_block .content > p" ).text( formatted.investValueEnd ) ;
		$( "#portfolio_block_id_1 .portfolio_value_change_block .content > p" ).html( markupSpan ) ;
		
		$( "#portfolio_block_id_1 .portfolio_start_day_block .content > p" ).text( formatted.investStartDay ) ;
		$( "#portfolio_block_id_1 .portfolio_change_value_absolute_block .content > p" ).text( formatted.changeInvestValueAbsolute ) ;
		$( "#portfolio_block_id_1 .portfolio_change_value_percent_block .content > p" ).text( formatted.changeInvestValuePercent ) ;
		$( "#portfolio_block_id_1 .portfolio_value_start_block .content > p" ).text( formatted.investValueStart ) ;
		$( "#portfolio_block_id_1 .portfolio_value_end_block .content > p" ).text( formatted.investValueEnd ) ;
		
		
		$( "#investments_block_id_1 td.format_price" ).text( function ( index, oldText ) {
			
			return priceFormatter.format( oldText ) ;
			
		} ) ;
		
		$( "#investments_block_id_1 td.format_percent" ).each( function ( index, element ) {
			
			const changeInvestValuePercent = Number.parseFloat( $( this ).text() ) / 100.0 ; 
			
			let indicatorClass = "" ;
			let iconIndicatorArrow = "" ;

			if ( changeInvestValuePercent > 0 ) {
				
				indicatorClass = "indicator_positive" ;
				iconIndicatorArrow = "&#9650;" ;
				
			} else if ( changeInvestValuePercent < 0 ) {
				
				indicatorClass = "indicator_negative" ;
				iconIndicatorArrow = "&#9660;" ;
				
			}
			
			$( this ).html( iconIndicatorArrow + " " + 
				            percentFormatter.format( changeInvestValuePercent ) ).addClass( indicatorClass ) ;
			
			
		} ) ;
		
	} ) ;
	
} ) ;

