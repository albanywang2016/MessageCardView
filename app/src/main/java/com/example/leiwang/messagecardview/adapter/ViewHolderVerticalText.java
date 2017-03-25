package com.example.leiwang.messagecardview.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.leiwang.messagecardview.R;

/**
 * Created by lei.wang on 3/24/2017.
 */

public class ViewHolderVerticalText extends RecyclerView.ViewHolder {
    public TextView title;

    public ViewHolderVerticalText(View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.tv_vertical_title);
    }


    public TextView getTitle() {
        return title;
    }

    public void setTitle(TextView title) {
        this.title = title;
    }
}
