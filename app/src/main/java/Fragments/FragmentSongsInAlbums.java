package Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beathub.kamenov.BitmapWorkerAsyncTask;
import com.beathub.kamenov.MainActivity;
import com.beathub.kamenov.R;

import java.util.ArrayList;

import ObjectClasses.Song;
import AdaptersAndAbstractClasses.SongAdapter;
import DataBases.BeatHubBaseHelper;


public class FragmentSongsInAlbums extends Fragment {

    private BeatHubBaseHelper db;
    private ArrayList<Song> listOfSongs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_songs_in_album, container, false);

        //get albumId
        Bundle bundle = this.getArguments();
        int albumId = bundle.getInt("albumId", 0);

        db = new BeatHubBaseHelper(getActivity());
        listOfSongs = db.getSongsFromAlbum(albumId);

        RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.current_selected_album_relative_layout);
        ImageView albumImage = (ImageView) relativeLayout.findViewById(R.id.current_album_image);
        TextView albumName = (TextView) relativeLayout.findViewById(R.id.current_album_name);
        TextView countOfSongs = (TextView) relativeLayout.findViewById(R.id.count_of_songs_in_current_album);

        String path = db.getPathOfOneSongInAlbum(albumId);
        loadBitmap(path, albumImage);
        albumName.setText(bundle.getString("albumName", "Unknown album"));

        if(listOfSongs.size() == 1){
            countOfSongs.setText(listOfSongs.size() + " song");
        }else{
            countOfSongs.setText(listOfSongs.size() + " songs");
        }

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //go back to GridView fragment
                FragmentGridViewAlbums fragmentGridViewAlbums = new FragmentGridViewAlbums();

                ((FragmentAlbums)getParentFragment()).openFragment(fragmentGridViewAlbums, getParentFragment().getChildFragmentManager());
            }
        });

        ListView songsInAlbum = (ListView) view.findViewById(R.id.songs_in_album_listview);
        SongAdapter adapter = new SongAdapter(getActivity(), R.layout.song_simple_row_item, db.getSongsFromAlbum(albumId));
        songsInAlbum.setAdapter(adapter);

        songsInAlbum.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((MainActivity)getActivity()).playSong(listOfSongs, position);
            }
        });

        return view;
    }

    public void loadBitmap(String songPath, ImageView imageView) {
        BitmapWorkerAsyncTask task = new BitmapWorkerAsyncTask(imageView);
        task.setResolutionOfImages(40);
        task.execute(songPath);
    }
}
