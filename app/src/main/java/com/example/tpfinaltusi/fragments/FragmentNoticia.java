package com.example.tpfinaltusi.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tpfinaltusi.R;

public class FragmentNoticia extends Fragment {

    public FragmentNoticia() {
        // Required empty public constructor
    }

    public static FragmentNoticia newInstance() {
        FragmentNoticia fragment = new FragmentNoticia();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_noticia, container, false);
    }
}