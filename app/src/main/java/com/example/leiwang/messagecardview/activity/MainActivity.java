package com.example.leiwang.messagecardview.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonValue;

import com.example.leiwang.messagecardview.R;
import com.example.leiwang.messagecardview.adapter.MessageAdapter;
import com.example.leiwang.messagecardview.controller.AppVolleySingleton;
import com.example.leiwang.messagecardview.model.NewsMessage;
import com.example.leiwang.messagecardview.utils.Const;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.leiwang.messagecardview.utils.Const.GET_JSON_VIA_PHP;

public class MainActivity extends AppCompatActivity {

    RecyclerView rv;
    MessageAdapter ma;
    List<NewsMessage> messageList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rv = (RecyclerView) findViewById(R.id.messageList);
        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llm);

        messageList = new ArrayList<>();

        //prepareMessageList(readFile(FILE_PATH));

        //prepareMessageList();

        //make json array request to retrieve Json object via http web
        //getJsonArrayRequest();

        //get Json array view php
        getJsonArrayViaPHP();

        Button registerBtn = (Button) findViewById(R.id.bt_login);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callLoginDialog();
            }
        });

    }

    private void callLoginDialog(){

        final Dialog myDialog = new Dialog(this);
        myDialog.setContentView(R.layout.login_layout);
        myDialog.setCancelable(false);
        final Button login = (Button) myDialog.findViewById(R.id.loginButton);

        final EditText etUserName = (EditText) myDialog.findViewById(R.id.login_et_username);
        final EditText etPassword = (EditText) myDialog.findViewById(R.id.login_et_password);
        myDialog.show();

        final Button register = (Button) myDialog.findViewById(R.id.login_btn_register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callRegisterDialog();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = etUserName.getText().toString().trim();
                final String password = etPassword.getText().toString().trim();
                if(username.length() == 0 || password.length() == 0){
                    showAlert(Const.OOPS, Const.INPUT_WRONG);
                    return;
                }else{
                    StringRequest sr = new StringRequest(Request.Method.POST, Const.USER_LOGIN_PHP, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            myDialog.dismiss();
                            showAlert("", response.toString());
                            return;

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            myDialog.dismiss();
                            showAlert("Error!", error.toString());
                            return;
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> params = new HashMap<String, String>();
                            params.put(Const.KEY_USERNAME,username);
                            params.put(Const.KEY_PASSWORD,password);
                            return params;
                        }
                    };
                    AppVolleySingleton.getmInstance().addToRequestQueue(sr, Const.TAG);
                }
            }
        });

    }



    private void callRegisterDialog()
    {
        final Dialog myDialog = new Dialog(this);
        myDialog.setContentView(R.layout.register_layout);
        myDialog.setCancelable(false);
        final Button register = (Button) myDialog.findViewById(R.id.registerButton);

        final EditText etUsername = (EditText) myDialog.findViewById(R.id.register_et_username);
        final EditText etEmail = (EditText) myDialog.findViewById(R.id.register_et_email);
        final EditText etPassword = (EditText) myDialog.findViewById(R.id.register_et_password);
        myDialog.show();

        final Button login = (Button) myDialog.findViewById(R.id.register_btn_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callLoginDialog();
            }
        });

        register.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                //your login calculation goes here
                final String username = etUsername.getText().toString().trim();
                final String email = etEmail.getText().toString().trim();
                final String password = etPassword.getText().toString().trim();

                if(username.length() == 0 || email.length() == 0 || password.length() == 0){
                    showAlert(Const.OOPS, Const.INPUT_WRONG);

                    return;
                }else{
                    StringRequest sr = new StringRequest(Request.Method.POST, Const.USER_REGISTER_PHP, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            myDialog.dismiss();
                            showAlert("", response.toString());

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            myDialog.dismiss();
                            showAlert("Error!", error.toString());

                            return;
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> params = new HashMap<String, String>();
                            params.put(Const.KEY_USERNAME,username);
                            params.put(Const.KEY_PASSWORD,password);
                            params.put(Const.KEY_EMAIL, email);
                            return params;
                        }
                    };
                    AppVolleySingleton.getmInstance().addToRequestQueue(sr, Const.TAG);
                }

            }
        });


    }

    private void showAlert(String title, String alert){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(alert)
                .setTitle(title)
                .setPositiveButton(android.R.string.ok, null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }


//    public void setFloatingActionButton(final View view) {
//        float actionButton = (android.support.design.widget.FloatingActionButton) getActivity()
//                .findViewById(R.id.float);
//        actionButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView
//                        .getLayoutManager();
//                layoutManager.scrollToPositionWithOffset(0, 0);
//            }
//        });
//    }



    private void getJsonArrayViaPHP() {

        StringRequest sr = new StringRequest(Request.Method.GET, GET_JSON_VIA_PHP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Log.d("getJsonArrayViaPHP", "the response is =" + response);
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                messageList = Arrays.asList(gson.fromJson(response, NewsMessage[].class));
                //Log.d("getJsonArrayViaPHP", "message List = " + messageList.toString());
                rv.setAdapter(new MessageAdapter(messageList, new MessageAdapter.RecyclerviewClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = rv.getChildLayoutPosition(view);
                        NewsMessage item = messageList.get(position);
                        startWebViewActivity(item.getLink());
                    }
                }));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("getJsonArrayViaPHP", "error = " + error.toString());
            }
        });

        sr.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppVolleySingleton.getmInstance().addToRequestQueue(sr, Const.TAG);

    }

    private void getJsonArrayRequest() {
        JsonObjectRequest objReq = new JsonObjectRequest(Request.Method.GET, Const.ASAHI_JSON, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //parseJsonObject(response.toString());

                rv.setAdapter(new MessageAdapter(messageList, new MessageAdapter.RecyclerviewClickListener() {

                    @Override
                    public void onClick(View view) {
                        int position = rv.getChildLayoutPosition(view);
                        NewsMessage item = messageList.get(position);
                        //Toast.makeText(view.getContext(), item.getTitle(), Toast.LENGTH_LONG).show();
                        startWebViewActivity(item.getLink());
                    }
                }));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(Const.TAG, "Error: " + error.getMessage());
            }
        });

        AppVolleySingleton.getmInstance().addToRequestQueue(objReq, Const.TAG);

    }

    private void parseJsonArray(String result) {
        JsonArray array = Json.parse(result).asArray();
        for (JsonValue value : array) {
            messageList.add(new NewsMessage(value.asObject().getString(Const.ID, ""),
                    value.asObject().getString(Const.SOURCE_NAME, ""),
                    value.asObject().getString(Const.CHANNEL, ""),
                    value.asObject().getString(Const.TITLE, ""),
                    value.asObject().getString(Const.LINK, ""),
                    value.asObject().getInt(Const.HAS_IMAGE, 0),
                    value.asObject().getString(Const.PUB_DATE, ""),
                    value.asObject().getString(Const.IMAGE_URL, ""),
                    value.asObject().getInt(Const.IMAGE_WIDTH, 0),
                    value.asObject().getInt(Const.IMAGE_HEIGHT, 0)));
        }

    }

    private void parseStringToJsonArray(JSONArray response)  {

        for(int i=0; i< response.length(); i++){
            try {
                JSONObject item = (JSONObject) response.get(i);
                NewsMessage message = new NewsMessage();
                message.setId(item.getString(Const.ID));
                message.setSource_name(item.getString(Const.SOURCE_NAME));
                message.setChannel(item.getString(Const.CHANNEL));
                message.setTitle(item.getString(Const.TITLE));
                message.setLink(item.getString(Const.LINK));
                message.setHas_image(item.getInt(Const.HAS_IMAGE));
                message.setPub_date(item.getString(Const.PUB_DATE));
                message.setImage_url(item.getString(Const.IMAGE_URL));
                message.setImage_width(item.getInt(Const.IMAGE_WIDTH));
                message.setImage_height(item.getInt(Const.IMAGE_HEIGHT));
                messageList.add(message);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


    }

//    private void parseJsonObject(String result) {
//
//        JsonArray array = Json.parse(result).asObject().get(Const.ITEM).asArray();
//        for (JsonValue value : array) {
//            NewsMessage message = new NewsMessage(value.asObject().getString(Const.MESSAGE_ID, ""),
//                    value.asObject().getString(Const.SOURCE_NAME, ""),
//                    value.asObject().getString(Const.TITLE, ""),
//                    value.asObject().getString(Const.PUB_DATE, ""),
//                    value.asObject().getString(Const.LINK, ""),
//                    value.asObject().getString(Const.CONTENTS_URL, ""),
//                    value.asObject().getString(Const.CONTENTS, ""),
//                    value.asObject().getInt(Const.HAS_IMAGE, 0),
//                    value.asObject().getInt(Const.IMAGE_WIDTH, 0),
//                    value.asObject().getInt(Const.IMAGE_HEIGHT, 0));
//
//            Log.i(Const.TAG, ("has_image = " + message.getHas_image() + ", image_width = " + message.getImage_width() + ", image_height = " + message.getImage_height()));
//            messageList.add(message);
//
//
//        }
//
//    }

    private void startWebViewActivity(String url) {
        Intent intent = new Intent(this, WebViewContents.class);
        intent.putExtra("ArticleURL", url);
        startActivity(intent);
    }

    private void prepareMessageList() {

//        messageList.add(new NewsMessage("1488744605300", "Asahi", "長野のヘリ墜落、新たにパイロットの死亡確認", "2017-03-06T04:24:03+09:00", "", "http://mainichi.jp/articles/20170313/k00/00m/050/112000c", "<!-- Ad BGN -->\n<div class=\"AdMod\">\n <!-- impact 社会記事中レクタングル(Aone仕様) RECT4★ここから-->\n <script type=\"text/javascript\" language=\"JavaScript\"><!-- impAcn = \"ASAHISEG\";impAco = document.cookie;impApos = impAco.indexOf(impAcn+\"=\");impAseg = (impApos!=-1)?impAco.substring(impApos+impAcn.length+1,(impAco.indexOf(\"; \",impApos)!=-1)?impAco.indexOf(\"; \",impApos):impAco.length):null;impAseg = (impAseg)?unescape(impAseg):null;impAserver = 'http://imp.asahi.com';impAtag = \"/SITE=SYAKAI/AREA=RECT4/AAMSZ=300X250/OENCJP=UTF8\";impAtarget = (impAseg)?impAtag+\"/\"+impAseg:impAtag;impArnd = Math.round(Math.random() * 100000000);if (!impApid) var impApid = Math.round(Math.random() * 100000000);document.write('<scr');document.write('ipt src=\"' + impAserver + '/jserver/acc_random=' + impArnd + impAtarget + '/pageid=' + impApid + '\">');document.write('</scr');document.write('ipt>');//--></script>\n <span>[PR]</span>\n <!-- impact 社会記事中レクタングル(Aone仕様) RECT4★ここまで-->\n</div>\n<!-- Ad END -->\n<!-- ArticleText BGN -->\n<div class=\"ArticleText\">\n <!--googleon:index-->\n <p>　長野県松本、岡谷の市境にある鉢伏山付近５日午後、県の防災ヘリコプター「アルプス」が墜落した事故で、県警は６日、死亡が確認された３人のうち１人の身元について、県消防防災航空隊のパイロット岩田正滋さん（５６）＝同県松本市神林＝と判明した、と発表した。</p>\n <p>　岩田さんは機体の外で発見され、搬送先の松本市内の病院で死亡が確認された。</p>\n <!--googleoff:index-->\n</div>\n<!-- ArticleText END -->\n<!-- 緊急時用リンクはありません -->"));
//        messageList.add(new NewsMessage("1488744606407", "Asahi", "強盗殺人容疑、自首した男逮捕　名古屋の老夫婦殺害事件", "2017-03-06T04:24:03+09:00", "", "http://www.asahi.com/articles/ASK361CSSK35UOOB01Q.html?ref=rss", "<!-- Ad BGN -->\n<div class=\"AdMod\">\n <!-- impact 社会記事中レクタングル(Aone仕様) RECT4★ここから-->\n <script type=\"text/javascript\" language=\"JavaScript\"><!-- impAcn = \"ASAHISEG\";impAco = document.cookie;impApos = impAco.indexOf(impAcn+\"=\");impAseg = (impApos!=-1)?impAco.substring(impApos+impAcn.length+1,(impAco.indexOf(\"; \",impApos)!=-1)?impAco.indexOf(\"; \",impApos):impAco.length):null;impAseg = (impAseg)?unescape(impAseg):null;impAserver = 'http://imp.asahi.com';impAtag = \"/SITE=SYAKAI/AREA=RECT4/AAMSZ=300X250/OENCJP=UTF8\";impAtarget = (impAseg)?impAtag+\"/\"+impAseg:impAtag;impArnd = Math.round(Math.random() * 100000000);if (!impApid) var impApid = Math.round(Math.random() * 100000000);document.write('<scr');document.write('ipt src=\"' + impAserver + '/jserver/acc_random=' + impArnd + impAtarget + '/pageid=' + impApid + '\">');document.write('</scr');document.write('ipt>');//--></script>\n <span>[PR]</span>\n <!-- impact 社会記事中レクタングル(Aone仕様) RECT4★ここまで-->\n</div>\n<!-- Ad END -->\n<!-- ArticleText BGN -->\n<div class=\"ArticleText\">\n <!--googleon:index-->\n <p>　名古屋市南区の住宅で２日朝、８０代の夫婦が殺害されているのが見つかった事件で、愛知県警は５日、夫婦宅近くのアパートに住む無職松井広志容疑者（４２）を強盗殺人容疑で緊急逮捕し、発表した。容疑をおおむね認めているという。捜査関係者によると、松井容疑者の部屋には夫婦から奪ったとみられる財布があったほか、包丁も見つかり押収したという。</p>\n <p>　県警は、この包丁が凶器に使われたとみて鑑定するとともに、事件の詳しい動機などの調べを進める。</p>\n <p>　発表によると、松井容疑者は１日午後８時ごろ、同区豊１丁目の住宅で、いずれも無職の大島克夫さん（８３）と妻たみ子さん（８０）の首などを刃物で突き刺すなどして殺害し、財布を奪った疑いがある。</p>\n <p>　松井容疑者は４日午後６時４５分ごろ、捜査本部のある南署を訪れ、「豊１丁目の件です」と自首したという。供述に基づき県警が松井容疑者のアパートを捜索したところ、財布などが見つかったため緊急逮捕した。</p>\n <!--googleoff:index-->\n</div>\n<!-- ArticleText END -->\n<!-- 緊急時用リンクはありません -->"));
//        messageList.add(new NewsMessage("1488744607003", "Asahi", "北陸新幹線、一時運転見合わせ　人身事故の影響", "2017-03-06T04:24:03+09:00", "", "http://www.asahi.com/articles/ASK361CSSK35UOOB01Q.html?ref=rss", "<!-- Ad BGN -->\\n<div class=\\\"AdMod\\\">\\n <!-- impact 速報記事中レクタングル(Aone仕様) RECT4★ここから-->\\n <script type=\\\"text/javascript\\\" language=\\\"JavaScript\\\"><!-- impAcn = \\\"ASAHISEG\\\";impAco = document.cookie;impApos = impAco.indexOf(impAcn+\\\"=\\\");impAseg = (impApos!=-1)?impAco.substring(impApos+impAcn.length+1,(impAco.indexOf(\\\"; \\\",impApos)!=-1)?impAco.indexOf(\\\"; \\\",impApos):impAco.length):null;impAseg = (impAseg)?unescape(impAseg):null;impAserver = 'http://imp.asahi.com';impAtag = \\\"/SITE=SOKUHOU/AREA=RECT4/AAMSZ=300X250/OENCJP=UTF8\\\";impAtarget = (impAseg)?impAtag+\\\"/\\\"+impAseg:impAtag;impArnd = Math.round(Math.random() * 100000000);if (!impApid) var impApid = Math.round(Math.random() * 100000000);document.write('<scr');document.write('ipt src=\\\"' + impAserver + '/jserver/acc_random=' + impArnd + impAtarget + '/pageid=' + impApid + '\\\">');document.write('</scr');document.write('ipt>');//--></script>\\n <span>[PR]</span>\\n <!-- impact 速報記事中レクタングル(Aone仕様) RECT4★ここまで-->\\n</div>\\n<!-- Ad END -->\\n<!-- ArticleText BGN -->\\n<div class=\\\"ArticleText\\\">\\n <!--googleon:index-->\\n <p>　【ＪＲ東日本発表】北陸新幹線は５日午後１０時２０分ごろに佐久平―上田駅間で起きた人身事故の影響で、東京―長野駅間の上下線で運転を一時見合わせていたが、上り線は午後１１時５５分ごろに運転を再開した。その後、下り線も６日午前２時２０分ごろに再開した。</p>\\n <!--googleoff:index-->\\n</div>\\n<!-- ArticleText END -->\\n<!-- 緊急時用リンクはありません -->\\n<div class=\\\"RelatedLinkMod\\\">\\n <div class=\\\"Title\\\">\\n  関連ニュース\\n </div>\\n <ul>\\n  <li><a href=\\\"http://www.asahi.com/articles/ASK2X2TBZK2XUOOB002.html\\\">リニア開業予定、すでにギリギリ？　掘削作業ずれ込み</a></li>\\n  <li><a href=\\\"http://www.asahi.com/articles/ASK2Q42NTK2QPTIL00H.html\\\">豪華寝台列車「瑞風」　深緑の先頭車両、初お目見え</a></li>\\n  <li><a href=\\\"http://www.asahi.com/articles/ASK2M513JK2MUTIL00C.html\\\">多摩の人の流れ、一変させたモノレール　上北台駅</a></li>\\n  <li><a href=\\\"http://www.asahi.com/articles/ASK343VQLK34TIPE007.html\\\">警察の刺しゅうにワッペン…６億円金塊盗難、綿密な計画</a></li>\\n  <li><a href=\\\"http://www.asahi.com/articles/ASK2K61QZK2KUUPI005.html\\\">１歳、歩き始めた矢先に…　小さなことが重なった事故</a></li>\\n </ul>\\n</div>"));
//        messageList.add(new NewsMessage("1488744607529", "Asahi", "博多の金塊盗難、都内の貴金属店を捜索　換金関与か", "2017-03-06T04:24:03+09:00", "http://www.asahicom.jp/articles/images/AS20170305002375_commL.jpg", "http://www.asahi.com/articles/ASK361CSSK35UOOB01Q.html?ref=rss", "<div class=\"ImagesMod\">\n <div class=\"Image\">\n  <p class=\"Height\"><a href=\"./photo/AS20170305002375.html\"><img alt=\"写真・図版\" oncontextmenu=\"return false\" src=\"//www.asahicom.jp/articles/images/AS20170305002375_commL.jpg\"><em class=\"Caption\">犯行グループの動きと金塊の流れ</em></a></p>\n </div>\n <!--Image-->\n</div>\n<!--ImagesMod-->\n<!-- Ad BGN -->\n<div class=\"AdMod\">\n <!-- impact 社会記事中レクタングル(Aone仕様) RECT4★ここから-->\n <script type=\"text/javascript\" language=\"JavaScript\"><!-- impAcn = \"ASAHISEG\";impAco = document.cookie;impApos = impAco.indexOf(impAcn+\"=\");impAseg = (impApos!=-1)?impAco.substring(impApos+impAcn.length+1,(impAco.indexOf(\"; \",impApos)!=-1)?impAco.indexOf(\"; \",impApos):impAco.length):null;impAseg = (impAseg)?unescape(impAseg):null;impAserver = 'http://imp.asahi.com';impAtag = \"/SITE=SYAKAI/AREA=RECT4/AAMSZ=300X250/OENCJP=UTF8\";impAtarget = (impAseg)?impAtag+\"/\"+impAseg:impAtag;impArnd = Math.round(Math.random() * 100000000);if (!impApid) var impApid = Math.round(Math.random() * 100000000);document.write('<scr');document.write('ipt src=\"' + impAserver + '/jserver/acc_random=' + impArnd + impAtarget + '/pageid=' + impApid + '\">');document.write('</scr');document.write('ipt>');//--></script>\n <span>[PR]</span>\n <!-- impact 社会記事中レクタングル(Aone仕様) RECT4★ここまで-->\n</div>\n<!-- Ad END -->\n<!-- ArticleText BGN -->\n<div class=\"ArticleText\">\n <!--googleon:index-->\n <p>　福岡市博多区のＪＲ博多駅前で昨年７月、６億円相当の金塊が警察官を装った男らに盗まれた事件で、犯行グループがレンタカーを使い、事件当日に広島県内で返却していたことが捜査関係者への取材でわかった。警官を装った服が山口県内で捨てられていたことも判明しており、福岡県警は車の借り主や移動ルートを調べている。</p>\n <p>　金塊は事件後の１週間で約４億円分が換金された。福岡県警は５日、換金に関わった疑いがあるとして、６０代男性が経営する東京都内の貴金属店や男性の千葉県内の自宅などを、盗品等処分あっせん容疑で家宅捜索した。</p>\n <p>　事件は昨年７月上旬の午前、ＪＲ博多駅近くのビルの入り口付近で発生。ビルに入る貴金属店に金塊を売るために持ち込もうとしていた被害男性らの前に、警察官のようなベストを着た数人の男が現れた。「密輸品なのは分かっているぞ」などと言い、金塊の入ったケース５個を調べるふりをして、男性らが目を離したすきに車に積んで逃げた。</p>\n <p>　金塊は約１６０キロあり、被害…</p>\n <!--googleoff:index-->\n</div>\n<!-- ArticleText END -->\n<!-- 有料記事　続きを読む BGN -->\n<div class=\"LoginSelectArea\" id=\"KeySilver\">\n <div class=\"ArticleTypeArea\"></div>\n <div class=\"MoveLink\">\n  <p class=\"Count\">残り：299文字／全文：708文字</p>\n  <ul>\n   <li class=\"ReadMore\"><a href=\"http://digital.asahi.com/info/information/free_member/?jumpUrl=http%3A%2F%2Fdigital.asahi.com%2Farticles%2FASK3531XXK35TIPE003.html%3F_requesturl%3Darticles%2FASK3531XXK35TIPE003.html\" class=\"NonRegi\" onclick=\"s.getPreviousValue('fn3','sc_clk_btn','');\"><span>無料登録して全文を読む</span></a></li>\n   <li class=\"ReadMore\"><a href=\"http://digital.asahi.com/articles/ASK3531XXK35TIPE003.html?rm=409\" class=\"Lite Member\" onclick=\"s.getPreviousValue('fn1','sc_clk_btn','');\"><span>全文を読む</span></a></li>\n   <li class=\"ReadMore\"><a href=\"http://digital.asahi.com/rd/lgck2.html?jumpUrl=http%3A%2F%2Fdigital.asahi.com%2Farticles%2FASK3531XXK35TIPE003.html%3F_requesturl%3Darticles%2FASK3531XXK35TIPE003.html%26rm%3D409\" class=\"Logout Member\" onclick=\"s.getPreviousValue('fn2','sc_clk_btn','');\"><span>ログインして全文を読む</span></a></li>\n   <li class=\"ReadMore\">\n    <noscript>\n     <a href=\"http://digital.asahi.com/rd/lgck2.html?jumpUrl=http%3A%2F%2Fdigital.asahi.com%2Farticles%2FASK3531XXK35TIPE003.html%3F_requesturl%3Darticles%2FASK3531XXK35TIPE003.html%26rm%3D409\" class=\"NoScript\"><span>ログインして全文を読む</span></a>\n    </noscript></li>\n  </ul>\n </div>\n</div>\n<!-- 有料記事　続きを読む END -->\n<!--ArticleNoteBox BGN -->\n<div class=\"ArticleNoteBox Lite LiteOut\">\n <p>有料会員に登録すると全ての記事が読み放題です。</p>\n <p>初月無料につき月初のお申し込みがお得</p>\n <p><span class=\"EmText\">980円で月300本まで</span>読めるシンプルコースは<a href=\"http://digital.asahi.com/info/simple_course/?iref=com_smpl_txtpr\">こちら</a></p>\n</div>\n<!--ArticleNoteBox END -->\n<!-- 緊急時用リンクはありません -->\n<div class=\"RelatedLinkMod\">\n <div class=\"Title\">\n  関連ニュース\n </div>\n <ul>\n  <li><a href=\"http://www.asahi.com/articles/ASK343VQLK34TIPE007.html\">警察の刺しゅうにワッペン…６億円金塊盗難、綿密な計画</a></li>\n  <li><a href=\"http://www.asahi.com/articles/ASK343VQLK34TIPE006.html\">盗難の６億円金塊、４億円分換金　貴金属店が名義貸しか</a></li>\n  <li><a href=\"http://www.asahi.com/articles/ASJCX4WG8JCXTIPE01C.html\">金の密輸横行、消費税逃れ利ざや　自家用機に大量隠匿も</a></li>\n  <li><a href=\"http://www.asahi.com/articles/ASK355T47K35UTIL013.html\">晴天の上空、なぜ墜落？　長野県防災ヘリ、裏返し大破</a></li>\n  <li><a href=\"http://www.asahi.com/articles/ASK355GFDK35UOOB00N.html\">長野県防災ヘリが墜落、３人死亡２人意識不明　９人搭乗</a></li>\n </ul>\n</div>"));
//        messageList.add(new NewsMessage("1488744608953", "Asahi", "上空で何が？　長野県防災ヘリ、裏返し大破", "2017-03-06T04:24:03+09:00", "http://www.asahicom.jp/articles/images/AS20170305002408_commL.jpg", "http://www.asahi.com/articles/ASK361CSSK35UOOB01Q.html?ref=rss", "<div class=\"ImagesMod\">\n <div class=\"Image\">\n  <p class=\"Height\"><a href=\"./photo/AS20170305002408.html\"><img alt=\"写真・図版\" oncontextmenu=\"return false\" src=\"//www.asahicom.jp/articles/images/AS20170305002408_commL.jpg\"><em class=\"Caption\">墜落した防災ヘリと救助活動を行う県警のヘリ＝５日午後５時５分、鉢伏山付近、朝日新聞社ヘリから、竹花徹朗撮影</em></a></p>\n  <ul class=\"Thum\">\n   <li><a href=\"./photo/AS20170305002469.html\"><span class=\"Width\"><img alt=\"写真・図版\" oncontextmenu=\"return false\" src=\"//www.asahicom.jp/articles/images/AS20170305002469_commL.jpg\"></span></a></li>\n   <li><a href=\"./photo/AS20170305002311.html\"><span class=\"Width\"><img alt=\"写真・図版\" oncontextmenu=\"return false\" src=\"//www.asahicom.jp/articles/images/AS20170305002311_commL.jpg\"></span></a></li>\n   <li><a href=\"./photo/AS20170305002312.html\"><span class=\"Width\"><img alt=\"写真・図版\" oncontextmenu=\"return false\" src=\"//www.asahicom.jp/articles/images/AS20170305002312_commL.jpg\"></span></a></li>\n  </ul>\n </div>\n <!--Image-->\n</div>\n<!--ImagesMod-->\n<!-- Ad BGN -->\n<div class=\"AdMod\">\n <!-- impact 社会記事中レクタングル(Aone仕様) RECT4★ここから-->\n <script type=\"text/javascript\" language=\"JavaScript\"><!-- impAcn = \"ASAHISEG\";impAco = document.cookie;impApos = impAco.indexOf(impAcn+\"=\");impAseg = (impApos!=-1)?impAco.substring(impApos+impAcn.length+1,(impAco.indexOf(\"; \",impApos)!=-1)?impAco.indexOf(\"; \",impApos):impAco.length):null;impAseg = (impAseg)?unescape(impAseg):null;impAserver = 'http://imp.asahi.com';impAtag = \"/SITE=SYAKAI/AREA=RECT4/AAMSZ=300X250/OENCJP=UTF8\";impAtarget = (impAseg)?impAtag+\"/\"+impAseg:impAtag;impArnd = Math.round(Math.random() * 100000000);if (!impApid) var impApid = Math.round(Math.random() * 100000000);document.write('<scr');document.write('ipt src=\"' + impAserver + '/jserver/acc_random=' + impArnd + impAtarget + '/pageid=' + impApid + '\">');document.write('</scr');document.write('ipt>');//--></script>\n <span>[PR]</span>\n <!-- impact 社会記事中レクタングル(Aone仕様) RECT4★ここまで-->\n</div>\n<!-- Ad END -->\n<!-- ArticleText BGN -->\n<div class=\"ArticleText\">\n <!--googleon:index-->\n <p>　雪が積もる急斜面に、上下逆になって大破した機体。山岳救助訓練で飛び立った長野県の防災ヘリが５日午後、同県松本、岡谷両市境の山中で見つかった。消防隊員ら乗員９人のうち、３人の死亡が確認された大事故。上空で何があったのか。</p>\n <div class=\"ExtendedLinkMod\">\n  <ul>\n   <li><a href=\"http://www.asahi.com/articles/ASK355GFDK35UOOB00N.html\">【動画】３人死亡２人意識不明　９人搭乗</a></li>\n  </ul>\n </div>\n <p>　事故後、朝日新聞記者が取材ヘリで上空から現場を見た。墜落した機体は後部が折れ、裏返しになっていた。上空では県警ヘリなどが代わる代わる近づき、救助活動を続けていた。</p>\n <p>　斜面には機体が数十メートル滑り落ちたような跡が見られ、肌がめくれた木々がなぎ倒されていた。周辺には、ヘリの座席などが外に放り出され、衝撃の大きさを物語っていた。</p>\n <p>　救助隊員はヘリから現場斜面に…</p>\n <!--googleoff:index-->\n</div>\n<!-- ArticleText END -->\n<!-- 有料記事　続きを読む BGN -->\n<div class=\"LoginSelectArea\" id=\"KeySilver\">\n <div class=\"ArticleTypeArea\"></div>\n <div class=\"MoveLink\">\n  <p class=\"Count\">残り：1469文字／全文：1744文字</p>\n  <ul>\n   <li class=\"ReadMore\"><a href=\"http://digital.asahi.com/info/information/free_member/?jumpUrl=http%3A%2F%2Fdigital.asahi.com%2Farticles%2FASK355T47K35UTIL013.html%3F_requesturl%3Darticles%2FASK355T47K35UTIL013.html\" class=\"NonRegi\" onclick=\"s.getPreviousValue('fn3','sc_clk_btn','');\"><span>無料登録して全文を読む</span></a></li>\n   <li class=\"ReadMore\"><a href=\"http://digital.asahi.com/articles/ASK355T47K35UTIL013.html?rm=275\" class=\"Lite Member\" onclick=\"s.getPreviousValue('fn1','sc_clk_btn','');\"><span>全文を読む</span></a></li>\n   <li class=\"ReadMore\"><a href=\"http://digital.asahi.com/rd/lgck2.html?jumpUrl=http%3A%2F%2Fdigital.asahi.com%2Farticles%2FASK355T47K35UTIL013.html%3F_requesturl%3Darticles%2FASK355T47K35UTIL013.html%26rm%3D275\" class=\"Logout Member\" onclick=\"s.getPreviousValue('fn2','sc_clk_btn','');\"><span>ログインして全文を読む</span></a></li>\n   <li class=\"ReadMore\">\n    <noscript>\n     <a href=\"http://digital.asahi.com/rd/lgck2.html?jumpUrl=http%3A%2F%2Fdigital.asahi.com%2Farticles%2FASK355T47K35UTIL013.html%3F_requesturl%3Darticles%2FASK355T47K35UTIL013.html%26rm%3D275\" class=\"NoScript\"><span>ログインして全文を読む</span></a>\n    </noscript></li>\n  </ul>\n </div>\n</div>\n<!-- 有料記事　続きを読む END -->\n<!--ArticleNoteBox BGN -->\n<div class=\"ArticleNoteBox Lite LiteOut\">\n <p>有料会員に登録すると全ての記事が読み放題です。</p>\n <p>初月無料につき月初のお申し込みがお得</p>\n <p><span class=\"EmText\">980円で月300本まで</span>読めるシンプルコースは<a href=\"http://digital.asahi.com/info/simple_course/?iref=com_smpl_txtpr\">こちら</a></p>\n</div>\n<!--ArticleNoteBox END -->\n<!-- 緊急時用リンクはありません -->\n<div class=\"RelatedLinkMod\">\n <div class=\"Title\">\n  関連ニュース\n </div>\n <ul>\n  <li><a href=\"http://www.asahi.com/articles/ASK356CTJK35UTIL01K.html\">高まるニーズ、不足する熟練操縦士　長野県防災ヘリ墜落</a></li>\n  <li><a href=\"http://www.asahi.com/articles/ASK3563X5K35UTIL01B.html\">事故ヘリ、着陸直前に墜落か　つり上げ救助訓練</a></li>\n  <li><a href=\"http://www.asahi.com/articles/ASK343VQLK34TIPE007.html\">警察の刺しゅうにワッペン…６億円金塊盗難、綿密な計画</a></li>\n  <li><a href=\"http://www.asahi.com/articles/ASK3442ZDK34UHBI00X.html\">北朝鮮、得意の銅像で外貨稼ぎ　アフリカ１５カ国に輸出</a></li>\n </ul>\n</div>"));
//        messageList.add(new NewsMessage("1488744610133", "Asahi", "長野県防災ヘリが墜落、３人死亡２人意識不明　９人搭乗", "2017-03-06T04:24:03+09:00", "http://www.asahicom.jp/articles/images/AS20170305001801_commL.jpg", "http://www.asahi.com/articles/ASK361CSSK35UOOB01Q.html?ref=rss", "<div class=\\\"VideoExMod\\\">\\n <video data-video-id=\\\"5348264396001\\\" data-account=\\\"4508222237001\\\" data-player=\\\"1b255d8b-8264-412f-b5b6-ca07afdefd77\\\" data-embed=\\\"default\\\" class=\\\"video-js gpm\\\" controls></video>\\n <script src=\\\"//players.brightcove.net/4508222237001/1b255d8b-8264-412f-b5b6-ca07afdefd77_default/index.min.js\\\"></script>\\n <script type=\\\"text/javascript\\\">$(document).ready(function(){$(\\\".video-js\\\").on(\\\"contextmenu\\\",function() { return false; });});</script>\\n <span class=\\\"Caption\\\">【動画】鉢伏山付近に墜落した長野県の防災ヘリ「アルプス」＝三澤泰博撮影</span>\\n</div>\\n<div class=\\\"ImagesMod\\\">\\n <div class=\\\"Image\\\">\\n  <p class=\\\"Height\\\"><a href=\\\"./photo/AS20170305001801.html\\\"><img alt=\\\"写真・図版\\\" oncontextmenu=\\\"return false\\\" src=\\\"//www.asahicom.jp/articles/images/AS20170305001801_commL.jpg\\\"><em class=\\\"Caption\\\">墜落した防災ヘリの事故現場＝５日午後５時１４分、長野県の鉢伏山付近、朝日新聞社ヘリから、竹花徹朗撮影</em></a></p>\\n  <ul class=\\\"Thum\\\">\\n   <li><a href=\\\"./photo/AS20170305001642.html\\\"><span class=\\\"Width\\\"><img alt=\\\"写真・図版\\\" oncontextmenu=\\\"return false\\\" src=\\\"//www.asahicom.jp/articles/images/AS20170305001642_commL.jpg\\\"></span></a></li>\\n   <li><a href=\\\"./photo/AS20170305002572.html\\\"><span class=\\\"Width\\\"><img alt=\\\"写真・図版\\\" oncontextmenu=\\\"return false\\\" src=\\\"//www.asahicom.jp/articles/images/AS20170305002572_commL.jpg\\\"></span></a></li>\\n  </ul>\\n </div>\\n <!--Image-->\\n</div>\\n<!--ImagesMod-->\\n<!-- Ad BGN -->\\n<div class=\\\"AdMod\\\">\\n <!-- impact 社会記事中レクタングル(Aone仕様) RECT4★ここから-->\\n <script type=\\\"text/javascript\\\" language=\\\"JavaScript\\\"><!-- impAcn = \\\"ASAHISEG\\\";impAco = document.cookie;impApos = impAco.indexOf(impAcn+\\\"=\\\");impAseg = (impApos!=-1)?impAco.substring(impApos+impAcn.length+1,(impAco.indexOf(\\\"; \\\",impApos)!=-1)?impAco.indexOf(\\\"; \\\",impApos):impAco.length):null;impAseg = (impAseg)?unescape(impAseg):null;impAserver = 'http://imp.asahi.com';impAtag = \\\"/SITE=SYAKAI/AREA=RECT4/AAMSZ=300X250/OENCJP=UTF8\\\";impAtarget = (impAseg)?impAtag+\\\"/\\\"+impAseg:impAtag;impArnd = Math.round(Math.random() * 100000000);if (!impApid) var impApid = Math.round(Math.random() * 100000000);document.write('<scr');document.write('ipt src=\\\"' + impAserver + '/jserver/acc_random=' + impArnd + impAtarget + '/pageid=' + impApid + '\\\">');document.write('</scr');document.write('ipt>');//--></script>\\n <span>[PR]</span>\\n <!-- impact 社会記事中レクタングル(Aone仕様) RECT4★ここまで-->\\n</div>\\n<!-- Ad END -->\\n<!-- ArticleText BGN -->\\n<div class=\\\"ArticleText\\\">\\n <!--googleon:index-->\\n <p>　５日午後３時すぎ、長野県松本、岡谷の市境にある鉢伏（はちぶせ）山（標高１９２９メートル）付近で、県の防災ヘリコプター「アルプス」が墜落しているのを県警のヘリが見つけた。防災ヘリに乗っていた男性９人のうち３人が松本市の病院に搬送されたが、いずれも死亡が確認された。このほか２人が意識不明で、４人の安否が確認できていない。</p>\\n <p>　県警によると、死亡した３人のうち２人は、いずれも県消防防災航空隊員の伊熊直人さん（３５）＝松本市＝と甲田道昭さん（４０）＝同県上田市。</p>\\n <p>　県警によると、防災ヘリにはパイロット１人（５６）、整備士１人（４５）、３５～４７歳の消防隊員７人の計９人が乗っていた。意識不明の２人は機内で見つかったが呼びかけに応じず、壊れた機体に挟まれた状態で救助できていない。県警は日没と積雪のため、午後６時１０分にヘリによる捜索を打ち切った。</p>\\n <p>　県や国土交通省などによると、墜落したヘリは、つり上げ救助の訓練をするため、同日午後１時３３分に松本空港を離陸。１時５３分に高ボッチ高原の臨時ヘリポートに着く予定だったが、２時１５分ごろ、県消防防災航空隊から「アルプスと連絡が取れなくなった」と県警航空隊に連絡が入ったという。防災ヘリなどは通常、離陸後は民間航空機のような管制は特に受けず、主に目視で飛行する。</p>\\n <p>　機体は米ベル・ヘリコプター社の「ベル４１２ＥＰ型」で１９９７年に導入された。今年２月にあった「（飛行時間）３００時間点検」では異常はなかったという。</p>\\n <p>　長野地方気象台によると、現場付近は５日午前中から高気圧に覆われて晴れていた。昼過ぎから薄い雲がかかり始めたが、雨や雪は観測されていない。午後２時ごろの現場付近の風速は北の風４・７メートルだったという。気象台の担当者は「現場付近の天候は比較的平穏だったと考えている」と話した。</p>\\n <p>　国交省は航空事故に認定し、国の運輸安全委員会は６日に航空事故調査官を現地へ送ることを決めた。</p>\\n <!--googleoff:index-->\\n</div>\\n<!-- ArticleText END -->\\n<!-- 緊急時用リンクはありません -->\\n<div class=\\\"RelatedLinkMod\\\">\\n <div class=\\\"Title\\\">\\n  関連ニュース\\n </div>\\n <ul>\\n  <li><a href=\\\"http://www.asahi.com/articles/ASK343VQLK34TIPE007.html\\\">警察の刺しゅうにワッペン…６億円金塊盗難、綿密な計画</a></li>\\n  <li><a href=\\\"http://www.asahi.com/articles/ASK335R51K33ULFA02Z.html\\\">ヤマト、サービス残業常態化　パンク寸前、疲弊する現場</a></li>\\n  <li><a href=\\\"http://www.asahi.com/articles/ASK2R5QZ9K2RUTIL04Q.html\\\">女の子へ「寿司と指輪は自分で買おう」　西原理恵子さん</a></li>\\n  <li><a href=\\\"http://www.asahi.com/articles/ASK2V6226K2VTIPE00Z.html\\\">授業は教科書を読むだけ…　悪質な日本語学校、野放しに</a></li>\\n  <li><a href=\\\"http://www.asahi.com/articles/ASK3172XTK31UGTB01H.html\\\">女性店員に「会いに来たよ」　福島原発ローソン開店１年</a></li>\\n  <li><a href=\\\"http://www.asahi.com/video/\\\">動画ページ</a></li>\\n </ul>\\n</div>"));
//        messageList.add(new NewsMessage("1488744612001", "Asahi", "英国向け高速鉄道、昼に陸送　山口・下松、ファン３万人", "2017-03-06T04:24:03+09:00", "http://www.asahicom.jp/articles/images/AS20170305002097_commL.jpg", "http://www.asahi.com/articles/ASK361CSSK35UOOB01Q.html?ref=rss", "<div class=\"VideoExMod\">\n <video data-video-id=\"5348261732001\" data-account=\"4508222237001\" data-player=\"1b255d8b-8264-412f-b5b6-ca07afdefd77\" data-embed=\"default\" class=\"video-js gpm\" controls></video>\n <script src=\"//players.brightcove.net/4508222237001/1b255d8b-8264-412f-b5b6-ca07afdefd77_default/index.min.js\"></script>\n <script type=\"text/javascript\">$(document).ready(function(){$(\".video-js\").on(\"contextmenu\",function() { return false; });});</script>\n <span class=\"Caption\">【動画】英国向けの高速鉄道車両が輸送される様子を見ようと、大勢の見物客が詰めかけた＝高橋伸竹、三沢敦、金子淳撮影</span>\n</div>\n<div class=\"ImagesMod\">\n <div class=\"Image\">\n  <p class=\"Width\"><a href=\"./photo/AS20170305002097.html\"><img alt=\"写真・図版\" oncontextmenu=\"return false\" src=\"//www.asahicom.jp/articles/images/AS20170305002097_commL.jpg\"><em class=\"Caption\">多くの人が見守る中、陸送される高速鉄道車両＝５日午後２時３４分、山口県下松市、金子淳撮影</em></a></p>\n  <ul class=\"Thum\">\n   <li><a href=\"./photo/AS20170305002099.html\"><span class=\"Width\"><img alt=\"写真・図版\" oncontextmenu=\"return false\" src=\"//www.asahicom.jp/articles/images/AS20170305002099_commL.jpg\"></span></a></li>\n   <li><a href=\"./photo/AS20170305002059.html\"><span class=\"Width\"><img alt=\"写真・図版\" oncontextmenu=\"return false\" src=\"//www.asahicom.jp/articles/images/AS20170305002059_commL.jpg\"></span></a></li>\n   <li><a href=\"./photo/AS20170305002057.html\"><span class=\"Width\"><img alt=\"写真・図版\" oncontextmenu=\"return false\" src=\"//www.asahicom.jp/articles/images/AS20170305002057_commL.jpg\"></span></a></li>\n   <li><a href=\"./photo/AS20170305002084.html\"><span class=\"Width\"><img alt=\"写真・図版\" oncontextmenu=\"return false\" src=\"//www.asahicom.jp/articles/images/AS20170305002084_commL.jpg\"></span></a></li>\n   <li><a href=\"./photo/AS20170305002092.html\"><span class=\"Height\"><img alt=\"写真・図版\" oncontextmenu=\"return false\" src=\"//www.asahicom.jp/articles/images/AS20170305002092_commL.jpg\"></span></a></li>\n   <li><a href=\"./photo/AS20170305002053.html\"><span class=\"Width\"><img alt=\"写真・図版\" oncontextmenu=\"return false\" src=\"//www.asahicom.jp/articles/images/AS20170305002053_commL.jpg\"></span></a></li>\n  </ul>\n </div>\n <!--Image-->\n</div>\n<!--ImagesMod-->\n<!-- Ad BGN -->\n<div class=\"AdMod\">\n <!-- impact 速報記事中レクタングル(Aone仕様) RECT4★ここから-->\n <script type=\"text/javascript\" language=\"JavaScript\"><!-- impAcn = \"ASAHISEG\";impAco = document.cookie;impApos = impAco.indexOf(impAcn+\"=\");impAseg = (impApos!=-1)?impAco.substring(impApos+impAcn.length+1,(impAco.indexOf(\"; \",impApos)!=-1)?impAco.indexOf(\"; \",impApos):impAco.length):null;impAseg = (impAseg)?unescape(impAseg):null;impAserver = 'http://imp.asahi.com';impAtag = \"/SITE=SOKUHOU/AREA=RECT4/AAMSZ=300X250/OENCJP=UTF8\";impAtarget = (impAseg)?impAtag+\"/\"+impAseg:impAtag;impArnd = Math.round(Math.random() * 100000000);if (!impApid) var impApid = Math.round(Math.random() * 100000000);document.write('<scr');document.write('ipt src=\"' + impAserver + '/jserver/acc_random=' + impArnd + impAtarget + '/pageid=' + impApid + '\">');document.write('</scr');document.write('ipt>');//--></script>\n <span>[PR]</span>\n <!-- impact 速報記事中レクタングル(Aone仕様) RECT4★ここまで-->\n</div>\n<!-- Ad END -->\n<!-- ArticleText BGN -->\n<div class=\"ArticleText\">\n <!--googleon:index-->\n <p>　「ものづくりのまち」をＰＲしようと、山口県下松市が５日昼、市内の日立製作所笠戸事業所で製造された英国向けの高速鉄道車両の輸送を観覧するイベントを開いた。沿道には鉄道ファンら約３万人の見物客が詰めかけ、鉄道発祥の国へと旅立つ車両を見送った。</p>\n <div class=\"ExtendedLinkMod\">\n  <ul>\n   <li><a href=\"http://www.asahi.com/special/train/\">テツの広場</a></li>\n  </ul>\n </div>\n <p>　これまでは交通量の少ない深夜帯に運んでいたが、「新たな観光資源に」と市が日立製作所などの協力を得て実現した。車両を積んだ専用トレーラーは午後２時に事業所を出発。４キロ離れた徳山下松港まで、交通規制された国道や県道を４０分かけて移動した。（三沢敦）</p>\n <!--googleoff:index-->\n</div>\n<!-- ArticleText END -->\n<!-- 緊急時用リンクはありません -->\n<div class=\"RelatedLinkMod\">\n <div class=\"Title\">\n  関連ニュース\n </div>\n <ul>\n  <li><a href=\"http://www.asahi.com/articles/ASK342DXYK34PPTB001.html\">特急サンダーバード、高槻駅にも一部停車</a></li>\n  <li><a href=\"http://www.asahi.com/articles/ASK2X2TBZK2XUOOB002.html\">リニア開業予定、すでにギリギリ？　掘削作業ずれ込み</a></li>\n  <li><a href=\"http://www.asahi.com/video/\">動画ページ</a></li>\n </ul>\n</div>"));
//        messageList.add(new NewsMessage("1488744613010", "Asahi", "高まるニーズ、不足する熟練操縦士　長野県防災ヘリ墜落", "2017-03-06T04:24:03+09:00", "http://www.asahicom.jp/articles/images/AS20170305002283_commL.jpg", "http://www.asahi.com/articles/ASK361CSSK35UOOB01Q.html?ref=rss", "<div class=\\\"ImagesMod\\\">\\n <div class=\\\"Image\\\">\\n  <p class=\\\"Width\\\"><a href=\\\"./photo/AS20170305002283.html\\\"><img alt=\\\"写真・図版\\\" oncontextmenu=\\\"return false\\\" src=\\\"//www.asahicom.jp/articles/images/AS20170305002283_commL.jpg\\\"><em class=\\\"Caption\\\">防災ヘリが墜落した現場＝５日午後５時２３分、長野県の鉢伏山付近、本社ヘリから、竹花徹朗撮影</em></a></p>\\n </div>\\n <!--Image-->\\n</div>\\n<!--ImagesMod-->\\n<!-- Ad BGN -->\\n<div class=\\\"AdMod\\\">\\n <!-- impact 社会記事中レクタングル(Aone仕様) RECT4★ここから-->\\n <script type=\\\"text/javascript\\\" language=\\\"JavaScript\\\"><!-- impAcn = \\\"ASAHISEG\\\";impAco = document.cookie;impApos = impAco.indexOf(impAcn+\\\"=\\\");impAseg = (impApos!=-1)?impAco.substring(impApos+impAcn.length+1,(impAco.indexOf(\\\"; \\\",impApos)!=-1)?impAco.indexOf(\\\"; \\\",impApos):impAco.length):null;impAseg = (impAseg)?unescape(impAseg):null;impAserver = 'http://imp.asahi.com';impAtag = \\\"/SITE=SYAKAI/AREA=RECT4/AAMSZ=300X250/OENCJP=UTF8\\\";impAtarget = (impAseg)?impAtag+\\\"/\\\"+impAseg:impAtag;impArnd = Math.round(Math.random() * 100000000);if (!impApid) var impApid = Math.round(Math.random() * 100000000);document.write('<scr');document.write('ipt src=\\\"' + impAserver + '/jserver/acc_random=' + impArnd + impAtarget + '/pageid=' + impApid + '\\\">');document.write('</scr');document.write('ipt>');//--></script>\\n <span>[PR]</span>\\n <!-- impact 社会記事中レクタングル(Aone仕様) RECT4★ここまで-->\\n</div>\\n<!-- Ad END -->\\n<!-- ArticleText BGN -->\\n<div class=\\\"ArticleText\\\">\\n <!--googleon:index-->\\n <p>　長野県の防災ヘリ事故で総務省消防庁は職員５人を現地へ派遣し、埼玉、岐阜、愛知の３県に応援を要請した。長野県と相互応援協定を結ぶ山梨県も合わせ、計４県から長野県に向けてヘリが出動した。</p>\\n <div class=\\\"ExtendedLinkMod\\\">\\n  <ul>\\n   <li><a href=\\\"http://www.asahi.com/articles/ASK355GFDK35UOOB00N.html\\\">長野県防災ヘリが墜落　９人乗り、３人の死亡確認</a></li>\\n  </ul>\\n </div>\\n <p>　救急や災害対応、山岳事故への対応などのために運航するヘリのニーズは高まっている。消防白書によると、２０１６年１１月現在で総務省消防庁や自治体が保有する計７６機が、佐賀、沖縄両県を除く４５都道府県に配備されている。消防庁によると長野県は１９９７年に初めて、今回の事故で墜落したヘリを導入した。</p>\\n <p>　１５年の全国の出動件数は６８４２件。救急が最も多く３３０８件、次いで救助が２２１８件だった。同年の総運航時間は１万８４３０時間で、うち訓練が最も長く１万５８１時間（５７・４％）を占め、次いで災害が５５７１時間（３０・２％）だった。</p>\\n <p>　ベテラン操縦士の大量退職が今後見込まれており、高い技術を持つ操縦士が不足しつつある。白書によると、２４時間態勢で運航できることが必要だが、操縦士不足のために態勢が十分でない自治体もあるという。</p>\\n <p>　ベル・ヘリコプター社のホームページなどによると、今回事故を起こしたベル４１２ＥＰ型機は最大１５人乗り。内部が広く、乗客や積み荷を多く載せられるため、世界で遭難救助などに使われ、日本では自治体の防災ヘリや全国の警察、海上保安庁などで約５０機の採用実績がある。０９年には同型の岐阜県の防災ヘリが、北アルプスの奥穂高岳近くで救助活動中、岩壁に回転翼をぶつけて墜落し、乗員３人が死亡した。１０年には香川県で海保のヘリが電線に接触し、５人が死亡している。（四倉幹木、工藤隆治）</p>\\n <!--googleoff:index-->\\n</div>\\n<!-- ArticleText END -->\\n<!-- 緊急時用リンクはありません -->\\n<div class=\\\"RelatedLinkMod\\\">\\n <div class=\\\"Title\\\">\\n  関連ニュース\\n </div>\\n <ul>\\n  <li><a href=\\\"http://www.asahi.com/articles/ASK3563X5K35UTIL01B.html\\\">事故ヘリ、着陸直前に墜落か　つり上げ救助訓練</a></li>\\n  <li><a href=\\\"http://www.asahi.com/articles/ASK3172XTK31UGTB01H.html\\\">女性店員に「会いに来たよ」　福島原発ローソン開店１年</a></li>\\n  <li><a href=\\\"http://www.asahi.com/articles/ASK3564NKK35UTQP03K.html\\\">木村沙織、あっけないラストゲーム　足がつり退場</a></li>\\n  <li><a href=\\\"http://www.asahi.com/articles/ASK353H1XK35UHBI006.html\\\">オバマ氏側、真っ向から否定　トランプ氏の「盗聴」発言</a></li>\\n </ul>\\n</div>"));
//        messageList.add(new NewsMessage("1488744615926", "Asahi", "イチゴ狩り、千人同時に　ギネス記録達成　宮城・亘理", "2017-03-06T04:24:03+09:00", "http://www.asahicom.jp/articles/images/AS20170305001853_commL.jpg", "http://www.asahi.com/articles/ASK361CSSK35UOOB01Q.html?ref=rss", "<div class=\\\"VideoExMod\\\">\\n <video data-video-id=\\\"5348249772001\\\" data-account=\\\"4508222237001\\\" data-player=\\\"1b255d8b-8264-412f-b5b6-ca07afdefd77\\\" data-embed=\\\"default\\\" class=\\\"video-js gpm\\\" controls></video>\\n <script src=\\\"//players.brightcove.net/4508222237001/1b255d8b-8264-412f-b5b6-ca07afdefd77_default/index.min.js\\\"></script>\\n <script type=\\\"text/javascript\\\">$(document).ready(function(){$(\\\".video-js\\\").on(\\\"contextmenu\\\",function() { return false; });});</script>\\n <span class=\\\"Caption\\\">【動画】「同時にイチゴ狩りをした最多人数」のギネス世界記録に挑戦＝福留庸友撮影</span>\\n</div>\\n<div class=\\\"ImagesMod\\\">\\n <div class=\\\"Image\\\">\\n  <p class=\\\"Width\\\"><a href=\\\"./photo/AS20170305001853.html\\\"><img alt=\\\"写真・図版\\\" oncontextmenu=\\\"return false\\\" src=\\\"//www.asahicom.jp/articles/images/AS20170305001853_commL.jpg\\\"><em class=\\\"Caption\\\">記念撮影のため採れたイチゴを掲げる参加者＝５日午前、宮城県亘理町、福留庸友撮影</em></a></p>\\n  <ul class=\\\"Thum\\\">\\n   <li><a href=\\\"./photo/AS20170305002098.html\\\"><span class=\\\"Width\\\"><img alt=\\\"写真・図版\\\" oncontextmenu=\\\"return false\\\" src=\\\"//www.asahicom.jp/articles/images/AS20170305002098_commL.jpg\\\"></span></a></li>\\n   <li><a href=\\\"./photo/AS20170305002074.html\\\"><span class=\\\"Width\\\"><img alt=\\\"写真・図版\\\" oncontextmenu=\\\"return false\\\" src=\\\"//www.asahicom.jp/articles/images/AS20170305002074_commL.jpg\\\"></span></a></li>\\n   <li><a href=\\\"./photo/AS20170305002105.html\\\"><span class=\\\"Width\\\"><img alt=\\\"写真・図版\\\" oncontextmenu=\\\"return false\\\" src=\\\"//www.asahicom.jp/articles/images/AS20170305002105_commL.jpg\\\"></span></a></li>\\n  </ul>\\n </div>\\n <!--Image-->\\n</div>\\n<!--ImagesMod-->\\n<!-- Ad BGN -->\\n<div class=\\\"AdMod\\\">\\n <!-- impact 速報記事中レクタングル(Aone仕様) RECT4★ここから-->\\n <script type=\\\"text/javascript\\\" language=\\\"JavaScript\\\"><!-- impAcn = \\\"ASAHISEG\\\";impAco = document.cookie;impApos = impAco.indexOf(impAcn+\\\"=\\\");impAseg = (impApos!=-1)?impAco.substring(impApos+impAcn.length+1,(impAco.indexOf(\\\"; \\\",impApos)!=-1)?impAco.indexOf(\\\"; \\\",impApos):impAco.length):null;impAseg = (impAseg)?unescape(impAseg):null;impAserver = 'http://imp.asahi.com';impAtag = \\\"/SITE=SOKUHOU/AREA=RECT4/AAMSZ=300X250/OENCJP=UTF8\\\";impAtarget = (impAseg)?impAtag+\\\"/\\\"+impAseg:impAtag;impArnd = Math.round(Math.random() * 100000000);if (!impApid) var impApid = Math.round(Math.random() * 100000000);document.write('<scr');document.write('ipt src=\\\"' + impAserver + '/jserver/acc_random=' + impArnd + impAtarget + '/pageid=' + impApid + '\\\">');document.write('</scr');document.write('ipt>');//--></script>\\n <span>[PR]</span>\\n <!-- impact 速報記事中レクタングル(Aone仕様) RECT4★ここまで-->\\n</div>\\n<!-- Ad END -->\\n<!-- ArticleText BGN -->\\n<div class=\\\"ArticleText\\\">\\n <!--googleon:index-->\\n <p>　震災からの復興と感謝の気持ちを表そうと、宮城県亘理町のイチゴ栽培のビニールハウスで５日、「同時にイチゴ狩りをした最多人数」のギネス世界記録に挑戦するイベントがあった。町内外から集まった１１４１人が５分以内に同時にイチゴを摘んだと認められ、世界記録を達成した。</p>\\n <p>　亘理町は東北一のイチゴ産地として知られ、震災前は年に２２００トンの生産量があったが、同町の作付面積の９４％が津波で大きな被害をうけた。その後、復興交付金を使いビニールハウスを大型化、集約化し「イチゴ団地」を造った。生産農家の数は減ったが、２０１６年産の生産量は、震災前の約８割にあたる、１８００トンまで回復した。</p>\\n <p>　亘理町で生まれ、家族５人で参加した日下裕子さん（４０）は「何もなくなった町を見たときは本当にショックだった。でも、６年でこんな楽しいイベントが開けるようになって、みなさんの努力のたまものなんだな」と感心していた。（福留庸友）</p>\\n <!--googleoff:index-->\\n</div>\\n<!-- ArticleText END -->\\n<!-- 緊急時用リンクはありません -->\\n<div class=\\\"RelatedLinkMod\\\">\\n <div class=\\\"Title\\\">\\n  関連ニュース\\n </div>\\n <ul>\\n  <li><a href=\\\"http://www.asahi.com/articles/ASK3172XTK31UGTB01H.html\\\">女性店員に「会いに来たよ」　福島原発ローソン開店１年</a></li>\\n  <li><a href=\\\"http://www.asahi.com/articles/ASK2C7WWPK2CUEHF00K.html\\\">線量上昇に緊張・厳重検査に不安…　初めて見た福島第一</a></li>\\n  <li><a href=\\\"http://www.asahi.com/articles/ASK3453PHK34UQIP01S.html\\\">「だるま朝日」照らす南三陸の藻場、震災前の水準に回復</a></li>\\n  <li><a href=\\\"http://www.asahi.com/articles/ASK313Q3JK31UUHB008.html\\\">梅佳代さん、被災生徒見守り写真集　きょう最後の卒業式</a></li>\\n  <li><a href=\\\"http://www.asahi.com/articles/ASK332V2BK33UNHB001.html\\\">さんさん商店街が常設に　３・３から始動　宮城・南三陸</a></li>\\n </ul>\\n</div>"));
//        messageList.add(new NewsMessage("1488744617265", "Asahi", "事故ヘリ、着陸直前に墜落か　つり上げ救助訓練", "2017-03-06T04:24:03+09:00", "http://www.asahicom.jp/articles/images/AS20170305001992_commL.jpg", "http://www.asahi.com/articles/ASK361CSSK35UOOB01Q.html?ref=rss", "<div class=\\\"ImagesMod\\\">\\n <div class=\\\"Image\\\">\\n  <p class=\\\"Width\\\"><a href=\\\"./photo/AS20170305001992.html\\\"><img alt=\\\"写真・図版\\\" oncontextmenu=\\\"return false\\\" src=\\\"//www.asahicom.jp/articles/images/AS20170305001992_commL.jpg\\\"><em class=\\\"Caption\\\">地図</em></a></p>\\n  <ul class=\\\"Thum\\\">\\n   <li><a href=\\\"./photo/AS20170305002177.html\\\"><span class=\\\"Width\\\"><img alt=\\\"写真・図版\\\" oncontextmenu=\\\"return false\\\" src=\\\"//www.asahicom.jp/articles/images/AS20170305002177_commL.jpg\\\"></span></a></li>\\n  </ul>\\n </div>\\n <!--Image-->\\n</div>\\n<!--ImagesMod-->\\n<!-- Ad BGN -->\\n<div class=\\\"AdMod\\\">\\n <!-- impact 社会記事中レクタングル(Aone仕様) RECT4★ここから-->\\n <script type=\\\"text/javascript\\\" language=\\\"JavaScript\\\"><!-- impAcn = \\\"ASAHISEG\\\";impAco = document.cookie;impApos = impAco.indexOf(impAcn+\\\"=\\\");impAseg = (impApos!=-1)?impAco.substring(impApos+impAcn.length+1,(impAco.indexOf(\\\"; \\\",impApos)!=-1)?impAco.indexOf(\\\"; \\\",impApos):impAco.length):null;impAseg = (impAseg)?unescape(impAseg):null;impAserver = 'http://imp.asahi.com';impAtag = \\\"/SITE=SYAKAI/AREA=RECT4/AAMSZ=300X250/OENCJP=UTF8\\\";impAtarget = (impAseg)?impAtag+\\\"/\\\"+impAseg:impAtag;impArnd = Math.round(Math.random() * 100000000);if (!impApid) var impApid = Math.round(Math.random() * 100000000);document.write('<scr');document.write('ipt src=\\\"' + impAserver + '/jserver/acc_random=' + impArnd + impAtarget + '/pageid=' + impApid + '\\\">');document.write('</scr');document.write('ipt>');//--></script>\\n <span>[PR]</span>\\n <!-- impact 社会記事中レクタングル(Aone仕様) RECT4★ここまで-->\\n</div>\\n<!-- Ad END -->\\n<!-- ArticleText BGN -->\\n<div class=\\\"ArticleText\\\">\\n <!--googleon:index-->\\n <p>　長野県消防防災航空センターのヘリコプターが落下した事故で、国土交通省は、つり上げ救助の訓練を行う場所に向かう途中で墜落したとみられることを明らかにした。着陸の直前に墜落した可能性があるという。</p>\\n <div class=\\\"ExtendedLinkMod\\\">\\n  <ul>\\n   <li><a href=\\\"http://www.asahi.com/articles/ASK355GFDK35UOOB00N.html\\\">長野県防災ヘリが墜落</a></li>\\n  </ul>\\n </div>\\n <p>　国交省によると、墜落したのは午後１時５３分ごろ。墜落したヘリコプター（ベル４１２ＥＰ型）の定員は１５人で、松本空港を午後１時３３分に離陸し、１時５３分に高ボッチ高原の離着陸場に到着予定だった。</p>\\n <p>　国交省は航空事故に認定し、国の運輸安全委員会は航空事故調査官を６日に現地へ派遣する。</p>\\n <!--googleoff:index-->\\n</div>\\n<!-- ArticleText END -->\\n<!-- 緊急時用リンクはありません -->"));
//        messageList.add(new NewsMessage("1488744618163", "Asahi", "小川直也さんの息子・雄勢が初Ｖ　全日本選手権東京予選", "2017-03-06T04:24:03+09:00", "http://www.asahicom.jp/articles/images/AS20170305002673_commL.jpg", "http://www.asahi.com/articles/ASK361CSSK35UOOB01Q.html?ref=rss", "<div class=\\\"ImagesMod\\\">\\n <div class=\\\"Image\\\">\\n  <p class=\\\"Width\\\"><a href=\\\"./photo/AS20170305002673.html\\\"><img alt=\\\"写真・図版\\\" oncontextmenu=\\\"return false\\\" src=\\\"//www.asahicom.jp/articles/images/AS20170305002673_commL.jpg\\\"><em class=\\\"Caption\\\">決勝で香川大吾（東海大）を破った小川雄勢（右）</em></a></p>\\n </div>\\n <!--Image-->\\n</div>\\n<!--ImagesMod-->\\n<!-- Ad BGN -->\\n<div class=\\\"AdMod\\\">\\n <!-- impact スポーツ記事中レクタングル(Aone仕様) RECT4★ここから-->\\n <script type=\\\"text/javascript\\\" language=\\\"JavaScript\\\"><!-- impAcn = \\\"ASAHISEG\\\";impAco = document.cookie;impApos = impAco.indexOf(impAcn+\\\"=\\\");impAseg = (impApos!=-1)?impAco.substring(impApos+impAcn.length+1,(impAco.indexOf(\\\"; \\\",impApos)!=-1)?impAco.indexOf(\\\"; \\\",impApos):impAco.length):null;impAseg = (impAseg)?unescape(impAseg):null;impAserver = 'http://imp.asahi.com';impAtag = \\\"/SITE=SPORTS/AREA=RECT4/AAMSZ=300X250/OENCJP=UTF8\\\";impAtarget = (impAseg)?impAtag+\\\"/\\\"+impAseg:impAtag;impArnd = Math.round(Math.random() * 100000000);if (!impApid) var impApid = Math.round(Math.random() * 100000000);document.write('<scr');document.write('ipt src=\\\"' + impAserver + '/jserver/acc_random=' + impArnd + impAtarget + '/pageid=' + impApid + '\\\">');document.write('</scr');document.write('ipt>');//--></script>\\n <span>[PR]</span>\\n <!-- impact スポーツ記事中レクタングル(Aone仕様) RECT4★ここまで-->\\n</div>\\n<!-- Ad END -->\\n<!-- ArticleText BGN -->\\n<div class=\\\"ArticleText\\\">\\n <!--googleon:index-->\\n <p>　５日に行われた柔道の全日本選手権の東京地区予選で、小川雄勢（明大２年）が初優勝した。「本戦の出場が目標だった。しっかりできてよかった」と笑顔を見せた。この日は２回戦から出場し、トーナメントの途中で右手の親指を突き指。痛みを抱えながら勝ち進み、大学３年で優勝した父・直也さんとの父子制覇を果たした。</p>\\n <p>　昨年１１月の講道館杯は３回戦で敗れ、その後のグランドスラム東京や欧州の国際大会には出場できなかった。東京五輪へ向けた最初の年。同世代に差をつけられたという思いもあったというが、「新たなスタート」と位置づけた大会で結果を残した。</p>\\n <p>　父は１９８９年大会で東京地区予選を制し、本大会でも初優勝。その勢いで、同じ年の世界選手権で金メダルに輝いた。雄勢も４月の全日本選手権の優勝と、８月にある世界選手権での飛躍を今年最大の目標に置く。息子の戦いぶりを見た直也さんは「（雄勢の）下半身の柔らかさは俺になかったもの。まだ伸びしろがある」と目を細めた。（波戸健一）</p>\\n <!--googleoff:index-->\\n</div>\\n<!-- ArticleText END -->\\n<!-- 緊急時用リンクはありません -->\\n<!-- Outbrain TAG PC 2017.02.17 -->\\n<div class=\\\"OutbrainRecMod\\\">\\n <div class=\\\"Title\\\">\\n  こんなニュースも\\n </div>\\n <div class=\\\"OUTBRAIN\\\" data-src=\\\"DROP_PERMALINK_HERE\\\" data-widget-id=\\\"AR_7\\\" data-ob-template=\\\"AsahiShimbunDigital\\\"></div>\\n <div class=\\\"OUTBRAIN\\\" data-src=\\\"DROP_PERMALINK_HERE\\\" data-widget-id=\\\"AR_8\\\" data-ob-template=\\\"AsahiShimbunDigital\\\"></div>\\n <div class=\\\"OUTBRAIN\\\" data-src=\\\"DROP_PERMALINK_HERE\\\" data-widget-id=\\\"AR_9\\\" data-ob-template=\\\"AsahiShimbunDigital\\\"></div>\\n</div>\\n<!-- /Outbrain TAG PC 2017.02.17 -->"));
//        messageList.add(new NewsMessage("1488744619042", "Asahi", "高木美帆が総合３位で銅　スピードスケート世界選手権", "2017-03-06T04:24:03+09:00", "http://www.asahicom.jp/articles/images/AS20170305002676_commL.jpg", "http://www.asahi.com/articles/ASK361CSSK35UOOB01Q.html?ref=rss", "<div class=\\\"ImagesMod\\\">\\n <div class=\\\"Image\\\">\\n  <p class=\\\"Width\\\"><a href=\\\"./photo/AS20170305002676.html\\\"><img alt=\\\"写真・図版\\\" oncontextmenu=\\\"return false\\\" src=\\\"//www.asahicom.jp/articles/images/AS20170305002676_commL.jpg\\\"><em class=\\\"Caption\\\">１５００メートルで２位に入った高木美帆＝ＡＰ</em></a></p>\\n  <ul class=\\\"Thum\\\">\\n   <li><a href=\\\"./photo/AS20170305002674.html\\\"><span class=\\\"Width\\\"><img alt=\\\"写真・図版\\\" oncontextmenu=\\\"return false\\\" src=\\\"//www.asahicom.jp/articles/images/AS20170305002674_commL.jpg\\\"></span></a></li>\\n   <li><a href=\\\"./photo/AS20170305002566.html\\\"><span class=\\\"Width\\\"><img alt=\\\"写真・図版\\\" oncontextmenu=\\\"return false\\\" src=\\\"//www.asahicom.jp/articles/images/AS20170305002566_commL.jpg\\\"></span></a></li>\\n  </ul>\\n </div>\\n <!--Image-->\\n</div>\\n<!--ImagesMod-->\\n<!-- Ad BGN -->\\n<div class=\\\"AdMod\\\">\\n <!-- impact スポーツ記事中レクタングル(Aone仕様) RECT4★ここから-->\\n <script type=\\\"text/javascript\\\" language=\\\"JavaScript\\\"><!-- impAcn = \\\"ASAHISEG\\\";impAco = document.cookie;impApos = impAco.indexOf(impAcn+\\\"=\\\");impAseg = (impApos!=-1)?impAco.substring(impApos+impAcn.length+1,(impAco.indexOf(\\\"; \\\",impApos)!=-1)?impAco.indexOf(\\\"; \\\",impApos):impAco.length):null;impAseg = (impAseg)?unescape(impAseg):null;impAserver = 'http://imp.asahi.com';impAtag = \\\"/SITE=SPORTS/AREA=RECT4/AAMSZ=300X250/OENCJP=UTF8\\\";impAtarget = (impAseg)?impAtag+\\\"/\\\"+impAseg:impAtag;impArnd = Math.round(Math.random() * 100000000);if (!impApid) var impApid = Math.round(Math.random() * 100000000);document.write('<scr');document.write('ipt src=\\\"' + impAserver + '/jserver/acc_random=' + impArnd + impAtarget + '/pageid=' + impApid + '\\\">');document.write('</scr');document.write('ipt>');//--></script>\\n <span>[PR]</span>\\n <!-- impact スポーツ記事中レクタングル(Aone仕様) RECT4★ここまで-->\\n</div>\\n<!-- Ad END -->\\n<!-- ArticleText BGN -->\\n<div class=\\\"ArticleText\\\">\\n <!--googleon:index-->\\n <p>　スピードスケートの世界選手権最終日は５日、ノルウェー・ハーマルで男女各２種目があり、高木美帆（２２）＝日体大＝が総合３位で銅メダルを獲得した。日本勢の表彰台は２０００年銅の田畑真紀以来で、女子では橋本聖子、上原三枝、田畑に続き４人目。</p>\\n <p>　高木美は初日は５００メートル１位、３０００メートル６位の総合首位で折り返した。この日の１５００メートルは１分５５秒８１で２位。最終種目５０００メートルは７分１０秒１４で６位と踏ん張り、日本女子１７年ぶりの快挙を成し遂げた。総合優勝はイレイン・ブスト（オランダ）。佐藤綾乃（高崎健康福祉大）は１０位、高木菜那（日本電産サンキョー）は１３位でともに最終種目に進めなかった。</p>\\n <p>　男子１５００メートルは中村奨太（ロジネットジャパン）が１分４６秒４１で７位となり、最終種目の１万メートルへ進んだ。</p>\\n <p>　大会はオールラウンダーの世界一決定戦で、スプリント力と持久力の両方が求められる。２日間で男女各４種目が実施され、タイムを換算した総合得点で争われた。</p>\\n <p>■「自分のレース」に集中、日本女子１７年ぶり快挙</p>\\n <p>　最強のオールラウンダーを決める憧れの舞台で、高木美が表彰台に上がった。「やっとできた思いがある。名を刻めたことがうれしい」。日本女子１７年ぶりの快挙に喜びがにじんだ。</p>\\n <p>　総合首位で迎えた最終日。得意の１５００メートルで世界と渡り合った。この種目バンクーバー五輪金のブスト（オランダ）と０秒３２差の２位。前日からの総合１位を守ったまま最終種目５０００メートルに進んだ。</p>\\n <p>　総合４位との差は約１３秒。このタイム差を守り切れれば表彰台が決まる。だが、高木美は「自分のレースをしようと。タイムをいかに縮められるかに集中した方がいい結果を生む」。自らの滑りに集中した。</p>\\n <p>　１周３３～３４秒のラップを刻んでいく。３８００～４２００メートルで３５秒台に落としたが「このままでは今までの３種目が無駄になる」。残り２周は３４秒台で粘った。６位に入り、総合のメダル圏内を守った。</p>\\n <p>　今季、ナショナルチームではオランダ人コーチの下、特にスタミナを強化した。高い強度を設定して長い時で３時間半、自転車をこぎ、氷上トレーニングもこなした。「全体的な力が底上げされた」。鍛錬の成果がここぞで発揮された。</p>\\n <p>　「日本の選手でも『できる』ということが証明できた」。３世紀にまたがるスピードスケートで最古の歴史を持つ大会（初開催は１８８９年）で高い総合力を示した２２歳。短距離だけではない日本の強さを世界に示してみせた。（榊原一生）</p>\\n <p>　　　　　◇</p>\\n <p>　〈世界選手権〉　オールラウンダーの世界一決定戦。男女の５００メートルと１５００メートルのほか、男子は５０００メートルと１万メートル、女子は３０００メートルと５０００メートルの計４種目の総合ポイントで争う。スプリントと持久力の両方が求められる。主要な国際大会は他に、２日間で５００メートル、１０００メートルを２回ずつ滑り、総合得点で最強の短距離選手を決める「世界スプリント選手権」、五輪と同じ単一種目で優勝を争う「世界距離別選手権」がある。３大会で最も歴史が古いのは１８８９年に始まった世界選手権。世界スプリント選手権は１９７０年、距離別選手権は１９９６年にそれぞれ開かれた。</p>\\n <!--googleoff:index-->\\n</div>\\n<!-- ArticleText END -->\\n<!-- 緊急時用リンクはありません -->\\n<!-- Outbrain TAG PC 2017.02.17 -->\\n<div class=\\\"OutbrainRecMod\\\">\\n <div class=\\\"Title\\\">\\n  こんなニュースも\\n </div>\\n <div class=\\\"OUTBRAIN\\\" data-src=\\\"DROP_PERMALINK_HERE\\\" data-widget-id=\\\"AR_7\\\" data-ob-template=\\\"AsahiShimbunDigital\\\"></div>\\n <div class=\\\"OUTBRAIN\\\" data-src=\\\"DROP_PERMALINK_HERE\\\" data-widget-id=\\\"AR_8\\\" data-ob-template=\\\"AsahiShimbunDigital\\\"></div>\\n <div class=\\\"OUTBRAIN\\\" data-src=\\\"DROP_PERMALINK_HERE\\\" data-widget-id=\\\"AR_9\\\" data-ob-template=\\\"AsahiShimbunDigital\\\"></div>\\n</div>\\n<!-- /Outbrain TAG PC 2017.02.17 -->"));
//        messageList.add(new NewsMessage("1488744620710", "Asahi", "男子５０キロフリー、吉田が２０位　スキー世界選手権", "2017-03-06T04:24:03+09:00", "http://www.asahicom.jp/articles/images/AS20170305002655_commL.jpg", "http://www.asahi.com/articles/ASK361CSSK35UOOB01Q.html?ref=rss", "<div class=\\\"ImagesMod\\\">\\n <div class=\\\"Image\\\">\\n  <p class=\\\"Height\\\"><a href=\\\"./photo/AS20170305002655.html\\\"><img alt=\\\"写真・図版\\\" oncontextmenu=\\\"return false\\\" src=\\\"//www.asahicom.jp/articles/images/AS20170305002655_commL.jpg\\\"><em class=\\\"Caption\\\">距離男子５０キロフリーでゴールする吉田圭伸＝林敏行撮影</em></a></p>\\n  <ul class=\\\"Thum\\\">\\n   <li><a href=\\\"./photo/AS20170305002639.html\\\"><span class=\\\"Height\\\"><img alt=\\\"写真・図版\\\" oncontextmenu=\\\"return false\\\" src=\\\"//www.asahicom.jp/articles/images/AS20170305002639_commL.jpg\\\"></span></a></li>\\n   <li><a href=\\\"./photo/AS20170305002642.html\\\"><span class=\\\"Width\\\"><img alt=\\\"写真・図版\\\" oncontextmenu=\\\"return false\\\" src=\\\"//www.asahicom.jp/articles/images/AS20170305002642_commL.jpg\\\"></span></a></li>\\n   <li><a href=\\\"./photo/AS20170305002649.html\\\"><span class=\\\"Width\\\"><img alt=\\\"写真・図版\\\" oncontextmenu=\\\"return false\\\" src=\\\"//www.asahicom.jp/articles/images/AS20170305002649_commL.jpg\\\"></span></a></li>\\n   <li><a href=\\\"./photo/AS20170305002664.html\\\"><span class=\\\"Height\\\"><img alt=\\\"写真・図版\\\" oncontextmenu=\\\"return false\\\" src=\\\"//www.asahicom.jp/articles/images/AS20170305002664_commL.jpg\\\"></span></a></li>\\n   <li><a href=\\\"./photo/AS20170305002661.html\\\"><span class=\\\"Height\\\"><img alt=\\\"写真・図版\\\" oncontextmenu=\\\"return false\\\" src=\\\"//www.asahicom.jp/articles/images/AS20170305002661_commL.jpg\\\"></span></a></li>\\n  </ul>\\n </div>\\n <!--Image-->\\n</div>\\n<!--ImagesMod-->\\n<!-- Ad BGN -->\\n<div class=\\\"AdMod\\\">\\n <!-- impact スポーツ記事中レクタングル(Aone仕様) RECT4★ここから-->\\n <script type=\\\"text/javascript\\\" language=\\\"JavaScript\\\"><!-- impAcn = \\\"ASAHISEG\\\";impAco = document.cookie;impApos = impAco.indexOf(impAcn+\\\"=\\\");impAseg = (impApos!=-1)?impAco.substring(impApos+impAcn.length+1,(impAco.indexOf(\\\"; \\\",impApos)!=-1)?impAco.indexOf(\\\"; \\\",impApos):impAco.length):null;impAseg = (impAseg)?unescape(impAseg):null;impAserver = 'http://imp.asahi.com';impAtag = \\\"/SITE=SPORTS/AREA=RECT4/AAMSZ=300X250/OENCJP=UTF8\\\";impAtarget = (impAseg)?impAtag+\\\"/\\\"+impAseg:impAtag;impArnd = Math.round(Math.random() * 100000000);if (!impApid) var impApid = Math.round(Math.random() * 100000000);document.write('<scr');document.write('ipt src=\\\"' + impAserver + '/jserver/acc_random=' + impArnd + impAtarget + '/pageid=' + impApid + '\\\">');document.write('</scr');document.write('ipt>');//--></script>\\n <span>[PR]</span>\\n <!-- impact スポーツ記事中レクタングル(Aone仕様) RECT4★ここまで-->\\n</div>\\n<!-- Ad END -->\\n<!-- ArticleText BGN -->\\n<div class=\\\"ArticleText\\\">\\n <!--googleon:index-->\\n <p>　ノルディックスキーの世界選手権最終日は５日、フィンランド・ラハティで、距離の男子５０キロフリーが行われ、吉田圭伸（自衛隊）は１時間４８分３８秒６で２０位だった。優勝はアレックス・ハービー（カナダ）で１時間４６分２８秒９。</p>\\n <p>　第１０日のジャンプ男子団体ラージヒル（ＨＳ１３０メートル）は伊東大貴（雪印メグミルク）、葛西紀明（土屋ホーム）、小林陵侑（同）、竹内択（北野建設）で臨んだ日本は９２２・７点で７位に終わった。優勝は１１０４・２点のポーランド。</p>\\n <!--googleoff:index-->\\n</div>\\n<!-- ArticleText END -->\\n<!-- 緊急時用リンクはありません -->\\n<!-- Outbrain TAG PC 2017.02.17 -->\\n<div class=\\\"OutbrainRecMod\\\">\\n <div class=\\\"Title\\\">\\n  こんなニュースも\\n </div>\\n <div class=\\\"OUTBRAIN\\\" data-src=\\\"DROP_PERMALINK_HERE\\\" data-widget-id=\\\"AR_7\\\" data-ob-template=\\\"AsahiShimbunDigital\\\"></div>\\n <div class=\\\"OUTBRAIN\\\" data-src=\\\"DROP_PERMALINK_HERE\\\" data-widget-id=\\\"AR_8\\\" data-ob-template=\\\"AsahiShimbunDigital\\\"></div>\\n <div class=\\\"OUTBRAIN\\\" data-src=\\\"DROP_PERMALINK_HERE\\\" data-widget-id=\\\"AR_9\\\" data-ob-template=\\\"AsahiShimbunDigital\\\"></div>\\n</div>\\n<!-- /Outbrain TAG PC 2017.02.17 -->"));
//        messageList.add(new NewsMessage("1488744621721", "Asahi", "伏見工ＯＢ、グラウンドに別れ　山口総監督らが記念撮影", "2017-03-06T04:24:03+09:00", "http://www.asahicom.jp/articles/images/AS20170305001793_commL.jpg", "http://www.asahi.com/articles/ASK361CSSK35UOOB01Q.html?ref=rss", "<div class=\\\"ImagesMod\\\">\\n <div class=\\\"Image\\\">\\n  <p class=\\\"Width\\\"><a href=\\\"./photo/AS20170305001793.html\\\"><img alt=\\\"写真・図版\\\" oncontextmenu=\\\"return false\\\" src=\\\"//www.asahicom.jp/articles/images/AS20170305001793_commL.jpg\\\"><em class=\\\"Caption\\\">京都市内のクラブチームと伏見工ＯＢとの試合には大八木淳史さん（右から２人目）も出場＝京都市伏見区</em></a></p>\\n  <ul class=\\\"Thum\\\">\\n   <li><a href=\\\"./photo/AS20170305001778.html\\\"><span class=\\\"Width\\\"><img alt=\\\"写真・図版\\\" oncontextmenu=\\\"return false\\\" src=\\\"//www.asahicom.jp/articles/images/AS20170305001778_commL.jpg\\\"></span></a></li>\\n   <li><a href=\\\"./photo/AS20170305001786.html\\\"><span class=\\\"Width\\\"><img alt=\\\"写真・図版\\\" oncontextmenu=\\\"return false\\\" src=\\\"//www.asahicom.jp/articles/images/AS20170305001786_commL.jpg\\\"></span></a></li>\\n   <li><a href=\\\"./photo/AS20170305001781.html\\\"><span class=\\\"Height\\\"><img alt=\\\"写真・図版\\\" oncontextmenu=\\\"return false\\\" src=\\\"//www.asahicom.jp/articles/images/AS20170305001781_commL.jpg\\\"></span></a></li>\\n   <li><a href=\\\"./photo/AS20170305001773.html\\\"><span class=\\\"Width\\\"><img alt=\\\"写真・図版\\\" oncontextmenu=\\\"return false\\\" src=\\\"//www.asahicom.jp/articles/images/AS20170305001773_commL.jpg\\\"></span></a></li>\\n  </ul>\\n </div>\\n <!--Image-->\\n</div>\\n<!--ImagesMod-->\\n<!-- Ad BGN -->\\n<div class=\\\"AdMod\\\">\\n <!-- impact スポーツ記事中レクタングル(Aone仕様) RECT4★ここから-->\\n <script type=\\\"text/javascript\\\" language=\\\"JavaScript\\\"><!-- impAcn = \\\"ASAHISEG\\\";impAco = document.cookie;impApos = impAco.indexOf(impAcn+\\\"=\\\");impAseg = (impApos!=-1)?impAco.substring(impApos+impAcn.length+1,(impAco.indexOf(\\\"; \\\",impApos)!=-1)?impAco.indexOf(\\\"; \\\",impApos):impAco.length):null;impAseg = (impAseg)?unescape(impAseg):null;impAserver = 'http://imp.asahi.com';impAtag = \\\"/SITE=SPORTS/AREA=RECT4/AAMSZ=300X250/OENCJP=UTF8\\\";impAtarget = (impAseg)?impAtag+\\\"/\\\"+impAseg:impAtag;impArnd = Math.round(Math.random() * 100000000);if (!impApid) var impApid = Math.round(Math.random() * 100000000);document.write('<scr');document.write('ipt src=\\\"' + impAserver + '/jserver/acc_random=' + impArnd + impAtarget + '/pageid=' + impApid + '\\\">');document.write('</scr');document.write('ipt>');//--></script>\\n <span>[PR]</span>\\n <!-- impact スポーツ記事中レクタングル(Aone仕様) RECT4★ここまで-->\\n</div>\\n<!-- Ad END -->\\n<!-- ArticleText BGN -->\\n<div class=\\\"ArticleText\\\">\\n <!--googleon:index-->\\n <p>　熱血教師とやんちゃな生徒たちが高校日本一を達成する軌跡を描いたテレビドラマ「スクール・ウォーズ」のモデルとなった京都・伏見工ラグビー部のＯＢ・関係者約４００人が５日、元日本代表の故平尾誠二さんら、多くの名選手を育んだ同校の土のグラウンドに別れを告げた。</p>\\n <p>　全国高校大会（花園）４度優勝の名門は今年度の新入生から洛陽工と統合・再編されて京都工学院に。４月からは伏見工の新３年生も京都工学院の校舎で学ぶため、グラウンドが自由に使えるのは３月で最後だ。</p>\\n <p>　この日は「春のＯＢ戦　グラウンドファイナル」と銘打ち、強豪に育て上げた山口良治・現総監督が見守る中、京都市内のクラブチームと試合をしたり、ＯＢ同士でタッチフットを楽しんだりした。最後は全員で記念撮影し、グラウンドに一礼した。</p>\\n <p>　山口総監督は約４０年前に赴任…</p>\\n <!--googleoff:index-->\\n</div>\\n<!-- ArticleText END -->\\n<!-- 有料記事　続きを読む BGN -->\\n<div class=\\\"LoginSelectArea\\\" id=\\\"KeySilver\\\">\\n <div class=\\\"ArticleTypeArea\\\"></div>\\n <div class=\\\"MoveLink\\\">\\n  <p class=\\\"Count\\\">残り：488文字／全文：826文字</p>\\n  <ul>\\n   <li class=\\\"ReadMore\\\"><a href=\\\"http://digital.asahi.com/info/information/free_member/?jumpUrl=http%3A%2F%2Fdigital.asahi.com%2Farticles%2FASK3554R8K35PTQP00L.html%3F_requesturl%3Darticles%2FASK3554R8K35PTQP00L.html\\\" class=\\\"NonRegi\\\" onclick=\\\"s.getPreviousValue('fn3','sc_clk_btn','');\\\"><span>無料登録して全文を読む</span></a></li>\\n   <li class=\\\"ReadMore\\\"><a href=\\\"http://digital.asahi.com/articles/ASK3554R8K35PTQP00L.html?rm=338\\\" class=\\\"Lite Member\\\" onclick=\\\"s.getPreviousValue('fn1','sc_clk_btn','');\\\"><span>全文を読む</span></a></li>\\n   <li class=\\\"ReadMore\\\"><a href=\\\"http://digital.asahi.com/rd/lgck2.html?jumpUrl=http%3A%2F%2Fdigital.asahi.com%2Farticles%2FASK3554R8K35PTQP00L.html%3F_requesturl%3Darticles%2FASK3554R8K35PTQP00L.html%26rm%3D338\\\" class=\\\"Logout Member\\\" onclick=\\\"s.getPreviousValue('fn2','sc_clk_btn','');\\\"><span>ログインして全文を読む</span></a></li>\\n   <li class=\\\"ReadMore\\\">\\n    <noscript>\\n     <a href=\\\"http://digital.asahi.com/rd/lgck2.html?jumpUrl=http%3A%2F%2Fdigital.asahi.com%2Farticles%2FASK3554R8K35PTQP00L.html%3F_requesturl%3Darticles%2FASK3554R8K35PTQP00L.html%26rm%3D338\\\" class=\\\"NoScript\\\"><span>ログインして全文を読む</span></a>\\n    </noscript></li>\\n  </ul>\\n </div>\\n</div>\\n<!-- 有料記事　続きを読む END -->\\n<!--ArticleNoteBox BGN -->\\n<div class=\\\"ArticleNoteBox Lite LiteOut\\\">\\n <p>有料会員に登録すると全ての記事が読み放題です。</p>\\n <p>初月無料につき月初のお申し込みがお得</p>\\n <p><span class=\\\"EmText\\\">980円で月300本まで</span>読めるシンプルコースは<a href=\\\"http://digital.asahi.com/info/simple_course/?iref=com_smpl_txtpr\\\">こちら</a></p>\\n</div>\\n<!--ArticleNoteBox END -->\\n<!-- 緊急時用リンクはありません -->\\n<div class=\\\"RelatedLinkMod\\\">\\n <div class=\\\"Title\\\">\\n  関連ニュース\\n </div>\\n <ul>\\n  <li><a href=\\\"http://www.asahi.com/articles/ASJDM4T0NJDMPTQP004.html\\\">「ラグビーでマイナス、一つもない」　橋下徹さんと花園</a></li>\\n  <li><a href=\\\"http://www.asahi.com/articles/ASJCF4SQTJCFPTQP005.html\\\">伏見工・京都工学院、花園かなわず　故平尾さんの母校</a></li>\\n  <li><a href=\\\"http://www.asahi.com/articles/ASJDQ51MDJDQUTQP00Y.html\\\">平尾誠二さん、最後のインタビュー　５月に電話取材</a></li>\\n  <li><a href=\\\"http://www.asahi.com/articles/ASJ6G766CJ6GUTQP01W.html\\\">ラグビー史に輝く勝利　平尾誠二さんとスコットランド戦</a></li>\\n  <li><a href=\\\"http://www.asahi.com/articles/ASJBN452SJBNPTQP00F.html\\\">平尾誠二さん死去　５３歳、ラグビー日本代表の司令塔</a></li>\\n </ul>\\n</div>"));
//        messageList.add(new NewsMessage("1488744622706", "Asahi", "「つなぐ意識」流れ変えた　侍Ｊ・鈴木が３ラン", "2017-03-06T04:24:03+09:00", "http://www.asahicom.jp/articles/images/AS20170305002614_commL.jpg", "http://www.asahi.com/articles/ASK361CSSK35UOOB01Q.html?ref=rss", "<div class=\\\"ImagesMod\\\">\\n <div class=\\\"Image\\\">\\n  <p class=\\\"Width\\\"><a href=\\\"./photo/AS20170305002614.html\\\"><img alt=\\\"写真・図版\\\" oncontextmenu=\\\"return false\\\" src=\\\"//www.asahicom.jp/articles/images/AS20170305002614_commL.jpg\\\"><em class=\\\"Caption\\\">オリックスに先制された後の二回表の攻撃前、青木（中央）を中心に円陣を組む日本代表の選手たち＝川村直子撮影</em></a></p>\\n </div>\\n <!--Image-->\\n</div>\\n<!--ImagesMod-->\\n<!-- Ad BGN -->\\n<div class=\\\"AdMod\\\">\\n <!-- impact スポーツ記事中レクタングル(Aone仕様) RECT4★ここから-->\\n <script type=\\\"text/javascript\\\" language=\\\"JavaScript\\\"><!-- impAcn = \\\"ASAHISEG\\\";impAco = document.cookie;impApos = impAco.indexOf(impAcn+\\\"=\\\");impAseg = (impApos!=-1)?impAco.substring(impApos+impAcn.length+1,(impAco.indexOf(\\\"; \\\",impApos)!=-1)?impAco.indexOf(\\\"; \\\",impApos):impAco.length):null;impAseg = (impAseg)?unescape(impAseg):null;impAserver = 'http://imp.asahi.com';impAtag = \\\"/SITE=SPORTS/AREA=RECT4/AAMSZ=300X250/OENCJP=UTF8\\\";impAtarget = (impAseg)?impAtag+\\\"/\\\"+impAseg:impAtag;impArnd = Math.round(Math.random() * 100000000);if (!impApid) var impApid = Math.round(Math.random() * 100000000);document.write('<scr');document.write('ipt src=\\\"' + impAserver + '/jserver/acc_random=' + impArnd + impAtarget + '/pageid=' + impApid + '\\\">');document.write('</scr');document.write('ipt>');//--></script>\\n <span>[PR]</span>\\n <!-- impact スポーツ記事中レクタングル(Aone仕様) RECT4★ここまで-->\\n</div>\\n<!-- Ad END -->\\n<!-- ArticleText BGN -->\\n<div class=\\\"ArticleText\\\">\\n <!--googleon:index-->\\n <p>（５日、ＷＢＣ強化試合　日本代表５―３オリックス）</p>\\n <p>　鈴木が、一振りで嫌な流れを変えた。失策絡みで失った２点を追う二回、無死二、三塁の好機が回ってきた。１ボールからの２球目、高めに来た１３３キロを引っ張って左翼席へ。３点本塁打で試合をひっくり返した。「つなぐ意識だった。それが最高の形になった」と笑った。</p>\\n <p>　攻撃の直前、侍ジャパンは円陣…</p>\\n <!--googleoff:index-->\\n</div>\\n<!-- ArticleText END -->\\n<!-- 有料記事　続きを読む BGN -->\\n<div class=\\\"LoginSelectArea\\\" id=\\\"KeySilver\\\">\\n <div class=\\\"ArticleTypeArea\\\"></div>\\n <div class=\\\"MoveLink\\\">\\n  <p class=\\\"Count\\\">残り：160文字／全文：312文字</p>\\n  <ul>\\n   <li class=\\\"ReadMore\\\"><a href=\\\"http://digital.asahi.com/info/information/free_member/?jumpUrl=http%3A%2F%2Fdigital.asahi.com%2Farticles%2FASK353GN8K35UTQP00G.html%3F_requesturl%3Darticles%2FASK353GN8K35UTQP00G.html\\\" class=\\\"NonRegi\\\" onclick=\\\"s.getPreviousValue('fn3','sc_clk_btn','');\\\"><span>無料登録して全文を読む</span></a></li>\\n   <li class=\\\"ReadMore\\\"><a href=\\\"http://digital.asahi.com/articles/ASK353GN8K35UTQP00G.html?rm=152\\\" class=\\\"Lite Member\\\" onclick=\\\"s.getPreviousValue('fn1','sc_clk_btn','');\\\"><span>全文を読む</span></a></li>\\n   <li class=\\\"ReadMore\\\"><a href=\\\"http://digital.asahi.com/rd/lgck2.html?jumpUrl=http%3A%2F%2Fdigital.asahi.com%2Farticles%2FASK353GN8K35UTQP00G.html%3F_requesturl%3Darticles%2FASK353GN8K35UTQP00G.html%26rm%3D152\\\" class=\\\"Logout Member\\\" onclick=\\\"s.getPreviousValue('fn2','sc_clk_btn','');\\\"><span>ログインして全文を読む</span></a></li>\\n   <li class=\\\"ReadMore\\\">\\n    <noscript>\\n     <a href=\\\"http://digital.asahi.com/rd/lgck2.html?jumpUrl=http%3A%2F%2Fdigital.asahi.com%2Farticles%2FASK353GN8K35UTQP00G.html%3F_requesturl%3Darticles%2FASK353GN8K35UTQP00G.html%26rm%3D152\\\" class=\\\"NoScript\\\"><span>ログインして全文を読む</span></a>\\n    </noscript></li>\\n  </ul>\\n </div>\\n</div>\\n<!-- 有料記事　続きを読む END -->\\n<!--ArticleNoteBox BGN -->\\n<div class=\\\"ArticleNoteBox Lite LiteOut\\\">\\n <p>有料会員に登録すると全ての記事が読み放題です。</p>\\n <p>初月無料につき月初のお申し込みがお得</p>\\n <p><span class=\\\"EmText\\\">980円で月300本まで</span>読めるシンプルコースは<a href=\\\"http://digital.asahi.com/info/simple_course/?iref=com_smpl_txtpr\\\">こちら</a></p>\\n</div>\\n<!--ArticleNoteBox END -->\\n<!-- 緊急時用リンクはありません -->\\n<div class=\\\"RelatedLinkMod\\\">\\n <div class=\\\"Title\\\">\\n  関連ニュース\\n </div>\\n <ul>\\n  <li><a href=\\\"http://www.asahi.com/articles/ASK353GN7K35UTQP00D.html\\\">侍Ｊ、打線模索のまま７日初戦へ　先発藤浪も不安露呈</a></li>\\n  <li><a href=\\\"http://www.asahi.com/articles/ASK353HX0K35UTQP00Y.html\\\">侍Ｊ、代打秋山が九回に勝ち越し打　オリックスに勝利</a></li>\\n  <li><a href=\\\"http://www.asahi.com/articles/ASK2F6DLKK2FUEHF00H.html\\\">北京五輪のエラー？ああ、それ聞きます？　ＧＧ佐藤さん</a></li>\\n  <li><a href=\\\"http://www.asahi.com/articles/ASK2X7FH9K2XUEHF01Z.html\\\">ＷＢＣ連覇へ内川のファインプレー　名場面をモノマネで</a></li>\\n  <li><a href=\\\"http://www.asahi.com/articles/ASK2P1DRNK2NUEHF00X.html\\\">ＷＢＣ、捕手は？抑え投手は？　里崎智也さんに聞く展望</a></li>\\n </ul>\\n</div>"));
//        messageList.add(new NewsMessage("1488744625234", "Asahi", "「すかいらーく」復活のわけ…　谷真社長インタビュー", "2017-03-06T04:24:03+09:00", "http://www.asahicom.jp/articles/images/AS20170305001628_commL.jpg", "http://www.asahi.com/articles/ASK361CSSK35UOOB01Q.html?ref=rss", "<div class=\\\"ImagesMod\\\">\\n <div class=\\\"Image\\\">\\n  <p class=\\\"Width\\\"><a href=\\\"./photo/AS20170305001628.html\\\"><img alt=\\\"写真・図版\\\" oncontextmenu=\\\"return false\\\" src=\\\"//www.asahicom.jp/articles/images/AS20170305001628_commL.jpg\\\"><em class=\\\"Caption\\\">「バーミヤン」方南町駅前店で従業員と談笑する谷真社長（左）＝１月３１日、東京都杉並区、林紗記撮影</em></a></p>\\n  <ul class=\\\"Thum\\\">\\n   <li><a href=\\\"./photo/AS20170305001627.html\\\"><span class=\\\"Height\\\"><img alt=\\\"写真・図版\\\" oncontextmenu=\\\"return false\\\" src=\\\"//www.asahicom.jp/articles/images/AS20170305001627_commL.jpg\\\"></span></a></li>\\n   <li><a href=\\\"./photo/AS20170305001625.html\\\"><span class=\\\"Width\\\"><img alt=\\\"写真・図版\\\" oncontextmenu=\\\"return false\\\" src=\\\"//www.asahicom.jp/articles/images/AS20170305001625_commL.jpg\\\"></span></a></li>\\n  </ul>\\n </div>\\n <!--Image-->\\n</div>\\n<!--ImagesMod-->\\n<!-- Ad BGN -->\\n<div class=\\\"AdMod\\\">\\n <!-- impact 経済・マネー記事中レクタングル(Aone仕様) RECT4★ここから-->\\n <script type=\\\"text/javascript\\\" language=\\\"JavaScript\\\"><!-- impAcn = \\\"ASAHISEG\\\";impAco = document.cookie;impApos = impAco.indexOf(impAcn+\\\"=\\\");impAseg = (impApos!=-1)?impAco.substring(impApos+impAcn.length+1,(impAco.indexOf(\\\"; \\\",impApos)!=-1)?impAco.indexOf(\\\"; \\\",impApos):impAco.length):null;impAseg = (impAseg)?unescape(impAseg):null;impAserver = 'http://imp.asahi.com';impAtag = \\\"/SITE=BUSINESS/AREA=RECT4/AAMSZ=300X250/OENCJP=UTF8\\\";impAtarget = (impAseg)?impAtag+\\\"/\\\"+impAseg:impAtag;impArnd = Math.round(Math.random() * 100000000);if (!impApid) var impApid = Math.round(Math.random() * 100000000);document.write('<scr');document.write('ipt src=\\\"' + impAserver + '/jserver/acc_random=' + impArnd + impAtarget + '/pageid=' + impApid + '\\\">');document.write('</scr');document.write('ipt>');//--></script>\\n <span>[PR]</span>\\n <!-- impact 経済・マネー記事中レクタングル(Aone仕様) RECT4★ここまで-->\\n</div>\\n<!-- Ad END -->\\n<!-- ArticleText BGN -->\\n<div class=\\\"ArticleText\\\">\\n <!--googleon:index-->\\n <p>　どうしてすかいらーくは、一時は危機に陥り、復活できたのか。そしてこれからどこを目指すのか。谷真（たに・まこと）社長に聞いた。</p>\\n <div class=\\\"ExtendedLinkMod\\\">\\n  <ul>\\n   <li><a href=\\\"http://www.asahi.com/articles/ASK2S4GRSK2SULFA00X.html\\\">すかいらーく、ビッグデータ重視　新ブランド連発で復活</a></li>\\n  </ul>\\n </div>\\n <p>　――２００８年の社長就任前、すかいらーくは厳しい経営状況にありました。</p>\\n <p>　「デフレが進行して、（１９９７年をピークに）外食産業は縮小する傾向にありました。赤字店の閉鎖などの構造改革を行った後に、既存店の成長を促す施策をとらなければいけないのに、『出店＝成長』という考え方が社内に広がっていました。マーケットは拡大するもの、売り上げは上がるもの。そんな時代ではなかったのですが……。そうした考え方を変えていかないと何も始まらない状態でした」</p>\\n <p>　「私はすかいらーくの子会社、ニラックスに長く在籍していたので、さめた目で見てきました。消費者のニーズと当時のビジネスモデルが合っていたかといえば、明らかにギャップが生じていました。消費者のニーズが細分化されている状況に対応できる組織にはなっていなかったのです」</p>\\n <p>　――社長就任後、創業ブランドの「すかいらーく」の閉鎖を決断しました。７０年に１号店を開業し、ファミリーレストランの先駆け的な存在です。多くの従業員から「１店だけでも残して欲しい」との声も出たようですね。</p>\\n <p>　「確かに、そうした声は出ていました。しかし、創業ブランドのすかいらーくは、（同じファミレス業態の）ジョナサンと競合していました。ガストなどへの転換は避けて通れないことでした。ブランドを集約する動きと並行して、消費者ニーズの変化に対応するため『ステーキガスト』という新しいブランドを立ち上げてもいます」</p>\\n <p>　「（すかいらーくを閉鎖した翌…</p>\\n <!--googleoff:index-->\\n</div>\\n<!-- ArticleText END -->\\n<!-- 有料記事　続きを読む BGN -->\\n<div class=\\\"LoginSelectArea\\\" id=\\\"KeySilver\\\">\\n <div class=\\\"ArticleTypeArea\\\"></div>\\n <div class=\\\"MoveLink\\\">\\n  <p class=\\\"Count\\\">残り：2292文字／全文：2963文字</p>\\n  <ul>\\n   <li class=\\\"ReadMore\\\"><a href=\\\"http://digital.asahi.com/info/information/free_member/?jumpUrl=http%3A%2F%2Fdigital.asahi.com%2Farticles%2FASK2S560KK2SULFA029.html%3F_requesturl%3Darticles%2FASK2S560KK2SULFA029.html\\\" class=\\\"NonRegi\\\" onclick=\\\"s.getPreviousValue('fn3','sc_clk_btn','');\\\"><span>無料登録して全文を読む</span></a></li>\\n   <li class=\\\"ReadMore\\\"><a href=\\\"http://digital.asahi.com/articles/ASK2S560KK2SULFA029.html?rm=671\\\" class=\\\"Lite Member\\\" onclick=\\\"s.getPreviousValue('fn1','sc_clk_btn','');\\\"><span>全文を読む</span></a></li>\\n   <li class=\\\"ReadMore\\\"><a href=\\\"http://digital.asahi.com/rd/lgck2.html?jumpUrl=http%3A%2F%2Fdigital.asahi.com%2Farticles%2FASK2S560KK2SULFA029.html%3F_requesturl%3Darticles%2FASK2S560KK2SULFA029.html%26rm%3D671\\\" class=\\\"Logout Member\\\" onclick=\\\"s.getPreviousValue('fn2','sc_clk_btn','');\\\"><span>ログインして全文を読む</span></a></li>\\n   <li class=\\\"ReadMore\\\">\\n    <noscript>\\n     <a href=\\\"http://digital.asahi.com/rd/lgck2.html?jumpUrl=http%3A%2F%2Fdigital.asahi.com%2Farticles%2FASK2S560KK2SULFA029.html%3F_requesturl%3Darticles%2FASK2S560KK2SULFA029.html%26rm%3D671\\\" class=\\\"NoScript\\\"><span>ログインして全文を読む</span></a>\\n    </noscript></li>\\n  </ul>\\n </div>\\n</div>\\n<!-- 有料記事　続きを読む END -->\\n<!--ArticleNoteBox BGN -->\\n<div class=\\\"ArticleNoteBox Lite LiteOut\\\">\\n <p>有料会員に登録すると全ての記事が読み放題です。</p>\\n <p>初月無料につき月初のお申し込みがお得</p>\\n <p><span class=\\\"EmText\\\">980円で月300本まで</span>読めるシンプルコースは<a href=\\\"http://digital.asahi.com/info/simple_course/?iref=com_smpl_txtpr\\\">こちら</a></p>\\n</div>\\n<!--ArticleNoteBox END -->\\n<!-- 緊急時用リンクはありません -->\\n<!-- Outbrain TAG PC 2017.02.17 -->\\n<div class=\\\"OutbrainRecMod\\\">\\n <div class=\\\"Title\\\">\\n  こんなニュースも\\n </div>\\n <div class=\\\"OUTBRAIN\\\" data-src=\\\"DROP_PERMALINK_HERE\\\" data-widget-id=\\\"AR_7\\\" data-ob-template=\\\"AsahiShimbunDigital\\\"></div>\\n <div class=\\\"OUTBRAIN\\\" data-src=\\\"DROP_PERMALINK_HERE\\\" data-widget-id=\\\"AR_8\\\" data-ob-template=\\\"AsahiShimbunDigital\\\"></div>\\n <div class=\\\"OUTBRAIN\\\" data-src=\\\"DROP_PERMALINK_HERE\\\" data-widget-id=\\\"AR_9\\\" data-ob-template=\\\"AsahiShimbunDigital\\\"></div>\\n</div>\\n<!-- /Outbrain TAG PC 2017.02.17 -->"));

  }


    private String readFile(String filePath) {
        try {
            @SuppressWarnings("resource")
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            StringBuilder sb = new StringBuilder();

            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }

            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
