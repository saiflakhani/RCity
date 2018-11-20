package com.quicsolv.rcity;

/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

import java.net.URISyntaxException;
import java.util.List;


import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * While subscribed in the background, this service shows a persistent notification with the
 * current set of messages from nearby beacons. Nearby launches this service when a message is
 * found or lost, and this service updates the notification, then stops itself.
 */
public class BackgroundSubscribeIntentService extends BroadcastReceiver {
    private Socket mSocket;

    @Override
    public void onReceive(final Context context, Intent intent) {

        try {
            mSocket = IO.socket("http://49.248.65.54:5051");
        } catch (URISyntaxException e) {
        }


        Nearby.getMessagesClient(context).handleIntent(intent, new MessageListener() {
            @Override
            public void onFound(Message message) {
                Log.i(TAG, "Found message via PendingIntent: " + message);
                AppConstants.currentMessage = new String(message.getContent());
                showNotification(context, "Located you", new String(message.getContent()));
                mSocket.connect();
                String pUID="";
                JSONObject beaconObject = new JSONObject();
                if(!MapsActivity.poiList.isEmpty()){
                    for(Poi curPoi:MapsActivity.poiList){
                        if(curPoi.getName().equalsIgnoreCase(new String(message.getContent()))){
                            pUID = curPoi.getPuid();
                            break;
                        }
                    }
                }
                SharedPreferences prefs = context.getSharedPreferences("RCityPrefs",0);
                String userId = prefs.getString("USER_ID","null");
                String time = String.valueOf(System.currentTimeMillis());
                try {
                    beaconObject.put("userId",userId);
                    beaconObject.put("routeDate",time);
                    if ((pUID.equals(""))) {
                        beaconObject.put("shopId", new String(message.getContent()));
                    } else {
                        beaconObject.put("shopId", pUID);
                    }
                    beaconObject.put("shopName", new String(message.getContent()));
                    beaconObject.put("isDeleted","false");
                    beaconObject.put("time",time);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mSocket.emit("routingMessage", beaconObject.toString());
            }

            @Override
            public void onLost(Message message) {
                Log.i(TAG, "Lost message via PendingIntent: " + message);
                //showNotification(context, "BEACON FOUND", new String(message.getContent()));
            }
        });
    }


    public void showNotification(Context context, String title, String body) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationId = 1;
        String channelId = "R City";
        String channelName = "R City";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body);


        notificationManager.notify(notificationId, mBuilder.build());
    }
}
