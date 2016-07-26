package com.artoo.bountynotes;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.artoo.bountynotes.data.NoteItem;

import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerItemClickListener.OnItemClickListener {

    public static final int EDITOR_ACTIVITY_REQUEST = 1001;
    //    private NoteDataSource datasource;
    private List<NoteItem> notesList;
    private StaggeredGridLayoutManager sGridLayoutManager;
    private RecyclerView recyclerView;
    private SQLiteDataHelper dataHelper;
    private View mContentView;
    private int mShortAnimationDuration;
    private EditText etTitle, etNote;
    private NoteItem note;
    private boolean modeCreate;
    private int id = 1;
    private RecyclerViewAdapter mAdapter;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        mShortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);

        dataHelper = new SQLiteDataHelper(this, "bounty");
        dataHelper.getReadableDatabase();

        sGridLayoutManager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(sGridLayoutManager);


        refreshDisplay();
    }

    private void findViews() {
        mContentView = findViewById(R.id.add_note_content);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, this));
        recyclerView.setHasFixedSize(true);
        etTitle = (EditText) findViewById(R.id.etTitle);
        etNote = (EditText) findViewById(R.id.etNote);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                createNote();
                modeCreate = true;
                crossfade();
            }
        });
        mContentView.setVisibility(View.GONE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshDisplay();
    }

    private void refreshDisplay() {
        notesList = dataHelper.noteItemList();
        if (mAdapter == null) {
            mAdapter = new RecyclerViewAdapter(MainActivity.this, notesList);
            recyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setNotesList(notesList);
        }
        mAdapter.dataSetChanged();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dataHelper.close();
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
        reverseCrossfade();
        etNote.setText("");
        etTitle.setText("");
    }

    private void crossfade() {
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Set the content view to 0% opacity but visible, so that it is visible
        // (but fully transparent) during the animation.
        mContentView.setAlpha(0f);
        mContentView.setVisibility(View.VISIBLE);

        // Animate the content view to 100% opacity, and clear any animation
        // listener set on the view.
        mContentView.animate()
                .alpha(1f)
                .setDuration(mShortAnimationDuration)
                .setListener(null);
        recyclerView.animate()
                .alpha(0f)
                .setDuration(mShortAnimationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        recyclerView.setVisibility(View.GONE);
                    }
                });
        fab.animate()
                .alpha(0f)
                .setDuration(mShortAnimationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        fab.setVisibility(View.GONE);
                    }
                });
        note = new NoteItem();

    }

    private void reverseCrossfade() {
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        recyclerView.setAlpha(0f);
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.animate()
                .alpha(1f)
                .setDuration(mShortAnimationDuration)
                .setListener(null);
        fab.setAlpha(0f);
        fab.setVisibility(View.VISIBLE);
        fab.animate()
                .alpha(1f)
                .setDuration(mShortAnimationDuration)
                .setListener(null);
        mContentView.animate()
                .alpha(0f)
                .setDuration(mShortAnimationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mContentView.setVisibility(View.GONE);
                    }
                });
        refreshDisplay();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (modeCreate) {
            doSave();
            modeCreate = false;
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onItemClick(View childView, int position) {
        NoteItem note = notesList.get(position);
        Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
//            intent.putExtra(NoteItem.KEY, note.getKey());
        intent.putExtra(NoteItem.ID, note.getId());
        intent.putExtra(NoteItem.TITLE, note.getTitle());
        intent.putExtra(NoteItem.DESC, note.getDescription());
        intent.putExtra(NoteItem.TIME, note.getTime());
        intent.putExtra(AddNoteActivity.INTENT_MODE_CREATE, false);
        startActivity(intent);
//        startActivityForResult(intent, MainActivity.EDITOR_ACTIVITY_REQUEST);
    }

    @Override
    public void onItemLongPress(View childView, int position) {
        Log.d("long click", "click" + position);
        confirmDelete(position);
    }

    protected void confirmDelete(final int pos) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                MainActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

        alertDialogBuilder.setMessage("Are you sure to delete?");
        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doDeleteNote(pos);
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void doDeleteNote(int pos) {
        dataHelper.deleteNoteItem(notesList.get(pos).getId());
        refreshDisplay();
    }
}