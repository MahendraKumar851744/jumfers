package com.jumfers.mocktestseries.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jumfers.mocktestseries.R;
import com.jumfers.mocktestseries.databases.weakness.WeaknessModel;

import java.util.ArrayList;
import java.util.Arrays;

public class Ad_Weakness extends RecyclerView.Adapter<Ad_Weakness.CategoryViewHolder> {

    Context context;
    ArrayList<WeaknessModel> items;
    boolean[] booleans;

    public Ad_Weakness(Context context, ArrayList<WeaknessModel> items ) {
        this.context = context;
        this.items = items;
        booleans = new boolean[items.size()];
        Arrays.fill(booleans, false);
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryViewHolder(LayoutInflater.from(context).inflate(R.layout.weakness_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.tv_qpname.setText(items.get(position).getQp_name());
        holder.tv_papername.setText(items.get(position).getPaper_name());
        holder.tv_answer.setText(items.get(position).getAnswer());
        holder.tv_status.setText(items.get(position).getStatus());
        holder.tv_qn.setText(items.get(position).getQuestion());
        holder.itemView.setOnClickListener(v->{
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
        TextView tv_qpname,tv_papername,tv_qn,tv_status,tv_answer;
        ImageView iv_arrow;
        LinearLayout status;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_qpname = itemView.findViewById(R.id.tv_qpname);
            tv_papername = itemView.findViewById(R.id.tv_papername);
            tv_qn = itemView.findViewById(R.id.tv_qn);
            tv_status = itemView.findViewById(R.id.tv_status);
            tv_answer = itemView.findViewById(R.id.tv_answer);
            iv_arrow = itemView.findViewById(R.id.iv_arrow);
            status = itemView.findViewById(R.id.status);
        }
    }

}
