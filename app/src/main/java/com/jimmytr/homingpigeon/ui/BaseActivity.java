package com.jimmytr.homingpigeon.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.jimmytr.homingpigeon.models.Candidate;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class BaseActivity extends Activity{
    public final static String EXTRA_MESSAGE = "com.mycompany.homingpigon.CANDIDATE_INFO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void gotoActivity(Context context, Class<?> cls, JSONObject jsonObject) throws JSONException {

    }

    public void gotoActivity(Context context, Class<?> cls, int flag) {
        Intent intent = new Intent(context, cls);
        intent.addFlags(flag);
        startActivity(intent);
    }

    public void gotoActivity(Context context, Class<?> cls, Candidate candidate) {
        Intent intent = new Intent(context, cls);
        intent.putExtra(EXTRA_MESSAGE, candidate);
        startActivity(intent);
    }

    public void setupVariables() {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
