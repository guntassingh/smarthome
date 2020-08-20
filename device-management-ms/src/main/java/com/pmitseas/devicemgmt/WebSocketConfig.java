package com.pmitseas.devicemgmt;


import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.WebSocketMessageBrokerStats;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;
import org.springframework.web.socket.handler.WebSocketHandlerDecoratorFactory;
import org.springframework.web.socket.messaging.DefaultSimpUserRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

import com.pmitseas.devicemgmt.interceptor.FilterChannelInterceptor;
import com.pmitseas.devicemgmt.interceptor.HttpHandshakeInterceptor;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	
	@Autowired
	private WebSocketMessageBrokerStats webSocketMessageBrokerStats;
	

  


    @Value("${broker.relay.host}")
    private String brokerRelayHost;

    @Value("${broker.relay.user}")
    private String brokerRelayUser;

    @Value("${broker.relay.password}")
    private String brokerRelayPass;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
    	config.enableSimpleBroker("/queue", "/topic")
    	.setHeartbeatValue(new long[] {10000, 20000})
        .setTaskScheduler(threadPoolTaskScheduler());
    	
    	config.setApplicationDestinationPrefixes("/app");
		
		/*
		 * config.enableStompBrokerRelay("/queue", "/topic")
		 * .setRelayHost(brokerRelayHost) .setClientLogin(brokerRelayUser)
		 * .setClientPasscode(brokerRelayPass) .setSystemLogin(brokerRelayUser)
		 * .setSystemPasscode(brokerRelayPass)
		 * .setUserDestinationBroadcast("/topic/unresolved-user")
		 * .setUserRegistryBroadcast("/topic/log-user-registry");
		 */
		 
        
       
    }
    
    
    @Bean
    @Scope(value = "singleton")
    public Map<String, WebSocketSession> websocketSessionHolder(){
         Map<String, WebSocketSession> websocketSessionHolder = new HashMap<>();
         return websocketSessionHolder;
    }
    
    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        registration.addDecoratorFactory(new WebSocketHandlerDecoratorFactory() {
            @Override
            public WebSocketHandler decorate(final WebSocketHandler handler) {
                return new WebSocketHandlerDecorator(handler) {

                    @Override
                    public void afterConnectionEstablished(final WebSocketSession session) throws Exception {

                        // We will store current user's session into WebsocketSessionHolder after connection is established
                        String username = session.getPrincipal().getName();
                        websocketSessionHolder().put(username, session);

                        super.afterConnectionEstablished(session);
                    }
                };
            }
        });
    }
    

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/websocket")
        .setAllowedOrigins("*")
        .withSockJS()
        .setInterceptors(new HttpHandshakeInterceptor());
    }

	@Bean // experimental
	public DefaultSimpUserRegistry getDefaultSimpRegistry() {
		return new DefaultSimpUserRegistry();
	}
	
	@Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new FilterChannelInterceptor());
    }
	
	@Bean
	public ServletServerContainerFactoryBean createWebSocketContainer() {
		ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
		container.setMaxTextMessageBufferSize(8192);
		container.setMaxBinaryMessageBufferSize(8192);
		return container;
	}
	
	@Bean
	public WebSocketMessageBrokerStats stats(){
		return webSocketMessageBrokerStats;
	}
	
	@Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler(){
        ThreadPoolTaskScheduler threadPoolTaskScheduler
          = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(5);
        threadPoolTaskScheduler.setThreadNamePrefix(
          "ThreadPoolTaskScheduler");
        return threadPoolTaskScheduler;
    }
}
	
