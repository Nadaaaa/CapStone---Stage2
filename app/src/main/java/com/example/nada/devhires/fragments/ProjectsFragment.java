package com.example.nada.devhires.fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
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
import com.example.nada.devhires.adapters.RepositoryAdapter;
import com.example.nada.devhires.models.Repository;
import com.example.nada.devhires.network.MyJobService;
import com.example.nada.devhires.network.Retrofit;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
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
    FirebaseJobDispatcher dispatcher;
    private Parcelable saveState;

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

        dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(getContext()));

        ButterKnife.bind(this, rootView);

        if (savedInstanceState != null) {
            saveState = savedInstanceState.getParcelable("SAVE_STATE");
        }

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

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState = mRepositoryList.getLayoutManager().onSaveInstanceState();
        outState.putParcelable("SAVE_STATE", saveState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mRepositoryList.getLayoutManager().onRestoreInstanceState(saveState);
    }

    private void getRepositories() {
        scheduleJob();
        avi.show();
        Retrofit.getService("https://api.github.com/")
                .getRepos(githubUsername)
                .enqueue(new Callback<List<Repository>>() {
                    @Override
                    public void onResponse(Call<List<Repository>> call, Response<List<Repository>> response) {

                        try {
                            if (response.body() != null || response.body().size() <= 0) {
                                tempRepositoryList.addAll(response.body());
                                repositoryAdapter.notifyDataSetChanged();
                            } else if (response.body() == null) {
                                EmptyViewFragment emptyViewFragment = new EmptyViewFragment();
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                fragmentManager
                                        .beginTransaction()
                                        .replace(R.id.fragment_container, emptyViewFragment)
                                        .commit();
                            }
                            avi.hide();
                        } catch (NullPointerException e) {
                            DatabaseReference userDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            userDatabase.child("github_username").setValue("");
                            Toast.makeText(getContext(), R.string.wrong_username, Toast.LENGTH_LONG).show();
                            avi.hide();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Repository>> call, Throwable t) {
                        Toast.makeText(getContext(), getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
                        avi.hide();
                        cancelJob();
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
        cancelJob();
    }

    public void scheduleJob() {
        Job myJob = dispatcher.newJobBuilder()
                .setService(MyJobService.class) // the JobService that will be called
                .setTag("myJob")        // uniquely identifies the job
                .build();

        dispatcher.mustSchedule(myJob);
    }

    public void cancelJob() {
        dispatcher.cancel("myJob");
    }
}
