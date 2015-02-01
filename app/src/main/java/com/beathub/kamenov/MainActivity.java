package com.beathub.kamenov;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import DataBases.BeatHubBaseHelper;

public class MainActivity extends FragmentActivity implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener, SeekBar.OnSeekBarChangeListener {

    private final static int REPEAT_OFF = 0;
    private final static int REPEAT_ALL_SONGS = 1;
    private final static int REPEAT_CURR_SONG = 2;

    private final static int SHUFFLE_OFF = 3;
    private final static int SHUFFLE_ON = 4;

    private final static String SHARED_PREFS_LAST_PLAYED_SONG = "lastPlayedSong";
    private final static String TAB_ID = "tabId";
    private final static String PLAYLIST_ID = "playlistId";
    private final static String ALBUM_ID = "playlistId";
    private final static String LAST_SONG_ID = "posOfSong";

    private int currentTab = 0;
    private int currentPlaylist = -1;
    private int currentAlbum = -1;
    private final static int ALL_SONGS_TAB = 0;
    private final static int PLAYLISTS_TAB = 1;
    private final static int ALBUMS_TAB = 2;

    private int repeatMode = REPEAT_OFF;
    private int shuffleMode = SHUFFLE_OFF;

    private ArrayList<Song> currentPlayedListOfSongs;
    public ArrayList<Song> allSongsList;
    public ArrayList<Song> playlistSongsList;
    public Song currentPlaylingSong;

    //card flip
    private boolean isBackSide = false;
    private BeatHubBaseHelper db;

    //media player controllers
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private FileDescriptor fd;
    private Handler handler = new Handler();
    private int currentPlayingSongPosition;
    private TextView currentDurationLabel;
    private TextView totalDurationLabel;
    private SeekBar progressBar;
    private Button buttonPlay;
    private Button buttonPrevious;
    private Button buttonNext;
    private Button buttonRepeat;
    private Button buttonShuffle;

    private TextView currSongName;
    private TextView currSongArtist;
    private TextView numberOfSong;

    public int getCurrentTab() {
        return currentTab;
    }

    public void setCurrentTab(int currentTab) {
        this.currentTab = currentTab;
    }

    public int getCurrentAlbum() {
        return currentAlbum;
    }

    public void setCurrentAlbum(int currentAlbum) {
        this.currentAlbum = currentAlbum;
    }

    public int getCurrentPlaylist() {
        return currentPlaylist;
    }

    public void setCurrentPlaylist(int currentPlaylist) {
        this.currentPlaylist = currentPlaylist;
    }

    public ArrayList<Song> getCurrentPlayedListOfSongs() {
        return currentPlayedListOfSongs;
    }

    public void setCurrentPlayedListOfSongs(ArrayList<Song> currentPlayedListOfSongs) {
        this.currentPlayedListOfSongs = currentPlayedListOfSongs;
    }

    public void setAllSongsList(ArrayList<Song> songList) {
        this.allSongsList = songList;
    }

    public ArrayList<Song> getAllSongsList() {
        return allSongsList;
    }

    public Song getCurrentPlaylingSong() {
        return currentPlaylingSong;
    }

    public void setCurrentPlaylingSong(Song currentPlaylingSong) {
        this.currentPlaylingSong = currentPlaylingSong;
    }

    public int getCurrentPlayingSongPosition() {
        return currentPlayingSongPosition;
    }

    public void setCurrentPlayingSongPosition(int currentPlayingSongPosition) {
        this.currentPlayingSongPosition = currentPlayingSongPosition;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new BeatHubBaseHelper(getApplicationContext());
        setIdsForViews();

        firstInstalling();
        initializeSongLists();

        progressBar.setOnSeekBarChangeListener(this);
        mediaPlayer.setOnCompletionListener(this);

        // After install by default play first song
        playSong(currentPlayedListOfSongs, 0);

        buttonPlay.setBackgroundResource(R.drawable.pause_button_default);
        buttonRepeat.setBackgroundResource(R.drawable.repeat_button_off);
        buttonShuffle.setBackgroundResource(R.drawable.shuffle_button_off);

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

        //long click to stop the player
        buttonPlay.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                stopPlayer();

                return true;
            }
        });

        //play next song
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNextSong();
            }
        });

        //play previous song
        buttonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPrevSong();
            }
        });

        buttonRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (repeatMode) {
                    case REPEAT_OFF:
                        buttonRepeat.setBackgroundResource(R.drawable.repeat_button_all_songs);
                        showToastWithMessage("Repeat all songs");
                        repeatMode = REPEAT_ALL_SONGS;
                        break;

                    case REPEAT_ALL_SONGS:
                        buttonRepeat.setBackgroundResource(R.drawable.repeat_button_same_song);
                        showToastWithMessage("Repeat this song");
                        repeatMode = REPEAT_CURR_SONG;
                        break;

                    case REPEAT_CURR_SONG:
                        buttonRepeat.setBackgroundResource(R.drawable.repeat_button_off);
                        showToastWithMessage("Repeat off");
                        repeatMode = REPEAT_OFF;
                        break;
                }
            }
        });

        buttonShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (shuffleMode) {
                    case SHUFFLE_OFF:
                        buttonShuffle.setBackgroundResource(R.drawable.shuffle_button_on);
                        showToastWithMessage("Shuffle on");
                        shuffleMode = SHUFFLE_ON;
                        break;

                    case SHUFFLE_ON:
                        buttonShuffle.setBackgroundResource(R.drawable.shuffle_button_off);
                        showToastWithMessage("Shuffle off");
                        shuffleMode = SHUFFLE_OFF;
                        break;
                }
            }
        });

    }

    @Override
    protected void onPause() {
        saveLastPlayedSong(currentPlayingSongPosition, currentPlaylist, currentTab);
        super.onPause();
    }

    public void updateProgressBar() {
        handler.postDelayed(mUpdateTimeTask, 100);
    }

    /**
     * Background Runnable thread
     */
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
     */
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        handler.removeCallbacks(mUpdateTimeTask);
    }

    /**
     * When user stops moving the progress handler
     */
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

    //what to do when the song complete
    @Override
    public void onCompletion(MediaPlayer player) {

        player.reset();

        switch (repeatMode) {
            case REPEAT_ALL_SONGS:

                switch (shuffleMode) {

                    case SHUFFLE_ON:
                        playSong(currentPlayedListOfSongs, new Random().nextInt(currentPlayedListOfSongs.size()));
                        refreshArtCoverFragment();
                        break;
                    case SHUFFLE_OFF:
                        playNextSong();
                        break;
                }

            case REPEAT_CURR_SONG:

                playSong(allSongsList, currentPlayingSongPosition);
                refreshArtCoverFragment();
                break;

            case REPEAT_OFF:

                switch (shuffleMode) {

                    case SHUFFLE_ON:
                        playSong(currentPlayedListOfSongs, new Random().nextInt(currentPlayedListOfSongs.size()));
                        refreshArtCoverFragment();
                        break;

                    case SHUFFLE_OFF:

                        if (currentPlayingSongPosition == (currentPlayedListOfSongs.size() - 1)) {
                            stopPlayer();
                        } else {
                            playNextSong();
                        }
                        break;
                }
        }
    }

    private void firstInstalling() {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean previouslyStarted = prefs.getBoolean("previouslyStarted", false);

        if (!previouslyStarted) {
            SharedPreferences.Editor edit = prefs.edit();
            edit.putBoolean("previouslyStarted", Boolean.TRUE);
            edit.commit();

            dbInit();
        }
    }

    /**
     *
     * @param position the position of the song in the list
     * @param playlist_id the id of the playlist the last played song is from
     * @param tabId the id of the tab the last played song is from
     */
    private void saveLastPlayedSong(int position, int playlist_id, int tabId) {
        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS_LAST_PLAYED_SONG, 0);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putInt(LAST_SONG_ID, position);
        edit.putInt(PLAYLIST_ID, playlist_id);
        edit.putInt(TAB_ID, tabId);
        edit.commit();
    }

    private void initializeSongLists(){

        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS_LAST_PLAYED_SONG, 0);
        int tab_id = prefs.getInt(TAB_ID, 0);
        if(tab_id == ALL_SONGS_TAB){
            allSongsList = db.getAllSongs();
            currentPlayedListOfSongs = allSongsList;
            currentTab = ALL_SONGS_TAB;

        }else if(tab_id == PLAYLISTS_TAB){
            int playlist_id = prefs.getInt(PLAYLIST_ID, 0);
            playlistSongsList = db.getSongsFromPlaylist(playlist_id);
            currentPlayedListOfSongs = playlistSongsList;
            currentPlaylist = PLAYLISTS_TAB;

            //load the allSongsList
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            allSongsList = db.getAllSongs();
                            if(FragmentAllSongs.getSongAdapter() != null){
                                FragmentAllSongs.getSongAdapter().notifyDataSetChanged();
                            }
                        }
                    });
                }
            });
            thread.start();

        }else if(tab_id == ALBUMS_TAB){
            int album_id = prefs.getInt(ALBUM_ID, 0);
        }
    }

    private void dbInit() {
        // CHANGE FOR YOUR PHONE
        db.addFolderPath("/storage/extSdCard/Music");
        //db.addFolderPath("/storage/emulated/0/Music/");
        db.importFilesInDBByFolders(getContentResolver());
    }

    @Override
    public void onPrepared(MediaPlayer player) {
        player.start();
    }

    public void playSong(ArrayList<Song> songList, int position) {

        currentPlayingSongPosition = position;
        currentPlayedListOfSongs = songList;

        Song s = currentPlayedListOfSongs.get(position);

        setCurrentPlaylingSong(s);

        currSongName.setText(s.getTitle());
        currSongArtist.setText(s.getArtist());
        numberOfSong.setText((position + 1) + "/" + songList.size());

        try {
            mediaPlayer.reset();
            File f = new File(s.getPath());
            FileInputStream fileIS = new FileInputStream(f);
            fd = fileIS.getFD();
            mediaPlayer.setDataSource(fd);
            mediaPlayer.prepare();
            mediaPlayer.start();
            progressBar.setMax(100);
            progressBar.setProgress(0);

            updateProgressBar();

            //set pause image
            buttonPlay.setBackgroundResource(R.drawable.pause_button_default);

            //flip the view
            if (isBackSide) {
                flipCard();
            }

        } catch (IOException e) {

        }
    }

    public void playNextSong() {

        if (currentPlayingSongPosition < currentPlayedListOfSongs.size() - 1) {
            int pos = currentPlayingSongPosition;
            playSong(currentPlayedListOfSongs, pos + 1);
        } else {
            //play first song
            playSong(currentPlayedListOfSongs, 0);
            currentPlayingSongPosition = 0;
        }

        if (!isBackSide) {
            refreshArtCoverFragment();
        }
    }

    public void playPrevSong() {
        if (currentPlayingSongPosition > 0) {
            int pos = currentPlayingSongPosition;
            playSong(currentPlayedListOfSongs, pos - 1);
        } else {
            playSong(currentPlayedListOfSongs, currentPlayedListOfSongs.size() - 1);
            currentPlayingSongPosition = currentPlayedListOfSongs.size() - 1;
        }

        if (!isBackSide) {
            refreshArtCoverFragment();
        }
    }


    private void stopPlayer() {

        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(new FileInputStream(new File(getCurrentPlaylingSong().getPath())).getFD());
            mediaPlayer.prepare();

        } catch (IOException e) {

        }
        currentDurationLabel.setText(Utils.durationFormat(0));
        progressBar.setProgress(0);
        buttonPlay.setBackgroundResource(R.drawable.stop_button_default);

        //set again 'play' icon after a half second
        handler.postDelayed(new Runnable() {
            public void run() {
                buttonPlay.setBackgroundResource(R.drawable.play_button_default);
            }
        }, 500);

    }

    private void setIdsForViews() {

        progressBar = (SeekBar) findViewById(R.id.progress_bar);
        currSongName = (TextView) findViewById(R.id.currentSongName);
        currSongArtist = (TextView) findViewById(R.id.currentSongArtistName);
        numberOfSong = (TextView) findViewById(R.id.numberOfSongInList);
        buttonPlay = (Button) findViewById(R.id.playButton);
        buttonNext = (Button) findViewById(R.id.nextButton);
        buttonPrevious = (Button) findViewById(R.id.previousButton);
        buttonRepeat = (Button) findViewById(R.id.repeatButton);
        buttonShuffle = (Button) findViewById(R.id.shuffleButton);

        currentDurationLabel = (TextView) findViewById(R.id.current_song_duration);
        totalDurationLabel = (TextView) findViewById(R.id.total_song_duration);
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

    public void refreshArtCoverFragment() {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragments_container, new MainArtCoverFragment())
                .addToBackStack(null)
                .commit();
    }

    public void refreshMainListsFragment() {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragments_container, new MainListsFragment())
                .addToBackStack(null)
                .commit();
    }

    public void showToastWithMessage(String message) {

        View layout = getLayoutInflater().inflate(R.layout.toast_layout,
                (ViewGroup) findViewById(R.id.custom_toast_linear_layout));

        TextView textView = (TextView) layout.findViewById(R.id.toast_text_view);
        textView.setText(message);

        Toast toast = new Toast(this);
        toast.setView(layout);
        toast.setGravity(Gravity.CENTER, 0, 400);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }


}
