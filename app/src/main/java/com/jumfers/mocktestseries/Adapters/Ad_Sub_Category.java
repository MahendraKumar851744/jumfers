package com.jumfers.mocktestseries.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jumfers.mocktestseries.databases.Favourites.Fav_database;
import com.jumfers.mocktestseries.databases.Favourites.FavouriteModel;
import com.jumfers.mocktestseries.databases.Favourites.fav_dao;
import com.jumfers.mocktestseries.ItemClickListener;
import com.jumfers.mocktestseries.databases.SubCategories.SubCategoryItem;
import com.jumfers.mocktestseries.R;

import java.util.ArrayList;

public class Ad_Sub_Category extends RecyclerView.Adapter<Ad_Sub_Category.CategoryViewHolder> {

    Context context;
    ArrayList<SubCategoryItem> items;

    ItemClickListener itemClickListener;

    public Ad_Sub_Category(Context context, ArrayList<SubCategoryItem> items, ItemClickListener itemClickListener ) {
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
        int category_id = items.get(position).getCategory();
        int item_position = position;
        Fav_database fav_database = Fav_database.getDbInstance(context);
        fav_dao favDao = fav_database.dao();
        for(FavouriteModel model:favDao.getAllQuestions()){
            if(model.getCategory_id().matches(String.valueOf(category_id)) && model.getItem_position().matches(String.valueOf(item_position))){
                holder.iv_fav.setImageResource(R.drawable.fav_colored);
            }
        }


        holder.tv_title.setText(items.get(position).getTitle());
        if(items.get(position).getCategory() == 1){
            holder.iv_icon.setImageResource(R.drawable.railway);
        }else if(items.get(position).getCategory() == 2){
            holder.iv_icon.setImageResource(R.drawable.ssc);
        }else if(items.get(position).getCategory() == 3){
            holder.iv_icon.setImageResource(R.drawable.law);
        }else{
            holder.iv_icon.setImageResource(R.drawable.civil);
        }

        holder.itemView.setOnClickListener(v->{
            SharedPreferences sp = context.getSharedPreferences("BASEAPP",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putInt("SUBCATEGORY_ID",items.get(position).getId());
            editor.putString("SUBCATEGORY_NAME",items.get(position).getTitle());
            editor.apply();
            itemClickListener.onClick(position,items.get(position).getId());
        });

        holder.iv_fav.setOnClickListener(v->{
            int flag = 0;
            for(FavouriteModel model:favDao.getAllQuestions()){
                if(model.getCategory_id().matches(String.valueOf(category_id)) && model.getItem_position().matches(String.valueOf(item_position))){
                    flag = 1;
                    favDao.delete(model);
                    holder.iv_fav.setImageResource(R.drawable.favourite);
                    break;
                }
            }
            if(flag == 0){
                holder.iv_fav.setImageResource(R.drawable.fav_colored);
                SharedPreferences sp = context.getSharedPreferences("BASEAPP",Context.MODE_PRIVATE);
                String title = sp.getString("CATEGORY_NAME","");
                favDao.insert(new FavouriteModel(items.get(position).getTitle(),String.valueOf(category_id),title,String.valueOf(item_position)));
            }
        });


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
