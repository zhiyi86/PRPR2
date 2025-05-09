package com.example.meme;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView memeRecyclerView;
    private RecyclerView.Adapter adapter;
    private Button btCreate;

    private List memesArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        memeRecyclerView = findViewById(R.id.recycler_memes);
        memeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        makeRequest();
    }

    private void makeRequest() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://api.imgflip.com/get_memes";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray res = null;
                        try {
                            res = response.getJSONObject("data").getJSONArray("memes");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        memesArray = new Gson().fromJson(String.valueOf(res),new TypeToken<List<Meme>>(){}.getType());
                        updateUI();
                        Log.e("resposta", "La resposta es: "+ response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("resposta", "Hi ha hagut un error:" + error);
                    }
                }
                );

        queue.add(jsonObjectRequest);
    }
    private void updateUI() {
        adapter = new PokemonAdapter( (List) memesArray, this);

        memeRecyclerView.setAdapter(adapter);
    }

    public class MemeHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private Meme meme;
        private TextView tvNom;
        private TextView tvAvistament;
        private ImageView image;

        public MemeHolder(LayoutInflater inflater, ViewGroup parent, Activity
                activity) {
            super(inflater.inflate(R.layout.meme_item, parent, false));
            tvNom = (TextView) itemView.findViewById(R.id.nom);
            image = (ImageView)itemView.findViewById(R.id.ivmeme);
            itemView.setOnClickListener(this);
        }

        public void bind(Meme meme) {
            this.meme = meme;
            tvNom.setText(meme.getName());

            Picasso.get().load(meme.getUrl()).into(image);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(MainActivity.this,
                            meme.getName() + " apretat!", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    public class PokemonAdapter extends RecyclerView.Adapter<MemeHolder> {
        private List memes;
        private Activity activity;
        public PokemonAdapter(List memes, Activity activity) {
            this.memes = memes;
            this.activity = activity;
        }
        @Override
        public MemeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(activity);
            return new MemeHolder(layoutInflater, parent, activity);
        }
        @Override
        public void onBindViewHolder(MemeHolder holder, int position) {

            Meme meme = (Meme) memes.get(position);
            holder.bind(meme);

        }
        @Override
        public int getItemCount() {
            return memes.size();
        }
    }

}