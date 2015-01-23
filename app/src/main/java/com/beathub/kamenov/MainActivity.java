package com.beathub.kamenov;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import DataBases.BeatHubBaseHelper;

public class MainActivity extends FragmentActivity implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener, SeekBar.OnSeekBarChangeListener{

    public ArrayList<Song> songList;
    public Song currentPlaylingSong;

    public void setSongList(ArrayList<Song> songList) {
        this.songList = songList;
    }
    public ArrayList<Song> getSongList() {
        return songList;
    }

    public int getCurrentPlayingSongPosition() {
        return currentPlayingSongPosition;
    }

    //card flip
    private boolean isBackSide = false;
    private BeatHubBaseHelper db;

    //media player controllers
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private Handler handler = new Handler();
    private int currentPlayingSongPosition;
    private TextView currentDurationLabel;
    private TextView totalDurationLabel;
    private SeekBar progressBar;
    private Button buttonPlay;
    private Button buttonPrevious;
    private Button buttonNext;

    private TextView currSongName;
    private TextView currSongArtist;
    private TextView numberOfSong;

    private int seekForwardTime = 5000; // 5000 milliseconds
    private int seekBackwardTime = 5000;

    public Song getCurrentPlaylingSong() {
        return currentPlaylingSong;
    }

    public void setCurrentPlaylingSong(Song currentPlaylingSong) {
        this.currentPlaylingSong = currentPlaylingSong;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new BeatHubBaseHelper(getApplicationContext());
        setIdsForViews();

        firstInstalling();

        progressBar.setOnSeekBarChangeListener(this);
        mediaPlayer.setOnCompletionListener(this);

        // By default play first song
        playSong(indexOfLastPlayedSong());
        buttonPlay.setBackgroundResource(R.drawable.pause_button_default);

        RelativeLayout currentSongView = (RelativeLayout) findViewById(R.id.currentSongListView);

        if (savedInstanceState == null) {
            MainArtCoverFragment artcoverFragment = new MainArtCoverFragment();

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

        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!mediaPlayer.isPlaying()) {
                    if (mediaPlayer != null) {
                        mediaPlayer.start();
                        buttonPlay.setBackgroundResource(R.drawable.pause_button_default);
                    }

                } else {
                    if (mediaPlayer != null) {
                        mediaPlayer.pause();
                        // Changing button image to pause button
                        buttonPlay.setBackgroundResource(R.drawable.play_button_default);
                    }
                }
            }
        });

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNextSong();
            }
        });

        buttonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPrevSong();
            }
        });
    }

    @Override
    protected void onPause() {
        saveLastPlayedSong(currentPlayingSongPosition);
        super.onPause();
    }

    public void updateProgressBar() {
            handler.postDelayed(mUpdateTimeTask, 100);
        }

        /**
         * Background Runnable thread
         * */
        private Runnable mUpdateTimeTask = new Runnable() {
            public void run() {
                long totalDuration = mediaPlayer.getDuration();
                long currentDuration = mediaPlayer.getCurrentPosition();

                // Displaying Total Duration time
                totalDurationLabel.setText(Utils.durationFormat(totalDuration));
                // Displaying time completed playing
                currentDurationLabel.setText(Utils.durationFormat(currentDuration));

                // Updating progress bar
                int progress = (Utils.getProgressPercentage(currentDuration, totalDuration));
                //Log.d("Progress", ""+progress);
                progressBar.setProgress(progress);

                // Running this thread after 100 milliseconds
                handler.postDelayed(this, 100);
            }
        };

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        }

         /**
         * When user starts moving the progress handler
         * */
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            handler.removeCallbacks(mUpdateTimeTask);
        }

         /**
         * When user stops moving the progress hanlder
         * */
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            handler.removeCallbacks(mUpdateTimeTask);
            int totalDuration = mediaPlayer.getDuration();
            int currentPosition = Utils.progressToTimer(seekBar.getProgress(), totalDuration);

            // forward or backward to certain seconds
            mediaPlayer.seekTo(currentPosition);

            // update timer progress again
            updateProgressBar();
        }

         @Override
          public void onCompletion(MediaPlayer player) {
             player.reset();
             playNextSong();
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


    private void firstInstalling() {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean previouslyStarted = prefs.getBoolean("previouslyStarted", false);

        if (!previouslyStarted) {
            SharedPreferences.Editor edit = prefs.edit();
            edit.putBoolean("previouslyStarted", Boolean.TRUE);
            edit.commit();

//            new Thread(new Runnable() {
//                @Override
//                public void run() {
                    dbInit();
//                }
//            });
        }
    }

    private int indexOfLastPlayedSong(){

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        int lastPlayedSong = prefs.getInt("lastSong", 0);
        return lastPlayedSong;
    }

    private void saveLastPlayedSong(int position){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor edit = prefs.edit();
        edit.putInt("lastSong", position);
        edit.commit();
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

                    .replace(R.id.fragments_container, new MainArtCoverFragment())

                    .addToBackStack(null)

                            // Commit the transaction.
                    .commit();
            isBackSide = false;
        }
    }

    private void dbInit() {

        // CHANGE FOR YOUR PHONE
        db.addFolderPath("/storage/extSdCard/Music");
        db.importFilesInDBByFolders(getContentResolver());
    }


    @Override
    public void onPrepared(MediaPlayer player) {
        player.start();
    }

    public void playSong(int position){

        currentPlayingSongPosition = position;


        if(songList == null){
            songList = db.getAllSongs();
        }
        Song s = songList.get(position);

        setCurrentPlaylingSong(s);

        currSongName.setText(s.getTitle());
        currSongArtist.setText(s.getArtist());
        numberOfSong.setText((position + 1) + "/" + songList.size());

        try {
            mediaPlayer.reset();
            File f = new File(s.getPath());
            FileInputStream fileIS = new FileInputStream(f);
            FileDescriptor fd = fileIS.getFD();
            mediaPlayer.setDataSource(fd);
            //mediaPlayer.setOnPreparedListener(this);
            //mediaPlayer.prepareAsync();
            mediaPlayer.prepare();
            mediaPlayer.start();
            progressBar.setMax(100);
            progressBar.setProgress(0);

            updateProgressBar();

            //set pause image
            buttonPlay.setBackgroundResource(R.drawable.pause_button_default);

            //flip the view
            if(isBackSide){
                flipCard();
            }

        } catch (IOException e) {
            //e.printStackTrace();
            //Log.i(getString(R.string.app_name), e.getMessage());
        }
    }

    public void playNextSong(){
        if (currentPlayingSongPosition < songList.size() - 1) {
            int pos = currentPlayingSongPosition;
            playSong(pos + 1);
        } else {
            //play first song
            playSong(0);
            currentPlayingSongPosition = 0;
        }
        refreshArtCoverFragment();
    }

    public void playPrevSong(){
        if (currentPlayingSongPosition > 0) {
            int pos = currentPlayingSongPosition;
            playSong(pos - 1);
        } else {
            playSong(songList.size() - 1);
            currentPlayingSongPosition = songList.size() - 1;
        }
        refreshArtCoverFragment();
    }

    private void setIdsForViews() {

        progressBar = (SeekBar) findViewById(R.id.progress_bar);
        currSongName = (TextView) findViewById(R.id.currentSongName);
        currSongArtist = (TextView) findViewById(R.id.currentSongArtistName);
        numberOfSong = (TextView) findViewById(R.id.numberOfSongInList);
        buttonPlay = (Button) findViewById(R.id.playButton);
        buttonNext = (Button) findViewById(R.id.nextButton);
        buttonPrevious = (Button) findViewById(R.id.previousButton);
        currentDurationLabel = (TextView) findViewById(R.id.current_song_duration);
        totalDurationLabel = (TextView) findViewById(R.id.total_song_duration);
    }

    private void refreshArtCoverFragment(){

        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.animator.card_flip_right_in, R.animator.card_flip_right_out,
                        R.animator.card_flip_left_in, R.animator.card_flip_left_out)

                .replace(R.id.fragments_container, new MainArtCoverFragment())

                .addToBackStack(null)

                        // Commit the transaction.
                .commit();
    }

}
