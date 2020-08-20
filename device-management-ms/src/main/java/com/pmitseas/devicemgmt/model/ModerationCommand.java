package com.pmitseas.devicemgmt.model;

import lombok.*;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ModerationCommand implements Serializable {
	
	private String apiVersion = "1.0";
    private String creationTimestamp;
    private String endpointGuid;
    private String participantId;
    private String conferenceId;
    private String roomId;
    private String command;
    private String mediaType;
    private String mediaSource;
 
}
