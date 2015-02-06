package Fragments;

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

import com.beathub.kamenov.MainActivity;
import com.beathub.kamenov.R;

import java.util.ArrayList;

import ObjectClasses.Song;
import AdaptersAndAbstractClasses.SongAdapter;
import DataBases.BeatHubBaseHelper;

public class FragmentAllSongs extends Fragment {

    private BeatHubBaseHelper db;
    private ListView listView;
    private ArrayList<Song> songs;
    private static SongAdapter songAdapter;

    public static SongAdapter getSongAdapter() {
        return songAdapter;
    }

    public static FragmentAllSongs newInstance() {
        FragmentAllSongs f = new FragmentAllSongs();
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (((MainActivity) getActivity()).getAllSongsList() == null) {
            db = new BeatHubBaseHelper(getActivity().getApplicationContext());
            songs = db.getAllSongs();
            ((MainActivity) getActivity()).setAllSongsList(songs);
        } else {
            songs = ((MainActivity) getActivity()).getAllSongsList();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_allsongs, container, false);
        listView = (ListView) view.findViewById(R.id.list_view_all_songs);
        songAdapter = new SongAdapter(getActivity().getApplicationContext(), R.layout.song_simple_row_item, songs);
        listView.setAdapter(songAdapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        registerForContextMenu(listView);

        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((MainActivity) getActivity()).playSong(songs, position);
            }
        });

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        if (v.getId() == R.id.list_view_all_songs) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            String[] menuItems = getResources().getStringArray(R.array.context_menu_items);

            for (int i = 0; i < menuItems.length; i++) {
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

