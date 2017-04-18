package com.example.leiwang.messagecardview.adapter;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.leiwang.messagecardview.R;

import com.example.leiwang.messagecardview.model.ChannelModel;
import com.example.leiwang.messagecardview.model.NewsMessage;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.leiwang.messagecardview.utils.Const.GET_JSON_VIA_PHP;

/**
 * Created by leiwang on 4/3/17.
 */

public class BlankFragment extends Fragment {
    int color;
    MessageAdapter messageAdapter;

    public BlankFragment() {
    }

    public BlankFragment(int color) {
        this.color = color;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_blank, container, false);
        //final FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.tab_layout);
        FrameLayout frameLayout;
        //frameLayout.setBackgroundColor(color);

        RecyclerView rv = (RecyclerView) view.findViewById(R.id.rv_recycler_view);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity().getBaseContext());
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        List<String> list = new ArrayList<>();
        for(int i=0; i< ChannelModel.data.length; i++){
            list.add(ChannelModel.data[i]);
        }

        return view;

    }
}
