package com.pmitseas.control.event;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;

import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pmitseas.control.service.SseService;

import io.nats.client.Connection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NatsMessagingService {
	
	private final Connection natsConnection;
	private final SseService sseService;
	private final MessageConverter jacksonJmsMessageConverter;
	
	
	public void send(ModerationAction message) {
		try {
			
			ObjectMapper mapper = new ObjectMapper();
	
		    String jsonString = mapper.writeValueAsString(message);
		    System.out.println("the converted JSON string is "+jsonString);
			
			natsConnection.publish("subject",jsonString.getBytes(StandardCharsets.UTF_8));
			log.info("received message="+message);
			SseEmitter emitter = sseService.getEmitterMap().get(message.getEndpointGuid());
			if (emitter == null)
				log.info("No emitter found for this message: {}", message);
			else {
				try {
					emitter.send("OK");
				} catch (IOException e) {
					log.info("Error while sending emitter data", e);
				} finally {
					emitter.complete();
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

}
