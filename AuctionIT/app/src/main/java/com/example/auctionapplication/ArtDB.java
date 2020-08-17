package com.example.auctionapplication;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities={Art.class}, version=2, exportSchema = false)
public abstract class ArtDB extends RoomDatabase {
    private final static String DB_NAME="art.db";
    private static ArtDB instance;

    //method to create the instance
    public static ArtDB getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context,
                    ArtDB.class, DB_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
    public abstract ArtDao getArtDao();
}
