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
import com.example.project.API.SongApiService;
import com.example.project.Adapter.SongAdapter;
import com.example.project.Modelo.Song;
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
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_search) {
                startActivity(new Intent(AllSongsActivity.this, MainActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.nav_guess) {
                Intent intent = new Intent(AllSongsActivity.this, GuessSongActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.nav_favorites) {
                startActivity(new Intent(AllSongsActivity.this, FavoritesActivity.class));
                finish();
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

        SongApiService.fetchSongs(this, currentPage, newSongs -> {
            songList.addAll(newSongs);
            adapter.notifyDataSetChanged();
            isLoading = false;
        }, () -> {
            currentPage--; // Si falla, retrocedemos la página
            isLoading = false;
        });
    }
}