package org.shirdrn.kodz.inaction.mina;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TinyClientProtocolHandler extends IoHandlerAdapter {

	private final static Logger LOGGER = LoggerFactory
			.getLogger(TinyClientProtocolHandler.class);

	@Override
	public void sessionCreated(IoSession session) {
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
		LOGGER.info("*** IDLE #"
				+ session.getIdleCount(IdleStatus.BOTH_IDLE) + " ***");
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) {
		LOGGER.info("Exception caught", cause);
		session.close(true);
	}

	public void messageSent(IoSession session, Object message) throws Exception {
		LOGGER.info("Message sent: " + message);
	}

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		String m = (String) message;
		LOGGER.info("Receive response: " + m);
		if (m.equals("OK")) {
			LOGGER.info("Finished, try to close session...");
			session.close(true);
			LOGGER.info("Done!");
		}
	}
}