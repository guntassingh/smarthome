package com.pmitseas.control.dto;

import lombok.*;

import java.io.Serializable;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ActionDTO implements Serializable {
	private String endpointGuid;
	private String participantId;
	private String conferenceId;
	private String roomId;
	private String command;
    private String mediaType;
    private String mediaSource;
}
