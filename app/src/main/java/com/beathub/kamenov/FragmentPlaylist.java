package com.beathub.kamenov;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class FragmentPlaylist extends Fragment {

    private ListView list = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.listview_layout, container, false);

        //list = (ListView) view.findViewById(R.id.listview_playlists);

        //some logic

        return view;
    }
}
