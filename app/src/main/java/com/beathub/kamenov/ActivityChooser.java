package com.beathub.kamenov;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import AdaptersAndAbstractClasses.DirectoryChooserDialog;
import ObjectClasses.Song;
import DataBases.BeatHubBaseHelper;

/**
 * Created by rosentsankov on 15-2-4.
 */
public class ActivityChooser extends Activity {

    private final static String PREVIOUSLY_STARTED = "previouslyStarted";
    private final static String DIRECTORY_IS_ADDED = "Folder is successfully added";
    private Button buttonGetStarted = null;
    private Button buttonSkip = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ArrayList<Song> songs = new BeatHubBaseHelper(ActivityChooser.this).getAllSongs();

        setContentView(R.layout.begin_screen);

        buttonGetStarted = (Button) findViewById(R.id.getStartedButton);

        buttonSkip = (Button) findViewById(R.id.skipButton);

        if (firstInstalling()) {
            buttonSkip.setVisibility(View.INVISIBLE);
        } else {
            buttonSkip.setVisibility(View.VISIBLE);
        }

        buttonSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivityIntent();
            }
        });

        buttonGetStarted.setOnClickListener(new View.OnClickListener() {
            private String m_chosenDir = "";
            private boolean m_newFolderEnabled = true;

            @Override
            public void onClick(View v) {

                DirectoryChooserDialog directoryChooserDialog =
                        new DirectoryChooserDialog(ActivityChooser.this,
                                new DirectoryChooserDialog.ChosenDirectoryListener() {
                                    @Override
                                    public void onChosenDir(String chosenDir) {

                                        m_chosenDir = chosenDir;
                                        Toast.makeText(
                                                ActivityChooser.this, "Chosen directory: " +
                                                        chosenDir, Toast.LENGTH_LONG).show();
                                        dbInit(chosenDir);
                                        Toast.makeText(ActivityChooser.this, DIRECTORY_IS_ADDED, Toast.LENGTH_SHORT).show();
                                        buttonSkip.setVisibility(View.VISIBLE);
                                    }
                                });
                // Toggle new folder button enabling0
                directoryChooserDialog.setNewFolderEnabled(m_newFolderEnabled);
                // Load directory chooser dialog for initial 'm_chosenDir' directory.
                // The registered callback will be called upon final directory selection.
                directoryChooserDialog.chooseDirectory(m_chosenDir);
                m_newFolderEnabled = !m_newFolderEnabled;
            }
        });


    }

    private void mainActivityIntent() {
        Intent mainActivityIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(mainActivityIntent);

    }

    private boolean firstInstalling() {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        boolean previouslyStarted = prefs.getBoolean(PREVIOUSLY_STARTED, false);

        if (!previouslyStarted) {
            SharedPreferences.Editor edit = prefs.edit();
            edit.putBoolean(PREVIOUSLY_STARTED, Boolean.TRUE);
            edit.commit();
            return true;
            // dbInit("/storage/emulated/0/Music");
        }
        return false;
    }

    private void dbInit(String musicDirectory) {
        BeatHubBaseHelper db = new BeatHubBaseHelper(getBaseContext());
        db.addFolderPath(musicDirectory);
        db.importFilesInDBByFolders(getContentResolver());
    }

}
