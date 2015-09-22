package com.jimmytr.homingpigeon.common.ApiService;

import com.jimmytr.homingpigeon.models.Candidate;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;

import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import io.socket.SocketIOException;

/**
 * Created by FRAMGIA\trieu.duc.phuong on 25/08/2015.
 */
public class SocketConnect {
    public static SocketIO socket;

    public static SocketIO connectToSocketServer(Candidate candidate) {
        {
            try {
                socket = new SocketIO("http://192.168.3.3:4000?uid=" + candidate.getId() + "&channel=" + candidate.getApply_no());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return socket;
    }
}
