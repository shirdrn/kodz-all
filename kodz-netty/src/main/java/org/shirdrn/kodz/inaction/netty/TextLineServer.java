package org.shirdrn.kodz.inaction.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.util.logging.Logger;

public class TextLineServer {

	private static final Logger LOGGER = Logger.getLogger(
			TextLineServer.class.getName());
	private int port = 8080;

	public TextLineServer() {
		super();
	}
	
	public TextLineServer(int port) {
		super();
		this.port = port;
	}

	public void startServer() throws Exception {
		// Configure the server.
		ServerBootstrap b = new ServerBootstrap();
		try {
			b.group(new NioEventLoopGroup(), new NioEventLoopGroup())
					.channel(NioServerSocketChannel.class)
					.option(ChannelOption.SO_BACKLOG, 100)
					.handler(new LoggingHandler(LogLevel.INFO))
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						public void initChannel(SocketChannel ch)
								throws Exception {
							ch.pipeline().addLast(
									new LoggingHandler(LogLevel.INFO),
									new ServerTextLineHandler());
						}
					});

			// Start the server.
			ChannelFuture f = b.bind(port).sync();
			LOGGER.info("Bind to " + port);

			// Wait until the server socket is closed.
			f.channel().closeFuture().sync();
			
			LOGGER.info("Server is started!");
		} finally {
			// Shut down all event loops to terminate all threads.
			b.shutdown();
		}
	}

	public static void main(String[] args) throws Exception {
		TextLineServer server = new TextLineServer();
		server.startServer();
	}

}
