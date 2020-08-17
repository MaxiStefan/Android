package com.example.auctionapplication;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "art")
public class Art implements Serializable {
    @PrimaryKey (autoGenerate =  true)
    private int id;
    private String type;
    private String date;
    private String name;
    private String imageURL;
    private byte[] imageByte;
    private Double estimatedValue;
    private Double startingBid;

    @Ignore
    public Art(){

    }
    @Ignore
    public Art(String type, String date, String name, Double estimatedValue, Double startingBid,String imageURL) {
        this.type = type;
        this.date = date;
        this.name = name;
        this.estimatedValue = estimatedValue;
        this.startingBid = startingBid;
        this.imageURL = imageURL;
    }

    public Art(int id, String type, String date, String name, Double estimatedValue, Double startingBid, String imageURL) {
        this.id = id;
        this.type = type;
        this.date = date;
        this.name = name;
        this.estimatedValue = estimatedValue;
        this.startingBid = startingBid;
        this.imageURL = imageURL;
    }
    @Ignore
    public Art(int id, String type, String date, String name, Double estimatedValue, Double startingBid) {
        this.id = id;
        this.type = type;
        this.date = date;
        this.name = name;
        this.estimatedValue = estimatedValue;
        this.startingBid = startingBid;
    }
    @Ignore
    public Art( String type, String date, String name, Double estimatedValue, Double startingBid, byte[] imageByte) {
        this.type = type;
        this.date = date;
        this.name = name;
        this.imageByte = imageByte;
        this.estimatedValue = estimatedValue;
        this.startingBid = startingBid;
    }
    @Ignore
    public Art(String type, String date, String name, Double estimatedValue, Double startingBid) {
        this.type = type;
        this.date = date;
        this.name = name;
        this.estimatedValue = estimatedValue;
        this.startingBid = startingBid;
    }

    public byte[] getImageByte() {
        return imageByte;
    }

    public void setImageByte(byte[] imageByte) {
        this.imageByte = imageByte;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getEstimatedValue() {
        return estimatedValue;
    }

    public void setEstimatedValue(Double estimatedValue) {
        this.estimatedValue = estimatedValue;
    }

    public Double getStartingBid() {
        return startingBid;
    }

    public void setStartingBid(Double startingBid) {
        this.startingBid = startingBid;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    @Override
    public String toString() {
        return "Art{" +
                "type='" + type + '\'' +
                ", date='" + date + '\'' +
                ", name='" + name + '\'' +
                ", estimatedValue=" + estimatedValue +
                ", startingBid=" + startingBid +
                '}';
    }
}
