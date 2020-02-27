package com.mmorpg.framework.http.asynchttp;

import com.google.common.base.Charsets;
import com.mmorpg.framework.http.HttpUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.ContentDecoder;
import org.apache.http.nio.IOControl;
import org.apache.http.nio.client.methods.HttpAsyncMethods;
import org.apache.http.nio.protocol.AbstractAsyncResponseConsumer;
import org.apache.http.nio.util.HeapByteBufferAllocator;
import org.apache.http.nio.util.SimpleInputBuffer;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * 异步Http请求
 *
 * @author Ariescat
 * @version 2020/2/20 17:04
 */
public class AsyncHttpClientUtils {

	private final static Logger log = LoggerFactory.getLogger(AsyncHttpClientUtils.class);
	private static CloseableHttpAsyncClient HTTP_CLIENT;

	static {
		RequestConfig requestConfig = RequestConfig.custom()
			.setSocketTimeout(3000)
			.setConnectTimeout(3000)
			.setConnectionRequestTimeout(3000)
			.build();
		IOReactorConfig ioConfig = IOReactorConfig.custom()
			.setIoThreadCount(4).build();

		HTTP_CLIENT = HttpAsyncClients.custom()
			.setDefaultRequestConfig(requestConfig)
			.setDefaultIOReactorConfig(ioConfig)
			.setMaxConnPerRoute(50)
			.setMaxConnTotal(100)
			.disableAuthCaching()
			.disableCookieManagement()
			.disableConnectionState().build();
		HTTP_CLIENT.start();
	}

	public static Future<String> execute(String url, Map<String, Object> params,
										 FutureCallback<String> callback) {
		if (MapUtils.isNotEmpty(params)) {
			String param = HttpUtils.makeQueryString(params);
			url += "?" + param;
		}
		final HttpGet httpGet = new HttpGet(url);
		HttpHost target = null;
		final URI requestURI = httpGet.getURI();
		if (requestURI.isAbsolute()) {
			target = URIUtils.extractHost(requestURI);
		}
		try {
			log.info(requestURI.toURL().toString());
		} catch (MalformedURLException e) {
			log.error("AsyncHttpClientUtils:", e);
		}
		return HTTP_CLIENT.execute(
			HttpAsyncMethods.create(target, httpGet), new StringResponseConsumer(),
			callback);
	}

	public static void stop() {
		try {
			if (HTTP_CLIENT.isRunning()) {
				HTTP_CLIENT.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static class StringResponseConsumer extends AbstractAsyncResponseConsumer<String> {

		private volatile SimpleInputBuffer buf = new SimpleInputBuffer(256, new HeapByteBufferAllocator());

		private static Charset DEFAULT = Charsets.UTF_8;

		@Override
		protected void onResponseReceived(HttpResponse httpResponse) throws HttpException, IOException {

		}

		@Override
		protected void onContentReceived(ContentDecoder decoder, IOControl ioControl) throws IOException {
			this.buf.consumeContent(decoder);
		}

		@Override
		protected void onEntityEnclosed(HttpEntity httpEntity, ContentType contentType) throws IOException {

		}

		@Override
		protected String buildResult(HttpContext httpContext) throws Exception {
			byte[] bs = new byte[this.buf.length()];
			this.buf.read(bs);
			return new String(bs, DEFAULT);
		}

		@Override
		protected void releaseResources() {
			buf.reset();
			buf.shutdown();
			buf = null;
		}
	}
}
