package tech.japan.news.messagecardview.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tech.japan.news.messagecardview.R;
import tech.japan.news.messagecardview.adapter.ConnectivityReceiver;
import tech.japan.news.messagecardview.adapter.MessageAdapter;
import tech.japan.news.messagecardview.controller.AppVolleySingleton;
import tech.japan.news.messagecardview.model.NewsMessage;
import tech.japan.news.messagecardview.utils.Const;

/**
 * Created by lei.wang on 8/9/2017.
 */

public class TabFragment extends Fragment implements ConnectivityReceiver.ConnectivityReceiverListener{

    private String channel;
    private RecyclerView rv;
    private ProgressBar pb;
    private List<NewsMessage> messageList;
    boolean isConnected;


    public TabFragment() {
        super();
    }

    public static TabFragment newInstance(int page){
        TabFragment tabFragment = new TabFragment();
        Bundle bundle = new Bundle();
        switch (page){
            case 0:
                bundle.putString(Const.CHANNEL, Const.CHANNEL_DOMESTIC);
                break;
            case 1:
                bundle.putString(Const.CHANNEL, Const.CHANNEL_INTERNATIONAL);
                break;
            case 2:
                bundle.putString(Const.CHANNEL, Const.CHANNEL_BUSINESS);
                break;
            case 3:
                bundle.putString(Const.CHANNEL, Const.CHANNEL_ENTERTAINMENT);
                break;
            case 4:
                bundle.putString(Const.CHANNEL, Const.CHANNEL_SPORT);
                break;
            case 5:
                bundle.putString(Const.CHANNEL, Const.CHANNEL_SCIENCE);
                break;
            case 6:
                bundle.putString(Const.CHANNEL, Const.CHANNEL_LIFE);
                break;
            case 7:
                bundle.putString(Const.CHANNEL, Const.CHANNEL_LOCAL);
                break;
            case 8:
                bundle.putString(Const.CHANNEL, Const.CHANNEL_MAGAZINE);
                break;
            case 9:
                bundle.putString(Const.CHANNEL, Const.CHANNEL_VEDIO);
                break;
            default:
                break;

        }

        tabFragment.setArguments(bundle);
        return tabFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        messageList = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();

        View view = inflater.inflate(R.layout.tab_layout, container, false);
        pb = (ProgressBar) view.findViewById(R.id.tab_progressbar);
        rv = (RecyclerView) view.findViewById(R.id.tab_message);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llm);
        isConnected = ConnectivityReceiver.isConnected();

        if(!isConnected){
            pb.setVisibility(ProgressBar.INVISIBLE);
            Toast.makeText(getActivity(), getResources().getString(R.string.internet_not_connected), Toast.LENGTH_LONG).show();
            return null;
        }else{
            pb.setVisibility(ProgressBar.VISIBLE);
            if(bundle != null){
                channel = bundle.getString(Const.CHANNEL);
                getJsonArrayViaPHP(channel);
            }else{
                Log.d("TabFragmentonCreateView", "channel is empty");
            }
            return view;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(rv != null){
            rv.setAdapter(null);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(rv != null){
            rv.setAdapter(null);
        }
    }

    private void getJsonArrayViaPHP(final String channel) {
        StringRequest sr = new StringRequest(Request.Method.POST, Const.GET_MESSAGE_BY_CHANNEL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                messageList = Arrays.asList(gson.fromJson(response, NewsMessage[].class));

                if(pb != null){
                    pb.setVisibility(ProgressBar.INVISIBLE);
                }
                if(rv != null){
                    rv.setVisibility(RecyclerView.VISIBLE);
                    rv.setAdapter(new MessageAdapter(messageList, new MessageAdapter.RecyclerviewClickListener() {
                        @Override
                        public void onClick(View view) {
                            int position = rv.getChildLayoutPosition(view);
                            NewsMessage item = messageList.get(position);

                            if(item.getChannel().equalsIgnoreCase(Const.CHANNEL_VEDIO)){
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(item.getLink())));
                            }else{
                                startWebViewActivity(item.getLink());
                            }
                        }
                    }));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("getJsonArrayViaPHP", "error = " + error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put(Const.CHANNEL, channel);
                return params;
            }
        };

        sr.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppVolleySingleton.getmInstance().addToRequestQueue(sr, Const.TAG);

    }


    private void startWebViewActivity(String url) {
        Intent intent = new Intent(getActivity(), WebViewContents.class);
        intent.setAction(Intent.ACTION_VIEW);
        intent.putExtra("ArticleURL", url);
        startActivity(intent);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        this.isConnected = isConnected;

    }

}
