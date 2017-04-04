package com.example.leiwang.messagecardview.adapter;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.leiwang.messagecardview.R;

import com.example.leiwang.messagecardview.model.NewsMessage;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.leiwang.messagecardview.utils.Const.GET_JSON_VIA_PHP;

/**
 * Created by leiwang on 4/3/17.
 */

public class BlankFragment extends Fragment {


    public BlankFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_blank, container, false);
        RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.rv_recycler_view);
        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);


        return rootView;

    }


}
