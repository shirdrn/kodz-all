package org.shirdrn.kodz.inaction.mina;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TinyServerProtocolHandler extends IoHandlerAdapter {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(TinyServerProtocolHandler.class);

	@Override
	public void sessionCreated(IoSession session) {
		session.getConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		LOGGER.info("CLOSED");
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		LOGGER.info("OPENED");
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) {
		LOGGER.info("*** IDLE #" + session.getIdleCount(IdleStatus.BOTH_IDLE)
				+ " ***");
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) {
		session.close(true);
	}

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		LOGGER.info("Received : " + message);
		if (!session.isConnected()) {
			session.close(true);
		}
	}
}
