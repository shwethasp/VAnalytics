package google.shwethasp.com.analytics_google.activity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
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
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.*;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
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
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import google.shwethasp.com.analytics_google.BuildConfig;
import google.shwethasp.com.analytics_google.Connectivity;
import google.shwethasp.com.analytics_google.MyAccountManager2;
import google.shwethasp.com.analytics_google.R;
import google.shwethasp.com.analytics_google.WebsiteModel;
import google.shwethasp.com.analytics_google.adapter.PagerAdapter;
import google.shwethasp.com.analytics_google.adapter.Website_CustomAdapter;

import okhttp3.OkHttpClient;

//import google.shwethasp.com.analytics_google.adapters.PagerAdapter;
//import google.shwethasp.com.analytics_google.adapters.Website_CustomAdapter;
import google.shwethasp.com.analytics_google.api_call.IGoogleAnalyticsApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WebSitePage extends ActionBarActivity implements NavigationView.OnNavigationItemSelectedListener, TabLayout.OnTabSelectedListener, MyAccountManager2.MyAccountManagerListener {

    private RecyclerView mRecyclerView;
    private Website_CustomAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "CardViewActivity";

    private HashMap<ArrayList<String>, String> Hashmap = new HashMap<>();

    //This is our tablayout
    private TabLayout web_tabLayout;
    ProgressDialog authDialog;
    public static ProgressDialog authDialog1;
    //This is our viewPager
    private ViewPager web_viewPager;
    private Menu optionsMenu;
    PagerAdapter web_adapter;

    Spinner nav_header_spinner;
    TextView nav_header_name;
    CircleImageView nav_header_image;
    private MyAccountManager2 accountManager;
    private static final String TAG = WebSitePage.class.getCanonicalName();
    private google.shwethasp.com.analytics_google.Account account;
    private String accountName;

    public static String resid;
    public static Boolean clicked_navigation = false;
    public static int website_clickedposition;
    private CoordinatorLayout coordinatorLayout;
    ProgressDialog progressDialog, progressDialog1;
    private SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    int nav_pos;

    private ArrayList<WebsiteModel> modelArrayList = new ArrayList<WebsiteModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_web_site_page);

        //handleIntent(getIntent());
        Toolbar toolbar = (Toolbar) findViewById(R.id.websitetoolbar);
        setSupportActionBar(toolbar);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorlayout);
        sharedpreferences = getApplicationContext().getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // Create an object for subclass of AsyncTask
        GetXMLTask task = new GetXMLTask();
        // Execute the task
        task.execute(new String[]{BaseLoginActivity.pic_url});

        if (BaseLoginActivity.resnamesubList != null && BaseLoginActivity.resnamesubList.size() != 0) {
            for (int i = 0; i < BaseLoginActivity.resnamesubList.size(); i++) {
                WebsiteModel websiteModel = new WebsiteModel();
                websiteModel.setWebSiteName(BaseLoginActivity.resnamesubList.get(i));
                websiteModel.setWebSiteType(BaseLoginActivity.webtypetext.get(i));
                websiteModel.setTimeZone(BaseLoginActivity.response_timeZoneArray.get(i));
                websiteModel.setWebSiteId(BaseLoginActivity.arrayListid.get(i));
                modelArrayList.add(websiteModel);
            }
            mAdapter = new Website_CustomAdapter(modelArrayList);
             /*,BaseLoginActivity.webtype*/
            mRecyclerView.setAdapter(mAdapter);
        }


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
        // nav_header_name.setText(String.valueOf(accounts.get(BaseLoginActivity.pos - 1)).split("@")[0]);
        nav_header_name.setText(BaseLoginActivity.profile_name);

        // Code to Add an item with default animation
        //((MyRecyclerViewAdapter) mAdapter).addItem(obj, index);

        // Code to remove an item with default animation
        //((MyRecyclerViewAdapter) mAdapter).deleteItem(index);
        //Initializing the tablayout
     /*   web_tabLayout = (TabLayout) findViewById(R.id.website_tabLayout);

        //Adding the tabs using addTab() method
        web_tabLayout.addTab(web_tabLayout.newTab().setText("RECENTS"));
        web_tabLayout.addTab(web_tabLayout.newTab().setText("STARRED"));
        web_tabLayout.addTab(web_tabLayout.newTab().setText("ALL"));
        web_tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        //  chart = (BarChart) findViewById(R.id.chart);

        //Initializing viewPager
        web_viewPager = (ViewPager) findViewById(R.id.website_pager);

        //Creating our pager adapter
        web_adapter = new PagerAdapter(getSupportFragmentManager(), web_tabLayout.getTabCount(), 0);

        //Adding adapter to pager
        web_viewPager.setAdapter(web_adapter);

        web_tabLayout.setupWithViewPager(web_viewPager);

        web_viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1)

                    web_tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                        @Override
                        public void onTabSelected(TabLayout.Tab tab) {

                            web_viewPager.setCurrentItem(tab.getPosition());

                        }

                        @Override
                        public void onTabUnselected(TabLayout.Tab tab) {

                        }

                        @Override
                        public void onTabReselected(TabLayout.Tab tab) {

                        }
                    });
            }


            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //Adding onTabSelectedListener to swipe views
        web_tabLayout.setOnTabSelectedListener(this);


*/
        toolbar.setTitle("Analytics");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        nav_view.setNavigationItemSelectedListener(this);


        nav_header_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (Connectivity.isConnected(WebSitePage.this)) {
                    if (BaseLoginActivity.isFirstlogin1) {
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
                        if (!accountManager.startAuthActivity(WebSitePage.this, 12, accountName, WebSitePage.this)) {
                            Toast.makeText(WebSitePage.this, "Account name error", Toast.LENGTH_SHORT).show();
                        }

                        authDialog = createAuthDialog1(WebSitePage.this);
                        nav_header_name.setText(BaseLoginActivity.profile_name);
                        WebSitePage.clicked_navigation = false;
                        authDialog.show();
                        BaseLoginActivity.pos = nav_pos + 1;
                        BaseLoginActivity.isFirstlogin1 = false;
                        WebSitePage.website_clickedposition = 0;
                    } else {
                        BaseLoginActivity.isFirstlogin1 = true;
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
                Log.i(WebSitePage.TAG, "Cancel Authenticated task!!");
            }
        });

        return progressDialog;
    }

    private ProgressDialog createAuthDialog2(Context context) {
        progressDialog1 = new ProgressDialog(context);
        progressDialog1.setIndeterminate(false);
        progressDialog1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog1.setMessage("Please wait ");
        progressDialog1.setCancelable(false);
        progressDialog1.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                cancelSendTask();
            }

            private void cancelSendTask() {
                Log.i(WebSitePage.TAG, "Cancel Authenticated task!!");
            }
        });

        return progressDialog1;
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((Website_CustomAdapter) mAdapter).setOnItemClickListener(new Website_CustomAdapter
                .MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Log.i(LOG_TAG, " Clicked on Item " + position);
             /*   authDialog1 = createAuthDialog2(WebSitePage.this);
                authDialog1.show();*/
                // Toast.makeText(getApplicationContext(), "clicked" + position, Toast.LENGTH_SHORT).show();
                if (Connectivity.isConnected(WebSitePage.this)) {
                    Intent i = new Intent(WebSitePage.this, MainDashboard.class);

                    String pos = BaseLoginActivity.arrayListid.get(website_clickedposition);
//                    website_clickedposition = position;
                /* final HashMap<String, String> resultlistid = BaseLoginActivity.Hashmap1;*/

                    resid = pos;
                    clicked_navigation = true;
                    BaseLoginActivity.isFirstlogin = false;
                    MainDashboard.isFirstCall = true;
                    BaseLoginActivity.isFirstlogin1 = false;
                    BaseLoginActivity.isFirstlogin2 = false;
                    //  Hashmap.put(BaseLoginActivity.resnamesubList, BaseLoginActivity.resultentid);
                    i.putExtra("position", resid);
                    i.putExtra("FirstCall", true);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    overridePendingTransition(R.anim.activity_slide_left_in,
                            R.anim.activity_slide_left_out);
//                    finish();
                } else {
                    Intent i = new Intent(WebSitePage.this, MainDashboard.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.activity_slide_left_in,
                            R.anim.activity_slide_left_out);
                    finish();
                    MainDashboard.dismissDialog();
                }
            }
        });

        List<String> accounts = new ArrayList();
        accounts.addAll(this.accountManager.getAccountNames());
        Collections.sort(accounts);

        BaseLoginActivity.isFirstlogin1 = false;
        BaseLoginActivity.isFirstlogin2 = false;

        //  accounts.add(0, "Select Account");
        nav_header_spinner.setAdapter(new ArrayAdapter(this, R.layout.nav_header_spinnerrow, R.id.nav_simple_row, accounts));
        nav_header_spinner.setSelection(BaseLoginActivity.pos - 1);
        logAccounts(accounts);
    }

    @Override
    public void onBackPressed() {
        BaseLoginActivity.isFirstlogin1 = false;
        BaseLoginActivity.isFirstlogin2 = false;
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        } else {
            super.onBackPressed();
        }
    }

    private void logAccounts(List<String> accounts) {
        int i = 0;
        if (accounts != null && accounts.size() > 0) {
            for (String e : accounts) {
                int i2 = i + 1;
                Crashlytics.getInstance().core.setString("Email " + i, e);
                i = i2;
            }
        }
        Crashlytics.getInstance().core.setString("Emails #", BuildConfig.FLAVOR + i);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.home) {

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            if (Connectivity.isConnected(WebSitePage.this)) {
                BaseLoginActivity.isFirstlogin = false;
                MainDashboard.isFirstCall = true;
                BaseLoginActivity.isFirstlogin1 = false;
                BaseLoginActivity.isFirstlogin2 = false;

                Intent i = new Intent(WebSitePage.this, MainDashboard.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);

                overridePendingTransition(R.anim.activity_slide_left_in,
                        R.anim.activity_slide_left_out);
            } else {
                Snackbar.make(coordinatorLayout, "Check your internet connection", Snackbar.LENGTH_SHORT).show();
            }
            // Handle the camera action
        } else if (id == R.id.websites) {

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
         /*   DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;*/
            // Handle the camera action
        } else if (id == R.id.aboutus) {
            if (Connectivity.isConnected(WebSitePage.this)) {
                Intent i = new Intent(WebSitePage.this, About_Us.class);
                startActivity(i);

                overridePendingTransition(R.anim.activity_slide_left_in,
                        R.anim.activity_slide_left_out);

            } else {
                Snackbar.make(coordinatorLayout, "Check your internet connection", Snackbar.LENGTH_SHORT).show();
            }

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);

        } else if (id == R.id.signout) {
            if (Connectivity.isConnected(WebSitePage.this)) {
                Intent i = new Intent(WebSitePage.this, LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                BaseLoginActivity.isFirstlogin1 = false;
                BaseLoginActivity.isFirstlogin2 = false;
                startActivity(i);
                overridePendingTransition(R.anim.activity_slide_left_in,
                        R.anim.activity_slide_left_out);
            } else {
                Snackbar.make(coordinatorLayout, "Check your internet connection", Snackbar.LENGTH_SHORT).show();
            }

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);


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

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void authToken(String authToken) {
        Log.d(TAG, "authToken " + authToken);

        if (!isFinishing() && authToken != null) {
            this.account = new google.shwethasp.com.analytics_google.Account(this.accountName, authToken);
            getProfiles();
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 12 && resultCode == -1) {
            this.accountManager.getAsyncToken(this.accountName, this);
        } else if (requestCode == 10 && resultCode == -1) {
            setResult(-1);
            finish();
        }
    }


    /* private ArrayList<DataObject> getDataSet() {
         ArrayList results = new ArrayList<DataObject>();
         for (int index = 0; index < 20; index++) {
             DataObject obj = new DataObject("Some Primary Text " + index,
                     "Secondary " + index);
             results.add(index, obj);
         }
         return results;
     }*/

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
                    Intent intent = new Intent(WebSitePage.this, MainDashboard.class);
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
                    if (!accountManager.startAuthActivity(WebSitePage.this, 12, accountName, WebSitePage.this)) {
                        Toast.makeText(WebSitePage.this, "Account name error", Toast.LENGTH_SHORT).show();
                    }
                } else if (response1.code() == 403) {
                    dismissDialog();
                    //  Snackbar.make(coordinatorLayout,"Enable Analytics API to access data",Snackbar.LENGTH_SHORT).show();

                    AlertDialog.Builder builder = new AlertDialog.Builder(WebSitePage.this);
                    // Setting Dialog Message
                    builder.setMessage("Enable Analytics API")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                    Intent i = new Intent(WebSitePage.this, LoginActivity.class);
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

    private void getProfiles() {
        Log.i(TAG, "startService");
        createAuthDialog(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.optionsMenu = menu;
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.website_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
     /*   mSearchMenuItem = menu.findItem(R.id.action_search);
        SearchView searchView  = (SearchView) MenuItemCompat.getActionView(mSearchMenuItem);*/
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
        SearchView.OnQueryTextListener textChangeListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                // this is your adapter that will be filtered
                mAdapter.getFilter().filter(newText);
                System.out.println("on text change text: " + newText);
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                // this is your adapter that will be filtered
//                mAdapter.getFilter().filter(query);
                System.out.println("on query submit: " + query);
                return true;
            }
        };
        searchView.setOnQueryTextListener(textChangeListener);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case R.id.action_search:


        }

        return super.onOptionsItemSelected(item);
    }

   /* @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search
        }
    }*/

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
                Log.i(WebSitePage.TAG, "Cancel Authenticated task!!");
            }
        });

        return authDialog;
    }

    public void dismissDialog() {
        if (authDialog != null) {
            try {
                authDialog.dismiss();
            } catch (Exception e) {
            }
        }
    }
    /*public void authToken(String authToken) {
        Log.d(TAG, "authToken " + authToken);

        if (!isFinishing() && authToken != null) {
            this.account = new google.vmokshagroup.com.analytics_google.Account(this.accountName, authToken);

        }
    }*/

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
                        BaseLoginActivity.profile_name = profilename_obj.getString("$t");
                        BaseLoginActivity.pic_url = profile_obj.getString("$t");

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
