package org.shirdrn.kodz.inaction.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundByteHandlerAdapter;

import java.util.logging.Logger;

public class ClientTextLineHandler extends ChannelInboundByteHandlerAdapter {

	private static final Logger LOGGER = Logger.getLogger(
			ClientTextLineHandler.class.getName());
	
	@Override
	protected void inboundBufferUpdated(ChannelHandlerContext ctx, ByteBuf in)
			throws Exception {
		int readableBytes = in.readableBytes();
		byte[] b = new byte[readableBytes];
		in.readBytes(b);
		String data = new String(b);
		LOGGER.info("Receive message: length=" + readableBytes + ", data=" + data);
		
		if(data.equals("OK")) {
			ctx.close();
		}
	}
	
	static int index = 0;

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ByteBuf in = ctx.alloc().buffer(100);
		in.writeBoolean(false);
		byte[] bytes = TextLineClient.getMessages()[index].getBytes();
		in.writeInt(bytes.length);
		in.writeBytes(bytes);
		LOGGER.info("Send message: " + TextLineClient.getMessages()[index]);
		index++;
		ctx.write(in);
	}

}
