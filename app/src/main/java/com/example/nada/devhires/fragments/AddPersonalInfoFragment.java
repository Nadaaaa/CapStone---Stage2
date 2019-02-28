package com.example.nada.devhires.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.nada.devhires.R;

import com.example.nada.devhires.utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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


    @OnClick(R.id.addPersonal_toolbar_image)
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


}
