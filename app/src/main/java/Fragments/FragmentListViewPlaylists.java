package Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import ObjectClasses.Playlist;

import com.beathub.kamenov.R;

import java.util.ArrayList;

import AdaptersAndAbstractClasses.PlaylistsAdapter;
import DataBases.BeatHubBaseHelper;


public class FragmentListViewPlaylists extends Fragment {

    private BeatHubBaseHelper db;
    private ArrayList<Playlist> listOfPlaylist;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_listview_all_playlist, container, false);

        db = new BeatHubBaseHelper(getActivity());
        listOfPlaylist = db.getAllPlaylists();

        RelativeLayout createNewPlaylist = (RelativeLayout) view.findViewById(R.id.create_new_playlist_relative_layout);
        createNewPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainListsFragment.createNewPlaylist(getActivity());
            }
        });

        ListView playlists = (ListView) view.findViewById(R.id.all_playlists_listview);
        PlaylistsAdapter adapter = new PlaylistsAdapter(getActivity(), R.layout.playlist_simple_row_item, listOfPlaylist);
        playlists.setAdapter(adapter);

        playlists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Playlist playlist = listOfPlaylist.get(position);
                String playlistName = playlist.getName();
                int playlistId = playlist.getPlaylistId();

                Bundle bundle = new Bundle();
                bundle.putString("playlistName", playlistName);
                bundle.putInt("playlistId", playlistId);

                FragmentSongsInPlaylist fragmentSongsInPlaylist = new FragmentSongsInPlaylist();
                fragmentSongsInPlaylist.setArguments(bundle);

                ((FragmentPlaylist) getParentFragment()).openFragment(fragmentSongsInPlaylist,
                        getParentFragment().getChildFragmentManager());
            }
        });

        registerForContextMenu(playlists);

        return view;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add("Delete Playlist");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Toast.makeText(getActivity(), "MARA", Toast.LENGTH_LONG).show();
        return true;
    }
}
