package com.pmitseas.devicemgmt.handler;

import javax.jms.TextMessage;

import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class MyHandler extends TextWebSocketHandler {

	public void handleTextMessage(WebSocketSession session, TextMessage message) {
		
	}

}

