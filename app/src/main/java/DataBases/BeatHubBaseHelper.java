package DataBases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Rosen on 6.1.2015 г..
 */
public class BeatHubBaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DB_NAME = "BeatHubBase";
    private static DataBaseTables tables = new DataBaseTables();


    public BeatHubBaseHelper(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (String CREATE_STATEMENT : tables.CREATE_TABLES) {
            db.execSQL(CREATE_STATEMENT);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (String DELETE_STATEMENT : tables.DELETE_TABLES) {
            db.execSQL(DELETE_STATEMENT);
            onCreate(db);
        }
    }
}
