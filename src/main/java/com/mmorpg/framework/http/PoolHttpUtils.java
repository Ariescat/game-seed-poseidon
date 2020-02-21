package com.mmorpg.framework.http;

import com.google.common.base.Charsets;
import org.apache.http.HttpException;
import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 同步Http请求池
 *
 * @author Ariescat
 * @version 2020/2/21 11:04
 */
public class PoolHttpUtils {

	private static ResponseHandler<String> responseHandler;
	private static CloseableHttpClient HTTP_CLIENT;

	static {
		responseHandler = new StringResponseHandler();

		SocketConfig socketConfig = SocketConfig.custom()
			.setTcpNoDelay(true)
			.setSoTimeout(3000).build();

		RequestConfig defaultRequestConfig = RequestConfig.custom()
			.setCookieSpec(CookieSpecs.IGNORE_COOKIES)
			.setExpectContinueEnabled(false)
			.setStaleConnectionCheckEnabled(true)
			.setSocketTimeout(3000)
			.setConnectTimeout(3000)
			.setConnectionRequestTimeout(3000)
			.setMaxRedirects(3)
			.build();

		PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
		connManager.setDefaultSocketConfig(socketConfig);
		connManager.setDefaultMaxPerRoute(2);
		connManager.setMaxTotal(28);

		HTTP_CLIENT = HttpClients.custom()
			.setConnectionManager(connManager)
			.setDefaultRequestConfig(defaultRequestConfig)
			.build();
	}

	public static String get(String url) throws HttpException, IOException {
		HttpGet httpget = new HttpGet(url);
		try {
			return HTTP_CLIENT.execute(httpget, responseHandler);
		} catch (Exception e) {
			throw new HttpException(url, e);
		}
	}

	public static String get(String url, Map<String, Object> params) throws HttpException, IOException {
		return get(url + "?" + HttpUtils.makeQueryString(params));
	}

	public static String post(String url) throws IOException, HttpException {
		return post(url, null);
	}

	public static String post(String url, Map<String, Object> params) throws HttpException, IOException {
		HttpPost httpPost = new HttpPost(url);
		if (params != null && !params.isEmpty()) {
			List<NameValuePair> nvps = new ArrayList<>();
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				Object value = entry.getValue();
				String valueStr = value == null ? "" : value.toString();
				nvps.add(new BasicNameValuePair(entry.getKey(), valueStr));
			}
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, Charsets.UTF_8));
		}
		try {
			return HTTP_CLIENT.execute(httpPost, responseHandler);
		} catch (Exception e) {
			throw new HttpException(url + "?" + HttpUtils.makeQueryString(params), e);
		}
	}
}
