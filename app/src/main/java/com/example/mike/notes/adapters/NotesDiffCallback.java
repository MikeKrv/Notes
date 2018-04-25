package com.example.mike.notes.adapters;

import android.support.v7.util.DiffUtil;

import com.example.mike.notes.model.Note;

import java.util.List;

/**
 * Created by Mike on 28.01.2018.
 */

public class NotesDiffCallback extends DiffUtil.Callback {

    private final List<Note> mOldNoteList;
    private final List<Note> mNewNoteList;

    public NotesDiffCallback(List<Note> mOldNoteList, List<Note> mNewNoteList) {
        this.mOldNoteList = mOldNoteList;
        this.mNewNoteList = mNewNoteList;
    }

    @Override
    public int getOldListSize() {
        return mOldNoteList.size();
    }

    @Override
    public int getNewListSize() {
        return mNewNoteList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldNoteList.get(oldItemPosition).getId() == mNewNoteList.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Note oldNote = mOldNoteList.get(oldItemPosition);
        Note newNote = mNewNoteList.get(newItemPosition);
        return oldNote.getTime() == newNote.getTime();
        //return mOldNoteList.get(oldItemPosition).equals(mNewNoteList.get(newItemPosition));
    }
}
