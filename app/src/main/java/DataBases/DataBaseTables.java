package DataBases;

/**
 * Created by Rosen on 6.1.2015 г..
 */
public class DataBaseTables {

    protected class Folders {
        private static final String TABLE_NAME = "folders";

        private static final String COLUMN_Id = "_id";
        private static final String COLUMN_FOLDER_NAME = "folder_name";
        private static final String COLUMN_PATH = "path";

        protected final String CREATE_STATEMENT_Folders = "CREATE TABLE " + Folders.TABLE_NAME + " (" +
                Folders.COLUMN_Id + " INTEGER PRIMARY KEY," + Folders.COLUMN_FOLDER_NAME + " TEXT, " +
                Folders.COLUMN_PATH + " TEXT " + " ) ";
    }


    protected class Files {
        private static final String TABLE_NAME = "files";

        private static final String COLUMN_Id = "_id";
        private static final String COLUMN_RAW_NAME = "raw_name_file";
        private static final String COLUMN_NAME_NO_EXT = "name_no_ext";
        private static final String COLUMN_FOLDER_ID = "folder_id";
        private static final String COLUMN_DURATION = "duration_of_song";
        private static final String COLUMN_ALBUM_ID = "album_id";
        private static final String COLUMN_ARTIST_ID = "artist_id";

        protected final String CREATE_STATEMENT_Files = "CREATE TABLE " + Files.TABLE_NAME + " (" +
                Files.COLUMN_Id + " INTEGER PRIMARY KEY," + Files.COLUMN_RAW_NAME + " TEXT, " +
                Files.COLUMN_NAME_NO_EXT + " TEXT, " + Files.COLUMN_FOLDER_ID + " TEXT, " +
                Files.COLUMN_DURATION + " TEXT, " + Files.COLUMN_ALBUM_ID + " TEXT, " +
                Files.COLUMN_ARTIST_ID + " TEXT " + " ) ";

    }


    protected class Albums {
        private static final String TABLE_NAME = "albums";

        private static final String COLUMN_Id = "_id";
        private static final String COLUMN_ALBUM_NAME = "album_name";
        private static final String COLUMN_ARTIST_OF_ALBUM = "album_artist_name";

        protected final String CREATE_STATEMENT_Albums = "CREATE TABLE " + Albums.TABLE_NAME + " (" +
                Albums.COLUMN_Id + " INTEGER PRIMARY KEY," + Albums.COLUMN_ALBUM_NAME + " TEXT, " +
                Albums.COLUMN_ARTIST_OF_ALBUM + " TEXT " + " ) ";
    }


    protected class Playlists {
        private static final String TABLE_NAME = "playlists";

        private static final String COLUMN_Id = "_id";
        private static final String COLUMN_PLAYLIST_NAME = "list_name";

        protected final String CREATE_STATEMENT_Playlists = "CREATE TABLE " + Playlists.TABLE_NAME +
                " (" + Playlists.COLUMN_Id + " INTEGER PRIMARY KEY," + Playlists.COLUMN_PLAYLIST_NAME
                + " TEXT " + " ) ";
    }


    protected class PlaylistsEntries {
        private static final String TABLE_NAME = "playlists_entries";

        private static final String COLUMN_Id = "_id";
        private static final String COLUMN_FILE_ID = "file_id";
        private static final String COLUMN_PLAYLIST_ID = "playlist_id";

        protected final String CREATE_STATEMENT_PlaylistEntries = "CREATE TABLE " + PlaylistsEntries.TABLE_NAME +
                " (" + PlaylistsEntries.COLUMN_Id + " INTEGER PRIMARY KEY," + PlaylistsEntries.COLUMN_FILE_ID +
                " TEXT, " + PlaylistsEntries.COLUMN_PLAYLIST_ID + " TEXT " + " ) ";
    }
}
