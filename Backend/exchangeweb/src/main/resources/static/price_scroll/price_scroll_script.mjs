$( document ).ready( function () {
	
	$.get( window.projectData.urlPrefixServer1 + 
		   "/companies", function ( data, status ) {
		
		const companiesArray = data ;
		

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

		
		
		let markupPriceScroll = "" ;
		
		for ( let i = 0 ; i < companiesArray.length ; i++ ) {
			
			const symbol = companiesArray[ i ].symbol ;
			const currentPrice = companiesArray[ i ].quote.currPrice ;
			const changePricePercent = companiesArray[ i ].quote.changePricePercent ;
			
						
			const currentPriceFormatted = priceFormatter.format( currentPrice ) ;
			const changePricePercentFormatted = percentFormatter.format( changePricePercent / 100.0 ) ;
			
			
			const divClass = "price_scroll_item" ;
			
			let indicatorClass = "" ;
			let iconIndicatorArrow = "" ;
			let iconSymbolSeperator = "&#9679;" ;
			
			if ( changePricePercent > 0 ) {
				
				indicatorClass = "indicator_positive" ;
				iconIndicatorArrow = "&#9650;" ;
				
			} else if ( changePricePercent < 0 ) {
				
				indicatorClass = "indicator_negative" ;
				iconIndicatorArrow = "&#9660;" ;
				
			}
			
			const markupDiv = "<div " +	
			                  "class=\"" + divClass + "\"" + 
							  ">" + 
							     symbol + " " + 
							     iconSymbolSeperator + " " +
							     currentPriceFormatted + " " + 
							     "<span " + 
								 "class=\"" + indicatorClass + "\"" +
								 ">" + 
							        iconIndicatorArrow + " " +
							        changePricePercentFormatted + " " + 
								 "</span>" + 
							  "</div>" ;
							  
			markupPriceScroll += markupDiv ;
							  		
		}
		
		$( "#price_scroll_holder_1" ).append( markupPriceScroll ) ;
		$( "#price_scroll_holder_2" ).append( markupPriceScroll ) ;
		
	} ) ;
	
} ) ;

