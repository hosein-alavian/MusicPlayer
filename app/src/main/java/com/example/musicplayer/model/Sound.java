package com.example.musicplayer.model;

public class Sound {
    private String mAssetsPath;
    private String mName;
    private String mArtist;
    private String mAlbumName;

    public Sound(String assetsPath,String artist,String albumName) {
        mAssetsPath = assetsPath;
        String[] components = assetsPath.split("/");
        String filename = components[components.length - 1];
        mName=filename.replace(".mp3","");
        mArtist=artist;
        mAlbumName =albumName;
    }

    public String getAssetsPath() {
        return mAssetsPath;
    }

    public String getName() {
        return mName;
    }

    public String getArtist() {
        return mArtist;
    }

    public void setArtist(String artist) {
        mArtist = artist;
    }

    public String getAlbumName() {
        return mAlbumName;
    }

    public void setAlbumName(String albumName) {
        mAlbumName = albumName;
    }
}
