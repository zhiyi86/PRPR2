package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
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

public class AllAlbumsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AlbumAdapter adapter;
    private List<Album> albumList = new ArrayList<>();
    private String searchQuery;
    private int currentPage = 0;
    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_albums);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_search) {
                finish();
                return true;
            }
            return false;
        });

        searchQuery = getIntent().getStringExtra("search_query");

        recyclerView = findViewById(R.id.recycler_view_albums);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AlbumAdapter(albumList, album -> {
            // Click en álbum - abrir actividad con sus canciones
            Intent intent = new Intent(this, AlbumSongsActivity.class);
            intent.putExtra("album_id", album.getCollectionId());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        // Scroll listener para paginación
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
                    loadMoreAlbums();
                }
            }
        });

        loadMoreAlbums();
    }

    private void loadMoreAlbums() {
        isLoading = true;
        currentPage++;

        String url = "https://itunes.apple.com/search?term=" + searchQuery +
                "&media=music&entity=album&limit=15&offset=" + (currentPage * 15);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
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
                        adapter.notifyDataSetChanged();
                        isLoading = false;
                    } catch (JSONException e) {
                        Log.e("API_ERROR", "Error parsing albums", e);
                        isLoading = false;
                        currentPage--;
                    }
                },
                error -> {
                    Log.e("API_ERROR", "Error loading albums: " + error.getMessage());
                    isLoading = false;
                    currentPage--;
                });

        Volley.newRequestQueue(this).add(request);
    }
}