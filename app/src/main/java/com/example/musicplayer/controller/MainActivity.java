package com.example.musicplayer.controller;

import android.os.Bundle;

import com.example.musicplayer.R;
import com.example.musicplayer.model.MusicRepository;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class MainActivity extends AppCompatActivity {
    public static final String MUSIC_PAGE_FRAGMENT = "music page fragment";

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
        MusicRepository.getInstance(this,this).requestRead();
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
}