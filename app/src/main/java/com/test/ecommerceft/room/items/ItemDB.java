package com.test.ecommerceft.room.items;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Item.class}, version = ItemDB.VERSION)
public abstract class ItemDB extends RoomDatabase {
    static final int VERSION = 1;
    public abstract ItemDao getItemDao();
}

