package com.pmitseas.devicemgmt.interceptor;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.web.socket.WebSocketSession;

import com.sun.jmx.snmp.Timestamp;


import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class FilterChannelInterceptor implements ChannelInterceptor {
	

	private Map<String, WebSocketSession> websocketSessionHolder;
	
	@Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        StompCommand command = accessor.getCommand();
        System.out.println("Detailed message is "+accessor.getDetailedLogMessage(message));
        MessageHeaders headers = accessor.getMessageHeaders();
        headers.forEach((key,value) -> {
        	System.out.println("The timestamp is : "+new Timestamp(System.currentTimeMillis())+" and The header name is "+key+ " and value is "+value);
        });
        
        if(command == StompCommand.CONNECT) {
        	System.out.println("This is CONNECT request");
        }
        if(command == StompCommand.SUBSCRIBE) {
        	System.out.println("This is SUBSCRIBE request");
        }
        if(command == StompCommand.DISCONNECT) {
        	System.out.println("This is DISCONNECT request");
        	System.out.println("The user is "+accessor.getUser().getName());
        	websocketSessionHolder.remove(accessor.getUser().getName());
        	System.out.println("Successfully removed "+accessor.getUser().getName());
        	
        }
        if(command == StompCommand.ERROR) {
        	System.out.println("This is ERROR request");
        }
        if(command == StompCommand.SEND) {
        	System.out.println("This is SEND request");
        }
        if(command == StompCommand.MESSAGE) {
        	System.out.println("This is MESSAGE request");
        }
    }
	
	

	
	
	
}
