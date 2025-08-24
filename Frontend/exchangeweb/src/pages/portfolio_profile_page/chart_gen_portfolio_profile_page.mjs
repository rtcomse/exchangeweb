import * as d3 from "d3" ;

export function createChart ( dataLabels, 
                              portfolioValueList,
                              investSlotValueLists,
                              investSlotValueStartIndexList ) {

   const numDataPoints = portfolioValueList.length ;
   const minPrice = 0 ;
   const maxPrice = Math.max( ...portfolioValueList ) ;

   const brightnessStep = 100 / ( investSlotValueLists.length ) ;

   const chartDataPortfolio = [] ;

   for ( let i = 0 ; i < portfolioValueList.length ; i++ ) {

         let dataItem = {

            day: -(portfolioValueList.length - 1) + i,
            investValue: portfolioValueList[ i ]
         } ;

         chartDataPortfolio.push( dataItem ) ;
   }


   const chartDataInvestSlotList = [] ;

   for ( let i = 0 ; i < investSlotValueLists.length ; i++ ) {

      chartDataInvestSlotList[ i ] = [] ;

      for ( let j = 0 ; j < investSlotValueLists[ i ].length ; j++ ) {

         let dataItem = {

            day: -(investSlotValueLists[ i ].length - 1) + j,
            investValue: investSlotValueLists[ i ][ j ]
         } ;

         chartDataInvestSlotList[ i ].push( dataItem ) ;
      }

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

   const chartTitleLabelContent = "Portfolio Investment Value" ;
   const xAxisLabelContent = "Day" ;
   const yAxisLabelContent = "Investment Value" ;

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
                  .y( d => yScale( d.investValue ) ) ;

   for ( let i = 0 ; i < chartDataInvestSlotList.length ; i++ ) {

      svg.append( "path" )
         .attr( "d", line( chartDataInvestSlotList[ i ] ) )
         .attr( "id", `data_path_invest_slot_${i}` )
         .classed( "data_path", true )
         .style( "filter", `brightness(${100 + (brightnessStep * (i+1))}%)` ) ;
               
   }
               
   svg.append( "path" )
      .attr( "d", line( chartDataPortfolio ) )
      .attr( "id", "data_path_portfolio" )
      .classed( "data_path", true ) ;

   const dataHighlightGroup = svg.append( "g" )
                                 .attr( "id", "data_highlight_group" ) ;


   for ( let i = 0 ; i < chartDataPortfolio.length ; i++ ) {

      const currDataItem = chartDataPortfolio[ i ] ;

      const dataItemHighlightGroup = dataHighlightGroup.append( "g" ) ;

      dataItemHighlightGroup.append( "line" )
                            .attr( "x1", xScale( currDataItem.day ) )
                            .attr( "y1", xAxisPlacementTop )
                            .attr( "x2", xScale( currDataItem.day ) )
                            .attr( "y2", yScale( currDataItem.investValue ) ) ;

      dataItemHighlightGroup.append( "circle" )
                            .attr( "cx", xScale( currDataItem.day ) )
                            .attr( "cy", yScale( currDataItem.investValue ) )
                            .attr( "r", 10 )
                            .attr( "data-bs-toggle", "tooltip" )
                            .attr( "data-bs-placement", "top" )
                            .attr( "data-bs-html", "true" )
                            .attr( "data-bs-title", 
                                   `Portfolio: <br /> ${dataLabels.portfolioLabel} <hr /> 
                                   Day: ${currDataItem.day}, <br /> 
                                   Value: ${currDataItem.investValue.toFixed( 2 )}` ) ;


      for ( let j = 0 ; j < chartDataInvestSlotList.length ; j++ ) {

         if ( i >= investSlotValueStartIndexList[ j ] ) {

            const currInvestValueIndex = i - investSlotValueStartIndexList[ j ] ;

            const currInvestValueDataItem = chartDataInvestSlotList[ j ][ currInvestValueIndex ] ;


            dataItemHighlightGroup.append( "circle" )
                                  .attr( "cx", xScale( currInvestValueDataItem.day ) )
                                  .attr( "cy", yScale( currInvestValueDataItem.investValue ) )
                                  .attr( "r", 10 )
                                  .attr( "data-bs-toggle", "tooltip" )
                                  .attr( "data-bs-placement", "top" )
                                  .attr( "data-bs-html", "true" )
                                  .attr( "data-bs-title", 
                                         `Company: <br /> ${dataLabels.investSlotLabelList[ j ]} <hr /> 
                                         Day: ${currInvestValueDataItem.day}, <br />  
                                         Value: ${currInvestValueDataItem.investValue.toFixed( 2 )}` ) 
                                  .style( "filter", `brightness(${100 + (brightnessStep * (j+1))}%)` ) ;

         }

                                            
      }
                              
   }
   

   return svg.node() ;

}
