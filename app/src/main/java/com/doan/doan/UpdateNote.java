package com.doan.doan;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.doan.doan.api.ApiService;
import com.doan.doan.model.NoteModel;
import com.doan.doan.sharedpreferences.Const;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateNote extends AppCompatActivity {

    private EditText edtTitle;
    private EditText edtContent;
    private TextView tvIdNote;
    private Button back;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_notes);
        Bundle bundle = getIntent().getExtras();
        sharedPreferences = getSharedPreferences(Const.Pre_Login,MODE_PRIVATE);
        edtTitle = findViewById(R.id.edtTitle);
        edtContent = findViewById(R.id.edtContent);
        tvIdNote = findViewById(R.id.tvIdNote);
        back = findViewById(R.id.back);
        String s = sharedPreferences.getString("token", "default");

        if(bundle != null){
            NoteModel note = (NoteModel) bundle.get("note");
            edtTitle.setText(note.getTitle());
            edtContent.setText(note.getContent());
            tvIdNote.setText(note.getId().toString());
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String title = edtTitle.getText().toString();
                    String content = edtContent.getText().toString();
                    Long id = Long.parseLong(tvIdNote.getText().toString());
                    NoteModel noteModel = new NoteModel(title,content);

                    ApiService.apiService.updateNote("Bearer "+ sharedPreferences.getString("token", "default"),id, noteModel).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(UpdateNote.this, "Update successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(UpdateNote.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                int statusCode = response.code();
                                Log.e("API_ERROR", "HTTP Status Code: " + statusCode);
                                Toast.makeText(UpdateNote.this, "Failed to update note. Status Code: " + statusCode, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(UpdateNote.this, "Failed to update note", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(UpdateNote.this, MainActivity.class);
                        }
                    });

                }
            });
        }


    }
}
