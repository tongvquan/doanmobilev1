package com.doan.doan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity{

    private RecyclerView rcvNote;
    private TextView share;
    private List<NoteModel> list;
    private Button addButton;
    private Button logOut;
    private Button btnSearch;
    private EditText inputSearch;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rcvNote = findViewById(R.id.rcvNote);
        addButton = findViewById(R.id.addButton);
        logOut = findViewById(R.id.logOut);
        btnSearch = findViewById(R.id.btnSearch);
        inputSearch=findViewById(R.id.inputSearch);
        sharedPreferences = getSharedPreferences(Const.Pre_Login,MODE_PRIVATE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvNote.setLayoutManager(linearLayoutManager);
//        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
//        rcvNote.addItemDecoration(itemDecoration);
        String s = sharedPreferences.getString("token", "default");
        list = new ArrayList<>();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CreateNote.class);
                startActivity(intent);
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                String input = inputSearch.getText().toString().trim();
                bundle.putSerializable("search",input);

                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferences.edit().remove("token").commit();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        callApiGetNotes();
    }

    private void callApiGetNotes(){
        ApiService.apiService.getListNotes("Bearer "+ sharedPreferences.getString("token", "default")).enqueue(new Callback<List<NoteModel>>() {
            @Override
            public void onResponse(Call<List<NoteModel>> call, Response<List<NoteModel>> response) {
                list = response.body();
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
                Toast.makeText(MainActivity.this, "failed", Toast.LENGTH_SHORT).show();
            }
        });
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
                Toast.makeText(MainActivity.this, "suc", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(MainActivity.this, "failed", Toast.LENGTH_SHORT).show();
            }
        });
    }


}