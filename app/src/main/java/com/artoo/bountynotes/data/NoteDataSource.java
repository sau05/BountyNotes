package com.artoo.bountynotes.data;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class NoteDataSource {

    private static final String PREFKEY = "notes";
    private SharedPreferences notePrefs;

    public NoteDataSource(Context context) {
        notePrefs = context.getSharedPreferences(PREFKEY, Context.MODE_PRIVATE);
    }

    public List<NoteItem> findAll() {

        Map<String, ?> notesMap = notePrefs.getAll();

        SortedSet<String> keys = new TreeSet<String>(notesMap.keySet());

        List<NoteItem> noteList = new ArrayList<NoteItem>();
        for (String key : keys) {
            NoteItem note = new NoteItem();
            note.setKey(key);
            note.setTitle(((String) notesMap.get(key)).split("-")[0]);
            note.setDescription(((String) notesMap.get(key)).split("-")[1]);
            noteList.add(note);
        }
        return noteList;
    }

    public boolean update(NoteItem note) {
        SharedPreferences.Editor editor = notePrefs.edit();
        editor.putString(note.getKey(), note.getTitle()+"-"+note.getDescription());
        editor.apply();
        return true;
    }

    public boolean remove(NoteItem note) {
        if (notePrefs.contains(note.getKey())) {
            SharedPreferences.Editor editor = notePrefs.edit();
            editor.remove(note.getKey());
            editor.apply();
        }
        return true;
    }
}