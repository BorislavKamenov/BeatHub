package com.beathub.kamenov;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import DataBases.BeatHubBaseHelper;

public class FragmentGridViewAlbums extends Fragment {

    private BeatHubBaseHelper db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_grid_all_albums, container, false);

        db = new BeatHubBaseHelper(getActivity());

        GridView gridView = (GridView) view.findViewById(R.id.albums_grid_view);

        AlbumsGridAdapter adapter = new AlbumsGridAdapter(
                getActivity().getApplicationContext(),
                db.getAllAlbums());

        gridView.setAdapter(adapter);

        return view;
    }
}
