package com.pp.asn.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.pp.asn.model.NoteData;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class DbUtils {
    private String TAG = "DBUtils";

    public void storeNotesInDb(final NoteData noteData, final Context context, final String titleBeforeEditing) {
        new Thread() {
            @Override
            public void run() {
                Log.d(TAG, "----storeNotesInDb----->" + noteData.getStarred() + ", " + noteData.toString());
                ContentValues contentValues = new ContentValues();
                contentValues.put(DatabaseHelper.NotesTable.TITLE, noteData.getTitle());
                contentValues.put(DatabaseHelper.NotesTable.TEXT, noteData.getText());
                contentValues.put(DatabaseHelper.NotesTable.STATUS_STARRED, noteData.getStarred());
                contentValues.put(DatabaseHelper.NotesTable.STATUS_HEARTED, noteData.getHearted());
                contentValues.put(DatabaseHelper.NotesTable.STATUS_POEM, noteData.getPoem());
                contentValues.put(DatabaseHelper.NotesTable.STATUS_STORY, noteData.getStory());
                contentValues.put(DatabaseHelper.NotesTable.SAVE_TIME, noteData.getTime());
                SQLiteDatabase db = null;
                try {
                    boolean state = false;
                    DatabaseHelper databaseHelperInstance = new DatabaseHelper(context.getApplicationContext());
                    db = databaseHelperInstance.getWritableDatabase();
                    if (noteData.getId() != -1) {
                        state = true;
                    } else {
                        String query = new StringBuilder(50).append("Select * from ").append(DatabaseHelper.NAME_NOTES_TABLE).append(" where ")
                                .append(DatabaseHelper.NotesTable.TITLE).append(" = '").append(noteData.getTitle()).append("'").toString();
                        state = db.rawQuery(query, null).moveToFirst();
                    }
                    if (state) {
                        String title = "";
                        if (noteData.getId() != -1) {
                           /* String query = new StringBuilder(50).append("Select " + DatabaseHelper.NotesTable.TITLE + " from ").append(DatabaseHelper.NAME_NOTES_TABLE).append(" where ")
                                    .append("rowid").append(" = ").append(noteData.getId() + 1)*//*.append("'")*//*.toString();

                            Cursor cursor = db.rawQuery(query, null);
                            cursor.moveToFirst();
                            Log.d(TAG, cursor.getCount() + "---query:" + query);
                            title = cursor.getString(DatabaseHelper.NotesTable.INDEX_TITLE);*/
                            title = titleBeforeEditing;
                        } else
                            title = noteData.getTitle();
                        Log.d(TAG, "Storing notes in db: " + db.update(DatabaseHelper.NAME_NOTES_TABLE, contentValues, DatabaseHelper.NotesTable.TITLE + "='" + title + "'", null));
                    } else {
                        Log.d(TAG, "Inserting notes in db: " + db.insert(DatabaseHelper.NAME_NOTES_TABLE, null, contentValues));
                    }
                } catch (SQLiteException sql) {
                    sql.printStackTrace();
                } finally {
                    if (db != null) db.close();
                }
            }
        }.start();
    }

    public void getAllNotesFromDb(final DbCallBack dbCallBack, final Context context) {
        new Thread() {
            @Override
            public void run() {
                Log.d(TAG, "getAllNotesFromDb~~~>run");
                SQLiteDatabase db = null;
                Cursor cursor = null;
                try {
                    DatabaseHelper databaseHelperInstance = new DatabaseHelper(context.getApplicationContext());
                    db = databaseHelperInstance.getReadableDatabase();
                    cursor = db.rawQuery(new StringBuilder(50).append("select *,rowid from ").append(DatabaseHelper.NAME_NOTES_TABLE).toString(), null);
                    Log.d(TAG, "-------getAllNotesFromDb:0-->" + cursor.getCount());
                    LinkedHashMap<String, NoteData> noteDataHashMap = new LinkedHashMap<String, NoteData>();
                    if (cursor.moveToFirst()) {
                        do {
                            NoteData noteData = new NoteData();
                            noteData.setTitle(cursor.getString(DatabaseHelper.NotesTable.INDEX_TITLE));
                            noteData.setText(cursor.getString(DatabaseHelper.NotesTable.INDEX_TEXT));
                            noteData.setHearted(cursor.getInt(DatabaseHelper.NotesTable.INDEX_STATUS_HEARTED));
                            noteData.setPoem(cursor.getInt(DatabaseHelper.NotesTable.INDEX_STATUS_POEM));
                            noteData.setStarred(cursor.getInt(DatabaseHelper.NotesTable.INDEX_STATUS_STARRED));
                            noteData.setStory(cursor.getInt(DatabaseHelper.NotesTable.INDEX_STATUS_STORY));
                            noteData.setTime(cursor.getLong(DatabaseHelper.NotesTable.INDEX_SAVE_TIME));
                            noteData.setId(cursor.getInt(7));
                            noteDataHashMap.put(noteData.getTitle(), noteData);
                            Log.d(TAG + "1", "-------getAllNotesFromDb:1-->" + noteData);
                        } while (cursor.moveToNext());
                        dbCallBack.sendData(noteDataHashMap);
                        // Log.d(TAG, "-------getAllNotesFromDb:1-->" + cursor.getString(DatabaseHelper.NotesTable.INDEX_TITLE));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (db != null)
                        db.close();
                    if (cursor != null)
                        cursor.close();
                }
            }
        }.start();
    }

    public void getNoteFromDb(final DbCallBack dbCallBack, final Context context, final String title) {
        new Thread() {
            @Override
            public void run() {
                Log.d(TAG, "getNoteFromDb~~~>run");
                SQLiteDatabase db = null;
                Cursor cursor = null;
                try {
                    DatabaseHelper databaseHelperInstance = new DatabaseHelper(context.getApplicationContext());
                    db = databaseHelperInstance.getReadableDatabase();
                    cursor = db.rawQuery(new StringBuilder(50).append("select rowid,* from ").append(DatabaseHelper.NAME_NOTES_TABLE)
                            .append(" where ").append(DatabaseHelper.NotesTable.TITLE).append("='")
                            .append(title).append("'").toString(), null);
                    Log.d(TAG, "-------getNoteFromDb:0-->" + cursor.getCount());
                    HashMap<String, NoteData> noteDataHashMap = new HashMap<String, NoteData>();
                    NoteData noteData = new NoteData();
                    if (cursor.moveToFirst()) {
                        noteData.setTitle(cursor.getString(DatabaseHelper.NotesTable.INDEX_TITLE));
                        noteData.setText(cursor.getString(DatabaseHelper.NotesTable.INDEX_TEXT));
                        noteData.setHearted(cursor.getInt(DatabaseHelper.NotesTable.INDEX_STATUS_HEARTED));
                        noteData.setPoem(cursor.getInt(DatabaseHelper.NotesTable.INDEX_STATUS_POEM));
                        noteData.setStarred(cursor.getInt(DatabaseHelper.NotesTable.INDEX_STATUS_STARRED));
                        noteData.setStory(cursor.getInt(DatabaseHelper.NotesTable.INDEX_STATUS_STORY));
                        noteData.setTime(cursor.getLong(DatabaseHelper.NotesTable.INDEX_SAVE_TIME));
                        noteData.setId(cursor.getInt(0));
                        Log.d(TAG, "-------getNoteFromDb:1-->" + noteData);
                    }
                    dbCallBack.sendData(noteData);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (db != null)
                        db.close();
                    if (cursor != null)
                        cursor.close();
                }
            }
        }.start();
    }

    public void deleteNote(final DbCallBack dbCallBack, final Context context, final NoteData noteData) {
        new Thread() {
            @Override
            public void run() {
                Log.d(TAG, "deleteNote~~~>run");
                SQLiteDatabase db = null;
                Cursor cursor = null;
                try {
                    DatabaseHelper databaseHelperInstance = new DatabaseHelper(context.getApplicationContext());
                    db = databaseHelperInstance.getReadableDatabase();
                    boolean result = db.delete(DatabaseHelper.NAME_NOTES_TABLE, DatabaseHelper.NotesTable.TITLE + "='" + noteData.getTitle() + "'", null) > 0;
                    dbCallBack.sendData(result);
                    Log.d(TAG, "-------deleteNote:0-->" + result);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (db != null)
                        db.close();
                    if (cursor != null)
                        cursor.close();
                }
            }
        }.start();
    }


    public interface DbCallBack {
        public void sendData(Object object);
    }

}
