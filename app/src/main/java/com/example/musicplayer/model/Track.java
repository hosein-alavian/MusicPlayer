package com.example.musicplayer.model;

import android.net.Uri;

public class Track {
    private long mTrackId;
    private String mAssetsPath;
    private String mTrackName;
    private String mArtist;
    private String mAlbumName;
    private Uri mTrackImage;
    private String mData;

/*    public Track() {

*//*        mAssetsPath = assetsPath;
        String[] components = assetsPath.split("/");
        String filename = components[components.length - 1];
        mTrackName =filename.replace(".mp3","");*//*
    }*/

    public Track(long trackId, String mTrackName, String artist, String albumName, String data, Uri trackImage) {
        mTrackId=trackId;
        this.mTrackName = mTrackName;
        mArtist = artist;
        mAlbumName = albumName;
        mData=data;
        mTrackImage =trackImage;
    }

    public long getTrackId() {
        return mTrackId;
    }

    public void setTrackId(long trackId) {
        mTrackId = trackId;
    }

    public String getAssetsPath() {
        return mAssetsPath;
    }

    public String getTrackName() {
        return mTrackName;
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

    public Uri getTrackImage() {
        return mTrackImage;
    }

    public void setTrackImage(Uri trackImage) {
        mTrackImage = trackImage;
    }

    public String getData() {
        return mData;
    }

    public void setData(String data) {
        mData = data;
    }
}
