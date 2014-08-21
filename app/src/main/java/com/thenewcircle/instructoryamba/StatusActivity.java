package com.thenewcircle.instructoryamba;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class StatusActivity extends BaseYambaActivity implements TextWatcher {

    private static final String TAG = "newcircle.Yamba." + StatusActivity.class.getSimpleName();
    private TextView textViewNumLeft;
    private Button buttonPostStats;
    private EditText editTextStatusMessage;
    private boolean loggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_status);
        editTextStatusMessage = (EditText) findViewById(R.id.editTextStatusMessage);
        editTextStatusMessage.addTextChangedListener(this);
        textViewNumLeft = (TextView) findViewById(R.id.textViewNumLeft);
        buttonPostStats = (Button) findViewById(R.id.buttonPostStatus);

        buttonPostStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = editTextStatusMessage.getText().toString();
                Log.d(TAG, "okClick " + message);
                Intent postIntent = new Intent(StatusActivity.this, PostMessageService.class);
                postIntent.putExtra("message", message);
                startService(postIntent);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        String username = prefs.getString("username", null);
        String password = prefs.getString("password", null);
        loggedIn = username != null && password != null;
        buttonPostStats.setEnabled(loggedIn);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // Don't care
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // Don't care
    }

    @Override
    public void afterTextChanged(Editable s) {
        int numLeft = 140 - s.length();
        // Log.d(TAG, "afterTextChanged " + numLeft);
        textViewNumLeft.setText(numLeft + "");
        // disable button if message is too large
        buttonPostStats.setEnabled(loggedIn && numLeft >= 0);

        if(numLeft < 10) {
            // change text color
            textViewNumLeft.setTextColor(getResources().getColor(R.color.error));
        }
        else {
            textViewNumLeft.setTextColor(getResources().getColor(R.color.valid));
        }
    }
}
