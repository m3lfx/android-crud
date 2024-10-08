package com.example.itemcrud2023;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListItemActivity extends AppCompatActivity {
//    private static final String urlString = "http://172.34.97.101:4000/api/item";
    private static final String urlString = "http://172.34.95.235:8000/api/items";

    private List<Item> item_list;
    private RecyclerView rv;
    private myAdapter adapter;
    Context mContext;
    private SwipeRefreshLayout swipeContainer;
    private SwipeAdapter swipeAdapter;
    private URL myURL;

    private String accessToken;
    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        mContext = getApplicationContext();
        rv = (RecyclerView) findViewById(R.id.itemrecyclerview);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        item_list = new ArrayList<>();
        swipeContainer = findViewById(R.id.swipeContainer);
        getItemData();
        swipeContainer.setOnRefreshListener(() -> {
            getItemData();
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    private void getItemData() {
        item_list.clear();
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET,
                urlString, null, response -> {
            try {

                JSONArray array = response.getJSONArray("data");
                Log.i("data", array.toString());
                for (int i = 0; i < array.length(); i++) {
                    JSONObject ob = array.getJSONObject(i);
                    Item listData = new Item(ob.getString("description")
                            , ob.getString("sell_price"),
                            ob.getString("cost_price"),
                            ob.getString("img_path"),
                            ob.getInt("item_id"));
                    item_list.add(listData);
                }
//                adapter = new myAdapter(mContext, item_list);
                swipeAdapter = new SwipeAdapter(mContext, item_list);
                rv.setAdapter(adapter);
                Intent i=getIntent();
                 accessToken = i.getStringExtra("access_token");
//                swipeAdapter = new SwipeAdapter(mContext, item_list, accessToken);
                swipeAdapter = new SwipeAdapter(mContext, item_list);
                rv.setAdapter(swipeAdapter);

                swipeContainer.setRefreshing(false);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> Log.e("error :", "cannot get items"))
        {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                Intent i=getIntent();
                accessToken=i.getStringExtra("access_token");
                params.put("Authorization", "Bearer "+ accessToken);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(jsonRequest);
    }


}