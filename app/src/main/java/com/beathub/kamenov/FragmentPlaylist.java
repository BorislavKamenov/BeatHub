package com.beathub.kamenov;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import DataBases.BeatHubBaseHelper;

public class FragmentPlaylist extends Fragment {
    private ListView listViewPlaylists;
    private PlaylistsAdapter playlistsAdapter;
    private ArrayList<Playlist> playlists;
    private ArrayList<Song> songsFromPlaylist;

    public static FragmentPlaylist newInstance() {
        FragmentPlaylist f = new FragmentPlaylist();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.allplaylilsts_fragment, container, false);

        listViewPlaylists = (ListView) view.findViewById(R.id.all_playlists_listview_in_fragment);

        playlists = new BeatHubBaseHelper(getActivity()).getAllPlaylists();

        playlistsAdapter = new PlaylistsAdapter(getActivity(), R.layout.playlist_simple_row_item, playlists);

        listViewPlaylists.setAdapter(playlistsAdapter);

        listViewPlaylists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                songsFromPlaylist = new BeatHubBaseHelper(getActivity()).getSongsFromPlaylist(position + 1);
                SongAdapter songAdapter = new SongAdapter(getActivity(), R.layout.song_simple_row_item, songsFromPlaylist);
                listViewPlaylists.setAdapter(songAdapter);

                registerForContextMenu(listViewPlaylists);

                listViewPlaylists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ((MainActivity)getActivity()).playSong(songsFromPlaylist, position);
                    }
                });
            }
        });


        return view;
    }
//
//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//
//        if (v.getId() == R.id.all_playlists_listview_in_fragment) {
//            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
//            String[] menuItems = getResources().getStringArray(R.array.playlist_context_menu_items);
//
//            for (int i = 0; i < menuItems.length; i++) {
//                menu.add(Menu.NONE, i, i, menuItems[i]);
//            }
//        }
//    }
//
//    @Override
//    public boolean onContextItemSelected(MenuItem item) {
//
//        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
//        int pos = menuInfo.position;
//
//        int menuItemIndex = item.getItemId();
//
//        switch (menuItemIndex) {
//            case 0:
                  //songsFromPlaylists си е ок, но не взима песента от него, а от класа FragmentAllSongs на ред 97
//                MainListsFragment.addToPlayListDialog(getActivity(), songsFromPlaylist.get(pos));
//            case 1:
//
//            case 2:
//        }
//        return true;
//    }
}
