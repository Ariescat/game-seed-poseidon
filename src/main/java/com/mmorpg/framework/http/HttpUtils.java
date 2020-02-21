package com.mmorpg.framework.http;

import com.google.common.base.Charsets;
import org.apache.commons.lang.StringUtils;
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
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.IOException;
import java.util.*;

/**
 * 同步Http请求
 *
 * @author Ariescat
 * @version 2020/2/21 10:45
 */
public class HttpUtils {

	private static ResponseHandler<String> responseHandler;
	private static SocketConfig DEFAULT_SOCKET_CONFIG;
	private static RequestConfig DEFAULT_REQUEST_CONFIG;

	static {
		responseHandler = new StringResponseHandler();
		DEFAULT_SOCKET_CONFIG = SocketConfig.custom()
			.setTcpNoDelay(true)
			.setSoTimeout(3000)
			.build();
		DEFAULT_REQUEST_CONFIG = RequestConfig.custom()
			.setCookieSpec(CookieSpecs.IGNORE_COOKIES)
			.setExpectContinueEnabled(false)
			.setStaleConnectionCheckEnabled(false)
			.setSocketTimeout(3000)
			.setConnectTimeout(3000)
			.setConnectionRequestTimeout(3000)
			.setMaxRedirects(3)
			.build();
	}

	public static String makeQueryString(Map<String, Object> params) {
		if (params == null || params.isEmpty()) {
			return "";
		}
		Iterator<Map.Entry<String, Object>> it = params.entrySet().iterator();
		StringBuilder sb = new StringBuilder();
		if (it.hasNext()) {
			Map.Entry<String, Object> entry = it.next();
			sb.append(entry.getKey()).append("=").append(entry.getValue());
			while (it.hasNext()) {
				entry = it.next();
				sb.append('&');
				sb.append(entry.getKey()).append("=").append(entry.getValue());
			}
		}
		return sb.toString();
	}

	public static Map<String, String> parseQueryString(String queryString) {
		if (StringUtils.isBlank(queryString)) {
			return Collections.emptyMap();
		}
		String[] pairs = StringUtils.split(queryString, '&');
		Map<String, String> result = new HashMap<>(pairs.length);
		for (String pair : pairs) {
			String[] key2value = StringUtils.split(pair, '=');
			if (key2value.length == 2) {
				result.put(key2value[0], key2value[1]);
			} else {
				result.put(key2value[0], "");
			}
		}
		return result;
	}

	public static CloseableHttpClient createDefault() {
		HttpClientBuilder builder = HttpClients.custom()
			.setDefaultSocketConfig(DEFAULT_SOCKET_CONFIG)
			.setDefaultRequestConfig(DEFAULT_REQUEST_CONFIG)
			.setConnectionManager(new BasicHttpClientConnectionManager());
		return builder.build();
	}

	public static String get(String url) throws HttpException, IOException {
		try (CloseableHttpClient client = createDefault()) {
			HttpGet httpget = new HttpGet(url);
			httpget.addHeader(HTTP.CONN_DIRECTIVE, HTTP.CONN_CLOSE);
			return client.execute(httpget, responseHandler);
		} catch (Exception e) {
			throw new HttpException(url, e);
		}
	}

	public static String get(String url, Map<String, Object> params) throws HttpException, IOException {
		return get(url + "?" + makeQueryString(params));
	}

	public static String post(String url) throws IOException, HttpException {
		return post(url, null);
	}

	public static String post(String url, Map<String, Object> params) throws HttpException, IOException {
		try (CloseableHttpClient client = createDefault()) {
			HttpPost httpPost = new HttpPost(url);
			httpPost.addHeader(HTTP.CONN_DIRECTIVE, HTTP.CONN_CLOSE);
			if (params != null && !params.isEmpty()) {
				List<NameValuePair> nvps = new ArrayList<>();
				for (Map.Entry<String, Object> entry : params.entrySet()) {
					Object value = entry.getValue();
					String valueStr = value == null ? "" : value.toString();
					nvps.add(new BasicNameValuePair(entry.getKey(), valueStr));
				}
				httpPost.setEntity(new UrlEncodedFormEntity(nvps, Charsets.UTF_8));
			}
			return client.execute(httpPost, responseHandler);
		} catch (Exception e) {
			throw new HttpException(url + "?" + makeQueryString(params), e);
		}
	}
}
