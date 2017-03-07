package com.example.leiwang.messagecardview.adapter;

import android.view.View;

/**
 * Created by lei.wang on 3/6/2017.
 */

public interface RecyclerviewClickListener {

    void onRowClicked(int position);
    void onViewClicked(View view, int position);

}
