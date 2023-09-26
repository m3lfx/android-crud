package com.example.itemcrud2023;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailView extends AppCompatActivity {
    private TextView nametxt;
    private ImageView fullimg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);
        nametxt=(TextView)findViewById(R.id.name_txt);
        fullimg=(ImageView)findViewById(R.id.full_image);
        Intent i=getIntent();
        String name=i.getStringExtra("name");
        String imageurl=i.getStringExtra("imageurl");
        nametxt.setText(name);
        Picasso.get()
                .load(imageurl)
                .into(fullimg);
        Log.i("url","test"+imageurl);

    }
}