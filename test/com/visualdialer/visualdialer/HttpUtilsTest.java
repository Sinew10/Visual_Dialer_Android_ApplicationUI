package com.visualdialer.visualdialer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Test;

import com.visualdialer.visualdialer.HttpUtils.RequestMethod;

public class HttpUtilsTest {

	private void verifyHttpUriRequest(HttpUriRequest request, String method, String url) {
		assertEquals(method, request.getMethod());
		assertEquals(url, request.getURI().toString());
	}

	private void verifyHttpPost(HttpUriRequest request, String url, String paramsString) throws IOException {
		verifyHttpUriRequest(request, "POST", url);

		final HttpPost httpPost = (HttpPost) request;
		verifyHttpUriRequest(httpPost, "POST", url);
		final BufferedReader bufferedReader = new BufferedReader(
        		new InputStreamReader(httpPost.getEntity().getContent(), "UTF-8"));
		assertEquals(paramsString, bufferedReader.readLine());
		bufferedReader.close();
	}

	@Test
	public void testMakeHttpRequest_nullResponse() throws IllegalStateException, IOException {
		final List<NameValuePair> params = new ArrayList<NameValuePair>();
		final String urlParamKey = "key";
		final String urlParamValue = "value";
		params.add(new BasicNameValuePair(urlParamKey, urlParamValue));

		final List<HttpUriRequest> requests = new ArrayList<HttpUriRequest>();
		final HttpUtils httpUtils = new HttpUtils(new MockHttpClient() {
			@Override
			public HttpResponse execute(HttpUriRequest request) throws IOException,
					ClientProtocolException {
				requests.add(request);
				return null;
			}
		});

		final String url = "http://www.yyy.zzz";
		assertNull(httpUtils.makeHttpRequest(url, RequestMethod.DELETE, params));
		assertEquals(1, requests.size());
		final String paramsString = urlParamKey + "=" + urlParamValue;
		final String finalUrl = url + "?" + paramsString;
		verifyHttpUriRequest(requests.get(0), "DELETE", finalUrl);

		requests.clear();
		assertNull(httpUtils.makeHttpRequest(url, RequestMethod.GET, params));
		assertEquals(1, requests.size());
		verifyHttpUriRequest(requests.get(0), "GET", finalUrl);

		requests.clear();
		assertNull(httpUtils.makeHttpRequest(url, RequestMethod.POST, params));
		assertEquals(1, requests.size());
		verifyHttpPost(requests.get(0), url, paramsString);
	}
}
