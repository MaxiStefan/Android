package com.example.proiect1;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities={FXRate.class}, version=1, exportSchema = false)
public abstract class FXRatesDB extends RoomDatabase {
    private final static String DB_NAME="fxrates.db";
    private static FXRatesDB instance;

    //method to create the instance
    public static FXRatesDB getInstance(Context context){
        if(instance==null){
            instance= Room.databaseBuilder(context,
                    FXRatesDB.class, DB_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
    public abstract FXRateDao getDaoRate();
}
