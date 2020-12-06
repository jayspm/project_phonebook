package com.example.project_phonebook.database;

import com.example.project_phonebook.models.Contact;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RemotePhonebookDatabase {

    @POST("Contacts")
    Call<Contact> ContactCreate(@Body Contact contact);

    @GET("Contacts")
    Call<List<Contact>> ContactAll();

    @GET("Contacts/{id}")
    Call<Contact> Contact(@Path("id") int id);

    @PUT("Contacts/{id}")
    Call<Void> ContactUpdate(@Path("id") int id, @Body Contact contact);

    @DELETE("Contacts/{id}")
    Call<Contact> ContactDelete(@Path("id") int id);
}
