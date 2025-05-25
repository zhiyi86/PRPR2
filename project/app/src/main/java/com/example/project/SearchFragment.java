package com.example.project;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.Instant;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {
    private SearchView searchView;
    private LinearLayout songsContainer, albumsContainer;
    private Button btnViewAllSongs, btnViewAllAlbums;
    private String searchQuery = "music";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        searchView = view.findViewById(R.id.search_view);
        songsContainer = view.findViewById(R.id.songs_container);
        albumsContainer = view.findViewById(R.id.albums_container);
        btnViewAllSongs = view.findViewById(R.id.btn_view_all_songs);
        btnViewAllAlbums = view.findViewById(R.id.btn_view_all_albums);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                performSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        btnViewAllSongs.setOnClickListener(v -> {
            if (!searchQuery.isEmpty()) {
                Intent intent = new Intent(getActivity(), AllSongsActivity.class);
                intent.putExtra("search_query", searchQuery);
                startActivity(intent);
                getActivity().finish();
            } else {
                Toast.makeText(getContext(), "Introduce un término de búsqueda primero", Toast.LENGTH_SHORT).show();
            }
        });

        btnViewAllAlbums.setOnClickListener(v -> {
            if (!searchQuery.isEmpty()) {
                Intent intent = new Intent(getActivity(), AllAlbumsActivity.class);
                intent.putExtra("search_query", searchQuery);
                startActivity(intent);
            } else {
                Toast.makeText(getContext(), "Introduce un término de búsqueda primero", Toast.LENGTH_SHORT).show();
            }
        });

        performSearch("music");

        return view;
    }

    private void performSearch(String query) {
        songsContainer.removeAllViews();
        albumsContainer.removeAllViews();

        String urlSongs = "https://itunes.apple.com/search?term=" + query +
                "&media=music&entity=song&limit=15";

        String urlAlbums = "https://itunes.apple.com/search?term=" + query +
                "&media=music&entity=album&limit=15";

        JsonObjectRequest requestSongs = new JsonObjectRequest(Request.Method.GET, urlSongs, null,
                response -> {
                    try {
                        JSONArray results = response.getJSONArray("results");
                        for (int i = 0; i < results.length(); i++) {
                            JSONObject song = results.getJSONObject(i);
                            addSongCard(song);
                        }
                    } catch (JSONException e) {
                        Log.e("API_ERROR", "Error procesando canciones", e);
                    }
                },
                error -> Log.e("API_ERROR", "Error en canciones: " + error.getMessage()));

        JsonObjectRequest requestAlbums = new JsonObjectRequest(Request.Method.GET, urlAlbums, null,
                response -> {
                    try {
                        JSONArray results = response.getJSONArray("results");
                        for (int i = 0; i < results.length(); i++) {
                            JSONObject album = results.getJSONObject(i);
                            addAlbumCard(album);
                        }
                    } catch (JSONException e) {
                        Log.e("API_ERROR", "Error procesando álbumes", e);
                    }
                },
                error -> Log.e("API_ERROR", "Error en álbumes: " + error.getMessage()));

        Volley.newRequestQueue(getContext()).add(requestSongs);
        Volley.newRequestQueue(getContext()).add(requestAlbums);
    }

    private void addSongCard(JSONObject song) throws JSONException {
        View card = LayoutInflater.from(getContext())
                .inflate(R.layout.card_song, songsContainer, false);

        TextView songName = card.findViewById(R.id.song_name);
        TextView artistName = card.findViewById(R.id.artist_name);
        ImageView artwork = card.findViewById(R.id.artwork);

        songName.setText(song.getString("trackName"));
        artistName.setText(song.getString("artistName"));
        Picasso.get().load(song.getString("artworkUrl100"))
                .into(artwork);

        songsContainer.addView(card);
    }

    private void addAlbumCard(JSONObject album) throws JSONException {
        View card = LayoutInflater.from(getContext())
                .inflate(R.layout.card_album, albumsContainer, false);

        TextView albumName = card.findViewById(R.id.album_name);
        TextView artistName = card.findViewById(R.id.artist_name);
        TextView albumPrice = card.findViewById(R.id.album_price);
        ImageView artwork = card.findViewById(R.id.artwork);

        albumName.setText(album.getString("collectionName"));
        artistName.setText(album.getString("artistName"));
        Log.d("API_RESPONSE", album.getString("collectionName"));
        double price = album.getDouble("collectionPrice");
        String currency = album.getString("currency");
        albumPrice.setText(String.format(Locale.getDefault(), "%.2f %s", price, currency));

        Picasso.get().load(album.getString("artworkUrl100"))
                .into(artwork);

        albumsContainer.addView(card);
    }
}