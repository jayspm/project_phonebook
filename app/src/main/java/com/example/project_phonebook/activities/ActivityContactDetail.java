package com.example.project_phonebook.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.project_phonebook.R;
import com.example.project_phonebook.database.PhonebookDatabase;
import com.example.project_phonebook.models.Contact;

import static com.example.project_phonebook.database.PhonebookDatabase.getDbInstance;

public class ActivityContactDetail extends AppCompatActivity {

    PhonebookDatabase _db;
    private String TAG = this.getClass().getSimpleName();
    private String isDarkThemeKey = "is_dark_theme_key";
    private int activeThemeResId;
    private Button btnBackToPrevious;
    TextView tv_name, tv_phone, tv_email, tv_dob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }

        setContentView(R.layout.activity_detaildisplayed);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Contact Detail");
        }

        _db = getDbInstance(getApplicationContext());

        int index = -1;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey("id")) {
            index = bundle.getInt("id");

            if (index >= 0) {
                Contact getItem = _db.contactDao().getContactById(index);

                if (getItem != null) {
                    tv_name = findViewById(R.id.detail_lblDisplayName);
                    tv_name.setText(getItem.name);
                    tv_phone = findViewById(R.id.detail_lblDisplayPhone);
                    tv_phone.setText(getItem.phone);
                    tv_email = findViewById(R.id.detail_lblDisplayEmail);
                    tv_email.setText(getItem.email);
                    tv_dob = findViewById(R.id.detail_lblDisplayDob);
                    tv_dob.setText(getItem.dob);
                }

                Log.d(TAG, "This is index from show detail page " + index + " and the name is " + getItem.name);
            }
        }

        btnBackToPreviousClick();
    }

    private void btnBackToPreviousClick() {
        btnBackToPrevious = findViewById(R.id.detail_btnBack);
        btnBackToPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}