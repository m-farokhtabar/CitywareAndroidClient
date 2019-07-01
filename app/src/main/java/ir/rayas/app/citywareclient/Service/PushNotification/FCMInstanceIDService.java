package ir.rayas.app.citywareclient.Service.PushNotification;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import static ir.rayas.app.citywareclient.Share.Constant.DefaultConstant.FCM_Registered;


/**
 * Created by Macs on 12/29/2018.
 */

//این کلاس وظیفه ساخت توکن و بروزرسانی اون درصورتی که اکسپایر شده رو بر عهده داره
public class FCMInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onTokenRefresh() {
//        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
//
//        // Notify UI that registration has completed, so the progress indicator can be hidden.
//        Intent registrationComplete = new Intent(FCM_Registered);
//        registrationComplete.putExtra("fcm_token", refreshedToken);
//        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);


            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
            Log.d(TAG, "Refreshed token: " + refreshedToken);

            // If you want to send messages to this application instance or
            // manage this apps subscriptions on the server side, send the
            // Instance ID token to your app server.
            sendRegistrationToServer(refreshedToken);

    }

    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
    }
}
