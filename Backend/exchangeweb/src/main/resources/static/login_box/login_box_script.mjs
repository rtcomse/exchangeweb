$( document ).ready( function () {
	
	$.get( window.projectData.urlPrefixServer1 + 
		   "/user_details/current_user/username", function ( data, status ) {
		
		
		const retrievedCurrentUserUsernameList = data ;
		
		
		let currentUserUsername = "" ;
		
		if ( retrievedCurrentUserUsernameList.length == 0 ) {
			
			currentUserUsername = "Not logged in." ;
			
			$( "#login_box_logout_button" ).addClass( "hide_login_box_button" ) ;
			$( "#login_box_delete_button" ).addClass( "hide_login_box_button" ) ;
			$( "#login_box_login_button" ).removeClass( "hide_login_box_button" ) ;
			$( "#login_box_signup_button" ).removeClass( "hide_login_box_button" ) ;
			
		}
		else {
			
			currentUserUsername = retrievedCurrentUserUsernameList[ 0 ] ;
			
			$( "#login_box_login_button" ).addClass( "hide_login_box_button" ) ;
			$( "#login_box_signup_button" ).addClass( "hide_login_box_button" ) ;
			$( "#login_box_logout_button" ).removeClass( "hide_login_box_button" ) ;
			$( "#login_box_delete_button" ).removeClass( "hide_login_box_button" ) ;
			
		}

			
		$( "#login_box_block_id_1 .content" ).append( currentUserUsername ) ;
		
		
		
	} ) ;
	
			
	$( "#login_box_block_id_1 .modal-body form" ).on( "submit", function () {

		$( ".modal-footer button[type=\"submit\"][form=\"" + this.getAttribute( "id" ) + "\"]" ).each( function () {
			
			const loginBoxPlaceholdersFormSubmitNode = $( ".login_box_placeholders_form_submit" ).clone( true, true ) ;
			
			$( this ).prepend( loginBoxPlaceholdersFormSubmitNode ) ;
						 	 	 		
		} ) ;
	 	 	 		
	} ) ;	
	
	
	
	
} ) ;

