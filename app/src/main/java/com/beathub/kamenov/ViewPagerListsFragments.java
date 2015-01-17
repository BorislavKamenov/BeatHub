package com.beathub.kamenov;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

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
                FragmentAllSongs fragmentAllSongs = new FragmentAllSongs();
                return fragmentAllSongs;

            // Open FragmentPlaylist.java
            case 1:
                FragmentPlaylist fragmentPlaylist = new FragmentPlaylist();
                return fragmentPlaylist;

            // Open FragmentAlbums.java
            case 2:
                FragmentAlbums fragmentAlbums = new FragmentAlbums();
                return fragmentAlbums;
        }
        return null;

    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabtitles[position];
    }
}
