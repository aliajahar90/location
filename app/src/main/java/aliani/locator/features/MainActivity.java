package aliani.locator.features;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import aliani.locator.R;
import aliani.locator.location.LocationView;
import aliani.locator.location.PermissionImpl;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ajahar.ali on 05-12-2017.
 */

public class MainActivity extends AppCompatActivity implements LocationView {

    @BindView(R.id.textLocation)
    TextView textLocation;
    @BindView(R.id.locateMe)
    Button locateMe;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.locateMe)
    public void onViewClicked() {
        new PermissionImpl(MainActivity.this,MainActivity.this).giveMeCurrentLocation();
    }

    @Override
    public void next(@NonNull Location location) {
        textLocation.setText(location.getLatitude()+"\n"+location.getLongitude());
        Log.v("Here",location.getProvider());
    }

    @Override
    public void error() {
        Log.v("Here","error");
    }
}
