package aliani.locator.location;

import android.content.Context;
import android.location.Location;

import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by ajahar.ali on 03-11-2017.
 */

public class LocationPresenterImpl implements LocationPresenter,LocationInteractor.OnLocationListener, LocationManagerInteractor.OnLocationManagerListener {
    private Context mContext;
    private  LocationView locationView;
    private  LocationInteractor locationInteractor;
    private GoogleApiClient googleApiClient;
    private LocationManagerInteractor locationManagerInteractor;

    public LocationPresenterImpl(Context mContext,LocationView locationView) {
        this.mContext=mContext;
        this.locationView=locationView;
        this.locationInteractor=new LocationInteractorImpl();
        this.locationManagerInteractor=new LocationManagerInteractorImpl();
    }

    @Override
    public void checkLocationCriteria() {
          googleApiClient= locationInteractor.getLocation(mContext,this);
    }

    @Override
    public void onDestroy() {
        if (googleApiClient!=null && googleApiClient.isConnected())
        googleApiClient.disconnect();
    }

    @Override
    public void onGetLocation(Location location) {
        if(location!=null) {
            locationView.next(location);
        }
        else {
            locationManagerInteractor.getLocation(mContext,this);
        }
    }

    @Override
    public void onGetManagerLocation(Location location) {
        if(location!=null) {
            locationView.next(location);
        }
        else {
            locationView.error();
        }
    }
}
