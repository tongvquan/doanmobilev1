package com.doan.doan;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.doan.doan.api.ApiService;
import com.doan.doan.model.RegisterModel;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText edtUsername,edtPassword,edtConfirmPassword,edtFullname;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_register);

            edtUsername = findViewById(R.id.edtUsername);
            edtPassword = findViewById(R.id.edtPassword);
            edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
            edtFullname = findViewById(R.id.edtFullname);
            btnRegister = findViewById(R.id.btnRegisters);

            btnRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String userName = edtUsername.getText().toString().trim();
                    String password = edtPassword.getText().toString().trim();
                    String fullName = edtFullname.getText().toString().trim();
                    String confirmPassword = edtConfirmPassword.getText().toString().trim();
                    if (!password.equals(confirmPassword)) {
                        Toast.makeText(RegisterActivity.this, "Password does not match the confirm password", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    RegisterModel registerModel = new RegisterModel(userName,password,fullName);
                    ApiService.apiService.register(registerModel).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                ResponseBody responseBody = response.body();
                                    Toast.makeText(RegisterActivity.this, "Register successful", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                    startActivity(intent);
                            }

                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.e("RegisterActivity", "Failed to register account", t);
                            Toast.makeText(RegisterActivity.this,"Failed to register account1", Toast.LENGTH_SHORT).show();
                        }
                    });
                }


            });
    }



}
