package Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beathub.kamenov.R;


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
        ft.add(R.id.album_fragment_container_frame_layout, f);
        ft.commit();

        return view;
    }

    public static void openFragment(Fragment f, FragmentManager childFragmentManager){

        FragmentManager fm = childFragmentManager;
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.album_fragment_container_frame_layout, f);
        ft.commit();

    }
}
