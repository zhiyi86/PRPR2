package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AlbumSongsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SongAdapter adapter;
    private List<Song> songList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_songs);
        // Configuración del BottomNavigation
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_search) {
                startActivity(new Intent(this, MainActivity.class));
                finish();
                return true;
            }
            return false;
        });

        String albumId = getIntent().getStringExtra("album_id");

        recyclerView = findViewById(R.id.recycler_view_songs);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SongAdapter(songList, this);
        recyclerView.setAdapter(adapter);

        loadAlbumSongs(albumId);
    }

    private void loadAlbumSongs(String albumId) {
        String url = "https://itunes.apple.com/lookup?id=" + albumId + "&entity=song";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray results = response.getJSONArray("results");

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
                                    songJson.getString("trackPrice"),
                                    songJson.getString("primaryGenreName")
                            );
                            songList.add(song);
                        }
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        Log.e("API_ERROR", "Error parsing album songs", e);
                    }
                },
                error -> Log.e("API_ERROR", "Error loading album songs: " + error.getMessage()));

        Volley.newRequestQueue(this).add(request);
    }
}