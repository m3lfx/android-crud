package com.example.itemcrud2023;

import androidx.appcompat.app.AppCompatActivity;

//import android.app.Activity;
import android.content.Context;
//import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;


import com.android.volley.Request;
import com.android.volley.RequestQueue;

import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;


public class MainActivity extends AppCompatActivity {
    private Context mContext;
    private final String mJSONURLString = "http://192.168.1.11:8000/api/item/";
    private final String imgUrl = "http://192.168.1.11:8000/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();

        Button btnSearch = findViewById(R.id.search);

        EditText desc = findViewById(R.id.description);
        EditText cost = findViewById(R.id.cost);
        EditText sell = findViewById(R.id.sell);
        EditText itemId = findViewById(R.id.item_no);

        ImageView imageView  =  findViewById(R.id.imageView);
        btnSearch.setOnClickListener(view -> {

            String urlString = mJSONURLString+itemId.getText();
            Log.i("url","url"+ urlString);

            // Initialize a new RequestQueue instance
            RequestQueue requestQueue = Volley.newRequestQueue(mContext);

            // Initialize a new JsonObjectRequest instance
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    urlString,
                    null,
                    response -> {
                        Log.i("json",String.valueOf(response));
                        try{
                            String description = response.getString("description");
                            String item_cost = response.getString("cost_price");
                            String item_sell = response.getString("sell_price");
                            String image_url = imgUrl + response.getString("img_path");

                            // Display the formatted json data in text view
                            desc.setText(description);
                            cost.setText(item_cost);
                            sell.setText(item_sell);
                            Picasso.get()
                                    .load(image_url)
                                    .into(imageView);
                            //  }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    },
                    error -> {
                        // Do something when error occurred
                        Log.e("error :",error.getMessage());
                    });

            // Add JsonObjectRequest to the RequestQueue
            requestQueue.add(jsonObjectRequest);
        });
    }
}