package ir.rayas.app.citywareclient.View.Share;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;

import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityResult;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityResultPassing;
import ir.rayas.app.citywareclient.Share.Helper.Gps;
import ir.rayas.app.citywareclient.Share.Helper.IResponseTurnOnGpsDialog;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityIdList;
import ir.rayas.app.citywareclient.View.Base.BaseActivity;

public class MapActivity extends BaseActivity implements IResponseTurnOnGpsDialog, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private double Latitude = 0.0;
    private double Longitude = 0.0;
    private Gps CurrentGps = null;

    private GoogleMap mMap;

    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;

    private LatLng SelectedLocation = null;

    FloatingActionButton GetLocationFloatingActionButton;
    int Going;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        //تنظیم کد اکتیویتی جاری جهت شناسایی برای استفاده در کلاس پایه و یا دریافت و ارسال اطلاعات مابین اکتیویتی ها
        setCurrentActivityId(ActivityIdList.MAP_ACTIVITY);
        //کلاس کنترل و مدیریت GPS
        CurrentGps = new Gps();

        //آماده سازی قسمت لودینگ و پنجره خطا و سیستم پیغام در برنامه
        InitView(R.id.MasterContentLinearLayout, null,R.string.show_map_Address_on_map);

        Latitude = getIntent().getExtras().getDouble("Latitude");
        Longitude = getIntent().getExtras().getDouble("Longitude");

        //دریافت حالت فرم -1 حالت ویرایش، ثبت -2 حالت نمایش
        Going = getIntent().getExtras().getInt("Going");


        //ایجاد طرحبندی صفحه
        CreateLayout();
    }

    private void CreateLayout() {
        GetLocationFloatingActionButton = findViewById(R.id.GetLocationFloatingActionButton);
        if (Going == 2) {
            GetLocationFloatingActionButton.setVisibility(View.GONE);
        } else {
            GetLocationFloatingActionButton.setVisibility(View.VISIBLE);
        }
        GetLocationFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnGetLocationFloatingActionButtonClick();
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        try {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

            if (mGoogleApiClient == null) {
                buildGoogleApiClient();
            }
        }
        catch(Exception Ex)
        {

        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        //Initialize Google Play Services
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

        //گرفتن مختصات با کلیک بر روی نقشه
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                //وقتی برای حالت نمایش است نباید تاچ عمل کند
                if (Going!=2) {
                    SelectedLocation = latLng;
                    GetLocationOnTouchOnMap();
                }
            }
        });

        //زوم دوربین بر روی مختصات خاص
        if (Latitude > 0 && Longitude > 0) {
            SelectedLocation = new LatLng(Latitude, Longitude);
            GetLocationOnTouchOnMap();
        }

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        try {
            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(1000);
            mLocationRequest.setFastestInterval(1000);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                if (!(Latitude > 0 && Longitude > 0)) {
                    //این دستور مکان جاری را به وسط صفجه می آورد
                    LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
                }
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onLocationChanged(final Location location) {

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        //Place current location marker

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }


    private void GetLocationOnTouchOnMap() {
        // ایجاد مارکر(ایکون) و فیکس کردن بر روی مختصات انتخاب شده
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(SelectedLocation);
        mMap.clear();
        mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(SelectedLocation));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17));


        CameraPosition cameraPosition =
                new CameraPosition.Builder()
                        .target(SelectedLocation)
                        .zoom(17)
                        .build();
        mMap.animateCamera(
                CameraUpdateFactory.newCameraPosition(cameraPosition),
                new GoogleMap.CancelableCallback() {

                    @Override
                    public void onFinish() {
                    }

                    @Override
                    public void onCancel() {
                    }
                }
        );
    }

    /**
     * بستن فرم انتخاب مکان از روی نقشه و  ارسال مکان انتخابی
     */
    private void OnGetLocationFloatingActionButtonClick() {
        if (CurrentGps.IsMapAlreadyToUse(this, this, R.string.to_use_map_turn_on_location)) {
            if (SelectedLocation!=null && SelectedLocation.latitude>0 && SelectedLocation.longitude>0) {
                if (getIntent().getIntExtra("FromActivityId",-1)>-1) {
                    HashMap<String, Object> Output = new HashMap<>();
                    Output.put("Latitude",  SelectedLocation.latitude);
                    Output.put("Longitude", SelectedLocation.longitude);
                    ActivityResultPassing.Push(new ActivityResult(getIntent().getIntExtra("FromActivityId", -1), getCurrentActivityId(), Output));
                }
                //این قسمت به دلیل SingleInstance بودن Parent بایستی مطمئن شوبم که اکتیویتی Parent بعد از اتمام این اکتیویتی دوباره صدا  زده می شود
                //در حالت خروج از برنامه و ورود دوباره این اکتیوتی ممکن است Parent خود را گم کند
                FinishCurrentActivity();
            }
            else
            {
                ShowToast(getResources().getString(R.string.at_first_please_touch_some_where_to_select),Toast.LENGTH_LONG,MessageType.Warning);
            }
        }
    }


    /**
     * زمانی که پنجره دسترسی به Gps می آید و کاربر باید انتخاب کند که اجازه می دهد ا خیر
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (CurrentGps.IsPermissionEnabled()) {
            if (!CurrentGps.IsEnabled()) {
                CurrentGps.ShowTurnOnGpsDialog(this, this, R.string.to_use_map_turn_on_location);
            } else {
                //GoToLocation();
            }
        } else {
            ShowToast(getResources().getString(R.string.app_permission_denied), Toast.LENGTH_LONG, MessageType.Warning);
            FinishCurrentActivity();
        }
    }

    @Override
    public void OnDismissTurnOnGpsDialog(boolean IsClickYes) {
        if (!IsClickYes) {
            ShowToast(getResources().getString(R.string.for_register_address_turn_on_gps), Toast.LENGTH_LONG, MessageType.Warning);
            FinishCurrentActivity();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Runtime.getRuntime().gc();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}
