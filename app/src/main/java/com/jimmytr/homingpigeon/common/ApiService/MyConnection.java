package com.jimmytr.homingpigeon.common.ApiService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;


public class MyConnection {
    private static final int BUFFER_SIZE = 4096;
    public static HttpURLConnection httpConn;

    public static HttpURLConnection sendPostRequest(String requestURL,
                                                    Map<String, String> params) throws IOException {
        URL url = new URL(requestURL);
        httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setUseCaches(false);
        httpConn.setReadTimeout(15000);
        httpConn.setConnectTimeout(15000);
        httpConn.setRequestMethod("POST");
        httpConn.setDoInput(true); // true indicates the server returns response
        StringBuffer requestParams = new StringBuffer();

        if (params != null && params.size() > 0) {

            httpConn.setDoOutput(true); // true indicates POST request

            // creates the params string, encode them using URLEncoder
            Iterator<String> paramIterator = params.keySet().iterator();
            while (paramIterator.hasNext()) {
                String key = paramIterator.next();
                String value = params.get(key);
                requestParams.append(URLEncoder.encode(key, "UTF-8"));
                requestParams.append("=").append(
                        URLEncoder.encode(value, "UTF-8"));
                requestParams.append("&");
            }

            // sends POST data
            OutputStreamWriter writer = new OutputStreamWriter(
                    httpConn.getOutputStream());
            writer.write(requestParams.toString());
            writer.flush();
        }

        return httpConn;
    }

    public static JSONObject readSingleLineRespone() throws IOException,
            JSONException {

        InputStream inputStream = null;
        if (httpConn != null) {
            inputStream = httpConn.getInputStream();
        } else {
            throw new IOException("Connection is not established.");
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                inputStream));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response = response.append(line);
        }
        reader.close();
        if (response != null && !response.equals("")) {
            return new JSONObject(response.toString());
        } else {
            return null;
        }
    }

}
