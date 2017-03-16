package com.example.leiwang.messagecardview.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.leiwang.messagecardview.R;

/**
 * Created by lei.wang on 3/16/2017.
 */

public class ViewHolderNoImage extends RecyclerView.ViewHolder {

    private TextView title, source, time;

    public ViewHolderNoImage(View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.tv_ni_title);
        source = (TextView) itemView.findViewById(R.id.tv_ni_source);
        time = (TextView) itemView.findViewById(R.id.tv_ni_time);
    }

    public TextView getTitle() {
        return title;
    }

    public void setTitle(TextView title) {
        this.title = title;
    }

    public TextView getSource() {
        return source;
    }

    public void setSource(TextView source) {
        this.source = source;
    }

    public TextView getTime() {
        return time;
    }

    public void setTime(TextView time) {
        this.time = time;
    }
}
