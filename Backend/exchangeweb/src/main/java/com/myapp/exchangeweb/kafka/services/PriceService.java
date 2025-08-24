package com.myapp.exchangeweb.kafka.services;

import java.time.Duration;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.DeleteTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.KafkaFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.myapp.exchangeweb.models.Company;



@Service
public class PriceService {
	
	
	@Value( "${com.myapp.exchangeweb.kafka.servers}" )
	private String kafkaServers ;
	
	// LOGGER
	private static final Logger LOGGER = LoggerFactory.getLogger( PriceService.class ) ;
	
	
		
	public List<Double> findByDays ( Company inputCompany, Integer numDays ) {
		
		LOGGER.debug( "ENTER: findByDays ()" ) ;
		LOGGER.debug( "inputCompany: {}", inputCompany ) ;
		LOGGER.debug( "numDays: {}", numDays ) ;
		
		List<Double> retPriceList = new ArrayList<Double>() ;
		
		if ( numDays.intValue() <= 0 ) {
			
			LOGGER.error( "Non-positive value for numDays: {}", numDays.intValue() ) ;
			LOGGER.debug( "EXIT: findByDays ()" ) ;
			
			return retPriceList ;
			
		}
		
		
		Company requestedCompany = inputCompany ;
		
				
		Properties consumerProperties = new Properties() ;
		consumerProperties.setProperty("bootstrap.servers", this.kafkaServers) ;	
		consumerProperties.setProperty("group.id", UUID.randomUUID().toString() ) ;	
		consumerProperties.setProperty("auto.offset.reset", "earliest") ;		
		consumerProperties.setProperty("enable.auto.commit", "true") ;
		consumerProperties.setProperty("auto.commit.interval.ms", "100") ;		
		consumerProperties.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer") ;
		consumerProperties.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer") ;
	
		try ( 
				KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>( consumerProperties ) ;
				
			) {
			
			
			kafkaConsumer.subscribe( Arrays.asList( requestedCompany.getSymbol() ) ) ;
			
			Deque<Double> priceDeque = new ArrayDeque<Double>() ;
			
			while ( true ) {
				
				LOGGER.debug( "Kafka: Polling loop start." ) ;
				
				ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll( Duration.ofMillis( 1000 ) ) ;
				
				
				if ( kafkaConsumer.assignment().isEmpty() ) {
					
					LOGGER.debug( "Kafka: Topic partition assignment is empty." ) ;
					
					continue ;
					
				}
				
				
				LOGGER.debug( "Kafka: Topic partition assigned: {}", kafkaConsumer.assignment().toString() ) ;
				
				
				if ( consumerRecords.isEmpty() ) {
					
					LOGGER.debug( "Kafka: No new records were fetched." ) ;
					
					break ;
					
				}
				
				
				LOGGER.debug( "Kafka: Number of new records fetched: {}", consumerRecords.count() ) ;
				
				LOGGER.debug( "Kafka: Before inserting new records, priceDeque.size(): {}", priceDeque.size() ) ;
				
				for ( ConsumerRecord<String, String> consumerRecord : consumerRecords ) {
					
					Double currPrice = Double.valueOf( consumerRecord.value() )  ;
					
					if ( priceDeque.size() == numDays ) {
						
						priceDeque.removeFirst() ;
						
					}
					
					priceDeque.addLast( currPrice ) ;
					
				}
				
				LOGGER.debug( "Kafka: After inserting new records, priceDeque.size(): {}", priceDeque.size() ) ;
				
			}

			retPriceList.addAll( priceDeque ) ;			

			kafkaConsumer.unsubscribe() ;
			
			
		}
		
		
		LOGGER.debug( "EXIT: findByDays ()" ) ;		
		
		return retPriceList ;
		
	}

	
	public Boolean appendByDays ( Company inputCompany, List<Double> priceList ) {
		
		LOGGER.debug( "ENTER: appendByDays ()" ) ;
		LOGGER.debug( "inputCompany: {}", inputCompany ) ;
		LOGGER.debug( "priceList: {}", priceList ) ;
		
		Boolean retAppendSuccessful = Boolean.valueOf( false ) ;
		
		
		Company requestedCompany = inputCompany ;
		
				
		Properties producerProperties = new Properties() ;
		producerProperties.setProperty("bootstrap.servers", this.kafkaServers) ;	
		producerProperties.setProperty("linger.ms", "1") ;					
		producerProperties.setProperty("key.serializer", "org.apache.kafka.common.serialization.StringSerializer") ;
		producerProperties.setProperty("value.serializer", "org.apache.kafka.common.serialization.StringSerializer") ;
		
		try ( 
				KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>( producerProperties ) ;
				
			) {
			
			for ( int i = 0 ; i < priceList.size() ; i++ ) {
				
				kafkaProducer.send( new ProducerRecord<String, String>( requestedCompany.getSymbol(), 
						                                                priceList.get( i ).toString() ) ) ;
				
			}
						
			
		}
		
		
		retAppendSuccessful = Boolean.valueOf( true ) ;
		
		
		LOGGER.debug( "EXIT: appendByDays ()" ) ;
		
		return retAppendSuccessful ;
		
	}
	
	
	
	public Boolean createTopic ( Company inputCompany ) {
		
		LOGGER.debug( "ENTER: createTopic ()" ) ;
		
		Boolean retCreateSuccessful = Boolean.valueOf( false ) ;
		
		
		Company requestedCompany = inputCompany ;
		
		Properties adminProperties = new Properties() ;
		adminProperties.setProperty( AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, this.kafkaServers ) ;
		
		try ( 
				Admin admin = Admin.create( adminProperties ) ;
				
			) {
			
			String currTopicName = requestedCompany.getSymbol() ;
			int currTopicNumPartitions = 1 ;
			short currTopicReplicationFactor = 1 ;
			
			
			NewTopic currTopic = new NewTopic( currTopicName, 
					                           currTopicNumPartitions, 
					                           currTopicReplicationFactor ) ;
			
			List<NewTopic> topicList = new ArrayList<NewTopic>() ;
			topicList.add( currTopic ) ;
			
			CreateTopicsResult result = admin.createTopics( topicList ) ;
			
			KafkaFuture<Void> future = result.values().get( currTopicName ) ;
			
			future.get() ;
						
			
		}
		catch ( Exception e ) {
			
			LOGGER.error( "Exception: {}", e ) ;
			
		}
		
		
		retCreateSuccessful = Boolean.valueOf( true ) ;
		
		
		LOGGER.debug( "EXIT: createTopic ()" ) ;
		
		return retCreateSuccessful ;
		
	}
	
	
	public Boolean deleteTopic ( Company inputCompany ) {
		
		LOGGER.debug( "ENTER: deleteTopic ()" ) ;
		LOGGER.debug( "inputCompany: {}", inputCompany ) ;
		
		Boolean retDeleteSuccessful = Boolean.valueOf( false ) ;
		
				
		Company requestedCompany = inputCompany ;
		
		Properties adminProperties = new Properties() ;
		adminProperties.setProperty( AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, this.kafkaServers ) ;
		
		try ( 
				Admin admin = Admin.create( adminProperties ) ;
				
			) {
			
			String currTopicName = requestedCompany.getSymbol() ;
			
			List<String> topicNameList = new ArrayList<String>() ;
			topicNameList.add( currTopicName ) ;
			
			DeleteTopicsResult result = admin.deleteTopics( topicNameList ) ;
			
			KafkaFuture<Void> future = result.topicNameValues().get( currTopicName ) ;
			
			future.get() ;
						
			
		}
		catch ( Exception e ) {
			
			LOGGER.error( "Exception: {}", e ) ;
			
		}
		
		
		retDeleteSuccessful = Boolean.valueOf( true ) ;
		
		
		LOGGER.debug( "EXIT: deleteTopic ()" ) ;
		
		return retDeleteSuccessful ;
		
	}
	
	
	

}
