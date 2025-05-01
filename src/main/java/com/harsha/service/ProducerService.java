package com.harsha.service;

import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ProducerService {

	public String sendMessage(String broker, String topic,String message) {
		Properties properties=new Properties();
		properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, broker);
		properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		try(KafkaProducer<String, String> producer=new KafkaProducer<String, String>(properties)){
			ProducerRecord<String, String> record=new ProducerRecord<String, String>(topic, message);
			producer.send(record);
			producer.flush();
			System.out.println("Message sent");
			return "Message sent to broker" + broker + " on topic " + topic;
			
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return "Error in producing message";
		}
	}

	public Set<String> getAllTopics(String broker){
		Properties config = new Properties();
	    config.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, broker);
	    config.put(AdminClientConfig.REQUEST_TIMEOUT_MS_CONFIG, "3000");
	    config.put(AdminClientConfig.DEFAULT_API_TIMEOUT_MS_CONFIG, "3000");

	    try (AdminClient adminClient = AdminClient.create(config)) {
	        ListTopicsResult topics = adminClient.listTopics();
	        return topics.names().get();  // Blocking call
	    } catch (InterruptedException | ExecutionException e) {
	        System.err.println("Failed to connect to Kafka broker: " + broker);
	        System.err.println("Error: " + e.getMessage());
	        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Broker connection failed or broker is unavailable");

	    }
	}
	public String createTopic(String broker, String topicName) {
        Properties config = new Properties();
        config.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, broker);

        try (AdminClient adminClient = AdminClient.create(config)) {
            NewTopic newTopic = new NewTopic(topicName, 1,(short)1);
            adminClient.createTopics(Collections.singleton(newTopic)).all().get();
            return "Topic '" + topicName + "' created successfully.";
        } catch (ExecutionException | InterruptedException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to create topic: ");
        }
    }
}
