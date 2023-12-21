package com.doan.doan;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

public class CreateNote extends AppCompatActivity {

    private EditText edtTitle;
    private EditText edtContent;
    private Button back;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        sharedPreferences = getSharedPreferences(Const.Pre_Login,MODE_PRIVATE);
        edtTitle = findViewById(R.id.edtTitle);
        edtContent = findViewById(R.id.edtContent);
        back = findViewById(R.id.back);
        String s = sharedPreferences.getString("token", "default");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = edtTitle.getText().toString();
                String content = edtContent.getText().toString();
                NoteModel noteModel = new NoteModel(title,content);
                ApiService.apiService.addNote("Bearer "+ sharedPreferences.getString("token", "default"),noteModel)
                        .enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(CreateNote.this, "Add note successful", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(CreateNote.this, MainActivity.class);
                                    startActivity(intent);
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Toast.makeText(CreateNote.this, "Add note fail", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}
