package com.augusta.dev.personalize.dbhelper;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by shanmugavel on 02-12-2016.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "PersonalDB";
    // Table Names
    public static final String TABLE_ROUSE_LIST = "RouseList";

    public static final String ROUSE_ID = "_id";
    public static final String ROUSE_NAME = "_name";
    public static final String ROUSE_TIME = "_time";
    public static final String ROUSE_SONG_LIST = "_song_list";

    // Todo table create statement
    private static final String CREATE_TABLE_ROUSE_LIST = "CREATE TABLE "
            + TABLE_ROUSE_LIST + "(" + ROUSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + ROUSE_NAME
            + " TEXT," + ROUSE_TIME + " TEXT," + ROUSE_SONG_LIST
            + " TEXT" + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_ROUSE_LIST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROUSE_LIST);
        onCreate(db);
    }
}
