package com.example.airlinebookingapp.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.airlinebookingapp.models.User;

@Dao
public interface UserDao {
    @Insert
    void insert(User user);

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    User getUserByEmail(String email);

    @Update
    void update(User user);
}
