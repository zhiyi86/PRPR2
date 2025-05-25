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
import com.example.project.API.SongApiService;
import com.example.project.Adapter.SongAdapter;
import com.example.project.Modelo.Song;
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
            int itemId = item.getItemId();

            if (itemId == R.id.nav_search) {
                startActivity(new Intent(AlbumSongsActivity.this, MainActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.nav_guess) {
                Intent intent = new Intent(AlbumSongsActivity.this, GuessSongActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.nav_favorites) {
                startActivity(new Intent(AlbumSongsActivity.this, FavoritesActivity.class));
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
        SongApiService.fetchSongsByAlbumId(this, albumId, songs -> {
            songList.addAll(songs);
            adapter.notifyDataSetChanged();
        }, () -> {
            Log.e("AlbumSongsActivity", "Error");
        });
    }
}