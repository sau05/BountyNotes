package com.artoo.bountynotes.data;
import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Saurabh on 7/25/2016.
 */

public class NoteItem {

    private String key;
    private String title;
    private String description;
    long time;
    int id;

    public static final String TITLE = "title";
    public static final String DESC = "desc";
    public static final String KEY = "key";
    public static final String ID = "id";
    public static final String TIME = "time";

    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}

