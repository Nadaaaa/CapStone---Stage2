package com.example.nada.devhires.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.nada.devhires.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddGitHubUsernameFragment extends Fragment {

    @BindView(R.id.addGitHub_editText_username)
    EditText editText_githubUsername;

    @BindView(R.id.addGitHub_button)
    Button button_addGithubUsername;

    public AddGitHubUsernameFragment() {
        // Required empty public constructor
    }


    public static AddGitHubUsernameFragment newInstance(String param1, String param2) {
        AddGitHubUsernameFragment fragment = new AddGitHubUsernameFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_add_git_hub_username, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @OnClick(R.id.addGitHub_button)
    public void AddGithubUserNameToDatabase() {
        if (editText_githubUsername.getText().toString().isEmpty()) {
            return;
        }
        final String githubUsername = editText_githubUsername.getText().toString();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String UID = mAuth.getCurrentUser().getUid();
        DatabaseReference usersDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(UID);
        usersDatabase.child("github_username").setValue(githubUsername);
    }

}
