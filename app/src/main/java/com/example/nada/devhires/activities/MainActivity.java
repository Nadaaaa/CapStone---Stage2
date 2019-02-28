package com.example.nada.devhires.activities;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.nada.devhires.fragments.AddGitHubUsernameFragment;
import com.example.nada.devhires.fragments.AddPersonalInfoFragment;
import com.example.nada.devhires.fragments.PersonalInfoFragment;
import com.example.nada.devhires.fragments.ProjectsFragment;
import com.example.nada.devhires.R;
import com.example.nada.devhires.models.User;
import com.example.nada.devhires.network.MyJobService;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getName();

    @BindView(R.id.bottomNavigation)
    BottomNavigationView bottomNavigationView;

    PersonalInfoFragment personalInfoFragment;
    AddGitHubUsernameFragment addGitHubUsernameFragment;
    ProjectsFragment projectsFragment;
    AddPersonalInfoFragment addPersonalInfoFragment;
    FragmentManager fragmentManager;
    private int saveState;

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

        if (savedInstanceState != null) {
            bottomNavigationView.setSelectedItemId(saveState);
        } else {
            getPersonalInfoFromDatabase();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        bottomNavigationView.setSelectedItemId(saveState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        saveState = bottomNavigationView.getSelectedItemId();
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

                User user = new User();
                user.setUsername(username);
                user.setJobTitle(String.valueOf(dataSnapshot.child("job_title").getValue()));
                user.setCompany(String.valueOf(dataSnapshot.child("company").getValue()));
                user.setEmail(String.valueOf(dataSnapshot.child("email").getValue()));
                user.setPhone(String.valueOf(dataSnapshot.child("phone").getValue()));
                user.setMaritalStatus(String.valueOf(dataSnapshot.child("marital_status").getValue()));

                createSampleDataForWidget(MainActivity.this, user);


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

    public static void createSampleDataForWidget(Context context, User user) {
        SharedPreferences sharedPref = context.getSharedPreferences("userData", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(user);
        prefsEditor.putString("user", json);
        prefsEditor.apply();
    }

    public static User getDataFromSharedPrefs(Context context) {
        User user;
        SharedPreferences mPrefs = context.getSharedPreferences("userData", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("user", "");
        Type type = new TypeToken<User>() {
        }.getType();
        user = gson.fromJson(json, type);
        return user;
    }

}
