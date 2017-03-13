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

import com.android.volley.Cache;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.leiwang.messagecardview.R;
import com.example.leiwang.messagecardview.controller.AppVolleySingleton;
import com.example.leiwang.messagecardview.model.NewsMessage;
import com.example.leiwang.messagecardview.utils.Const;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by lei.wang on 3/1/2017.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    public interface RecyclerviewClickListener extends View.OnClickListener {
        @Override
        void onClick(View view);
    }

    private final List<NewsMessage> items;
    private final RecyclerviewClickListener listener;


    private NetworkImageView imgNetWorkView;

    public MessageAdapter(List<NewsMessage> items, RecyclerviewClickListener listener) {

        this.items = items;
        this.listener = listener;
    }

    @Override
    public MessageAdapter.MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        view.setOnClickListener(listener);

        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MessageAdapter.MessageViewHolder holder, int position) {

        NewsMessage message = items.get(position);
        holder.title.setText(message.getTitle());
        switch(message.getSource_name()){
            case Const.ASAHI:
                holder.source_image.setImageResource(R.drawable.asahi);
                break;
            default:
                break;
        }

        Long time = Long.valueOf(message.getMessage_id());
        Long now = System.currentTimeMillis();
        Long diff = now - time;

        long minute = Math.abs((now-time)/60000);
        int hours = (int) minute/60;
        int minutes = (int)minute%60;
        if(hours > 0 ){
            holder.time.setText(hours + " h ago");
        }else{
            holder.time.setText(minutes + " min ago");
        }

        holder.time.setText(message.getTime());
        Uri uri = Uri.parse(message.getImageLink());
        Context context = holder.image.getContext();
        Picasso.with(context).load(uri).noFade().into(holder.image);
        //ImageView view = makeImageRequest(message.getImageLink());
        //holder.image = view;

    }

    private ImageView makeImageRequest(String imageURL) {
        ImageView view = null;
        ImageLoader loader = AppVolleySingleton.getmInstance().getmImageLoader();

        //try cache first
        loader.get(imageURL, ImageLoader.getImageListener(view, R.drawable.ico_loading, R.drawable.ico_error));

        Cache cache = AppVolleySingleton.getmInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(imageURL);
        if(entry != null){
            try{

            }catch(Exception e){
                e.printStackTrace();
            }
        }else{
            imgNetWorkView.setImageUrl(imageURL, loader);
            view = imgNetWorkView ;
        }
        return view;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder{
        private ImageView image;
        private TextView title;
        private ImageView source_image;
        private TextView time;
        private LinearLayout linerLayout;

        public MessageViewHolder(View itemView) {
            super(itemView);
            linerLayout = (LinearLayout) itemView.findViewById(R.id.ll_layout);
            image = (ImageView) itemView.findViewById(R.id.iv_image);
            title = (TextView) itemView.findViewById(R.id.tv_description);
            source_image = (ImageView) itemView.findViewById(R.id.iv_source);
            time = (TextView) itemView.findViewById(R.id.tv_time);
        }
    }
}
