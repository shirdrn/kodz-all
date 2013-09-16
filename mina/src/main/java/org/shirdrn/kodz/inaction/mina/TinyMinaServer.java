package org.shirdrn.kodz.inaction.mina;

import java.net.InetSocketAddress;

import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TinyMinaServer {
    
     private final static Logger LOG = LoggerFactory.getLogger(TinyMinaServer.class);
    private static final int PORT = 8080;

    public static void main(String[] args) throws Exception {
        SocketAcceptor acceptor = new NioSocketAcceptor();
        acceptor.setReuseAddress(true);
        acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory()));
       
        // Bind
        acceptor.setHandler(new TinyServerProtocolHandler());
        acceptor.bind(new InetSocketAddress(PORT));
        LOG.info("Listening on port " + PORT);
       
        LOG.info("Server started!");
       
        for (;;) {
            LOG.info("R: " + acceptor.getStatistics().getReadBytesThroughput() +
                      ", W: " + acceptor.getStatistics().getWrittenBytesThroughput());
            Thread.sleep(5000);
        }
       
    }

}