package com.example.nada.devhires.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nada.devhires.R;

public class EmptyViewFragment extends Fragment {


    public EmptyViewFragment() {
        // Required empty public constructor
    }

    public static EmptyViewFragment newInstance(String param1, String param2) {
        EmptyViewFragment fragment = new EmptyViewFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_empty_view, container, false);
    }

}
