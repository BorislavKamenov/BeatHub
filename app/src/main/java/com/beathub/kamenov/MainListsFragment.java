package com.beathub.kamenov;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import DataBases.BeatHubBaseHelper;

public class MainListsFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_pager_fragment, null);

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.pager);

        // Set the ViewPagerAdapter into ViewPager
        viewPager.setAdapter(new ViewPagerListsFragments(getChildFragmentManager()));

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    protected static void addToPlayListDialog(Activity activity) {
       PlaylistsAdapter playlistsAdapter = new PlaylistsAdapter(activity.getApplicationContext(),
                R.layout.playlist_simple_row_item,
                new BeatHubBaseHelper(activity.getApplicationContext()).getAllPlaylists());
        ListView playlistView = (ListView) activity.findViewById(R.id.playlists_listView);
        playlistView.setAdapter(playlistsAdapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity.getApplicationContext());
        LayoutInflater li = LayoutInflater.from(activity.getApplicationContext());
        View pumpView = li.inflate(R.layout.add_to_playlist_dialog_layout, null);
        builder.create();
        builder.show();
    }

}
