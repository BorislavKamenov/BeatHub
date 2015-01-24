package com.beathub.kamenov;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MainArtCoverFragment extends Fragment {

    // You can choose a bigger number for LOOPS, but you know, nobody will fling
    // more than 1000 times just in order to test your "infinite" ViewPager :D
    public final static int LOOPS = 10;
    public final static int FIRST_PAGE = 9;
    public final static float BIG_SCALE = 1.0f;
    public final static float SMALL_SCALE = 0.8f;
    public final static float DIFF_SCALE = BIG_SCALE - SMALL_SCALE;

    public ArtcoverViewPagerAdapter adapter;
    public ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.artcover_fragment, null, false);

        viewPager = (ViewPager) view.findViewById(R.id.artcover_main_viewpager);

        adapter = new ArtcoverViewPagerAdapter(this, getChildFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(adapter);

        // Set current item to the middle page so we can fling to both
        // directions left and right
        //viewPager.setCurrentItem(((MainActivity)getActivity()).getCurrentPlayingSongPosition());

        // Necessary or the pager will only have one extra page to show
        // make this at least however many pages you can see
        viewPager.setOffscreenPageLimit(1);

        // Set margin for pages as a negative number, so a part of next and
        // previous pages will be showed

        //
        viewPager.setPageMargin(Integer.parseInt(getString(R.string.pagermargin)));

        return view;
    }
}
