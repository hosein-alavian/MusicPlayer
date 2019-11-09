package com.example.musicplayer.controller;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaMetadataRetriever;
import android.util.Log;

import com.example.musicplayer.model.Sound;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BeatBox {

    public static final String SAMPLE_SOUNDS = "sample_sounds";
    public static final String TAG = "BeatBox";
    private AssetManager mAssets;
    private String[] mSoundNames;
    private List<Sound> mSounds=new ArrayList<>();
    private Context mContext;

    public BeatBox(Context context) {
        mContext = context;
        mAssets = context.getAssets();
        loadSounds();
    }

    private void loadSounds() {
        try {
            mSoundNames = mAssets.list(SAMPLE_SOUNDS);
            Log.i(TAG, "Found " + mSoundNames.length + " sounds");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG,"could not list assets",e);
        }
        for (String filename : mSoundNames) {
            String assetPath = SAMPLE_SOUNDS + "/" + filename;

            AssetFileDescriptor afd = null;
            try {
                afd = mContext.getAssets().openFd(assetPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            String artist = mmr.extractMetadata((MediaMetadataRetriever.METADATA_KEY_ARTIST));
            String albumName = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
            Sound sound = new Sound(assetPath,artist,albumName);

            mSounds.add(sound);
        }

    }

    public List<Sound> getSounds() {
        return mSounds;
    }
}
