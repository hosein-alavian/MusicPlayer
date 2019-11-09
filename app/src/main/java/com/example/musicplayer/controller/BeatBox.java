package com.example.musicplayer.controller;

import android.content.Context;
import android.content.res.AssetManager;
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

    public BeatBox(Context context) {
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
            Sound sound = new Sound(assetPath);
            mSounds.add(sound);
        }
    }

    public List<Sound> getSounds() {
        return mSounds;
    }
}
