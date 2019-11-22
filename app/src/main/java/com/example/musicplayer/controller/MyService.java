package com.example.musicplayer.controller;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.musicplayer.R;
import com.example.musicplayer.model.BeatBoxRepository;

import static com.example.musicplayer.model.BeatBoxRepository.TAG;

public class MyService extends Service {

    private NotificationCompat.Builder builder;
    private BeatBoxRepository mRepositoryInstance;

    public static Intent newIntent(Context context) {
        return new Intent(context, MyService.class);
    }

    public MyService() {
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mRepositoryInstance = BeatBoxRepository.getInstance(this);

        PendingIntent pi = PendingIntent.getActivity(
                this,
                0,
                new Intent(this, MainActivity.class),
                0);

        Intent resumeI = MyService.newIntent(this);
        resumeI.setAction("RESUME");
        PendingIntent resumePi = PendingIntent.getService(this, 0, resumeI, 0);

        Intent previousI = MyService.newIntent(this);
        previousI.setAction("PREVIOUS");
        PendingIntent previousPi = PendingIntent.getService(this, 0, previousI, 0);

        Intent nextI = MyService.newIntent(this);
        nextI.setAction("NEXT");
        PendingIntent nextPi = PendingIntent.getService(this, 0, nextI, 0);

        if (builder == null) {
            builder = new NotificationCompat.Builder(this,
                    getString(R.string.channelId))
                    .setSmallIcon(R.drawable.notification_small_icon)
                    .setContentIntent(pi)
                    .addAction(R.drawable.previous_music_icon, "PREVIOUS", previousPi)
                    .addAction(R.drawable.pause_icon, "RESUME", resumePi)
                    .addAction(R.drawable.next_music_icon, "NEXT", nextPi);

            if (intent.getAction().equals("PLAY")) {
                Log.i(TAG, "onStartCommand: " + intent.getAction() + startId);
                builder.setContentText("music is running")
                        .setSubText("RINGTONE");
                startForeground(1, builder.build());
                mRepositoryInstance.play();
            }
            if (intent.getAction().equals("STOP")) {
                Log.i(TAG, "onStartCommand: " + intent.getAction() + startId);
                mRepositoryInstance.release();
                stopSelf();
            }
            if (intent.getAction().equals("RESUME")) {
                Log.i(TAG, "onStartCommand: " + intent.getAction() + startId);
                startForeground(1, builder.build());
                mRepositoryInstance.resumePause();
            }
            if (intent.getAction().equals("NEXT")) {
                Log.i(TAG, "onStartCommand: " + intent.getAction() + startId);
                builder.setSubText("ALARM");
                startForeground(1,builder.build());
                mRepositoryInstance.next(this);
            }
            if (intent.getAction().equals("PREVIOUS")) {
                Log.i(TAG, "onStartCommand: " + intent.getAction() + startId);
                builder.setSubText("RINGTONE");
                startForeground(1,builder.build());
                mRepositoryInstance.previous(this);
            }
        }

        return Service.START_NOT_STICKY;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        mRepositoryInstance.release();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
