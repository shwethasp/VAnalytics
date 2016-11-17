package google.shwethasp.com.analytics_google.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
//import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarData;
import com.google.gson.JsonElement;
import com.squareup.picasso.Picasso;

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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import google.shwethasp.com.analytics_google.BuildConfig;
import google.shwethasp.com.analytics_google.Connectivity;
//import google.shwethasp.com.analytics_google.interfaces.IGoogleAnalyticsApi;
import google.shwethasp.com.analytics_google.adapter.MainDashboard_CustomSpinner_adapter;
import google.shwethasp.com.analytics_google.MyAccountManager2;
import google.shwethasp.com.analytics_google.adapter.PagerAdapter;
import google.shwethasp.com.analytics_google.R;
import google.shwethasp.com.analytics_google.api_call.IGoogleAnalyticsApi;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainDashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, TabLayout.OnTabSelectedListener, MyAccountManager2.MyAccountManagerListener, AdapterView.OnItemSelectedListener, View.OnClickListener {
    //This is our tablayout
    private TabLayout tabLayout;

    private Snackbar snackbar;
    private CoordinatorLayout coordinatorLayout;
    ProgressDialog progressDialog;
    public static CircleImageView nav_header_image;
    ImageView start_date_picker, end_date_picker;
    private boolean internetConnected = true, daterange_boolean = false, enable_API_boolean = false;
    Boolean countbool = true;
    public static boolean isFirstCall, isFirstCall1;

    private DatePickerDialog fromDatePickerDialog, toDatePickerDialog;
    private static MyAccountManager2 accountManager;
    private static String LOG_TAG = "CardViewActivity";
    Button Start_Date, End_Date, fetchbutton;

    //This is our viewPager
    DateFormat dateFormatter;
    private ViewPager viewPager;
    Dialog mCustomDialog;
    private static Menu optionsMenu;
    TextView website_name, website_typename, nav_header_name;
    //    MainDashboard_CustomSpinner_adapter tooladapter;

    // BarChart chart;
    BarData data;
    ArrayList<Integer> rescountarraycommon = new ArrayList<Integer>();
    private static ProgressDialog authDialog, progress;
    FragmentTransaction transaction;

    public String myMessage;
    private static String accountName;
    private String update_Id, resStardate, resEndate, nav_updated_accountname;
    private static final String TAG = MainDashboard.class.getCanonicalName();
    public static final String PROFILE_REQUEST = "profile_request";
    public static String adpStartDate, adpEndDate, adpId;

    PagerAdapter adapter;
    int updated_Position, website_clickedposition;
    public static int adpMyMessageCount;
    public int myMessageCount;
    Spinner nav_header_spinner, toolbar_spinner;
    private google.shwethasp.com.analytics_google.Account account;
    MainDashboard_CustomSpinner_adapter tooladapter;
    private SharedPreferences sharedpreferences;
    ArrayList arrayListname1 = new ArrayList();
    Editor editor;
    int nav_pos, nav_updated_account_position;


    private static Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maindashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // Create an object for subclass of AsyncTask
        GetXMLTask task = new GetXMLTask();
        // Execute the task
        task.execute(new String[] { BaseLoginActivity.pic_url });


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        showcustomDialog();
        setDateTimeField();

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorlayout);
        toolbar_spinner = (Spinner) findViewById(R.id.toolbar_profiles);
        mActivity = MainDashboard.this;
        Intent intent = getIntent();

        if (intent.getBooleanExtra("FirstCall", false)) {
            isFirstCall = true;
        } else {
            isFirstCall = false;
        }
        if (intent.getBooleanExtra("FirstCall1", false)) {
            isFirstCall1 = true;
        } else {
            isFirstCall1 = false;
        }
    /*    if (!BaseLoginActivity.isFirstlogin) {
            adpMyMessageCount = 0;
            selected_calender = "1 Week";

        }*/

        sharedpreferences = getApplicationContext().getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE); //1
        editor = sharedpreferences.edit();
        toolbar_spinner = (Spinner) findViewById(R.id.toolbar_profiles);
        ArrayList<String> WebsiteName = BaseLoginActivity.resnamesubList;
        ArrayList<String> WebTypeName = BaseLoginActivity.webtypetext;

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        showcustomDialog();
        setDateTimeField();


        // Create custom adapter object ( see below CustomAdapter.java )
        tooladapter = new MainDashboard_CustomSpinner_adapter(getApplicationContext(), WebsiteName, WebTypeName);

        // Set adapter to spinner
        toolbar_spinner.setAdapter(tooladapter);
        if (WebSitePage.website_clickedposition < WebsiteName.size() && WebSitePage.website_clickedposition > WebsiteName.size()) {
            toolbar_spinner.setSelection(0);

        } else if (WebSitePage.website_clickedposition < WebsiteName.size() || WebSitePage.website_clickedposition > WebsiteName.size()) {
            if (BaseLoginActivity.isFirstlogin) {
                toolbar_spinner.setSelection(0);
            } else {
                toolbar_spinner.setSelection(WebSitePage.website_clickedposition);
            }
        } else {
            toolbar_spinner.setSelection(WebSitePage.website_clickedposition);
        }
       /* if(WebSitePage.website_clickedposition !=0)
        {
            TextView website_name = (TextView) findViewById(R.id.websitename);
            TextView website_typename = (TextView) findViewById(R.id.website_type);
            website_name.setTextColor(getResources().getDrawable(R.));
            website_typename.setTextColor(getResources().getColor(Color.WHITE));
        }*/
        toolbar_spinner.setOnItemSelectedListener(this);
        toolbar_spinner.getDropDownWidth();


        NavigationView nav_view = (NavigationView) findViewById(R.id.nav_view);
        nav_header_image = (CircleImageView) nav_view.getHeaderView(0).findViewById(R.id.nav_header_imageView);
        nav_header_name = (TextView) nav_view.getHeaderView(0).findViewById(R.id.nav_header_name_text);
        nav_header_spinner = (Spinner) nav_view.getHeaderView(0).findViewById(R.id.navigation_header_spinner);

        List<String> accounts = new ArrayList();
        accountManager = new MyAccountManager2(this);
        accounts.addAll(this.accountManager.getAccountNames());
        Collections.sort(accounts);
        // accounts.add(0, "Select Account");

        nav_header_spinner.setAdapter(new ArrayAdapter(this, R.layout.nav_header_spinnerrow, R.id.nav_simple_row, accounts));
        nav_header_spinner.setSelection(BaseLoginActivity.pos - 1);

       // nav_header_name.setText(accounts.get(BaseLoginActivity.pos - 1).split("@")[0]);
        nav_header_name.setText(BaseLoginActivity.profile_name);
        boolean profileRequest = getIntent().getBooleanExtra(PROFILE_REQUEST, false);

      /*  if(BaseLoginActivity.accountsListView.getSelectedItemPosition()){

        }*/
        logAccounts(accounts);
       /* Picasso.with(MainDashboard.this)
                .load(BaseLoginActivity.pic_url)
                .placeholder(R.drawable.avatar_placeholder)
                .into(nav_header_image);*/

        nav_header_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //  if (internetConnected)
                if (!BaseLoginActivity.isFirstlogin) {
                    //accountName = (String) parent.getAdapter().getItem(position);
                   /* BaseLoginActivity.resnamesubList.clear();
                    BaseLoginActivity.webtypetext.clear();
                    BaseLoginActivity.arrayListid.clear();
                    BaseLoginActivity.response_timeZoneArray.clear();*/
                    accountName = String.valueOf(nav_header_spinner.getSelectedItem());
                    nav_pos = nav_header_spinner.getSelectedItemPosition();
                    nav_updated_account_position = nav_pos;
                    editor.putInt("position", 0);
                    editor.putString("selected_calender_pos", "1 Week");
                    editor.commit();

                    //accountName = (String) parent.getAdapter().getItem(position);
                    if (!accountManager.startAuthActivity(MainDashboard.this, 12, accountName, MainDashboard.this)) {
                        Toast.makeText(MainDashboard.this, "Account name error", Toast.LENGTH_SHORT).show();
                    }
                    authDialog = createAuthDialog1(MainDashboard.this);
                   // nav_header_name.setText(accountName.split("@")[0]);
                    nav_header_name.setText(BaseLoginActivity.profile_name);
                    authDialog.show();
                    BaseLoginActivity.pos = nav_pos + 1;
                    WebSitePage.clicked_navigation = false;
                    BaseLoginActivity.isFirstlogin = true;

                    WebSitePage.website_clickedposition = 0;
                } else {
                    BaseLoginActivity.isFirstlogin = false;
                }
                // } else {
                //      snackbar.show();
                //  }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




     /* tooladapter = new ArrayAdapter(this,R.layout.simple_list_item_1, R.id.simple_row_text, NameList);
      //  toolbar_spinner.setAdapter(new ArrayAdapter(this, R.layout.simple_list_item_1, NameList));
      toolbar_spinner.setAdapter(tooladapter);*/


        //Initializing the tablayout
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        //Adding the tabs using addTab() method
        tabLayout.addTab(tabLayout.newTab().setText("Reach"));
        tabLayout.addTab(tabLayout.newTab().setText("Visitors"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        //  chart = (BarChart) findViewById(R.id.chart);

        //Initializing viewPager
        viewPager = (ViewPager) findViewById(R.id.pager);

//        Boolean updated_boolean = sharedpreferences.getBoolean("positio_Boolean", countbool);
//
        update_Id = intent.getStringExtra("position");
        updated_Position = sharedpreferences.getInt("position", 0);
        resStardate = sharedpreferences.getString("Sdate", "");
        resEndate = sharedpreferences.getString("Edate", "");
//        MenuItem menuItem=(MenuItem)findViewById(R.id.calendar);

        //Creating our pager adapter
        adpMyMessageCount = updated_Position;
        adpStartDate = resStardate;
        adpEndDate = resEndate;
        adpId = update_Id;
        adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);


        //Adding adapter to pager


        tabLayout.setupWithViewPager(viewPager);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1)

                    tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                        @Override
                        public void onTabSelected(TabLayout.Tab tab) {

                            viewPager.setCurrentItem(tab.getPosition());

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
        tabLayout.setOnTabSelectedListener(this);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
        Call<JsonElement> mService = mInterfaceService.getProfile(BASE_URL +email+"?alt=json");
        //    Call<List> mService = mInterfaceService.getData("Bearer "+token);
        mService.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> profileresponse) {

                Log.e("mprofile response code", profileresponse.code() + "");

                if (profileresponse.code() == 200) {
                    String response_profile = profileresponse.body().toString();
                    Log.d("response_profile", response_profile);

                    try {

                        JSONObject profile_response_object = new JSONObject(response_profile);

//                      JSONObject object_pic=new JSONObject("object");

                        JSONObject profile_Jsonobj=profile_response_object.getJSONObject("entry");
                        JSONObject profilename_obj = profile_Jsonobj.getJSONObject("gphoto$nickname");
                        JSONObject profile_obj = profile_Jsonobj.getJSONObject("gphoto$thumbnail");

//                        JSONObject pic_result =new JSONObject(String.valueOf(profile_obj));
                        BaseLoginActivity.profile_name = profilename_obj.getString("$t");
                        BaseLoginActivity.pic_url=profile_obj.getString("$t");

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

    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.calendar).setChecked(true);
        //Also you can do this for sub menu
        menu.getItem(1).getSubMenu().getItem(updated_Position).setChecked(true);
        return true;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

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

    @Override
    protected void onResume() {

        List<String> accounts = new ArrayList();
        accounts.addAll(this.accountManager.getAccountNames());
        Collections.sort(accounts);
        //  accounts.add(0, "Select Account");
        nav_header_spinner.setAdapter(new ArrayAdapter(this, R.layout.nav_header_spinnerrow, R.id.nav_simple_row, accounts));
        nav_header_spinner.setSelection(BaseLoginActivity.pos - 1);
        logAccounts(accounts);
        BaseLoginActivity.isFirstlogin = true;
        Editor editor = sharedpreferences.edit();
        editor.putInt("position", sharedpreferences.getInt("position", 0));
        editor.commit();
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            //  drawer.closeDrawers();
            drawer.closeDrawer(GravityCompat.START);

        } else {
            // BaseLoginActivity.webtypetext.clear();
            //  BaseLoginActivity.resnamesubList.clear();
            // mBackPressDialog.show();

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            // Setting Dialog Message
            builder.setMessage("Are you sure you want to Logout?")
                    .setCancelable(false)
                    .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            BaseLoginActivity.arrayListid.clear();
                            tooladapter.notifyDataSetChanged();
                            MainDashboard.this.finish();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            // Showing Alert Message
            alert.show();

        }
        //   BaseLoginActivity.webtypetext.clear();
        //   BaseLoginActivity.resnamesubList.clear();

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
    public boolean onCreateOptionsMenu(Menu menu) {
        this.optionsMenu = menu;
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_active, menu);

        return true;
    }


    public boolean onOptionsItemSelected(final android.view.MenuItem item) {


        switch (item.getItemId()) {

            case R.id.oneWeek:
                if (Connectivity.isConnected(MainDashboard.this)) {
                    if (!String.valueOf(item).contentEquals(sharedpreferences.getString("selected_calender_pos", ""))) {
                        countbool = false;
                        myMessage = "1 Week";
                        myMessageCount = 0;
                        item.setChecked(true);
                        Fragment fragment = getSupportFragmentManager().findFragmentByTag("Fragment");
                        editor.putString("selected_calender_pos", String.valueOf(item));
                        editor.commit();
                        if (fragment != null)
                            getSupportFragmentManager().beginTransaction().remove(fragment).commit();

                        // adapter.destroyItem(getSupportFragmentManager(), tabLayout.getTabCount(), Object object);

                        //  adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), myMessageCount, resStardate, resEndate, update_Id);
                        //Adding adapter to pager
                        //  viewPager.setAdapter(adapter);
                        adpMyMessageCount = myMessageCount;
                        adpStartDate = resStardate;
                        adpEndDate = resEndate;
                        adpId = update_Id;

                        adapter.notifyDataSetChanged();
                        viewPager.setCurrentItem(tabLayout.getSelectedTabPosition());

//                    Toast.makeText(getApplicationContext(), "oneWeek", Toast.LENGTH_LONG).show();
                        editor.putInt("position", myMessageCount);
                        editor.commit();
                    } else {
                    }
                } else {
                    Snackbar.make(coordinatorLayout, "Lost Internet Connection", Snackbar.LENGTH_LONG).show();
                }
                break;
            case R.id.oneMonth:
                if (Connectivity.isConnected(MainDashboard.this)) {

                    if (!String.valueOf(item).contentEquals(sharedpreferences.getString("selected_calender_pos", ""))) {
                        countbool = false;
                        item.setChecked(true);
                        // myMessage = "oneMonth";
                        myMessageCount = 1;
                        editor.putString("selected_calender_pos", String.valueOf(item));
                        editor.commit();
                        Fragment fragment = getSupportFragmentManager().findFragmentByTag("Fragment");

                        if (fragment != null)
                            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
//                    adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), myMessageCount, resStardate, resEndate, update_Id);
                        //Adding adapter to pager
//                    viewPager.setAdapter(adapter);
                        adpMyMessageCount = myMessageCount;
                        adpStartDate = resStardate;
                        adpEndDate = resEndate;
                        adpId = update_Id;
                        adapter.notifyDataSetChanged();
                        viewPager.setCurrentItem(tabLayout.getSelectedTabPosition());
                        editor.putInt("position", myMessageCount);
                        editor.commit();
//                    Toast.makeText(getApplicationContext(), "oneMonth", Toast.LENGTH_LONG).show();
                    } else {
                    }
                } else {
                    Snackbar.make(coordinatorLayout, "Lost Internet Connection", Snackbar.LENGTH_LONG).show();
                }
                break;
            case R.id.twoMonth:
                if (Connectivity.isConnected(MainDashboard.this)) {
                    if (!String.valueOf(item).contentEquals(sharedpreferences.getString("selected_calender_pos", ""))) {
                        countbool = false;
                        item.setChecked(true);
                        myMessage = "twoMonth";
                        myMessageCount = 2;
//                    adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), myMessageCount, resStardate, resEndate, update_Id);
                        //Adding adapter to pager
//                    viewPager.setAdapter(adapter);
                        editor.putString("selected_calender_pos", String.valueOf(item));
                        editor.commit();
                        adpMyMessageCount = myMessageCount;
                        adpStartDate = resStardate;
                        adpEndDate = resEndate;
                        adpId = update_Id;
                        adapter.notifyDataSetChanged();
                        viewPager.setCurrentItem(tabLayout.getSelectedTabPosition());
                        editor.putInt("position", myMessageCount);
                        editor.commit();
//                    Toast.makeText(getApplicationContext(), "twoMonth", Toast.LENGTH_LONG).show();
                    } else {

                    }
                } else {
                    Snackbar.make(coordinatorLayout, "Lost Internet Connection", Snackbar.LENGTH_LONG).show();
                }
                break;
            case R.id.threeMonth:
                if (Connectivity.isConnected(MainDashboard.this)) {
                    if (!String.valueOf(item).contentEquals(sharedpreferences.getString("selected_calender_pos", ""))) {
                        countbool = false;
                        item.setChecked(true);
                        myMessage = "threeMonth";
                        myMessageCount = 3;
                        editor.putString("selected_calender_pos", String.valueOf(item));
                        editor.commit();
                        Fragment fragment = getSupportFragmentManager().findFragmentByTag("Fragment");

                        if (fragment != null)
                            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
//                    adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), myMessageCount, resStardate, resEndate, update_Id);
                        //Adding adapter to pager
//                    viewPager.setAdapter(adapter);
                        adpMyMessageCount = myMessageCount;
                        adpStartDate = resStardate;
                        adpEndDate = resEndate;
                        adpId = update_Id;
                        adapter.notifyDataSetChanged();
                        viewPager.setCurrentItem(tabLayout.getSelectedTabPosition());
                        editor.putInt("position", myMessageCount);
                        editor.commit();
//                    Toast.makeText(getApplicationContext(), "threeMonth", Toast.LENGTH_LONG).show();
                    } else {
                    }
                } else {
                    Snackbar.make(coordinatorLayout, "Lost Internet Connection", Snackbar.LENGTH_LONG).show();
                }
                break;
            case R.id.fourMonth:
                if (Connectivity.isConnected(MainDashboard.this)) {
                    if (!String.valueOf(item).contentEquals(sharedpreferences.getString("selected_calender_pos", ""))) {
                        countbool = false;
                        item.setChecked(true);
                        myMessage = "fourMonth";
                        myMessageCount = 4;
                        editor.putString("selected_calender_pos", String.valueOf(item));
                        editor.commit();
                        Fragment fragment = getSupportFragmentManager().findFragmentByTag("Fragment");

                        if (fragment != null)
                            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
//                    adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), myMessageCount, resStardate, resEndate, update_Id);
                        //Adding adapter to pager
//                    viewPager.setAdapter(adapter);
                        adpMyMessageCount = myMessageCount;
                        adpStartDate = resStardate;
                        adpEndDate = resEndate;
                        adpId = update_Id;
                        adapter.notifyDataSetChanged();
                        viewPager.setCurrentItem(tabLayout.getSelectedTabPosition());
                        editor.putInt("position", myMessageCount);
                        editor.commit();
//                    Toast.makeText(getApplicationContext(), "fourMonth", Toast.LENGTH_LONG).show();
                    } else {
                    }
                } else {
                    Snackbar.make(coordinatorLayout, "Lost Internet Connection", Snackbar.LENGTH_LONG).show();
                }
                break;
            case R.id.fiveMonth:
                if (Connectivity.isConnected(MainDashboard.this)) {
                    if (!String.valueOf(item).contentEquals(sharedpreferences.getString("selected_calender_pos", ""))) {
                        countbool = false;
                        item.setChecked(true);
                        myMessage = "fiveMonth";
                        myMessageCount = 5;
                        editor.putString("selected_calender_pos", String.valueOf(item));
                        editor.commit();
                        Fragment fragment = getSupportFragmentManager().findFragmentByTag("Fragment");

                        if (fragment != null)
                            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
//                    adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), myMessageCount, resStardate, resEndate, update_Id);
                        //Adding adapter to pager
//                    viewPager.setAdapter(adapter);
                        adpMyMessageCount = myMessageCount;
                        adpStartDate = resStardate;
                        adpEndDate = resEndate;
                        adpId = update_Id;
                        adapter.notifyDataSetChanged();
                        viewPager.setCurrentItem(tabLayout.getSelectedTabPosition());
                        editor.putInt("position", myMessageCount);
                        editor.commit();
//                    Toast.makeText(getApplicationContext(), "fiveMonth", Toast.LENGTH_LONG).show();
                    } else {
                    }
                } else {
                    Snackbar.make(coordinatorLayout, "Lost Internet Connection", Snackbar.LENGTH_LONG).show();
                }
                break;
            case R.id.sixMonth:
                if (Connectivity.isConnected(MainDashboard.this)) {
                    if (!String.valueOf(item).contentEquals(sharedpreferences.getString("selected_calender_pos", ""))) {
                        countbool = false;
                        item.setChecked(true);
                        myMessage = "sixthMonth";
                        myMessageCount = 6;
                        editor.putString("selected_calender_pos", String.valueOf(item));
                        editor.commit();
                        Fragment fragment = getSupportFragmentManager().findFragmentByTag("Fragment");

                        if (fragment != null)
                            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
//                    adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), myMessageCount, resStardate, resEndate, update_Id);
                        //Adding adapter to pager
//                    viewPager.setAdapter(adapter);
                        adpMyMessageCount = myMessageCount;
                        adpStartDate = resStardate;
                        adpEndDate = resEndate;
                        adpId = update_Id;
                        adapter.notifyDataSetChanged();
                        viewPager.setCurrentItem(tabLayout.getSelectedTabPosition());
                        editor.putInt("position", myMessageCount);
                        editor.commit();
//                    Toast.makeText(getApplicationContext(), "sixthMonth", Toast.LENGTH_LONG).show();
                    } else {

                    }
                } else {
                    Snackbar.make(coordinatorLayout, "Lost Internet Connection", Snackbar.LENGTH_LONG).show();

                }
                break;


            case R.id.custom:
                if (Connectivity.isConnected(MainDashboard.this)) {
                    countbool = false;
                    item.setChecked(true);
                    myMessage = "custom";
                    myMessageCount = 7;
                    mCustomDialog.show();
                   /* adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), myMessageCount, resStardate, resEndate, update_Id);
                    //Adding adapter to pager
                    viewPager.setAdapter(adapter);
                    viewPager.setCurrentItem(tabLayout.getSelectedTabPosition());
                   */

                    editor.putInt("position", myMessageCount);
                    editor.putString("selected_calender_pos", String.valueOf(item));
                    editor.commit();

//                    Toast.makeText(getApplicationContext(), "custom", Toast.LENGTH_LONG).show();

                } else {
                    Snackbar.make(coordinatorLayout, "Lost Internet Connection", Snackbar.LENGTH_LONG).show();
                }
                break;
            case R.id.refresh:
                if (Connectivity.isConnected(MainDashboard.this)) {
                    item.setChecked(true);
                    setRefreshActionButtonState(true);
                    Fragment fragment = getSupportFragmentManager().findFragmentByTag("Fragment");

                    if (fragment != null)
                        getSupportFragmentManager().beginTransaction().remove(fragment).commit();

//                adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), sharedpreferences.getInt("position", 0), resStardate, resEndate, update_Id);
//                viewPager.setAdapter(adapter);
                    adpMyMessageCount = sharedpreferences.getInt("position", 0);
                    adpStartDate = resStardate;
                    adpEndDate = resEndate;
                    adpId = update_Id;
                    adapter.notifyDataSetChanged();
                    viewPager.setCurrentItem(tabLayout.getSelectedTabPosition());
//                Toast.makeText(getApplicationContext(), "refresh", Toast.LENGTH_LONG).show();
                } else {
                    Snackbar.make(coordinatorLayout, "Lost Internet Connection", Snackbar.LENGTH_LONG).show();
                }
                break;
            /*case R.id.menu_right:
                item.setChecked(true);
                setRefreshActionButtonState(false);
                Toast.makeText(getApplicationContext(), "menu_right", Toast.LENGTH_LONG).show();
                break;*/
            default:
                return super.onOptionsItemSelected(item);


        }

        return super.onOptionsItemSelected(item);
    }

    public static void setRefreshActionButtonState(final boolean refreshing) {

        if (optionsMenu != null) {
            final MenuItem refreshItem = optionsMenu
                    .findItem(R.id.refresh);
            if (refreshItem != null) {
                if (refreshing) {
                    refreshItem.setActionView(R.layout.actionbar_indeterminate_progress);
                } else {
                    refreshItem.setActionView(null);
                }
            }
        }
    }

    /*private void showcustomDialog() {
        mCustomDialog = new Dialog(this);
        mCustomDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mCustomDialog.setContentView(R.layout.custom_calender_dialog);
        mCustomDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mCustomDialog.setCancelable(true);

        Start_Date = (Button) mCustomDialog.findViewById(R.id.startdate);
        start_date_picker = (ImageView) mCustomDialog.findViewById(R.id.startdatepicker);
        End_Date = (Button) mCustomDialog.findViewById(R.id.enddate);
        end_date_picker = (ImageView) mCustomDialog.findViewById(R.id.enddatepicker);
        fetchbutton = (Button) mCustomDialog.findViewById(R.id.fetch);


        fetchbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (String.valueOf(Start_Date.getText()).contentEquals("Start_Date") || String.valueOf(End_Date.getText()).contentEquals("End_Date")) {
                    Toast.makeText(getApplicationContext(), "Select Date range", Toast.LENGTH_LONG).show();
                    daterange_boolean = false;
                } else {
                    Log.d("stdate", resStardate);
                    Log.d("Enddate", resEndate);
                    mCustomDialog.dismiss();
                   *//* Intent i = new Intent(MainDashboard.this,ReachFragment.class);
                    i.putExtra("start_date",resStardate);
                    i.putExtra("end_date",resEndate);*//*
                    daterange_boolean = true;
                    if (daterange_boolean == true) {
                        adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), myMessageCount, resStardate, resEndate, update_Id);
                        //Adding adapter to pager
                        viewPager.setAdapter(adapter);
                        viewPager.setCurrentItem(tabLayout.getSelectedTabPosition());
                        daterange_boolean = false;
                        Toast.makeText(getApplicationContext(), "Date range Saved", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Daterange is not updated", Toast.LENGTH_SHORT).show();
                    }


                }
            }
        });

    }*/




/*    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        *//*if (id == R.id.action_settings) {
            return true;
        }*//*

        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.home) {
            // Handle the camera action
        } else if (id == R.id.websites) {
            if (Connectivity.isConnected(MainDashboard.this)) {
                Intent i = new Intent(MainDashboard.this, WebSitePage.class);
                startActivity(i);
                overridePendingTransition(R.anim.activity_slide_left_in,
                        R.anim.activity_slide_left_out);
            } else {
                Snackbar.make(coordinatorLayout, "Check your internet connection", Snackbar.LENGTH_SHORT).show();
            }


            // Handle the camera action
        } else if (id == R.id.aboutus) {
            Intent i = new Intent(MainDashboard.this, About_Us.class);
            startActivity(i);
            overridePendingTransition(R.anim.activity_slide_left_in,
                    R.anim.activity_slide_left_out);


        } else if (id == R.id.signout) {
            if (Connectivity.isConnected(MainDashboard.this)) {
                editor.putInt("position", 0);
                editor.putString("selected_calender_pos", "1 Week");
                editor.commit();
                finish();
                overridePendingTransition(R.anim.activity_slide_left_in,
                        R.anim.activity_slide_left_out);
            } else {
                Snackbar.make(coordinatorLayout, "Check your internet connection", Snackbar.LENGTH_SHORT).show();
            }

           /* Intent i = new Intent(MainDashboard.this,LoginActivity.class);
            startActivity(i);*/

        }/* else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
*/
        item.setCheckable(false);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void authToken(final String authToken) {
        Log.d(TAG, "authToken " + authToken);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isFinishing() && authToken != null) {
                    account = new google.shwethasp.com.analytics_google.Account(accountName, authToken);
                    getProfiles();
                }
            }
        }, 500);

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //TODO: after item checked want to check for boolean value (not yet Created boolean value)
        if (Connectivity.isConnected(MainDashboard.this)) {
            if (!isFirstCall) {

                website_name = (TextView) findViewById(R.id.websitename);
                website_typename = (TextView) findViewById(R.id.website_type);
                LinearLayout linearLayout = (LinearLayout) findViewById(R.id.spinnerItemLayout);
                linearLayout.setBackgroundColor(getResources().getColor(R.color.background_color));
                website_name.setTextColor(getResources().getColor(R.color.white));
                website_typename.setTextColor(getResources().getColor(R.color.white));
                String pos = BaseLoginActivity.resnamesubList.get(position);
                website_clickedposition = position;
                final HashMap<String, String> resultlistid = BaseLoginActivity.Hashmap1;
                WebSitePage.resid = resultlistid.get(pos);
                update_Id = resultlistid.get(pos);
                WebSitePage.website_clickedposition = position;
          /* Intent i = new Intent(MainDashboard.this, MainDashboard.class);

            //  Hashmap.put(BaseLoginActivity.resnamesubList, BaseLoginActivity.resultentid);
            i.putExtra("position", resid);

            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.putExtra("FirstCall", true);
            startActivity(i);*/
      /*   Fragment fragment = getSupportFragmentManager().findFragmentByTag("Fragment");

            if (fragment != null)
                getSupportFragmentManager().beginTransaction().remove(fragment).commit();*/


           /* if (WebSitePage.clicked_navigation) {*/
//            adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), sharedpreferences.getInt("position", 0), resStardate, resEndate, update_Id);
                //Adding adapter to pager
//            viewPager.setAdapter(adapter);
                adpMyMessageCount = sharedpreferences.getInt("position", 0);
                adpStartDate = resStardate;
                adpEndDate = resEndate;
                adpId = update_Id;
                adapter.notifyDataSetChanged();
                viewPager.setCurrentItem(tabLayout.getSelectedTabPosition());
            /*} else {
                adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), myMessageCount, resStardate, resEndate, update_Id);
                //Adding adapter to pager
                viewPager.setAdapter(adapter);
                viewPager.setCurrentItem(tabLayout.getSelectedTabPosition());
            }*/
                WebSitePage.clicked_navigation = false;
//            isFirstCall = true;
            } else {
                isFirstCall = false;
            }
        } else {
            Snackbar.make(coordinatorLayout, "Lost Internet connection", Snackbar.LENGTH_LONG).show();
        }
     /*   if (countbool) {

        } else {
            Log.i(LOG_TAG, " Clicked on Item " + position);
            Toast.makeText(getApplicationContext(), "clicked" + position, Toast.LENGTH_SHORT).show();
            Intent i = new Intent(MainDashboard.this, MainDashboard.class);

            String pos = BaseLoginActivity.resnamesubList.get(position);
            website_clickedposition = position;
            final HashMap<String, String> resultlistid = BaseLoginActivity.Hashmap1;
            resid = resultlistid.get(pos);
            //  Hashmap.put(BaseLoginActivity.resnamesubList, BaseLoginActivity.resultentid);
            i.putExtra("position", resid);

            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            countbool=false;
        }
*/
      /*  Log.i(LOG_TAG, " Clicked on Item " + position);
        Toast.makeText(getApplicationContext(), "clicked" + position, Toast.LENGTH_SHORT).show();
//        String pos = BaseLoginActivity.resnamesubList.get(position);
        website_clickedposition = position;
      *//*  final HashMap<String, String> resultlistid = BaseLoginActivity.Hashmap1;
        resid = resultlistid.get(pos);*//*
        //  Hashmap.put(BaseLoginActivity.resnamesubList, BaseLoginActivity.resultentid);

        adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), website_clickedposition);
        //Adding adapter to pager
        viewPager.setAdapter(adapter);*/


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void setSnackbarMessage(String status, boolean showBar) {
        String internetStatus = "";
        if (status.equalsIgnoreCase("Wifi enabled") || status.equalsIgnoreCase("Mobile data enabled")) {
            internetStatus = "Internet Connected";
            internetConnected = true;
        } else {
            internetStatus = "Lost Internet Connection";
            internetConnected = false;
        }
        snackbar = Snackbar
                .make(coordinatorLayout, internetStatus, Snackbar.LENGTH_LONG)
                .setAction("X", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                    }
                });
        // Changing message text color
        snackbar.setActionTextColor(Color.WHITE);
        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        if (internetStatus.equalsIgnoreCase("Lost Internet Connection")) {
            if (internetConnected) {
                snackbar.show();
            }
        } else {
            if (!internetConnected) {
                snackbar.show();
            }
        }
    }

    /*@Override
    protected void onRestart() {
        Editor editor = sharedpreferences.edit();
        editor.putInt("position", 0);
        editor.commit();
        super.onRestart();
    }*/
    public static void dismissDialog() {
        if (authDialog != null) {
            try {
                authDialog.dismiss();
            } catch (Exception e) {
            }
        }
    }

    private ProgressDialog createAuthDialog1(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setIndeterminate(false);
        progressDialog.setMessage("Please wait");
        progressDialog.setCancelable(false);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                cancelSendTask();
            }

            private void cancelSendTask() {
                Log.i(MainDashboard.TAG, "Cancel Authenticated task!!");
            }
        });

        return progressDialog;
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
                Log.i(MainDashboard.TAG, "Cancel Authenticated task!!");
            }
        });

        return authDialog;
    }

    private void getAccounts(final String token) {

        getProfilephoto(accountName);


        final OkHttpClient okHttpClient = new OkHttpClient();
        final String BASE_URL = "https://www.googleapis.com";

        Retrofit retrofit = new Retrofit.Builder()
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

                Log.e("maind response code", response1.code() + "");

                if (response1.code() == 200) {
                    enable_API_boolean = true;
                    nav_updated_account_position = nav_pos;
                    nav_updated_accountname = accountName;

                    BaseLoginActivity.arrayListname.clear();
                    BaseLoginActivity.arrayListid.clear();
                    BaseLoginActivity.Hashmap.clear();
                    BaseLoginActivity.Hashmap1.clear();
                    BaseLoginActivity.webtypetext.clear();
                    BaseLoginActivity.response_timeZoneArray.clear();
                    BaseLoginActivity.resnamesubList.clear();
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
              /*  .client(okHttpClient)*/
                            .client(okHttpClient.newBuilder().connectTimeout(30, TimeUnit.SECONDS)
                                    .readTimeout(30, TimeUnit.SECONDS).writeTimeout(30,
                                            TimeUnit.SECONDS).build())
                            .build();
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

                    String Response = response1.body().toString();
                    Log.d("Result", String.valueOf(Response));


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

                                arrayListname1.add(BaseLoginActivity.resultentname1);
                                Log.d("name1", String.valueOf(arrayListname1));

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
                    finish();
                    Intent intent = new Intent(MainDashboard.this, MainDashboard.class);
                    intent.putExtra("FirstCall", true);
                    startActivity(intent);
                    overridePendingTransition(R.anim.activity_slide_left_in,
                            R.anim.activity_slide_left_out);
                } else if (response1.code() == 401) {
                    //TODO: Token expired here
                    dismissDialog();
                    MainDashboard.setRefreshActionButtonState(false);
                    //Snackbar.make(coordinatorLayout, "Login again", Snackbar.LENGTH_LONG).show();
                    if (!accountManager.startAuthActivity(MainDashboard.this, 12, accountName, MainDashboard.this)) {
                        Toast.makeText(MainDashboard.this, "Account name error", Toast.LENGTH_SHORT).show();
                    }
                } else if (response1.code() == 403) {
                    dismissDialog();
                    // Snackbar.make(coordinatorLayout,"Enable Analytics API to access data",Snackbar.LENGTH_SHORT).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainDashboard.this);
                    // Setting Dialog Message
                    builder.setMessage("Enable Analytics API")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                    Intent i = new Intent(MainDashboard.this, LoginActivity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(i);
                                    overridePendingTransition(R.anim.activity_slide_left_in,
                                            R.anim.activity_slide_left_out);
                                 /*   if (enable_API_boolean == true) {
                                        nav_header_spinner.setSelection(nav_updated_account_position);
                                        nav_header_name.setText(nav_updated_accountname.split("@")[0]);
                                        enable_API_boolean = false;
                                    } else {
                                        nav_header_spinner.setSelection(BaseLoginActivity.pos-1);
                                        nav_header_name.setText(BaseLoginActivity.accountName.split("@")[0]);
                                    }*/
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

                if (throwable instanceof ConnectException) {
                    dismissDialog();
                    MainDashboard.setRefreshActionButtonState(false);
                    Snackbar.make(coordinatorLayout, "Check your internet connection.", Snackbar.LENGTH_LONG).show();
                }
                if (throwable instanceof SocketTimeoutException) {
                    dismissDialog();
                    MainDashboard.setRefreshActionButtonState(false);
                    Snackbar.make(coordinatorLayout, "Check your internet connection.", Snackbar.LENGTH_LONG).show();
                    //Snackbar.make(coordinatorLayout, "Socket Time out. Please try again.", Snackbar.LENGTH_LONG).show();
                }
                if (throwable instanceof UnknownHostException) {
                    dismissDialog();
                    MainDashboard.setRefreshActionButtonState(false);
                    Snackbar.make(coordinatorLayout, "Check your internet connection.", Snackbar.LENGTH_LONG).show();
                }
            }


        });


    }

    public static void tokengenerate(String name, MyAccountManager2.MyAccountManagerListener listener) {
        if (!accountManager.startAuthActivity(mActivity, 12, name, listener)) {
            Toast.makeText(mActivity, "Account name error", Toast.LENGTH_SHORT).show();
        }
    }

    private void showcustomDialog() {
        mCustomDialog = new Dialog(this);
        mCustomDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mCustomDialog.setContentView(R.layout.custom_calender_dialog);
        mCustomDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mCustomDialog.setCancelable(false);

        Start_Date = (Button) mCustomDialog.findViewById(R.id.startdate);
        start_date_picker = (ImageView) mCustomDialog.findViewById(R.id.startdatepicker);
        End_Date = (Button) mCustomDialog.findViewById(R.id.enddate);
        end_date_picker = (ImageView) mCustomDialog.findViewById(R.id.enddatepicker);
        // enddate_layout = (LinearLayout)mCustomDialog.findViewById(R.id.enddate_layout);
        // startdate_layout = (LinearLayout)mCustomDialog.findViewById(R.id.startdate_layout);
        fetchbutton = (Button) mCustomDialog.findViewById(R.id.fetch);


        fetchbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (String.valueOf(Start_Date.getText()).contentEquals("Start Date") || String.valueOf(End_Date.getText()).contentEquals("End Date")) {
                    Toast.makeText(getApplicationContext(), "Select Date range", Toast.LENGTH_LONG).show();
                    //  Snackbar.make(coordinatorLayout, "Select date range", Snackbar.LENGTH_LONG).show();
                    daterange_boolean = false;
                } else {
                    Log.d("stdate", resStardate);
                    Log.d("Enddate", resEndate);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("Sdate", resStardate);
                    editor.putString("Edate", resEndate);
                    editor.commit();
                    mCustomDialog.dismiss();
                   /* Intent i = new Intent(MainDashboard.this,ReachFragment.class);
                    i.putExtra("start_date",resStardate);
                    i.putExtra("end_date",resEndate);*/
                    daterange_boolean = true;
                    if (daterange_boolean == true) {
//                        adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), myMessageCount, resStardate, resEndate, update_Id);
                        //Adding adapter to pager
//                        viewPager.setAdapter(adapter);
                        adpMyMessageCount = myMessageCount;
                        adpStartDate = resStardate;
                        adpEndDate = resEndate;
                        adpId = update_Id;
                        adapter.notifyDataSetChanged();
                        viewPager.setCurrentItem(tabLayout.getSelectedTabPosition());
                        daterange_boolean = false;
                        //  Snackbar.make(coordinatorLayout, "Daterange not upadted", Snackbar.LENGTH_LONG).show();
                        //   Toast.makeText(getApplicationContext(), "Date range Saved", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Daterange is not updated", Toast.LENGTH_SHORT).show();
                        // Snackbar.make(coordinatorLayout, "Daterange not upadted", Snackbar.LENGTH_LONG).show();
                    }


                }
            }
        });

    }

    public void onClick(View v) {

        if (v == start_date_picker || v == Start_Date) {
            fromDatePickerDialog.show();
        } else if (v == end_date_picker || v == End_Date) {
            toDatePickerDialog.show();
        }

    }


    private void setDateTimeField() {
        start_date_picker.setOnClickListener(this);
        end_date_picker.setOnClickListener(this);
        Start_Date.setOnClickListener(this);
        End_Date.setOnClickListener(this);


        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                // fromDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                resStardate = dateFormatter.format(newDate.getTime());
                String[] startDate = resStardate.split("-");

                Start_Date.setText(startDate[2] + "-" + startDate[1] + "-" + startDate[0]);

                Date Sdate = null;

                Date today = new Date();
                Date EndatefromButtton = null;
                Calendar cal = Calendar.getInstance();

                //   dateFormatter.format(cal.getTime());
                try {
                    Sdate = dateFormatter.parse(resStardate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                try {
                    EndatefromButtton = dateFormatter.parse(resEndate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                try {
                    today = dateFormatter.parse(dateFormatter.format(cal.getTime()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (today.after(Sdate) || today.equals(Sdate)) {
                    if (!String.valueOf(End_Date.getText()).contentEquals("End Date")) {
                        if (EndatefromButtton.after(Sdate)) {

                        } else {


                            if (EndatefromButtton.equals(Sdate)) {

                            } else {
                                Start_Date.setText("Start Date");
                                Toast.makeText(getApplicationContext(), "Start Date must be before End Date", Toast.LENGTH_SHORT).show();

                            }
                        }
                    }

                } else {
                    Start_Date.setText("Start Date");
                    Toast.makeText(getApplicationContext(), "Start Date should be within today", Toast.LENGTH_SHORT).show();
                }

            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        toDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();

                newDate.set(year, monthOfYear, dayOfMonth);
                //  toDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());//disable future dates in calender
                resEndate = dateFormatter.format(newDate.getTime());
                String[] endDate = resEndate.split("-");
                End_Date.setText(endDate[2] + "-" + endDate[1] + "-" + endDate[0]);
                Date Edate = null;

                Date today = new Date();
                Date startdateref = null;
                Calendar cal = Calendar.getInstance();

                try {
                    today = dateFormatter.parse(dateFormatter.format(cal.getTime()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                try {
                    Edate = dateFormatter.parse(resEndate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                try {
                    startdateref = dateFormatter.parse(resStardate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (today.after(Edate) || today.equals(Edate)) {

                    if (startdateref != null && Start_Date.getText() != "Start Date") {
                        if ((startdateref.after(Edate)) && !startdateref.before(Edate)) {
                            End_Date.setText("End Date");
                            Toast.makeText(getApplicationContext(), "EndDate must be after Startdate", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        End_Date.setText("End Date");
                        Toast.makeText(getApplicationContext(), "Select Startdate", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    End_Date.setText("End Date");
                    Toast.makeText(getApplicationContext(), "EndDate should be within today", Toast.LENGTH_SHORT).show();
                }

            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
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
                targetHeight,Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(targetBitmap);
        Path path = new Path();
        path.addCircle(((float) targetWidth ) / 2,
                ((float) targetHeight ) / 2,
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
}

