package com.example.nada.devhires.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nada.devhires.R;
import com.example.nada.devhires.activities.LoginActivity;
import com.example.nada.devhires.activities.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class PersonalInfoFragment extends Fragment {

    @BindView(R.id.personal_userImage)
    CircleImageView imageView_user;

    @BindView(R.id.personal_userName)
    TextView textView_username;

    @BindView(R.id.personal_userTitle)
    TextView textView_userTitle;

    @BindView(R.id.personal_userCompany)
    TextView textView_userCompany;

    @BindView(R.id.personal_userBio)
    TextView textView_userBio;

    @BindView(R.id.personal_userEmail)
    TextView textView_userEmail;

    @BindView(R.id.personal_userPhone)
    TextView textView_userPhone;

    @BindView(R.id.personal_userMaritalStatus)
    TextView textView_userMaritalStatus;

    @BindView(R.id.personal_signOut)
    TextView textView_signOut;

    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    public PersonalInfoFragment() {
        // Required empty public constructor
    }

    public static PersonalInfoFragment newInstance(String param1, String param2) {
        PersonalInfoFragment fragment = new PersonalInfoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_personal_info, container, false);
        mAuth = FirebaseAuth.getInstance();
        ButterKnife.bind(this, rootView);
        getPersonalInfoFromDatabase();
        return rootView;
    }

    @OnClick(R.id.personal_signOut)
    public void onClickSignOutText() {
        mAuth.signOut();
        ((MainActivity)getActivity()).sendToStart();
    }

    void getPersonalInfoFromDatabase() {
        avi.show();
        String UID = mAuth.getCurrentUser().getUid();
        DatabaseReference usersDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(UID);

        usersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                textView_username.setText(String.valueOf(dataSnapshot.child("username").getValue()));
                textView_userTitle.setText(String.valueOf(dataSnapshot.child("job_title").getValue()));
                textView_userCompany.setText(String.valueOf(dataSnapshot.child("company").getValue()));
                textView_userBio.setText(String.valueOf(dataSnapshot.child("bio").getValue()));
                textView_userEmail.setText(String.valueOf(dataSnapshot.child("email").getValue()));
                textView_userPhone.setText(String.valueOf(dataSnapshot.child("phone").getValue()));
                textView_userMaritalStatus.setText(String.valueOf(dataSnapshot.child("marital_status").getValue()));

                avi.hide();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                avi.hide();
            }
        });
    }
}
