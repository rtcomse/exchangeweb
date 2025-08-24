const path = require( "path" ) ;

module.exports = {

    entry: {

        chart_gen_company_profile_page: {

            import: "./src/pages/company_profile_page/chart_gen_company_profile_page.mjs",
            
            filename: path.join( "pages",
                                 "company_profile_page",
                                 "bundle_chart_gen_company_profile_page.js" ),


        },
        chart_gen_portfolio_profile_page: {

            import: "./src/pages/portfolio_profile_page/chart_gen_portfolio_profile_page.mjs",
            
            filename: path.join( "pages",
                                 "portfolio_profile_page",
                                 "bundle_chart_gen_portfolio_profile_page.js" ),


        }

    },

    experiments: {

        outputModule: true
    },

    output: {

        path: path.resolve( __dirname, "dist" ),

        library: {

            type: "module"
        },
        
        clean: true
    }
} ;