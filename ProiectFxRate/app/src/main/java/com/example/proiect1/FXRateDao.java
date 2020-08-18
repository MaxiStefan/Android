package com.example.proiect1;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FXRateDao {
    @Insert
    public void insert(FXRate fxRate);

    @Insert
    public void insert (List<FXRate> rates);

    @Delete
    public void deleteCV(FXRate rate);

    @Query("Delete from fxrates")
    public void deleteAll();

    @Query("SELECT * FROM fxrates")
    public List<FXRate> getAll();

//    @Query("SELECT * FROM fxrates WHERE id=:id")
//    FXRate getFXRate(int id);
}
