package com.test.ecommerceft.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface ItemDao {
    @Insert
    void insert(Item... item);

    @Query("SELECT * FROM Item")
    List<Item> getAllItems();
}
