package com.harsha.service;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
}
