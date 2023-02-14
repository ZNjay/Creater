package com.example.text.fragment;

import android.annotation.SuppressLint;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.example.text.R;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ValidFragment")
public class SecondFragment extends Fragment {


    ArrayAdapter<String> adapter;
    List<String> contactList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.secondfragment, container, false);

        return view;
    }

}
