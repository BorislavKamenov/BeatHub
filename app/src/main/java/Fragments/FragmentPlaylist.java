package Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import ObjectClasses.Playlist;
import com.beathub.kamenov.R;

import java.util.ArrayList;

import AdaptersAndAbstractClasses.PlaylistsAdapter;
import AdaptersAndAbstractClasses.Song;

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

        View view = inflater.inflate(R.layout.allplaylists_fragment, container, false);

        FragmentListViewPlaylists f = new FragmentListViewPlaylists();
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.playlist_fragment_container_frame_layout, f);
        ft.commit();

        return view;
    }

    public static void openFragment(Fragment f, FragmentManager childFragmentManager){

        FragmentManager fm = childFragmentManager;
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.playlist_fragment_container_frame_layout, f);
        ft.commit();

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
