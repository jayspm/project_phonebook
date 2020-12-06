package com.example.project_phonebook;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.project_phonebook.activities.ActivityContactList;
import com.example.project_phonebook.database.RemotePhonebookDatabase;
import com.example.project_phonebook.models.Contact;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements RetrofitServices.ResultsHandler{

    private String TAG = this.getClass().getSimpleName();
    private Button btnDarkTheme;
    private Button btnLightTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }

        setContentView(R.layout.activity_main);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Phonebook Application");
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.107:5000/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RemotePhonebookDatabase service = retrofit.create(RemotePhonebookDatabase.class);

        Call<Contact> contactCreate = service.ContactCreate(new Contact("Jay Ma", "0498545441", "jma@gmail.com", "8/8/1998"));
        contactCreate.enqueue(new Callback<Contact>() {
            @Override
            public void onResponse(Call<Contact> call, Response<Contact> response) {
                Contact contact = response.body();
                Log.d(TAG, contact.toString());
                return;
            }

            @Override
            public void onFailure(Call<Contact> call, Throwable t) {
                Log.d(TAG, "onFailure");
                return;
            }
        });

        setBtnDarkThemeClick();
        setBtnLightThemeClick();
    }

    private void setBtnDarkThemeClick() {
        btnDarkTheme = findViewById(R.id.welcome_btnDark);
        btnDarkTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }

                Intent contactListIntent = new Intent(MainActivity.this, ActivityContactList.class);
                startActivity(contactListIntent);
            }
        });
    }

    private void setBtnLightThemeClick() {
        btnLightTheme = findViewById(R.id.welcome_btnLight);
        btnLightTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }

                Intent contactListIntent = new Intent(MainActivity.this, ActivityContactList.class);
                startActivity(contactListIntent);
            }
        });
    }

    @Override
    public void CreateOnResponseHandler(Contact contact) {

    }

    @Override
    public void ReadOneOnResponseHandler(Contact contact) {

    }

    @Override
    public void ReadAllOnResponseHandler(List<Contact> contactList) {

    }

    @Override
    public void UpdateOnResponseHandler() {

    }

    @Override
    public void DeleteOnResponseHandler(Contact contact) {

    }

    @Override
    public void OnFailureHandler() {

    }
}