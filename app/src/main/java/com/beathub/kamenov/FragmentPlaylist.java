package com.beathub.kamenov;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import DataBases.BeatHubBaseHelper;

public class FragmentPlaylist extends Fragment {
    private ListView listViewPlaylists;
    private PlaylistsAdapter playlistsAdapter;
    private ArrayList<Playlist> playlists;

    public static FragmentPlaylist newInstance() {
        FragmentPlaylist f = new FragmentPlaylist();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.allplaylilsts_fragment, container, false);

        listViewPlaylists = (ListView) view.findViewById(R.id.all_playlists_listview_in_fragment);

        playlists = new BeatHubBaseHelper(getActivity()).getAllPlaylists();

        playlistsAdapter = new PlaylistsAdapter(getActivity(), R.layout.playlist_simple_row_item,
                playlists);

        listViewPlaylists.setAdapter(playlistsAdapter);

        listViewPlaylists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<Song> songsFromPlaylist = new ArrayList<Song>();
                songsFromPlaylist = new BeatHubBaseHelper(getActivity()).getSongsFromPlaylist(position + 1);
                SongAdapter songAdapter = new SongAdapter(getActivity(), R.layout.song_simple_row_item, songsFromPlaylist);
                listViewPlaylists.setAdapter(songAdapter);

                listViewPlaylists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                });
            }
        });


        return view;
    }
}
