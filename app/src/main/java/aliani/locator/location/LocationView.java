package aliani.locator.location;

import android.location.Location;

/**
 * Created by ajahar.ali on 03-11-2017.
 */

public interface LocationView {
    void next(Location location);
    void error();
}
