package com.example.womenssafetyapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.provider.Settings;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import java.util.Locale;

public class CurrentLocation extends AppCompatActivity {

    protected Location mLastLocation;

    private String mLatitudeLabel;
    private String mLongitudeLabel;
    private TextView mLatitudeText;
    private TextView mLongitudeText;
    private FusedLocationProviderClient mFusedLocationClient;

    private static final String TAG = MainActivity.class.getSimpleName ();

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);

        mLatitudeLabel = "latitude";
        mLongitudeLabel = "longitude";
        mLatitudeText = (TextView) findViewById ((R.id.latitude_text));
        mLongitudeText = (TextView) findViewById ((R.id.longitude_text));

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient (this);

    }

    @Override
    public void onStart() {
        super.onStart ();

        if (!checkPermissions ()) {
            requestPermissions ();
        } else {
            getLastLocation ();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult (requestCode, permissions, grantResults);
        Log.i (TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i (TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
                getLastLocation ();
            } else {
               
                showSnackbar (R.string.textwarn, R.string.settings,
                        new View.OnClickListener () {
                            @Override
                            public void onClick(View view) {
                                // Build intent that displays the App settings screen.
                                Intent intent = new Intent ();
                                intent.setAction (
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts ("package",
                                        BuildConfig.APPLICATION_ID, null);
                                intent.setData (uri);
                                intent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity (intent);
                            }
                        });
            }
        }
    }

    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make (findViewById (android.R.id.content),
                        getString (mainTextStringId),
                        Snackbar.LENGTH_INDEFINITE)
                .setAction (getString (actionStringId), listener).show ();
    }

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission (this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void startLocationPermissionRequest() {
        ActivityCompat.requestPermissions (CurrentLocation.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_PERMISSIONS_REQUEST_CODE);
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale (this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

       
        if (shouldProvideRationale) {
            Log.i (TAG, "Displaying permission rationale to provide additional context.");

            showSnackbar (R.string.textwarn, android.R.string.ok,
                    new View.OnClickListener () {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            startLocationPermissionRequest ();
                        }
                    });

        } else {
            Log.i (TAG, "Requesting permission");
            
            startLocationPermissionRequest ();
        }
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission (this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
           
            return;
        }
        mFusedLocationClient.getLastLocation ()
                .addOnCompleteListener (this, new OnCompleteListener<Location> () {
                    @Override
                    public void onComplete( Task<Location> task) {
                        if (task.isSuccessful () && task.getResult () != null) {
                            mLastLocation = task.getResult ();

                            mLatitudeText.setText (String.format (Locale.ENGLISH, "%s: %f",
                                    mLatitudeLabel,
                                    mLastLocation.getLatitude ()));
                            mLongitudeText.setText (String.format (Locale.ENGLISH, "%s: %f",
                                    mLongitudeLabel,
                                    mLastLocation.getLongitude ()));
                        } else {
                            Log.w (TAG, "getLastLocation:exception", task.getException ());

                        }
                    }
                });
    }

}
