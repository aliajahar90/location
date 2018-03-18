package aliani.locator.location;

import android.content.Context;
import android.location.Location;

import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by ajahar.ali on 03-11-2017.
 */

public interface LocationInteractor {
    interface OnLocationListener {
        void onGetLocation(Location location);
    }

    GoogleApiClient getLocation(Context context, OnLocationListener listener);
}

