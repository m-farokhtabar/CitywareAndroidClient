package ir.rayas.app.citywareclient.Service.PushNotification;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


//این کلاس وظیفه ساخت توکن و بروزرسانی اون درصورتی که اکسپایر شده رو بر عهده داره
public class MFirebaseInstanceIdService extends FirebaseInstanceIdService {

    private static final String TAG = "MFirebaseIdService";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

//        FirebaseMessaging.getInstance().subscribeToTopic("news")
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        String msg = getString(R.string.msg_subscribed);
//                        if (!task.isSuccessful()) {
//                            msg = getString(R.string.msg_subscribe_failed);
//                        }
//                        Log.d(TAG, msg);
////                        showSnackBar(msg);
//                    }
//                });

        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String token) {
        // send token to your server of you want !
    }
}
