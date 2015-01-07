package DataBases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
        protected static final String   UPDATE_STATEMENT_FOLDERS = " DELETE IF EXISTS TABLE " + TABLE_NAME_FOLDERS;


        //table files
        private static final String TABLE_NAME_FILES = "files";
        private static final String COLUMN_RAW_NAME = "raw_name_file";
        private static final String COLUMN_NAME_NO_EXT = "name_no_ext";
        private static final String COLUMN_FOLDER_ID = "folder_id";
        private static final String COLUMN_DURATION = "duration_of_song";
        private static final String COLUMN_ALBUM_ID = "album_id";
        private static final String COLUMN_ARTIST_ID = "artist_id";

        protected static final String CREATE_STATEMENT_FILES = " CREATE TABLE " + TABLE_NAME_FILES + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_RAW_NAME + " TEXT, " +
                COLUMN_NAME_NO_EXT + " TEXT, " +
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
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
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
        private static final String COLUMN_Id = "_id";
        private static final String COLUMN_NAME = "artist_name";

        protected static final String CREATE_STATEMENT_ARTISTS = " CREATE TABLE " + TABLE_NAME_ARTISTS + " (" +
                COLUMN_Id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT " + " ) ";

        protected static final String UPDATE_STATEMENT_ARTISTS = " DELETE IF EXISTS TABLE " + TABLE_NAME_ARTISTS;


    public final String[] create_tables = {
            CREATE_STATEMENT_FOLDERS,
            CREATE_STATEMENT_FILES,
            CREATE_STATEMENT_ALBUMS,
            CREATE_STATEMENT_PLAYLISTS,
            CREATE_STATEMENT_PLAYLISTS_ENTRIES,
            CREATE_STATEMENT_ARTISTS};

    public final String[] update_tables = {
            UPDATE_STATEMENT_FOLDERS,
            UPDATE_STATEMENT_FILES,
            UPDATE_STATEMENT_ALBUMS,
            UPDATE_STATEMENT_PLAYLISTS,
            UPDATE_STATEMENT_PLAYLISTS_ENTRIES,
            UPDATE_STATEMENT_ARTISTS};


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

}
