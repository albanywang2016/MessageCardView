package com.example.leiwang.messagecardview.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leiwang.messagecardview.R;
import com.example.leiwang.messagecardview.control.MainActivity;
import com.example.leiwang.messagecardview.control.WebViewContents;
import com.example.leiwang.messagecardview.model.NewsMessage;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by lei.wang on 3/1/2017.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<NewsMessage> mList;
    private Context context;
    private RecyclerviewClickListener listener;

    public MessageAdapter(Context context, List<NewsMessage> mList, RecyclerviewClickListener listener) {
        this.context = context;
        this.mList = mList;
        this.listener = listener;
    }

    public MessageAdapter(RecyclerviewClickListener listener) {
        this.listener = listener;
    }

    @Override
    public MessageAdapter.MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);

        return new MessageViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(MessageAdapter.MessageViewHolder holder, int position) {
        NewsMessage message = mList.get(position);
        holder.title.setText(message.getTitle());
        holder.source_name.setText(message.getSource_name());
        holder.time.setText(message.getTime());
        Uri uri = Uri.parse(message.getImageLink());
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
        protected LinearLayout linerLayout;

        public MessageViewHolder(View itemView, final RecyclerviewClickListener listener) {
            super(itemView);
            linerLayout = (LinearLayout) itemView.findViewById(R.id.ll_layout);
            image = (ImageView) itemView.findViewById(R.id.iv_image);
            title = (TextView) itemView.findViewById(R.id.tv_description);
            source_name = (TextView) itemView.findViewById(R.id.tv_source);
            time = (TextView) itemView.findViewById(R.id.tv_time);

            itemView.setClickable(true);
            image.setClickable(true);

            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(image.getContext(),"position " + Integer.toString(getAdapterPosition()), Toast.LENGTH_LONG ).show();
                    if(listener != null){
                        //listener.onRowClicked(getAdapterPosition());
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(),"itemView position " + Integer.toString(getAdapterPosition()), Toast.LENGTH_LONG ).show();
                    if(listener != null){
                        listener.onViewClicked(v,getAdapterPosition());
                    }
                }
            });

        }


    }
}
