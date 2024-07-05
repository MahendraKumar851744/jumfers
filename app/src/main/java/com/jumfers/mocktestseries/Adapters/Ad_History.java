package com.jumfers.mocktestseries.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jumfers.mocktestseries.databases.History.HistoryModel;

import com.jumfers.mocktestseries.R;

import java.util.ArrayList;

public class Ad_History extends RecyclerView.Adapter<Ad_History.CategoryViewHolder> {

    Context context;
    ArrayList<HistoryModel> items;


    public Ad_History(Context context, ArrayList<HistoryModel> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryViewHolder(LayoutInflater.from(context).inflate(R.layout.history_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.tv_title.setText(items.get(position).getTitle());
        holder.paper_name.setText(items.get(position).getPaper_num()+" | ");
        holder.cat_name.setText(items.get(position).getCategory_name());
        holder.time.setText(items.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title,paper_name,cat_name,time;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            paper_name = itemView.findViewById(R.id.paper_name);
            tv_title = itemView.findViewById(R.id.tv_title);
            cat_name = itemView.findViewById(R.id.cat_name);
            time = itemView.findViewById(R.id.time);
        }
    }

}
