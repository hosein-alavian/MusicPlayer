package com.example.musicplayer.model;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

public class BeatBoxRepository {

    public static final String SAMPLE_SOUNDS = "sample_sounds";
    public static final String TAG = "BeatBoxRepository";
    public static final int MAX_SOUNDS = 5;
    private AssetManager mAssets;
    private String[] mSoundNames;
    private List<Sound> mSounds = new ArrayList<>();
    private Context mContext;
    private SoundPool mSoundPool;
    private AssetFileDescriptor mAssetFileDescriptor;
    private static BeatBoxRepository instance;
    private MediaPlayer mMediaPlayer;
    public static Uri RINGTONE = Settings.System.DEFAULT_RINGTONE_URI;
    public static Uri ALARM = Settings.System.DEFAULT_ALARM_ALERT_URI;

    public MediaPlayer getMediaPlayer() {
        return mMediaPlayer;
    }

    public static BeatBoxRepository getInstance(Context context) {
        if (instance == null) {
            instance = new BeatBoxRepository(context);
        }
        return instance;
    }

    private BeatBoxRepository(Context context) {
        mContext = context;
        mAssets = context.getAssets();
//        mSoundPool = new SoundPool(MAX_SOUNDS, AudioManager.STREAM_MUSIC, 0);
        mMediaPlayer = MediaPlayer.create(mContext, ALARM);

//        loadSounds();
    }

    private void loadSounds() {
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
                Sound sound = new Sound(assetPath);
//                load(sound);
                getMetaData(sound);
                Uri uri = Uri.parse(sound.getAssetsPath());

                mSounds.add(sound);
            } catch (IOException e) {
                Log.e(TAG, "could not load sound " + filename, e);
            }
        }

    }

    private void load(Sound sound) throws IOException {

        mAssetFileDescriptor = mContext.getAssets().openFd(sound.getAssetsPath());
        int soundId = mSoundPool.load(mAssetFileDescriptor, 1);
        sound.setSoundId(soundId);
    }

    private void getMetaData(Sound sound) throws IOException {
        mAssetFileDescriptor = mContext.getAssets().openFd(sound.getAssetsPath());
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(mAssetFileDescriptor.getFileDescriptor(),
                mAssetFileDescriptor.getStartOffset(),
                mAssetFileDescriptor.getLength());
        String artist = mediaMetadataRetriever.extractMetadata((
                MediaMetadataRetriever.METADATA_KEY_ARTIST));
        String albumName = mediaMetadataRetriever.extractMetadata(
                MediaMetadataRetriever.METADATA_KEY_ALBUM);
        sound.setArtist(artist);
        sound.setAlbumName(albumName);
    }

    public void play() {
/*        Integer soundId = sound.getSoundId();
        if (soundId != null) {
            mSoundPool.play(soundId,
                    1.0f,
                    1.0f,
                    0,
                    0,
                    1.0f);

        }*/
        mMediaPlayer.start();
    }

    public void resumePause() {
/*        Integer soundId = sound.getSoundId();
        if (soundId != null) {
            mSoundPool.resumePause(soundId);
        }*/
        if(!mMediaPlayer.isPlaying()){
            mMediaPlayer.start();

        }
        else
            mMediaPlayer.pause();
    }

/*    public void pause() {
        Integer soundId = sound.getSoundId();
        if (soundId != null) {
            mSoundPool.pause(soundId);
        }
    }*/

    public void next(Context context) {
/*        Integer soundId = sound.getSoundId();
        if (soundId != null) {
            mSoundPool.stop(soundId);
            for (int i = 0; i < mSounds.size(); i++) {
                if(mSounds.get(i)==sound &&i+1< mSounds.size()){
                    play(mSounds.get(i+1));
                    break;
                }
            }
        }*/
        mMediaPlayer.release();
        mMediaPlayer = MediaPlayer.create(context, RINGTONE);
        play();
    }

    public void previous(Context context) {
/*        Integer soundId = sound.getSoundId();
        if (soundId != null) {
            mSoundPool.stop(soundId);
            for (int i = 0; i < mSounds.size(); i++) {
                if (mSounds.get(i) == sound && i - 1 > -1) {
                    play(mSounds.get(i - 1));
                    break;
                }
            }
        }*/
        mMediaPlayer.release();
        mMediaPlayer=MediaPlayer.create(context,ALARM);
    }

    public List<Sound> getSounds() {
        return mSounds;
    }

    public Sound getSound(Integer soundId) {
        for (Sound sound : mSounds) {
            if (sound.getSoundId() == soundId) {
                return sound;
            }
        }
        return null;
    }

    public void release() {
//        mSoundPool.release();
        mMediaPlayer.release();
    }
}
