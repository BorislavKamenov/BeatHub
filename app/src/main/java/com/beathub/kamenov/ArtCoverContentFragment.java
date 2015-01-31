package com.beathub.kamenov;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nineoldandroids.view.ViewHelper;

public class ArtCoverContentFragment extends Fragment {

    private ImageView artcover;

    private static int position;

    public static Fragment newInstance(MainArtCoverFragment context, int pos, float scale, boolean IsBlured) {
        position = pos;
        Bundle b = new Bundle();
        b.putInt("pos", pos);
        b.putFloat("scale", scale);
        b.putBoolean("IsBlured", IsBlured);
        return Fragment.instantiate(context.getActivity(), ArtCoverContentFragment.class.getName(), b);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (container == null) {
            return null;
        }

        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.artcover_inner_view_fragment, container, false);

        artcover = (ImageView) layout.findViewById(R.id.artwork_viewpager_imageview);

        loadBitmap(((MainActivity)getActivity())
                .getCurrentPlayedListOfSongs()
                .get(((MainActivity)getActivity()).getCurrentPlayingSongPosition())
                .getPath(), artcover);

        MyLinearLayout root = (MyLinearLayout) layout.findViewById(R.id.animated_view_pager);
        float scale = this.getArguments().getFloat("scale");
        root.setScaleBoth(scale);
        boolean isBlured = this.getArguments().getBoolean("IsBlured");
        if (isBlured) {
            ViewHelper.setAlpha(root, ArtcoverViewPagerAdapter.getMinAlpha());
            ViewHelper.setRotationY(root, ArtcoverViewPagerAdapter.getMinDegree());
        }
        return layout;
    }

    public void loadBitmap(String songPath, ImageView imageView) {
        BitmapWorkerAsyncTask task = new BitmapWorkerAsyncTask(imageView);
        task.setResolutionOfImages(200);
        task.execute(songPath);
    }



}
