package com.visualdialer.visualdialer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Utility class for working with HTTP requests.
 *
 * @author Peter Radmanesh (peter.radmanesh@gmail.com).
 */
public class HttpUtils {

	private static final String APPLICATION_JSON = "application/json";
	private static final String CHARSET = "iso-8859-1";
	private static final String CONTENT_TYPE = "Content-type";
	private static final String UTF_8 = "utf-8";

	/** Request methods that are supported. */
	public enum RequestMethod {
		DELETE, GET, POST
	}

	private final HttpClient httpClient;

	public HttpUtils() {
		this(new DefaultHttpClient());
	}

	public HttpUtils(HttpClient httpClient) {
		this.httpClient = httpClient;
	}

	/**
	 * Makes request for given URL and returns its response content.
	 *
	 * @param url URL to request.
	 * @param method Method of request.
	 * @param params Optional list of request parameters.
	 * @return Response content if successful, null otherwise.
	 */
	public String makeHttpRequest(String url, RequestMethod method, List<NameValuePair> params) {
		String content = null;

		final boolean hasParams = params != null && params.size() > 0;		
		try {
			HttpResponse response = null;
			if (method == RequestMethod.DELETE) {
				final String paramString = hasParams ? ("?" + URLEncodedUtils.format(params, UTF_8)) : "";
				final HttpDelete httpDelete = new HttpDelete(url + paramString);
				response = httpClient.execute(httpDelete);
			} else if (method == RequestMethod.GET) {
				final String paramString = hasParams ? ("?" + URLEncodedUtils.format(params, UTF_8)) : "";
				final HttpGet httpGet = new HttpGet(url + paramString);
				response = httpClient.execute(httpGet);
			} else {
				final HttpPost httpPost = new HttpPost(url);
				if (hasParams) {
					httpPost.setEntity(new UrlEncodedFormEntity(params));
				}
				response = httpClient.execute(httpPost);
			}
			content = getResponseContent(response);
		} catch (UnsupportedEncodingException e) {
	        e.printStackTrace();
	    } catch (ClientProtocolException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }

		return content;
	 }

	/**
	 * Makes JSON request for given URL and returns its response content.
	 *
	 * @param url URL to request.
	 * @param jsonObject JSON Object to send.
	 * @return Response content if successful, null otherwise.
	 */
	public String makeJsonRequest(String url, Object jsonObject) {
		String content = null;

		try {
			final HttpPost httpPost = new HttpPost(url);
			httpPost.setHeader(CONTENT_TYPE, APPLICATION_JSON);
			final StringEntity stringEntity = new StringEntity(jsonObject.toString(), UTF_8);
			httpPost.setEntity(stringEntity);

			final HttpResponse response = httpClient.execute(httpPost);
			content = getResponseContent(response);
		} catch (UnsupportedEncodingException e) {
	        e.printStackTrace();
	    } catch (ClientProtocolException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }

		return content;
	 }

	/**
	 * Returns content of response if it was successful.
	 *
	 * @param response Response to use.
	 * @return Content if successful, null otherwise.
	 */
	private static String getResponseContent(HttpResponse response) {
		String content = null;

		if (isHttpResponseSuccessful(response)) {
			final HttpEntity httpEntity = response.getEntity();
			if (httpEntity != null) {
				InputStream is = null;
				try {
					is = httpEntity.getContent();
					final BufferedReader reader = new BufferedReader(new InputStreamReader(is, CHARSET), 8);
					final StringBuilder builder = new StringBuilder();
					String line = null;
					while ((line = reader.readLine()) != null) {
						builder.append(line + "\n");
					}
					content = builder.toString();
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (is != null) {
						try {
							is.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}

		return content;
	}

	/**
	 * Returns whether given HTTP response is successful.
	 *
	 * @param response Response to use.
	 * @return Whether successful.
	 */
	private static boolean isHttpResponseSuccessful(HttpResponse response) {
		boolean successful = false;

		if (response != null) {
			final StatusLine statusLine = response.getStatusLine();
			if (statusLine != null) {
				successful = statusLine.getStatusCode() == HttpStatus.SC_OK ||
						statusLine.getStatusCode() == HttpStatus.SC_CREATED;
			}
		}

		return successful;
	}
}
