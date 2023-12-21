package com.doan.doan.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.doan.doan.R;
import com.doan.doan.api.ApiService;
import com.doan.doan.model.NoteModel;
import com.doan.doan.sharedpreferences.Const;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewAdapter> {
    private List<NoteModel> list;
    private IRecyclerViewItemClickListener viewItemClickListener;


    public NoteAdapter(List<NoteModel> list, IRecyclerViewItemClickListener viewItemClickListener) {
        this.list = list;
        this.viewItemClickListener = viewItemClickListener;
    }

    @NonNull
    @Override
    public NoteViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent,false);
        return new NoteViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewAdapter holder, int position) {
        NoteModel note = list.get(position);
        if(note == null){
            return;
        }
        holder.tvTitle.setText(note.getTitle());
//        holder.tvContent.setText(note.getContent());
        holder.tvId.setText(note.getId().toString());

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewItemClickListener.onButtonDelClick(note);
            }
        });

        holder.layoutNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewItemClickListener.onItemClick(note);
            }
        });
    }


    @Override
    public int getItemCount() {
        if(list != null)
            return list.size();
        return 0;
    }

    public static class NoteViewAdapter extends RecyclerView.ViewHolder {
        private final TextView tvTitle;
//        private final TextView tvContent;

        private final TextView tvId;
        private Button btnDelete;
        public LinearLayout layoutNote;

        public NoteViewAdapter(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
//            tvContent = itemView.findViewById(R.id.tvSub);
            tvId = itemView.findViewById(R.id.tvId);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            layoutNote=itemView.findViewById(R.id.layoutNote);
        }
    }
}
