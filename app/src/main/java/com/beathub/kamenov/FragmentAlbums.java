package com.beathub.kamenov;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class FragmentAlbums extends Fragment{

    public static FragmentAlbums newInstance() {
        FragmentAlbums f = new FragmentAlbums();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.album_fragment_container, container, false);

        FragmentGridViewAlbums f = new FragmentGridViewAlbums();
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.album_fragment_container, f);
        ft.commit();

        return view;
    }
}
