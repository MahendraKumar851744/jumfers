package com.androidai.jumfers.Adapters;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidai.jumfers.Models.NotificationModel;
import com.androidai.jumfers.R;
import com.androidai.jumfers.weakness.WeaknessModel;

import java.util.ArrayList;
import java.util.Arrays;

public class Ad_Notification extends RecyclerView.Adapter<Ad_Notification.CategoryViewHolder> {

    Context context;
    ArrayList<NotificationModel> items;

    public Ad_Notification(Context context, ArrayList<NotificationModel> items ) {
        this.context = context;
        this.items = items;

    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryViewHolder(LayoutInflater.from(context).inflate(R.layout.notification_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.title.setText(items.get(position).getTitle());
        holder.desc.setText(items.get(position).getDesc());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView title,desc;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_title);
            desc = itemView.findViewById(R.id.desc);
        }
    }

}
