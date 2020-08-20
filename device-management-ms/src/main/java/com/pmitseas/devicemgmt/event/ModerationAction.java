package com.pmitseas.devicemgmt.event;

import lombok.*;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ModerationAction implements Serializable {
        private UUID id;
	//private String destination;

	    private Map<String, String> args;
	
	    private String version;
	    private String apiVersion = "1.0";
	    private String creationTimestamp;
	    private String queueTimestamp;
	    private String recievedTimeStamp;
	    private String endpointGuid;
	    private String participantId;
	    private String conferenceId;
	    private String roomId;
	    private String command;
	    private String mediaType;
	    private String mediaSource;
	    private String roomDisplayName;

}