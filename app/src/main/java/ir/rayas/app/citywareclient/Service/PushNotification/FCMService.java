package ir.rayas.app.citywareclient.Service.PushNotification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.View.Master.NotificationActivity;

import static ir.rayas.app.citywareclient.Share.Constant.DefaultConstant.FCM_ACTION_CLICK_NOTIFICATION;
import static ir.rayas.app.citywareclient.Share.Constant.DefaultConstant.FCM_ACTION_NEW_NOTIFICATION;




//پیام‌های دریافتی FCM رو مدیریت میکنه
public class FCMService extends FirebaseMessagingService {

    private static final String TAG = FCMService.class.getSimpleName();


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages
        // are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data
        // messages are the type
        // traditionally used with GCM. Notification messages are only received here in
        // onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated
        // notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages
        // containing both notification
        // and data payloads are treated as notification messages. The Firebase console always
        // sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use WorkManager.
               // scheduleJob();
            } else {
                // Handle message within 10 seconds
               // handleNow();
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }


  /*  @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.e(TAG, "New notification from: " + remoteMessage.getFrom());

        if (remoteMessage.getNotification() != null) {
            Log.i(TAG, "Notification message: " + remoteMessage.getNotification());
            notificationMessageReceived(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        }

        if (remoteMessage.getData().size() > 0) {
            Log.i(TAG, "Data Payload: " + remoteMessage.getData());
            dataMessageReceived(remoteMessage.getData());
        }

    }


    private void notificationMessageReceived(String title, String body){

        Intent intent = new Intent(this, NotificationActivity.class); //Open activity if clicked on notification
        PendingIntent pendingIntent;

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setAction(FCM_ACTION_CLICK_NOTIFICATION);
        pendingIntent = PendingIntent.getActivity(this, 0, intent,PendingIntent.FLAG_UPDATE_CURRENT);

        long[] pattern = {500,500,500,500,500}; //Notification vibration pattern

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION); //Notification alert sound

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setVibrate(pattern)
                .setLights(Color.BLUE,1,1)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setSmallIcon(getNotificationIcon())
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setColor(ContextCompat.getColor(this, R.color.colorPrimary));


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(getNotify(), notificationBuilder.build());

    }

    private void dataMessageReceived(java.util.Map<String,String> body) {

        try{

            JSONObject bodyObjects = new JSONObject(body);

            if(bodyObjects.getString("type").equals("banner")){
                showNotificationWithBanner(bodyObjects.getString("title"), bodyObjects.getString("message"), bodyObjects.getString("banner_url"));
            }else if(bodyObjects.getString("type").equals("dialog_message")){
                broadcastTheMessage(bodyObjects.getString("title"), bodyObjects.getString("message"));
            }

        }catch (Exception e){
            Log.e("There is a problem","Exception: "+e);
        }

    }

    private void showNotificationWithBanner(String title, String message, String bannerURL){

        Intent intent = new Intent(this, NotificationActivity.class); //Open activity if clicked on notification
        PendingIntent pendingIntent;

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setAction(FCM_ACTION_CLICK_NOTIFICATION);
        pendingIntent = PendingIntent.getActivity(this, 0, intent,PendingIntent.FLAG_UPDATE_CURRENT);

        long[] pattern = {500,500,500,500,500}; //Notification vibration pattern

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION); //Notification alert sound

        //Style for showing notification With Banner
      //  Bitmap remote_picture = BitmapFactory.decodeResource(getResources(), R.drawable.gray_banner);
        NotificationCompat.BigPictureStyle notiStyle = new NotificationCompat.BigPictureStyle();
//        try {
//            remote_picture = BitmapFactory.decodeStream((InputStream) new URL(bannerURL).getContent());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        notiStyle.bigPicture(remote_picture);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setVibrate(pattern)
                .setLights(Color.BLUE,1,1)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setSmallIcon(getNotificationIcon())
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .setStyle(notiStyle);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(getNotify(), notificationBuilder.build());

    }

    private void broadcastTheMessage(String title, String message){

        Intent notification = new Intent(FCM_ACTION_NEW_NOTIFICATION);
        notification.putExtra("title", title);
        notification.putExtra("message", message);
        LocalBroadcastManager.getInstance(this).sendBroadcast(notification);
    }

    //Random and unique notification ID
    public int getNotify(){
        return Integer.parseInt(new SimpleDateFormat("ddHHmmss",  Locale.US).format(new Date()));
    }

    //In LOLLIPOP and later the small icon has white tint
    private int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.mipmap.ic_launcher : R.mipmap.ic_launcher_round;
    }
*/
}