package com.beathub.kamenov;

import android.os.Bundle;
import android.app.Fragment;
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
        return inflater.inflate(R.layout.allsongs_list_fragment, container, false);
    }
}
