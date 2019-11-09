package com.example.musicplayer.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

public abstract class SingleFragmentActivity extends AppCompatActivity {

    public abstract Fragment createFragment();
    public abstract int getContentView();
    public abstract int getResourceId();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());

        getSupportFragmentManager()
                .beginTransaction()
                .replace(getResourceId(),createFragment())
                .commit();
    }
}
