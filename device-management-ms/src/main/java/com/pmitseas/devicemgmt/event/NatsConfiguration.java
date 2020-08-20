package com.pmitseas.devicemgmt.event;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pmitseas.devicemgmt.service.ClientMgmtService;
import io.nats.client.Connection;
import io.nats.client.Dispatcher;
import io.nats.client.Nats;
import io.nats.client.Options;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Configuration
@NoArgsConstructor
@Getter
@Setter
public class NatsConfiguration {
	
	@Autowired
	private NatsMessagingService natsService;

	@Bean("natsConnection")
	public Connection natsConnection() throws IOException, InterruptedException{
		
		Options o = new Options.Builder().server(Options.DEFAULT_URL).maxReconnects(-1).build();
		Connection nc = Nats.connect(o);
		
		Dispatcher d = nc.createDispatcher((msg) -> {
			String response = new String(msg.getData(), StandardCharsets.UTF_8);
			ObjectMapper mapper = new ObjectMapper();
			
				ModerationAction event = new ModerationAction();
				try {
					event = mapper.readValue(response, ModerationAction.class);
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				try {
				natsService.recieveMessage(event);
				} catch (Exception e ) {
					e.printStackTrace();
				}
			
			
		}).subscribe("subject");
	    
	    return nc;
	 
	}
	
	
	

	
	
	
}
