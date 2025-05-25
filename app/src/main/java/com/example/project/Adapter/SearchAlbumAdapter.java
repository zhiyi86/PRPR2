package com.example.project.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.AlbumSongsActivity;
import com.example.project.Modelo.Album;
import com.example.project.Modelo.Song;
import com.example.project.R;
import com.example.project.SongDetailActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SearchAlbumAdapter extends RecyclerView.Adapter<SearchAlbumAdapter.SearchAlbumViewHolder>{
    private List<Album> albumList;
    private Context context;

    public SearchAlbumAdapter(List<Album> albumList, Context context) {
        this.albumList = albumList;
        this.context = context;
    }

    public void setAlbums(List<Album> albums) {
        this.albumList = albums;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public SearchAlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_album, parent, false);
        return new SearchAlbumAdapter.SearchAlbumViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAlbumViewHolder holder, int position) {
        Album album = albumList.get(position);
        holder.bind(album);


        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, AlbumSongsActivity.class);
            intent.putExtra("album_id", album.getCollectionId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    static class SearchAlbumViewHolder extends RecyclerView.ViewHolder {
        TextView albumName, artistName, albumPrice;
        ImageView artwork;

        public SearchAlbumViewHolder(@NonNull View itemView) {
            super(itemView);
            albumName = itemView.findViewById(R.id.album_name);
            artistName = itemView.findViewById(R.id.artist_name);
            albumPrice = itemView.findViewById(R.id.album_price);
            artwork = itemView.findViewById(R.id.artwork);
        }
        public void bind(Album album) {
            albumName.setText(album.getCollectionName());
            artistName.setText(album.getArtistName());
            albumPrice.setText(String.format("%.2f %s", album.getPrice(), album.getCurrency()));
            Picasso.get().load(album.getArtworkUrl()).into(artwork);

        }
    }
}
