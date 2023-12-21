package com.doan.doan.api;

import com.doan.doan.model.LoginModel;
import com.doan.doan.model.NoteModel;
import com.doan.doan.model.RegisterModel;
import com.doan.doan.model.ResponseLogin;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd")
            .setLenient()
            .create();

    ApiService apiService = new Retrofit.Builder()
            .baseUrl("http://192.168.0.102:8080/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService.class);

    @POST("auth/login")
    Call<ResponseLogin> response(@Body LoginModel loginModel);

    @POST("register/account")
    Call<ResponseBody> register(@Body RegisterModel registerModel);
    @PUT("user/note/{noteId}")
    Call<ResponseBody> updateNote(@Header("Authorization") String token,@Path("noteId") Long noteId, @Body NoteModel noteModel);

    @GET("user/notes")
    Call<List<NoteModel>> getListNotes(@Header("Authorization") String token);

    @POST("user/note")
    Call<ResponseBody> addNote(@Header("Authorization") String token, @Body NoteModel noteModel);

    @DELETE("user/note/{noteId}")
    Call<ResponseBody> deleteNote(@Header("Authorization") String token,@Path("noteId") Long noteId);

    @GET("user/notes/search")
    Call<List<NoteModel>> searchNotes(@Header("Authorization") String token,
                                       @Query("title") String title);
}
