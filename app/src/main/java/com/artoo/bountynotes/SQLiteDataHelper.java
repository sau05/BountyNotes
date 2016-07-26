package com.artoo.bountynotes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.artoo.bountynotes.data.NoteItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Saurabh on 7/26/2016.
 */
public class SQLiteDataHelper extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "tb_bounty";
    private long id;

    public SQLiteDataHelper(Context context, String name) {
        super(context, name, null, 1);
    }

    private static final String CREATE_SQL = "CREATE TABLE " + TABLE_NAME + " (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT NOT NULL, desc TEXT NOT NULL, _date LONG NOT NULL)";

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_SQL);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    public long insertNote(NoteItem noteItem) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", noteItem.getTitle());
        contentValues.put("desc", noteItem.getDescription());
        contentValues.put("_date", noteItem.getTime());
        id = database.insert(TABLE_NAME, null, contentValues);
        return id;
    }

    public void updateData(int id, NoteItem noteItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", noteItem.getTitle());
        contentValues.put("desc", noteItem.getDescription());
        db.update(TABLE_NAME, contentValues, "id=" + id, null);
    }

    public List<NoteItem> noteItemList() {
        List<NoteItem> items = new ArrayList<NoteItem>();
        NoteItem noteItem;
        Cursor c = null;
        SQLiteDatabase db = this.getReadableDatabase();
        c = db.query(TABLE_NAME, new String[]{"id", "title", "desc", "_date"}, null, null, null, null, "id" + " ASC");
        while (c.moveToNext()) {
            noteItem = new NoteItem();
            applyCursor(noteItem, c);
            items.add(noteItem);
        }
        Collections.sort(items, new Comparator<NoteItem>() {
            @Override
            public int compare(NoteItem noteItem, NoteItem t1) {
                return noteItem.getId() < t1.getId() ? 1 : -1;
            }
        });
        return items;
    }

    public boolean deleteNoteItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int r = db.delete(TABLE_NAME, "id = ?", new String[]{String.valueOf(id)});
        return r > 0;
    }

    private void applyCursor(NoteItem noteItem, Cursor c) {
        int i = 0;
        for (String n : c.getColumnNames()) {
            if (n.equals("id")) {
                noteItem.setId(c.getInt(i));
            } else if (n.equals("title")) {
                noteItem.setTitle(c.getString(i));
            } else if (n.equals("desc")) {
                noteItem.setDescription(c.getString(i));
            } else if (n.equals("_date")) {
                noteItem.setTime(c.getLong(i));
            }
            i++;
        }
    }
}