package ir.rayas.app.citywareclient.Share.Helper;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import ir.rayas.app.citywareclient.Service.Helper.AppController;
import ir.rayas.app.citywareclient.Share.Constant.DefaultConstant;

/**
 * Created by Hajar on 1/14/2019.
 */

public class ExternalStorage {


    public boolean IsPermissionEnabled() {
        boolean Output = false;
        Context CurrentContext = AppController.getInstance();
        if (ContextCompat.checkSelfPermission(CurrentContext, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Output = true;
        }
        return Output;
    }

    /**
     * نمایش پنجره دریافت مجوز ExternalStorage
     *
     * @param CurrentActivity
     */
    public void ShowPermissionDialog(Activity CurrentActivity) {
        if (!IsPermissionEnabled()) {
            ActivityCompat.requestPermissions(CurrentActivity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, DefaultConstant.RequestLocationPermission);
        }
    }


    /**
     *
     *
     * @return
     */
    public boolean IsUploadImageToUse(Activity CurrentActivity) {
        boolean Output = false;
        if (!IsPermissionEnabled()) {
            ShowPermissionDialog(CurrentActivity);
        } else {

            Output = true;

        }
        return Output;
    }
}

