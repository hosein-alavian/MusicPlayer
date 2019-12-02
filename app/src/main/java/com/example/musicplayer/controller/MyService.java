/*
package com.example.musicplayer.controller;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.musicplayer.R;
import com.example.musicplayer.model.MusicRepository;

import static com.example.musicplayer.model.MusicRepository.TAG;

public class MyService extends Service {

    private NotificationCompat.Builder builder;
    private MusicRepository mRepositoryInstance;

    public static Intent newIntent(Context context) {
        return new Intent(context, MyService.class);
    }

    public MyService() {
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotificationChannel();
        mRepositoryInstance = MusicRepository.getInstance(this);

        PendingIntent pi = PendingIntent.getActivity(
                this,
                0,
                new Intent(this, MainActivity.class),
                0);

        Intent playI = MyService.newIntent(this);
        playI.setAction("PLAY");
        PendingIntent playPi = PendingIntent.getService(this, 0, playI, 0);

        Intent previousI = MyService.newIntent(this);
        previousI.setAction("PREVIOUS");
        PendingIntent previousPi = PendingIntent.getService(this, 0, previousI, 0);

        Intent nextI = MyService.newIntent(this);
        nextI.setAction("NEXT");
        PendingIntent nextPi = PendingIntent.getService(this, 0, nextI, 0);

        if (builder == null) {
            builder = new NotificationCompat.Builder(this,
                    getString(R.string.channelId));
            builder
                    .setSmallIcon(R.drawable.notification_small_icon)
                    .setContentIntent(pi)
                    .addAction(R.drawable.previous_music_icon, "PREVIOUS", previousPi);
                    if(mRepositoryInstance.getMediaPlayer()!=null) {
                    builder.addAction(mRepositoryInstance.getMediaPlayer().isPlaying() ?
                                        R.drawable.resume_music_icon : R.drawable.pause_icon,
                                "PLAY",
                                playPi);
                    }
                    builder.addAction(R.drawable.next_music_icon, "NEXT", nextPi)
                    .setAutoCancel(true);

            if (intent.getAction().equals("PLAY")) {
                Log.i(TAG, "onStartCommand: " + intent.getAction() + startId);
                builder.setContentText("music is running")
                        .setSubText("RINGTONE");
                startForeground(1, builder.build());
                mRepositoryInstance.play();
            }
*/
/*            if (intent.getAction().equals("PAUSE")) {
                Log.i(TAG, "onStartCommand: " + intent.getAction() + startId);
                startForeground(1, builder.build());
                mRepositoryInstance.pause();
            }*//*

            if (intent.getAction().equals("RELEASE")) {
                Log.i(TAG, "onStartCommand: " + intent.getAction() + startId);
                mRepositoryInstance.release();
                stopSelf();
            }
            if (intent.getAction().equals("NEXT")) {
                Log.i(TAG, "onStartCommand: " + intent.getAction() + startId);
                builder.setSubText("ALARM");
                startForeground(1, builder.build());
                mRepositoryInstance.next(this);
            }
            if (intent.getAction().equals("PREVIOUS")) {
                Log.i(TAG, "onStartCommand: " + intent.getAction() + startId);
                builder.setSubText("RINGTONE");
                startForeground(1, builder.build());
                mRepositoryInstance.previous(this);
            }
        }

        return Service.START_NOT_STICKY;

    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    getString(R.string.channelId),
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        mRepositoryInstance.release();
//        mRepositoryInstance.pause();
        Log.i(TAG, "onDestroy: myService");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
*/
