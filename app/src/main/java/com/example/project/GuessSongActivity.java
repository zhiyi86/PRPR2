package com.example.project;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project.Modelo.Song;
import com.example.project.Service.FavoritesManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class GuessSongActivity extends AppCompatActivity {

    private List<Song> favoriteSongs;
    private int currentIndex = 0;
    private int attempts = 0;
    private int guessedCont = 0;

    private MediaPlayer mediaPlayer;

    private ImageView buttonPlay;
    private Button buttonSubmit;
    private EditText editGuess;
    private TextView textProgress, textFeedback;

    private boolean songEnded = true;
    private boolean isPrepared = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess_song);

        buttonPlay = findViewById(R.id.button_play);
        buttonSubmit = findViewById(R.id.button_submit);
        editGuess = findViewById(R.id.edit_guess);
        textProgress = findViewById(R.id.text_progress);
        textFeedback = findViewById(R.id.text_feedback);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_guess);
        favoriteSongs = FavoritesManager.getInstance(this).getFavoriteSongs();
        Collections.shuffle(favoriteSongs); // aleatori

        updateProgressText();

        buttonPlay.setOnClickListener(v -> playCurrentSong());
        buttonSubmit.setOnClickListener(v -> checkGuess());

        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_search) {
                startActivity(new Intent(GuessSongActivity.this, MainActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.nav_guess) {

            } else if (itemId == R.id.nav_favorites) {
                startActivity(new Intent(GuessSongActivity.this, FavoritesActivity.class));
                finish();
                return true;
            }
            return false;
        });
    }

    private void playCurrentSong() {
        if (isPrepared && mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            } else {
                mediaPlayer.seekTo(0);
                mediaPlayer.start();
            }
            return;
        }

        if (!songEnded) return;

        if (currentIndex >= favoriteSongs.size()) {
            textFeedback.setText("Has acabat totes les cançons!");
            buttonPlay.setEnabled(false);
            buttonSubmit.setEnabled(false);
            return;
        }

        Song song = favoriteSongs.get(currentIndex);

        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
            isPrepared = false;
        }

        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(song.getPreviewUrl());
            mediaPlayer.setOnPreparedListener(mp -> {
                isPrepared = true;
                mp.start();
                songEnded = false;
                attempts = 0;
                textFeedback.setText("");
                editGuess.setText("");
                buttonSubmit.setEnabled(true);
            });

            mediaPlayer.setOnCompletionListener(mp -> {
                songEnded = true;
            });

            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
            textFeedback.setText("Error al carregar la cançó.");
        }
    }

    private void checkGuess() {
        if (currentIndex >= favoriteSongs.size() || songEnded) return;

        String guess = editGuess.getText().toString().trim();
        String correctTitle = favoriteSongs.get(currentIndex).getTitle().trim();

        attempts++;

        if (guess.equalsIgnoreCase(correctTitle)) {
            guessedCont++;
            textFeedback.setText("Correcte!");
            finishCurrentSong();
        } else if (attempts >= 3) {
            textFeedback.setText("Incorrecte! Era: " + correctTitle);
            finishCurrentSong();
        } else {
            textFeedback.setText("Intenta-ho de nou (" + (3 - attempts) + " intents restants)");
        }

        updateProgressText();
    }

    private void finishCurrentSong() {
        songEnded = true;
        buttonSubmit.setEnabled(false);
        currentIndex++;
        attempts = 0;
        isPrepared = false;
        updateProgressText();
    }

    private void updateProgressText() {
        int remainingSongs = Math.max(0, favoriteSongs.size() - currentIndex);

        if (remainingSongs == 0 && favoriteSongs.size() > 0) {
            textProgress.setText("Has encertat " + guessedCont + " de " + favoriteSongs.size() + " cançons!");
            textFeedback.setText("Enhorabona! Has completat totes les cançons.");
        } else {
            textProgress.setText(guessedCont + "/" + favoriteSongs.size() + " cançons (" + remainingSongs + " restants)");
        }
    }

    @Override
    protected void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
    }
}
