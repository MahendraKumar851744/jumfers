package com.jumfers.mocktestseries;

import static com.jumfers.mocktestseries.utils.Constants.GET_SUBCATEGORIES;
import static com.jumfers.mocktestseries.utils.Constants.SUBCATEGORIES;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jumfers.mocktestseries.databases.History.His_database;
import com.jumfers.mocktestseries.Profile.ui.Fragments.Frag_Edit_Profile;

import com.jumfers.mocktestseries.databases.Categories.CategoryModel;
import com.jumfers.mocktestseries.databases.Categories.cat_dao;
import com.jumfers.mocktestseries.databases.Categories.cat_database;
import com.jumfers.mocktestseries.databases.SubCategories.Subcat_database;
import com.jumfers.mocktestseries.utils.FragmentUtil;
import com.jumfers.mocktestseries.utils.NetworkConnection;
import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.jumfers.mocktestseries.utils.RequestHandler;
import com.jumfers.mocktestseries.utils.SharedPreferencesUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    ImageView img1,img2,img3,img4;
    TextView tv_seeall;

    TextView test_name,test_score;

    CardView cv_recent_test;

    FragmentUtil fragmentUtil;

    CardView cv1,cv2,cv3,cv4;
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
        tv_seeall = v.findViewById(R.id.tv_seeall);
        progress_circular = v.findViewById(R.id.progress_circular);
        progress_circular.setVisibility(View.GONE);
        exam1 = v.findViewById(R.id.exam1);
        exam2 = v.findViewById(R.id.exam2);
        exam3 = v.findViewById(R.id.exam3);
        exam4 = v.findViewById(R.id.exam4);
        img1 = v.findViewById(R.id.img1);
        img2 = v.findViewById(R.id.img2);
        img3 = v.findViewById(R.id.img3);
        img4 = v.findViewById(R.id.img4);
        name = v.findViewById(R.id.name);
        profile_image = v.findViewById(R.id.profile_image);
        test_name = v.findViewById(R.id.test_name);
        test_score = v.findViewById(R.id.test_score);
        cv_recent_test = v.findViewById(R.id.cv_recent_test);

        cv1 = v.findViewById(R.id.cv1);
        cv2 = v.findViewById(R.id.cv2);
        cv3 = v.findViewById(R.id.cv3);
        cv4 = v.findViewById(R.id.cv4);

        name.setText(getContext().getSharedPreferences("BASE_APP", Context.MODE_PRIVATE).getString("username",""));

        fragmentUtil = new FragmentUtil(getParentFragmentManager(), R.id.flFragment);

        railway.setOnClickListener(this);
        ssc.setOnClickListener(this);
        law.setOnClickListener(this);
        civil.setOnClickListener(this);

        getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getContext(), R.color.primary));

        drawerMenu(v);


        cat_database cat_db = cat_database.getDbInstance(getContext());
        if(cat_db.dao().getAllQuestions().size()==0){
            progress_circular.setVisibility(View.VISIBLE);
            getCategoryData(getContext());
        }else{
            setData(getContext());
        }


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

        tv_seeall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new Frag_Category());
            }
        });


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
        progress_circular.setVisibility(View.VISIBLE);

        SharedPreferencesUtil util = new SharedPreferencesUtil(getContext());
        util.putInt("CATEGORY_ID",category_id);
        if(category_id==1){
            util.putString("CATEGORY_NAME",exam1.getText().toString());
        } else if (category_id==2) {
            util.putString("CATEGORY_NAME",exam2.getText().toString());
        } else if (category_id==3) {
            util.putString("CATEGORY_NAME",exam3.getText().toString());
        } else {
            util.putString("CATEGORY_NAME",exam4.getText().toString());
        }

        RequestHandler requestHandler = new RequestHandler(getContext());
        Map<String, String> params = new HashMap<>();
        params.put("category_id", String.valueOf(category_id));

        Subcat_database db = Subcat_database.getDbInstance(getContext());

        db.dao().doesItemExist(category_id).observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean exist) {
                if(exist){
                    progress_circular.setVisibility(View.GONE);
                    fragmentUtil.replaceFragment(fragment,"SUB_CATEGORY");
                }else{
                    requestHandler.makePostRequest(GET_SUBCATEGORIES, params, new RequestHandler.ResponseCallback() {
                        @Override
                        public void onComplete(boolean success) {
                            progress_circular.setVisibility(View.GONE);
                            if(success){
                                fragmentUtil.replaceFragment(fragment,"SUB_CATEGORY");
                            }else{
                                Toast.makeText(getContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    },SUBCATEGORIES);
                }
            }
        });


    }

    private void replaceFragment(Fragment fragment){

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flFragment, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void setData(Context context){
        cat_database catDb = cat_database.getDbInstance(context);
        cat_dao catDao = catDb.dao();
        int size = catDao.getAllQuestions().size();
        if(size==1){
            exam1.setText(catDao.getAllQuestions().get(0).getCategory_title());
            Glide.with(this).load("https://test.jenacademy.in/storage/category/"+catDao.getAllQuestions().get(0).getCategory_img()).into(img1);
            cv2.setVisibility(View.GONE);
            cv3.setVisibility(View.GONE);
            cv4.setVisibility(View.GONE);
        }else if(size==2){
            exam1.setText(catDao.getAllQuestions().get(0).getCategory_title());
            Glide.with(this).load("https://test.jenacademy.in/storage/category/"+catDao.getAllQuestions().get(0).getCategory_img()).into(img1);
            exam2.setText(catDao.getAllQuestions().get(1).getCategory_title());
            Glide.with(this).load("https://test.jenacademy.in/storage/category/"+catDao.getAllQuestions().get(1).getCategory_img()).into(img2);
            cv3.setVisibility(View.GONE);
            cv4.setVisibility(View.GONE);
        }else if(size==3){
            exam1.setText(catDao.getAllQuestions().get(0).getCategory_title());
            Glide.with(this).load("https://test.jenacademy.in/storage/category/"+catDao.getAllQuestions().get(0).getCategory_img()).into(img1);
            exam2.setText(catDao.getAllQuestions().get(1).getCategory_title());
            Glide.with(this).load("https://test.jenacademy.in/storage/category/"+catDao.getAllQuestions().get(1).getCategory_img()).into(img2);
            exam3.setText(catDao.getAllQuestions().get(2).getCategory_title());
            Glide.with(this).load("https://test.jenacademy.in/storage/category/"+catDao.getAllQuestions().get(3).getCategory_img()).into(img3);
            cv4.setVisibility(View.GONE);
        }else{
            exam1.setText(catDao.getAllQuestions().get(0).getCategory_title());
            Glide.with(this).load("https://test.jenacademy.in/storage/category/"+catDao.getAllQuestions().get(0).getCategory_img()).into(img1);
            exam2.setText(catDao.getAllQuestions().get(1).getCategory_title());
            Glide.with(this).load("https://test.jenacademy.in/storage/category/"+catDao.getAllQuestions().get(1).getCategory_img()).into(img2);
            exam3.setText(catDao.getAllQuestions().get(2).getCategory_title());
            Glide.with(this).load("https://test.jenacademy.in/storage/category/"+catDao.getAllQuestions().get(3).getCategory_img()).into(img3);
            exam4.setText(catDao.getAllQuestions().get(3).getCategory_title());
            Glide.with(this).load("https://test.jenacademy.in/storage/category/"+catDao.getAllQuestions().get(4).getCategory_img()).into(img4);

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

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        RelativeLayout rel_logout = v.findViewById(R.id.rel_logout);
        rel_logout.setOnClickListener(V->{

            drawerLayout.closeDrawer(leftDrawerMenu);
            if(user!=null){
                FirebaseAuth.getInstance().signOut();
            }
            Intent i = new Intent(getActivity(),Act_register.class);
            editor.putString("password","");
            editor.putString("username","");
            editor.putString("email","");
            editor.putString("mobile","");
            editor.apply();
            startActivity(i);
            requireActivity().finish();
        });

        RelativeLayout rel_delete = v.findViewById(R.id.rel_deleteAccount);
        rel_delete.setOnClickListener(V->{
            drawerLayout.closeDrawer(leftDrawerMenu);
            if(user!=null){
                user.delete();
            }
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
    public void getCategoryData(Context context){
        cat_database cat_db = cat_database.getDbInstance(context);
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
                                    cat_db.dao().insert(new CategoryModel(model.getString("id"),model.getString("title"),model.getString("image")));
                                }



                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }

                            setData(getContext());
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
                    String token = getContext().getSharedPreferences("BASE_APP",Context.MODE_PRIVATE).getString("token","");
                    headers.put("Authorization", "Bearer " + token);
                    return headers;
                }
            };

            Volley.newRequestQueue(context).add(stringRequest);

        }else{
            Toast.makeText(context, "Check Your Internet Connection!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences mpref = getContext().getSharedPreferences("BASE_APP", Context.MODE_PRIVATE);

        String score = mpref.getString("RECENT_PAPER_SCORE", "");
        String name = mpref.getString("RECENT_PAPER_NAME", "");

        if(score.isEmpty()){
            cv_recent_test.setVisibility(View.GONE);
        }else{
            cv_recent_test.setVisibility(View.VISIBLE);
            test_name.setText(name);
            test_score.setText(score);
        }
    }



}