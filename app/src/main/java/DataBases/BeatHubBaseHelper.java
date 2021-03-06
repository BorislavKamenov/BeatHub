package DataBases;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.MediaStore;

import ObjectClasses.Album;
import ObjectClasses.Playlist;
import ObjectClasses.Song;

import java.util.ArrayList;

public class BeatHubBaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DB_NAME = "BeatHubDataBase";

    public BeatHubBaseHelper(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
    }

    //common column
    private static final String COLUMN_ID = "_id";

    //table folders
    private static final String TABLE_NAME_FOLDERS = "folders";
    private static final String COLUMN_FOLDER_NAME = "folder_name";
    private static final String COLUMN_PATH = "path";

    protected static final String CREATE_STATEMENT_FOLDERS = " CREATE TABLE " + TABLE_NAME_FOLDERS + " ( " +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_FOLDER_NAME + " TEXT, " +
            COLUMN_PATH + " TEXT " + " ) ";
    protected static final String UPDATE_STATEMENT_FOLDERS = " DELETE IF EXISTS TABLE " + TABLE_NAME_FOLDERS;

    //table files
    private static final String TABLE_NAME_FILES = "files";
    private static final String COLUMN_RAW_NAME = "raw_name_file";
    private static final String COLUMN_NAME_NO_EXT = "name_no_ext";
    private static final String COLUMN_SONG_TITLE = "song_title";
    private static final String COLUMN_FOLDER_ID = "folder_id";
    private static final String COLUMN_DURATION = "duration_of_song";
    private static final String COLUMN_ALBUM_ID = "album_id";
    private static final String COLUMN_ARTIST_ID = "artist_id";

    protected static final String CREATE_STATEMENT_FILES = " CREATE TABLE " + TABLE_NAME_FILES + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_RAW_NAME + " TEXT, " +
            COLUMN_NAME_NO_EXT + " TEXT, " +
            COLUMN_SONG_TITLE + " TEXT, " +
            COLUMN_FOLDER_ID + " TEXT, " +
            COLUMN_DURATION + " TEXT, " +
            COLUMN_ALBUM_ID + " TEXT, " +
            COLUMN_ARTIST_ID + " TEXT " + " ) ";
    protected static final String UPDATE_STATEMENT_FILES = " DELETE IF EXISTS TABLE " + TABLE_NAME_FILES;


    //table albums
    private static final String TABLE_NAME_ALBUMS = "albums";
    private static final String COLUMN_ALBUM_NAME = "album_name";
    private static final String COLUMN_ARTIST_OF_ALBUM = "album_artist_name";

    protected static final String CREATE_STATEMENT_ALBUMS = " CREATE TABLE " + TABLE_NAME_ALBUMS + " (" +
            COLUMN_ID + " INTEGER , " +
            COLUMN_ALBUM_NAME + " TEXT, " +
            COLUMN_ARTIST_OF_ALBUM + " TEXT " + " ) ";
    protected static final String UPDATE_STATEMENT_ALBUMS = " DELETE IF EXISTS TABLE " + TABLE_NAME_ALBUMS;


    //table playlists
    private static final String TABLE_NAME_PLAYLISTS = "playlists";
    private static final String COLUMN_PLAYLIST_NAME = "list_name";

    private static final String CREATE_STATEMENT_PLAYLISTS = " CREATE TABLE " + TABLE_NAME_PLAYLISTS + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_PLAYLIST_NAME + " TEXT " + " ) ";
    protected static final String UPDATE_STATEMENT_PLAYLISTS = " DELETE IF EXISTS TABLE " +
            TABLE_NAME_PLAYLISTS;


    //table playlist entries
    private static final String TABLE_NAME_PLAYLISTS_ENTRIES = "playlists_entries";
    private static final String COLUMN_FILE_ID = "file_id";
    private static final String COLUMN_PLAYLIST_ID = "playlist_id";
    protected static final String CREATE_STATEMENT_PLAYLISTS_ENTRIES = " CREATE TABLE " + TABLE_NAME_PLAYLISTS_ENTRIES + " ( " +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_FILE_ID + " TEXT, " +
            COLUMN_PLAYLIST_ID + " TEXT " + " ) ";

    protected static final String UPDATE_STATEMENT_PLAYLISTS_ENTRIES = " DELETE IF EXISTS TABLE " +
            TABLE_NAME_PLAYLISTS_ENTRIES;


    //table artists
    private static final String TABLE_NAME_ARTISTS = "artists";
    private static final String COLUMN_ARTIST_NAME = "artist_name";

    protected static final String CREATE_STATEMENT_ARTISTS = " CREATE TABLE " + TABLE_NAME_ARTISTS + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_ARTIST_NAME + " TEXT " + " ) ";

    protected static final String UPDATE_STATEMENT_ARTISTS = " DELETE IF EXISTS TABLE " + TABLE_NAME_ARTISTS;


    public final String[] create_tables = {
            CREATE_STATEMENT_FOLDERS,
            CREATE_STATEMENT_FILES,
            CREATE_STATEMENT_ALBUMS,
            CREATE_STATEMENT_PLAYLISTS,
            CREATE_STATEMENT_PLAYLISTS_ENTRIES,
            CREATE_STATEMENT_ARTISTS,
    };

    public final String[] update_tables = {
            UPDATE_STATEMENT_FOLDERS,
            UPDATE_STATEMENT_FILES,
            UPDATE_STATEMENT_ALBUMS,
            UPDATE_STATEMENT_PLAYLISTS,
            UPDATE_STATEMENT_PLAYLISTS_ENTRIES,
            UPDATE_STATEMENT_ARTISTS,
    };


    @Override
    public void onCreate(SQLiteDatabase db) {
        for (String CREATE_STATEMENT : create_tables) {
            db.execSQL(CREATE_STATEMENT);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (String DELETE_STATEMENT : update_tables) {
            db.execSQL(" DROP TABLE IF EXISTS " + DELETE_STATEMENT);
            this.onCreate(db);
        }

    }


    public void addFolderPath(String path) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues content = new ContentValues();
        content.put(COLUMN_FOLDER_NAME, getFolderNameFromPath(path));
        content.put(COLUMN_PATH, path);

        db.insert(TABLE_NAME_FOLDERS, null, content);
        db.close();
    }

    private String getFolderNameFromPath(String path) {

        for (int i = path.length() - 1; i >= 0; i--) {
            if (path.charAt(i) == '/') {
                return path.substring(i + 1, path.length());
            }
        }
        return "No Folder";
    }

    public void importFilesInDBByFolders(ContentResolver resolver) {

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor folders = db.query(TABLE_NAME_FOLDERS, null, null, null, null, null, null);

        if (folders.moveToFirst()) {
            do {
                String path = folders.getString(folders.getColumnIndex(COLUMN_PATH));
                long folder_id = folders.getLong(folders.getColumnIndex(COLUMN_ID));

                insertAllFilesFromSingleFolderInDB(resolver, path, folder_id);
            } while (folders.moveToNext());
        }

        folders.close();
        db.close();
    }

    private void insertAllFilesFromSingleFolderInDB(ContentResolver resolver, String path, long folder_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String key = "%" + path + "%";
        Cursor songsInFolder = resolver.query(musicUri, null, MediaStore.Audio.Media.DATA + " like ? ", new String[]{key}, null);

        if (songsInFolder.moveToFirst()) {
            do {

                ContentValues values = new ContentValues();

                int artist_id = songsInFolder.getInt(songsInFolder.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID));
                int album_id = songsInFolder.getInt(songsInFolder.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                String albumName = songsInFolder.getString(songsInFolder.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                String albumArtistName = songsInFolder.getString(songsInFolder.getColumnIndex(MediaStore.Audio.Media.ARTIST));

                values.put(COLUMN_RAW_NAME, songsInFolder.getString(songsInFolder.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)));
                values.put(COLUMN_NAME_NO_EXT, songsInFolder.getString(songsInFolder.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)));
                values.put(COLUMN_SONG_TITLE, songsInFolder.getString(songsInFolder.getColumnIndex(MediaStore.Audio.Media.TITLE)));
                values.put(COLUMN_FOLDER_ID, folder_id);
                values.put(COLUMN_DURATION, songsInFolder.getInt(songsInFolder.getColumnIndex(MediaStore.Audio.Media.DURATION)));
                values.put(COLUMN_ALBUM_ID, album_id);
                values.put(COLUMN_ARTIST_ID, songsInFolder.getInt(songsInFolder.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID)));

                db.insert(TABLE_NAME_FILES, null, values);

                addArtistBySongs(artist_id, albumArtistName);
                addAlbumBySongs(album_id, albumName, albumArtistName);

            } while (songsInFolder.moveToNext());
        }

        db.close();
    }

    /**
     * add the album for every song
     */
    private void addAlbumBySongs(int id, String albumName, String albumArtistName) {

        SQLiteDatabase db = getWritableDatabase();

        boolean isContainAlbum = false;
        Cursor cursor = db.query(TABLE_NAME_ALBUMS, new String[]{COLUMN_ID}, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                if (cursor.getInt(cursor.getColumnIndex(COLUMN_ID)) == id) {
                    isContainAlbum = true;
                    break;
                }
            } while (cursor.moveToNext());
        }

        //if the album is already exist, doesn't add a duplicate
        if (!isContainAlbum) {

            ContentValues values = new ContentValues();
            values.put(COLUMN_ID, id);
            values.put(COLUMN_ALBUM_NAME, albumName);
            values.put(COLUMN_ARTIST_OF_ALBUM, albumArtistName);

            db.insert(TABLE_NAME_ALBUMS, null, values);
        }
        cursor.close();
    }

    private void addArtistBySongs(int id, String artistName) {

        SQLiteDatabase db = getWritableDatabase();

        boolean isArtistExist = false;
        Cursor cursor = db.query(TABLE_NAME_ARTISTS, new String[]{COLUMN_ID}, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                if (cursor.getInt(cursor.getColumnIndex(COLUMN_ID)) == id) {
                    isArtistExist = true;
                    break;
                }
            } while (cursor.moveToNext());
        }

        //if the artist is already exist, doesn't add a duplicate
        if (!isArtistExist) {

            ContentValues values = new ContentValues();
            values.put(COLUMN_ID, id);
            values.put(COLUMN_ARTIST_NAME, artistName);

            db.insert(TABLE_NAME_ARTISTS, null, values);
        }
        cursor.close();
    }

    private Song getSongParameters(SQLiteDatabase db, int positionOfCursor) {

        Cursor cursor = db.query(TABLE_NAME_FILES,
                null,
                " _id = ? ",
                new String[]{String.valueOf(positionOfCursor)},
                null, null, null);

        Song s = null;

        if (cursor.moveToFirst()) {

            do {

                String folder_id = String.valueOf(cursor.getLong(cursor.getColumnIndex(COLUMN_FOLDER_ID)));
                Cursor foldersCursor = db.query(TABLE_NAME_FOLDERS, null, " _id = ?", new String[]{folder_id}, null, null, null);
                foldersCursor.moveToFirst();
                String folderPath = foldersCursor.getString(foldersCursor.getColumnIndex(COLUMN_PATH));
                foldersCursor.close();

                //get artist name
                String artist_id = String.valueOf(cursor.getLong(cursor.getColumnIndex(COLUMN_ARTIST_ID)));
                Cursor artistsCursor = db.query(TABLE_NAME_ARTISTS, null, " _id = ?", new String[]{artist_id}, null, null, null);
                artistsCursor.moveToFirst();
                String artistName = artistsCursor.getString(artistsCursor.getColumnIndex(COLUMN_ARTIST_NAME));
                artistsCursor.close();

                //get title of the song
                String title = cursor.getString(cursor.getColumnIndex(COLUMN_SONG_TITLE));

                //get song duration
                long duration = cursor.getLong(cursor.getColumnIndex(COLUMN_DURATION));

                //get song id
                long id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID));

                //get absolute path of the song
                String songPath = folderPath + "/" + cursor.getString(cursor.getColumnIndex(COLUMN_RAW_NAME));

                s = new Song(id, songPath, title, artistName, duration);

                if (s != null) {
                    break;
                }

            } while (cursor.moveToNext());
        }

        //create new Song object
        return s;
    }

    public ArrayList<Song> getAllSongs() {

        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Song> listOfSongs = new ArrayList<>();

        Cursor cursor = db.query(TABLE_NAME_FILES, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int position = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                Song song = getSongParameters(db, position);

                listOfSongs.add(song);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listOfSongs;
    }

    public void addPlaylist(String name) {

        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = db.query(TABLE_NAME_PLAYLISTS, new String[]{COLUMN_PLAYLIST_NAME}, null, null, null, null, null);

        boolean ifExist = false;

        if (cursor.moveToFirst()) {

            do {
                if (cursor.getString(cursor.getColumnIndex(COLUMN_PLAYLIST_NAME)).equals(name)) {
                    ifExist = true;
                    break;
                } else {
                    cursor.moveToNext();
                }

            } while (!cursor.isAfterLast());
        }
        if (!ifExist) {
            ContentValues content = new ContentValues();
            content.put(COLUMN_PLAYLIST_NAME, name);

            db.insert(TABLE_NAME_PLAYLISTS, null, content);
        }
        db.close();
    }

    public void deletePlaylist(String name) {
        SQLiteDatabase db = getWritableDatabase();

        db.delete(TABLE_NAME_PLAYLISTS, COLUMN_PLAYLIST_NAME + "=" + name, new String[]{name});

    }

    public ArrayList<Playlist> getAllPlaylists() {

        ArrayList<Playlist> playlists = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME_PLAYLISTS, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {

                int playlistId = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                String playlistName = cursor.getString(cursor.getColumnIndex(COLUMN_PLAYLIST_NAME));
                Playlist playlist = new Playlist(playlistName, playlistId);
                playlists.add(playlist);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return playlists;
    }

    public ArrayList<Song> getSongsFromPlaylist(int playlistId) {

        ArrayList<Song> songsFromPlayList = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        Cursor playlistCursor = db.query(
                TABLE_NAME_PLAYLISTS_ENTRIES,
                new String[]{COLUMN_PLAYLIST_ID, COLUMN_FILE_ID},
                " playlist_id = ? ",
                new String[]{String.valueOf(playlistId)},
                null, null, null);

        if (playlistCursor.moveToFirst()) {

            do {
                int songId = playlistCursor.getInt(playlistCursor.getColumnIndex(COLUMN_FILE_ID));
                songsFromPlayList.add(getSongParameters(db, songId));

            } while (playlistCursor.moveToNext());
        }

        playlistCursor.close();
        db.close();

        return songsFromPlayList;
    }

    public void addSongToPlaylist(int song_id, int playlist_id) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_FILE_ID, song_id);
        contentValues.put(COLUMN_PLAYLIST_ID, playlist_id);

        db.insert(TABLE_NAME_PLAYLISTS_ENTRIES, null, contentValues);
        db.close();
    }

    public ArrayList<Album> getAllAlbums() {

        SQLiteDatabase db = getReadableDatabase();

        ArrayList<Album> albums = new ArrayList<>();

        Cursor albumsCursor = db.query(TABLE_NAME_ALBUMS, null, null, null, null, null, null);

        if (albumsCursor.moveToFirst()) {

            do {
                int album_id = albumsCursor.getInt(albumsCursor.getColumnIndex(COLUMN_ID));
                String albumName = albumsCursor.getString(albumsCursor.getColumnIndex(COLUMN_ALBUM_NAME));
                String artistName = albumsCursor.getString(albumsCursor.getColumnIndex(COLUMN_ARTIST_OF_ALBUM));

                albums.add(new Album(album_id, albumName, artistName));

            } while (albumsCursor.moveToNext());
        }
        albumsCursor.close();

        return albums;
    }

    public ArrayList<Song> getSongsFromAlbum(int album_id) {

        SQLiteDatabase db = getWritableDatabase();

        ArrayList<Song> songs = new ArrayList<>();

        Cursor allSongsCursor = db.query(TABLE_NAME_FILES,
                null,
                " album_id = ?",
                new String[]{String.valueOf(album_id)},
                null, null, null);

        if (allSongsCursor.moveToFirst()) {
            do {
                int position = allSongsCursor.getInt(allSongsCursor.getColumnIndex(COLUMN_ID));
                Song song = getSongParameters(db, position);
                songs.add(song);

            } while (allSongsCursor.moveToNext());
        }
        allSongsCursor.close();

        return songs;
    }

    public String getPathOfOneSongInAlbum(int album_id) {

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME_FILES,
                null,
                " album_id = ?",
                new String[]{String.valueOf(album_id)},
                null, null, null);

        cursor.moveToFirst();

        int cursorPos = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
        Song s = getSongParameters(db, cursorPos);

        cursor.close();

        return s.getPath();
    }

}
