package aliani.locator.location;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

/**
 * Created by ajahar.ali on 03-11-2017.
 */

public class LocationInteractorImpl implements LocationInteractor, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private GoogleApiClient mGoogleApiClient;
    private OnLocationListener onLocationListener;
    private Context mContext;
    private SettingsClient mSettingsClient;
    private LocationSettingsRequest mLocationSettingsRequest;
    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    @Override
    public GoogleApiClient getLocation(Context context, OnLocationListener listener) {
        this.mContext=context;
        this.onLocationListener = listener;
            buildGoogleApiClient(context);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (checkPlayServices()) {
                    mSettingsClient = LocationServices.getSettingsClient(mContext);
                    LocationRequest locationRequest = LocationRequest.create();
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    locationRequest.setInterval(5 * 1000);
                    locationRequest.setFastestInterval(5 * 1000);
                    LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
                    builder.addLocationRequest(locationRequest);
                    mLocationSettingsRequest = builder.build();
                    startLocationUpdates();
                }
            }
        }, 200);
        return mGoogleApiClient;

    }

    protected synchronized void buildGoogleApiClient(Context context) {
        Log.i("TAG", "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i("location", "onConnected");
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
           Location mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            onLocationListener.onGetLocation(mCurrentLocation);
        }


    @Override
    public void onConnectionSuspended(int i) {
        Log.i("location", "Connection suspended");
        if(mGoogleApiClient!=null && !mGoogleApiClient.isConnecting())
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        mGoogleApiClient.disconnect();
        Log.i("location", "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }
    private void startLocationUpdates() {
        // Begin by checking if the device has the necessary location settings.
        mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener((Activity) mContext, new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mGoogleApiClient.connect();
                            }
                        },1000);

                    }
                }).addOnFailureListener((Activity) mContext, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                int statusCode = ((ApiException) e).getStatusCode();
                switch (statusCode) {
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i("MAIn", "Location settings are not satisfied. Attempting to upgrade " +
                                "location settings ");
                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the
                            // result in onActivityResult().
                            ResolvableApiException rae = (ResolvableApiException) e;
                            rae.startResolutionForResult((Activity) mContext, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException sie) {
                            Log.i("MAIN", "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        String errorMessage = "Location settings are inadequate, and cannot be " +
                                "fixed here. Fix in Settings.";
                        Log.i("MAIN", "PendingIntent unable to execute request.");
                }
            }
        });
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(mContext);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog((Activity) mContext, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            }
            return false;
        }
        return true;
    }

}
