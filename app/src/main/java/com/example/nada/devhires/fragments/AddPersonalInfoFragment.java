package com.example.nada.devhires.fragments;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.nada.devhires.R;
import com.example.nada.devhires.activities.SplashActivity;
import com.example.nada.devhires.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class AddPersonalInfoFragment extends Fragment {

    @BindView(R.id.addPersonal_toolbar_image)
    ImageView addPersonal_toolbar_image;

    @BindView(R.id.addPersonal_image)
    CircleImageView imageView_user;

    @BindView(R.id.addPersonal_username)
    EditText editText_username;

    @BindView(R.id.addPersonal_title)
    EditText editText_title;

    @BindView(R.id.addPersonal_company)
    EditText editText_company;

    @BindView(R.id.addPersonal_phone)
    EditText editText_phone;

    @BindView(R.id.addPersonal_maritalStatus)
    EditText editText_maritalStatus;

    @BindView(R.id.addPersonal_bio)
    EditText editText_bio;


    DatabaseReference usersDatabase;
    String UID;

    public AddPersonalInfoFragment() {
        // Required empty public constructor
    }

    public static AddPersonalInfoFragment newInstance(String param1) {
        AddPersonalInfoFragment fragment = new AddPersonalInfoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_personal_info, container, false);

        ButterKnife.bind(this, rootView);

        UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        usersDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(UID);

        retrievePersonalInfo();

        return rootView;
    }

    void retrievePersonalInfo() {

        usersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("username").getValue() != null) {
                    editText_username.setText(String.valueOf(dataSnapshot.child("username").getValue()));
                    editText_title.setText(String.valueOf(dataSnapshot.child("job_title").getValue()));
                    editText_company.setText(String.valueOf(dataSnapshot.child("company").getValue()));
                    editText_bio.setText(String.valueOf(dataSnapshot.child("bio").getValue()));
                    editText_phone.setText(String.valueOf(dataSnapshot.child("phone").getValue()));
                    editText_maritalStatus.setText(String.valueOf(dataSnapshot.child("marital_status").getValue()));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


  /*  @OnClick(R.id.addPersonal_toolbar_image)
    void addPersonalInfo() {
        if (!Utils.isEmptyEditTexts(getContext(), editText_username, editText_title, editText_company, editText_phone, editText_bio, editText_maritalStatus)) {

            usersDatabase.child("bio").setValue(editText_bio.getText().toString());
            usersDatabase.child("company").setValue(editText_company.getText().toString());
            usersDatabase.child("phone").setValue(editText_phone.getText().toString());
            usersDatabase.child("username").setValue(editText_username.getText().toString());
            usersDatabase.child("job_title").setValue(editText_title.getText().toString());
            usersDatabase.child("marital_status").setValue(editText_maritalStatus.getText().toString());

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            PersonalInfoFragment personalInfoFragment = new PersonalInfoFragment();
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container, personalInfoFragment)
                    .commit();
        }
    }

    @OnClick(R.id.addPersonal_image)
    void getImageFromGallery() {
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // ******** code for crop image
        i.putExtra("crop", "true");
        i.putExtra("aspectX", 100);
        i.putExtra("aspectY", 100);
        i.putExtra("outputX", 256);
        i.putExtra("outputY", 356);

        try {

            i.putExtra("return-data", true);
            startActivityForResult(
                    Intent.createChooser(i, "Select Picture"), 0);
        } catch (ActivityNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    private Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        // inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            try {
                String UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference usersDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(UID);

                Bundle bundle = data.getExtras();
                Bitmap bitmap = bundle.getParcelable("data");
                //imageView_user.setImageBitmap(bitmap);

                String s = String.valueOf(getImageUri(getContext(), bitmap));

                usersDatabase.child("image").setValue(String.valueOf(getImageUri(getContext(), bitmap)));

                Glide.with(getContext()).load(s).into(imageView_user);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }*/
}
