package com.beathub.kamenov;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import DataBases.BeatHubBaseHelper;

public class FragmentPlaylist extends Fragment {

    private ListView list = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.allplaylilsts_fragment, container, false);

        ListView listViewPlaylists = (ListView) view.findViewById(R.id.playlists_listView);
        /*PlaylistsAdapter playlistsAdapter = new PlaylistsAdapter(getActivity(), R.layout.playlist_simple_row_item, new BeatHubBaseHelper(getActivity()).getAllPlaylists());
        listViewPlaylists.setAdapter(playlistsAdapter);*/
        return view;
    }
}
