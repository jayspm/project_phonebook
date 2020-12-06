package com.example.project_phonebook.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.project_phonebook.models.Contact;
import com.example.project_phonebook.models.ContactDao;

@Database(
        entities = {Contact.class},
        version = 1,
        exportSchema = false
)
public abstract class PhonebookDatabase extends RoomDatabase {
    public abstract ContactDao contactDao();

    private static PhonebookDatabase phonebookDatabase;

    public static PhonebookDatabase getDbInstance(final Context context) {
        if(phonebookDatabase == null) {
            phonebookDatabase = Room.databaseBuilder(context.getApplicationContext(),
                    PhonebookDatabase.class, "phonebook_room.db")
                    .allowMainThreadQueries()
                    .build();
        }
        return phonebookDatabase;
    }
}