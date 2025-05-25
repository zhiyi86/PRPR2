package com.example.project;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.project.Adapter.SongAdapter;
import com.example.project.Modelo.Song;
import com.example.project.Service.FavoritesManager;

import java.util.List;

public class FavoritesFragment extends Fragment {

    private RecyclerView recyclerView;
    private SongAdapter adapter;

    public FavoritesFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        recyclerView = view.findViewById(R.id.recycler_favorites);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Song> favorites = FavoritesManager.getInstance(getContext()).getFavoriteSongs();
        adapter = new SongAdapter(favorites, getContext());
        recyclerView.setAdapter(adapter);

        return view;
    }
}
