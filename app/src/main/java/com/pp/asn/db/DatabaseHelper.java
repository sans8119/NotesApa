package com.pp.asn.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "notes_db";
    public static final String NAME_NOTES_TABLE = "notes_table";
    private static final int DATABASE_VERSION = 1;
    private static final String CREATE_TABLE_NOTES = "CREATE TABLE " + NAME_NOTES_TABLE + "(" +
            NotesTable.TITLE + " TEXT," + NotesTable.TEXT + " TEXT," + NotesTable.STATUS_HEARTED + " INTEGER, " +
            NotesTable.STATUS_STARRED + " INTEGER, " + NotesTable.STATUS_POEM + " INTEGER, " +
            NotesTable.STATUS_STORY + " INTEGER," + NotesTable.SAVE_TIME + " INTEGER " +
            ");";
    private Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context.getApplicationContext();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DB", "----onCreate--->" + CREATE_TABLE_NOTES);
        db.execSQL(CREATE_TABLE_NOTES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + NAME_NOTES_TABLE);
        onCreate(db);
    }

    public static class NotesTable {
        public static final String TITLE = "notes_title";
        public static final String TEXT = "notes_text";
        public static final String STATUS_HEARTED = "status_hearted";
        public static final String STATUS_STARRED = "status_starred";
        public static final String STATUS_POEM = "status_poem";
        public static final String STATUS_STORY = "status_story";
        public static final String SAVE_TIME = "save_time";

        public static final int INDEX_TITLE = 0;
        public static final int INDEX_TEXT = 1;
        public static final int INDEX_STATUS_HEARTED = 2;
        public static final int INDEX_STATUS_STARRED = 3;
        public static final int INDEX_STATUS_POEM = 4;
        public static final int INDEX_STATUS_STORY = 5;
        public static final int INDEX_SAVE_TIME = 6;
    }

}