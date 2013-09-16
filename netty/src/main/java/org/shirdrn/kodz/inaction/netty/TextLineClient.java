package org.shirdrn.kodz.inaction.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.logging.Logger;

public class TextLineClient {

	private static final Logger LOGGER = Logger.getLogger(
			TextLineClient.class.getName());
	private String host = "localhost";
	private int port = 8080;

	static String[] messages = new String[] {
		"Hello, netty!",
		"I'm Jeff Yen Stone.",
		"Nice to meet you."
	};
	
	public TextLineClient() {
		super();
	}
	
	public TextLineClient(String host, int port) {
		super();
		this.host = host;
		this.port = port;
	}
	
	public static String[] getMessages() {
		return messages;
	}


	public void startClient() throws Exception {
        Bootstrap b = new Bootstrap();
        try {
	        b.group(new NioEventLoopGroup())
	         .channel(NioSocketChannel.class)
	         .handler(new ClientTextLineHandler());

		   // Make the connection attempt.
		   ChannelFuture f = b.connect(host, port).sync();
		   LOGGER.info("Connect to " + host + ":" + port);
		
		   // Wait until the connection is closed.
		   f.channel().closeFuture().syncUninterruptibly();
        } finally {
            b.shutdown();
        }
    
	}

	public static void main(String[] args) throws Exception {
		TextLineClient client = new TextLineClient();
		for(int i=0; i<messages.length; i++) {
			client.startClient();
		}
	}

}
