package com.artoo.bountynotes;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;

import com.artoo.bountynotes.data.NoteDataSource;
import com.artoo.bountynotes.data.NoteItem;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int EDITOR_ACTIVITY_REQUEST = 1001;
    private List<NoteItem> notesList;
    private StaggeredGridLayoutManager sGridLayoutManager;
    private RecyclerView recyclerView;
    private SQLiteDataHelper dataHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataHelper=new SQLiteDataHelper(this,"bounty");
        dataHelper.getReadableDatabase();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        sGridLayoutManager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(sGridLayoutManager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNote();
            }
        });
        refreshDisplay();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDITOR_ACTIVITY_REQUEST && resultCode == RESULT_OK) {
            refreshDisplay();
        }
    }

    private void createNote() {
//        NoteItem note = NoteItem.getNew();
        NoteItem note = new NoteItem();
        Intent intent = new Intent(this, AddNoteActivity.class);
//        intent.putExtra(NoteItem.KEY, note.getKey());
        intent.putExtra(NoteItem.TITLE, note.getTitle());
        intent.putExtra(NoteItem.DESC, note.getDescription());
        intent.putExtra(AddNoteActivity.INTENT_MODE_CREATE,true);
        startActivityForResult(intent,EDITOR_ACTIVITY_REQUEST);
    }

    private void refreshDisplay() {
        notesList=dataHelper.noteItemList();
        RecyclerViewAdapter rcAdapter = new RecyclerViewAdapter(
                MainActivity.this, notesList);
        recyclerView.setAdapter(rcAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dataHelper.close();
    }
}
