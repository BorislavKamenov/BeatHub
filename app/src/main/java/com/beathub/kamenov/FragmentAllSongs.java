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
import android.widget.ListView;

import java.util.ArrayList;

import DataBases.BeatHubBaseHelper;

public class FragmentAllSongs extends Fragment {

    private BeatHubBaseHelper db;
    private ListView listView;
    private ArrayList<Song> songs;
    private SongAdapter songAdapter;
    private MediaPlayer mp = new MediaPlayer();

    public static FragmentAllSongs newInstance() {
        FragmentAllSongs f = new FragmentAllSongs();
        return f;
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
        View view = inflater.inflate(R.layout.allplaylilsts_fragment, container, false);


        listView = (ListView) view.findViewById(R.id.all_playlists_listview_in_fragment);
        songAdapter = new SongAdapter(getActivity().getApplicationContext(), R.layout.song_simple_row_item, songs);
        listView.setAdapter(songAdapter);
        registerForContextMenu(listView);

        new Thread(new Runnable() {
            @Override
            public void run() {

                db = new BeatHubBaseHelper(getActivity().getApplicationContext());
                db.addFolderPath("storage/extSdCard/Music");
                db.importFilesInDBByFolders(getActivity().getContentResolver());
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
                ((MainActivity) getActivity()).playSong(position);
                Log.i("FragmentAllSongs clicked position in listview", position + "");
            }
        });

    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        if (v.getId() == R.id.all_playlists_listview_in_fragment) {
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

        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int pos = menuInfo.position;

        int menuItemIndex = item.getItemId();

        switch (menuItemIndex) {
            case 0:

                MainListsFragment.addToPlayListDialog(getActivity(), songs.get(pos));

            case 1:

            case 2:
        }
        return true;
    }
}

