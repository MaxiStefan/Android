package com.example.auctionapplication;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ArtDao {
    @Insert
    public void insert(Art art);

    @Insert
    public void insert (List<Art> arts);

    @Delete
    public void deleteArt(Art art);

    @Query("Delete from art")
    public void deleteAll();

    @Query("SELECT * FROM art")
    public List<Art> getAll();

    @Query("SELECT * FROM art WHERE type = 'Paintings'")
    public List<Art> getByArtYpe();

    @Query("SELECT * FROM art WHERE type =:type")
    public List<Art> getByArtType(String type);

    @Query("SELECT COUNT(name) FROM art WHERE name =:name")
    int getCount(String name);
    @Query("SELECT COUNT(*) FROM art ")
    int getCountAll();

    @Query("SELECT COUNT(*) FROM art where type = 'Collectibles' ")
    int getCountAllCollectibles();
    @Query("SELECT COUNT(*) FROM art where type = 'Furniture'")
    int getCountAllFurniture();
    @Query("SELECT COUNT(*) FROM art where type ='Jewelry'")
    int getCountAllJews();
    @Query("SELECT COUNT(*) FROM art where type ='Paintings'")
    int getCountAllPaintings();
    @Query("SELECT COUNT(*) FROM art where type ='Firearms'")
    int getCountAllFirearms();
}
