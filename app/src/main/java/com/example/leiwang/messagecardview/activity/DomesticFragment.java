package com.example.leiwang.messagecardview.activity;

import android.content.Intent;
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
import com.example.leiwang.messagecardview.adapter.MessageAdapter;
import com.example.leiwang.messagecardview.model.NewsMessage;

import java.util.List;

/**
 * Created by lei.wang on 4/5/2017.
 */

public class DomesticFragment extends Fragment{

    List<NewsMessage> mList;
    String title;

    public DomesticFragment(List<NewsMessage> mList, String title) {
        this.mList = mList;
        this.title = title;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_domestic, null);

        final RecyclerView rv = (RecyclerView) view.findViewById(R.id.fragment_rv_domestic);
        rv.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        rv.setAdapter(new MessageAdapter(mList, new MessageAdapter.RecyclerviewClickListener() {
            @Override
            public void onClick(View view) {
                int position = rv.getChildLayoutPosition(view);
                NewsMessage item = mList.get(position);

                Intent intent = new Intent(getActivity(), WebViewContents.class);
                intent.putExtra("ArticleURL", item.getLink());
                startActivity(intent);

            }
        }));

        return view;
    }

    @Override
    public String toString() {
        return title;
    }
}
