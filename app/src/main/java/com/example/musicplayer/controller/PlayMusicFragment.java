package com.example.musicplayer.controller;


import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.musicplayer.R;
import com.example.musicplayer.model.MusicRepository;
import com.example.musicplayer.model.Track;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayMusicFragment extends Fragment {


    public static final String TRACK_POSITION = "sound id";
    public static final Uri DEFAULT_ALARM_ALERT_URI = Settings.System.DEFAULT_ALARM_ALERT_URI;
    private ImageButton mPlayPauseButton;
    private ImageButton mPreviousButton;
    private ImageButton mNextButton;
    private ImageButton mRepeatAllButton;
    private ImageButton mShuffleButton;
    private int mTrackIndex;
    private int mCount;
    private Track mTrack;
    private MediaPlayer mMediaPlayer;
    private SeekBar mSeekBar;
    private Handler mHandler;
    private TextView mEndTimeTV;
    private TextView mPassedTimeTV;
    private Runnable mRunnable;
    private MusicRepository mMusicRepository;
    private ImageView mAlbumArt;
    private TextView mMusicNameTV;
    private TextView mMusicArtistTV;
    private List<Track> mTracks;

    public PlayMusicFragment() {
        // Required empty public constructor
    }

    public static PlayMusicFragment newInstance(int mPosition) {

        PlayMusicFragment fragment = new PlayMusicFragment();

        Bundle args = new Bundle();
        args.putInt(TRACK_POSITION, mPosition);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTrackIndex = getArguments().getInt(TRACK_POSITION);
        }
//        startService();

        mHandler = new Handler();
/*        try {
            mMediaPlayer.stop();
            mMediaPlayer.reset();
            mMediaPlayer.release();

        // mplayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(
                R.layout.fragment_play_music,
                container,
                false);

/*        mMediaPlayer =new MediaPlayer();
        try {*/
//            mMediaPlayer=MediaPlayer.create(getContext(),DEFAULT_ALARM_ALERT_URI);
/*        } catch (IOException e) {
            e.printStackTrace();
        }*/
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
        mPassedTimeTV.setText(convertTimeFormat(0));
        mEndTimeTV = view.findViewById(R.id.endTime_textView);
        mAlbumArt = view.findViewById(R.id.albumArt_imageView);
//        mAlbumArt.setImageBitmap(mTrack.getTrackImage());
        mMusicNameTV = view.findViewById(R.id.musicName_textView);
        mMusicArtistTV = view.findViewById(R.id.musicArtist_textView);

        prepareMediaPlayer();
        trackInitUI(mTrack);
    }

    public void prepareMediaPlayer() {

//        playCycle();
        mMusicRepository = MusicRepository.getInstance(getContext(),
                getActivity());
        mTracks = mMusicRepository.getTracks();
        mTrack = mTracks.get(mTrackIndex);
        mMusicRepository.start(mTrack);
        mMediaPlayer = mMusicRepository.getMediaPlayer();
        playCycle();
        setShuffleTracks();
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                playNextTrack();
            }
        });
    }

    private void setListener() {

        mPlayPauseButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                mMediaPlayer = mMusicRepository.getMediaPlayer();
                if (!mMediaPlayer.isPlaying()) {
//                mMusicRepository.play();
//                    startService();
                    mMusicRepository.play(mTrack);
                    playCycle();
                    mPlayPauseButton.setImageResource(R.drawable.resume_music_icon);
                } else {
//                    mMusicRepository.pause();
//                    pauseService();
                    mMusicRepository.pause();
                    mPlayPauseButton.setImageResource(R.drawable.pause_icon);
                }

            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
//                nextService();
//                mMusicRepository.next(getContext());
/*                if (mMediaPlayer.isPlaying())
                    mMediaPlayer.release();*/
/*                mMediaPlayer = MediaPlayer.create(getContext(),
                        Settings.System.DEFAULT_RINGTONE_URI);
                mMusicRepository.setMediaPlayer(mMediaPlayer);*/
                playNextTrack();
            }
        });

        mPreviousButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
//                previousService();
//                mMusicRepository.previous(getContext());
/*                if (mMediaPlayer.isPlaying())
                    mMediaPlayer.release();*/
/*                mMediaPlayer = MediaPlayer.create(getContext(),
                        Settings.System.DEFAULT_ALARM_ALERT_URI);
                mMusicRepository.setMediaPlayer(mMediaPlayer);*/
                Track previousTrack =
                        mTracks.get(mTrackIndex - 1 == -1 ? 0 : mTrackIndex--);
                mHandler.removeCallbacks(mRunnable);
                trackInitUI(previousTrack);
                mMusicRepository.previous(previousTrack);
//                previousService();
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
                if (b) mMediaPlayer.seekTo(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mRepeatAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mMediaPlayer = mMusicRepository.getMediaPlayer();
                if (!mMediaPlayer.isLooping()) {
                    mMediaPlayer.setLooping(true);
                    mRepeatAllButton.setImageResource(R.drawable.repeat_one_music_icon);
                    Toast.makeText(getContext(),
                            getString(R.string.repeat_current_song),
                            Toast.LENGTH_SHORT).show();
                } else {
                    mMediaPlayer.setLooping(false);
                    mRepeatAllButton.setImageResource(R.drawable.repeat_all_music_icon);
                    Toast.makeText(getContext(),
                            "Repeat list",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        mShuffleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMusicRepository.setShuffle(!mMusicRepository.isShuffle());
                setShuffleTracks();

            }
        });
    }

    private void setShuffleTracks() {
       /* if(mMusicRepository.isShuffle()) {
            Collections.shuffle(mTracks);
            mShuffleButton.setImageResource(R.drawable.shuffle_music_icon);
            Toast.makeText(getContext(),
                    getString(R.string.shuffle),
                    Toast.LENGTH_SHORT).show();
        }else{
            mTracks= mMusicRepository.getTracks();
            mShuffleButton.setImageResource(R.drawable.play_in_order_music_icon);
            Toast.makeText(getContext(),
                    getString(R.string.play_in_order),
                    Toast.LENGTH_SHORT).show();
        }*/
    }

    private void playNextTrack() {
        Track nextTrack =
                mTracks.get(mTrackIndex + 1 == mTracks.size() ? mTrackIndex : ++mTrackIndex);
        mHandler.removeCallbacks(mRunnable);
        trackInitUI(nextTrack);
        mMusicRepository.next(nextTrack);
//                nextService();
        playCycle();
    }

    private void trackInitUI(Track track) {
        mPlayPauseButton.setImageResource(R.drawable.resume_music_icon);
        Glide
                .with(this)
                .load(track.getTrackImage())
                .apply(new RequestOptions().placeholder(R.drawable.track_cover))
                .into(mAlbumArt);
        mMusicNameTV.setText(track.getTrackName());
        mMusicArtistTV.setText(track.getArtist());
    }

/*    private void startService() {
        Intent intent = MyService.newIntent(getActivity());
        intent.setAction("PLAY");
        ContextCompat.startForegroundService(getActivity(), intent);
    }

    private void releaseService() {
        Intent intent = MyService.newIntent(getActivity());
        intent.setAction("RELEASE");
        ContextCompat.startForegroundService(getActivity(), intent);
    }

    private void pauseService() {
        Intent intent = MyService.newIntent(getActivity());
        intent.setAction("PAUSE");
        ContextCompat.startForegroundService(getActivity(), intent);

    }

    private void nextService() {
        Intent intent = MyService.newIntent(getContext());
        intent.setAction("NEXT");
        ContextCompat.startForegroundService(getContext(), intent);
    }

    private void previousService() {
        Intent intent = MyService.newIntent(getContext());
        intent.setAction("PREVIOUS");
        ContextCompat.startForegroundService(getContext(), intent);
    }*/


    @Override
    public void onDestroy() {
        super.onDestroy();
//        releaseService();
        mMusicRepository.release();
        mHandler.removeCallbacks(mRunnable);
    }

    public void playCycle() {
        mMediaPlayer = mMusicRepository.getMediaPlayer();

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

            mHandler.postDelayed(mRunnable, 500);
        }
    }

    private String convertTimeFormat(int currentPosition) {
/*        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentPosition);*/
        return String.format("%02d : %02d",
                TimeUnit.MILLISECONDS.toMinutes(currentPosition),
                TimeUnit.MILLISECONDS.toSeconds(currentPosition) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(currentPosition))
        );
/*        return new SimpleDateFormat("mm:ss")
                .format(calendar.getTime());*/
    }

}
