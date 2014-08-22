package com.thenewcircle.instructoryamba;



import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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
public class StatusFragment extends Fragment implements TextWatcher, LocationListener {

    private static final String TAG = "newcircle.Yamba." + StatusFragment.class.getSimpleName();
    private TextView textViewNumLeft;
    private Button buttonPostStats;
    private EditText editTextStatusMessage;
    private boolean loggedIn;
    private Location location;

    public StatusFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
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
                if(location != null) {
                    postIntent.putExtra("lat", location.getLatitude());
                    postIntent.putExtra("lon", location.getLongitude());
                }
                getActivity().startService(postIntent);
            }
        });

        return layout;

    }

    @Override
    public void onResume() {
        super.onResume();

        LocationManager locationManager = (LocationManager)getActivity().getSystemService(
                Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_HIGH);

        String provider = locationManager.getBestProvider(criteria, true);
        Log.d(TAG, "provider = " + provider);

        locationManager.requestLocationUpdates(provider, 2000, 2, this);


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        String username = prefs.getString("username", null);
        String password = prefs.getString("password", null);
        loggedIn = username != null && password != null;
        buttonPostStats.setEnabled(loggedIn);
    }

    @Override
    public void onPause() {
        super.onPause();
        LocationManager locationManager = (LocationManager)getActivity().getSystemService(
                Context.LOCATION_SERVICE);
        locationManager.removeUpdates(this);
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

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
        Log.d(TAG, "onLocationChanged: " + location.getLongitude() + " " + location.getLatitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d(TAG, "onStatusChanged " + provider + " " + status);

    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d(TAG, "onProviderEnabled");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d(TAG, "onProviderDisabled");
    }
}
