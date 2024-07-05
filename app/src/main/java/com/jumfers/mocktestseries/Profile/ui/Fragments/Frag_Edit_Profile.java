package com.jumfers.mocktestseries.Profile.ui.Fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.jumfers.mocktestseries.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Frag_Edit_Profile extends Fragment {

    EditText name,email,number;
    String profileimage = "";
    ImageView profile_image;
    CardView btn_save;
    ProgressBar progressBar;
    TextView heading;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        assert container != null;
        View v = LayoutInflater.from(container.getContext()).inflate(R.layout.activity_act_edit_profile,container,false);
        profile_image = v.findViewById(R.id.profile_img);
        btn_save = v.findViewById(R.id.btn_save);
        name = v.findViewById(R.id.name);
        email = v.findViewById(R.id.email);
        number = v.findViewById(R.id.number);
        progressBar = v.findViewById(R.id.progress_circular);
        heading = v.findViewById(R.id.heading);
        getProfile();
        heading.setText("Edit Profile");
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences mpref = getContext().getSharedPreferences("BASE_APP", Context.MODE_PRIVATE);
                SharedPreferences.Editor myEdit = mpref.edit();
                if(!name.getText().toString().isEmpty()){
                    myEdit.putString("username",name.getText().toString());
                }
                if(!email.getText().toString().isEmpty()){
                    myEdit.putString("email",email.getText().toString());
                }
                if(!number.getText().toString().isEmpty()){
                    myEdit.putString("mobile",number.getText().toString());
                }
                myEdit.apply();
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        });
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageChooser();
            }
        });
        return v;
    }
    public void imageChooser() {
        progressBar.setVisibility(View.VISIBLE);
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select Picture"), 190);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        progressBar.setVisibility(View.GONE);
        if (resultCode == RESULT_OK) {
            if (requestCode == 190) {
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    profile_image.setImageURI(selectedImageUri);
                    profileimage = String.valueOf(selectedImageUri);
                    Bitmap bmap = null;
                    try {
                        bmap = MediaStore.Images.Media.getBitmap(this.getContext().getContentResolver(), selectedImageUri);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                        byte[] b = baos.toByteArray();
                        String encoded = Base64.encodeToString(b, Base64.DEFAULT);
                        SharedPreferences mpref = getContext().getSharedPreferences("BASE_APP", Context.MODE_PRIVATE);
                        SharedPreferences.Editor myEdit = mpref.edit();
                        myEdit.putString("profile_image",encoded);
                        myEdit.apply();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }

    private void getProfile(){
        SharedPreferences mpref = getContext().getSharedPreferences("BASE_APP", Context.MODE_PRIVATE);
        String encoded = mpref.getString("profile_image","");
        if(!encoded.isEmpty()){
            byte[] imageAsBytes = Base64.decode(encoded.getBytes(), Base64.DEFAULT);
            profile_image.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
        }
        name.setText(mpref.getString("username",""));
        email.setText(mpref.getString("email",""));
        number.setText(mpref.getString("mobile",""));

    }

}
