package com.thenewcircle.instructoryamba;



import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class StatusFragment extends Fragment implements TextWatcher {

    private static final String TAG = "newcircle.Yamba." + StatusFragment.class.getSimpleName();
    private TextView textViewNumLeft;
    private Button buttonPostStats;
    private EditText editTextStatusMessage;
    private boolean loggedIn;

    public StatusFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout =  inflater.inflate(R.layout.fragment_status, container, false);
        editTextStatusMessage = (EditText) layout.findViewById(R.id.editTextStatusMessage);
        editTextStatusMessage.addTextChangedListener(this);
        textViewNumLeft = (TextView) layout.findViewById(R.id.textViewNumLeft);
        buttonPostStats = (Button) layout.findViewById(R.id.buttonPostStatus);

        buttonPostStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = editTextStatusMessage.getText().toString();
                Log.d(TAG, "okClick " + message);
                Intent postIntent = new Intent(getActivity(), PostMessageService.class);
                postIntent.putExtra("message", message);
                getActivity().startService(postIntent);
            }
        });

        return layout;

    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        String username = prefs.getString("username", null);
        String password = prefs.getString("password", null);
        loggedIn = username != null && password != null;
        buttonPostStats.setEnabled(loggedIn);

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

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
