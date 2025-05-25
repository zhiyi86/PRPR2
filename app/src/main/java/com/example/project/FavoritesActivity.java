package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.project.Adapter.SongAdapter;
import com.example.project.Modelo.Song;
import com.example.project.Service.FavoritesManager;
import com.google.android.material.navigation.NavigationBarView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class FavoritesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SongAdapter adapter;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        recyclerView = findViewById(R.id.recycler_favorites);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Song> favoriteSongs = FavoritesManager.getInstance(this).getFavoriteSongs();

        adapter = new SongAdapter(favoriteSongs, this);
        recyclerView.setAdapter(adapter);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_favorites);
        bottomNavigationView.setOnItemSelectedListener(navListener);

    }
    private final NavigationBarView.OnItemSelectedListener navListener =
            new NavigationBarView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    int itemId = item.getItemId();

                    if (itemId == R.id.nav_search) {
                        startActivity(new Intent(FavoritesActivity.this, MainActivity.class));
                        finish();
                        return true;
                    } else if (itemId == R.id.nav_guess) {
                        startActivity(new Intent(FavoritesActivity.this, GuessSongActivity.class));
                        finish();
                        return true;
                    } else if (itemId == R.id.nav_favorites) {
                        return true;
                    }
                    return false;
                }
            };
}

