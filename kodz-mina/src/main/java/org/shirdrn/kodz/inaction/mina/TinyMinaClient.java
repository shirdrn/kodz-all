package org.shirdrn.kodz.inaction.mina;

import java.net.InetSocketAddress;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.SocketConnector;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TinyMinaClient {

     private final static Logger LOG = LoggerFactory.getLogger(TinyMinaClient.class);
     /** Choose your favorite port number. */
     private static final int PORT = 8080;

     public static void main(String[] args) throws Exception {
          SocketConnector connector = new NioSocketConnector();

          // Connect
          connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory()));
          connector.setHandler(new TinyClientProtocolHandler());
         
          for(int i=0; i<10; i++) {
               ConnectFuture future = connector.connect(new InetSocketAddress(PORT));
               LOG.info("Connect to port " + PORT);
               future.awaitUninterruptibly();
               future.getSession().write(String.valueOf(i));
               Thread.sleep(1500);
          }
         
     }

}