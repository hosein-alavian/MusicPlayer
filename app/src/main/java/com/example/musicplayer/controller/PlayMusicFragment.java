package com.example.musicplayer.controller;


import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.util.TimeUtils;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.musicplayer.R;
import com.example.musicplayer.model.BeatBoxRepository;
import com.example.musicplayer.model.Sound;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayMusicFragment extends Fragment {


    public static final String SOUND_ID = "sound id";
    private ImageButton mPlayPauseButton;
    private ImageButton mPreviousButton;
    private ImageButton mNextButton;
    private ImageButton mRepeatAllButton;
    private ImageButton mShuffleButton;
    private Integer mSoundId;
    private int mCount;
    private Sound mSound;
    private BeatBoxRepository mBeatBoxRepository;
    private MediaPlayer mMediaPlayer;
    private SeekBar mSeekBar;
    private Handler mHandler;
    private TextView mEndTimeTV;
    private TextView mPassedTimeTV;
    private Runnable mRunnable;

    public PlayMusicFragment() {
        // Required empty public constructor
    }

    public static PlayMusicFragment newInstance() {

        PlayMusicFragment fragment = new PlayMusicFragment();

        Bundle args = new Bundle();
//        args.putInt(SOUND_ID,soundId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSoundId = getArguments().getInt(SOUND_ID);
        }
/*        mBeatBoxRepository = BeatBoxRepository.getInstance(getContext());
        mSound = mBeatBoxRepository.getSound(mSoundId);
        mBeatBoxRepository.play(mSound);*/
//        startService();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(
                R.layout.fragment_play_music,
                container,
                false);
        mHandler = new Handler();
        prepareMediaPlayer();
        initUI(view);
        setListener();
        return view;
    }

    private void initUI(View view) {
        mPlayPauseButton = view.findViewById(R.id.playMusic_imageButton);
        mPreviousButton = view.findViewById(R.id.previousMusic_imageButton);
        mNextButton = view.findViewById(R.id.nextMusic_imageButton);
        mRepeatAllButton = view.findViewById(R.id.repeatAllMusic_imageButton);
        mShuffleButton = view.findViewById(R.id.shuffleMusic_imageButton);
        mSeekBar = view.findViewById(R.id.seekBar);
        mPassedTimeTV = view.findViewById(R.id.passedTime_TextView);
        mPassedTimeTV.setText(convertTimeFormat(mMediaPlayer.getCurrentPosition()));
        mEndTimeTV = view.findViewById(R.id.endTime_textView);
    }

    public void prepareMediaPlayer() {
        mMediaPlayer = MediaPlayer.create(
                getActivity(),
                Settings.System.DEFAULT_ALARM_ALERT_URI);
/*        mBeatBoxRepository = BeatBoxRepository.getInstance(getContext());
        mMediaPlayer=mBeatBoxRepository.getMediaPlayer();*/
//        playCycle();
    }

    private void setListener() {
        mPlayPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                resumePauseService();
                if (!mMediaPlayer.isPlaying()) {
                    mMediaPlayer.start();
                    playCycle();
                    mPlayPauseButton.setImageResource(R.drawable.resume_music_icon);
                } else {
                    mMediaPlayer.pause();
                    mPlayPauseButton.setImageResource(R.drawable.pause_icon);
                }

            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                nextService();
//                mBeatBoxRepository.next(getContext());
                if (mMediaPlayer.isPlaying())
                    mMediaPlayer.stop();
                mMediaPlayer = MediaPlayer.create(getContext(),
                        Settings.System.DEFAULT_RINGTONE_URI);
                mMediaPlayer.start();
                mPlayPauseButton.setImageResource(R.drawable.resume_music_icon);
                mHandler.removeCallbacks(mRunnable);
                playCycle();
            }
        });

        mPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                prevousService();
//                mBeatBoxRepository.previous(getContext());
                if (mMediaPlayer.isPlaying())
                    mMediaPlayer.stop();
                mMediaPlayer = MediaPlayer.create(getContext(),
                        Settings.System.DEFAULT_ALARM_ALERT_URI);
                mMediaPlayer.start();
                mPlayPauseButton.setImageResource(R.drawable.resume_music_icon);
                mHandler.removeCallbacks(mRunnable);
                playCycle();
            }
        });


        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                int duration = mMediaPlayer.getDuration();
                mSeekBar.setMax(duration);
                String dateFormat = convertTimeFormat(duration);
                mEndTimeTV.setText(dateFormat);
            }
        });
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b) {
                    mMediaPlayer.seekTo(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void startService() {
        Intent intent = MyService.newIntent(getActivity());
        intent.setAction("PLAY");
        ContextCompat.startForegroundService(getActivity(), intent);
    }

    private void stopService() {
        Intent intent = MyService.newIntent(getActivity());
        intent.setAction("STOP");
        ContextCompat.startForegroundService(getActivity(), intent);
    }

    private void resumePauseService() {
        Intent intent = MyService.newIntent(getActivity());
        intent.setAction("RESUME");
        ContextCompat.startForegroundService(getActivity(), intent);

    }

    private void nextService() {
        Intent intent = MyService.newIntent(getContext());
        intent.setAction("NEXT");
        ContextCompat.startForegroundService(getContext(), intent);
    }

    private void prevousService() {
        Intent intent = MyService.newIntent(getContext());
        intent.setAction("PREVIOUS");
        ContextCompat.startForegroundService(getContext(), intent);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
//        stopService();
        mMediaPlayer.release();
        mHandler.removeCallbacks(mRunnable);
    }

    public void playCycle() {
        int currentPosition = mMediaPlayer.getCurrentPosition();
        String dateFormat = convertTimeFormat(currentPosition);
        mSeekBar.setProgress(currentPosition);
        mPassedTimeTV.setText(dateFormat);

        if (mMediaPlayer.isPlaying()) {
            mRunnable = new Runnable() {
                @Override
                public void run() {
                    playCycle();
                }
            };

            mHandler.postDelayed(mRunnable, 0);
        }
    }

    private String convertTimeFormat(int currentPosition) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentPosition);
        return new SimpleDateFormat("00:ss")
                .format(calendar.getTime());
    }
}
