package com.mmorpg.framework.net;

import com.mmorpg.framework.net.handler.*;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Ariescat
 * @version 2020/2/27 13:04
 */
@Slf4j
public class NetServer {

	private EventLoopGroup bossGroup = new NioEventLoopGroup(8);
	private EventLoopGroup workerGroup = new NioEventLoopGroup();

	public void startServer(int port) throws Exception {
		try {
			final GameServerIdleHandler idleStateHandler = new GameServerIdleHandler();
			final IPCountHandler ipCountHandler = new IPCountHandler();
			final GameAuthHandler authHandler = new GameAuthHandler();
			final GameServerHandler serverHandler = new GameServerHandler();
			final ResponseEncoder responseEncoder = new ResponseEncoder();

			ServerBootstrap serverBootstrap = new ServerBootstrap();
			serverBootstrap.group(bossGroup, workerGroup)
				.channel(NioServerSocketChannel.class)
				.childOption(ChannelOption.TCP_NODELAY, true)
				.childOption(ChannelOption.SO_KEEPALIVE, true)
				.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
				.option(ChannelOption.SO_REUSEADDR, true)
				.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel socketChannel) throws Exception {
						socketChannel.pipeline()
							.addLast("IdleStateHandler", new IdleStateHandler(0, 0, 180))
							.addLast("WriteTimeoutHandler", new WriteTimeoutHandler(60))
							.addLast("GameServerIdleHandler", idleStateHandler)
							.addLast("BaseFrameDecode", new BaseFrameDecode())
							.addLast("IPCountHandler", ipCountHandler)
							.addLast("GameAuthHandler", authHandler)
							.addLast("GameServerHandler", serverHandler)
							.addLast("ResponseEncoder", responseEncoder);
					}
				})
				.bind(port).sync();

			log.info("NetServer bind port {}", port);
		} catch (Exception e) {
			stop();
			throw e;
		}
	}

	public void stop() {
		bossGroup.shutdownGracefully();
		workerGroup.shutdownGracefully();
	}
}
