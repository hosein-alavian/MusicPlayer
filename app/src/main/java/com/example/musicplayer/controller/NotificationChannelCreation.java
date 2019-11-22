package com.example.musicplayer.controller;

import android.app.*;
import android.os.*;

import com.example.musicplayer.R;

public class NotificationChannelCreation extends Application {

    public void createNotificationChannel(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            String channelId=getString(R.string.channelId);
            NotificationChannel notificationChannel=
                    new NotificationChannel(
                            channelId,
                            "my channel",
                            NotificationManager.IMPORTANCE_LOW);
            notificationChannel.setDescription("myDescription");
            NotificationManager notificationManager=
                    getApplicationContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }
}
