package com.pmitseas.devicemgmt.controller;

import com.pmitseas.devicemgmt.model.ResponseMessage;
import com.pmitseas.devicemgmt.service.ClientMgmtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.WebSocketMessageBrokerStats;
import org.springframework.web.socket.messaging.DefaultSimpUserRegistry;

import java.io.IOException;
import java.security.Principal;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequiredArgsConstructor
@Slf4j
public class WebsocketController {

	private final ClientMgmtService clientMgmtService;
	private final DefaultSimpUserRegistry getDefaultSimpRegistry;
	private final WebSocketMessageBrokerStats stats;
	private final Map<String, WebSocketSession> websocketSessionHolder;

	@MessageMapping("/device")
	public void handleMessageFromDevice(@Payload ResponseMessage message, Principal principal) {
		log.info("Received message from device: {}", principal.getName());
		//deviceMgmtService.handleMessageFromDevice(message);
	}
	
	@GetMapping("/users")
	@ResponseBody
	public Set<Result> getUsers() {
		Set<SimpUser> users = getDefaultSimpRegistry.getUsers();
		
		Set<Result> results = new HashSet<Result>();
		for(SimpUser user : users) {
			Result result = new Result();
			result.setName(user.getName());
			result.setConnections(getDefaultSimpRegistry.getUserCount());
			results.add(result);
		}
		return results;
	}
	
	@GetMapping("/userStats")
	@ResponseBody
	public Result getUserStats() {
		String inboundStats = stats.getClientInboundExecutorStatsInfo();
		String outboundStats = stats.getClientOutboundExecutorStatsInfo();
		Result result = new Result();
		result.setInboundStats(inboundStats);
		result.setOutboundStats(outboundStats);
		return result;
	}
	
	
	@GetMapping("/disconnect/{id}")
	@ResponseBody
	public ResponseEntity disconnectUser(HttpServletResponse response, @PathVariable String id) {
		System.out.println("the id is "+id);
		WebSocketSession session = websocketSessionHolder.get(id);
		if (session != null) {
			try {
				session.close(CloseStatus.NORMAL);
				websocketSessionHolder.remove(id);
				return new ResponseEntity(HttpStatus.OK);
			} catch (IOException e) {
				e.printStackTrace();
				return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}else {
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	@GetMapping("/disconnectAll")
	@ResponseBody
	public ResponseEntity disconnectAll() {
		for(WebSocketSession session : websocketSessionHolder.values()) {
			try {
				session.close(CloseStatus.NORMAL);
				
			} catch (IOException e) {
				e.printStackTrace();
				return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		
			}
		}
		websocketSessionHolder.clear();
		return new ResponseEntity(HttpStatus.OK);
		
		
	}
	
	
	private class Result{
		
		private String name;
		private int connections;
		private String inboundStats;
		private String outboundStats;
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getConnections() {
			return connections;
		}
		public void setConnections(int connections) {
			this.connections = connections;
		}
		public String getInboundStats() {
			return inboundStats;
		}
		public void setInboundStats(String inboundStats) {
			this.inboundStats = inboundStats;
		}
		public String getOutboundStats() {
			return outboundStats;
		}
		public void setOutboundStats(String outboundStats) {
			this.outboundStats = outboundStats;
		}
		
		
		
		
	}

}
