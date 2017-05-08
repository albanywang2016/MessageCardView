package tech.japan.news.messagecardview.activity;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;


/**
 * Created by lei.wang on 3/6/2017.
 */

public class WebViewContents extends Activity {

    WebView webView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(tech.japan.news.messagecardview.R.layout.progressbar);

        webView = new WebView(this);
        String url = getIntent().getStringExtra("ArticleURL");

        if(url == null){
            //do sth here
        }

        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                setContentView(webView);
            }
        });

    }
}
