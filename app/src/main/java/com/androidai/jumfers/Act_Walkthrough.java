package com.androidai.jumfers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Act_Walkthrough extends AppCompatActivity {
    private GestureDetector gestureDetector;
    private static int current_screen = 0;

    CardView register,login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_walkthrough);
        register = findViewById(R.id.register);
        login = findViewById(R.id.login);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Act_Walkthrough.this,Act_register.class);
                startActivity(i);
                finish();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Act_Walkthrough.this,Act_login.class);
                startActivity(i);
                finish();
            }
        });
        SwipeGestureListener gestureListener = new SwipeGestureListener() {
            @Override
            public void onSwipeRight() {
                current_screen -= 1;
                handleUiChanges();
            }

            @Override
            public void onSwipeLeft() {
                current_screen += 1;
                handleUiChanges();
            }
        };
        gestureDetector = new GestureDetector(this, gestureListener);
        handleUiChanges();
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }
    public void handleUiChanges(){
        ImageView iv_frame = findViewById(R.id.iv_frame);
        ImageView iv_dot1 = findViewById(R.id.iv_dot1);
        ImageView iv_dot2 = findViewById(R.id.iv_dot2);
        ImageView iv_dot3 = findViewById(R.id.iv_dot3);
        TextView title = findViewById(R.id.tv_title);
        TextView desc = findViewById(R.id.tv_desc);
        if((Math.abs(current_screen))%3==0){
            iv_frame.setImageResource(R.drawable.frame_38);
            title.setText(getResources().getString(R.string.walkthrough_title_1));
            desc.setText(getResources().getString(R.string.walkthrough_desc_1));
            iv_dot1.setImageResource(R.drawable.selected);
            iv_dot2.setImageResource(R.drawable.unselected);
            iv_dot3.setImageResource(R.drawable.unselected);
        }else if((Math.abs(current_screen))%3==1){
            iv_frame.setImageResource(R.drawable.frame_37);
            title.setText(getResources().getString(R.string.walkthrough_title_2));
            desc.setText(getResources().getString(R.string.walkthrough_desc_2));
            iv_dot2.setImageResource(R.drawable.selected);
            iv_dot1.setImageResource(R.drawable.unselected);
            iv_dot3.setImageResource(R.drawable.unselected);
        }else{
            iv_frame.setImageResource(R.drawable.frame_39);
            title.setText(getResources().getString(R.string.walkthrough_title_3));
            desc.setText(getResources().getString(R.string.walkthrough_desc_3));
            iv_dot3.setImageResource(R.drawable.selected);
            iv_dot2.setImageResource(R.drawable.unselected);
            iv_dot1.setImageResource(R.drawable.unselected);
        }
    }

}