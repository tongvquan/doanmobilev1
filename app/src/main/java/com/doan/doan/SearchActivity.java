package com.doan.doan;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.doan.doan.adapter.IRecyclerViewItemClickListener;
import com.doan.doan.adapter.NoteAdapter;
import com.doan.doan.api.ApiService;
import com.doan.doan.model.NoteModel;
import com.doan.doan.sharedpreferences.Const;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;

    private RecyclerView rcvNote;
    private TextView tvIdNote;
    private EditText edtTitle;
    private EditText edtContent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_note);
        Bundle bundle = getIntent().getExtras();
        rcvNote = findViewById(R.id.rcvNoteSearch);
        sharedPreferences = getSharedPreferences(Const.Pre_Login,MODE_PRIVATE);

        if (bundle != null) {
            String title = bundle.getString("search");

            ApiService.apiService.searchNotes("Bearer "+ sharedPreferences.getString("token", "default"), title)
                    .enqueue(new Callback<List<NoteModel>>() {
                        @Override
                        public void onResponse(Call<List<NoteModel>> call, Response<List<NoteModel>> response) {
                            List<NoteModel> list = response.body();
                            NoteAdapter noteAdapter = new NoteAdapter(list, new IRecyclerViewItemClickListener() {
                                @Override
                                public void onItemClick(NoteModel note) {
                                    onClickGoToUpdateNote(note);
                                }

                                @Override
                                public void onButtonDelClick(NoteModel noteModel) {
                                    deleteNote(noteModel);
                                }
                            });
                            rcvNote.setAdapter(noteAdapter);
                        }

                        @Override
                        public void onFailure(Call<List<NoteModel>> call, Throwable t) {

                        }
                    });
        }

    }
    private void onClickGoToUpdateNote(NoteModel note){
        Intent intent = new Intent(this, UpdateNote.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("note",note);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void deleteNote(NoteModel note){
        ApiService.apiService.deleteNote("Bearer "+ sharedPreferences.getString("token", "default"),note.getId()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(SearchActivity.this, "suc", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SearchActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(SearchActivity.this, "failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
