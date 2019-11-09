package com.example.musicplayer.controller;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.musicplayer.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class musicListFragment extends Fragment {


    private RecyclerView mRecyclerView;

    public musicListFragment() {
        // Required empty public constructor
    }

    public static musicListFragment newInstance() {

        Bundle args = new Bundle();

        musicListFragment fragment = new musicListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_music_list, container, false);
        mRecyclerView = view.findViewById(R.id.musicList_RecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        SoundAdapter soundAdapter=new SoundAdapter();
        mRecyclerView.setAdapter(soundAdapter);


        return view;
    }

    public class SoundAdapter extends RecyclerView.Adapter<SoundHolder>{


        @NonNull
        @Override
        public SoundHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new SoundHolder(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull SoundHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }

    public class SoundHolder extends RecyclerView.ViewHolder{

        public SoundHolder(@NonNull View itemView) {
            super(itemView);
        }

        void bindSound(){}
    }

}
