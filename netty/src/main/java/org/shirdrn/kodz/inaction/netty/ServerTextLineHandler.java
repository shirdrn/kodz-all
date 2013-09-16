package org.shirdrn.kodz.inaction.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundByteHandlerAdapter;

import java.util.logging.Logger;

public class ServerTextLineHandler extends ChannelInboundByteHandlerAdapter {

	private static final Logger LOGGER = Logger.getLogger(
			ServerTextLineHandler.class.getName());
	
	@Override
	protected void inboundBufferUpdated(ChannelHandlerContext context, ByteBuf in)
			throws Exception {
		
		boolean discard = in.readBoolean();
		if(!discard) {
			int length = in.readInt();
			byte[] recv = new byte[in.readableBytes()];
			in.readBytes(recv);
			String data = new String(recv);
			LOGGER.info("Receive message:discard=" + discard + ", length=" + length + ", data=" + data);
			ByteBuf out = context.nextOutboundByteBuffer();
			
			String reply = "OK";
			out.writeBytes(reply.getBytes());
			LOGGER.info("Replied: " + reply);
			context.flush();
		}
	}

}
