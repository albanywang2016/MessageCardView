package com.example.leiwang.messagecardview.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.leiwang.messagecardview.R;

/**
 * Created by lei.wang on 3/24/2017.
 */

public class ViewHolderVerticalText extends RecyclerView.ViewHolder {
    public VerticalTextView title;

    public ViewHolderVerticalText(View itemView) {
        super(itemView);
        title = (VerticalTextView) itemView.findViewById(R.id.tv_vertical_title);
    }


    public VerticalTextView getTitle() {
        return title;
    }

    public void setTitle(VerticalTextView title) {
        this.title = title;
    }
}
