package com.test.ecommerceft.room.cart;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Cart.class}, version = 1)
public abstract class CartDB extends RoomDatabase{
    public abstract CartDao getCartDao();

}
