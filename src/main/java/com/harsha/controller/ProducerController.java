package com.harsha.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.harsha.dto.MessageDto;
import com.harsha.service.ProducerService;

@RestController
@RequestMapping("/api/v1")
public class ProducerController {

	@Autowired
	private ProducerService producerService;
	
	@PostMapping("/produce")
	public String sendMessage(@RequestBody MessageDto message) {
		return producerService.sendMessage(message.getBroker(), message.getTopic(), message.getMessage());
		
	}
	
	
}
