package com.example.musicplayer.controller;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.example.musicplayer.R;
import com.example.musicplayer.model.MusicRepository;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

public class MainActivity extends AppCompatActivity {
    public static final String MUSIC_PAGE_FRAGMENT = "music page fragment";
    private static final int READ_EXTERNAL_STORAGE_PERMISSION_REQ = 2;

/*    @Override
    public Fragment createFragment() {
        return null;
    }

    @Override
    public int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    public int getResourceId() {
        return R.id.container;
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!checkPermission()) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_EXTERNAL_STORAGE_PERMISSION_REQ);
        }
        MusicRepository.getInstance(this, this).requestRead();
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(
                this,
                getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
/*        getSupportFragmentManager().beginTransaction()
                .add(R.id.container,PlayMusicFragment.newInstance(), MUSIC_PAGE_FRAGMENT)
                .commit();*/

    }

    private boolean checkPermission() {
        int checkpermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        return checkpermission == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case READ_EXTERNAL_STORAGE_PERMISSION_REQ:
                if (grantResults.length > 0 &&
                        (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    recreate();
                }
                break;
        }
    }
}