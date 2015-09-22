package com.jimmytr.homingpigeon.ui;

import com.jimmytr.homingpigeon.R;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends BaseActivity {

    private Handler mHandler;

    public static final int SPLASH_LOADING = 2000;
    private boolean isBack = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mHandler = new Handler();
        mHandler.postDelayed(runnable, SPLASH_LOADING);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (!isBack) {
                gotoActivity(SplashActivity.this, LoginActivity.class,
                        Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
            }

        }
    };

    private void removeCallback() {
        if (mHandler != null) {
            mHandler.removeCallbacks(runnable);
            mHandler = null;
        }
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isBack = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isBack = true;
        removeCallback();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isBack = true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        isBack = true;
        removeCallback();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}
