package com.jimmytr.homingpigeon.ui;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jimmytr.homingpigeon.R;
import com.jimmytr.homingpigeon.common.CandidateAuthenticate.AuthenticateControl;
import com.jimmytr.homingpigeon.models.Candidate;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends BaseActivity{

    private EditText inputEmailText, inputPassword;
    private Button btnLogin;
    private TextView framgiaTitle;

    private Candidate candidate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy =
                new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_login);
        setupVariables();
        initEvent();
    }

    private void initEvent() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject result = AuthenticateControl.candidateLogin(inputEmailText.getText().toString(), inputPassword.getText().toString());

                if (result != null) {
                    try {
                        JSONObject cand = result.getJSONObject("candidate");
                        candidate = new Candidate(cand.getString("id"), cand.getString("name"), cand.getString("email"),
                                cand.getString("auth_token"), cand.getString("apply_no"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getApplicationContext(), "Login successfully!", Toast.LENGTH_SHORT).show();
                    if (candidate != null) {
                        gotoActivity(LoginActivity.this, CandidateWaitCallActivity.class, candidate);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Login Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void setupVariables() {
        inputEmailText = (EditText) findViewById(R.id.inputEmailText);
        inputPassword = (EditText) findViewById(R.id.inputPassword);
        framgiaTitle = (TextView) findViewById(R.id.framgiaTitle);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        inputEmailText.setHintTextColor(getResources().getColor(R.color.input_text_color));

//        inputEmailText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                btnLogin.setEnabled(!inputEmailText.getText().toString().trim().isEmpty());
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
