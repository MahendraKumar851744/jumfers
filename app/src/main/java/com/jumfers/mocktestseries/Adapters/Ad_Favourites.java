package com.jumfers.mocktestseries.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jumfers.mocktestseries.FavItemClickListener;
import com.jumfers.mocktestseries.databases.Favourites.FavouriteModel;

import com.jumfers.mocktestseries.R;

import java.util.ArrayList;

public class Ad_Favourites extends RecyclerView.Adapter<Ad_Favourites.CategoryViewHolder> {

    Context context;
    ArrayList<FavouriteModel> items;

    FavItemClickListener itemClickListener;

    public Ad_Favourites(Context context, ArrayList<FavouriteModel> items,FavItemClickListener itemClickListener ) {
        this.context = context;
        this.items = items;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryViewHolder(LayoutInflater.from(context).inflate(R.layout.lay_sub_category_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.tv_title.setText(items.get(position).getTitle());
        if(Integer.parseInt(items.get(position).getCategory_id()) == 1){
            holder.iv_icon.setImageResource(R.drawable.railway);
        }else if(Integer.parseInt(items.get(position).getCategory_id()) == 2){
            holder.iv_icon.setImageResource(R.drawable.ssc);
        }else if(Integer.parseInt(items.get(position).getCategory_id()) == 3){
            holder.iv_icon.setImageResource(R.drawable.law);
        }else{
            holder.iv_icon.setImageResource(R.drawable.civil);
        }
        holder.itemView.setOnClickListener(v->{
            itemClickListener.onClick(position,items.get(position));
        });
        holder.iv_fav.setImageResource(R.drawable.fav_colored);



    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title;
        ImageView iv_icon,iv_fav;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_icon = itemView.findViewById(R.id.iv_icon);
            tv_title = itemView.findViewById(R.id.tv_title);
            iv_fav = itemView.findViewById(R.id.iv_fav);
        }
    }

}
