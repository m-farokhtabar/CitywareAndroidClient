package ir.rayas.app.citywareclient.Share.Helper;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Service.Helper.AppController;
import ir.rayas.app.citywareclient.Share.Constant.DefaultConstant;
import ir.rayas.app.citywareclient.Share.Layout.Font.Font;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.LayoutUtility;

/**
 * کلاس مار با Gps
 * Created by Programmer on 3/6/2018.
 */

public class Gps {
    /**
     * مشخص نمودن وضعیت GPS
     * روشن و خاموش بودن مکان
     *
     * @return
     */
    public boolean IsEnabled() {
        boolean Output;
        Context CurrentContext = AppController.getInstance();
        LocationManager CurrentLocationManager = (LocationManager) CurrentContext.getSystemService(CurrentContext.LOCATION_SERVICE);
        Output = CurrentLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        Output = Output & CurrentLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return Output;
    }

    /**
     * وجود مجوز استفاده از Gps
     * @return
     */
    public boolean IsPermissionEnabled() {
        boolean Output = false;
        Context CurrentContext = AppController.getInstance();
        if (ContextCompat.checkSelfPermission(CurrentContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Output = true;
        }
        return Output;
    }

    /**
     * نمایش پنجره دریافت مجوز gps
     * @param CurrentActivity
     */
    public void ShowPermissionDialog(Activity CurrentActivity) {
        if (!IsPermissionEnabled()) {
            ActivityCompat.requestPermissions(CurrentActivity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, DefaultConstant.RequestLocationPermission);
        }
    }
    /**
     * نمایش دیالوگ روشن کردن gps
     */
    public boolean ShowTurnOnGpsDialog(final Context CurrentContext,final IResponseTurnOnGpsDialog ResponseTurnOnGpsDialog,int ResourceIdOfDescriptionText) {
        boolean Output = false;
        final Dialog GPSDialog = new Dialog(CurrentContext);
        GPSDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        GPSDialog.setContentView(R.layout.dialog_gps);
        TextView DialogGPSTitleTextView = GPSDialog.findViewById(R.id.DialogGPSTitleTextView);
        TextView DialogGPSOkButton = GPSDialog.findViewById(R.id.DialogGPSOkButton);
        TextView DialogGPSCancelButton = GPSDialog.findViewById(R.id.DialogGPSCancelButton);
        TextViewPersian DialogGPSDescriptionTextView = GPSDialog.findViewById(R.id.DialogGPSDescriptionTextView);
        DialogGPSDescriptionTextView.setText(ResourceIdOfDescriptionText);


        ImageView LogoGpsImageView = GPSDialog.findViewById(R.id.LogoGpsImageView);
        int Width = LayoutUtility.GetWidthAccordingToScreen((Activity) CurrentContext,4);
        RelativeLayout.LayoutParams  LogoGpsImageViewLayoutParams =new RelativeLayout.LayoutParams(Width,Width);
        LogoGpsImageViewLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        LogoGpsImageViewLayoutParams.addRule(RelativeLayout.BELOW, R.id.DialogGPSTitleTextView);
        LogoGpsImageViewLayoutParams.setMargins(0,16,0,16);
        LogoGpsImageView.setLayoutParams(LogoGpsImageViewLayoutParams);

        DialogGPSTitleTextView.setTypeface(Font.MasterLightFont);

        DialogGPSOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentContext.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                GPSDialog.dismiss();
                ResponseTurnOnGpsDialog.OnDismissTurnOnGpsDialog(true);
            }
        });

        DialogGPSCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GPSDialog.dismiss();
                ResponseTurnOnGpsDialog.OnDismissTurnOnGpsDialog(false);
            }
        });
        GPSDialog.show();
        return Output;
    }
    /**
     * بررسی اینکه آیا مجوز وجود دارد و در صورت وجود مجوز حتما دکمه location موبایل نیز روشن باشد
     * @return
     */
    public boolean IsMapAlreadyToUse(Activity CurrentActivity,IResponseTurnOnGpsDialog ResponseTurnOnGpsDialog,int ResourceIdOfDescriptionText) {
        boolean Output = false;
        if (!IsPermissionEnabled()) {
            ShowPermissionDialog(CurrentActivity);
        } else {
            if (!IsEnabled()) {
                ShowTurnOnGpsDialog(CurrentActivity, ResponseTurnOnGpsDialog,ResourceIdOfDescriptionText);
            } else {
                Output = true;
            }
        }
        return Output;
    }
}
