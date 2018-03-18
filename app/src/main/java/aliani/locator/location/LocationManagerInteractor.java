package aliani.locator.location;

import android.content.Context;
import android.location.Location;


/**
 * Created by ajahar.ali on 05-12-2017.
 */

public interface LocationManagerInteractor {
    interface OnLocationManagerListener {
        void onGetManagerLocation(Location location);
    }
    void getLocation(Context context, OnLocationManagerListener onLocationManagerListener);
}
