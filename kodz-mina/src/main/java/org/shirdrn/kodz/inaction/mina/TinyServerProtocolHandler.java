package org.shirdrn.kodz.inaction.mina;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TinyServerProtocolHandler extends IoHandlerAdapter {

	private final static Logger LOGGER = LoggerFactory
			.getLogger(TinyServerProtocolHandler.class);

	@Override
	public void sessionCreated(IoSession session) {
		session.getConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
		LOGGER.info("Session created.");
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		LOGGER.info("Session closed.");
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		LOGGER.info("Session opened.");
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) {
		LOGGER.info("*** IDLE #" + session.getIdleCount(IdleStatus.BOTH_IDLE)
				+ " ***");
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) {
		LOGGER.error("Exeception caught", cause);
		session.close(true);
	}

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		LOGGER.info("Received : " + message);

		String result = "Got it!!!";
		LOGGER.info("Prepare to reply: " + result);
		session.write(result);
		
		String responseMessage = "OK";
		LOGGER.info("Prepare to reply: " + responseMessage);
		session.write(responseMessage);
	}
}
