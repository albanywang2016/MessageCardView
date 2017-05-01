package com.example.leiwang.messagecardview.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import com.example.leiwang.messagecardview.R;
import com.example.leiwang.messagecardview.adapter.MessageAdapter;
import com.example.leiwang.messagecardview.controller.AppVolleySingleton;

import com.example.leiwang.messagecardview.model.NewsMessage;
import com.example.leiwang.messagecardview.utils.Const;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    RecyclerView rv;
    MessageAdapter ma;
    List<NewsMessage> messageList;
    DisplayMetrics metrics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        metrics = getResources().getDisplayMetrics();
        Const.CURRENT_CHANNEL = Const.CHANNEL_DOMESTIC;

        rv = (RecyclerView) findViewById(R.id.messageList);
        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llm);

        messageList = new ArrayList<>();

        //get Json array view php
        getJsonArrayViaPHP(Const.CHANNEL_DOMESTIC);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        String underscore = getResources().getString(R.string.enderscore);
        String app_name = getResources().getString(R.string.app_name);

        switch (id){
            case R.id.action_share:
                shareIt();
                break;
            case R.id.action_login_or_register:
                callLoginOrRegister();
                break;
            case R.id.action_refresh:
                getJsonArrayViaPHP(Const.CURRENT_CHANNEL);
                break;
            case R.id.action_domestic:
                this.setTitle(app_name + underscore + getResources().getString(R.string.domestic));
                Const.CURRENT_CHANNEL = Const.CHANNEL_DOMESTIC;
                getJsonArrayViaPHP(Const.CHANNEL_DOMESTIC);
                break;
            case R.id.action_international:
                getJsonArrayViaPHP(Const.CHANNEL_INTERNATIONAL);
                this.setTitle(app_name + underscore + getResources().getString(R.string.international));
                Const.CURRENT_CHANNEL = Const.CHANNEL_INTERNATIONAL;
                break;
            case R.id.action_business:
                getJsonArrayViaPHP(Const.CHANNEL_BUSINESS);
                this.setTitle(app_name + underscore + getResources().getString(R.string.business));
                Const.CURRENT_CHANNEL = Const.CHANNEL_BUSINESS;
                break;
            case R.id.action_entertainment:
                getJsonArrayViaPHP(Const.CHANNEL_ENTERTAINMENT);
                this.setTitle(app_name + underscore + getResources().getString(R.string.entertainment));
                Const.CURRENT_CHANNEL = Const.CHANNEL_ENTERTAINMENT;
                break;
            case R.id.action_sport:
                getJsonArrayViaPHP(Const.CHANNEL_SPORT);
                this.setTitle(app_name + underscore + getResources().getString(R.string.sport));
                Const.CURRENT_CHANNEL = Const.CHANNEL_SPORT;
                break;
            case R.id.action_science:
                getJsonArrayViaPHP(Const.CHANNEL_SCIENCE);
                this.setTitle(app_name + underscore + getResources().getString(R.string.science));
                Const.CURRENT_CHANNEL = Const.CHANNEL_SCIENCE;
                break;
            case R.id.action_life:
                getJsonArrayViaPHP(Const.CHANNEL_LIFE);
                this.setTitle(app_name + underscore + getResources().getString(R.string.life));
                Const.CURRENT_CHANNEL = Const.CHANNEL_LIFE;
                break;
            case R.id.action_local:
                getJsonArrayViaPHP(Const.CHANNEL_LOCAL);
                this.setTitle(app_name + underscore + getResources().getString(R.string.local));
                Const.CURRENT_CHANNEL = Const.CHANNEL_LOCAL;
                break;
            case R.id.action_magazine:
                getJsonArrayViaPHP(Const.CHANNEL_MAGAZINE);
                this.setTitle(app_name + underscore + getResources().getString(R.string.magazine));
                Const.CURRENT_CHANNEL = Const.CHANNEL_MAGAZINE;
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void shareIt(){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.share));
        intent.putExtra(intent.EXTRA_TEXT, getResources().getString(R.string.download_from));
        startActivity(Intent.createChooser(intent, getResources().getString(R.string.share_via)));

    }

    private void callLoginOrRegister(){

        final Dialog myDialog = new Dialog(this);
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.setContentView(R.layout.login_or_register);
        myDialog.setCancelable(true);
        myDialog.setCanceledOnTouchOutside(true);
        myDialog.getWindow().setLayout((8 * metrics.widthPixels)/9, (2 * metrics.heightPixels)/3);
        myDialog.show();

        final Button loginBtn = (Button) myDialog.findViewById(R.id.btn_login);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
                callLoginDialog();
            }
        });

        final Button registerBtn = (Button) myDialog.findViewById(R.id.btn_register);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
                callRegisterDialog();
            }
        });

        final Button cancelBtn = (Button) myDialog.findViewById(R.id.btn_login_register_cancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

    }

    private void callLoginDialog(){

        final Dialog myDialog = new Dialog(this);
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.setContentView(R.layout.login_layout);
        myDialog.setCancelable(false);

        final EditText etUserName = (EditText) myDialog.findViewById(R.id.login_et_username);
        final EditText etPassword = (EditText) myDialog.findViewById(R.id.login_et_password);
        myDialog.setCanceledOnTouchOutside(true);
        myDialog.getWindow().setLayout((6 * metrics.widthPixels)/7, (2 * metrics.heightPixels)/3);
        myDialog.show();

        final Button cancelBtn = (Button) myDialog.findViewById(R.id.btn_login_cancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        //forgot password, direct to forgot password dalog
        final Button forgot = (Button) myDialog.findViewById(R.id.login_forgot_password_btn);
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
                callForgotPasswrodDialog();
            }
        });


        final Button login = (Button) myDialog.findViewById(R.id.loginButton);
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
                            Log.d("userid", response.toString());
                            if(response.contains(Const.COULD_NOT_LOGIN)){
                                showAlert(Const.OOPS, Const.COULD_NOT_LOGIN + " " + Const.INPUT_WRONG);
                                return;
                            }else{
                                Const.USER_ID = Integer.parseInt(response.toString());
                                Toast.makeText(MainActivity.this, Const.LOG_IN_SUCCESSFULLY, Toast.LENGTH_LONG).show();
                                return;
                            }
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
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.setContentView(R.layout.register_layout);
        myDialog.setCancelable(false);

        final EditText etUsername = (EditText) myDialog.findViewById(R.id.register_et_username);
        final EditText etEmail = (EditText) myDialog.findViewById(R.id.register_et_email);
        final EditText etMail2 = (EditText) myDialog.findViewById(R.id.register_et_email2);
        final EditText etPassword = (EditText) myDialog.findViewById(R.id.register_et_password);
        final EditText etPassword2 = (EditText) myDialog.findViewById(R.id.register_et_password2);
        myDialog.setCanceledOnTouchOutside(true);
        myDialog.getWindow().setLayout((7 * metrics.widthPixels)/8, (6 * metrics.heightPixels)/7);
        myDialog.show();

        final Button cancelBtn = (Button) myDialog.findViewById(R.id.btn_register_cancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        final Button register = (Button) myDialog.findViewById(R.id.registerButton);
        register.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                //your login calculation goes here
                final String username = etUsername.getText().toString().trim();
                final String email = etEmail.getText().toString().trim();
                final String email2 = etMail2.getText().toString().trim();
                final String password = etPassword.getText().toString().trim();
                final String password2 = etPassword2.getText().toString().trim();

                if(username.length() == 0 || email.length() == 0 || email2.length() == 0 || password.length() == 0 || password2.length() == 0){
                    showAlert(Const.OOPS, Const.INPUT_WRONG);
                    return;
                }else if (!email.equalsIgnoreCase(email2) || !password.equalsIgnoreCase(password2)){
                    showAlert(Const.OOPS, Const.INPUT_WRONG_NOT_THE_SAME);
                    return;
                }
                else{
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

    private void callForgotPasswrodDialog() {
        final Dialog myDialog = new Dialog(this);
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.setContentView(R.layout.forgot_password);
        myDialog.setCancelable(false);
        myDialog.setCanceledOnTouchOutside(true);
        myDialog.getWindow().setLayout((6 * metrics.widthPixels)/7, (1 * metrics.heightPixels)/3);
        myDialog.show();

        final Button cancelBtn = (Button) myDialog.findViewById(R.id.btn_forgot_cancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        final EditText etEmail = (EditText) myDialog.findViewById(R.id.forgot_et_email);
        final Button send = (Button) myDialog.findViewById(R.id.forgot_send_btn);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = etEmail.getText().toString().trim();
                if(email.length() == 0){
                    showAlert(Const.OOPS, Const.INPUT_WRONG);
                    return;
                }else{
                    StringRequest sr = new StringRequest(Request.Method.POST, Const.USER_FORGOT_PASSWORD_PHP, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            myDialog.dismiss();
                            //Log.d("RetrieveUserPassword", response.toString());
                            if(response.contains(Const.COULD_NOT_GET_PASSWORD)){
                                showAlert(Const.OOPS, Const.COULD_NOT_GET_PASSWORD + " " + Const.INPUT_WRONG);
                                return;
                            }else{
                                //Log.d("RetrieveUserPassword", response.toString());
                                showAlert("", Const.YOUR_PASSWORD_IS + response.toString());
                                return;
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            myDialog.dismiss();
                            return;
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> params = new HashMap<String, String>();
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

    private void getJsonArrayViaPHP(final String channel) {
        StringRequest sr = new StringRequest(Request.Method.POST, Const.GET_MESSAGE_BY_CHANNEL, new Response.Listener<String>() {
            //List<NewsMessage> mList = new ArrayList<>();
            @Override
            public void onResponse(String response) {
                Log.d("getJsonArrayViaPHP", "the response is =" + response);
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                messageList = Arrays.asList(gson.fromJson(response, NewsMessage[].class));
                Log.d("getJsonArrayViaPHP", "message List = " + messageList.toString());
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
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put(Const.CHANNEL, channel);
                return params;
            }
        };

        sr.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppVolleySingleton.getmInstance().addToRequestQueue(sr, Const.TAG);

    }


    private void startWebViewActivity(String url) {
        Intent intent = new Intent(this, WebViewContents.class);
        intent.putExtra("ArticleURL", url);
        startActivity(intent);
    }

}
