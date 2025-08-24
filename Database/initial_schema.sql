/* Set up initial database schema.
*/


/* Create new database: exchangeweb_db.
*/
CREATE DATABASE exchangeweb_db ;

USE exchangeweb_db ;



/* Create new table: authorities.
*/
CREATE TABLE authorities (

   id INT PRIMARY KEY AUTO_INCREMENT,
   name VARCHAR( 40 ) NOT NULL UNIQUE,
   
   CONSTRAINT authorities_name_does_not_start_with_prefix_role CHECK( SUBSTRING( name, 1, 5 ) != "ROLE_" )

) ;


/* Create new table: roles.
*/
CREATE TABLE roles (

   id INT PRIMARY KEY AUTO_INCREMENT,
   name VARCHAR( 40 ) NOT NULL UNIQUE,
   
   CONSTRAINT roles_name_starts_with_prefix_role CHECK( SUBSTRING( name, 1, 5 ) = "ROLE_" )

) ;


/* Create new table: roles_to_authorities.
*/
CREATE TABLE roles_to_authorities (

   id INT PRIMARY KEY AUTO_INCREMENT,
   roles_id INT NOT NULL,
   authorities_id INT NOT NULL,
   
   FOREIGN KEY( roles_id ) REFERENCES roles( id ),
   FOREIGN KEY( authorities_id ) REFERENCES authorities( id )

) ;


/* Create new table: accounts.
*/
CREATE TABLE accounts (

   id INT PRIMARY KEY AUTO_INCREMENT,
   username VARCHAR( 40 ) NOT NULL UNIQUE,
   password VARCHAR( 100 ) NOT NULL,
   is_account_non_expired BOOLEAN NOT NULL DEFAULT TRUE,
   is_account_non_locked BOOLEAN NOT NULL DEFAULT TRUE,
   is_credentials_non_expired BOOLEAN NOT NULL DEFAULT TRUE,
   is_enabled BOOLEAN NOT NULL DEFAULT TRUE,
   
   CONSTRAINT accounts_username_length_greater_than_zero CHECK( CHAR_LENGTH( username ) > 0 ),
   CONSTRAINT accounts_password_length_greater_than_zero CHECK( CHAR_LENGTH( password ) > 0 )

) ;





/* Create new table: accounts_to_roles.
*/
CREATE TABLE accounts_to_roles (

   id INT PRIMARY KEY AUTO_INCREMENT,
   accounts_id INT NOT NULL,
   roles_id INT NOT NULL,
   
   FOREIGN KEY( accounts_id ) REFERENCES accounts( id ),
   FOREIGN KEY( roles_id ) REFERENCES roles( id )

) ;


/* Create new table: accounts_to_authorities.
*/
CREATE TABLE accounts_to_authorities (

   id INT PRIMARY KEY AUTO_INCREMENT,
   accounts_id INT NOT NULL,
   authorities_id INT NOT NULL,
   
     
   FOREIGN KEY( accounts_id ) REFERENCES accounts( id ),
   FOREIGN KEY( authorities_id ) REFERENCES authorities( id )

) ;





/* Create new table: quotes.
*/
CREATE TABLE quotes (

   id INT PRIMARY KEY AUTO_INCREMENT,
   curr_price DECIMAL( 5, 2 ) NOT NULL,
   last_close_price DECIMAL( 5, 2 ) NOT NULL,
   change_price_absolute DECIMAL( 5, 2 ) NOT NULL,
   change_price_percent DECIMAL( 9, 2 ) NOT NULL,
   one_year_high_price DECIMAL( 5, 2 ) NOT NULL,
   one_year_low_price DECIMAL( 5, 2 ) NOT NULL

) ;


/* Create new table: companies.
*/
CREATE TABLE companies (

   id INT PRIMARY KEY AUTO_INCREMENT,
   name VARCHAR( 40 ) NOT NULL,
   symbol VARCHAR( 40 ) NOT NULL UNIQUE,
   description VARCHAR( 500 ) NOT NULL,
   quotes_id INT NOT NULL UNIQUE,
   
   FOREIGN KEY( quotes_id ) REFERENCES quotes( id ),
   
   CONSTRAINT companies_name_length_greater_than_zero CHECK( CHAR_LENGTH( name ) > 0 ),
   CONSTRAINT companies_symbol_length_greater_than_zero CHECK( CHAR_LENGTH( symbol ) > 0 )

) ;





/* Create new table: portfolios.
*/
CREATE TABLE portfolios (

   id INT PRIMARY KEY AUTO_INCREMENT,
   name VARCHAR( 40 ) NOT NULL,
   description VARCHAR( 500 ) NOT NULL,
   invest_start_day INT NOT NULL,
   invest_value_start DECIMAL( 14, 2 ) NOT NULL,
   invest_value_end DECIMAL( 14, 2 ) NOT NULL,
   change_invest_value_absolute DECIMAL( 14, 2 ) NOT NULL,
   change_invest_value_percent DECIMAL( 18, 2 ) NOT NULL,
   
   CONSTRAINT portfolios_name_length_greater_than_zero CHECK( CHAR_LENGTH( name ) > 0 )

) ;



/* Create new table: invest_slots.
*/
CREATE TABLE invest_slots (

   id INT PRIMARY KEY AUTO_INCREMENT,
   companies_id INT NOT NULL,
   invest_quantity INT NOT NULL,
   invest_price_start DECIMAL( 5, 2 ) NOT NULL,
   invest_price_end DECIMAL( 5, 2 ) NOT NULL,
   invest_value_start DECIMAL( 14, 2 ) NOT NULL,
   invest_value_end DECIMAL( 14, 2 ) NOT NULL, 
   change_invest_value_absolute DECIMAL( 14, 2 ) NOT NULL,
   change_invest_value_percent DECIMAL( 18, 2 ) NOT NULL,
   
   FOREIGN KEY( companies_id ) REFERENCES companies( id )

) ;


/* Create new table: portfolios_to_invest_slots.
*/
CREATE TABLE portfolios_to_invest_slots (

   id INT PRIMARY KEY AUTO_INCREMENT,
   portfolios_id INT NOT NULL,
   invest_slots_id INT NOT NULL UNIQUE,
   
   FOREIGN KEY( portfolios_id ) REFERENCES portfolios( id ),
   FOREIGN KEY( invest_slots_id ) REFERENCES invest_slots( id )
   
) ;


/* Create new table: accounts_to_portfolios.
*/
CREATE TABLE accounts_to_portfolios (

   id INT PRIMARY KEY AUTO_INCREMENT,
   accounts_id INT NOT NULL,
   portfolios_id INT NOT NULL UNIQUE,
   
   FOREIGN KEY( accounts_id ) REFERENCES accounts( id ),
   FOREIGN KEY( portfolios_id ) REFERENCES portfolios( id )

) ;




DELIMITER //

CREATE PROCEDURE refresh_portfolio( IN input_portfolios_to_invest_slots_id INT )
BEGIN
   DECLARE var_portfolios_id INT ;
   DECLARE var_portfolios_invest_value_start DECIMAL( 14, 2 ) ;
   DECLARE var_portfolios_invest_value_end DECIMAL( 14, 2 ) ;        
   DECLARE var_portfolios_change_invest_value_absolute DECIMAL( 14, 2 ) ;
   DECLARE var_portfolios_change_invest_value_percent DECIMAL( 18, 2 ) ;

   
   
   SELECT portfolios_id 
   INTO var_portfolios_id
   FROM portfolios_to_invest_slots 
   WHERE id = input_portfolios_to_invest_slots_id ;

   
   SELECT SUM( invest_slots.invest_value_start ) 
   INTO var_portfolios_invest_value_start
   FROM
   (
      SELECT *
      FROM portfolios_to_invest_slots 
      WHERE portfolios_id = var_portfolios_id
   ) 
   AS portfolio_specific_portfolios_to_invest_slots
   INNER JOIN invest_slots
   ON portfolio_specific_portfolios_to_invest_slots.invest_slots_id = invest_slots.id ;


   SELECT SUM( invest_slots.invest_value_end ) 
   INTO var_portfolios_invest_value_end
   FROM
   (
      SELECT *
      FROM portfolios_to_invest_slots 
      WHERE portfolios_id = var_portfolios_id
   ) 
   AS portfolio_specific_portfolios_to_invest_slots
   INNER JOIN invest_slots
   ON portfolio_specific_portfolios_to_invest_slots.invest_slots_id = invest_slots.id ;



   UPDATE portfolios
   SET portfolios.invest_value_start = var_portfolios_invest_value_start
   WHERE portfolios.id = var_portfolios_id ;


   UPDATE portfolios
   SET portfolios.invest_value_end = var_portfolios_invest_value_end
   WHERE portfolios.id = var_portfolios_id ;


       
   SET var_portfolios_change_invest_value_absolute = var_portfolios_invest_value_end - var_portfolios_invest_value_start ;
   
   IF var_portfolios_invest_value_start = 0 THEN 
      SET var_portfolios_change_invest_value_percent = (var_portfolios_change_invest_value_absolute / 0.01) * 100 ;
   ELSE 
      SET var_portfolios_change_invest_value_percent = (var_portfolios_change_invest_value_absolute / var_portfolios_invest_value_start) * 100 ;
   END IF ;


   UPDATE portfolios
   SET portfolios.change_invest_value_absolute = var_portfolios_change_invest_value_absolute
   WHERE portfolios.id = var_portfolios_id ;

   UPDATE portfolios
   SET portfolios.change_invest_value_percent = var_portfolios_change_invest_value_percent
   WHERE portfolios.id = var_portfolios_id ;
 

END //

DELIMITER ;



CREATE TRIGGER after_insert_on_portfolios_to_invest_slots
AFTER INSERT ON portfolios_to_invest_slots
FOR EACH ROW
CALL refresh_portfolio( NEW.id ) ;








