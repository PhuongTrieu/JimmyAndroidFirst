package com.jimmytr.homingpigeon.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jimmytr.homingpigeon.R;
import com.jimmytr.homingpigeon.common.ApiService.SocketConnect;
import com.jimmytr.homingpigeon.common.CandidateAuthenticate.AuthenticateControl;
import com.jimmytr.homingpigeon.models.Candidate;

import org.json.JSONException;
import org.json.JSONObject;


import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import io.socket.SocketIOException;


public class CandidateWaitCallActivity extends BaseActivity {
    public final static String BEING_CALLED = "being_called";
    public final static String END_CALLED = "user_end_call";
    public final static String SESSION_ID = "SESSION_ID";
    public final static String TOKEN = "TOKEN";

    private ImageView acceptCall;
    private ImageView birdCall;
    private ImageView signalCall;
    private LinearLayout linearLayout;

    private SocketIO socket;
    private Candidate candidate;
    private String cid;
    private String aid;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidate_waiting_call);
        setupVariables();
        ChatFunction();
        clickBtnEvents();
    }

    private void ChatFunction() {
        Intent intent = getIntent();
        if (intent != null) {
            candidate = (Candidate) intent.getSerializableExtra(EXTRA_MESSAGE);
        }
        socket = SocketConnect.connectToSocketServer(candidate);
        socket.connect(new IOCallback() {
            @Override
            public void onMessage(JSONObject json, IOAcknowledge ack) {
                try {
                    System.out.println("Server said:" + json.toString(2));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onMessage(String data, IOAcknowledge ack) {
                String a = data;
                System.out.println("Server said: " + data);
            }

            @Override
            public void onError(SocketIOException socketIOException) {
                System.out.println("an Error occured");
                socketIOException.printStackTrace();
            }

            @Override
            public void onDisconnect() {
                System.out.println("Connection terminated.");
            }

            @Override
            public void onConnect() {
                socket.emit("user_connect");
                System.out.println("Connection established");
            }

            @Override
            public void on(final String event, IOAcknowledge ack, Object... args) {
                System.out.println("Server triggered event '" + event + " " + args[0]);
                if (END_CALLED.equals(event)) {
                    new Thread() {
                        public void run() {
                            Message message = new Message();
                            message.what = Integer.parseInt(cid);
                            message.obj = event;
                            handler.sendMessage(message);
                            Thread.currentThread().interrupt();
                        }
                    }.start();
                }
                if (args.length > 0) try {
                    aid = ((JSONObject) args[0]).getString("aid");
                    cid = ((JSONObject) args[0]).getString("cid");
                    if (BEING_CALLED.equals(event) && candidate.getId().equals(cid)) {
                        new Thread() {
                            public void run() {
                                Message message = new Message();
                                message.what = Integer.parseInt(cid);
                                message.obj = event;
                                handler.sendMessage(message);
                                Thread.currentThread().interrupt();
                            }
                        }.start();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void changeElementsBackground() {
        acceptCall.setEnabled(true);
        acceptCall.setImageResource(R.drawable.telorange);
        birdCall.setImageResource(R.drawable.birdorange);
        linearLayout.setBackgroundResource(R.drawable.ic_background);
        signalCall.setImageResource(R.drawable.menu);
        signalCall.setVisibility(View.VISIBLE);
    }

    private void changeElementsBackgroundWhenEndCall() {
        acceptCall.setEnabled(false);
        acceptCall.setImageResource(R.drawable.telblue);
        birdCall.setImageResource(R.drawable.birdblue);
        linearLayout.setBackgroundResource(R.drawable.background2);
        signalCall.setVisibility(View.INVISIBLE);
        ChatFunction();
    }

    private void clickBtnEvents() {
        acceptCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject result = AuthenticateControl.candidateChatting(candidate.getAuth_token(), aid);
                if (result != null) try {
                    socket.emit("user_connect");
                    socket.emit("user_chatting");
                    gotoActivity(CandidateWaitCallActivity.this, OpenTokChatActivity.class, result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void setupVariables() {
        acceptCall = (ImageView) findViewById(R.id.btnCall);
        acceptCall.setEnabled(false);
        birdCall = (ImageView) findViewById(R.id.birdCall);
        linearLayout = (LinearLayout) findViewById(R.id.waitingLayout);
        signalCall = (ImageView) findViewById(R.id.signalCall);
    }

    @Override
    protected void onDestroy() {
        socket.disconnect();
        super.onDestroy();
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == Integer.parseInt(candidate.getId()) && msg.obj.toString().equals(END_CALLED)){
                changeElementsBackgroundWhenEndCall();
            } else {
                changeElementsBackground();
            }
        }

    };

    @Override
    public void gotoActivity(Context context, Class<?> cls, JSONObject jsonObject) throws JSONException {
        Intent intent = new Intent(context, cls);
        String session_id, token;

        session_id = jsonObject.getString("session_id");
        token = jsonObject.getString("token");

        intent.putExtra(SESSION_ID, session_id);
        intent.putExtra(TOKEN, token);

        startActivity(intent);
    }
}

