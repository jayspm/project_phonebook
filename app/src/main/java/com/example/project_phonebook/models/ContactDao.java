package com.example.project_phonebook.models;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ContactDao {
    // create
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertContacts(Contact... contacts);

    // update
    @Update
    public void updateContacts(Contact... contacts);

    // delete
    @Delete
    public void deleteContacts(Contact... contacts);

    // delete all
    @Query("DELETE FROM contacts")
    public void clearTable();

    // read all
    @Query("SELECT * FROM contacts")
    List<Contact> getAllContacts();

    @Query("SELECT id AS id, name AS name, phone AS phone FROM contacts")
    List<Contact> getAllContactsForModel();

    // read one by id
    @Query("SELECT * FROM contacts WHERE id = :contactId")
    Contact getContactById(int contactId);
}