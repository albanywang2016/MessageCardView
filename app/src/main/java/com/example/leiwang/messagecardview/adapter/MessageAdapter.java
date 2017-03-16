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
import com.example.leiwang.messagecardview.utils.ViewHolderTypeEnum;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by lei.wang on 3/1/2017.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private final List<NewsMessage> items;
    private final RecyclerviewClickListener listener;
    private NetworkImageView imgNetWorkView;


    public interface RecyclerviewClickListener extends View.OnClickListener {
        @Override
        void onClick(View view);
    }

    public MessageAdapter(List<NewsMessage> items, RecyclerviewClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {

        int viewHolderType = 0;

        boolean hasImage = items.get(position).getHas_image();
        if(!hasImage){
            viewHolderType = ViewHolderTypeEnum.NO_IMAGE;
        }else{
            int width = items.get(position).getWidth();
            int height = items.get(position).getHeight();
            if(width > 350){
                viewHolderType = ViewHolderTypeEnum.BIG_IMAGE;
            }else if ((float)(width/height) >= 0.5) {
                viewHolderType = ViewHolderTypeEnum.VERTICAL_IMAGE;
            }else{
                viewHolderType = ViewHolderTypeEnum.HIRIZONAL_IMAGE;
            }
        }

        return viewHolderType;
    }

    @Override
    public MessageAdapter.MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        MessageAdapter.MessageViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch(viewType){
            case ViewHolderTypeEnum.NO_IMAGE:
                View v1 = inflater.inflate(R.layout.card_no_image, parent, false);
                viewHolder = new MessageViewHolder(v1);
                break;
            case ViewHolderTypeEnum.VERTICAL_IMAGE:
                View v2 = inflater.inflate(R.layout.card_vertical_image, parent, false);
                viewHolder = new MessageViewHolder(v2);
                break;
            case ViewHolderTypeEnum.BIG_IMAGE:
                View v3 = inflater.inflate(R.layout.card_big_image, parent,false);
                viewHolder = new MessageViewHolder(v3);
                break;
            case ViewHolderTypeEnum.HIRIZONAL_IMAGE:
                View v4 = inflater.inflate(R.layout.card_vertical_image, parent, false);
                break;
        }

//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
//        view.setOnClickListener(listener);
        return viewHolder;
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


    public static class MessageViewHolder extends RecyclerView.ViewHolder{
        private ImageView image;
        private TextView title;
        private ImageView source_image;
        private TextView time;
        private LinearLayout linerLayout;

        public MessageViewHolder(View itemView) {
            super(itemView);

            image = (ImageView) itemView.findViewById(R.id.iv_image);
            title = (TextView) itemView.findViewById(R.id.tv_description);
            source_image = (ImageView) itemView.findViewById(R.id.iv_source);
            time = (TextView) itemView.findViewById(R.id.tv_time);
        }
    }
}
