package com.example.musicplayer.model;

public class Sound {
    private String mAssetsPath;
    private String mName;

    public Sound(String assetsPath) {
        mAssetsPath = assetsPath;
        String[] components = assetsPath.split("/");
        String filename = components[components.length - 1];
        mName=filename.replace(".wav","");
    }

    public String getAssetsPath() {
        return mAssetsPath;
    }

    public String getName() {
        return mName;
    }
}
