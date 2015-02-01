package com.beathub.kamenov;


import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.nineoldandroids.view.ViewHelper;

public class ArtcoverViewPagerAdapter extends FragmentPagerAdapter implements
        ViewPager.OnPageChangeListener {

    private boolean swipedLeft = false;
    private int lastPage;
    private MyLinearLayout cur = null;
    private MyLinearLayout next = null;
    private MyLinearLayout prev = null;
    private MyLinearLayout prevprev = null;
    private MyLinearLayout nextnext = null;
    private MainArtCoverFragment context;
    private FragmentManager fm;
    private float scale;
    private boolean IsBlured;
    private static float minAlpha = 0.6f;
    private static float maxAlpha = 1f;
    private static float minDegree = 60.0f;
    private int lastPosition;

    public static float getMinDegree() {
        return minDegree;
    }

    public static float getMinAlpha() {
        return minAlpha;
    }

    public static float getMaxAlpha() {
        return maxAlpha;
    }

    public ArtcoverViewPagerAdapter(MainArtCoverFragment context, FragmentManager fm) {
        super(fm);
        this.fm = fm;
        this.context = context;

        //initialise lastpage
        this.lastPage = ((MainActivity) context.getActivity()).getCurrentPlayedListOfSongs().size() - 1;

    }

    @Override
    public Fragment getItem(int position) {
        // make the first pager bigger than others
        if (position == MainArtCoverFragment.FIRST_PAGE)
            scale = MainArtCoverFragment.BIG_SCALE;
        else {
            scale = MainArtCoverFragment.SMALL_SCALE;
            IsBlured = true;
        }

        Fragment curFragment = ArtCoverContentFragment.newInstance(context, position, scale, IsBlured);
        cur = getRootView(position, 0);
        next = getRootView(position + 1, 1);
        prev = getRootView(position - 1, -1);

        return curFragment;
    }

    @Override
    public int getCount() {
        return ((MainActivity)context.getActivity()).getCurrentPlayedListOfSongs().size();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        if (positionOffset >= 0f && positionOffset <= 1f) {
            positionOffset = positionOffset * positionOffset;
            cur = getRootView(position, 0);
            next = getRootView(position + 1, 1);
            prev = getRootView(position - 1, -1);
            // nextnext = getRootView(position + 2);

            ViewHelper.setAlpha(cur, maxAlpha - 0.5f * positionOffset);
            ViewHelper.setAlpha(next, minAlpha + 0.5f * positionOffset);
            ViewHelper.setAlpha(prev, minAlpha + 0.5f * positionOffset);


//            if (nextnext != null) {
//                ViewHelper.setAlpha(nextnext, minAlpha);
//                ViewHelper.setRotationY(nextnext, -minDegree);
//            }
            if (cur != null) {
                cur.setScaleBoth(MainArtCoverFragment.BIG_SCALE
                        - MainArtCoverFragment.DIFF_SCALE * positionOffset);

                ViewHelper.setRotationY(cur, 0);
            }

            if (next != null) {
                next.setScaleBoth(MainArtCoverFragment.SMALL_SCALE
                        + MainArtCoverFragment.DIFF_SCALE * positionOffset);
                ViewHelper.setRotationY(next, -minDegree);
            }
            if (prev != null) {
                ViewHelper.setRotationY(prev, minDegree);
            }


			/*To animate it properly we must understand swipe direction
             * this code adjusts the rotation according to direction.
			 */
            if (swipedLeft) {
                if (next != null)
                    ViewHelper.setRotationY(next, -minDegree + minDegree * positionOffset);
                if (cur != null)
                    ViewHelper.setRotationY(cur, 0 + minDegree * positionOffset);

            } else {
                if (next != null)
                    ViewHelper.setRotationY(next, -minDegree + minDegree * positionOffset);
                if (cur != null) {
                    ViewHelper.setRotationY(cur, 0 + minDegree * positionOffset);
                }

            }
        }
        if (positionOffset >= 1f) {
            ViewHelper.setAlpha(cur, maxAlpha);
        }
    }

    @Override
    public void onPageSelected(int position) {

/*
 * to get finger swipe direction
 */
        if (lastPage < position) {
            swipedLeft = true;
            ((MainActivity)context.getActivity()).playNextSong();

        } else if (lastPage > position) {
            swipedLeft = false;
            ((MainActivity)context.getActivity()).playNextSong();
        }

        lastPage = position;

    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    //-1 - left page
    //0 - current page
    //1 - right page
    private MyLinearLayout getRootView(int position, int type) {

        MyLinearLayout ly;
        try {
            ly = (MyLinearLayout) fm.findFragmentByTag(this.getFragmentTag(position)).getView().findViewById(R.id.animated_view_pager);
        } catch (Exception e) {

            return null;
        }
        if (ly != null) {

            return ly;
        }
        return null;
    }

    private String getFragmentTag(int position) {

        return "android:switcher:" + context.viewPager.getId() + ":" + position;
    }
}
