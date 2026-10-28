package com.example.airlinebookingapp.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users") // The @Entity annotation tells Room to create a table for this class.
public class User {

    @PrimaryKey(autoGenerate = true) // @PrimaryKey marks this field as the primary key.
    private int id;

    private String uid;
    private String name;
    private String email;
    private String password; // Note: Storing plain passwords is not recommended.
    private boolean isAdmin;

    public User() {
        // Required for Firebase
    }

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.isAdmin = false;
    }

    // --- Getters and setters ---

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public boolean isAdmin() { return isAdmin; }
    public void setAdmin(boolean admin) { isAdmin = admin; }
}
