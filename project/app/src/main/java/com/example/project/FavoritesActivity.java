package com.example.project;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FavoritesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SongAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        recyclerView = findViewById(R.id.recycler_favorites);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Song> favoriteSongs = FavoritesManager.getInstance(this).getFavoriteSongs();

        adapter = new SongAdapter(favoriteSongs, this);
        recyclerView.setAdapter(adapter);
    }
}

