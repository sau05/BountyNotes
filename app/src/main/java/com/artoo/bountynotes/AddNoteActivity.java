package com.artoo.bountynotes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.artoo.bountynotes.data.NoteItem;

import java.util.Date;

public class AddNoteActivity extends AppCompatActivity {

    private EditText etTitle, etNote;
    private Button btnSave;
    private NoteItem note;
    private SQLiteDataHelper dataHelper;
    public static String INTENT_MODE_CREATE = "mode_create";
    private boolean modeCreate;
    private int id = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dataHelper = new SQLiteDataHelper(this, "bounty");
        Intent intent = this.getIntent();
        modeCreate = intent.getBooleanExtra(INTENT_MODE_CREATE, true);
        note = new NoteItem();
        id = intent.getIntExtra(NoteItem.ID, 1);
//        note.setKey(intent.getStringExtra(NoteItem.KEY));
        note.setId(id);
        note.setTitle(intent.getStringExtra(NoteItem.TITLE));
        note.setDescription(intent.getStringExtra(NoteItem.DESC));
//        note.setTime(intent.getLongExtra(NoteItem.TIME,0));
        etTitle = (EditText) findViewById(R.id.etTitle);
        etNote = (EditText) findViewById(R.id.etNote);

        if (!modeCreate) {
            etTitle.setText(note.getTitle());
            etNote.setText(note.getDescription());
            etNote.setSelection(note.getDescription().length());
        }
    }

    private void doSave() {
        String desc, title;
        desc = etNote.getText().toString();
        title = etTitle.getText().toString();
        note.setDescription(desc);
        note.setTitle(title);
        note.setTime(new Date().getTime());
        if (modeCreate) {
            if (!desc.equals("") || !title.equals("")) {
                id = (int) dataHelper.insertNote(note);
            }
        } else {
            dataHelper.updateData(id, note);
        }
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        doSave();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dataHelper.close();
    }
}