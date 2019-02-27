package com.example.nada.devhires.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.nada.devhires.fragments.AddGitHubUsernameFragment;
import com.example.nada.devhires.fragments.AddPersonalInfoFragment;
import com.example.nada.devhires.fragments.PersonalInfoFragment;
import com.example.nada.devhires.fragments.ProjectsFragment;
import com.example.nada.devhires.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.bottomNavigation)
    BottomNavigationView bottomNavigationView;


    PersonalInfoFragment personalInfoFragment;
    AddGitHubUsernameFragment addGitHubUsernameFragment;
    ProjectsFragment projectsFragment;
    AddPersonalInfoFragment addPersonalInfoFragment;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);


        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        personalInfoFragment = new PersonalInfoFragment();
        addGitHubUsernameFragment = new AddGitHubUsernameFragment();
        addPersonalInfoFragment = new AddPersonalInfoFragment();

        fragmentManager = getSupportFragmentManager();

        getPersonalInfoFromDatabase();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.item_menu_personal:
                    getPersonalInfoFromDatabase();
                    return true;

                case R.id.item_menu_projects:
                    getGithubUserNameFromDatabase();
                    return true;

                case R.id.item_menu_editProfile:
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    addPersonalInfoFragment = new AddPersonalInfoFragment();
                    fragmentManager
                            .beginTransaction()
                            .replace(R.id.fragment_container, addPersonalInfoFragment)
                            .commit();
                    return true;
            }
            return false;
        }
    };


    public void getGithubUserNameFromDatabase() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String UID = mAuth.getCurrentUser().getUid();
        DatabaseReference usersDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(UID);

        usersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String githubUsername = String.valueOf(dataSnapshot.child("github_username").getValue());

                if (githubUsername.equals("")) {
                    fragmentManager
                            .beginTransaction()
                            .replace(R.id.fragment_container, addGitHubUsernameFragment)
                            .commit();
                } else {
                    projectsFragment = ProjectsFragment.newInstance(githubUsername);
                    fragmentManager
                            .beginTransaction()
                            .replace(R.id.fragment_container, projectsFragment)
                            .commit();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getPersonalInfoFromDatabase() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String UID = mAuth.getCurrentUser().getUid();
        DatabaseReference usersDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(UID);

        usersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String username = String.valueOf(dataSnapshot.child("username").getValue());

                if (username.equals("")) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    addPersonalInfoFragment = new AddPersonalInfoFragment();
                    fragmentManager
                            .beginTransaction()
                            .replace(R.id.fragment_container, addPersonalInfoFragment)
                            .commit();
                } else {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager
                            .beginTransaction()
                            .replace(R.id.fragment_container, personalInfoFragment)
                            .commit();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void sendToStart() {
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }
}
