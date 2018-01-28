package com.example.mike.notes.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mike.notes.R;
import com.example.mike.notes.model.Note;
import com.example.mike.notes.ui.NoteActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mike on 27.01.2018.
 */

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {
    public static final int NEW_NOTE_ACTIVTIY_REQUEST_CODE = 1;

    private List<Note> notesList = new ArrayList<>();

    @Override
    public NotesViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
        final NotesViewHolder holder = new NotesViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Note note = notesList.get(holder.getAdapterPosition());
                Intent intent = new Intent(parent.getContext(), NoteActivity.class);
                intent.putExtra("TITLE", note.getTitle());
                intent.putExtra("TEXT", note.getText());
                intent.putExtra("TIME", note.getTime());
                intent.putExtra("ID", note.getId());
                ((Activity)parent.getContext()).startActivityForResult(intent, NEW_NOTE_ACTIVTIY_REQUEST_CODE);

            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(NotesViewHolder holder, int position) {
        holder.title.setText(notesList.get(position).getTitle());
        holder.text.setText(notesList.get(position).getText());
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public class NotesViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView text;

        public NotesViewHolder(View itemView){
            super(itemView);
            title = itemView.findViewById(R.id.tvTitle);
            text = itemView.findViewById(R.id.tvText);
        }

    }

    public void addItems(List<Note> items){
            notesList.addAll(items);
    }

    public void addItem(Note note){
        notesList.add(0, note);
    }

    public void refresh(){
        notesList.clear();
    }
}
