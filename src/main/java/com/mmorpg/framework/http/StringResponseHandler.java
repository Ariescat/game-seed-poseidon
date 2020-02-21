package com.mmorpg.framework.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @author Ariescat
 * @version 2020/2/21 10:37
 */
public class StringResponseHandler implements ResponseHandler<String> {

	private static Charset DEFAULT = Charset.forName("UTF-8");

	@Override
	public String handleResponse(HttpResponse httpResponse) throws ClientProtocolException, IOException {
		StatusLine statusLine = httpResponse.getStatusLine();
		int statusCode = statusLine.getStatusCode();
		if (statusCode >= 200 && statusCode < 300) {
			HttpEntity entity = httpResponse.getEntity();
			return entity != null ? EntityUtils.toString(entity, DEFAULT) : null;
		} else {
			HttpEntity entity = httpResponse.getEntity();
			EntityUtils.consume(entity);
			throw new DefaultHttpResponseException(statusCode, statusLine.getReasonPhrase());
		}
	}

	private class DefaultHttpResponseException extends HttpResponseException {

		DefaultHttpResponseException(int statusCode, String reasonPhrase) {
			super(statusCode, reasonPhrase + " " + statusCode);
		}
	}
}
