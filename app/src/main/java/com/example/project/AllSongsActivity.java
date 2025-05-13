package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
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

public class AllSongsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SongAdapter adapter;
    private List<Song> songList = new ArrayList<>();
    private String searchQuery;
    private int currentPage = 0;
    private boolean isLoading = false;

    private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_songs);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_search) {
                // Regresa a MainActivity
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish(); // Cierra AllSongsActivity
                return true;
            }
            return false;
        });
        searchQuery = getIntent().getStringExtra("search_query");

        recyclerView = findViewById(R.id.recycler_view_songs);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SongAdapter(songList, this);
        recyclerView.setAdapter(adapter);

        // Cargar primera página
        loadMoreSongs();

        // Listener para scroll para paginacion
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if (!isLoading && (visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0) {
                    loadMoreSongs();
                }
            }
        });
    }

    private void loadMoreSongs() {
        isLoading = true;
        currentPage++;

        String url = "https://itunes.apple.com/search?term=" + searchQuery +
                "&media=music&entity=song&limit=15&offset=" + (currentPage * 15);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
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
                                    songJson.getString("trackPrice"),
                                    songJson.getString("primaryGenreName")
                            );
                            songList.add(song);
                        }
                        adapter.notifyDataSetChanged();
                        isLoading = false;
                    } catch (JSONException e) {
                        Log.e("API_ERROR", "Error parsing songs", e);
                        isLoading = false;
                    }
                },
                error -> {
                    Log.e("API_ERROR", "Error loading songs", error);
                    isLoading = false;
                    currentPage--; // Reintentar la misma página
                });

        Volley.newRequestQueue(this).add(request);
    }
}