package com.example.redditmeme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    String curr_url=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_main);
        //ImageView memeImage= (ImageView)findViewById(R.id.meme_image);
        loadMeme();
        CardView cardView=findViewById(R.id.next_button);
        CardView cardView1=findViewById(R.id.share_button);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextMeme();
            }
        });
        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareMeme();
            }
        });

    }
    private void loadMeme(){

        //RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://meme-api.herokuapp.com/gimme";
        ProgressBar pb = (ProgressBar) findViewById(R.id.progress);
        pb.setVisibility(ProgressBar.VISIBLE);


        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String post_url=response.getString("url");
                            curr_url=post_url;
                            ImageView memeImage= (ImageView)findViewById(R.id.meme_image);
                            RequestManager manager = Glide.with(memeImage);
                            manager.load(post_url).into(memeImage);
                            //Glide.with(this).load(post_url).into(memeImage);
                            pb.setVisibility(ProgressBar.INVISIBLE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        mySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
        // Add the request to the RequestQueue.
        //queue.add(jsonObjectRequest);
    }
    public void nextMeme(){
        loadMeme();
    }
    public void shareMeme(){
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        Uri imageUri = Uri.parse(curr_url);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, curr_url);
        startActivity(sharingIntent);
    }
}