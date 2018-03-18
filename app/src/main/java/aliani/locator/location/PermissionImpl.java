package aliani.locator.location;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import java.util.List;

import aliani.locator.R;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by ajahar.ali on 05-12-2017.
 */

public class PermissionImpl implements EasyPermissions.PermissionCallbacks {


    private static final int RC_LOCATION_APP_PERM =1 ;
    private Activity activity;
    LocationPresenter locationPresenter;
    public PermissionImpl(Activity activity,LocationView locationView) {
        this.activity = activity;
        locationPresenter=new LocationPresenterImpl(activity,locationView);
    }
   public void giveMeCurrentLocation()
    {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};
        if (EasyPermissions.hasPermissions(activity, perms)) {
            canGetLocation();

        } else {
            EasyPermissions.requestPermissions(activity, activity.getString(R.string.rationale_location), RC_LOCATION_APP_PERM, perms);
        }
    }
    @AfterPermissionGranted(RC_LOCATION_APP_PERM)
    private void canGetLocation() {
        locationPresenter.checkLocationCriteria();
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(activity, perms)) {
            new AppSettingsDialog.Builder(activity)
                    .setTitle(activity.getString(R.string.title_settings_dialog))
                    .setRationale(activity.getString(R.string.rationale_ask_again))
                    .setPositiveButton(activity.getString(R.string.setting))
                    .setNegativeButton(activity.getString(R.string.cancel))
                    .setRequestCode(RC_LOCATION_APP_PERM)
                    .build()
                    .show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}
