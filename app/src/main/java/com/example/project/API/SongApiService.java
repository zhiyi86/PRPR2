package com.example.project.API;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.project.Modelo.Album;
import com.example.project.Modelo.Song;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SongApiService {

    public static void fetchSongs(Context context, int page, Consumer<List<Song>> onSuccess, Runnable onError) {
        String url = "https://itunes.apple.com/search?term=music"
                + "&media=music&entity=song&limit=15&offset=" + (page * 15);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    List<Song> songs = new ArrayList<>();
                    try {
                        JSONArray results = response.getJSONArray("results");
                        for (int i = 0; i < results.length(); i++) {
                            JSONObject songJson = results.getJSONObject(i);
                            Song song = new Song(
                                    songJson.getString("trackId"),
                                    songJson.getString("trackName"),
                                    songJson.getString("artistName"),
                                    songJson.getString("artworkUrl100"),
                                    songJson.getString("previewUrl"),
                                    songJson.getString("collectionName"),
                                    songJson.getString("releaseDate"),
                                    songJson.optString("trackPrice", "0.00"),
                                    songJson.getString("primaryGenreName")
                            );
                            songs.add(song);
                        }
                        onSuccess.accept(songs);
                    } catch (JSONException e) {
                        Log.e("SongApiService", "JSON parsing error", e);
                        onError.run();
                    }
                },
                error -> {
                    Log.e("SongApiService", "Volley error", error);
                    onError.run();
                });

        Volley.newRequestQueue(context).add(request);
    }

    public static void fetchAlbums(Context context, int page, Consumer<List<Album>> onSuccess, Runnable onError) {
        String url = "https://itunes.apple.com/search?term=album"
                + "&media=music&entity=album&limit=15&offset=" + (page * 15);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    List<Album> albums = new ArrayList<>();
                    try {
                        JSONArray results = response.getJSONArray("results");
                        for (int i = 0; i < results.length(); i++) {
                            JSONObject albumJson = results.getJSONObject(i);
                            Album album = new Album(
                                    albumJson.getString("collectionId"),
                                    albumJson.getString("collectionName"),
                                    albumJson.getString("artistName"),
                                    albumJson.getString("artworkUrl100"),
                                    albumJson.getDouble("collectionPrice"),
                                    albumJson.getString("currency")
                            );
                            albums.add(album);
                        }
                        onSuccess.accept(albums);
                    } catch (JSONException e) {
                        Log.e("SongApiService", "JSON parsing error", e);
                        onError.run();
                    }
                },
                error -> {
                    Log.e("SongApiService", "Volley error", error);
                    onError.run();
                });

        Volley.newRequestQueue(context).add(request);
    }

    public static void fetchSongsByAlbumId(Context context, String albumId, Consumer<List<Song>> onSuccess, Runnable onError) {
        String url = "https://itunes.apple.com/lookup?id=" + albumId + "&entity=song";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    List<Song> songs = new ArrayList<>();
                    try {
                        JSONArray results = response.getJSONArray("results");
                        // Saltar el primer resultado porque es información del álbum
                        for (int i = 1; i < results.length(); i++) {
                            JSONObject songJson = results.getJSONObject(i);
                            Song song = new Song(
                                    songJson.getString("trackId"),
                                    songJson.getString("trackName"),
                                    songJson.getString("artistName"),
                                    songJson.getString("artworkUrl100"),
                                    songJson.getString("previewUrl"),
                                    songJson.getString("collectionName"),
                                    songJson.getString("releaseDate"),
                                    songJson.optString("trackPrice", "0.00"),
                                    songJson.getString("primaryGenreName")
                            );
                            songs.add(song);
                        }
                        onSuccess.accept(songs);
                    } catch (JSONException e) {
                        Log.e("SongApiService", "Error parsing songs from album", e);
                        onError.run();
                    }
                },
                error -> {
                    Log.e("SongApiService", "Volley error fetching album songs", error);
                    onError.run();
                });

        Volley.newRequestQueue(context).add(request);
    }

    public static void searchSongs(Context context, String query, Consumer<List<Song>> onSuccess, Consumer<Exception> onError) {
        String url = "https://itunes.apple.com/search?term=" + query + "&media=music&entity=song&limit=15";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    List<Song> songList = new ArrayList<>();
                    try {
                        JSONArray results = response.getJSONArray("results");
                        for (int i = 0; i < results.length(); i++) {
                            JSONObject songJson = results.getJSONObject(i);
                            Song song = new Song(
                                    songJson.getString("trackId"),
                                    songJson.getString("trackName"),
                                    songJson.getString("artistName"),
                                    songJson.getString("artworkUrl100"),
                                    songJson.getString("previewUrl"),
                                    songJson.getString("collectionName"),
                                    songJson.getString("releaseDate"),
                                    songJson.optString("trackPrice", "0.00"),
                                    songJson.getString("primaryGenreName")
                            );
                            songList.add(song);
                        }
                        onSuccess.accept(songList);
                    } catch (JSONException e) {
                        onError.accept(e);
                    }
                },
                error -> onError.accept(new Exception(error.getMessage())));

        Volley.newRequestQueue(context).add(request);
    }

    public static void searchAlbums(Context context, String query, Consumer<List<Album>> onSuccess, Consumer<Exception> onError) {
        String url = "https://itunes.apple.com/search?term=" + query + "&media=music&entity=album&limit=15";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    List<Album> albumList = new ArrayList<>();
                    try {
                        JSONArray results = response.getJSONArray("results");
                        for (int i = 0; i < results.length(); i++) {
                            JSONObject albumJson = results.getJSONObject(i);
                            Album album = new Album(
                                    albumJson.getString("collectionId"),
                                    albumJson.getString("collectionName"),
                                    albumJson.getString("artistName"),
                                    albumJson.getString("artworkUrl100"),
                                    albumJson.getDouble("collectionPrice"),
                                    albumJson.getString("currency")
                            );
                            albumList.add(album);
                        }
                        onSuccess.accept(albumList);
                    } catch (JSONException e) {
                        onError.accept(e);
                    }
                },
                error -> onError.accept(new Exception(error.getMessage())));

        Volley.newRequestQueue(context).add(request);
    }
}
