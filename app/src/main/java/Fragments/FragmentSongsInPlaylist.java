package Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beathub.kamenov.MainActivity;
import com.beathub.kamenov.R;

import java.util.ArrayList;

import ObjectClasses.Song;
import AdaptersAndAbstractClasses.SongAdapter;
import DataBases.BeatHubBaseHelper;

public class FragmentSongsInPlaylist extends Fragment {

    private BeatHubBaseHelper db;
    private ArrayList<Song> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_songs_in_playlist, container, false);

        Bundle bundle = this.getArguments();
        int playlistId = bundle.getInt("playlistId", 0);

        db = new BeatHubBaseHelper(getActivity());
        list = db.getSongsFromPlaylist(playlistId);

        RelativeLayout layout = (RelativeLayout) view.findViewById(R.id.current_selected_playlist_relative_layout);
        TextView playlistName = (TextView) layout.findViewById(R.id.current_playlist_name);
        TextView countOfSongs = (TextView) layout.findViewById(R.id.count_of_songs_in_current_playlist);

        playlistName.setText(bundle.getString("playlistName"));

        if(list.size() == 1){
            countOfSongs.setText(list.size() + " song");
        }else{
            countOfSongs.setText(list.size() + " songs");
        }

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentListViewPlaylists fragmentListViewPlaylists = new FragmentListViewPlaylists();

                ((FragmentPlaylist)getParentFragment()).openFragment(fragmentListViewPlaylists,
                        getParentFragment().getChildFragmentManager());
            }
        });

        ListView songsInPlaylist = (ListView) view.findViewById(R.id.songs_in_playlist_listview);
        SongAdapter adapter = new SongAdapter(getActivity(), R.layout.song_simple_row_item, list);
        songsInPlaylist.setAdapter(adapter);

        songsInPlaylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ((MainActivity)getActivity()).playSong(list, position);
            }
        });

        return view;
    }
}
