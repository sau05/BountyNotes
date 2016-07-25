package com.artoo.bountynotes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.artoo.bountynotes.data.NoteItem;

import java.util.List;

/**
 * Created by Saurabh on 7/26/2016.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.SimpleViewHolder> {
    private List<NoteItem> notesList;
    private Context mContext;

    public RecyclerViewAdapter(Context context, List<NoteItem> noteItems) {
        this.mContext = context;
        this.notesList = noteItems;
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.list_item_layout, null);
        SimpleViewHolder rcv = new SimpleViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, int position) {
        holder.mTitle.setText(notesList.get(position).getTitle());
        holder.mDesc.setText(notesList.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return this.notesList.size();
    }

    @UiThread
    protected void dataSetChanged() {
        notifyDataSetChanged();
    }

    public class SimpleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mTitle;
        public TextView mDesc;

        public SimpleViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mTitle = (TextView) itemView.findViewById(R.id.item_title);
            mDesc = (TextView) itemView.findViewById(R.id.item_desc);
        }

        @Override
        public void onClick(View view) {
            NoteItem note = notesList.get(getAdapterPosition());
            Intent intent = new Intent(mContext, AddNoteActivity.class);
//            intent.putExtra(NoteItem.KEY, note.getKey());
            intent.putExtra(NoteItem.ID, note.getId());
            intent.putExtra(NoteItem.TITLE, note.getTitle());
            intent.putExtra(NoteItem.DESC, note.getDescription());
            intent.putExtra(NoteItem.TIME, note.getTime());
            intent.putExtra(AddNoteActivity.INTENT_MODE_CREATE,false);
            ((Activity) mContext).startActivityForResult(intent, MainActivity.EDITOR_ACTIVITY_REQUEST);
        }
    }
}