package com.jimmytr.homingpigeon.ui;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.jimmytr.homingpigeon.R;
import com.jimmytr.homingpigeon.common.ApiService.SocketConnect;
import com.opentok.android.BaseVideoRenderer;
import com.opentok.android.OpentokError;
import com.opentok.android.Publisher;
import com.opentok.android.PublisherKit;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Subscriber;
import com.opentok.android.SubscriberKit;

public class OpenTokChatActivity extends BaseActivity implements Session.SessionListener, SubscriberKit.SubscriberListener, PublisherKit.PublisherListener {
    public final static String SESSION_ID = "SESSION_ID";
    public final static String API_KEY = "45321362";
    public final static String TOKEN = "TOKEN";
    private static final String LOG_TAG = "OpenTok";

    private Session mSession;
    private String session_id, token;

    private FrameLayout mSubscribeContainer;
    private FrameLayout mPublisherContainer;
    private Subscriber mSubscriber;
    private Publisher mPublisher;

    private ImageView endCall;
    Chronometer stopWatch;
    TextView timer;
    long startTime;
    long countUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opentok_chat);
        setupVariables();
        Intent intent = getIntent();
        session_id = intent.getStringExtra(SESSION_ID);
        token = intent.getStringExtra(TOKEN);

        initializeSession();
        initButtonEvents();
        initCountTimeUp();
    }

    private void initCountTimeUp() {
        startTime = SystemClock.elapsedRealtime();

        stopWatch.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener(){
            @Override
            public void onChronometerTick(Chronometer arg0) {
                countUp = (SystemClock.elapsedRealtime() - arg0.getBase()) / 1000;
                String asText = ((countUp / 60) > 9 ? (countUp / 60) : "0" + (countUp / 60)) + ":" + ((countUp % 60) > 9 ? (countUp % 60) : "0" + (countUp % 60));
                timer.setText(asText);
            }
        });
        stopWatch.start();
    }

    private void initButtonEvents() {
        endCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initializeSession() {
        mSession = new Session(this, API_KEY, session_id);
        mSession.setSessionListener(this);
        mSession.connect(token);
    }

    private void publishStream() {
        mPublisher = new Publisher(OpenTokChatActivity.this, "Jimmy's video");
        mPublisher.setPublisherListener(this);
        mPublisher.getRenderer().setStyle(BaseVideoRenderer.STYLE_VIDEO_SCALE,
                BaseVideoRenderer.STYLE_VIDEO_FILL);
        mPublisherContainer.addView(mPublisher.getView());
        mSession.publish(mPublisher);
    }

    @Override
    public void setupVariables() {
        mSubscribeContainer = (FrameLayout) findViewById(R.id.mSubscribeContainer);
        mPublisherContainer = (FrameLayout) findViewById(R.id.mPublisherContainer);
        endCall = (ImageView) findViewById(R.id.endCall);
        timer = (TextView) findViewById(R.id.timer);
        stopWatch = (Chronometer) findViewById(R.id.chrono);
    }


    @Override
    public void onConnected(Session session) {
        Log.i(LOG_TAG, "Session Connected");
        if (mPublisher == null) {
            publishStream();
        }
    }

    @Override
    public void onDisconnected(Session session) {
        Log.i(LOG_TAG, "Disconnected from the session.");
        if (mPublisher != null) {
            mPublisherContainer.removeView(mPublisher.getView());
        }

        if (mSubscriber != null) {
            mSubscribeContainer.removeView(mSubscriber.getView());
        }

        mPublisher = null;
        mSubscriber = null;
        if(mSession != null)
            mSession.disconnect();
        stopWatch.stop();
    }

    @Override
    public void onStreamReceived(Session session, Stream stream) {
        Log.i(LOG_TAG, "Stream Received");

        if (mSubscriber == null) {
            mSubscriber = new Subscriber(this, stream);
            mSubscriber.setSubscriberListener(OpenTokChatActivity.this);
            mSubscriber.getRenderer().setStyle(BaseVideoRenderer.STYLE_VIDEO_SCALE,
                    BaseVideoRenderer.STYLE_VIDEO_FILL);
            mSession.subscribe(mSubscriber);
        }
    }

    @Override
    public void onStreamDropped(Session session, Stream stream) {
        Log.i(LOG_TAG, "Stream Dropped");

        if (mSubscriber != null) {
            mSubscriber = null;
            mSubscribeContainer.removeAllViews();
        }
        onBackPressed();
    }

    @Override
    public void onError(Session session, OpentokError opentokError) {
        Log.i(LOG_TAG, "Exception: " + opentokError.getMessage());
    }

    @Override
    public void onConnected(SubscriberKit subscriberKit) {
        Log.i(LOG_TAG, "Subscriber Connected");
        mSubscribeContainer.addView(mSubscriber.getView());
    }

    @Override
    public void onDisconnected(SubscriberKit subscriberKit) {
        if (mSubscriber != null) {
            mSubscriber = null;
        }
    }

    @Override
    public void onError(SubscriberKit subscriberKit, OpentokError opentokError) {

    }

    @Override
    public void onStreamCreated(PublisherKit publisherKit, Stream stream) {
        Log.i(LOG_TAG, "Publish stream!");
    }

    @Override
    public void onStreamDestroyed(PublisherKit publisherKit, Stream stream) {
        if (mPublisher != null) {
            mPublisher = null;
        }
    }

    @Override
    public void onError(PublisherKit publisherKit, OpentokError opentokError) {

    }

    @Override
    public void onBackPressed() {
        if (mSession != null)
            mSession.disconnect();
        if (mPublisher != null)
            mPublisher.destroy();
        if (mSubscriber != null)
            mSubscriber.destroy();
        SocketConnect.socket.emit("end_call");
        super.onBackPressed();
    }
}