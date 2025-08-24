import * as d3 from "d3" ;

export function createChart ( priceList ) {

   const numDataPoints = priceList.length ;
   const minPrice = Math.min( ...priceList ) ;
   const maxPrice = Math.max( ...priceList ) ;

   const chartData = [] ;

   for ( let i = 0 ; i < priceList.length ; i++ ) {

         let dataItem = {

            day: -(priceList.length - 1) + i,
            price: priceList[ i ]
         } ;

         chartData.push( dataItem ) ;
   }


   const height = 1000 ;
   const width = 1000 ;
   
   const marginTop = 100 ;
   const marginBottom = 100 ;
   const marginLeft = 150 ;
   const marginRight = 100 ;

   const xScaleStart = marginLeft ;
   const xScaleEnd = width - marginRight ;
   const yScaleStart = height - marginBottom ;
   const yScaleEnd = marginTop ;

   const xAxisLength = xScaleEnd - xScaleStart ;
   const xAxisMidpointLength = xAxisLength / 2 ;
   const yAxisLength = yScaleStart - yScaleEnd ;
   const yAxisMidpointLength = yAxisLength / 2 ;

   const xAxisPlacementLeft = 0 ;
   const xAxisPlacementTop = height - marginBottom ;

   const yAxisPlacementLeft = marginLeft ;
   const yAxisPlacementTop = 0 ;


   const xScale = d3.scaleLinear()
                    .domain( [ -(numDataPoints - 1), 0 ] )
                    .range( [ xScaleStart, xScaleEnd ] ) ;

   const yScale = d3.scaleLinear()
                    .domain( [ minPrice, maxPrice ] )
                    .range( [ yScaleStart, yScaleEnd ] ) ;


   const xTickValues = xScale.ticks()
                             .filter( d => Number.isInteger( d ) ) ;

   const yTickValues = yScale.ticks()
                             .filter( d => Number.isInteger( d ) ) ;


   const xAxis = d3.axisBottom( xScale )
                   .tickValues( xTickValues )
                   .tickFormat( d3.format( "d" ) ) ;

   const yAxis = d3.axisLeft( yScale )
                   .tickValues( yTickValues )
                   .tickFormat( d3.format( "d" ) ) ;

    
   const svg = d3.create( "svg" )
                 .attr( "width", width )
                 .attr( "height", height )
                 .attr( "viewBox", `0 0 ${width} ${height}` )
                 .attr( "preserveAspectRatio", `xMidYMid meet` ) ;

   svg.append( "g" )
      .attr( "transform", `translate( ${xAxisPlacementLeft}, ${xAxisPlacementTop} )` )
      .call( xAxis ) ;

   svg.append( "g" )
      .attr( "transform", `translate( ${yAxisPlacementLeft}, ${yAxisPlacementTop} )` )
      .call( yAxis ) ;

   const chartTitleLabelContent = "Stock Price" ;
   const xAxisLabelContent = "Day" ;
   const yAxisLabelContent = "Price" ;

   const chartTitleLabelGap = 40 ;
   const xAxisLabelGap = 80 ;
   const yAxisLabelGap = 100 ;

   const chartTitleLabelPlacementLeft = marginLeft + xAxisMidpointLength ;
   const chartTitleLabelPlacementTop = marginTop - chartTitleLabelGap ;

   const xAxisLabelPlacementLeft = marginLeft + xAxisMidpointLength ;
   const xAxisLabelPlacementTop = xAxisPlacementTop + xAxisLabelGap ;

   const yAxisLabelPlacementLeft = yAxisPlacementLeft - yAxisLabelGap ;
   const yAxisLabelPlacementTop = marginTop + yAxisMidpointLength ;


   svg.append( "text" )
      .attr( "x", chartTitleLabelPlacementLeft )
      .attr( "y", chartTitleLabelPlacementTop )
      .attr( "text-anchor", "middle" )
      .attr( "id", `data_label_chart_title` )
      .text( chartTitleLabelContent ) ;
   
   svg.append( "text" )
      .attr( "x", xAxisLabelPlacementLeft )
      .attr( "y", xAxisLabelPlacementTop )
      .attr( "text-anchor", "middle" )
      .attr( "id", `data_label_x_axis` )
      .text( xAxisLabelContent ) ;

   svg.append( "text" )
      .attr( "x", yAxisLabelPlacementLeft )
      .attr( "y", yAxisLabelPlacementTop )
      .attr( "text-anchor", "middle" )
      .attr( "transform", `rotate( -90, ${yAxisLabelPlacementLeft}, ${yAxisLabelPlacementTop} )` )
      .attr( "id", `data_label_y_axis` )
      .text( yAxisLabelContent ) ;



   const line = d3.line()
                  .x( d => xScale( d.day ) )
                  .y( d => yScale( d.price ) ) ;

   svg.append( "path" )
      .attr( "id", "data_path" )
      .attr( "d", line( chartData ) ) ;


   const dataHighlightGroup = svg.append( "g" )
                                 .attr( "id", "data_highlight_group" ) ;


   for ( let i = 0 ; i < chartData.length ; i++ ) {

      const currDataItem = chartData[ i ] ;

      const dataItemHighlightGroup = dataHighlightGroup.append( "g" ) ;

      dataItemHighlightGroup.append( "line" )
                            .attr( "x1", xScale( currDataItem.day ) )
                            .attr( "y1", xAxisPlacementTop )
                            .attr( "x2", xScale( currDataItem.day ) )
                            .attr( "y2", yScale( currDataItem.price ) ) ;

      dataItemHighlightGroup.append( "circle" )
                            .attr( "cx", xScale( currDataItem.day ) )
                            .attr( "cy", yScale( currDataItem.price ) )
                            .attr( "r", 10 )
                            .attr( "data-bs-toggle", "tooltip" )
                            .attr( "data-bs-placement", "top" )
                            .attr( "data-bs-title", 
                                   `Day: ${currDataItem.day}, Price: ${currDataItem.price.toFixed( 2 )}` ) ;


   }
   

   return svg.node() ;

}
