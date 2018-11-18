package com.quicsolv.rcity;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

import java.util.HashSet;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Service triggered in response to background beacon subscriptions.
 * Displays a notification to the user to indicate new beacons were
 * discovered. Tracks the current set of discovered beacons since last
 * user interaction to show as a count.
 */
public class BeaconService extends BroadcastReceiver {
    private static final int NOTIFICATION_ID = 42;

    private NotificationManager mNotificationManager;

    /* Handle user notifications */
    private void postScanResultNotification(int count,String message,Context context) {



        Notification note = new Notification.Builder(context)
                .setContentTitle("QuicSolv Beacon!")
                .setContentText("Found Beacon : "+message)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                //.setContentIntent(content)
                //.setDeleteIntent(delete)
                .build();

        mNotificationManager.notify(NOTIFICATION_ID, note);
    }


    @Override
    public void onReceive(final Context context, Intent intent) {
        Nearby.getMessagesClient(context).handleIntent(intent, new MessageListener() {
            @Override
            public void onFound(Message message) {
                Log.i(TAG, "Found message via PendingIntent: " + message);
                postScanResultNotification(1,new String(message.getContent()),context);
            }

            @Override
            public void onLost(Message message) {
                Log.i(TAG, "Lost message via PendingIntent: " + message);
            }
        });
    }
}
