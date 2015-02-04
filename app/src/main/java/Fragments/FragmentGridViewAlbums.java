package Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import ObjectClasses.Album;
import com.beathub.kamenov.R;

import java.util.ArrayList;

import AdaptersAndAbstractClasses.AlbumsGridAdapter;
import DataBases.BeatHubBaseHelper;

public class FragmentGridViewAlbums extends Fragment {

    private BeatHubBaseHelper db;
    private ArrayList<Album> listOfAlbums;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_grid_all_albums, container, false);

        db = new BeatHubBaseHelper(getActivity());
        listOfAlbums = db.getAllAlbums();

        GridView gridView = (GridView) view.findViewById(R.id.albums_grid_view);

        AlbumsGridAdapter adapter = new AlbumsGridAdapter(
                getActivity().getApplicationContext(),
                listOfAlbums);

        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //get and send the clicked album's position to another fragment
                Album currentAlbum = listOfAlbums.get(position);
                String albumName = currentAlbum.getAlbumName();
                int albumId = currentAlbum.getAlbumId();

                Bundle bundle = new Bundle();
                bundle.putString("albumName", albumName);
                bundle.putInt("albumId", albumId);

                FragmentSongsInAlbums fragmentSongsInAlbums = new FragmentSongsInAlbums();
                fragmentSongsInAlbums.setArguments(bundle);

                ((FragmentAlbums)getParentFragment()).openFragment(fragmentSongsInAlbums, getParentFragment().getChildFragmentManager());

            }
        });

        return view;
    }
}
