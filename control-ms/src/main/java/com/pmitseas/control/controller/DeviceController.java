package com.pmitseas.control.controller;

import com.pmitseas.control.dto.ActionDTO;
import com.pmitseas.control.event.ModerationAction;
import com.pmitseas.control.event.NatsMessagingService;
import com.pmitseas.control.service.SseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/device")
public class DeviceController {

	private final SseService sseService;
	private final NatsMessagingService natsService;

	@PostMapping
	public SseEmitter sendCommandToDevice(@RequestBody ActionDTO actionDTO) {
		log.info("Received POST request with body: {}", actionDTO);

		/* Create Action event*/
		ModerationAction event = ModerationAction.builder()
				.endpointGuid(actionDTO.getEndpointGuid())
				.conferenceId(actionDTO.getConferenceId())
				.roomId(actionDTO.getRoomId())
				.command(actionDTO.getCommand())
				.mediaType(actionDTO.getMediaType())
				.mediaSource(actionDTO.getMediaSource())
				.id(UUID.randomUUID())
				.build();

		//messagingService.send(event);
		natsService.send(event);
		return sseService.createPaymentEmitter(event.getId());
	}
}
