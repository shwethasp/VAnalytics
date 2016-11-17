package google.shwethasp.com.analytics_google.activity;

import android.annotation.TargetApi;
import android.Manifest;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.gson.JsonElement;
import com.squareup.picasso.Picasso;

import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.AuthorizationServiceConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import google.shwethasp.com.analytics_google.BuildConfig;
import google.shwethasp.com.analytics_google.Connectivity;
import google.shwethasp.com.analytics_google.api_call.IGoogleAnalyticsApi;
import google.shwethasp.com.analytics_google.MyAccountManager2;
import google.shwethasp.com.analytics_google.MyAccountManager2.MyAccountManagerListener;
import google.shwethasp.com.analytics_google.R;
import google.shwethasp.com.analytics_google.ScreenControllerV3;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public abstract class BaseLoginActivity extends AppCompatActivity implements OnItemClickListener, MyAccountManagerListener, AdapterView.OnItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {
    public static final String PROFILE_REQUEST = "profile_request";
    private static final String TAG = BaseLoginActivity.class.getCanonicalName();
    private google.shwethasp.com.analytics_google.Account account;
    private MyAccountManager2 accountManager;
    public static String accountName;
    public static Spinner accountsListView;
    private static ProgressDialog authDialog, progress;
    public static String pic_url, profile_name;
    private int mErrorCount = 0;
    private Handler mHandler = new Handler();
    private int mRequestId;
    private View selectAccountHolder;
    String Response;
    TextView accounts_select_account_message, accounts_select_account;
    FloatingActionButton addAccount;
    static String response;
    protected ScreenControllerV3 sc;
    public static String id = "", resultentname = "", resultentid = "", resultentname1 = "";
    public static ArrayList<String> arrayListid = new ArrayList<String>();
    ArrayList<String> webName = new ArrayList<String>();
    ArrayList<String> webType = new ArrayList<String>();
    public static ArrayList arrayListname = new ArrayList();
    ArrayList arrayListname1 = new ArrayList();
    public static HashMap<String, ArrayList<String>> Hashmap = new HashMap<>();
    public static HashMap<String, String> Hashmap1 = new HashMap<>();

    public static ArrayList<String> resnamesubList = new ArrayList<String>();
    public static ArrayList<String> response_timeZoneArray = new ArrayList<String>();
    public static ArrayList<String> webtypetext = new ArrayList<String>();


    private SharedPreferences sharedpreferences;
    public static boolean isFirstlogin = false;
    public static boolean isFirstlogin1 = false, isFirstlogin2 = false;


    static /* synthetic */ int access$304(BaseLoginActivity x0) {
        int i = x0.mErrorCount + 1;
        x0.mErrorCount = i;
        return i;
    }

    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;
    private Snackbar snackbar;
    public static CoordinatorLayout coordinatorLayout;
    // private boolean internetConnected;

    public static int pos;

    ProgressDialog progressDialog;

    //Global variables:
    GoogleSignInOptions gso;
    private GoogleApiClient mGoogleApiClient;
    public static String personName;
    public static final int REQUEST_CODE = 10;
    Editor editor;

    /**
     * Id to identify a getaccount permission request.
     */
    private static final int INITIAL_PERMISSION_REQUESTCODE = 0;

    String[] permissionArray;
    ArrayList<String> stringArrayList;
    int permissionPos = 0;


    @TargetApi(Build.VERSION_CODES.ECLAIR_MR1)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_view1);
      /*  Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(BuildConfig.FLAVOR);*/
        // setSupportActionBar(toolbar);
     /* getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);;
        *//*getSupportActionBar().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);*//*
        getSupportActionBar().setBackgroundDrawable(getDrawable(getColor(R.color.black)));*/

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorlayout);

        sharedpreferences = getApplicationContext().getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE); //1
        editor = sharedpreferences.edit();
        editor.putInt("position", 0);
        editor.putString("selected_calender_pos", "1 Week");
        editor.commit();

        Typeface tf3 = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");

        accounts_select_account_message = (TextView) findViewById(R.id.accounts_select_account_message);
        accounts_select_account_message.setTypeface(tf3);//set fontstyle

        accounts_select_account = (TextView) findViewById(R.id.accounts_select_account);
        accounts_select_account.setTypeface(tf3);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorlayout);

        selectAccountHolder = findViewById(R.id.accounts_select_holder);
        accountManager = new MyAccountManager2(this);
        accountsListView = (Spinner) findViewById(R.id.accounts_list_view);


        //inside onCreate():
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .requestScopes(new Scope(Scopes.PLUS_ME))
                .requestScopes(new Scope(Scopes.PLUS_LOGIN))
                .requestScopes(new Scope(Scopes.PROFILE))
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addApi(Plus.API)
                .build();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //checking run time permission
            Log.i(TAG, "Show GET ACCOUNTS button pressed. Checking permission.");
            // BEGIN_INCLUDE(camera_permission)
            // Check if the Camera permission is already available.
            if (!runTimepermission()) {

                accountsListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        if (position != 0) {
                            // GET ACCOUNTS permissions is already available, show the GET ACCOUNTS preview.
                            Log.i(TAG,
                                    "GET ACCOUNTS permission has already been granted. Displaying getaccount preview.");
                            if (Connectivity.isConnected(BaseLoginActivity.this)) {
                                accountName = String.valueOf(accountsListView.getSelectedItem());
                                pos = accountsListView.getSelectedItemPosition();


                                //accountName = (String) parent.getAdapter().getItem(position);
                                if (!accountManager.startAuthActivity(BaseLoginActivity.this, 12, accountName, BaseLoginActivity.this)) {
                                    Toast.makeText(BaseLoginActivity.this, "Account name error", Toast.LENGTH_SHORT).show();
                                }
                                authDialog = createAuthDialog1(BaseLoginActivity.this);
                                authDialog.show();
                            } else {
                                snackbar.make(coordinatorLayout, "Lost Internet Connection", Snackbar.LENGTH_LONG).show();
                                accountsListView.setSelection(0);

                            }
                            //dialog(please wait)
                 /*   progress=new ProregssDialog(getApplicationContext());
                    progress.setMessage("Please wait...");
                    progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    progress.setIndeterminate(true);
                    progress.setProgress(0);
                    progress.show();*/


                        }
                    }


                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        } else {

            accountsListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    if (position != 0) {
                        // GET ACCOUNTS permissions is already available, show the GET ACCOUNTS preview.
                        Log.i(TAG,
                                "GET ACCOUNTS permission has already been granted. Displaying getaccount preview.");
                        if (Connectivity.isConnected(BaseLoginActivity.this)) {
                            accountName = String.valueOf(accountsListView.getSelectedItem());
                            pos = accountsListView.getSelectedItemPosition();


                            //accountName = (String) parent.getAdapter().getItem(position);
                            if (!accountManager.startAuthActivity(BaseLoginActivity.this, 12, accountName, BaseLoginActivity.this)) {
                                Toast.makeText(BaseLoginActivity.this, "Account name error", Toast.LENGTH_SHORT).show();
                            }
                            authDialog = createAuthDialog1(BaseLoginActivity.this);
                            authDialog.show();
                        } else {
                            snackbar.make(coordinatorLayout, "Lost Internet Connection", Snackbar.LENGTH_LONG).show();
                            accountsListView.setSelection(0);

                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        addAccount = (FloatingActionButton) findViewById(R.id.accounts_add_account1);
        addAccount.setRippleColor(getResources().getColor(R.color.gray));
//        addAccount.setPaintFlags(addAccount.getPaintFlags() | 8);
        addAccount.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                BaseLoginActivity.this.accountManager.addAccount(BaseLoginActivity.this);


            }
        });
        boolean profileRequest = getIntent().getBooleanExtra(PROFILE_REQUEST, false);
      /*  if (!((ScreenControllerV3) getApplication()).isLogin() || profileRequest) {
            this.selectAccountHolder.setVisibility(View.VISIBLE);
        } else {
//            startMainActivity();
        }*/
    }


  /*  public void startMainActivity() {
        startActivity(new Intent(this, MainFragmentActivity.class));
        finish();
    }*/


    /**
     * Requests the Camera permission.
     * If the permission has been denied previously, a SnackBar will prompt the user to grant the
     * permission, otherwise it is requested directly.
     */
    private boolean runTimepermission() {
        /*{}*/
        stringArrayList = new ArrayList<String>();
        permissionArray = new String[]{};
        boolean isPermissionRequired = false;

        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS)
                != PackageManager.PERMISSION_GRANTED)) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(
                        Manifest.permission.GET_ACCOUNTS)) {

                }
            }
            stringArrayList.add(Manifest.permission.GET_ACCOUNTS);
            permissionPos = permissionPos + 1;
            isPermissionRequired = true;
        }

       /* if ((ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) && ((ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED))) {


            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

            }

            stringArrayList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            permissionPos = permissionPos + 1;
            isPermissionRequired = true;
        }
*/

        String[] permissionArray = stringArrayList.toArray(new String[permissionPos]);
        if (isPermissionRequired) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissionArray,
                        INITIAL_PERMISSION_REQUESTCODE);
            }
        }
        return isPermissionRequired;
    }


    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case INITIAL_PERMISSION_REQUESTCODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    accountsListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                            if (position != 0) {
                                // GET ACCOUNTS permissions is already available, show the GET ACCOUNTS preview.
                                Log.i(TAG,
                                        "GET ACCOUNTS permission has already been granted. Displaying getaccount preview.");
                                if (Connectivity.isConnected(BaseLoginActivity.this)) {
                                    accountName = String.valueOf(accountsListView.getSelectedItem());
                                    pos = accountsListView.getSelectedItemPosition();


                                    //accountName = (String) parent.getAdapter().getItem(position);
                                    if (!accountManager.startAuthActivity(BaseLoginActivity.this, 12, accountName, BaseLoginActivity.this)) {
                                        Toast.makeText(BaseLoginActivity.this, "Account name error", Toast.LENGTH_SHORT).show();
                                    }
                                    authDialog = createAuthDialog1(BaseLoginActivity.this);
                                    authDialog.show();
                                } else {
                                    snackbar.make(coordinatorLayout, "Lost Internet Connection", Snackbar.LENGTH_LONG).show();
                                    accountsListView.setSelection(0);

                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                } else {
                    //  Toast.makeText(getApplicationContext(), "permission denied", Toast.LENGTH_SHORT).show();
                    Snackbar.make(coordinatorLayout, "Permission denied", Snackbar.LENGTH_SHORT).show();
                }
                return;
            }

        }
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
                    Log.d("response_photo", response_profile);

                    try {

                        JSONObject profile_response_object = new JSONObject(response_profile);

//                      JSONObject object_pic=new JSONObject("object");

                        JSONObject profile_Jsonobj = profile_response_object.getJSONObject("entry");
                       JSONObject profilename_obj = profile_Jsonobj.getJSONObject("gphoto$nickname");
                        JSONObject profile_obj = profile_Jsonobj.getJSONObject("gphoto$thumbnail");

//                        JSONObject pic_result =new JSONObject(String.valueOf(profile_obj));
                      profile_name = profilename_obj.getString("$t");
                        pic_url = profile_obj.getString("$t");

                        // JSONObject re_json_obj=pic_result.get


                        Log.d("pic_url", pic_url);
                       Log.d("profile_name", profile_name);



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


    @Override
    protected void onResume() {
        super.onResume();
        //  registerInternetCheckReceiver();
        List<String> accounts = new ArrayList();
        accounts.addAll(this.accountManager.getAccountNames());
        Collections.sort(accounts);
        accounts.add(0, "Select Account");
        this.accountsListView.setAdapter(new ArrayAdapter(this, R.layout.simple_row, R.id.simple_row_text, accounts));
        WebSitePage.website_clickedposition = 0;
      /*  editor.putString("Website_name", resnamesubList.get(0));
        editor.putString("Website_type", webtypetext.get(0));
        editor.commit();*/
        // MainDashboard.updated_Position = 0;
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt("position", 0);
        editor.commit();
        logAccounts(accounts);


    }


    @Override
    protected void onPause() {
        super.onPause();
        //  unregisterReceiver(broadcastReceiver);
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
    public void onBackPressed() {
        editor.putInt("position", 0);
        editor.commit();
        super.onBackPressed();
        dismissDialog();
    }
    /* public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_send_feedback) {
            sendFeedback();
        }
        return super.onOptionsItemSelected(item);
    }*/

   /* private void sendFeedback() {
        AnalyticsFeedbackActivity.startFeedback(this, findViewById(R.id.accounts_main_view));
    }*/

    /*  public void onItemClick(AdapterView<?> adapterView, View v, int i, long arg3) {

      }
  */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 12 && resultCode == -1) {
            this.accountManager.getAsyncToken(this.accountName, this);
        } else if (requestCode == 10) {
            dismissDialog();
            if (resultCode == -1) {
                setResult(-1);
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                GoogleSignInAccount acct = result.getSignInAccount();
                personName = acct.getDisplayName();
                String personEmail = acct.getEmail();
                String personId = acct.getId();
                Uri personPhoto = acct.getPhotoUrl();
                finish();
            }
        }
    }


    public void authToken(String authToken) {
        Log.d(TAG, "authToken " + authToken);
        if (authToken != null) {

            if (!isFinishing() && authToken != null) {
                this.account = new google.shwethasp.com.analytics_google.Account(this.accountName, authToken);
                getProfiles();

            }
        } else {
            dismissDialog();
            Snackbar.make(coordinatorLayout, "Check Internet connection", Snackbar.LENGTH_LONG).show();
        }

    }


    private ProgressDialog createAuthDialog(Context context) {
      /*  ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setIndeterminate(false);
        authDialog.setMessage(getResources().getString(R.string.Authenticating));
        progressDialog.setCancelable(false);*/
        authDialog.setMessage(getResources().getString(R.string.Authenticating));
//        MyAccountManager2.restoken = "ya29.CjGGA1X1ZUu6rHgxY0cmYaAt1JjksIyHY6ww5kD8YYZkuVzmGF1nx_Ewl8lEjf_fewr";
        this.getAccounts(MyAccountManager2.restoken);

        //  this.userdetails(MyAccountManager2.restoken);
        authDialog.setOnCancelListener(new OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                cancelSendTask();
            }

            private void cancelSendTask() {
                Log.i(BaseLoginActivity.TAG, "Cancel Authenticated task!!");
            }
        });

        return authDialog;
    }

    private void getProfiles() {
        Log.i(TAG, "startService");

        createAuthDialog(this);

//        this.authDialog.show();

        //  buildStremaApi(getApplicationContext());
       /*Intent i = new Intent(BaseLoginActivity.this,GoogleAnalyticsApi.class);
        startActivity(i);*/


        //  GetProfilesJob requestJob = new GetProfilesJob(this, this.account);
        // this.mRequestId = requestJob.getRequestId();
        // this.sc.getJobManager().addJob(requestJob);
    }




   /* public IGoogleAnalyticsApi buildStremaApi(Context context) {
         OkHttpClient okHttpClient = new OkHttpClient();
         okHttpClient.setReadTimeout(20000, TimeUnit.MILLISECONDS);
         return (IGoogleAnalyticsApi) new RestAdapter.Builder().setConverter(new GsonConverter(new GsonBuilder().create()))
                 .setServer(IGoogleAnalyticsApi.SERVER).setLogLevel(RestAdapter.LogLevel.FULL)
             .setLog((RestAdapter.Log) new AndroidLog("Ganalytics http"))
                 .setClient(new OkClient(okHttpClient)).
                         setRequestInterceptor(new RequestInterceptor() {
                             public void intercept(RequestFacade facade) {
                                 facade.addHeader("Authorization", BaseLoginActivity.this.encodeCredentialsForBasicAuthorization());
                             }
                         }).setErrorHandler(new AnalyticsErrorHandler()).build().create(IGoogleAnalyticsApi.class);
     }
     private String encodeCredentialsForBasicAuthorization() {
         return "Bearer " + (GoogleAnalyticsApi.mAccount != null ? GoogleAnalyticsApi.mAccount.getToken() : BuildConfig.FLAVOR);
     }*/



   /* public void onEvent(final ExceptionResponse response) {
        if (this.mRequestId == response.getRequestId()) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    BaseLoginActivity.this.dismissDialog();
                    ScreenControllerV3.handleError(BaseLoginActivity.this, response.e);
                    if (BaseLoginActivity.access$304(BaseLoginActivity.this) % 3 == 0) {
                        BaseLoginActivity.this.showSupportDialog();
                    }
                }
            });
        }
    }*/

/*
    private void showSupportDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.contact_support_team).setPositiveButton(17039379, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //    BaseLoginActivity.this.sendFeedback();
            }
        }).setNegativeButton(17039360, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        builder.create().show();
    }
*/

    public static void dismissDialog() {
        if (authDialog != null) {
            try {
                authDialog.dismiss();
            } catch (Exception e) {
            }
        }
    }

    /* public void dismissDialog1() {
         if (this.authDialog1 != null) {
             try {
                 this.authDialog1.dismiss();
             } catch (Exception e) {
             }
         }
     }*/
    OkHttpClient okHttpClient = new OkHttpClient();

    private void getAccounts(final String token) {

        getProfilephoto(accountName);


        arrayListname.clear();
        arrayListid.clear();
        Hashmap.clear();
        Hashmap1.clear();
        webtypetext.clear();
        response_timeZoneArray.clear();

        final String BASE_URL = "https://www.googleapis.com";

        /*OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();*/
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
              /*  .client(okHttpClient)*/
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

                Log.e("baselogin response code", response1.code() + "");

                if (response1.code() == 200) {
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
                                    response_timeZoneArray.add(String.valueOf(result_timezon_obj.getString("timezone")));
                                }
                                response_timeZoneArray.toString();
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
                                // MainDashboard.setRefreshActionButtonState(false);
                                Snackbar.make(coordinatorLayout, "Check your internet connection.", Snackbar.LENGTH_LONG).show();
                                accountsListView.setSelection(0);
                            }
                            if (t instanceof SocketTimeoutException) {
                                dismissDialog();
                                //  MainDashboard.setRefreshActionButtonState(false);
                                Snackbar.make(coordinatorLayout, "Check your internet connection.", Snackbar.LENGTH_LONG).show();
                                accountsListView.setSelection(0);
                            }
                            if (t instanceof UnknownHostException) {
                                dismissDialog();
                                //  MainDashboard.setRefreshActionButtonState(false);
                                Snackbar.make(coordinatorLayout, "Check your internet connection.", Snackbar.LENGTH_LONG).show();
                                accountsListView.setSelection(0);
                            }
                            if (t instanceof IOException) {
                                dismissDialog();
                                // MainDashboard.setRefreshActionButtonState(false);
                                Snackbar.make(coordinatorLayout, "Check your internet connection.", Snackbar.LENGTH_LONG).show();
                                accountsListView.setSelection(0);
                            }
                        }
                    });

                    Response = response1.body().toString();
                    Log.d("Result", String.valueOf(Response));


                    try {
                        dismissDialog();
                        Hashmap.clear();
                        Hashmap1.clear();
                        resnamesubList.clear();
                        arrayListid.clear();
                        response_timeZoneArray.clear();
                        JSONObject j = new JSONObject(Response);
                        // String result = j.getString("rows");
                        JSONArray items = j.getJSONArray("items");

                        for (int i = 0; i < items.length(); i++) {

                            // Log.d("out", String.valueOf(rows.get(i)));

                            //   JSONObject obj=new JSONObject(String.valueOf(items.get(i)));


                            JSONObject itemobj = new JSONObject(String.valueOf(items.get(i)));

                            resultentname = itemobj.getString("name");
                            arrayListname.add(resultentname);

                            Log.d("name", String.valueOf(arrayListname));
                            JSONArray webProperties = itemobj.getJSONArray("webProperties");


                            //int len=array.length();
                            // Log.d("Length",String.valueOf(len));
                            //resnamesubList = new ArrayList();
                            ArrayList<String> resnamesubListid = new ArrayList();
                            String SubPropertiesnames, SubPropertiesid;
                            for (int l = 0; l < webProperties.length(); l++) {

                                JSONObject webProperties01 = new JSONObject(String.valueOf(webProperties.get(l)));
                                JSONArray profiles1 = webProperties01.getJSONArray("profiles");
                                resultentname1 = webProperties01.getString("name");
//                                resnamesubList.add(resultentname1);
                                arrayListname1.add(resultentname1);
                                Log.d("name1", String.valueOf(arrayListname1));

                                for (int x = 0; x < profiles1.length(); x++) {
                                    JSONObject profileObject = profiles1.getJSONObject(x);
                                    resultentid = profileObject.getString("id");
                                    resnamesubList.add(resultentname1); //important
                                    webtypetext.add(profileObject.getString("name"));
                                    //webtype.add(profileObject.getString("type"));

                             /*   webName.add(profileObject.getString("name"));
                                webType.add(profileObject.getString("type"));*/

                                    resnamesubListid.add(resultentid);
                                    arrayListid.add(resultentid);

                                }
                                Hashmap1.put(resultentname1, resultentid);
                            }
                            Hashmap.put(resultentname, resnamesubList);

                        }
                    } catch (Exception e) {
                        dismissDialog();
                        Log.d("Exception", String.valueOf(e));

                    }

                    Intent intent = new Intent(BaseLoginActivity.this, MainDashboard.class);
                    intent.putExtra("FirstCall", true);
                    startActivity(intent);
                    overridePendingTransition(R.anim.activity_slide_left_in,
                            R.anim.activity_slide_left_out);
                    //   BaseLoginActivity.this.finish();0

                    //Intent intent = new Intent(BaseLoginActivity.this, MainDashboard.class);
                } else if (response1.code() == 401) {
                    //TODO: Token expired here
                    dismissDialog();
                    Snackbar.make(coordinatorLayout, "Refresh", Snackbar.LENGTH_LONG).show();
                    accountsListView.setSelection(0);
                    if (!accountManager.startAuthActivity(BaseLoginActivity.this, 12, accountName, BaseLoginActivity.this)) {
                        Toast.makeText(BaseLoginActivity.this, "Account name error", Toast.LENGTH_SHORT).show();
                    }

                } else if (response1.code() == 403) {
                    dismissDialog();
                    accountsListView.setSelection(0);
                    Snackbar.make(coordinatorLayout, "Enable Analytics API to access data", Snackbar.LENGTH_SHORT).show();
                } else {

                }


            }

            public void onFailure(Call<JsonElement> call, Throwable throwable) {
                Log.d("rescode", throwable.toString());

                if (throwable instanceof ConnectException) {
                    dismissDialog();
                    MainDashboard.setRefreshActionButtonState(false);
                    Snackbar.make(coordinatorLayout, "Check your internet connection.", Snackbar.LENGTH_LONG).show();
                    accountsListView.setSelection(0);
                }
                if (throwable instanceof UnknownHostException) {
                    dismissDialog();
                    MainDashboard.setRefreshActionButtonState(false);
                    Snackbar.make(coordinatorLayout, "Check your internet connection.", Snackbar.LENGTH_LONG).show();
                    accountsListView.setSelection(0);
                }
                if (throwable instanceof SocketTimeoutException) {
                    dismissDialog();
                    MainDashboard.setRefreshActionButtonState(false);
                    Snackbar.make(coordinatorLayout, "Check your internet connection.", Snackbar.LENGTH_LONG).show();
                    //  snackbar.make(coordinatorLayout, "Socket Time out. Please try again.", Snackbar.LENGTH_LONG).show();
                    accountsListView.setSelection(0);
                }
            }


        });


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
                Log.i(BaseLoginActivity.TAG, "Cancel Authenticated task!!");
            }
        });

        return progressDialog;
    }
/*
    *//**
     * Method to register runtime broadcast receiver to show snackbar alert for internet connection..
     *//*
    private void registerInternetCheckReceiver() {
        IntentFilter internetFilter = new IntentFilter();
        internetFilter.addAction("android.net.wifi.STATE_CHANGE");
        internetFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(broadcastReceiver, internetFilter);
    }

    */

    /**
     * Runtime Broadcast receiver inner class to capture internet connectivity events
     *//*
    public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String status = getConnectivityStatusString(context);
            setSnackbarMessage(status, false);
        }
    };

    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }

    public static String getConnectivityStatusString(Context context) {
        int conn = getConnectivityStatus(context);
        String status = null;
        if (conn == TYPE_WIFI) {
            status = "Wifi enabled";
        } else if (conn == TYPE_MOBILE) {
            status = "Mobile data enabled";
        } else if (conn == TYPE_NOT_CONNECTED) {
            status = "Not connected to Internet";
        }
        return status;
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR_MR1)
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
    }*/

    //to get google account info:
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            String pic_info = null;
            int g;
            String gender = "Null";
            String userid = "";
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
                userid = currentPerson.getId(); //BY THIS CODE YOU CAN GET CURRENT LOGIN USER ID
                g = currentPerson.getGender();
                gender = (g == 1) ? "Female" : (g == 0) ? "Male" : "Others";
            }

            if (acct.getPhotoUrl() != null) {
                pic_info = acct.getPhotoUrl().toString();
                Log.e("info", pic_info + " ");
            }
            Toast.makeText(getApplicationContext(), "welcome " + acct.getDisplayName(), Toast.LENGTH_LONG).show();
            SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("user", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("social_media", "google");
            editor.putString("id", userid);
            editor.putString("email", acct.getEmail());
            editor.putString("name", acct.getDisplayName());
            Log.d("name", acct.getDisplayName());
            editor.putString("profile_pic", pic_info);
            editor.putString("gender", gender);
            editor.apply();
            Intent intent = new Intent(BaseLoginActivity.this, BaseLoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void retrieve_accountimgae() {
        AuthorizationServiceConfiguration serviceConfiguration = new AuthorizationServiceConfiguration(
                Uri.parse("https://accounts.google.com/o/oauth2/v2/auth") /* auth endpoint */,
                Uri.parse("https://www.googleapis.com/oauth2/v4/token") /* token endpoint */
        );
        AuthorizationService authorizationService = new AuthorizationService(getApplicationContext());
        // String clientId = "680172511404-fhdvp12h6h733h2909vonuntfj5ucmso.apps.googleusercontent.com";
        String clientId = "680172511404-fhdvp12h6h733h2909vonuntfj5ucmso.apps.googleusercontent.com";
        Uri redirectUri = Uri.parse("com.google.codelabs.appauth:/oauth2callback");
        AuthorizationRequest.Builder builder = new AuthorizationRequest.Builder(
                serviceConfiguration,
                clientId,
                AuthorizationRequest.RESPONSE_TYPE_CODE,
                redirectUri
        );
        builder.setScopes("profile");

       /* if(mMainActivity.getLoginHint() != null){
            Map loginHintMap = new HashMap<String, String>();
            loginHintMap.put(LOGIN_HINT,mMainActivity.getLoginHint());
            builder.setAdditionalParameters(loginHintMap);

            Log.i(LOG_TAG, String.format("login_hint: %s", mMainActivity.getLoginHint()));
        }
*/
        AuthorizationRequest request = builder.build();
        String action = "com.google.codelabs.appauth.HANDLE_AUTHORIZATION_RESPONSE";
        Intent postAuthorizationIntent = new Intent(action);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), request.hashCode(), postAuthorizationIntent, 0);
        authorizationService.performAuthorizationRequest(request, pendingIntent);
    }
}

