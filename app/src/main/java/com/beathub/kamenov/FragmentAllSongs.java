package com.beathub.kamenov;

import android.app.Fragment;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import DataBases.BeatHubBaseHelper;

public class FragmentAllSongs extends Fragment{

    private BeatHubBaseHelper db;
    private ListView listView;
    private ArrayList<Song> songs;
    private SongAdapter songAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(((MainActivity)getActivity()).getSongList() == null) {
            db = new BeatHubBaseHelper(getActivity().getApplicationContext());
            songs = db.getAllSongs();
            ((MainActivity)getActivity()).setSongList(songs);
        }else{
            songs = ((MainActivity)getActivity()).getSongList();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.listview_layout, container, false);

        listView = (ListView) view.findViewById(R.id.listview_songs_playlists);
        songAdapter = new SongAdapter(getActivity().getApplicationContext(), R.layout.song_simple_row_item, songs);
        listView.setAdapter(songAdapter);
        registerForContextMenu(listView);

        return view;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        if(v.getId() == R.id.listview_songs_playlists){
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            String[] menuItems = getResources().getStringArray(R.array.context_menu_items);

            for (int i = 0; i < menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        String[] menuItems = getResources().getStringArray(R.array.context_menu_items);
        String positionText = "";
        int menuItemIndex = item.getItemId();
        switch (menuItemIndex) {
            case 0:
                positionText = menuItems[menuItemIndex];
                Toast.makeText(getActivity().getApplicationContext(), positionText, Toast.LENGTH_LONG).show();
            case 1:
                positionText = menuItems[menuItemIndex];
                Toast.makeText(getActivity().getApplicationContext(), positionText, Toast.LENGTH_LONG).show();
            case 2:
                positionText = menuItems[menuItemIndex];
                Toast.makeText(getActivity().getApplicationContext(), positionText, Toast.LENGTH_LONG).show();
        }
        return true;
    }
}

