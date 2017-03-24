package com.example.leiwang.messagecardview.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.Cache;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.leiwang.messagecardview.R;
import com.example.leiwang.messagecardview.controller.AppVolleySingleton;
import com.example.leiwang.messagecardview.model.NewsMessage;
import com.example.leiwang.messagecardview.utils.ViewHolderTypeEnum;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by lei.wang on 3/1/2017.
 */

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
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

        int hasImage = items.get(position).getHas_image();
        if(hasImage == 0){
            viewHolderType = ViewHolderTypeEnum.NO_IMAGE;
        }else{
            int width = items.get(position).getWidth();
            int height = items.get(position).getHeight();

            if(width == 0 || height == 0){
                viewHolderType = ViewHolderTypeEnum.NO_IMAGE;
                return viewHolderType;
            }else if(width > 300){
                viewHolderType = ViewHolderTypeEnum.BIG_IMAGE;
            }else if (width > height) {
                viewHolderType = ViewHolderTypeEnum.VERTICAL_IMAGE;
            }else{
                //viewHolderType = ViewHolderTypeEnum.HIRIZONAL_IMAGE;
                viewHolderType = ViewHolderTypeEnum.VERTICAL_TEXT;
            }
        }

        return viewHolderType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch(viewType){
            case ViewHolderTypeEnum.NO_IMAGE:
                View v1 = inflater.inflate(R.layout.card_no_image, parent, false);
                viewHolder = new ViewHolderNoImage(v1);
                v1.setOnClickListener(listener);
                break;
            case ViewHolderTypeEnum.VERTICAL_IMAGE:
                View v2 = inflater.inflate(R.layout.card_vertical_image, parent, false);
                viewHolder = new ViewHolderVerticalImage(v2);
                v2.setOnClickListener(listener);
                break;
            case ViewHolderTypeEnum.BIG_IMAGE:
                View v3 = inflater.inflate(R.layout.card_big_image, parent,false);
                viewHolder = new ViewHolderBigImage(v3);
                v3.setOnClickListener(listener);
                break;
            case ViewHolderTypeEnum.HIRIZONAL_IMAGE:
                View v4 = inflater.inflate(R.layout.card_horizonal_image, parent, false);
                viewHolder = new ViewHolderHorizonalImage(v4);
                v4.setOnClickListener(listener);
                break;
            case ViewHolderTypeEnum.VERTICAL_TEXT:
                View v5 = inflater.inflate(R.layout.card_vertical_text, parent, false);
                viewHolder = new ViewHolderVerticalText(v5);
                v5.setOnClickListener(listener);
                break;
        }

//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
//        view.setOnClickListener(listener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch(holder.getItemViewType()){
            case ViewHolderTypeEnum.NO_IMAGE:
                ViewHolderNoImage viewHolderNoImage = (ViewHolderNoImage) holder;
                configureNoImageHolder(viewHolderNoImage, position);
                break;
            case ViewHolderTypeEnum.BIG_IMAGE:
                ViewHolderBigImage viewHolderBigImage = (ViewHolderBigImage) holder;
                configureBigImageHolder(viewHolderBigImage, position);
                break;
            case ViewHolderTypeEnum.HIRIZONAL_IMAGE:
                ViewHolderHorizonalImage viewHolderHorizonalImage = (ViewHolderHorizonalImage) holder;
                configureHorizonalImageHolder(viewHolderHorizonalImage, position);
                break;
            case ViewHolderTypeEnum.VERTICAL_IMAGE:
                ViewHolderVerticalImage viewHolderVertialImage = (ViewHolderVerticalImage) holder;
                configureVerticalImageHolder(viewHolderVertialImage, position);
                break;
            case ViewHolderTypeEnum.VERTICAL_TEXT:
                ViewHolderVerticalText viewHolderVerticalText = (ViewHolderVerticalText) holder;
                configureVerticalTextHolder(viewHolderVerticalText, position);
                break;
            default:
                break;

        }

    }

    private void configureNoImageHolder(ViewHolderNoImage holder, int position) {
        NewsMessage message = items.get(position);
        holder.title.setText(message.getTitle());
        holder.source.setText(message.getSource_name());
        holder.time.setText(message.getPub_date());

    }

    private void configureBigImageHolder(ViewHolderBigImage holder, int position) {
        NewsMessage message = items.get(position);
        holder.title.setTextSize(20);
        holder.title.setText(message.getTitle());

        holder.source.setText(message.getSource_name());
        holder.time.setText(message.getPub_date());

        Uri uri = Uri.parse(message.getImageLink());
        Context context = holder.iv_image.getContext();
        final float scale = context.getResources().getDisplayMetrics().density;
        int width = (int) (message.getWidth() * scale);
        int height = (int) (message.getHeight() * scale);
//        holder.iv_image.getLayoutParams().width = width;
//        holder.iv_image.setAdjustViewBounds(true);
//        holder.iv_image.setScaleType((ImageView.ScaleType.CENTER_CROP));

        //holder.iv_image.getLayoutParams().height = (int) (message.getHeight());

        Picasso.with(context).load(uri).resize(width,height).centerCrop().into(holder.iv_image);
    }

    private void configureHorizonalImageHolder(ViewHolderHorizonalImage holder, int position) {
        NewsMessage message = items.get(position);
        //holder.time.setText(message.getPub_date());
        holder.title.setText(message.getTitle());
       // holder.source.setText(message.getSource_name());

        Uri uri = Uri.parse(message.getImageLink());
        Context context = holder.iv_image.getContext();

        final float scale = context.getResources().getDisplayMetrics().density;
        int width = (int) (message.getWidth() * scale);
        int height = (int) (message.getHeight() * scale);

        //Picasso.with(context).load(uri).resize(width,height).into(holder.iv_image);

    }

    private void configureVerticalImageHolder(ViewHolderVerticalImage holder, int position) {

        NewsMessage message = items.get(position);
        holder.title.setText(message.getTitle());
        holder.source.setText(message.getSource_name());
        holder.time.setText(message.getPub_date());

        //holder.iv_image.setAdjustViewBounds(true);
        Uri uri = Uri.parse(message.getImageLink());
        Context context = holder.iv_image.getContext();
        final float scale = context.getResources().getDisplayMetrics().density;
        int width = (int) (message.getWidth() * scale);
        int height = (int) (message.getHeight() * scale);

        //holder.iv_image.getLayoutParams().width = width;
        //holder.iv_image.getLayoutParams().height = height;

        Picasso.with(context).load(uri).fit().into(holder.iv_image);
        //ImageView view = makeImageRequest(message.getImageLink());
        //holder.image = view;

    }

    private void configureVerticalTextHolder(ViewHolderVerticalText holder, int position) {
        NewsMessage message = items.get(position);

        holder.title.setText("this is just a vertical test message, that you will see this message in vertical instead of horizonal.");

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

//
//    public static class MessageViewHolder extends RecyclerView.ViewHolder{
//        private ImageView image;
//        private TextView title;
//        private ImageView source_image;
//        private TextView time;
//        private LinearLayout linerLayout;
//
//        public MessageViewHolder(View itemView) {
//            super(itemView);
//
//            image = (ImageView) itemView.findViewById(R.id.iv_image);
//            title = (TextView) itemView.findViewById(R.id.tv_description);
//            source_image = (ImageView) itemView.findViewById(R.id.iv_source);
//            time = (TextView) itemView.findViewById(R.id.tv_time);
//        }
//    }
}
