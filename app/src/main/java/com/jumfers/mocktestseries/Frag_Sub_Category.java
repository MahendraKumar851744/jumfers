package com.jumfers.mocktestseries;

import static com.jumfers.mocktestseries.utils.Constants.GET_PAPERS_COUNT;
import static com.jumfers.mocktestseries.utils.Constants.PAPERS_COUNT;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.jumfers.mocktestseries.Adapters.Ad_Sub_Category;
import com.jumfers.mocktestseries.databases.SubCategories.SubCategoryItem;

import com.jumfers.mocktestseries.databases.paperCountDb.Papers_dao;
import com.jumfers.mocktestseries.databases.paperCountDb.Papers_database;
import com.jumfers.mocktestseries.utils.FragmentUtil;
import com.jumfers.mocktestseries.utils.NetworkConnection;
import com.jumfers.mocktestseries.utils.RequestHandler;
import com.jumfers.mocktestseries.utils.SharedPreferencesUtil;
import com.jumfers.mocktestseries.viewModels.SubCategoryViewModel;
import com.jumfers.mocktestseries.viewModels.SubCategoryViewModelFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Frag_Sub_Category extends Fragment {

    String title;
    TextView tv_title;
    Ad_Sub_Category ad_sub_category;
    RecyclerView rv;
    int category_id;
    ProgressBar progress_circular;
    ItemClickListener itemClickListener;
    TextView total_papers;

    ImageView iv_back;
    public Frag_Sub_Category() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        assert container != null;
        View v = LayoutInflater.from(container.getContext()).inflate(R.layout.lay_sub_category,container,false);
        tv_title = v.findViewById(R.id.title);
        rv = v.findViewById(R.id.rv_sub_category_items);
        total_papers = v.findViewById(R.id.total_papers);
        progress_circular = v.findViewById(R.id.progress_circular);
        iv_back = v.findViewById(R.id.iv_back);

        SharedPreferences sp = getContext().getSharedPreferences("BASEAPP",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        title = sp.getString("CATEGORY_NAME","");
        category_id = sp.getInt("CATEGORY_ID",1);

        tv_title.setText(title);
        requireActivity().getWindow().setStatusBarColor(ContextCompat.getColor(requireContext(), R.color.white));
        itemClickListener = new ItemClickListener() {
            @Override
            public void onClick(int position,int subcategory_id) {
                Papers_database pdb = Papers_database.getDbInstance(getContext());
                Papers_dao papersDao = pdb.dao();
                papersDao.doesItemExist(category_id,subcategory_id).observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        progress_circular.setVisibility(View.VISIBLE);
                        if(!aBoolean){
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("category_id", String.valueOf(category_id));
                            params.put("subcategory_id", String.valueOf(subcategory_id));
                            RequestHandler handler = new RequestHandler(getContext());
                            handler.makePostRequest(GET_PAPERS_COUNT, params, new RequestHandler.ResponseCallback() {
                                @Override
                                public void onComplete(boolean success) {
                                    if(success){
                                        progress_circular.setVisibility(View.GONE);
                                        new FragmentUtil(requireActivity().getSupportFragmentManager(),R.id.flFragment).replaceFragment(new Frag_Exam(),"EXAM");
                                    }else{
                                        Toast.makeText(getContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                                        progress_circular.setVisibility(View.GONE);
                                    }
                                }
                            },PAPERS_COUNT);
                        }else{
                            progress_circular.setVisibility(View.GONE);
                            new FragmentUtil(requireActivity().getSupportFragmentManager(),R.id.flFragment).replaceFragment(new Frag_Exam(),"EXAM");
                        }
                    }
                });


            }
        };

        iv_back.setOnClickListener(view -> {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            fragmentManager.popBackStack();
        });

        SubCategoryViewModelFactory factory = new SubCategoryViewModelFactory(requireActivity().getApplication(), category_id);
        SubCategoryViewModel viewModel = new ViewModelProvider(this, factory).get(SubCategoryViewModel.class);
        viewModel.getSubCatItems().observe(getViewLifecycleOwner(), new Observer<List<SubCategoryItem>>() {
            @Override
            public void onChanged(List<SubCategoryItem> items) {
                progress_circular.setVisibility(View.GONE);
                total_papers.setText(items.size()+" Papers");
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
                rv.setLayoutManager(linearLayoutManager);
                ad_sub_category = new Ad_Sub_Category(getContext(), (ArrayList<SubCategoryItem>) items,itemClickListener);
                rv.setAdapter(ad_sub_category);
            }
        });





        return v;
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flFragment, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }


}
