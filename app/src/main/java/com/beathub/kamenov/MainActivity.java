package com.beathub.kamenov;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import DataBases.BeatHubBaseHelper;

public class MainActivity extends FragmentActivity {

    public final static int PAGES = 10;
    public final static int LOOPS = 10;
    public final static int FIRST_PAGE = 0;
    public final static float BIG_SCALE = 1.0f;
    public final static float SMALL_SCALE = 0.8f;
    public final static float DIFF_SCALE = BIG_SCALE - SMALL_SCALE;

    public MyPagerAdapter adapter;
    public ViewPager pager;
    public ArrayList<Song> songList;

    public void setSongList(ArrayList<Song> songList) {
        this.songList = songList;
    }

    public ArrayList<Song> getSongList() {
        return songList;
    }

    //card flip
    private boolean isBackSide = false;
    private BeatHubBaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbTests();

        RelativeLayout currentSongView = (RelativeLayout) findViewById(R.id.currentSongListView);
        TextView songTitle = (TextView) findViewById(R.id.currentSongName);
        TextView artistName = (TextView) findViewById(R.id.currentSongArtistName);

        if (savedInstanceState == null) {
            ArtCoverFragment artcoverFragment = new ArtCoverFragment();

            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragments_container, artcoverFragment)
                    .commit();
        }

        currentSongView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flipCard();
            }
        });

        //set default fragment


//        pager = (ViewPager) findViewById(R.id.myviewpager);
//
//        adapter = new MyPagerAdapter(this, this.getSupportFragmentManager());
//        pager.setAdapter(adapter);
//        pager.setOnPageChangeListener(adapter);
//
//        pager.setCurrentItem(FIRST_PAGE);
//
//        pager.setOffscreenPageLimit(2);
//        pager.setPageMargin(10);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //menu item selected
        switch (item.getItemId()) {
          /*  case R.id.action_shuffle:
                musicSrv.setShuffle();
                break;
            case R.id.action_end:
                stopService(playIntent);
                musicSrv = null;
                System.exit(0);
                break;*/
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Downsampling algorithm for an artcover images
     *
     * @param options
     * @param reqWidth  new width of the image
     * @param reqHeight new height of the image
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    /**
     * Get ArtCover image from the metadata of audio file
     *
     * @param filePath - absolute path of the current audio file
     * @return Bitmap
     */
    public Bitmap getAlbumArtCover(String filePath) {

        MediaMetadataRetriever metaRetriever;
        metaRetriever = new MediaMetadataRetriever();
        metaRetriever.setDataSource(filePath);

        byte[] art = metaRetriever.getEmbeddedPicture();
        if (art == null) {
            return BitmapFactory.decodeResource(getResources(), R.drawable.play);
        }


        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(art, 0, art.length, options);

        options.inSampleSize = calculateInSampleSize(options, 50, 50);

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(art, 0, art.length, options);
    }

    //card flip animation
    private void flipCard() {

        if (!isBackSide) {

            getFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(
                            R.animator.card_flip_right_in, R.animator.card_flip_right_out,
                            R.animator.card_flip_left_in, R.animator.card_flip_left_out)

                    .replace(R.id.fragments_container, new MainListsFragment())

                    .addToBackStack(null)

                            // Commit the transaction.
                    .commit();
            isBackSide = true;

        } else {

            getFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(
                            R.animator.card_flip_right_in, R.animator.card_flip_right_out,
                            R.animator.card_flip_left_in, R.animator.card_flip_left_out)

                    .replace(R.id.fragments_container, new ArtCoverFragment())

                    .addToBackStack(null)

                            // Commit the transaction.
                    .commit();
            isBackSide = false;
        }
    }

    private void dbTests() {
        db = new BeatHubBaseHelper(getApplicationContext());
// CHANGE FOR YOUR PHONE
        db.addFolderPath(Environment.getExternalStorageDirectory() + "/Music/Black");
        db.importFilesInDBByFolders(getContentResolver());
    }
}
