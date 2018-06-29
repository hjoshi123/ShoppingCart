package com.test.ecommerceft.room.cart;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface CartDao {
    @Insert
    void insert(Cart... cart);

    @Query("SELECT * FROM Cart WHERE phoneNumber LIKE :phoneNumber")
    List<Cart> getAllItems(String phoneNumber);
}
