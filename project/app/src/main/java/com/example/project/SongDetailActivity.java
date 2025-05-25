package com.example.project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SongDetailActivity extends AppCompatActivity {

    private ImageView imageViewSong, imageViewFavorite;
    private TextView textViewTitle, textViewArtist, textViewAlbum, textViewReleaseDate, textViewPrice, textViewGenre;
    private Song currentSong;
    private boolean isFavorite;

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_detail);

        imageViewSong = findViewById(R.id.imageViewSong);
        imageViewFavorite = findViewById(R.id.imageViewFavorite);
        textViewTitle = findViewById(R.id.textViewTitle);
        textViewArtist = findViewById(R.id.textViewArtist);
        textViewAlbum = findViewById(R.id.textViewAlbum);
        textViewReleaseDate = findViewById(R.id.textViewReleaseDate);
        textViewPrice = findViewById(R.id.textViewPrice);
        textViewGenre = findViewById(R.id.textViewGenre);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("song")) {
            currentSong = (Song) intent.getSerializableExtra("song");

            // Mostrar los datos
            assert currentSong != null;
            textViewTitle.setText(currentSong.getTitle());
            textViewArtist.setText(currentSong.getArtist());
            textViewAlbum.setText(currentSong.getAlbum());
            textViewReleaseDate.setText(currentSong.getReleaseDate());
            textViewPrice.setText(currentSong.getPrice());
            textViewGenre.setText(currentSong.getGenre());

            Glide.with(this).load(currentSong.getImageUrl()).into(imageViewSong);


            isFavorite = FavoritesManager.getInstance(this).isFavorite(currentSong.getTrackId());
            updateFavoriteIcon();

            imageViewFavorite.setOnClickListener(v -> {
                if (isFavorite) {
                    FavoritesManager.getInstance(this).removeFavorite(currentSong.getTrackId());
                } else {
                    FavoritesManager.getInstance(this).addFavorite(currentSong);
                }
                isFavorite = !isFavorite;
                updateFavoriteIcon();
            });
        }
    }

    private void updateFavoriteIcon() {
        imageViewFavorite.setImageResource(isFavorite ? R.drawable.ic_heart_filled : R.drawable.ic_heart_outline);
    }
}

