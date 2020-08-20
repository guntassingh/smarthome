package com.pmitseas.devicemgmt.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pmitseas.devicemgmt.event.ModerationAction;
import com.pmitseas.devicemgmt.event.ActionResultEvent;
import com.pmitseas.devicemgmt.model.ModerationCommand;
import com.pmitseas.devicemgmt.model.ResponseMessage;

import io.nats.client.Connection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientMgmtServiceImpl implements ClientMgmtService {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final  Connection natsConnection;


    @Override
    public void handleMessageFromClient(ResponseMessage message){
        // Custom logic here
        log.info("Message Contents: {}", message);

        ActionResultEvent resultEvent = ActionResultEvent.builder()
                .transactionId(message.getId())
                .data(message.getData())
                .status(message.getStatus())
                .time(message.getTime())
                .build();
        
        
        try {
			
			ObjectMapper mapper = new ObjectMapper();
	
		    String jsonString = mapper.writeValueAsString(resultEvent);
		    System.out.println("the converted JSON string is "+jsonString);
			
			natsConnection.publish("subject",jsonString.getBytes(StandardCharsets.UTF_8));
		}catch (Exception e) {
			e.printStackTrace();
		}

    }
    
    

    @Override
    public void sendMessageToClient(ModerationAction action) {
    	System.out.println("Sending message from Nats ");
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setLeaveMutable(true);

        String client = action.getEndpointGuid();
        System.out.println("Client on NATS is "+client);
        /*
         * Create a dummy message to send to client
         */
        ModerationCommand command = ModerationCommand.builder()
                .creationTimestamp(LocalDateTime.now().toString())
                .endpointGuid(action.getEndpointGuid())
                .participantId(action.getParticipantId())
                .conferenceId(action.getConferenceId())
                .roomId(action.getRoomId())
                .mediaType(action.getMediaType())
                .mediaSource(action.getMediaSource())
                .command(action.getCommand())
                .build();

        log.info("Sending message from Nats to device : {}", client);
        simpMessagingTemplate.convertAndSendToUser(client, "/queue/device", command, headerAccessor.getMessageHeaders());
    }
    
   


}
