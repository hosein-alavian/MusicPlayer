package com.example.musicplayer.model;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import com.example.musicplayer.controller.PlayMusicFragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MusicRepository {

    public static final String CONTENT_MEDIA_EXTERNAL_AUDIO_ALBUMART = "content://media/external/audio/albumart";
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    public static final String TAG = "MusicRepository";
    private AssetManager mAssets;
    private List<Track> mTracks;
    private Context mContext;
    private static MusicRepository instance;
    private MediaPlayer mMediaPlayer;
    private FragmentManager mFragmentManager;
    private PlayMusicFragment mPlayMusicFragment;
    private Handler mHandler;
    private Runnable mRunnable;
    private Activity mActivity;
    private boolean isShuffle=false;


    public MediaPlayer getMediaPlayer() {
        return mMediaPlayer;
    }

    public List<Track> getTracks() {
        return mTracks;
    }

    public boolean isShuffle() {
        return isShuffle;
    }

    public void setShuffle(boolean shuffle) {
        isShuffle = shuffle;
    }

    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        mMediaPlayer = mediaPlayer;
    }

/*    public static MusicRepository getInstance(Context context, FragmentManager fragmentManager) {
        if (instance == null) {
            instance = new MusicRepository(context, fragmentManager);
        }
        return instance;
    }*/

    public static MusicRepository getInstance(Context context, Activity activity) {
        if (instance == null) {
            instance = new MusicRepository(context, null, activity);
        }
        return instance;
    }

    private MusicRepository(Context context, FragmentManager fragmentManager, Activity activity) {
        mActivity = activity;
        mHandler = new Handler();
        mContext = context;
        mFragmentManager = fragmentManager;
//        getFragment();
        mAssets = context.getAssets();
        mTracks = new ArrayList<>();
//        mMediaPlayer = MediaPlayer.create(mContext, ALARM);

//        loadSounds();
    }

    public void requestRead() {
        if (ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(mActivity,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        } else {
            try {
                loadAudio();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadAudio() throws IOException {

        ContentResolver contentResolver = mContext.getContentResolver();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
        Cursor cursor = contentResolver.query(uri,
                null,
                selection,
                null,
                sortOrder);

        if (cursor != null && cursor.getCount() > 0) {
//                InputStream in=null;
            while (cursor.moveToNext()) {
                String trackId =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                String data =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                String title =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String album =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                String artist =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                Uri imagePath =
                        ContentUris.withAppendedId(Uri.parse(CONTENT_MEDIA_EXTERNAL_AUDIO_ALBUMART),
                                Long.parseLong(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID))));
                // Save to audioList
/*                    in = contentResolver
                            .openInputStream(Uri.fromFile(new File(data)));
                    Bitmap artwork = BitmapFactory.decodeStream(in);*/
//                mAssetFileDescriptor = mContext.getAssets().openFd(data);
/*                MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();

                mediaMetadataRetriever.setDataSource(new File(data).getPath());
                byte[] artwork = mediaMetadataRetriever.getEmbeddedPicture();*/

/*                Bitmap image = BitmapFactory
                        .decodeByteArray(artwork, 0, artwork.length);*/

                mTracks.add(new Track(Long.parseLong(trackId), title, artist, album, data, imagePath));
            }
//                in.close();
        }

        cursor.close();


    }

/*    private void loadSounds() {
        try {
            mSoundNames = mAssets.list(SAMPLE_SOUNDS);
            Log.i(TAG, "Found " + mSoundNames.length + " sounds");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "could not list assets", e);
        }
        for (String filename : mSoundNames) {
            try {
                String assetPath = SAMPLE_SOUNDS + "/" + filename;
                Track sound = new Track(assetPath);
//                load(sound);
                getMetaData(sound);
                mTracks.add(sound);
            } catch (IOException e) {
                Log.e(TAG, "could not load sound " + filename, e);
            }
        }

    }*/

/*    private void load(Track sound) throws IOException {

        mAssetFileDescriptor = mContext.getAssets().openFd(sound.getAssetsPath());
        int soundId = mSoundPool.load(mAssetFileDescriptor, 1);
        sound.setTrackId(soundId);
    }*/

/*    private void getMetaData(Track sound) throws IOException {
        mAssetFileDescriptor = mContext.getAssets().openFd(sound.getAssetsPath());
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(mAssetFileDescriptor.getFileDescriptor(),
                mAssetFileDescriptor.getStartOffset(),
                mAssetFileDescriptor.getLength());
        String artist = mediaMetadataRetriever.extractMetadata((
                MediaMetadataRetriever.METADATA_KEY_ARTIST));
        String albumName = mediaMetadataRetriever.extractMetadata(
                MediaMetadataRetriever.METADATA_KEY_ALBUM);
        String musicName = mediaMetadataRetriever.extractMetadata(
                MediaMetadataRetriever.METADATA_KEY_TITLE);
        sound.setArtist(artist);
        sound.setAlbumName(albumName);
        sound.setTrackName(musicName);
    }*/

    public void start(Track track) {
//            mMediaPlayer=new MediaPlayer();
        //                mAssetFileDescriptor = mContext.getAssets().openFd(track.getAssetsPath());
        Uri contentUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, track.getTrackId());
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mMediaPlayer.setDataSource(mContext, contentUri);
            mMediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMediaPlayer.start();
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {

            }
        });
    }

    public void play(Track track) {
/*        Integer soundId = track.getTrackId();
        if (soundId != null) {
            mSoundPool.play(soundId,
                    1.0f,
                    1.0f,
                    0,
                    0,
                    1.0f);
        }*/

/*        mPlayMusicFragment.getSeekBar().setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
        });*/
//        if (!mMediaPlayer.isPlaying())
//        {
        mMediaPlayer.start();
//            playCycle();
/*            mPlayMusicFragment.getPlayPauseButton().setImageResource(R.drawable.resume_music_icon);
        } else {
            mMediaPlayer.pause();
            mPlayMusicFragment.getPlayPauseButton().setImageResource(R.drawable.pause_icon);
        }*/
    }

    public void pause() {
/*        Integer soundId = sound.getTrackId();
        if (soundId != null) {
            mSoundPool.pause(soundId);
        }*/
        mMediaPlayer.pause();
    }

/*    public void pause() {
        Integer soundId = sound.getTrackId();
        if (soundId != null) {
            mSoundPool.pause(soundId);
        }
    }*/

    public void next(Track track) {
/*        Integer soundId = track.getTrackId();
        if (soundId != null) {
            mSoundPool.stop(soundId);
            for (int i = 0; i < mTracks.size(); i++) {
                if(mTracks.get(i)==track &&i+1< mTracks.size()){
                    play(mTracks.get(i+1));
                    break;
                }
            }
        }*/
        if (mMediaPlayer.isPlaying())
            mMediaPlayer.stop();
        mMediaPlayer.release();
//        mMediaPlayer=new MediaPlayer();
        //            mAssetFileDescriptor = mContext.getAssets().openFd(track.getAssetsPath());
        mMediaPlayer = MediaPlayer.create(mContext, Uri.fromFile(new File(track.getData())));
        //        mMediaPlayer = MediaPlayer.create(context, RINGTONE);
//        mHandler.removeCallbacks(mRunnable);
        play(track);
    }

    public void previous(Track track) {
/*        Integer soundId = track.getTrackId();
        if (soundId != null) {
            mSoundPool.stop(soundId);
            for (int i = 0; i < mTracks.size(); i++) {
                if (mTracks.get(i) == track && i - 1 > -1) {
                    play(mTracks.get(i - 1));
                    break;
                }
            }
        }*/
        if (mMediaPlayer.isPlaying())
            mMediaPlayer.stop();
        mMediaPlayer.release();
//        mMediaPlayer=new MediaPlayer();
        //            mAssetFileDescriptor = mContext.getAssets().openFd(track.getAssetsPath());
        mMediaPlayer = MediaPlayer.create(mContext, Uri.fromFile(new File(track.getData())));
        //        mHandler.removeCallbacks(mRunnable);
        play(track);
    }

    public Track getTrack(long trackId) {
        for (Track track : mTracks) {
            if (track.getTrackId() == trackId) {
                return track;
            }
        }
        return null;
    }

    public Track getNextTrack(long trackId) {
        for (int i = 0; i < mTracks.size(); i++) {
            if (i + 1 != mTracks.size() && mTracks.get(i).getTrackId() == trackId) {
                return mTracks.get(i + 1);
            } else if (i + 1 == mTracks.size()) {
                return mTracks.get(i);
            }
        }
        return null;
    }

    public Track getPreviousSound(long trackId) {
        for (int i = 0; i < mTracks.size(); i++) {
            if(i==0 && mTracks.get(i).getTrackId() == trackId)
                return mTracks.get(i);
            else if (mTracks.get(i).getTrackId() == trackId) {
                return mTracks.get(i - 1);
            }
        }
        return null;
    }

/*    public void getFragment() {
        Fragment fragmentByTag = mFragmentManager
                .findFragmentByTag(MainActivity.MUSIC_PAGE_FRAGMENT);
        if (fragmentByTag != null) {
            mPlayMusicFragment = (PlayMusicFragment) fragmentByTag;
        }
    }*/

    public void release() {
//        mSoundPool.release();
        mMediaPlayer.release();
    }

/*    public void playCycle() {
        if (mMediaPlayer != null) {
            int currentPosition = mMediaPlayer.getCurrentPosition();
            String dateFormat = convertTimeFormat(currentPosition);
            mPlayMusicFragment.getSeekBar().setProgress(currentPosition);
            mPlayMusicFragment.getPassedTimeTV().setText(dateFormat);
        }

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
    }*/
}
