package com.example.project.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.Modelo.Song;
import com.example.project.R;
import com.example.project.SongDetailActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SearchSongAdapter extends RecyclerView.Adapter<SearchSongAdapter.SearchSongViewHolder> {

    private List<Song> songs;
    private Context context;

    public SearchSongAdapter(List<Song> songs,Context context) {
        this.context = context;
        this.songs = songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SearchSongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_song, parent, false);
        return new SearchSongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchSongViewHolder holder, int position) {
        Song song = songs.get(position);
        holder.bind(song);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, SongDetailActivity.class);
            intent.putExtra("song", song);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public static class SearchSongViewHolder extends RecyclerView.ViewHolder {
        TextView title, artist;
        ImageView image;

        public SearchSongViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.song_name);
            artist = itemView.findViewById(R.id.artist_name);
            image = itemView.findViewById(R.id.artwork);
        }

        public void bind(Song song) {
            title.setText(song.getTitle());
            artist.setText(song.getArtist());
            Picasso.get().load(song.getImageUrl()).into(image);
        }
    }
}