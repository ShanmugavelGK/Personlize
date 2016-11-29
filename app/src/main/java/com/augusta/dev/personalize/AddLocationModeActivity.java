package com.augusta.dev.personalize;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.augusta.dev.personalize.utliz.Constants;
import com.augusta.dev.personalize.utliz.Preference;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AddLocationModeActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener, View.OnClickListener {

    private static final int MY_PERMISSION_FINE_LOCATION = 120;
    Button btn_submit;
    Spinner spr_mode;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> m_arr_modes = new ArrayList<>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location_mode);

        init();
        checkPermission();

        String str_json_modes = Preference.getSharedPreferenceString(this, Constants.MODES, "");
        if (str_json_modes.length() != 0) {
            try {
                JSONArray jsonArray = new JSONArray(str_json_modes);

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    String mode = jsonObject.getString(Constants.MODE_TYPE);
                    m_arr_modes.add(mode);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, m_arr_modes);
        spr_mode.setAdapter(arrayAdapter);
    }

    private void init() {
        btn_submit = (Button) findViewById(R.id.btn_submit);
        spr_mode = (Spinner) findViewById(R.id.spr_mode);
        btn_submit.setOnClickListener(this);
    }

    private void checkPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {

            initMap();

        } else {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSION_FINE_LOCATION);

        }
    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (permissions[0].equalsIgnoreCase(Manifest.permission.ACCESS_FINE_LOCATION) &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                requestCode == MY_PERMISSION_FINE_LOCATION) {
            initMap();

        } else {
            Toast.makeText(this, "'ACCESS_FINE_LOCATION' Permission is needed for showing map", Toast.LENGTH_SHORT).show();
            //checkPermission();
            finish();
        }
    }

    GoogleMap googleMap;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED 
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMap.setMyLocationEnabled(true);
        googleMap.setOnMapLongClickListener(this);
        this.googleMap = googleMap;
    }

    @Override
    public void onMapLongClick(LatLng latLng) {

        putMarkerInMap(latLng);
    }

    private void putMarkerInMap(LatLng latLng) {

        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(addresses != null) {
            String address = addresses.get(0).getAddressLine(0);

            googleMap.clear();
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));

            googleMap.addMarker(new MarkerOptions()
                    .title(address)
                    .snippet(latLng.latitude + ", " + latLng.longitude)
                    .position(latLng));
            selected_address = address;
        } else {

            googleMap.clear();
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));

            googleMap.addMarker(new MarkerOptions()
                    .title("Unknown Address")
                    .snippet("")
                    .position(latLng));
            selected_address="";
        }
        
        latitude = latLng.latitude;
        longitude = latLng.longitude;
    }

    String selected_address="";
    Double latitude=0.0;
    Double longitude=0.0;

    @Override
    public void onClick(View view) {
        
        if(view.getId() == R.id.btn_submit) {
            if(latitude == 0.0 && longitude == 0.0) {
                Toast.makeText(this, "Sorry please select a location from map", Toast.LENGTH_SHORT).show();
            } else {
                addData(spr_mode.getSelectedItem().toString());
            }
        }
    }

    private boolean addData(String mode) {
        String strSettings = Preference.getSharedPreferenceString(this, Constants.LOCATION_MODES, "");

        if(strSettings.length() == 0) {
            try {
                JSONArray jsonArray = new JSONArray();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(Constants.LATITUDE, latitude);
                jsonObject.put(Constants.LONGITUDE, longitude);
                jsonObject.put(Constants.ADDRESS, selected_address);
                jsonObject.put(Constants.MODE, mode);
                jsonArray.put(jsonObject);
                strSettings = jsonArray.toString();
                Preference.setSharedPreferenceString(this, Constants.LOCATION_MODES, strSettings);
            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            try {
                JSONArray jsonArray = new JSONArray(strSettings);
                JSONObject jsonObject = new JSONObject();

                jsonObject.put(Constants.LATITUDE, latitude);
                jsonObject.put(Constants.LONGITUDE, longitude);
                jsonObject.put(Constants.ADDRESS, selected_address);
                jsonObject.put(Constants.MODE, mode);
                jsonArray.put(jsonObject);
                strSettings = jsonArray.toString();
                Preference.setSharedPreferenceString(this, Constants.LOCATION_MODES, strSettings);
            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }
        }

        return true;
    }
}
