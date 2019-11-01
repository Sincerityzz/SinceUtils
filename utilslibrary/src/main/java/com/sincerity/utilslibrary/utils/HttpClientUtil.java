package com.sincerity.utilslibrary.utils;

import android.content.Context;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class HttpClientUtil {

	/** 连接超时 */
	private final static int timeoutConnection = 50000;//默认15000
	/** 读取数据超时 */
	private final static int timeoutSocket = 1800000;//默认15000
	/** 注册连接超时 */
	private final static int timeoutConnectionRuntime = 1500;
	/** 注册连接超时 */
	private final static int timeoutSocketRuntime = 1500;

	/**
	 * 处理Get请求
	 * 
	 * @param uri
	 * @return
	 */
	public static String doGet(Context context, String uri) {
		HttpGet request = null;
		String result = null;
		try {
			request = new HttpGet(uri);
			request.setHeader("Accept", "application/json");
			request.setHeader("Content-type", "application/json");
			HttpClient httpClient = getHttpClient(context);
			HttpResponse response = httpClient.execute(request);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				result = EntityUtils.toString(response.getEntity(), "UTF-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (request != null) {
				request.abort();
			}
		}
		return result;
	}

	/**
	 * 处理Post请求
	 * 
	 * @param uri
	 * @param josn
	 * @return
	 */
	public static String doPost(Context context, String uri, String josn) {
		HttpPost request = null;
		String result = null;
		try {
			request = new HttpPost(uri);
			request.setHeader("Accept", "application/json");
			request.setHeader("Content-type", "application/json");
			StringEntity entity = new StringEntity(josn, "UTF-8");
			request.setEntity(entity);
			HttpClient httpClient = getHttpClient(context);
			ClientConnectionManager ccManager = httpClient.getConnectionManager();
			HttpResponse response = httpClient.execute(request);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				result = EntityUtils.toString(response.getEntity(), "UTF-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (request != null) {
				request.abort();
			}
		}
		return result;
	}

	/**
	 * 处理Post请求
	 * 
	 * @param uri
	 * @param josn
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public static String doPostRuntime(Context context, String uri, String josn) throws Exception {
		HttpPost request = null;
		String result = null;
		request = new HttpPost(uri);
		request.setHeader("Accept", "application/json");
		request.setHeader("Content-type", "application/json");
		StringEntity entity = new StringEntity(josn, "UTF-8");
		request.setEntity(entity);
		HttpClient httpClient = getHttpClientRuntime(context);
		HttpResponse response = httpClient.execute(request);
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			result = EntityUtils.toString(response.getEntity(), "UTF-8");
		}
		if (request != null) {
			request.abort();
		}
		return result;
	}

	/**
	 * 上传数据
	 * 
	 * @param context
	 * @param uri
	 * @param data
	 * @return
	 */
	public static String doPost(Context context, String uri, byte[] data) {
		HttpPost request = null;
		String result = null;
		try {
			request = new HttpPost(uri);
			InputStream inputStream = new ByteArrayInputStream(data);
			InputStreamEntity entity = new InputStreamEntity(inputStream, -1);
			entity.setContentType("binary/octet-stream");
			entity.setChunked(true);
			request.setEntity(entity);
			HttpClient httpClient = getHttpClient(context);
			HttpResponse response = httpClient.execute(request);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				result = EntityUtils.toString(response.getEntity(), "UTF-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (request != null) {
				request.abort();
			}
		}
		return result;
	}

	/**
	 * 获取HttpClient
	 * 
	 * @param context
	 * @return
	 */
	public static HttpClient getHttpClient(Context context) {
		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
		HttpConnectionParams.setTcpNoDelay(httpParameters, true);
		HttpClient httpClient = new DefaultHttpClient(httpParameters);
		return httpClient;
	}

	/**
	 * 获取HttpClient
	 * 
	 * @param context
	 * @return
	 */
	public static HttpClient getHttpClientRuntime(Context context) {
		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnectionRuntime);
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocketRuntime);
		HttpConnectionParams.setTcpNoDelay(httpParameters, true);
		HttpClient httpClient = new DefaultHttpClient(httpParameters);
		return httpClient;
	}
}