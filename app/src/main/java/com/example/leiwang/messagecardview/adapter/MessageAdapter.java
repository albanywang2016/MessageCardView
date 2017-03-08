package com.example.leiwang.messagecardview.adapter;

import android.content.Context;
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
import com.example.leiwang.messagecardview.model.NewsMessage;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by lei.wang on 3/1/2017.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    public interface RecyclerviewClickListener {
        void onItemClick(NewsMessage item);
    }

    private final List<NewsMessage> items;
    private final Context context;
    private final RecyclerviewClickListener listener;

    public MessageAdapter(Context context, List<NewsMessage> items, RecyclerviewClickListener listener) {
        this.context = context;
        this.items = items;
        this.listener = listener;
    }

    @Override
    public MessageAdapter.MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);

        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MessageAdapter.MessageViewHolder holder, int position) {
        holder.bind(items.get(position), listener);

//        NewsMessage message = items.get(position);
//        holder.title.setText(message.getTitle());
//        holder.source_name.setText(message.getSource_name());
//        holder.time.setText(message.getTime());
//        Uri uri = Uri.parse(message.getImageLink());
//        Context context = holder.image.getContext();
//        Picasso.with(context).load(uri).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder{

        private ImageView image;
        private TextView title;
        private TextView source_name;
        private TextView time;
        private LinearLayout linerLayout;
        private View itemView;

        public MessageViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            linerLayout = (LinearLayout) itemView.findViewById(R.id.ll_layout);
            image = (ImageView) itemView.findViewById(R.id.iv_image);
            title = (TextView) itemView.findViewById(R.id.tv_description);
            source_name = (TextView) itemView.findViewById(R.id.tv_source);
            time = (TextView) itemView.findViewById(R.id.tv_time);
        }


        public void bind(final NewsMessage item, final RecyclerviewClickListener listener) {
            title.setText(item.getTitle());
            source_name.setText(item.getSource_name());
            time.setText(item.getTime());
            //Picasso.with(image.getContext()).load(item.getImageLink()).into(image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(),"itemView position " + Integer.toString(getAdapterPosition()), Toast.LENGTH_LONG ).show();
                    listener.onItemClick(item);
                }
            });
        }
    }
}
