package com.saurabh.bountynotes;

import android.content.Context;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.artoo.bountynotes.R;
import com.saurabh.bountynotes.data.NoteItem;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

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

    public void setNotesList(List<NoteItem> notes) {
        this.notesList = notes;
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM", Locale.US);
        holder.mDate.setText(dateFormat.format(notesList.get(position).getTime()));
        holder.itemView.setLongClickable(true);
    }

    @Override
    public int getItemCount() {
        return this.notesList.size();
    }

    @UiThread
    protected void dataSetChanged() {
        notifyDataSetChanged();
    }

    public class SimpleViewHolder extends RecyclerView.ViewHolder {
        public TextView mTitle;
        public TextView mDesc;
        public TextView mDate;

        public SimpleViewHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.item_title);
            mDesc = (TextView) itemView.findViewById(R.id.item_desc);
            mDate = (TextView) itemView.findViewById(R.id.item_date);
        }
    }
}