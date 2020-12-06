package com.example.project_phonebook.activities;

import android.os.Bundle;
import android.util.Log;
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

public class ActivityUpdateContact  extends AppCompatActivity {

    PhonebookDatabase _db;
    private String TAG = this.getClass().getSimpleName();
    private Button btnBackToPrevious, btnUpdateContact;
    private EditText edt_name, edt_phone, edt_email, edt_dob, edt_name_updated, edt_phone_updated, edt_email_updated, edt_dob_updated;
    private String update_name, update_phone, update_email, update_dob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }

        setContentView(R.layout.activity_updatecontact);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Update Contact");
        }

        _db = getDbInstance(getApplicationContext());

        int index = -1;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey("id")) {
            index = bundle.getInt("id");

            if (index >= 0) {
                Contact getItem = _db.contactDao().getContactById(index);

                if (getItem != null) {
                    edt_name = findViewById(R.id.update_txtEditName);
                    edt_name.setText(getItem.name);
                    edt_phone = findViewById(R.id.update_txtEditPhone);
                    edt_phone.setText(getItem.phone);
                    edt_email = findViewById(R.id.update_txtEditEmail);
                    edt_email.setText(getItem.email);
                    edt_dob = findViewById(R.id.update_txtEditDob);
                    edt_dob.setText(getItem.dob);
                }

                Log.d(TAG, "This is index from show detail page " + index + " and the name is " + getItem.name);
            }
        }

        btnBackToPreviousClick();
        btnUpdateContactClick();
    }

    private void btnBackToPreviousClick() {
        btnBackToPrevious = findViewById(R.id.update_btnBack);
        btnBackToPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void btnUpdateContactClick() {
        btnUpdateContact = findViewById(R.id.update_btnUpdate);
        btnUpdateContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edt_name.getText().toString().isEmpty()
                        || edt_phone.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter name or phone number!", Toast.LENGTH_LONG).show();
                } else {
                    edt_name_updated = findViewById(R.id.update_txtEditName);
                    edt_phone_updated = findViewById(R.id.update_txtEditPhone);
                    edt_email_updated = findViewById(R.id.update_txtEditEmail);
                    edt_dob_updated = findViewById(R.id.update_txtEditDob);

                    update_name = edt_name_updated.getText().toString().trim();
                    update_phone = edt_phone_updated.getText().toString().trim();
                    update_email = edt_email_updated.getText().toString().trim();
                    update_dob = edt_dob_updated.getText().toString().trim();
                }

                _db.contactDao().updateContacts(new Contact("" + update_name + "", "" + update_phone + "", "" + update_email + "", "" + update_dob + ""));
                Toast.makeText(getApplicationContext(), update_name + " is successfully updated", Toast.LENGTH_LONG).show();
                Log.d(TAG, "Update page -- " + update_name + " , " + update_phone + " , " + update_email + " , " + update_dob);

                edt_name_updated.setText("");
                edt_phone_updated.setText("");
                edt_email_updated.setText("");
                edt_dob_updated.setText("");
                finish();
            }
        });
    }
}