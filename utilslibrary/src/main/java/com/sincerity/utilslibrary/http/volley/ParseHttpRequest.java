package com.sincerity.utilslibrary.http.volley;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Sincerity on 2019/4/26.
 * 描述：
 */
public class ParseHttpRequest implements IHttpRequest {
    private String url;
    private byte[] prams;
    private IHttpListener listener;
    private HttpURLConnection connection;
    private String RequestMethod;

    public ParseHttpRequest() {
        RequestMethod = "GET";
    }

    public ParseHttpRequest(String method) {
        this.RequestMethod = method;
    }

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public void setParameters(byte[] bytes) {
        this.prams = bytes;
    }

    @Override
    public void execute() {
        URL url;
        OutputStream outputStream = null;
        BufferedOutputStream bufferedOutputStream = null;
        InputStream inputStream = null;
        try {
            url = new URL(this.url);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(6 * 1000);
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setReadTimeout(3 * 1000);
            connection.setRequestMethod(RequestMethod);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.connect();
            outputStream = connection.getOutputStream();
            bufferedOutputStream = new BufferedOutputStream(outputStream);
            if (prams != null) {
                bufferedOutputStream.write(prams);
            }
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = connection.getInputStream();
                listener.onSuccess(inputStream);
            } else {
                listener.onFail(connection.getResponseMessage());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            listener.onFail(e.getMessage());
        } finally {
            try {
                if (bufferedOutputStream != null) {
                    bufferedOutputStream.flush();
                    bufferedOutputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }

                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                listener.onFail(e.getMessage());
            }
        }
    }

    @Override
    public void HttpCall(IHttpListener listener) {
        this.listener = listener;
    }
}
