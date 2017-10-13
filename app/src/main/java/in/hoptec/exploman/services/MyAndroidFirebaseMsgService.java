package in.hoptec.exploman.services;

/**
 * Created by shivesh on 21/2/17.
 */

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;


import in.hoptec.exploman.Landing;
import in.hoptec.exploman.R;
import in.hoptec.exploman.utils.NotificationExtras;
import in.hoptec.exploman.utl;

public class MyAndroidFirebaseMsgService extends FirebaseMessagingService {

     private static final String TAG = "MyAndroidFCMService";


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());

         ctx=this;
         utl.init(ctx);



    }

    Gson js;
    Context ctx;

    private void createNotification( RemoteMessage remoteMessage) {
        try {
         //   String mess=remoteMessage.getNotification().getBody();


            Intent intent = new Intent( this , Landing. class );


            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            PendingIntent resultIntent = PendingIntent.getActivity( this , 0, intent,
                    PendingIntent.FLAG_ONE_SHOT);

            String dd="";

            boolean isLoggedIn=(utl.readUserData()!=null);
            Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            Notification.Builder b = new Notification.Builder(this)
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle(""+remoteMessage.getData().get("title"))
                    //.setColor(getResources().getColor(R.color.green_400))
                    .setContentText(""+remoteMessage.getData().get("text"))
                    .setAutoCancel( true )
                    .setSound(notificationSoundURI)
                    .setContentInfo("ExploOman")
                    .setContentIntent(isLoggedIn?resultIntent: PendingIntent.getActivity( this , 0, new Intent(ctx, Landing.class),
                            PendingIntent.FLAG_ONE_SHOT));


            Notification n = NotificationExtras.buildWithBackgroundColor(ctx, b, getResources().getColor(R.color.green_400));
            NotificationManagerCompat.from(this).notify(1, n);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }





}
