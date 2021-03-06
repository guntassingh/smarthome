package com.pmitseas.deviceclient.model;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
public class ModerationCommand implements Serializable {
    private UUID id;
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
