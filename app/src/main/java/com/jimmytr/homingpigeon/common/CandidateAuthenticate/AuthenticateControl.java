package com.jimmytr.homingpigeon.common.CandidateAuthenticate;

import com.jimmytr.homingpigeon.common.ApiService.MyConnection;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class AuthenticateControl {
    public static JSONObject candidateLogin(String uEmail, String pwd) {
        String url = "http://192.168.3.3:3000/v1/sessions/create";
        Map<String, String> urlParameters = new HashMap<String, String>();
        urlParameters.put("email", uEmail);
        urlParameters.put("password", pwd);
        urlParameters.put("device_token", "android_device_token");
        JSONObject jsonObject = null;
        try {
            MyConnection.sendPostRequest(url, urlParameters);
            jsonObject = MyConnection.readSingleLineRespone();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MyConnection.httpConn.disconnect();
        return jsonObject;
    }

    public static JSONObject candidateChatting(String auth_token, String id) {
        String url = "http://192.168.3.3:3000/v1/chat_videos/show";
        Map<String, String> urlParameters = new HashMap<String, String>();
        urlParameters.put("auth_token", auth_token);
        urlParameters.put("id", id);
        JSONObject jsonObject = null;
        try {
            MyConnection.sendPostRequest(url, urlParameters);
            jsonObject = MyConnection.readSingleLineRespone();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MyConnection.httpConn.disconnect();
        return jsonObject;
    }
}
