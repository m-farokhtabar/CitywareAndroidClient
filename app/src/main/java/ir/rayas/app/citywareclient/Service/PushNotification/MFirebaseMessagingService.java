package ir.rayas.app.citywareclient.Service.PushNotification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Random;

import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Repository.AccountRepository;
import ir.rayas.app.citywareclient.View.Master.MainActivity;
import ir.rayas.app.citywareclient.ViewModel.Notification.NotificationViewModel;
import ir.rayas.app.citywareclient.ViewModel.User.AccountViewModel;


//پیام‌های دریافتی FCM رو مدیریت میکنه
public class MFirebaseMessagingService extends FirebaseMessagingService {

    public static final String TAG = MFirebaseMessagingService.class.getSimpleName();

    private NotificationHelper notificationHelper;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage == null)
            return;

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {

            // Check if message contains a data payload.
            if (remoteMessage.getData().size() > 0) {
                Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

                try {
                    //JSONObject json = new JSONObject(remoteMessage.getData().toString());

                    Gson gson = new Gson();
                    Map<String, String> data = remoteMessage.getData();
                    String json = gson.toJson(data);
                    handleDataMessage(json);
                } catch (Exception e) {
                    Log.e(TAG, "Exception: " + e.getMessage());
                }
            } else {
                Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
                handleNotification(remoteMessage.getNotification().getBody());
                showNotification(remoteMessage);
            }
        }


    }

    private void showNotification(RemoteMessage remoteMessage) {
        String channelId = "Default";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.logotheme)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(remoteMessage.getNotification().getBody()).setAutoCancel(true);//.setContentIntent(pendingIntent);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }
        manager.notify(0, builder.build());
    }

    private void handleNotification(String message) {
        if (!NotificationHelper.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent("pushNotification");
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationHelper notificationHelper = new NotificationHelper(getApplicationContext());
            notificationHelper.playNotificationSound();
        } else {
            // If the app is in background, firebase itself handles the notification
        }
    }

    //    private void handleDataMessage(JSONObject json) {
    private void handleDataMessage(String json) {
        Log.e(TAG, "push json: " + json.toString());

        try {
            Gson gson = new Gson();
            Type CollectionType = new TypeToken<NotificationViewModel>() {
            }.getType();
            NotificationViewModel Fb = gson.fromJson(json, CollectionType);

            String title = Fb.getTitle();
            String message = Fb.getMessage();
            String Users = Fb.getUserIdList();

            Log.e(TAG, "title: " + title);
            Log.e(TAG, "message: " + message);

            Gson UserId = new Gson();
            Type listType = new TypeToken<List<Integer>>() {
            }.getType();
            List<Integer> UserIdList = UserId.fromJson(Users, listType);

            AccountRepository ARepository = new AccountRepository(null);
            AccountViewModel AccountViewModel = ARepository.getAccount();

            if (UserIdList == null) {
                Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                resultIntent.putExtra("message", message);

                // check for image attachment
                showNotificationMessage(getApplicationContext(), title, message, resultIntent);
            } else {
                for (int i = 0; i < UserIdList.size(); i++) {
                    if (UserIdList.get(i) == AccountViewModel.getUser().getId()) {
                        // app is in background, show the notification in notification tray
                        Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                        resultIntent.putExtra("message", message);

                        // check for image attachment
                        showNotificationMessage(getApplicationContext(), title, message, resultIntent);
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, Intent intent) {
        notificationHelper = new NotificationHelper(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationHelper.showNotificationMessage(title, message, intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, Intent intent, String imageUrl) {
        notificationHelper = new NotificationHelper(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationHelper.showNotificationMessage(title, message, intent, imageUrl);
    }

    private void showNotification(String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.logotheme)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(new Random().nextInt(100000), notificationBuilder.build());
    }

    public static Intent createWebIntent(String webAddress) {
        return createWebIntent(webAddress, Intent.ACTION_VIEW);
    }

    public static Intent createWebIntent(String webAddress, String action) {
        Intent webIntent = new Intent(action, Uri.parse(webAddress));
        return Intent.createChooser(webIntent, "مشاهده ...");
    }
}