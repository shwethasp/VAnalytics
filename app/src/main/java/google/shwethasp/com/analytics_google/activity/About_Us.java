package google.shwethasp.com.analytics_google.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import google.shwethasp.com.analytics_google.Connectivity;
import google.shwethasp.com.analytics_google.MyAccountManager2;
import google.shwethasp.com.analytics_google.R;
import google.shwethasp.com.analytics_google.api_call.IGoogleAnalyticsApi;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//import com.uncopt.android.widget.text.justify.JustifiedTextView;

public class About_Us extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, MyAccountManager2.MyAccountManagerListener {
    private CollapsingToolbarLayout collapsingToolbarLayout;
    Toolbar toolbar;
    TextView nav_header_name;
    Spinner nav_header_spinner;
    CircleImageView nav_header_image;
    private static MyAccountManager2 accountManager;
    boolean isShow = false;
    int scrollRange = -1;
    private String accountName;
    int nav_pos;
    private SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    ProgressDialog authDialog;
    private static final String TAG = About_Us.class.getCanonicalName();
    ProgressDialog progressDialog;
    private CoordinatorLayout coordinatorLayout;
    private google.shwethasp.com.analytics_google.Account account;
//TextView aboutus_text1;
//    JustifiedTextView about_us_text2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayShowTitleEnabled(true);
        sharedpreferences = getApplicationContext().getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        /*final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_action_menu_left);
        ab.setDisplayHomeAsUpEnabled(true);*/


        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // aboutus_text1 = (TextView)findViewById(R.id.aboutus_text1);
        //aboutus_text1.setText(R.string.aboutus_text1);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.aboutus_coordinatorlayout);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        collapsingToolbarLayout.setTitleEnabled(false);
        collapsingToolbarLayout.setTitle("About us");
        //  collapsingToolbarLayout.set(Gravity.CENTER);
//        toolbar.setTitle("About us");
//        toolbar.setBackgroundColor(getResources().getColor(R.color.background_color));
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);

//        appBarLayout.addOnOffsetChangedListener(this);
        /*appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitleEnabled(false);
                    toolbar.setTitle("About us");
                    toolbar.setBackgroundColor(getResources().getColor(R.color.background_color));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitleEnabled(true);
                    collapsingToolbarLayout.setTitle(" ");//carefull there should a space between double quote otherwise it wont work
                    toolbar.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    isShow = false;
                }
            }
        });
*/

        dynamicToolbarColor();
        toolbarTextAppernce();

        // Create an object for subclass of AsyncTask
        GetXMLTask task = new GetXMLTask();
        // Execute the task
        task.execute(new String[]{BaseLoginActivity.pic_url});

        NavigationView nav_view = (NavigationView) findViewById(R.id.nav_view);
        nav_header_image = (CircleImageView) nav_view.getHeaderView(0).findViewById(R.id.nav_header_imageView);
        nav_header_name = (TextView) nav_view.getHeaderView(0).findViewById(R.id.nav_header_name_text);
        nav_header_spinner = (Spinner) nav_view.getHeaderView(0).findViewById(R.id.navigation_header_spinner);
        nav_view.setItemIconTintList(null);

        List<String> accounts = new ArrayList();
        accountManager = new MyAccountManager2(this);
        accounts.addAll(this.accountManager.getAccountNames());
        Collections.sort(accounts);
        nav_header_spinner.setAdapter(new ArrayAdapter(this, R.layout.nav_header_spinnerrow, R.id.nav_simple_row, accounts));
        nav_header_spinner.setSelection(BaseLoginActivity.pos - 1);
        //nav_header_name.setText(String.valueOf(accounts.get(BaseLoginActivity.pos - 1)).split("@")[0]);
        nav_header_name.setText(BaseLoginActivity.profile_name);
        nav_header_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (Connectivity.isConnected(About_Us.this)) {
                    if (BaseLoginActivity.isFirstlogin2) {
                        //accountName = (String) parent.getAdapter().getItem(position);
                      /*  BaseLoginActivity.resnamesubList.clear();
                        BaseLoginActivity.webtypetext.clear();
                        BaseLoginActivity.arrayListid.clear();
                        BaseLoginActivity.response_timeZoneArray.clear();*/
                        accountName = String.valueOf(nav_header_spinner.getSelectedItem());
                        nav_pos = nav_header_spinner.getSelectedItemPosition();
                        editor.putInt("position", 0);
                        editor.putString("selected_calender_pos", "1 Week");
                        editor.commit();

                        //accountName = (String) parent.getAdapter().getItem(position);
                        if (!accountManager.startAuthActivity(About_Us.this, 12, accountName, About_Us.this)) {
                            Toast.makeText(About_Us.this, "Account name error", Toast.LENGTH_SHORT).show();
                        }

                        authDialog = createAuthDialog1(About_Us.this);
                        nav_header_name.setText(BaseLoginActivity.profile_name);
                        WebSitePage.clicked_navigation = false;
                        authDialog.show();
                        BaseLoginActivity.pos = nav_pos + 1;
                        BaseLoginActivity.isFirstlogin2 = false;
                        WebSitePage.website_clickedposition = 0;
                    } else {
                        BaseLoginActivity.isFirstlogin2 = true;
                    }
                } else {
                    Snackbar.make(coordinatorLayout, "Lost Internet Connection", Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private ProgressDialog createAuthDialog1(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setIndeterminate(false);
        progressDialog.setMessage("Please wait ");
        progressDialog.setCancelable(false);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                cancelSendTask();
            }

            private void cancelSendTask() {
                Log.i(About_Us.TAG, "Cancel Authenticated task!!");
            }
        });

        return progressDialog;
    }
        /*toolbar = (Toolbar) findViewById(R.id.toolbarEdit);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        collapsingToolbarLayout.setTitleEnabled(true);
        collapsingToolbarLayout.setTitle(" ");


        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitleEnabled(false);
                    toolbar.setTitle("About us");
                    toolbar.setBackgroundColor(getResources().getColor(R.color.background_color));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitleEnabled(true);
                    collapsingToolbarLayout.setTitle(" ");//carefull there should a space between double quote otherwise it wont work
                    toolbar.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    isShow = false;
                }
            }
        });*/


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /*public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        *//*if (scrollRange == -1) {
            scrollRange = appBarLayout.getTotalScrollRange();
        }*//*
        if (verticalOffset == 0) {
            collapsingToolbarLayout.setTitleEnabled(false);
            toolbar.setTitle("About us");
            toolbar.setBackgroundColor(getResources().getColor(R.color.background_color));
            isShow = true;
        } else if (verticalOffset==1) {
            collapsingToolbarLayout.setTitleEnabled(true);
            collapsingToolbarLayout.setTitle(" ");//carefull there should a space between double quote otherwise it wont work
            toolbar.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            isShow = false;
        }
    }*/
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.home) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            if (Connectivity.isConnected(About_Us.this)) {

                Intent i = new Intent(About_Us.this, MainDashboard.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                BaseLoginActivity.isFirstlogin = false;
                MainDashboard.isFirstCall = true;
                BaseLoginActivity.isFirstlogin1 = false;
                BaseLoginActivity.isFirstlogin2 = false;
                startActivity(i);
                overridePendingTransition(R.anim.activity_slide_left_in,
                        R.anim.activity_slide_left_out);
            } else {
                Snackbar.make(coordinatorLayout, "Check your internet connection", Snackbar.LENGTH_SHORT).show();
            }

            item.setCheckable(false);
            // Handle the camera action
        } else if (id == R.id.websites) {
            if (Connectivity.isConnected(About_Us.this)) {
                Intent i = new Intent(About_Us.this, WebSitePage.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                overridePendingTransition(R.anim.activity_slide_left_in,
                        R.anim.activity_slide_left_out);
            } else {
                Snackbar.make(coordinatorLayout, "Check your internet connection", Snackbar.LENGTH_SHORT).show();
            }
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            // Handle the camera action
        } else if (id == R.id.aboutus) {

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.signout) {
            if (Connectivity.isConnected(About_Us.this)) {
                Intent i = new Intent(About_Us.this, LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                BaseLoginActivity.isFirstlogin1 = false;
                BaseLoginActivity.isFirstlogin2 = false;
                startActivity(i);
                overridePendingTransition(R.anim.activity_slide_left_in,
                        R.anim.activity_slide_left_out);
            } else {
                Snackbar.make(coordinatorLayout, "Check your internet connection", Snackbar.LENGTH_SHORT).show();
            }
//            item.setCheckable(false);
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
           /* Intent i = new Intent(MainDashboard.this,LoginActivity.class);
            startActivity(i);*/

        }/* else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
*/
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
        item.setCheckable(false);
        return true;
    }

    private void dynamicToolbarColor() {

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.mipmap.image);
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {

            @Override
            public void onGenerated(Palette palette) {
                collapsingToolbarLayout.setContentScrimColor(palette.getMutedColor(R.attr.background));
                collapsingToolbarLayout.setStatusBarScrimColor(palette.getMutedColor(R.attr.background));
            }
        });
    }

    private void toolbarTextAppernce() {
        // collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.string.time_zone);
        // collapsingToolbarLayout.setExpandedTitleTextAppearance(R.string.time_zone);
    }

    @Override
    public void onBackPressed() {
        BaseLoginActivity.isFirstlogin2 = false;
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            //  drawer.closeDrawers();
            drawer.closeDrawer(GravityCompat.START);
            // finish();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void authToken(String authToken) {
        Log.d(TAG, "authToken " + authToken);

        if (!isFinishing() && authToken != null) {
            this.account = new google.shwethasp.com.analytics_google.Account(this.accountName, authToken);
            getProfiles();
        }
    }

    private void getProfiles() {
        Log.i(TAG, "startService");
        createAuthDialog(this);

    }

    private ProgressDialog createAuthDialog(Context context) {
      /*  ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setIndeterminate(false);
        authDialog.setMessage(getResources().getString(R.string.Authenticating));
        progressDialog.setCancelable(false);*/
        authDialog.setMessage(getResources().getString(R.string.Authenticating));
        this.getAccounts(MyAccountManager2.restoken);

        //  this.userdetails(MyAccountManager2.restoken);
        authDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                cancelSendTask();
            }

            private void cancelSendTask() {
                Log.i(About_Us.TAG, "Cancel Authenticated task!!");
            }
        });

        return authDialog;
    }

    OkHttpClient okHttpClient = new OkHttpClient();

    private void getAccounts(final String token) {
        getProfilephoto(accountName);

        String BASE_URL = "https://www.googleapis.com";
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient.newBuilder().connectTimeout(30, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS).writeTimeout(30,
                                TimeUnit.SECONDS).build())
                .build();
        IGoogleAnalyticsApi mInterfaceService = retrofit.create(IGoogleAnalyticsApi.class);
        Call<JsonElement> mService = mInterfaceService.getAccounts("Bearer " + token);
        //    Call<List> mService = mInterfaceService.getData("Bearer "+token);
        mService.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response1) {
                Log.e("response code", response1.code() + "");
                if (response1.code() == 200) {
                    BaseLoginActivity.arrayListname.clear();
                    BaseLoginActivity.arrayListid.clear();
                    BaseLoginActivity.Hashmap.clear();
                    BaseLoginActivity.Hashmap1.clear();
                    BaseLoginActivity.webtypetext.clear();
                    BaseLoginActivity.resnamesubList.clear();
                    BaseLoginActivity.response_timeZoneArray.clear();
                    String Response = response1.body().toString();
                    Log.d("Result", String.valueOf(Response));


                    IGoogleAnalyticsApi mInterfaceService = retrofit.create(IGoogleAnalyticsApi.class);
                    Call<JsonElement> mService1 = mInterfaceService.getTimeZome("Bearer " + token);
                    mService1.enqueue(new Callback<JsonElement>() {
                        @Override
                        public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {


                            String response_timeZone = response.body().toString();
                            Log.d("response_timeZone", response_timeZone);

                            try {
                                JSONObject response_object = new JSONObject(response_timeZone);
                                JSONArray reJsonArray = response_object.getJSONArray("items");

                                for (int r = 0; r < reJsonArray.length(); r++) {
                                    JSONObject result_timezon_obj = reJsonArray.getJSONObject(r);
                                    BaseLoginActivity.response_timeZoneArray.add(String.valueOf(result_timezon_obj.getString("timezone")));
                                }
                                BaseLoginActivity.response_timeZoneArray.toString();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.e("JSONException", "JSONException");
                            }

                        }

                        @Override
                        public void onFailure(Call<JsonElement> call, Throwable t) {
                            Log.e("rescode", t.toString());
                            if (t instanceof ConnectException) {
                                dismissDialog();
                                MainDashboard.setRefreshActionButtonState(false);
                                Snackbar.make(coordinatorLayout, "Check your internet connection.", Snackbar.LENGTH_LONG).show();
                                //  accountsListView.setSelection(0);
                            }
                            if (t instanceof SocketTimeoutException) {
                                dismissDialog();
                                MainDashboard.setRefreshActionButtonState(false);
                                Snackbar.make(coordinatorLayout, "Check your internet connection.", Snackbar.LENGTH_LONG).show();
                                // accountsListView.setSelection(0);
                            }
                            if (t instanceof UnknownHostException) {
                                dismissDialog();
                                MainDashboard.setRefreshActionButtonState(false);
                                Snackbar.make(coordinatorLayout, "Check your internet connection.", Snackbar.LENGTH_LONG).show();
                                // accountsListView.setSelection(0);
                            }
                        }
                    });

                    Response = response1.body().toString();
                    Log.d("Result", String.valueOf(Response));



             /*   Intent intent = new Intent(BaseLoginActivity.this, Resultant.class);
                intent.putExtra("response", Response);
                startActivity(intent);*/

                    try {
                        dismissDialog();
                        JSONObject j = new JSONObject(Response);
                        // String result = j.getString("rows");
                        JSONArray items = j.getJSONArray("items");

                        for (int i = 0; i < items.length(); i++) {

                            // Log.d("out", String.valueOf(rows.get(i)));

                            //   JSONObject obj=new JSONObject(String.valueOf(items.get(i)));


                            JSONObject itemobj = new JSONObject(String.valueOf(items.get(i)));

                            BaseLoginActivity.resultentname = itemobj.getString("name");

                            BaseLoginActivity.arrayListname.add(BaseLoginActivity.resultentname);

                            Log.d("name", String.valueOf(BaseLoginActivity.arrayListname));
                            JSONArray webProperties = itemobj.getJSONArray("webProperties");


                            //int len=array.length();
                            // Log.d("Length",String.valueOf(len));
                            //resnamesubList = new ArrayList();
                            ArrayList<String> resnamesubListid = new ArrayList();
                            String SubPropertiesnames, SubPropertiesid;
                            for (int l = 0; l < webProperties.length(); l++) {

                                JSONObject webProperties01 = new JSONObject(String.valueOf(webProperties.get(l)));
                                JSONArray profiles1 = webProperties01.getJSONArray("profiles");
                                BaseLoginActivity.resultentname1 = webProperties01.getString("name");

                          /* arrayListname1.add(BaseLoginActivity.resultentname1);
                           Log.d("name1", String.valueOf(arrayListname1));
*/
                                for (int x = 0; x < profiles1.length(); x++) {
                                    JSONObject profileObject = profiles1.getJSONObject(x);
                                    BaseLoginActivity.resultentid = profileObject.getString("id");
                                    BaseLoginActivity.resnamesubList.add(BaseLoginActivity.resultentname1);
                                    BaseLoginActivity.webtypetext.add(profileObject.getString("name"));
                                    //webtype.add(profileObject.getString("type"));

                             /*   webName.add(profileObject.getString("name"));
                                webType.add(profileObject.getString("type"));*/

                                    resnamesubListid.add(BaseLoginActivity.resultentid);
                                    BaseLoginActivity.arrayListid.add(BaseLoginActivity.resultentid);

                                }
                                BaseLoginActivity.Hashmap1.put(BaseLoginActivity.resultentname1, BaseLoginActivity.resultentid);
                            }
                            BaseLoginActivity.Hashmap.put(BaseLoginActivity.resultentname, BaseLoginActivity.resnamesubList);

                        }


                    } catch (Exception e) {
                        Log.d("Exception", String.valueOf(e));

                    }
                    Intent intent = new Intent(About_Us.this, MainDashboard.class);
                    intent.putExtra("FirstCall", true);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.activity_slide_left_in,
                            R.anim.activity_slide_left_out);
                } else if (response1.code() == 401) {
                    //TODO: Token expired here
                    dismissDialog();
                    // Snackbar.make(coordinatorLayout, "Token Expired", Snackbar.LENGTH_LONG).show();
                    MainDashboard.setRefreshActionButtonState(false);
                    Snackbar.make(coordinatorLayout, "Refresh", Snackbar.LENGTH_LONG).show();
                    if (!accountManager.startAuthActivity(About_Us.this, 12, accountName, About_Us.this)) {
                        Toast.makeText(About_Us.this, "Account name error", Toast.LENGTH_SHORT).show();
                    }
                } else if (response1.code() == 403) {
                    dismissDialog();
                    //  Snackbar.make(coordinatorLayout,"Enable Analytics API to access data",Snackbar.LENGTH_SHORT).show();

                    AlertDialog.Builder builder = new AlertDialog.Builder(About_Us.this);
                    // Setting Dialog Message
                    builder.setMessage("Enable Analytics API")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                    Intent i = new Intent(About_Us.this, LoginActivity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(i);
                                    overridePendingTransition(R.anim.activity_slide_left_in,
                                            R.anim.activity_slide_left_out);
                                    //nav_header_spinner.setSelection(BaseLoginActivity.pos-1);
                                }
                            });
                    AlertDialog alert = builder.create();
                    // Showing Alert Message
                    alert.show();


                } else {

                }
            }

            public void onFailure(Call<JsonElement> call, Throwable throwable) {
                Log.d("rescode", throwable.toString());
                if (throwable instanceof SocketTimeoutException) {
                    dismissDialog();
                    Snackbar.make(coordinatorLayout, "Check your internet connection.", Snackbar.LENGTH_LONG).show();
                    // Snackbar.make(coordinatorLayout, "Socket Time out. Please try again.", Snackbar.LENGTH_LONG).show();
                }
            }


        });
    }


    public void dismissDialog() {
        if (authDialog != null) {
            try {
                authDialog.dismiss();
            } catch (Exception e) {
            }
        }
    }

    private class GetXMLTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls) {
            Bitmap map = null;
            for (String url : urls) {
                map = downloadImage(url);
            }
            return map;
        }

        // Sets the Bitmap returned by doInBackground
        @Override
        protected void onPostExecute(Bitmap result) {

            nav_header_image.setImageBitmap(result);
        }

        // Creates Bitmap from InputStream and returns it
        private Bitmap downloadImage(String url) {
            Bitmap bitmap = null;
            InputStream stream = null;
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inSampleSize = 1;

            try {
                stream = getHttpConnection(url);
                bitmap = BitmapFactory.
                        decodeStream(stream, null, bmOptions);
                stream.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return bitmap;
        }

        // Makes HttpURLConnection and returns InputStream
        private InputStream getHttpConnection(String urlString)
                throws IOException {
            InputStream stream = null;
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();

            try {
                HttpURLConnection httpConnection = (HttpURLConnection) connection;
                httpConnection.setRequestMethod("GET");
                httpConnection.connect();

                if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    stream = httpConnection.getInputStream();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return stream;
        }
    }

    public Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
        int targetWidth = 100;
        int targetHeight = 100;
        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,
                targetHeight, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(targetBitmap);
        Path path = new Path();
        path.addCircle(((float) targetWidth) / 2,
                ((float) targetHeight) / 2,
                (Math.min(((float) targetWidth),
                        ((float) targetHeight)) / 2),
                Path.Direction.CCW);

        canvas.clipPath(path);
        Bitmap sourceBitmap = scaleBitmapImage;
        canvas.drawBitmap(sourceBitmap,
                new Rect(0, 0, sourceBitmap.getWidth(),
                        sourceBitmap.getHeight()),
                new Rect(0, 0, targetWidth, targetHeight), null);
        return targetBitmap;
    }

    @Override
    protected void onResume() {
        BaseLoginActivity.isFirstlogin2 = false;
        super.onResume();
    }

    private void getProfilephoto(String email) {


        final OkHttpClient okHttpClient = new OkHttpClient();
        final String BASE_URL = "http://picasaweb.google.com/data/entry/api/user/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)

                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient.newBuilder().connectTimeout(30, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS).writeTimeout(30,
                                TimeUnit.SECONDS).build())
                .build();
        IGoogleAnalyticsApi mInterfaceService = retrofit.create(IGoogleAnalyticsApi.class);
        Call<JsonElement> mService = mInterfaceService.getProfile(BASE_URL + email + "?alt=json");
        //    Call<List> mService = mInterfaceService.getData("Bearer "+token);
        mService.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> profileresponse) {

                Log.e("mprofile response code", profileresponse.code() + "");

                if (profileresponse.code() == 200) {
                    String response_profile = profileresponse.body().toString();
                    Log.d("response_timeZone", response_profile);

                    try {

                        JSONObject profile_response_object = new JSONObject(response_profile);

//                      JSONObject object_pic=new JSONObject("object");

                        JSONObject profile_Jsonobj = profile_response_object.getJSONObject("entry");
                        JSONObject profilename_obj = profile_Jsonobj.getJSONObject("gphoto$nickname");
                        JSONObject profile_obj = profile_Jsonobj.getJSONObject("gphoto$thumbnail");

//                        JSONObject pic_result =new JSONObject(String.valueOf(profile_obj));

                        BaseLoginActivity.pic_url = profile_obj.getString("$t");
                        BaseLoginActivity.profile_name = profilename_obj.getString("$t");

                        // JSONObject re_json_obj=pic_result.get


//                        Log.d("redi", pic_url);



                       /* for (int r = 0; r < reJsonArray.length(); r++) {
                            JSONObject result_timezon_obj = reJsonArray.getJSONObject(r);
                            BaseLoginActivity.response_timeZoneArray.add(String.valueOf(result_timezon_obj.getString("timezone")));
                        }
                        BaseLoginActivity.response_timeZoneArray.toString();*/
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("JSONException", "JSONException");
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {

            }


        });
    }
}
