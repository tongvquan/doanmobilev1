package com.doan.doan.adapter;

import androidx.recyclerview.widget.RecyclerView;

import com.doan.doan.model.NoteModel;

public interface IRecyclerViewItemClickListener {
    void onItemClick(NoteModel note);
    void onButtonDelClick(NoteModel noteModel);
}
