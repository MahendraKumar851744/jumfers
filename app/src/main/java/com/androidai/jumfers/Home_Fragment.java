package com.androidai.jumfers;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidai.jumfers.Adapters.Ad_Full_Paper;
import com.androidai.jumfers.History.His_database;
import com.androidai.jumfers.Profile.ui.Fragments.Frag_Edit_Profile;
import com.androidai.utils.NetworkConnection;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Home_Fragment extends Fragment implements View.OnClickListener{

    public Home_Fragment() {
    }
    List<SlideModel> imageList;
    ImageSlider imageSlider;
    DrawerLayout drawerLayout;
    View leftDrawerMenu;
    LinearLayout railway,ssc,law,civil;
    TextView name;
    ProgressBar progress_circular;
    TextView exam1,exam2,exam3,exam4;
    ImageView profile_image;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.sub_main, container, false);
        imageSlider = v.findViewById(R.id.image_slider);
        drawerLayout = v.findViewById(R.id.drawerLayout);
        leftDrawerMenu = v.findViewById(R.id.leftDrawerMenu);
        railway = v.findViewById(R.id.railway);
        ssc = v.findViewById(R.id.ssc);
        law = v.findViewById(R.id.law);
        civil = v.findViewById(R.id.civil);
        progress_circular = v.findViewById(R.id.progress_circular);
        exam1 = v.findViewById(R.id.exam1);
        exam2 = v.findViewById(R.id.exam2);
        exam3 = v.findViewById(R.id.exam3);
        exam4 = v.findViewById(R.id.exam4);
        name = v.findViewById(R.id.name);
        profile_image = v.findViewById(R.id.profile_image);
        name.setText(getContext().getSharedPreferences("BASE_APP", Context.MODE_PRIVATE).getString("username",""));

        railway.setOnClickListener(this);
        ssc.setOnClickListener(this);
        law.setOnClickListener(this);
        civil.setOnClickListener(this);

        getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getContext(), R.color.primary));

        drawerMenu(v);
        SharedPreferences mpref = getContext().getSharedPreferences("BASE_APP", Context.MODE_PRIVATE);
        String encoded = mpref.getString("profile_image","");
        if(!encoded.isEmpty()){
            byte[] imageAsBytes = Base64.decode(encoded.getBytes(), Base64.DEFAULT);
            profile_image.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
        }
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(leftDrawerMenu);
            }
        });
        setImageSlider();
        makeVolleyRequest(getContext());
        return v;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.railway:
                replaceFragment(new Frag_Sub_Category(),1);
                break;
            case R.id.ssc:
                replaceFragment(new Frag_Sub_Category(),2);
                break;
            case R.id.law:
                replaceFragment(new Frag_Sub_Category(),3);
                break;
            case R.id.civil:
                replaceFragment(new Frag_Sub_Category(),4);
                break;

        }

    }
    private void replaceFragment(Fragment fragment,int category_id){
        SharedPreferences sp = getContext().getSharedPreferences("BASEAPP",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("CATEGORY_ID",category_id);
        if(category_id==1){
            editor.putString("CATEGORY_NAME",exam1.getText().toString());
        } else if (category_id==2) {
            editor.putString("CATEGORY_NAME",exam2.getText().toString());
        } else if (category_id==3) {
            editor.putString("CATEGORY_NAME",exam3.getText().toString());
        } else {
            editor.putString("CATEGORY_NAME",exam4.getText().toString());
        }
        editor.apply();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flFragment, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flFragment, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void makeVolleyRequest(Context context){
        if(NetworkConnection.checkNetworkStatus(context)) {
            String url = "https://test.jenacademy.in/api/v1/category-listing";
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progress_circular.setVisibility(View.GONE);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray data = jsonObject.getJSONArray("data");
                                for(int i=0;i<data.length();i++){
                                    JSONObject model = data.getJSONObject(i);
                                    if(i==0){
                                        exam1.setText(model.getString("title"));
                                    }else if(i==1){
                                        exam2.setText(model.getString("title"));
                                    }else if(i==2){
                                        exam3.setText(model.getString("title"));
                                    }else if(i==3){
                                        exam4.setText(model.getString("title"));
                                    }
                                }
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progress_circular.setVisibility(View.GONE);
                            try {
                                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.d("APPLICATION", error.getMessage());
                            }catch (Exception e){
                                Log.d("APPLICATION", e.getMessage());
                            }
                        }
                    }){
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();
                    String token = context.getSharedPreferences("BASE_APP",Context.MODE_PRIVATE).getString("token","");
                    headers.put("Authorization", "Bearer " + token);
                    return headers;
                }
            };

            Volley.newRequestQueue(context).add(stringRequest);

        }else{
            Toast.makeText(context, "Check Your Internet Connection!", Toast.LENGTH_SHORT).show();
        }
    }
    private void drawerMenu(View v){
        SharedPreferences mpref = getContext().getSharedPreferences("BASE_APP", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mpref.edit();
        TextView tv_name = v.findViewById(R.id.tv_name);
        TextView tv_email = v.findViewById(R.id.tv_email);
        ImageView profile_image = v.findViewById(R.id.profile_image);
        ImageView left_profile_img = v.findViewById(R.id.left_profile_img);
        tv_name.setText(mpref.getString("username","Your Name"));
        tv_email.setText(mpref.getString("email","Your Email"));
        String encoded = mpref.getString("profile_image","");
        if(!encoded.isEmpty()){
            byte[] imageAsBytes = Base64.decode(encoded.getBytes(), Base64.DEFAULT);
            profile_image.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
            left_profile_img.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
        }
        CardView btn_edit = v.findViewById(R.id.btn_edit);
        btn_edit.setOnClickListener(V->{
            drawerLayout.closeDrawer(leftDrawerMenu);
            replaceFragment(new Frag_Edit_Profile());
        });

        RelativeLayout rel_aboutus = v.findViewById(R.id.rel_aboutus);
        rel_aboutus.setOnClickListener(V->{
            drawerLayout.closeDrawer(leftDrawerMenu);
            replaceFragment(new Frag_AboutUs());
        });

        RelativeLayout rel_fav = v.findViewById(R.id.rl_fav);
        rel_fav.setOnClickListener(V->{
            drawerLayout.closeDrawer(leftDrawerMenu);
            replaceFragment(new Frag_Favourites());
        });

        RelativeLayout rel_weak = v.findViewById(R.id.rl_weak);
        rel_weak.setOnClickListener(V->{
            drawerLayout.closeDrawer(leftDrawerMenu);
            replaceFragment(new Frag_Weakness());
        });
        RelativeLayout rel_clear_his = v.findViewById(R.id.rel_clear_his);
        rel_clear_his.setOnClickListener(V->{
            drawerLayout.closeDrawer(leftDrawerMenu);
            Thread workerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                His_database his_database = His_database.getDbInstance(getContext());
                his_database.clearAllTables();
            }
            });
            workerThread.start();

            Toast.makeText(getContext(),"Cleared SuccessFully",Toast.LENGTH_SHORT).show();
        });

        RelativeLayout rel_logout = v.findViewById(R.id.rel_logout);
        rel_logout.setOnClickListener(V->{
            drawerLayout.closeDrawer(leftDrawerMenu);
            Intent i = new Intent(getActivity(),Act_register.class);
            editor.putString("password","");
            editor.putString("username","");
            editor.putString("email","");
            editor.putString("mobile","");
            editor.apply();
            startActivity(i);
            requireActivity().finish();
        });

        ImageView iv_back = v.findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(leftDrawerMenu);
            }
        });

        TextView heading = v.findViewById(R.id.heading);
        heading.setText("My Profile");
    }

    private void setImageSlider(){
        imageList = new ArrayList<SlideModel>();
        imageList.add(new SlideModel(R.drawable.slideshowimg,ScaleTypes.CENTER_INSIDE));
        imageList.add(new SlideModel(R.drawable.slideshowimg,ScaleTypes.CENTER_INSIDE));
        imageList.add(new SlideModel(R.drawable.slideshowimg,ScaleTypes.CENTER_INSIDE));
        imageSlider.setImageList(imageList, ScaleTypes.FIT);
        imageSlider.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemSelected(int i) {

                intentToLink("");
            }
        });
    }
    public void intentToLink(String url){
        try {

        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }
}