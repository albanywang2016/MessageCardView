package tech.japan.news.messagecardview.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import tech.japan.news.messagecardview.R;
import tech.japan.news.messagecardview.adapter.ConnectivityReceiver;
import tech.japan.news.messagecardview.controller.AppVolleySingleton;

import tech.japan.news.messagecardview.controller.Appirater;
import tech.japan.news.messagecardview.model.NewsMessage;
import tech.japan.news.messagecardview.utils.Const;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{

    RecyclerView rv;
    List<NewsMessage> messageList;
    DisplayMetrics metrics;
    ProgressBar pb;
    String underscore;
    String app_name;
    String package_version = "1.19";
    private static MainActivity mInstance;
    boolean isConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInstance = this;

        //Checking network connectivity
        isConnected = ConnectivityReceiver.isConnected();
        if(!isConnected){
            showAlert(Const.OOPS, getResources().getString(R.string.internet_not_connected));
            return;
        }

        //checking application version
        retrievePackageVersionFromDB(Const.APPLICATION_NAME);

        //asking for rating
        Appirater.appLaunched(MainActivity.this);


        metrics = getResources().getDisplayMetrics();

        setContentView(R.layout.tab_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tab_activity_toolbar);
        if(toolbar != null){
            setSupportActionBar(toolbar);
        }

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        PagerAdapter  pagerAdapter = new GeneralPagerAdapter(getSupportFragmentManager(), MainActivity.this);
        viewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

    }

    public static synchronized MainActivity getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        this.isConnected = isConnected;
    }

    public class GeneralPagerAdapter extends FragmentStatePagerAdapter {
        String tabTitles[] = new String[] {
                getResources().getString(R.string.domestic),
                getResources().getString(R.string.international),
                getResources().getString(R.string.business),
                getResources().getString(R.string.entertainment),
                getResources().getString(R.string.sport),
                getResources().getString(R.string.science),
                getResources().getString(R.string.life),
                getResources().getString(R.string.local),
                getResources().getString(R.string.magazine),
                getResources().getString(R.string.vedio)};
        Context context;

        public GeneralPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public Fragment getItem(int position) {
            return TabFragment.newInstance(position);
        }


        @Override
        public int getCount() {
            return tabTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        MainActivity.getInstance().setConnectivityListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(tech.japan.news.messagecardview.R.menu.menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case tech.japan.news.messagecardview.R.id.action_share:
                shareIt();
                break;
            case tech.japan.news.messagecardview.R.id.action_login_or_register:
                callLoginOrRegister();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void retrievePackageVersionFromDB(final String application_name) {
        StringRequest sr = new StringRequest(Request.Method.POST, Const.GET_PACKAGE_VERSION_PHP, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                if(!package_version.equalsIgnoreCase(response.toString())){
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.please_update_your_package), Toast.LENGTH_LONG).show();
                }else {
                    Log.d("Package version = ", response.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("retrievePackage", "error = " + error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put(Const.PROGRAM_NAME, application_name);
                return params;
            }
        };

        sr.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppVolleySingleton.getmInstance().addToRequestQueue(sr, Const.TAG);
    }


    private void shareIt(){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(tech.japan.news.messagecardview.R.string.share));
        intent.putExtra(intent.EXTRA_TEXT, getResources().getString(tech.japan.news.messagecardview.R.string.download_from));
        startActivity(Intent.createChooser(intent, getResources().getString(tech.japan.news.messagecardview.R.string.share_via)));

    }

    private void callLoginOrRegister(){

        final Dialog myDialog = new Dialog(this);
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.setContentView(tech.japan.news.messagecardview.R.layout.login_or_register);
        myDialog.setCancelable(true);
        myDialog.setCanceledOnTouchOutside(true);
        myDialog.show();

        final Button loginBtn = (Button) myDialog.findViewById(tech.japan.news.messagecardview.R.id.btn_login);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
                callLoginDialog();
            }
        });

        final Button registerBtn = (Button) myDialog.findViewById(tech.japan.news.messagecardview.R.id.btn_register);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
                callRegisterDialog();
            }
        });


    }


    private void callLoginDialog(){

        final Dialog myDialog = new Dialog(this);
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.setContentView(tech.japan.news.messagecardview.R.layout.login_layout);
        myDialog.setCancelable(false);

        final EditText etUserName = (EditText) myDialog.findViewById(tech.japan.news.messagecardview.R.id.login_et_username);
        final EditText etPassword = (EditText) myDialog.findViewById(tech.japan.news.messagecardview.R.id.login_et_password);
        myDialog.setCanceledOnTouchOutside(true);
        myDialog.show();


        //forgot password, direct to forgot password dalog
        final Button forgot = (Button) myDialog.findViewById(tech.japan.news.messagecardview.R.id.login_forgot_password_btn);
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
                callForgotPasswrodDialog();
            }
        });


        final Button login = (Button) myDialog.findViewById(tech.japan.news.messagecardview.R.id.loginButton);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = etUserName.getText().toString().trim();
                final String password = etPassword.getText().toString().trim();
                final String last_login = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

                if(username.length() == 0 || password.length() == 0){
                    showAlert(Const.OOPS, getResources().getString(tech.japan.news.messagecardview.R.string.should_not_be_empty));
                    return;
                }else{
                    StringRequest sr = new StringRequest(Request.Method.POST, Const.USER_LOGIN_PHP, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            myDialog.dismiss();
                            Log.d("userid", response.toString());
                            if(response.contains(getResources().getString(tech.japan.news.messagecardview.R.string.could_not_login)) || response.length() == 0){
                                showAlert(Const.OOPS, getResources().getString(tech.japan.news.messagecardview.R.string.could_not_login) + " " + getResources().getString(tech.japan.news.messagecardview.R.string.input_wrong));
                                return;
                            }else{
                                Const.USER_ID = Integer.parseInt(response.toString());
                                Toast.makeText(MainActivity.this, getResources().getString(tech.japan.news.messagecardview.R.string.login_successfully), Toast.LENGTH_LONG).show();
                                return;
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            myDialog.dismiss();
                            showAlert(getResources().getString(tech.japan.news.messagecardview.R.string.error), error.toString());
                            return;
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> params = new HashMap<String, String>();
                            params.put(Const.KEY_USERNAME,username);
                            params.put(Const.KEY_PASSWORD,password);
                            params.put(Const.LAST_LOGIN, last_login);
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
        myDialog.setContentView(tech.japan.news.messagecardview.R.layout.register_layout);
        myDialog.setCancelable(false);

        final EditText etUsername = (EditText) myDialog.findViewById(tech.japan.news.messagecardview.R.id.register_et_username);
        final EditText etEmail = (EditText) myDialog.findViewById(tech.japan.news.messagecardview.R.id.register_et_email);
        final EditText etPassword = (EditText) myDialog.findViewById(tech.japan.news.messagecardview.R.id.register_et_password);
        final EditText etPassword2 = (EditText) myDialog.findViewById(tech.japan.news.messagecardview.R.id.register_et_password2);
        myDialog.setCanceledOnTouchOutside(true);
        //myDialog.getWindow().setLayout((7 * metrics.widthPixels)/8, (4 * metrics.heightPixels)/5);
        myDialog.show();


        final Button register = (Button) myDialog.findViewById(tech.japan.news.messagecardview.R.id.registerButton);
        register.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                //your login calculation goes here
                final String username = etUsername.getText().toString().trim();
                final String email = etEmail.getText().toString().trim();
                final String password = etPassword.getText().toString().trim();
                final String password2 = etPassword2.getText().toString().trim();
                final String created_date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                final String last_login = new SimpleDateFormat("yyyy-MM-dd").format(new Date());


                if(username.length() == 0 || email.length() == 0 || password.length() == 0 || password2.length() == 0){
                    showAlert(Const.OOPS, getResources().getString(tech.japan.news.messagecardview.R.string.should_not_be_empty));
                    return;
                }else if (!password.equalsIgnoreCase(password2)){
                    showAlert(Const.OOPS, getResources().getString(tech.japan.news.messagecardview.R.string.input_not_the_same));
                    return;
                }
                else{
                    StringRequest sr = new StringRequest(Request.Method.POST, Const.USER_REGISTER_PHP, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            myDialog.dismiss();
                            if(response.toString().contains(Const.DUPLICATE_ENTRY)){
                                showAlert(getResources().getString(tech.japan.news.messagecardview.R.string.error), getResources().getString(tech.japan.news.messagecardview.R.string.duplicate_entry));
                            }else if(response.toString().contains(Const.ERROR)){
                                showAlert(getResources().getString(tech.japan.news.messagecardview.R.string.error), response.toString());
                            }else{
                                showAlert("", getResources().getString(tech.japan.news.messagecardview.R.string.successfully_registered));
                            }
                            return;
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            myDialog.dismiss();
                            showAlert(getResources().getString(tech.japan.news.messagecardview.R.string.error), error.toString());
                            return;
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> params = new HashMap<String, String>();
                            params.put(Const.KEY_USERNAME,username);
                            params.put(Const.KEY_PASSWORD,password);
                            params.put(Const.KEY_EMAIL, email);
                            params.put(Const.CREATED_DATE, created_date);
                            params.put(Const.LAST_LOGIN, last_login);
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
        myDialog.setContentView(tech.japan.news.messagecardview.R.layout.forgot_password);
        myDialog.setCancelable(false);
        myDialog.setCanceledOnTouchOutside(true);
        //myDialog.getWindow().setLayout((6 * metrics.widthPixels)/7, (1 * metrics.heightPixels)/3);
        myDialog.show();


        final EditText etEmail = (EditText) myDialog.findViewById(tech.japan.news.messagecardview.R.id.forgot_et_email);
        final Button send = (Button) myDialog.findViewById(tech.japan.news.messagecardview.R.id.forgot_send_btn);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = etEmail.getText().toString().trim();
                if(email.length() == 0){
                    showAlert(Const.OOPS, getResources().getString(tech.japan.news.messagecardview.R.string.should_not_be_empty));
                    return;
                }else{
                    StringRequest sr = new StringRequest(Request.Method.POST, Const.USER_FORGOT_PASSWORD_PHP, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            myDialog.dismiss();
                            //Log.d("RetrieveUserPassword", response.toString());
                            if(response.contains(getResources().getString(tech.japan.news.messagecardview.R.string.could_not_get_password))){
                                showAlert(Const.OOPS, getResources().getString(tech.japan.news.messagecardview.R.string.could_not_get_password)+ " " + getResources().getString(tech.japan.news.messagecardview.R.string.input_wrong));
                            }else if(response.length() == 0){
                                //Log.d("RetrieveUserPassword", response.toString());
                                showAlert(getResources().getString(tech.japan.news.messagecardview.R.string.error), getResources().getString(tech.japan.news.messagecardview.R.string.could_not_get_password));
                            }else{
                                showAlert("", response.toString());
                            }
                            return;
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


}
