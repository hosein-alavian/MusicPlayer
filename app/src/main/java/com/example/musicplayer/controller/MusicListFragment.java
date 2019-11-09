package com.example.musicplayer.controller;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.musicplayer.R;
import com.example.musicplayer.model.Sound;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MusicListFragment extends Fragment {


    public static final String TAB_POSITION = "tab position";
    private RecyclerView mRecyclerView;
    private Sound mSound;
    private int mTabPosition;

    public MusicListFragment() {
        // Required empty public constructor
    }

    public static MusicListFragment newInstance(int tabPosition) {

        MusicListFragment fragment = new MusicListFragment();

        Bundle args = new Bundle();
        args.putInt(TAB_POSITION,tabPosition);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            mTabPosition=getArguments().getInt(TAB_POSITION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_music_list, container, false);
        mRecyclerView = view.findViewById(R.id.musicList_RecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        BeatBox beatBox=new BeatBox(getActivity());
        SoundAdapter soundAdapter=new SoundAdapter(beatBox.getSounds());
        mRecyclerView.setAdapter(soundAdapter);


        return view;
    }

    public class SoundAdapter extends RecyclerView.Adapter<SoundHolder>{

        private List<Sound> mSounds;

        public SoundAdapter(List<Sound> sounds) {
            mSounds = sounds;
        }

        @NonNull
        @Override
        public SoundHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.sound_item_view, parent,false);
            return new SoundHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SoundHolder holder, int position) {
            holder.bindSound(mSounds.get(position));
        }

        @Override
        public int getItemCount() {
            return mSounds==null?0:mSounds.size();
        }
    }

    public class SoundHolder extends RecyclerView.ViewHolder{

        private TextView mSoundNameTV;

        public SoundHolder(@NonNull View itemView) {
            super(itemView);
            mSoundNameTV = itemView.findViewById(R.id.soundName_textView);
        }

        void bindSound(Sound sound){
            mSound = sound;
            switch (mTabPosition) {
                case 0:
                    mSoundNameTV.setText(sound.getName());
                    break;
                case 1:
                    mSoundNameTV.setText(sound.getArtist());
                    break;
                case 2:
                    mSoundNameTV.setText(sound.getAlbumName());
                    break;
            }
        }
    }

}
