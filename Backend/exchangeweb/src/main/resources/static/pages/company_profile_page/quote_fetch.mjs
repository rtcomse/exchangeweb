$( document ).ready( function () {
	
	const quoteId = Number.parseInt( $( "#script_arguments_2_quote_id" ).text() ) ;
	
	
	$.get( window.projectData.urlPrefixServer1 + 
		   `/quotes/${quoteId}`, function ( data, status ) {
		
		const retrievedQuote = data[ 0 ] ;
		

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
			
			currPrice: priceFormatter.format( retrievedQuote.currPrice ),
			lastClosePrice: priceFormatter.format( retrievedQuote.lastClosePrice ),
			changePriceAbsolute: priceChangeFormatter.format( retrievedQuote.changePriceAbsolute ),
			changePricePercent: percentFormatter.format( retrievedQuote.changePricePercent / 100.0 ),
			oneYearHighPrice: priceFormatter.format( retrievedQuote.oneYearHighPrice ),
			oneYearLowPrice: priceFormatter.format( retrievedQuote.oneYearLowPrice )
			
		} ;
		

		let indicatorClass = "" ;
		let iconIndicatorArrow = "" ;
		let iconSymbolSeperator = "&#9679;" ;

		if ( retrievedQuote.changePricePercent > 0 ) {
			
			indicatorClass = "indicator_positive" ;
			iconIndicatorArrow = "&#9650;" ;
			
		} else if ( retrievedQuote.changePricePercent < 0 ) {
			
			indicatorClass = "indicator_negative" ;
			iconIndicatorArrow = "&#9660;" ;
			
		}

		const markupSpan = "<span " + 
						   "class=\"" + indicatorClass + "\"" +
						   ">" + 
						      iconIndicatorArrow + " " +
						      formatted.changePricePercent + " " + 
						   "</span>" ;

		
		$( "#quote_block_id_1 .company_current_price_block .content > p" ).text( formatted.currPrice ) ;
		$( "#quote_block_id_1 .company_price_change_block .content > p" ).html( markupSpan ) ;
		
		$( "#quote_block_id_1 .company_last_close_price_block .content > p" ).text( formatted.lastClosePrice ) ;
		$( "#quote_block_id_1 .company_change_price_absolute_block .content > p" ).text( formatted.changePriceAbsolute ) ;
		$( "#quote_block_id_1 .company_change_price_percent_block .content > p" ).text( formatted.changePricePercent ) ;
		$( "#quote_block_id_1 .company_one_year_high_price_block .content > p" ).text( formatted.oneYearHighPrice ) ;
		$( "#quote_block_id_1 .company_one_year_low_price_block .content > p" ).text( formatted.oneYearLowPrice ) ;
		
	} ) ;
	
} ) ;

