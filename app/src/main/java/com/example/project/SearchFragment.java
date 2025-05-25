package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.appcompat.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.project.API.SongApiService;
import com.example.project.Adapter.SearchAlbumAdapter;
import com.example.project.Adapter.SearchSongAdapter;
import com.example.project.Modelo.Album;
import com.example.project.Modelo.Song;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchFragment extends Fragment {
    private SearchView searchView;
    private RecyclerView songsRecyclerView;
    private RecyclerView albumsContainer;
    private Button btnViewAllSongs, btnViewAllAlbums;
    private String searchQuery = "music";
    private List<Song> songs = new ArrayList<>();
    private RecyclerView albumsRecyclerView;
    private SearchAlbumAdapter albumAdapter;
    private List<Album> albums = new ArrayList<>();
    private SearchSongAdapter songAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        searchView = view.findViewById(R.id.search_view);
        songsRecyclerView = view.findViewById(R.id.songs_container);
        albumsRecyclerView = view.findViewById(R.id.albums_container);
        btnViewAllSongs = view.findViewById(R.id.btn_view_all_songs);
        btnViewAllAlbums = view.findViewById(R.id.btn_view_all_albums);

        songAdapter = new SearchSongAdapter(songs,getContext());
        songsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        songsRecyclerView.setAdapter(songAdapter);
        albumAdapter = new SearchAlbumAdapter(albums,getContext());
        albumsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        albumsRecyclerView.setAdapter(albumAdapter);
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
                Toast.makeText(getContext(), "Err", Toast.LENGTH_SHORT).show();
            }
        });

        performSearch(searchQuery);

        return view;
    }

    private void performSearch(String query) {

        searchQuery = query;

        songAdapter.setSongs(new ArrayList<>());
        albumAdapter.setAlbums(new ArrayList<>());

        SongApiService.searchSongs(getContext(), query, resultSongs -> {
            songs.clear();
            songs.addAll(resultSongs);
            songAdapter.setSongs(songs);
        }, error -> Log.e("API_ERROR", "Error en canciones", error));


        SongApiService.searchAlbums(getContext(), query, resultAlbums -> {
            albums.clear();
            albums.addAll(resultAlbums);
            albumAdapter.setAlbums(albums);
        }, error -> Log.e("API_ERROR", "Error en albumes", error));
    }
}