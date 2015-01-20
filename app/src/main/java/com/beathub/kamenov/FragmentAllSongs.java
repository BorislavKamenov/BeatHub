package com.beathub.kamenov;

import android.app.Fragment;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import DataBases.BeatHubBaseHelper;

public class FragmentAllSongs extends Fragment {

    private BeatHubBaseHelper db;
    private ListView listView;
    private ArrayList<Song> songs;
    private SongAdapter songAdapter;
    private MediaPlayer mp = new MediaPlayer();
    private Button buttonPlay;
    private Button buttonPrevious;
    private Button buttonNext;
    private TextView currSongName;
    private TextView currSongArtist;

    private void setIdsForViews() {
        setViewId(currSongName, R.id.currentSongName);
        setViewId(currSongArtist, R.id.currentSongArtistName);
        setViewId(buttonPlay, R.id.playButton);
        setViewId(buttonNext, R.id.nextButton);
        setViewId(buttonPrevious, R.id.previousButton);
        setViewId(listView, R.id.listview_songs_playlists);
    }

    protected void setViewId(View view, int id) {
        view = (View) getActivity().findViewById(id);
    }

    protected void setListOfSongs() {


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (((MainActivity) getActivity()).getSongList() == null) {
            db = new BeatHubBaseHelper(getActivity().getApplicationContext());
            songs = db.getAllSongs();
            ((MainActivity) getActivity()).setSongList(songs);
        } else {
            songs = ((MainActivity) getActivity()).getSongList();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.listview_layout, container, false);
        songAdapter = new SongAdapter(getActivity().getApplicationContext(), R.layout.song_simple_row_item, songs);
        songs.addAll(db.getAllSongs());
        songAdapter.addAll(songs);
        listView.setAdapter(songAdapter);
        registerForContextMenu(listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                try {
                    if (mp.isPlaying()) {
                        mp.reset();

                    }
                    File f = new File(songs.get(position).getPath());
                    FileInputStream fileIS = null;
                    FileDescriptor fd = null;
                    fileIS = new FileInputStream(f);
                    fd = fileIS.getFD();

                    mp.setDataSource(fd);
                    mp.prepare();
                    mp.start();



                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i(getString(R.string.app_name), e.getMessage());
                }
            }
        });
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Song song = songs.get(position);
                currSongName.setText(song.getTitle());
                currSongArtist.setText(song.getArtist());

                try {
                    if (mp.isPlaying()) {
                        mp.reset();


                    }
                    File f = new File(song.getPath());
                    FileInputStream fileIS = null;
                    FileDescriptor fd = null;
                    fileIS = new FileInputStream(f);
                    fd = fileIS.getFD();
                    mp.setDataSource(fd);
                    mp.prepare();
                    mp.start();

                    //buttonPlay.setBackgroundResource(R.drawable.pause_button_selector);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i(getString(R.string.app_name), e.getMessage());
                }
            }
        });


    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo
            menuInfo) {

        if (v.getId() == R.id.listview_songs_playlists) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            String[] menuItems = getResources().getStringArray(R.array.context_menu_items);

            for (int i = 0; i < menuItems.length; i++) {
                mp.stop();
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

