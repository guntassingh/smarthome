package com.pmitseas.control;

import java.io.IOException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.nats.client.Connection;
import io.nats.client.Nats;
import io.nats.client.Options;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class NatsClientConfiguration {
	
	private static final NatsClientConfiguration instance = new NatsClientConfiguration();
	
	
	@Bean("natsConnection")
	public Connection natsConnection() throws IOException, InterruptedException{
		
		Options o = new Options.Builder().server(Options.DEFAULT_URL).maxReconnects(-1).build();
		Connection nc = Nats.connect(o);
	    
	    return nc;
	 
	}

}
