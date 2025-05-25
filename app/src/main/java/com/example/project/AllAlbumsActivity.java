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
import com.example.project.Adapter.AlbumAdapter;
import com.example.project.Modelo.Album;
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
            int itemId = item.getItemId();

            if (itemId == R.id.nav_search) {
                startActivity(new Intent(AllAlbumsActivity.this, MainActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.nav_guess) {
                Intent intent = new Intent(AllAlbumsActivity.this, GuessSongActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.nav_favorites) {
                startActivity(new Intent(AllAlbumsActivity.this, FavoritesActivity.class));
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

        SongApiService.fetchAlbums(this, currentPage, newAlbums -> {
            albumList.addAll(newAlbums);
            adapter.notifyDataSetChanged();
            isLoading = false;
        }, () -> {
            currentPage--;
            isLoading = false;
        });
    }
}