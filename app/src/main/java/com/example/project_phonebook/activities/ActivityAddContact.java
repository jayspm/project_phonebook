package com.example.project_phonebook.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.project_phonebook.R;
import com.example.project_phonebook.database.PhonebookDatabase;
import com.example.project_phonebook.models.Contact;

import static com.example.project_phonebook.database.PhonebookDatabase.getDbInstance;

public class ActivityAddContact extends AppCompatActivity {

    PhonebookDatabase _db;
    private Button btnBackToPrevious;
    private Button btnAddNewContact;
    private EditText edt_insert_name, edt_insert_phone, edt_insert_email, edt_insert_dob;
    private String insert_Name, insert_phone, insert_email, insert_dob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }

        setContentView(R.layout.activity_addnewcontact);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Add New Contact");
        }

        _db = getDbInstance(getApplicationContext());
        btnBackToPreviousClick();
        btnAddNewContactClick();
    }

    private void btnBackToPreviousClick() {
        btnBackToPrevious = findViewById(R.id.addNew_btnBack);
        btnBackToPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void btnAddNewContactClick() {
        btnAddNewContact = findViewById(R.id.addNew_btnAddNew);
        btnAddNewContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_insert_name = (EditText) findViewById(R.id.addNew_txtEditName);
                edt_insert_phone = (EditText) findViewById(R.id.addNew_txtEditPhone);
                edt_insert_email = (EditText) findViewById(R.id.addNew_txtEditEmail);
                edt_insert_dob = (EditText) findViewById(R.id.addNew_txtEditDob);

                if (edt_insert_name.getText().toString().isEmpty()
                        || edt_insert_phone.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter name or phone number!", Toast.LENGTH_LONG).show();
                } else {
                    insert_Name = edt_insert_name.getText().toString().trim();
                    insert_phone = edt_insert_phone.getText().toString().trim();
                    insert_email = edt_insert_email.getText().toString().trim();
                    insert_dob = edt_insert_dob.getText().toString().trim();
                }

                _db.contactDao().insertContacts(new Contact("" + insert_Name + "", "" + insert_phone + "", "" + insert_email + "", "" + insert_dob + ""));
                Toast.makeText(getApplicationContext(), insert_Name + " is successfully added", Toast.LENGTH_LONG).show();

                edt_insert_name.setText("");
                edt_insert_phone.setText("");
                edt_insert_email.setText("");
                edt_insert_dob.setText("");

                Intent backToDetailContactIntent = new Intent(ActivityAddContact.this, ActivityContactList.class);
                startActivity(backToDetailContactIntent);
            }
        });
    }
}