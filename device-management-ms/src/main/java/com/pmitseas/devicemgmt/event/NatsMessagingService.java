package com.pmitseas.devicemgmt.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.pmitseas.devicemgmt.service.ClientMgmtService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class NatsMessagingService {
	

	 private ClientMgmtService clientMgmtService;

	 
	public void recieveMessage(ModerationAction message) {
		log.info("Received event from Control MS ActiveMQ: {}", message);
        clientMgmtService.sendMessageToClient(message);
		
	}
	
	
	@Autowired
    @Lazy
    public void setDeviceMgmtService(ClientMgmtService deviceMgmtService) {
        //This must be lazily initiated because there is a circular dependency
        //between CDMService <-> AMQMessagingService
        this.clientMgmtService = deviceMgmtService;
    }

}
