package com.pmitseas.devicemgmt.service;

import com.pmitseas.devicemgmt.event.ModerationAction;
import com.pmitseas.devicemgmt.model.ResponseMessage;

public interface ClientMgmtService {

	/**
	 * Handle incoming client message.
	 *
	 * @param message the Response message received from websocket client
	 */
	void handleMessageFromClient(ResponseMessage message);

	/**
	 * Sends a message to a client through a websocket
	 *
	 * @param event the event received from the Control MS through Active MQ
	 */
     void sendMessageToClient(ModerationAction event);
     
  
}
