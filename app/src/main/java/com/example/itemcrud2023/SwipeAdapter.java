package com.example.itemcrud2023;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SwipeAdapter extends RecyclerView.Adapter<SwipeAdapter.SwipeViewHolder>{
    private Context mContext;
    private List<Item> list_data;
    private String accessToken;

    private String urlString ="http://192.168.1.11:4000/api" ;
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
//
//    public SwipeAdapter(Context mContext,List<Item> list_data,String accessToken) {
         public SwipeAdapter(Context mContext,List<Item> list_data) {
        this.list_data = list_data;
        this.mContext = mContext;
//        this.accessToken = accessToken;
//        viewBinderHelper.setOpenOnlyOne(true);
    }
    @Override
    public SwipeViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_swipe, viewGroup, false);
        return new SwipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SwipeViewHolder swipeViewHolder, int i) {
        viewBinderHelper.setOpenOnlyOne(true);
        viewBinderHelper.bind(swipeViewHolder.swipelayout, String.valueOf(list_data.get(i).getDescription()));
        viewBinderHelper.closeLayout(String.valueOf(list_data.get(i).getDescription()));
        swipeViewHolder.bindData(list_data.get(i));


    }
    @Override
    public int getItemCount() {
        return list_data.size();
    }
    @Override
    public long getItemId(int position) {
        return list_data.get(position).getId();
    }

    public class SwipeViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;
        private TextView txtEdit;
        private TextView txtDelete;
        private TextView txtsell_price;

        private String urlPhoto;

        private EditText cost;
        private EditText sell;
        ImageView imageView;
        private AlertDialog.Builder builder;
        private SwipeRevealLayout swipelayout;

        SwipeViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.textView);
            txtEdit = itemView.findViewById(R.id.txtEdit);
            txtDelete = itemView.findViewById(R.id.txtDelete);
            txtsell_price = itemView.findViewById(R.id.txtsell_price);
            imageView = (ImageView)itemView.findViewById(R.id.imageView);
            swipelayout = itemView.findViewById(R.id.swipelayout);

            urlPhoto = "http://192.168.1.11:4000/storage/" ;
            txtEdit.setOnClickListener(view -> {
                Log.i("edit",urlString + "/item/"+ list_data.get(getBindingAdapterPosition()).getId()+"/edit");
                Toast.makeText(mContext, "edit", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext());
                final View dialoglayout =  LayoutInflater.from(view.getRootView().getContext()).inflate(R.layout.fragment_item, null);
                final EditText description = (EditText) dialoglayout.findViewById(R.id.item_description);
                final EditText cost_price = (EditText) dialoglayout.findViewById(R.id.item_costprice);
                final EditText sell_price = (EditText) dialoglayout.findViewById(R.id.item_sellprice);
                RequestQueue requestQueue = Volley.newRequestQueue(mContext);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                        Request.Method.GET,
                        urlString + "/item/"+ list_data.get(getBindingAdapterPosition()).getId()+"/edit",
                        null,
                        response -> {
                            try{
                                Log.i("item:", response.toString());
                                String desc = response.getString("description");
                                String item_cost = response.getString("cost_price");
                                String item_sell = response.getString("sell_price");
                                description.setText(desc);
                                cost_price.setText(item_cost);
                                sell_price.setText(item_sell);
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        },
                        error -> {
                            // Do something when error occurred
                            Log.e("error :",error.getMessage());
                        })
                        {
                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("Authorization", "Bearer "+ accessToken);
                        return params;
                    }
                };
                // Add JsonObjectRequest to the RequestQueue
                requestQueue.add(jsonObjectRequest);
                builder.setView(dialoglayout)
                        .setTitle("Edit Item")
                        .setNegativeButton("cancel", (dialogInterface, i) -> {
                        })
                        .setPositiveButton("ok", (dialogInterface, i) -> {

                            JSONObject jsonItem = new JSONObject();
                            try {
                                jsonItem.put("description", description.getText());
                                jsonItem.put("sell_price", sell_price.getText());
                                jsonItem.put("cost_price", cost_price.getText());

                                Log.d("tag", jsonItem.toString(4));
                            }catch (JSONException e) {
                                e.printStackTrace();
                            }
                            // Initialize a new RequestQueue instance
                            RequestQueue requestQueue1 = Volley.newRequestQueue(mContext);
                            // Initialize a new JsonObjectRequest instance
                            JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(
                                    Request.Method.PUT,
                                    urlString +"/item/"+list_data.get(getBindingAdapterPosition()).getId(),
                                    jsonItem,
                                    response -> {
                                        try{
                                            String description1 = response.getString("description");
                                            String item_cost = response.getString("cost_price");
                                            String item_sell = response.getString("sell_price");
                                            list_data.get(getBindingAdapterPosition()).setDescription(description1);
                                            list_data.get(getBindingAdapterPosition()).setSell_price(item_sell);
                                            list_data.get(getBindingAdapterPosition()).setCost_price(item_cost);
                                            notifyItemChanged(getBindingAdapterPosition());
                                            notifyDataSetChanged();
                                            Log.i("item" , "data"+list_data);
                                            Toast.makeText(mContext,"Item Updated", Toast.LENGTH_LONG).show();
                                        }catch (JSONException e){
                                            e.printStackTrace();
                                        }
                                    },
                                    error -> {
                                        // Do something when error occurred
                                        Log.e("error :","update failed");
                                    });
//                                    {
//                                        @Override
//                                        public Map<String, String> getHeaders() {
//                                            Map<String, String> params = new HashMap<String, String>();
//                                            params.put("Authorization", "Bearer " + accessToken);
//                                            return params;
//                                        }
//                                    };

                            // Add JsonObjectRequest to the RequestQueue
                            requestQueue1.add(jsonObjectRequest1);
                        });
                AlertDialog dialog= builder.create();
                dialog.show();
            });
            txtDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("urlstring","view" + urlString);
                    builder = new AlertDialog.Builder(view.getRootView().getContext());
                    builder.setMessage("Do you want to delete this item ?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    String deleteString = urlString +"/item/" + list_data.get(getBindingAdapterPosition()).getId();
                                    Log.i("urlstring","view" + deleteString);
                                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                                            Request.Method.DELETE,
                                            deleteString,
                                            null,
                                            response -> {
                                                // Do something with response
                                                // Process the JSON
                                                try{
                                                    String status = response.getString("status");
                                                    Toast.makeText(mContext,"Item Deleted", Toast.LENGTH_LONG).show();
                                                }catch (JSONException e){
                                                    e.printStackTrace();
                                                }
                                            },
                                            error -> {
                                                // Do something when error occurred
                                                Log.e("error :","json");
                                            })
                                    {
                                        @Override
                                        public Map<String, String> getHeaders() throws AuthFailureError {
                                            Map<String, String> params = new HashMap<String, String>();
                                            params.put("Authorization", "Bearer " + accessToken);
                                            return params;
                                        }
                                    };
                                    jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                                            0,
                                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                    RequestQueue requestQueue = Volley.newRequestQueue(mContext);
                                    // Add JsonObjectRequest to the RequestQueue
                                    requestQueue.add(jsonObjectRequest);
                                    list_data.remove(getBindingAdapterPosition());
                                    notifyItemRemoved(getBindingAdapterPosition());
//                                    notifyDataSetChanged();
                                    notifyItemRangeChanged(getBindingAdapterPosition(), list_data.size());
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //  Action for 'NO' Button
                                    dialog.cancel();
                                    Toast.makeText(mContext,"you choose no action for alertbox",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                    AlertDialog alert = builder.create();
                    //Setting the title manually
                    alert.setTitle("Delete Item");
                    alert.show();

                }
            });
        }

        void bindData(final Item list_data) {
            txtsell_price.setText(list_data.getSell_price());
            textView.setText(list_data.getDescription());
            if (list_data.getImage().isEmpty()) {
                imageView.setImageResource(R.drawable.ic_launcher_background);
            } else{
                Picasso.get().load(urlPhoto + list_data.getImage()).into(imageView);
            }

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(mContext,DetailView.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("name",list_data.getDescription());
                    intent.putExtra("imageurl",urlPhoto + list_data.getImage());
//
                    mContext.startActivity(intent);
                }
            });
        }
    }
}
