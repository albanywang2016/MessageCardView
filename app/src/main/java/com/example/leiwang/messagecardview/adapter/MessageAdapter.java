package com.example.leiwang.messagecardview.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.leiwang.messagecardview.R;
import com.example.leiwang.messagecardview.model.NewsMessage;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by lei.wang on 3/1/2017.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<NewsMessage> mList;

    public MessageAdapter(List<NewsMessage> mList) {
        this.mList = mList;
    }

    @Override
    public MessageAdapter.MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);

        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MessageAdapter.MessageViewHolder holder, int position) {
        NewsMessage message = mList.get(position);
        holder.title.setText(message.getTitle());
        holder.source_name.setText(message.getSource_name());
        holder.time.setText(message.getTime());
        Uri uri = Uri.parse(message.getLink());
        Context context = holder.image.getContext();
        Picasso.with(context).load(uri).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder{

        protected ImageView image;
        protected TextView title;
        protected TextView source_name;
        protected TextView time;

        public MessageViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.iv_image);
            title = (TextView) itemView.findViewById(R.id.tv_description);
            source_name = (TextView) itemView.findViewById(R.id.tv_source);
            time = (TextView) itemView.findViewById(R.id.tv_time);

        }
    }
}
