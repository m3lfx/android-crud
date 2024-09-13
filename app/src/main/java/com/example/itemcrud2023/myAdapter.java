package com.example.itemcrud2023;

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

import com.squareup.picasso.Picasso;

import java.util.List;

public class myAdapter extends RecyclerView.Adapter<myAdapter.ViewHolder> {
    private Context mContext;
    private List<Item> list_data;
    private static final String urlString ="http://172.34.97.101:4000/storage/" ;

    public myAdapter(Context mContext,List<Item> list_data) {
        this.list_data = list_data;
        this.mContext = mContext;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.list_data,parent,false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Item listData = this.list_data.get(position);
        Log.i("image",urlString + listData.getImage());
        if (listData.getImage().isEmpty()) {
            holder.imageView.setImageResource(R.drawable.ic_launcher_background);
        } else{
            Picasso.get().load(urlString + listData.getImage()).into(holder.imageView);
        }
        holder.txtdescription.setText(listData.getDescription());
        holder.txtsell_price.setText(listData.getSell_price());

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(mContext, DetailView.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("name",listData.getDescription());
                intent.putExtra("imageurl",urlString + listData.getImage());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list_data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtdescription, txtsell_price;
        private ImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            txtdescription = (TextView) itemView.findViewById(R.id.txt_description);
            txtsell_price =  (TextView) itemView.findViewById(R.id.txt_sell_price);
             imageView = (ImageView) itemView.findViewById(R.id.imageView);
        }
    }
}
