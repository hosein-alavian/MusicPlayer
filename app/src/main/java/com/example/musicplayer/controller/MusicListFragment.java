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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.musicplayer.R;
import com.example.musicplayer.model.MusicRepository;
import com.example.musicplayer.model.Track;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MusicListFragment extends Fragment {


    public static final String TAB_POSITION = "tab position";
    private RecyclerView mRecyclerView;
    private int mTabPosition;

    public MusicListFragment() {
        // Required empty public constructor
    }

    public static MusicListFragment newInstance(int tabPosition) {

        MusicListFragment fragment = new MusicListFragment();

        Bundle args = new Bundle();
        args.putInt(TAB_POSITION, tabPosition);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTabPosition = getArguments().getInt(TAB_POSITION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_music_list, container, false);
        mRecyclerView = view.findViewById(R.id.musicList_RecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        MusicRepository beatBoxRepository = MusicRepository.getInstance(getContext(), getActivity());
        TracksAdapter tracksAdapter = new TracksAdapter(beatBoxRepository.getTracks());
        mRecyclerView.setAdapter(tracksAdapter);


        return view;
    }

    public class TracksAdapter extends RecyclerView.Adapter<TracksAdapter.SoundHolder> {

        private List<Track> mTracks;
        private Track mTrack;

        public TracksAdapter(List<Track> tracks) {
            mTracks = tracks;
        }

        @NonNull
        @Override
        public SoundHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.sound_item_view, parent, false);
            return new SoundHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SoundHolder holder, int position) {
            holder.bindSound(mTracks.get(position),position);
        }

        @Override
        public int getItemCount() {
            return mTracks == null ? 0 : mTracks.size();
        }


        public class SoundHolder extends RecyclerView.ViewHolder {

            private TextView mtrackNameTV;
            private ImageView mTrackImageView;
            private TextView mArtistTV;
            private int mPosition;

            public SoundHolder(@NonNull View itemView) {
                super(itemView);
                mtrackNameTV = itemView.findViewById(R.id.soundName_textView);
                mTrackImageView = itemView.findViewById(R.id.track_imageView);
                mArtistTV = itemView.findViewById(R.id.artist_textView);
                if (mTabPosition == 0)
                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getFragmentManager().beginTransaction()
                                    .add(R.id.container, PlayMusicFragment.newInstance(mPosition))
                                    .addToBackStack(MusicListFragment.class.getSimpleName())
                                    .commit();
                        }
                    });
            }

            void bindSound(Track track,int position) {
                this.mPosition = position;
                mTrack = track;
                switch (mTabPosition) {
                    case 0:
                        mtrackNameTV.setText(
                                track.getTrackName().length() > 29 ?
                                        track.getTrackName().substring(0, 29) + "..." :
                                        track.getTrackName());
                        mArtistTV.setVisibility(View.VISIBLE);
                        mArtistTV.setText(track.getArtist());
                        break;
                    case 1:
                        mtrackNameTV.setText(track.getArtist().length() > 29 ?
                                track.getArtist().substring(0, 29) + "..." :
                                track.getArtist());
                        break;
                    case 2:
                        mtrackNameTV.setText( track.getAlbumName().length()>29?
                                track.getAlbumName().substring(0,29)+"...":
                                track.getAlbumName()
                        );
                        break;
                }
                Glide
                        .with(itemView)
                        .load(track.getTrackImage())
                        .apply(new RequestOptions().placeholder(R.drawable.track_cover))
                        .into(mTrackImageView);
            }
        }
    }
}
