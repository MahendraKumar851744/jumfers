package com.androidai.jumfers.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.androidai.jumfers.Exam.Model;
import com.androidai.jumfers.Frag_test;
import com.androidai.jumfers.Models.Paper_Count;
import com.androidai.jumfers.R;

import java.util.ArrayList;

public class Ad_Confirm_Answers extends RecyclerView.Adapter<Ad_Confirm_Answers.CategoryViewHolder> {

    Context context;
    ArrayList<Model> items;


    public Ad_Confirm_Answers(Context context, ArrayList<Model> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryViewHolder(LayoutInflater.from(context).inflate(R.layout.confirm_ansers_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, @SuppressLint("RecyclerView") int position) {
        int x = position+1;
        holder.tv_sno.setText(x+"");
        holder.tv_title.setText(items.get(position).getQuestion());
        if(!items.get(position).getSelected_answer().isEmpty()){
            holder.tv_attempt.setText(items.get(position).getSelected_answer());
        }else{
            holder.tv_attempt.setTextColor(Color.parseColor("#EB5D1E"));
            holder.tv_attempt.setText("skipped");
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView tv_sno,tv_title,tv_attempt;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_sno = itemView.findViewById(R.id.tv_sno);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_attempt = itemView.findViewById(R.id.tv_attempt);
        }
    }
}
