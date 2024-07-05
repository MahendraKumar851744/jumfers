package com.jumfers.mocktestseries.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jumfers.mocktestseries.ItemClickListener;
import com.jumfers.mocktestseries.databases.papers.Paper;

import com.jumfers.mocktestseries.R;

import java.util.ArrayList;
import java.util.Arrays;

public class Ad_Paper extends RecyclerView.Adapter<Ad_Paper.CategoryViewHolder> {

    Context context;
    ArrayList<Paper> items;

    ItemClickListener itemClickListener;

    boolean[] booleans;


    public Ad_Paper(Context context, ArrayList<Paper> items, ItemClickListener itemClickListener) {
        this.context = context;
        this.items = items;
        this.itemClickListener = itemClickListener;
        booleans = new boolean[items.size()];
        Arrays.fill(booleans, false);
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryViewHolder(LayoutInflater.from(context).inflate(R.layout.paper_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.tv_title.setText(items.get(position).getTitle());
        holder.tv_num_of_qn.setText("Number of Questions: "+items.get(position).getTotal_papers());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sp = context.getSharedPreferences("BASEAPP",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("PAPER_NAME",items.get(position).getTitle());
                editor.putString("PAPER_ID", items.get(position).getId());
                editor.putString("PAPER_DURATION", items.get(position).getDuration());
                editor.apply();
                itemClickListener.onClick(position,Integer.parseInt(items.get(position).getId()));
            }
        });
        holder.tv_desc.setText(items.get(position).getDescription());
        holder.tv_time.setText(items.get(position).getDuration()+" min");

        holder.iv_arrow.setOnClickListener(v->{
            if(booleans[position]){
                booleans[position] = false;
                holder.status.setVisibility(View.GONE);
                holder.iv_arrow.setImageResource(R.drawable.dropdown_arrow);
            }else{
                booleans[position] = true;
                holder.status.setVisibility(View.VISIBLE);
                holder.iv_arrow.setImageResource(R.drawable.dropup_arrow);
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title,tv_num_of_qn,tv_time,tv_desc;
        ImageView iv_arrow;
        LinearLayout status;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_num_of_qn = itemView.findViewById(R.id.tv_num_of_qn);
            iv_arrow = itemView.findViewById(R.id.iv_arrow);
            status = itemView.findViewById(R.id.status);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_desc = itemView.findViewById(R.id.tv_desc);
        }
    }



}
