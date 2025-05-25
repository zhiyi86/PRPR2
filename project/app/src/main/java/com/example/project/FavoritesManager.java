package com.example.project;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FavoritesManager {

    private static final String PREFS_NAME = "favorites";
    private static final String KEY_SONGS = "favorite_songs";

    private static FavoritesManager instance;
    private SharedPreferences prefs;
    private Gson gson;
    private List<Song> favoriteSongs;

    private FavoritesManager(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
        loadFavorites();
    }

    public static FavoritesManager getInstance(Context context) {
        if (instance == null) {
            instance = new FavoritesManager(context.getApplicationContext());
        }
        return instance;
    }

    private void loadFavorites() {
        String json = prefs.getString(KEY_SONGS, "[]");
        Type type = new TypeToken<List<Song>>() {}.getType();
        favoriteSongs = gson.fromJson(json, type);
        if (favoriteSongs == null) {
            favoriteSongs = new ArrayList<>();
        }
    }

    private void saveFavorites() {
        String json = gson.toJson(favoriteSongs);
        prefs.edit().putString(KEY_SONGS, json).apply();
    }

    public void addFavorite(Song song) {
        if (!isFavorite(song.getTrackId())) {
            favoriteSongs.add(song);
            saveFavorites();
        }
    }

    public void removeFavorite(String trackId) {
        Iterator<Song> iterator = favoriteSongs.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getTrackId().equals(trackId)) { // Cambio de comparación
                iterator.remove();
                break;
            }
        }
        saveFavorites();
    }

    public boolean isFavorite(String trackId) {
        for (Song s : favoriteSongs) {
            if (s.getTrackId().equals(trackId)) return true; // Cambio de comparación
        }
        return false;
    }


    public List<Song> getFavoriteSongs() {
        return new ArrayList<>(favoriteSongs);
    }
}

