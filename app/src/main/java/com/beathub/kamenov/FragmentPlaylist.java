package com.beathub.kamenov;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import DataBases.BeatHubBaseHelper;

public class FragmentPlaylist extends Fragment {

    public static FragmentPlaylist newInstance() {
        FragmentPlaylist f = new FragmentPlaylist();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.allplaylilsts_fragment, container, false);


        ArrayList<Playlist> playlists = new BeatHubBaseHelper(getActivity()).getAllPlaylists();

        ListView listViewPlaylists = (ListView) view.findViewById(R.id.all_playlists_listview_in_fragment);
        PlaylistsAdapter playlistsAdapter = new PlaylistsAdapter(getActivity(), R.layout.playlist_simple_row_item,
                playlists);

        listViewPlaylists.setAdapter(playlistsAdapter);

        listViewPlaylists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //playlists.get(position)......
                //load songs for particular playlist
            }
        });

        Toast.makeText(getActivity(), "fragmentplaylistscalled", Toast.LENGTH_LONG);

        return view;
    }
}
