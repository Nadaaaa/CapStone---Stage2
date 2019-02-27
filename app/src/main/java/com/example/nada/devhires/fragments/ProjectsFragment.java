package com.example.nada.devhires.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.nada.devhires.R;
import com.example.nada.devhires.activities.MainActivity;
import com.example.nada.devhires.adapters.RepositoryAdapter;
import com.example.nada.devhires.models.Repository;
import com.example.nada.devhires.network.Retrofit;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//TODO: get username
public class ProjectsFragment extends Fragment {

    @BindView(R.id.rv_projects)
    RecyclerView mRepositoryList;

    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;

    @BindView(R.id.projects_layout_connection)
    LinearLayout layout_connection;

    //Vars
    private RepositoryAdapter repositoryAdapter;
    private List<Repository> tempRepositoryList;
    private String githubUsername;

    public ProjectsFragment() {
        // Required empty public constructor
    }

    public static ProjectsFragment newInstance(String githubUsername) {
        ProjectsFragment fragment = new ProjectsFragment();
        Bundle args = new Bundle();
        args.putString("github_username", githubUsername);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_projects, container, false);

        ButterKnife.bind(this, rootView);

        githubUsername = getArguments().getString("github_username");

        tempRepositoryList = new ArrayList<>();

        getRepositories();

        repositoryAdapter = new RepositoryAdapter(getContext(), tempRepositoryList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRepositoryList.setLayoutManager(linearLayoutManager);
        mRepositoryList.setHasFixedSize(false);
        mRepositoryList.setAdapter(repositoryAdapter);

        return rootView;
    }

    private void getRepositories() {
        avi.show();
        Retrofit.getService("https://api.github.com/")
                .getRepos(githubUsername)
                .enqueue(new Callback<List<Repository>>() {
                    @Override
                    public void onResponse(Call<List<Repository>> call, Response<List<Repository>> response) {

                        if (null != response.body() || response.body().size() <= 0) {
                            tempRepositoryList.addAll(response.body());
                            repositoryAdapter.notifyDataSetChanged();
                        } else {
                            EmptyViewFragment emptyViewFragment = new EmptyViewFragment();
                            FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                            fragmentManager
                                    .beginTransaction()
                                    .replace(R.id.fragment_container,emptyViewFragment)
                                    .commit();
                        }
                        avi.hide();
                    }

                    @Override
                    public void onFailure(Call<List<Repository>> call, Throwable t) {
                        Log.d("MainActivity", "onFailure: " + t.getCause());
                        Toast.makeText(getContext(), "Check Internet Connection", Toast.LENGTH_SHORT).show();
                        avi.hide();
                    }
                });

    }

    @OnClick(R.id.projects_layout_connection)
    void viewPopupMenu() {
        PopupMenu popupMenu = new PopupMenu(getContext(), layout_connection);
        popupMenu.getMenuInflater().inflate(R.menu.menu_github_connection, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                disconnectGithub();
                return true;
            }
        });

        popupMenu.show();
    }


    void disconnectGithub() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String UID = mAuth.getCurrentUser().getUid();
        DatabaseReference usersDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(UID);
        usersDatabase.child("github_username").setValue("");
    }

}
