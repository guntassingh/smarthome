package com.pmitseas.devicemgmt.interceptor;

import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

public class HttpHandshakeInterceptor implements  HandshakeInterceptor {

	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Map<String, Object> attributes) throws Exception {
		System.out.println("Inside the before the handshake");
		System.out.println("Request headers are "+request.getHeaders().toString());
		System.out.println("Request body "+request.getBody());
		return true;
	}

	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Exception exception) {
		System.out.println("Inside the after the handshake");
		System.out.println("Response attributes are "+response.getHeaders().toString());
		
	}

}
