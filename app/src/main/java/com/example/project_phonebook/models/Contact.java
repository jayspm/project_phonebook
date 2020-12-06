package com.example.project_phonebook.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "contacts")
public class Contact implements Comparable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "phone")
    public String phone;

    @ColumnInfo(name = "email")
    public String email;

    @ColumnInfo(name = "dob")
    public String dob;

    public Contact() {}

    @Ignore
    public Contact(String name, String phone, String email, String dob) {
        this.id = 0;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.dob = dob;
    }

    @Ignore
    public Contact(int id, String name, String phone, String email, String dob) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.dob = dob;
    }

    @Override
    public int compareTo(Object o) {
        return this.name.compareToIgnoreCase(((com.example.project_phonebook.models.Contact)o).name);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() {
        return name;
    }
    public void setName(String name) { this.name = name; }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) { this.phone = email; }
    public String getDob() {
        return dob;
    }
    public void setDob(String dob) { this.phone = dob; }

    @NonNull
    @Override
    public String toString() {
        return "ID: " + this.id + " Name: " + this.name
                + ", phone: " + this.phone + " AND email: " + this.email + ", DOB: " + this.dob;
    }
}