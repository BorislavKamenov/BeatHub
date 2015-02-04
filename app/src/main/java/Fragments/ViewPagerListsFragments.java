package Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import Fragments.FragmentAlbums;
import Fragments.FragmentAllSongs;
import Fragments.FragmentPlaylist;

public class ViewPagerListsFragments extends FragmentPagerAdapter {

    private String tabtitles[] = new String[] { "All Songs", "Playlists", "Albums" };

    public ViewPagerListsFragments(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {

            // Open FragmentAllSongs.java
            case 0:
                return FragmentAllSongs.newInstance();

            // Open FragmentPlaylist.java
            case 1:
                return FragmentPlaylist.newInstance();

            // Open FragmentAlbums.java
            case 2:
                return FragmentAlbums.newInstance();
        }
        return null;

    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabtitles[position];
    }
}
