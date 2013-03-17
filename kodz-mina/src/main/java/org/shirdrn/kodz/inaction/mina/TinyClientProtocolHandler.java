package org.shirdrn.kodz.inaction.mina;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TinyClientProtocolHandler extends IoHandlerAdapter {
   
     private final static Logger LOGGER = LoggerFactory.getLogger(TinyClientProtocolHandler.class);
   
    @Override
    public void sessionCreated(IoSession session) {
        LOGGER.info("CLIENT::CREATED");
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        LOGGER.info("CLIENT::CLOSED");
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        LOGGER.info("CLIENT::OPENED");
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) {
        LOGGER.info("CLIENT::*** IDLE #" + session.getIdleCount(IdleStatus.BOTH_IDLE) + " ***");
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) {
        LOGGER.info("CLIENT::EXCEPTIONCAUGHT");
        cause.printStackTrace();
    }

    public void messageSent(IoSession session, Object message) throws Exception {
         LOGGER.info("CLIENT::MESSAGESENT: " + message);
    }
}