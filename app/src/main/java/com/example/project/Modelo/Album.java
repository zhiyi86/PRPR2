package com.example.project.Modelo;

import java.io.Serializable;

public class Album implements Serializable {
    private String collectionId;
    private String collectionName;
    private String artistName;
    private String artworkUrl;
    private double price;
    private String currency;

    public Album(String collectionId, String collectionName, String artistName,
                 String artworkUrl, double price, String currency) {
        this.collectionId = collectionId;
        this.collectionName = collectionName;
        this.artistName = artistName;
        this.artworkUrl = artworkUrl;
        this.price = price;
        this.currency = currency;
    }


    public String getCollectionId() { return collectionId; }
    public String getCollectionName() { return collectionName; }
    public String getArtistName() { return artistName; }
    public String getArtworkUrl() { return artworkUrl; }
    public double getPrice() { return price; }
    public String getCurrency() { return currency; }
}