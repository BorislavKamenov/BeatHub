package DataBases;

/**
 * Created by Rosen on 6.1.2015 г..
 */
public class DataBaseTables {


    private class Folders {
        private static final String TABLE_NAME = "folders";

        private static final String COLUMN_Id = "_id";
        private static final String COLUMN_FOLDER_NAME = "folder_name";
        private static final String COLUMN_PATH = "path";

        protected static final String CREATE_STATEMENT_Folders = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_Id + " INTEGER PRIMARY KEY," + COLUMN_FOLDER_NAME + " TEXT, " +
                COLUMN_PATH + " TEXT " + " ) ";
        protected static final String DELETE_STATEMENT_Folders = "DELETE IF EXISTS TABLE " + TABLE_NAME;
    }


    private class Files {
        private static final String TABLE_NAME = "files";

        private static final String COLUMN_Id = "_id";
        private static final String COLUMN_RAW_NAME = "raw_name_file";
        private static final String COLUMN_NAME_NO_EXT = "name_no_ext";
        private static final String COLUMN_FOLDER_ID = "folder_id";
        private static final String COLUMN_DURATION = "duration_of_song";
        private static final String COLUMN_ALBUM_ID = "album_id";
        private static final String COLUMN_ARTIST_ID = "artist_id";

        protected static final String CREATE_STATEMENT_Files = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_Id + " INTEGER PRIMARY KEY," + COLUMN_RAW_NAME + " TEXT, " +
                COLUMN_NAME_NO_EXT + " TEXT, " + COLUMN_FOLDER_ID + " TEXT, " +
                COLUMN_DURATION + " TEXT, " + COLUMN_ALBUM_ID + " TEXT, " +
                COLUMN_ARTIST_ID + " TEXT " + " ) ";

        protected static final String DELETE_STATEMENT_Files = "DELETE IF EXISTS TABLE " + TABLE_NAME;

    }


    private class Albums {
        private static final String TABLE_NAME = "albums";

        private static final String COLUMN_Id = "_id";
        private static final String COLUMN_ALBUM_NAME = "album_name";
        private static final String COLUMN_ARTIST_OF_ALBUM = "album_artist_name";

        protected static final String CREATE_STATEMENT_Albums = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_Id + " INTEGER PRIMARY KEY," + COLUMN_ALBUM_NAME + " TEXT, " +
                COLUMN_ARTIST_OF_ALBUM + " TEXT " + " ) ";
        protected static final String DELETE_STATEMENT_Albums = "DELETE IF EXISTS TABLE " + TABLE_NAME;

    }


    private class Playlists {
        private static final String TABLE_NAME = "playlists";

        private static final String COLUMN_Id = "_id";
        private static final String COLUMN_PLAYLIST_NAME = "list_name";

        private static final String CREATE_STATEMENT_Playlists = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_Id + " INTEGER PRIMARY KEY," + COLUMN_PLAYLIST_NAME
                + " TEXT " + " ) ";
        protected static final String DELETE_STATEMENT_PlayList = "DELETE IF EXISTS TABLE " +
                TABLE_NAME;
    }


    private class PlaylistsEntries {
        private static final String TABLE_NAME = "playlists_entries";

        private static final String COLUMN_Id = "_id";
        private static final String COLUMN_FILE_ID = "file_id";
        private static final String COLUMN_PLAYLIST_ID = "playlist_id";

        protected static final String CREATE_STATEMENT_PlaylistEntries = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_Id + " INTEGER PRIMARY KEY," + COLUMN_FILE_ID +
                " TEXT, " + COLUMN_PLAYLIST_ID + " TEXT " + " ) ";

        protected static final String DELETE_STATEMENT_PlayListEntries = "DELETE IF EXISTS TABLE " +
                TABLE_NAME;
    }


    private class Artists {
        private static final String TABLE_NAME = "artists";

        private static final String COLUMN_Id = "_id";
        private static final String COLUMN_NAME = "artist_name";

        protected static final String CREATE_STATEMENT_Artists = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_Id + " INTEGER PRIMARY KEY," + COLUMN_NAME + " TEXT " + " ) ";

        protected static final String DELETE_STATEMENT_Artists = "DELETE IF EXISTS TABLE " + TABLE_NAME;
    }


    public final String[] CREATE_TABLES = {Folders.CREATE_STATEMENT_Folders, Files.CREATE_STATEMENT_Files,
            Albums.CREATE_STATEMENT_Albums, Playlists.CREATE_STATEMENT_Playlists,
            PlaylistsEntries.CREATE_STATEMENT_PlaylistEntries, Artists.CREATE_STATEMENT_Artists};

    public final String[] DELETE_TABLES = {Folders.DELETE_STATEMENT_Folders,
            Files.DELETE_STATEMENT_Files, Albums.DELETE_STATEMENT_Albums,
            Playlists.DELETE_STATEMENT_PlayList, PlaylistsEntries.DELETE_STATEMENT_PlayListEntries,
            Artists.DELETE_STATEMENT_Artists};
}

