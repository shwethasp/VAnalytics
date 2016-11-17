package google.shwethasp.com.analytics_google.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;


import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
//import com.github.mikephil.charting.utils.ValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import google.shwethasp.com.analytics_google.api_call.IGoogleAnalyticsApi;
import google.shwethasp.com.analytics_google.MyAccountManager2;
import google.shwethasp.com.analytics_google.R;
import google.shwethasp.com.analytics_google.adapter.Reach_CustomAdapter;
import google.shwethasp.com.analytics_google.activity.WebSitePage;
import google.shwethasp.com.analytics_google.activity.BaseLoginActivity;
import google.shwethasp.com.analytics_google.activity.MainDashboard;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ReachFragment extends Fragment implements MyAccountManager2.MyAccountManagerListener {
    int i, weekcount;
    public static final int ReachSource = 0;
    public static final int ReachStat = 1;
    private ProgressDialog authDialog;
    private static final String TAG = ReachFragment.class.getCanonicalName();
    private RecyclerView mRecyclerView;
    private Reach_CustomAdapter mReachAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String[] mDataset = new String[2];
    private int mDatasetTypes[] = {ReachSource, ReachStat}; //view types
    String Id;
    private SimpleDateFormat dateFormatter;
    BarData data;
    ArrayList<String> resultsites = new ArrayList<String>();
    int resultcount;
    String response, result01, result11, mStartDate, mEndDate, strtext, cal_startdate, cal_enddate;
    String ResSite = "", ResCount = "", ReshasSocialSourceReferral = "";
    String[] rescount = null;
    int myCount = 0, counttext;
    HashMap<String, ArrayList<String>> monthlist = new HashMap<String, ArrayList<String>>();

    ArrayList<String> facebooksessionArray = new ArrayList<String>();
    ArrayList<String> linkedinsessionArray = new ArrayList<String>();
    ArrayList<String> twittersessionArray = new ArrayList<String>();
    ArrayList<String> facebookPageviwesArray = new ArrayList<String>();
    ArrayList<String> facebookAvgSessionArray = new ArrayList<String>();
    ArrayList<String> facebookpageview_persessionArray = new ArrayList<String>();
    ArrayList<String> linkedinPageviwesArray = new ArrayList<String>();
    ArrayList<String> linkedinAvgSessionArray = new ArrayList<String>();
    ArrayList<String> linkedinpageview_persessionArray = new ArrayList<String>();
    ArrayList<String> twitterPageviwesArray = new ArrayList<String>();
    ArrayList<String> twitterAvgSessionArray = new ArrayList<String>();
    ArrayList<String> twitterpageview_persessionArray = new ArrayList<String>();
    ArrayList<String> dyanamicMonthlist = new ArrayList<String>();
    LinearLayout stat_layout;

    private SharedPreferences sharedpreferences;

    PieData piedatas;



    private OnFragmentInteractionListener mListener;




   /* public ReachFragment(int i) {
        // Required empty public constructor
    }
*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reach, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        stat_layout = (LinearLayout) view.findViewById(R.id.stat_layout);


       // mProgressBar.setVisibility(View.VISIBLE);
        sharedpreferences = getActivity().getApplicationContext().getSharedPreferences("mypreference", Context.MODE_PRIVATE);
        //Adapter is created in the last step
        mReachAdapter = new Reach_CustomAdapter(getActivity(), mDataset, mDatasetTypes, data, facebooksessionArray, linkedinsessionArray, twittersessionArray, dyanamicMonthlist, counttext, facebookPageviwesArray, linkedinPageviwesArray,
                twitterPageviwesArray, facebookAvgSessionArray, linkedinAvgSessionArray, twitterAvgSessionArray,
                facebookpageview_persessionArray, linkedinpageview_persessionArray, twitterpageview_persessionArray, piedatas);//change rescount to other
        mRecyclerView.setAdapter(mReachAdapter);

        // chart = (BarChart) view.findViewById(R.id.chart);
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        counttext = getArguments().getInt("monthcount");
        cal_startdate = getArguments().getString("start_date");
        cal_enddate = getArguments().getString("end_date");
        //  Integer calcount = sharedpreferences.getInt("count",0);


        Id = getArguments().getString("ID");
        //  Integer calcount = sharedpreferences.getInt("count",0);


        // strtext = getArguments().getString("oneWeek");

       /* chart.canScrollHorizontally(1);
        chart.setHorizontalScrollBarEnabled(true);*/
//        String websiteid = WebSitePage.resid;
//        String updated_Id;
//        if(!BaseLoginActivity.isFirstlogin){
        if (BaseLoginActivity.isFirstlogin) {
            if (WebSitePage.clicked_navigation) {
                if (Id != null) {
                    callcalenders(counttext, view, Id);
                } else {
                    callcalenders(counttext, view, String.valueOf(BaseLoginActivity.arrayListid.get(0)));
                }
//                callcalenders(counttext, view, Id);
            } else {
                callcalenders(counttext, view, String.valueOf(BaseLoginActivity.arrayListid.get(0)));
                WebSitePage.clicked_navigation=true;
            }
        } else {
            if (Id != null) {
                callcalenders(counttext, view, Id);
            } else {
                callcalenders(counttext, view, String.valueOf(BaseLoginActivity.arrayListid.get(0)));
            }
        }
        /*} else {
            updated_Id = String.valueOf(BaseLoginActivity.arrayListid.get(0));
            callcalenders(counttext, view, updated_Id);
        }*/
     /*   if (websiteid != null) {
//            Integer resultcalender_count = sharedpreferences.getInt("count",0);

        } else {
//            int resultcalender_count = sharedpreferences.getInt("count", 0);

        }*/
        return view;
    }

    public void callcalenders(int counttext, View view, String websiteid) {
        if (counttext == 0) {
            Log.d("week", String.valueOf(counttext));
//            if (Connectivity.isConnected(getActivity())) {
            Log.d("week", String.valueOf(counttext));
            Date date = new Date();
            String today = dateFormatter.format(date);
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -6);
            Date todate1 = cal.getTime();
            String fromdate = dateFormatter.format(todate1);
            String StartDate = fromdate;
            String Enddate = today;
            mDataset[0] = StartDate;
            mDataset[1] = Enddate;
               /* String decesedate1 = Decreasedate(Enddate);
                mStartDate = decesedate1;*/
            this.authDialog = createAuthDialog(getActivity());
            this.authDialog.show();


            Week(MyAccountManager2.restoken, websiteid, StartDate, Enddate);
//
           /* } else {

                Snackbar.make(view, "Lost Internet Connection", Snackbar.LENGTH_LONG).show();
            }*/
        } else if (counttext == 1) {
//            if (Connectivity.isConnected(getActivity())) {
            Log.d("week", String.valueOf(counttext));
            Date date = new Date();
            String today = dateFormatter.format(date);
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, -1);
            Date todate1 = cal.getTime();
            String fromdate = dateFormatter.format(todate1);
            String StartDate = fromdate;
            String Enddate = today;
            mDataset[0] = StartDate;
            mDataset[1] = Enddate;
               /* String decesedate1 = Decreasedate(Enddate);
                mStartDate = decesedate1;*/
            this.authDialog = createAuthDialog(getActivity());
            this.authDialog.show();
            Week(MyAccountManager2.restoken, websiteid, StartDate, Enddate);
            //  CustomorWeek(MyAccountManager2.restoken, String.valueOf(BaseLoginActivity.Hashmap1.get("bootcamplab")), StartDate, Enddate);
          /*  } else {
                Snackbar.make(view, "Lost Internet Connection", Snackbar.LENGTH_LONG).show();
            }*/

        } else if (counttext == 2) {
//            if (Connectivity.isConnected(getActivity())) {
            Date date = new Date();
            String today = dateFormatter.format(date);
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, -2);
            Date todate1 = cal.getTime();
            String fromdate = dateFormatter.format(todate1);
            String StartDate = fromdate;
            String Enddate = today;
            String decesedate1 = Decreasedate(Enddate);
            mStartDate = decesedate1;
            mEndDate = Enddate;
            myCount = 2;
            i = 1;
            this.authDialog = createAuthDialog(getActivity());
            this.authDialog.show();
            mDataset[1] = mEndDate;
            mDataset[0] = StartDate;
            MonthResultResponse(MyAccountManager2.restoken, websiteid, mStartDate, mEndDate, counttext);
          /*  } else {
                Snackbar.make(view, "Lost Internet Connection", Snackbar.LENGTH_LONG).show();
            }*/
        } else if (counttext == 3) {
//            if (Connectivity.isConnected(getActivity())) {
            Date date = new Date();
            String today = dateFormatter.format(date);
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, -3);
            Date todate1 = cal.getTime();
            String fromdate = dateFormatter.format(todate1);
            String StartDate = fromdate;
            String Enddate = today;
            String decesedate1 = Decreasedate(Enddate);
            mStartDate = decesedate1;
            mEndDate = Enddate;
            myCount = 3;
            i = 1;
            this.authDialog = createAuthDialog(getActivity());
            this.authDialog.show();
            mDataset[1] = mEndDate;
            mDataset[0] = StartDate;
            MonthResultResponse(MyAccountManager2.restoken, websiteid, mStartDate, mEndDate, counttext);
          /*  } else {
                Snackbar.make(view, "Lost Internet Connection", Snackbar.LENGTH_LONG).show();
            }*/
        } else if (counttext == 4) {
//            if (Connectivity.isConnected(getActivity())) {
            Date date = new Date();
            String today = dateFormatter.format(date);
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, -4);
            Date todate1 = cal.getTime();
            String fromdate = dateFormatter.format(todate1);
            String StartDate = fromdate;
            String Enddate = today;
            String decesedate1 = Decreasedate(Enddate);
            mStartDate = decesedate1;
            mEndDate = Enddate;
            myCount = 4;
            i = 1;
            this.authDialog = createAuthDialog(getActivity());
            this.authDialog.show();
            mDataset[1] = mEndDate;
            mDataset[0] = StartDate;
            MonthResultResponse(MyAccountManager2.restoken, websiteid, mStartDate, mEndDate, counttext);
           /* } else {
                Snackbar.make(view, "Lost Internet Connection", Snackbar.LENGTH_LONG).show();
            }*/
        } else if (counttext == 5) {
//            if (Connectivity.isConnected(getActivity())) {
            Date date = new Date();
            String today = dateFormatter.format(date);
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, -5);
            Date todate1 = cal.getTime();
            String fromdate = dateFormatter.format(todate1);
            String StartDate = fromdate;
            String Enddate = today;
            String decesedate1 = Decreasedate(Enddate);
            mStartDate = decesedate1;
            mEndDate = Enddate;
            myCount = 5;
            i = 1;
            this.authDialog = createAuthDialog(getActivity());
            this.authDialog.show();
            mDataset[1] = mEndDate;
            mDataset[0] = StartDate;
            MonthResultResponse(MyAccountManager2.restoken, websiteid, mStartDate, mEndDate, counttext);
           /* } else {
                Snackbar.make(view, "Lost Internet Connection", Snackbar.LENGTH_LONG).show();
            }*/
        } else if (counttext == 6) {
//            if (Connectivity.isConnected(getActivity())) {
            Date date = new Date();
            String today = dateFormatter.format(date);
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, -6);
            Date todate1 = cal.getTime();
            String fromdate = dateFormatter.format(todate1);
            String StartDate = fromdate;
            String Enddate = today;
            String decesedate1 = Decreasedate(Enddate);
            mStartDate = decesedate1;
            mEndDate = Enddate;
            myCount = 6;
            i = 1;
            this.authDialog = createAuthDialog(getActivity());
            this.authDialog.show();
            mDataset[1] = mEndDate;
            mDataset[0] = StartDate;
            MonthResultResponse(MyAccountManager2.restoken, websiteid, mStartDate, mEndDate, counttext);
           /* } else {
                Snackbar.make(view, "Lost Internet Connection", Snackbar.LENGTH_LONG).show();
            }*/
        } else if (counttext == 7) {
//            if (Connectivity.isConnected(getActivity())) {

            myCount = 7;
            i = 1;
            this.authDialog = createAuthDialog(getActivity());
            this.authDialog.show();
            mDataset[1] = cal_enddate;
            mDataset[0] = cal_startdate;
            Week(MyAccountManager2.restoken, websiteid, cal_startdate, cal_enddate);
           /* } else {
                Snackbar.make(view, "Lost Internet Connection", Snackbar.LENGTH_LONG).show();
            }*/
        }
    }

    OkHttpClient okHttpClient = new OkHttpClient();

    public String Week(String token, String id, final String sdate, final String edate) {

        String BASE_URL = "https://www.googleapis.com";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient.newBuilder().connectTimeout(30, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS).writeTimeout(30,
                                TimeUnit.SECONDS).build())
                .build();
        IGoogleAnalyticsApi mInterfaceService = retrofit.create(IGoogleAnalyticsApi.class);
        String ids = "ga:" + id;
        Vector<String> metrics = new Vector();
        Vector<String> dimensions = new Vector();
        Vector<String> filters = new Vector();
        metrics.add("ga:sessions,ga:pageviews,ga:avgSessionDuration,ga:pageviewsPerSession");
        dimensions.add("ga:socialNetwork");
        filters.add("ga:socialNetwork==Facebook,ga:socialNetwork==Twitter,ga:socialNetwork==LinkedIn");
        Call<JsonElement> mService = mInterfaceService.getResult("Bearer " + token, ids, sdate, edate, metrics, dimensions, filters);
        mService.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response1) {
                Log.d("reachw_response code", response1.code() + "");
                if (response1.code() == 200) {
                    try {

                        response = response1.body().toString();
                        week_month_custom_responsefilter(response, edate, 1);


                        String[] daterepresentation = sdate.split("-");
                        dyanamicMonthlist.add(daterepresentation[2] + "-" + daterepresentation[1]);
                  /*  i++  
                    if (weekcount > 1) {
                        mEndDate = mStartDate;
                        mStartDate = Decreasedate(mEndDate);
                        weekcount--;
                        Week(MyAccountManager2.restoken, String.valueOf(BaseLoginActivity.Hashmap1.get("bootcamplab")), mStartDate, mEndDate);
                    } else {*/
                        //data = new BarData(getXAxisValues(sdate), setyaxisData_for_Week());
                        // data.setDrawValues(false);

                        // creating data values<br />
                        ArrayList<Entry> entries = new ArrayList<Entry>();
                        entries.add(new Entry(Integer.parseInt(facebooksessionArray.get(0)), 0));
                        entries.add(new Entry(Integer.parseInt(twittersessionArray.get(0)), 1));
                        entries.add(new Entry(Integer.parseInt(linkedinsessionArray.get(0)), 2));
                  /*  entries.add(new Entry(12f, 3));
                    entries.add(new Entry(18f, 4));
                    entries.add(new Entry(9f, 5));*/


                        // creating labels<br />
                        ArrayList<String> labels = new ArrayList<String>();
                        labels.add("Facebook");
                        labels.add("Twitter");
                        labels.add("LinkedIn");


                        PieDataSet dataset = new PieDataSet(entries, "");
                        dataset.setSliceSpace(2f);
                        dataset.setSelectionShift(5f);
                        dataset.setDrawValues(true);

                        dataset.setValueFormatter(new ValueFormatter() {
                            @Override
                            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                                return Math.round(value) + "";
                            }
                        });
                        dataset.setValueTextSize(12f);
/*    dataset.setColor(Color.BLUE);
                    dataset.setColor(Color.YELLOW);
                    dataset.setColor(Color.RED);*/
                        int[] colors = {getResources().getColor(R.color.skyblue), getResources().getColor(R.color.yellow), getResources().getColor(R.color.pink)};
                        dataset.getYValueSum();
                        dataset.setColors(colors);
                        // dataset.setColors(ColorTemplate.COLORFUL_COLORS); // set the color


                        piedatas = new PieData(labels, dataset);
                        // initialize Piedata
                        //   pieChart.setData(data);
                        dismissDialog();
                        MainDashboard.setRefreshActionButtonState(false);
                        //  reachsessions = new int[]{Integer.parseInt(facebooksessionArray.get(0)), Integer.parseInt(linkedinsessionArray.get(0)), Integer.parseInt(twittersessionArray.get(0))};
                        mReachAdapter = new Reach_CustomAdapter(getActivity(), mDataset, mDatasetTypes, data, facebooksessionArray, linkedinsessionArray, twittersessionArray, dyanamicMonthlist, counttext, facebookPageviwesArray, linkedinPageviwesArray,
                                twitterPageviwesArray, facebookAvgSessionArray, linkedinAvgSessionArray, twitterAvgSessionArray,
                                facebookpageview_persessionArray, linkedinpageview_persessionArray, twitterpageview_persessionArray, piedatas);
                        mRecyclerView.setAdapter(mReachAdapter);


                    /*}*/
                    /*data = new BarData(getXAxisValues(sdate, edate), setyaxisData_for_Custonm_Week());
                    data.setDrawValues(false);
                    mVisitorsAdapter = new Visitors_CustomAdapter(getActivity(), mDataset, mDatasetTypes, data, directArray, organucArray, socialArray, referralArray, dyanamicMonthlist, counttext);
                    mRecyclerView.setAdapter(mVisitorsAdapter);
                    dismissDialog();*/

                    } catch (Exception e) {
                        dismissDialog();
                        Log.d("Exception", String.valueOf(e));
                    }

                    Log.d("Result", String.valueOf(response));
                } else if (response1.code() == 401) {
                    //TODO: Token expired here
                    dismissDialog();
                    MainDashboard.setRefreshActionButtonState(false);
                    Snackbar.make(getView(), "Reach unknown error. Refresh and try again", Snackbar.LENGTH_LONG).show();
                    MainDashboard.tokengenerate(BaseLoginActivity.accountName,ReachFragment.this);
                } else {

                }
            }


            @Override
            public void onFailure(Call<JsonElement> call, Throwable throwable) {
                Log.d("rescode", throwable.toString());
                dismissDialog();
                Snackbar.make(getView(), "Check your internet connection.", Snackbar.LENGTH_LONG).show();
                if(throwable instanceof ConnectException) {
                    dismissDialog();
                    MainDashboard.setRefreshActionButtonState(false);
                    Snackbar.make(getView(), "Check your internet connection.", Snackbar.LENGTH_LONG).show();
                }
                if (throwable instanceof SocketTimeoutException) {
                    dismissDialog();
                    MainDashboard.setRefreshActionButtonState(false);
                    Snackbar.make(getView(), "Check your internet connection.", Snackbar.LENGTH_LONG).show();
                }
                if (throwable instanceof UnknownHostException) {
                    dismissDialog();
                    MainDashboard.setRefreshActionButtonState(false);
                    Snackbar.make(getView(), "Check your internet connection.", Snackbar.LENGTH_LONG).show();
                }
            }


        });


        return response;
    }

    private ArrayList<BarDataSet> setyaxisData_for_Week() {
        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();


        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        BarEntry v1e1 = new BarEntry(Float.parseFloat(facebooksessionArray.get(6)), 0); // Jan
        valueSet1.add(v1e1);
        BarEntry v1e11 = new BarEntry(Float.parseFloat(facebooksessionArray.get(5)), 1); // Jan
        valueSet1.add(v1e11);
        BarEntry v1e12 = new BarEntry(Float.parseFloat(facebooksessionArray.get(4)), 2); // Jan
        valueSet1.add(v1e12);
        BarEntry v1e13 = new BarEntry(Float.parseFloat(facebooksessionArray.get(3)), 3); // Jan
        valueSet1.add(v1e13);
        BarEntry v1e14 = new BarEntry(Float.parseFloat(facebooksessionArray.get(2)), 4); // Jan
        valueSet1.add(v1e14);
        BarEntry v1e15 = new BarEntry(Float.parseFloat(facebooksessionArray.get(1)), 5); // Jan
        valueSet1.add(v1e15);
        BarEntry v1e16 = new BarEntry(Float.parseFloat(facebooksessionArray.get(0)), 6); // Jan
        valueSet1.add(v1e16);


        ArrayList<BarEntry> valueSet2 = new ArrayList<>();
        BarEntry v1e2 = new BarEntry(Float.parseFloat(twittersessionArray.get(6)), 0); // Feb
        valueSet2.add(v1e2);
        BarEntry v1e21 = new BarEntry(Float.parseFloat(twittersessionArray.get(5)), 1); // Feb
        valueSet2.add(v1e21);
        BarEntry v1e22 = new BarEntry(Float.parseFloat(twittersessionArray.get(4)), 2); // Feb
        valueSet2.add(v1e22);
        BarEntry v1e23 = new BarEntry(Float.parseFloat(twittersessionArray.get(3)), 3); // Feb
        valueSet2.add(v1e23);
        BarEntry v1e24 = new BarEntry(Float.parseFloat(twittersessionArray.get(2)), 4); // Feb
        valueSet2.add(v1e24);
        BarEntry v1e25 = new BarEntry(Float.parseFloat(twittersessionArray.get(1)), 5); // Feb
        valueSet2.add(v1e25);
        BarEntry v1e26 = new BarEntry(Float.parseFloat(twittersessionArray.get(0)), 6); // Feb
        valueSet2.add(v1e26);

        ArrayList<BarEntry> valueSet3 = new ArrayList<>();
        BarEntry v1e3 = new BarEntry(Float.parseFloat(linkedinsessionArray.get(6)), 0); // Mar
        valueSet3.add(v1e3);
        BarEntry v1e31 = new BarEntry(Float.parseFloat(linkedinsessionArray.get(5)), 1); // Mar
        valueSet3.add(v1e31);
        BarEntry v1e32 = new BarEntry(Float.parseFloat(linkedinsessionArray.get(4)), 2); // Mar
        valueSet3.add(v1e32);
        BarEntry v1e33 = new BarEntry(Float.parseFloat(linkedinsessionArray.get(3)), 3); // Mar
        valueSet3.add(v1e33);
        BarEntry v1e34 = new BarEntry(Float.parseFloat(linkedinsessionArray.get(2)), 4); // Mar
        valueSet3.add(v1e34);
        BarEntry v1e35 = new BarEntry(Float.parseFloat(linkedinsessionArray.get(1)), 5); // Mar
        valueSet3.add(v1e35);
        BarEntry v1e36 = new BarEntry(Float.parseFloat(linkedinsessionArray.get(0)), 6); // Mar
        valueSet3.add(v1e36);

        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Facebook");
        BarDataSet barDataSet2 = new BarDataSet(valueSet2, "Twitter");
        BarDataSet barDataSet3 = new BarDataSet(valueSet3, "LinkedIn");

        barDataSet1.setColor(Color.rgb(81, 182, 231));
        barDataSet2.setColor(Color.rgb(255, 235, 59));
        barDataSet3.setColor(Color.rgb(238, 64, 129));

        dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);
        dataSets.add(barDataSet2);
        dataSets.add(barDataSet3);

        return dataSets;

    }

    public String CustomorWeek(String token, String id, final String sdate, final String edate) {

        String BASE_URL = "https://www.googleapis.com";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient.newBuilder().connectTimeout(30, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS).writeTimeout(30,
                                TimeUnit.SECONDS).build())
                .build();


        IGoogleAnalyticsApi mInterfaceService = retrofit.create(IGoogleAnalyticsApi.class);
        String ids = "ga:" + id;
        Vector<String> metrics = new Vector();
        Vector<String> dimensions = new Vector();
        Vector<String> filters = new Vector();
        metrics.add("ga:sessions");
        dimensions.add("ga:socialNetwork,ga:hasSocialSourceReferral");
        filters.add("ga:socialNetwork==Facebook,ga:socialNetwork==Twitter,ga:socialNetwork==LinkedIn");
        Call<JsonElement> mService = mInterfaceService.getResult("Bearer " + token, ids, sdate, edate, metrics, dimensions, filters);
        mService.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response1) {
                try {
                    response = response1.body().toString();
                    // SEPres(response, edate);
                    /*sites.setText(ResSite);
                    count.setText(ResCount);
                    hasSocialSourceReferral.setText(ReshasSocialSourceReferral);*/

                    data = new BarData(getXAxisValues(sdate, edate), setyaxisData_for_Custonm_Week(sdate, edate));
                    // data = new BarData(setXaxisData_for_MonthWise(sdate), setyaxisData_for_Custonm_Week(sdate, edate));
                    data.setDrawValues(true);
                    data.setValueFormatter(new ValueFormatter() {
                        @Override
                        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                            return Math.round(value) + "";
                        }
                    });
                    mReachAdapter = new Reach_CustomAdapter(getActivity(), mDataset, mDatasetTypes, data, facebooksessionArray, linkedinsessionArray, twittersessionArray, dyanamicMonthlist, counttext, facebookPageviwesArray, linkedinPageviwesArray,
                            twitterPageviwesArray, facebookAvgSessionArray, linkedinAvgSessionArray, twitterAvgSessionArray,
                            facebookpageview_persessionArray, linkedinpageview_persessionArray, twitterpageview_persessionArray, piedatas);
                    mRecyclerView.setAdapter(mReachAdapter);
                    dismissDialog();
//                    chart.setData(data);

                    // dismissDialog();
                } catch (Exception e) {
                    Log.d("Exception", String.valueOf(e));
                }

                Log.d("Result", String.valueOf(response));
            }


            @Override
            public void onFailure(Call<JsonElement> call, Throwable throwable) {
                Log.d("rescode", throwable.toString());
                if(throwable instanceof ConnectException) {
                    dismissDialog();
                    Snackbar.make(getView(), "Check your internet connection.", Snackbar.LENGTH_LONG).show();
                }
            }

        });

        return response;
    }


    public String SEPres1(String result) {

        try {
            JSONObject j = new JSONObject(result);
            JSONObject arrayresult = j.getJSONObject("totalsForAllResults");
            String sessionscount = arrayresult.getString("ga:sessions");


            if (j.has("rows")) {
                JSONArray rows = j.getJSONArray("rows");


                for (int i = 0; i < rows.length(); i++) {

                    JSONArray array = rows.getJSONArray(i);
                    if (i == 0) {
                        if (String.valueOf(array.get(0)).contentEquals("Facebook")) {

                            String resfacebook = String.valueOf(array.get(2));
                            facebooksessionArray.add(resfacebook);
                           /* resultcount.add(0, String.valueOf(array.get(2)));*/


                        } else {
                            String resfacebook = "0";
                            facebooksessionArray.add(resfacebook);
                        }
                    } else if (i == 1) {
                        if (String.valueOf(array.get(0)).contentEquals("LinkedIn")) {
                            String reslinkedin = String.valueOf(array.get(2));
                            linkedinsessionArray.add(reslinkedin);
                            // resultcount.add(1, String.valueOf(array.get(2)));

                        } else {
                            String reslinkedin = "0";
                            linkedinsessionArray.add(reslinkedin);
                        }
                    } else if (i == 2) {
                        if (String.valueOf(array.get(0)).contentEquals("Twitter")) {
                            String restwitter = String.valueOf(array.get(2));
                            twittersessionArray.add(restwitter);
                            // resultcount.add(2, String.valueOf(array.get(2)));


                        } else {
                            String restwitter = "0";
                            twittersessionArray.add(restwitter);
                        }
                    }
                }
                if (rows.length() < 3) {
                    String restwitter = "0";
                    twittersessionArray.add(restwitter);
                }
                if (rows.length() < 2) {
                    String reslinkedin = "0";
                    linkedinsessionArray.add(reslinkedin);
                }
                if (rows.length() < 1) {
                    String resfacebook = "0";
                    facebooksessionArray.add(resfacebook);
                }

            } else {
                facebooksessionArray.add("0");
                linkedinsessionArray.add("0");
                twittersessionArray.add("0");
                monthlist.put("Facebook", facebooksessionArray);
                monthlist.put("LinkedIn", linkedinsessionArray);
                monthlist.put("Twitter", twittersessionArray);
            }
        } catch (Exception e) {

        }
        return ResSite + ResCount + ReshasSocialSourceReferral;
    }

    private ArrayList<String> getXAxisValues(String sdate, String edate) {
        ArrayList<String> xAxis = new ArrayList<>();

        String[] startDatearray = sdate.split("-");
        String[] endDatearray = edate.split("-");

        xAxis.add(startDatearray[2] + "-" + getMonthName(Integer.parseInt(startDatearray[1])).substring(0, 3).toUpperCase() + "-" + startDatearray[0] +
                " - " + endDatearray[2] + "-" + getMonthName(Integer.parseInt(endDatearray[1])).substring(0, 3).toUpperCase() + "-" + endDatearray[0]);

        dyanamicMonthlist.add(startDatearray[2] + "-" + getMonthName(Integer.parseInt(startDatearray[1])).substring(0, 3).toUpperCase() + "-" + startDatearray[0] +
                " - " + endDatearray[2] + "-" + getMonthName(Integer.parseInt(endDatearray[1])).substring(0, 3).toUpperCase() + "-" + endDatearray[0]);
        return xAxis;

    }

    private ArrayList<String> setXaxisData_for_MonthWise(String sdate) {
        ArrayList<String> xAxis = new ArrayList<>();

        int monthnum = Integer.parseInt(sdate.substring(5, 7));
        for (int i = 0; i < counttext; i++) {
            int month = monthnum + 1;
            String monthname = getMonthName(month);
            xAxis.add(monthname.substring(0, 3).toUpperCase());
            monthnum++;
            // dyanamicMonthlist.add(monthname.substring(0, 3).toUpperCase() + "-" + sdate.substring(2, 4));
        }
      /*  xAxis.add("Facebook");
        xAxis.add("LinkedIn");
        xAxis.add("Twitter");*/

        return xAxis;

    }

    String getMonthName(int monthNumber) {
        String[] months = new DateFormatSymbols().getMonths();
        int n = monthNumber - 1;
        return (n >= 0 && n <= 11) ? months[n] : "wrong number";
    }


    private ArrayList<BarDataSet> setyaxisData_for_Custonm_Week(String startDate, String endDate) {
        ArrayList<BarDataSet> dataSets = null;


        ArrayList<BarEntry> valueSet1 = new ArrayList<>();

        BarEntry v1e11 = new BarEntry(Float.parseFloat(facebooksessionArray.get(0)), 0); // Jan
        valueSet1.add(v1e11);

        ArrayList<BarEntry> valueSet2 = new ArrayList<>();
        BarEntry v1e12 = new BarEntry(Float.parseFloat(twittersessionArray.get(0)), 0); // Feb
        valueSet2.add(v1e12);

        ArrayList<BarEntry> valueSet3 = new ArrayList<>();
        BarEntry v1e13 = new BarEntry(Float.parseFloat(linkedinsessionArray.get(0)), 0); // Mar
        valueSet3.add(v1e13);


        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Facebook");
        BarDataSet barDataSet2 = new BarDataSet(valueSet2, "Twitter");
        BarDataSet barDataSet3 = new BarDataSet(valueSet3, "LinkedIn");


        barDataSet1.setColor(Color.rgb(81, 182, 231));
        barDataSet2.setColor(Color.rgb(255, 235, 59));
        barDataSet3.setColor(Color.rgb(238, 64, 129));

        dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);
        dataSets.add(barDataSet2);
        dataSets.add(barDataSet3);

        return dataSets;

    }

    @Override
    public void authToken(String str) {

    }

  /*  // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }*/

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public String MonthResultResponse(String token, final String id, final String sdate, final String edate, final int monthcount) {

        //   this.authDialog.dismiss();
        String BASE_URL = "https://www.googleapis.com";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient.newBuilder().connectTimeout(30, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS).writeTimeout(30,
                                TimeUnit.SECONDS).build())
                .build();


        IGoogleAnalyticsApi mInterfaceService = retrofit.create(IGoogleAnalyticsApi.class);
        String ids = "ga:" + id;
        Vector<String> metrics = new Vector();
        Vector<String> dimensions = new Vector();
        Vector<String> filters = new Vector();
        metrics.add("ga:sessions");
        dimensions.add("ga:socialNetwork,ga:hasSocialSourceReferral");
        filters.add("ga:socialNetwork==Facebook,ga:socialNetwork==Twitter,ga:socialNetwork==LinkedIn");
        Call<JsonElement> mService = mInterfaceService.getResult("Bearer " + token, ids, sdate, edate, metrics, dimensions, filters);
        mService.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response1) {
                Log.e("reachm_response code", response1.code() + "");
                if (response1.code() == 200) {
                    try {
                        String responsemn1 = response1.body().toString();
                        SEPresweek(responsemn1, sdate, i);
                        i++;
                        int monthnum = Integer.parseInt(sdate.substring(5, 7));
                        int month = monthnum + 1;
                        String monthname = getMonthName(month);
                        dyanamicMonthlist.add(monthname.substring(0, 3).toUpperCase() + "-" + sdate.substring(2, 4));
                    /*sites.setText(ResSite);
                    count.setText(ResCount);
                    hasSocialSourceReferral.setText(ReshasSocialSourceReferral);*/
                        if (myCount > 1) {
                            mEndDate = mStartDate;
                            mStartDate = Decreasedate(mEndDate);
                            myCount--;
                            MonthResultResponse(MyAccountManager2.restoken, id, mStartDate, mEndDate, monthcount);
                        } else {
       /*                 monthlist.put("Facebook", facebooksessionArray);
                        monthlist.put("LinkedIn", linkedinsessionArray);
                        monthlist.put("Twitter", twittersessionArray);*/


                            data = new BarData(setXaxisData_for_MonthWise(sdate), getDataSet());
                            data.setDrawValues(false);
                            dismissDialog();
                            MainDashboard.setRefreshActionButtonState(false);
                            mReachAdapter = new Reach_CustomAdapter(getActivity(), mDataset, mDatasetTypes, data, facebooksessionArray, linkedinsessionArray, twittersessionArray, dyanamicMonthlist, counttext, facebookPageviwesArray, linkedinPageviwesArray,
                                    twitterPageviwesArray, facebookAvgSessionArray, linkedinAvgSessionArray, twitterAvgSessionArray,
                                    facebookpageview_persessionArray, linkedinpageview_persessionArray, twitterpageview_persessionArray, piedatas);
                            mRecyclerView.setAdapter(mReachAdapter);

                        }
                    } catch (Exception e) {
                        Log.d("Exception", String.valueOf(e));
                    }


                    Log.d("Result", String.valueOf(response));
                } else if (response1.code() == 401) {
                    //TODO: Token expired here
                    dismissDialog();
                    MainDashboard.setRefreshActionButtonState(false);
                   // Snackbar.make(getView(), "Token Expired", Snackbar.LENGTH_LONG).show();
                    Snackbar.make(getView(), "Reach unknown error. Refresh and try again", Snackbar.LENGTH_LONG).show();
                    MainDashboard.tokengenerate(BaseLoginActivity.accountName,ReachFragment.this);
                } else {

                }

            }


            @Override
            public void onFailure(Call<JsonElement> call, Throwable throwable) {
                Log.d("rescode", throwable.toString());
                if(throwable instanceof ConnectException) {
                    dismissDialog();
                    MainDashboard.setRefreshActionButtonState(false);
                    Snackbar.make(getView(), "Check your internet connection.", Snackbar.LENGTH_LONG).show();
                }
                if (throwable instanceof SocketTimeoutException) {
                    dismissDialog();
                    MainDashboard.setRefreshActionButtonState(false);
                    Snackbar.make(getView(), "Check your internet connection.", Snackbar.LENGTH_LONG).show();
                }
                if (throwable instanceof UnknownHostException) {
                    dismissDialog();
                    MainDashboard.setRefreshActionButtonState(false);
                    Snackbar.make(getView(), "Check your internet connection.", Snackbar.LENGTH_LONG).show();

                }
            }


        });


        return response;
    }

    public String SEPresweek(String result, String Edate, int size) {

        try {
            String strFb = "", strLind = "", strTwitter = "", strStartDate = "", strEndDate = "";
            JSONObject j = new JSONObject(result);
            // String result = j.getString("rows");
            int monthnum = Integer.parseInt(Edate.substring(5, 7));
            int month = monthnum + 1;
            //JSONObject totalsForAllResults =j.get("totalsForAllResults");
            JSONObject arrayresult = j.getJSONObject("totalsForAllResults");
            String sessionscount = arrayresult.getString("ga:sessions");
        /*    if (sessionscount.contentEquals("0")) {
                ResSite = ResSite + "\n" + getMonthName(month) + "\n" + "Facebook" + "\n" + "LinkedIn" + "\n" + "Twitter" + "\n";
                ResCount = ResCount + "\n" + "\n" + "0" + "\n" + "0" + "\n" + "0" + "\n";
                ReshasSocialSourceReferral = ReshasSocialSourceReferral + "\n" + "\n" + "No" + "\n" + "No" + "\n" + "No" + "\n";
                strStartDate = "";
                strEndDate = "";

            } else {*/
               /* String strFb = "", strLind = "", strTwitter = "", strStartDate = "", strEndDate = "";*/


            JSONObject arrayQuery = j.getJSONObject("query");
            if (j.has("rows")) {
                JSONArray rows = j.getJSONArray("rows");

                for (int i = 0; i < rows.length(); i++) {

                    JSONArray array = rows.getJSONArray(i);

                    if (String.valueOf(array.get(0)).contentEquals("Facebook")) {
                        facebooksessionArray.add(String.valueOf(array.get(2)));
                    } else if (String.valueOf(array.get(0)).contentEquals("LinkedIn")) {
                        linkedinsessionArray.add(String.valueOf(array.get(2)));
                    } else if (String.valueOf(array.get(0)).contentEquals("Twitter")) {
                        twittersessionArray.add(String.valueOf(array.get(2)));
                    }
              /*      if (i == 0) {
                        if (String.valueOf(array.get(0)).contentEquals("Facebook")) {

                            String resfacebook = String.valueOf(array.get(2));
                            facebooksessionArray.add(resfacebook);
                           *//* resultcount.add(0, String.valueOf(array.get(2)));*//*


                        } else {
                            String resfacebook = "0";
                            facebooksessionArray.add(resfacebook);
                        }
                    } else if (i == 1) {
                        if (String.valueOf(array.get(0)).contentEquals("LinkedIn")) {
                            String reslinkedin = String.valueOf(array.get(2));
                            linkedinsessionArray.add(reslinkedin);
                            // resultcount.add(1, String.valueOf(array.get(2)));

                        } else {
                            String reslinkedin = "0";
                            linkedinsessionArray.add(reslinkedin);
                        }
                    } else if (i == 2) {
                        if (String.valueOf(array.get(0)).contentEquals("Twitter")) {
                            String restwitter = String.valueOf(array.get(2));
                            twittersessionArray.add(restwitter);
                            // resultcount.add(2, String.valueOf(array.get(2)));


                        } else {
                            String restwitter = "0";
                            twittersessionArray.add(restwitter);
                        }
                    }*/
                }


               /* if (rows.length() < 3) {
                    String restwitter = "0";
                    twittersessionArray.add(restwitter);
                }
                if (rows.length() < 2) {
                    String reslinkedin = "0";
                    linkedinsessionArray.add(reslinkedin);
                }
                if (rows.length() < 1) {
                    String resfacebook = "0";
                    facebooksessionArray.add(resfacebook);
                }*/

                if (facebooksessionArray.size() == size) {
                } else {
                    facebooksessionArray.add("0");
                }
                if (linkedinsessionArray.size() == size) {
                } else {
                    linkedinsessionArray.add("0");
                }
                if (twittersessionArray.size() == size) {
                } else {
                    twittersessionArray.add("0");
                }

            } else {
                facebooksessionArray.add("0");
                linkedinsessionArray.add("0");
                twittersessionArray.add("0");
            }
             /*   monthlist.put("Facebook", facebooksessionArray);
                monthlist.put("LinkedIn", linkedinsessionArray);
                monthlist.put("Twitter", twittersessionArray);
            }*/
        } catch (Exception e) {
            Log.d("Exception", e.getMessage());
        }
        monthlist.toString();
        return null;
    }


    //public String Month(String token, String id, final String sdate, final String edate) {
/*
        String BASE_URL = "https://www.googleapis.com";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        IGoogleAnalyticsApi mInterfaceService = retrofit.create(IGoogleAnalyticsApi.class);
        String ids = "ga:" + id;
        Vector<String> metrics = new Vector();
        Vector<String> dimensions = new Vector();
        Vector<String> filters = new Vector();
        metrics.add("ga:sessions");
        dimensions.add("ga:socialNetwork,ga:hasSocialSourceReferral");
        filters.add("ga:socialNetwork==Facebook,ga:socialNetwork==Twitter,ga:socialNetwork==LinkedIn");
        Call<JsonElement> mService = mInterfaceService.getResult("Bearer " + token, ids, sdate, edate, metrics, dimensions, filters);
        mService.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response1) {
                try {
                    response = response1.body().toString();

                    SEPresweek(response, edate, i);
                    i++;

                    String[] daterepresentation = sdate.split("-");
                    dyanamicMonthlist.add(daterepresentation[2] + "-" + daterepresentation[1]);
                    if (weekcount > 1) {

                        mEndDate = weekincrement(edate);
                        mStartDate = weekDecrement(mEndDate);
                        weekcount--;
                        Month(MyAccountManager2.restoken, String.valueOf(BaseLoginActivity.Hashmap1.get("bootcamplab")), mStartDate, mEndDate);

                    } else {
                        data = new BarData(getXAxisValues(sdate), setyaxisData_for_Month());
                        data.setDrawValues(false);
                        mReachAdapter = new Reach_CustomAdapter(getActivity(), mDataset, mDatasetTypes, data, facebooksessionArray, linkedinsessionArray, twittersessionArray, dyanamicMonthlist, counttext,facebookPageviwesArray, linkedinPageviwesArray,
                                twitterPageviwesArray, facebookAvgSessionArray, linkedinAvgSessionArray, twitterAvgSessionArray,
                                facebookpageview_persessionArray, linkedinpageview_persessionArray, twitterpageview_persessionArray);
                        mRecyclerView.setAdapter(mReachAdapter);
                        dismissDialog();
                    }

                    *//*data = new BarData(getXAxisValues(sdate), setyaxisData_for_Custonm_Week());
                    data.setDrawValues(false);
                    mVisitorsAdapter = new Visitors_CustomAdapter(getActivity(), mDataset, mDatasetTypes, data, directArray, organucArray, socialArray, referralArray, dyanamicMonthlist, counttext);
                    mRecyclerView.setAdapter(mVisitorsAdapter);
                    dismissDialog();*//*

                } catch (Exception e) {
                    Log.d("Exception", String.valueOf(e));
                }

                Log.d("Result", String.valueOf(response));
            }


            @Override
            public void onFailure(Call<JsonElement> call, Throwable throwable) {
                Log.d("rescode", throwable.toString());
            }


        });


        return response;
    }*/

    private ArrayList<String> getXAxisValues(String sdate) {
        ArrayList<String> xAxis = new ArrayList<>();

        for (i = dyanamicMonthlist.size(); i > 0; i--) {
            xAxis.add(dyanamicMonthlist.get(i - 1).substring(0, 3));
        }

        return xAxis;

    }

    private ArrayList<BarDataSet> setyaxisData_for_Month() {
        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();


        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
       /* BarEntry v1e1 = new BarEntry(Float.parseFloat(directArray.get(5)), 0); // Jan
        valueSet1.add(v1e1);*/
        BarEntry v1e11 = new BarEntry(Float.parseFloat(facebooksessionArray.get(4)), 0); // Jan
        valueSet1.add(v1e11);
        BarEntry v1e12 = new BarEntry(Float.parseFloat(facebooksessionArray.get(3)), 1); // Jan
        valueSet1.add(v1e12);
        BarEntry v1e13 = new BarEntry(Float.parseFloat(facebooksessionArray.get(2)), 2); // Jan
        valueSet1.add(v1e13);
        BarEntry v1e14 = new BarEntry(Float.parseFloat(facebooksessionArray.get(1)), 3); // Jan
        valueSet1.add(v1e14);
        BarEntry v1e15 = new BarEntry(Float.parseFloat(facebooksessionArray.get(0)), 4); // Jan
        valueSet1.add(v1e15);


        ArrayList<BarEntry> valueSet2 = new ArrayList<>();
        /*BarEntry v1e2 = new BarEntry(Float.parseFloat(organucArray.get(5)), 0); // Feb
        valueSet2.add(v1e2);*/
        BarEntry v1e21 = new BarEntry(Float.parseFloat(twittersessionArray.get(4)), 0); // Feb
        valueSet2.add(v1e21);
        BarEntry v1e22 = new BarEntry(Float.parseFloat(twittersessionArray.get(3)), 1); // Feb
        valueSet2.add(v1e22);
        BarEntry v1e23 = new BarEntry(Float.parseFloat(twittersessionArray.get(2)), 2); // Feb
        valueSet2.add(v1e23);
        BarEntry v1e24 = new BarEntry(Float.parseFloat(twittersessionArray.get(1)), 3); // Feb
        valueSet2.add(v1e24);
        BarEntry v1e25 = new BarEntry(Float.parseFloat(twittersessionArray.get(0)), 4); // Feb
        valueSet2.add(v1e25);


        ArrayList<BarEntry> valueSet3 = new ArrayList<>();
        /*BarEntry v1e3 = new BarEntry(Float.parseFloat(socialArray.get(5)), 0); // Mar
        valueSet3.add(v1e3);*/
        BarEntry v1e31 = new BarEntry(Float.parseFloat(linkedinsessionArray.get(4)), 0); // Mar
        valueSet3.add(v1e31);
        BarEntry v1e32 = new BarEntry(Float.parseFloat(linkedinsessionArray.get(3)), 1); // Mar
        valueSet3.add(v1e32);
        BarEntry v1e33 = new BarEntry(Float.parseFloat(linkedinsessionArray.get(2)), 2); // Mar
        valueSet3.add(v1e33);
        BarEntry v1e34 = new BarEntry(Float.parseFloat(linkedinsessionArray.get(1)), 3); // Mar
        valueSet3.add(v1e34);
        BarEntry v1e35 = new BarEntry(Float.parseFloat(linkedinsessionArray.get(0)), 4); // Mar
        valueSet3.add(v1e35);


        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Facebook");
        BarDataSet barDataSet2 = new BarDataSet(valueSet2, "Twitter");
        BarDataSet barDataSet3 = new BarDataSet(valueSet3, "LinkedIn");

        barDataSet1.setColor(Color.rgb(81, 182, 231));
        barDataSet2.setColor(Color.rgb(255, 235, 59));
        barDataSet3.setColor(Color.rgb(238, 64, 129));

        dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);
        dataSets.add(barDataSet2);
        dataSets.add(barDataSet3);

        return dataSets;

    }

    private ArrayList<BarDataSet> getDataSet() {
        ArrayList<BarDataSet> dataSets = null;
        if (counttext == 2) {

            ArrayList<BarEntry> valueSet1 = new ArrayList<>();
            BarEntry v1e11 = new BarEntry(Float.parseFloat(facebooksessionArray.get(1)), 0); // Jan
            valueSet1.add(v1e11);
            BarEntry v1e12 = new BarEntry(Float.parseFloat(facebooksessionArray.get(0)), 1); // Feb
            valueSet1.add(v1e12);


            ArrayList<BarEntry> valueSet2 = new ArrayList<>();
            BarEntry v2e22 = new BarEntry(Float.parseFloat(twittersessionArray.get(1)), 0);
            valueSet2.add(v2e22);
            BarEntry v2e23 = new BarEntry(Float.parseFloat(twittersessionArray.get(0)), 1); // Feb
            valueSet2.add(v2e23);


            ArrayList<BarEntry> valueSet3 = new ArrayList<>();
            BarEntry v2e31 = new BarEntry(Float.parseFloat(linkedinsessionArray.get(1)), 0);
            valueSet3.add(v2e31);
            BarEntry v2e32 = new BarEntry(Float.parseFloat(linkedinsessionArray.get(0)), 1); // Feb
            valueSet3.add(v2e32);

         /*   ArrayList<BarEntry> valueSet4 = new ArrayList<>();
            BarEntry v2e41 = new BarEntry(Float.parseFloat(referralArray.get(1)), 0);
            valueSet4.add(v2e41);
            BarEntry v2e42 = new BarEntry(Float.parseFloat(referralArray.get(0)), 1); // Feb
            valueSet4.add(v2e42);
*/
            BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Facebook");
            BarDataSet barDataSet2 = new BarDataSet(valueSet2, "Twitter");
            BarDataSet barDataSet3 = new BarDataSet(valueSet3, "LinkedIn");
            //  BarDataSet barDataSet4 = new BarDataSet(valueSet4, "Referral");

            barDataSet1.setColor(Color.rgb(81, 182, 231));
            barDataSet2.setColor(Color.rgb(255, 235, 59));
            barDataSet3.setColor(Color.rgb(106, 108, 178));
            // barDataSet4.setColor(Color.rgb(238, 64, 129));

            dataSets = new ArrayList<>();
            dataSets.add(barDataSet1);
            dataSets.add(barDataSet2);
            dataSets.add(barDataSet3);
           /* dataSets.add(barDataSet4);*/

        } else if (counttext == 3) {

            ArrayList<BarEntry> valueSet1 = new ArrayList<>();
            BarEntry v1e11 = new BarEntry(Float.parseFloat(facebooksessionArray.get(2)), 0);// Jan
            valueSet1.add(v1e11);
            BarEntry v1e12 = new BarEntry(Float.parseFloat(facebooksessionArray.get(1)), 1); // Feb
            valueSet1.add(v1e12);
            BarEntry v1e13 = new BarEntry(Float.parseFloat(facebooksessionArray.get(0)), 2); // Feb
            valueSet1.add(v1e13);


            ArrayList<BarEntry> valueSet2 = new ArrayList<>();
            BarEntry v2e22 = new BarEntry(Float.parseFloat(twittersessionArray.get(2)), 0);
            valueSet2.add(v2e22);
            BarEntry v2e23 = new BarEntry(Float.parseFloat(twittersessionArray.get(1)), 1); // Feb
            valueSet2.add(v2e23);
            BarEntry v2e24 = new BarEntry(Float.parseFloat(twittersessionArray.get(0)), 2); // Feb
            valueSet2.add(v2e24);

            ArrayList<BarEntry> valueSet3 = new ArrayList<>();
            BarEntry v2e31 = new BarEntry(Float.parseFloat(linkedinsessionArray.get(2)), 0);
            valueSet3.add(v2e31);
            BarEntry v2e32 = new BarEntry(Float.parseFloat(linkedinsessionArray.get(1)), 1); // Feb
            valueSet3.add(v2e32);
            BarEntry v2e33 = new BarEntry(Float.parseFloat(linkedinsessionArray.get(0)), 2); // Feb
            valueSet3.add(v2e33);

            BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Facebook");
            BarDataSet barDataSet2 = new BarDataSet(valueSet2, "Twitter");
            BarDataSet barDataSet3 = new BarDataSet(valueSet3, "LinkedIn");

            barDataSet1.setColor(Color.rgb(81, 182, 231));
            barDataSet2.setColor(Color.rgb(255, 235, 59));
            barDataSet3.setColor(Color.rgb(238, 64, 129));

            dataSets = new ArrayList<>();
            dataSets.add(barDataSet1);
            dataSets.add(barDataSet2);
            dataSets.add(barDataSet3);

        } else if (counttext == 4) {

            ArrayList<BarEntry> valueSet1 = new ArrayList<>();
            BarEntry v1e11 = new BarEntry(Float.parseFloat(facebooksessionArray.get(3)), 0); // Jan
            valueSet1.add(v1e11);
            BarEntry v1e12 = new BarEntry(Float.parseFloat(facebooksessionArray.get(2)), 1); // Feb
            valueSet1.add(v1e12);
            BarEntry v1e13 = new BarEntry(Float.parseFloat(facebooksessionArray.get(1)), 2); // Feb
            valueSet1.add(v1e13);
            BarEntry v1e14 = new BarEntry(Float.parseFloat(facebooksessionArray.get(0)), 3); // Feb
            valueSet1.add(v1e14);


            ArrayList<BarEntry> valueSet2 = new ArrayList<>();
            BarEntry v2e22 = new BarEntry(Float.parseFloat(twittersessionArray.get(3)), 0);
            valueSet2.add(v2e22);
            BarEntry v2e23 = new BarEntry(Float.parseFloat(twittersessionArray.get(2)), 1); // Feb
            valueSet2.add(v2e23);
            BarEntry v2e24 = new BarEntry(Float.parseFloat(twittersessionArray.get(1)), 2); // Feb
            valueSet2.add(v2e24);
            BarEntry v2e25 = new BarEntry(Float.parseFloat(twittersessionArray.get(0)), 3); // Feb
            valueSet2.add(v2e25);

            ArrayList<BarEntry> valueSet3 = new ArrayList<>();
            BarEntry v2e31 = new BarEntry(Float.parseFloat(linkedinsessionArray.get(3)), 0);
            valueSet3.add(v2e31);
            BarEntry v2e32 = new BarEntry(Float.parseFloat(linkedinsessionArray.get(2)), 1); // Feb
            valueSet3.add(v2e32);
            BarEntry v2e33 = new BarEntry(Float.parseFloat(linkedinsessionArray.get(1)), 2); // Feb
            valueSet3.add(v2e33);
            BarEntry v2e34 = new BarEntry(Float.parseFloat(linkedinsessionArray.get(0)), 3); // Feb
            valueSet3.add(v2e34);


            BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Facebook");
            BarDataSet barDataSet2 = new BarDataSet(valueSet2, "Twitter");
            BarDataSet barDataSet3 = new BarDataSet(valueSet3, "LinkedIn");

            barDataSet1.setColor(Color.rgb(81, 182, 231));
            barDataSet2.setColor(Color.rgb(255, 235, 59));
            barDataSet3.setColor(Color.rgb(238, 64, 129));

            dataSets = new ArrayList<>();
            dataSets.add(barDataSet1);
            dataSets.add(barDataSet2);
            dataSets.add(barDataSet3);


        } else if (counttext == 5) {

            ArrayList<BarEntry> valueSet1 = new ArrayList<>();
            BarEntry v1e11 = new BarEntry(Float.parseFloat(facebooksessionArray.get(4)), 0); // Jan
            valueSet1.add(v1e11);
            BarEntry v1e12 = new BarEntry(Float.parseFloat(facebooksessionArray.get(3)), 1); // Feb
            valueSet1.add(v1e12);
            BarEntry v1e13 = new BarEntry(Float.parseFloat(facebooksessionArray.get(2)), 2); // Feb
            valueSet1.add(v1e13);
            BarEntry v1e14 = new BarEntry(Float.parseFloat(facebooksessionArray.get(1)), 3); // Feb
            valueSet1.add(v1e14);
            BarEntry v1e15 = new BarEntry(Float.parseFloat(facebooksessionArray.get(0)), 4); // Feb
            valueSet1.add(v1e15);


            ArrayList<BarEntry> valueSet2 = new ArrayList<>();
            BarEntry v2e22 = new BarEntry(Float.parseFloat(twittersessionArray.get(4)), 0);
            valueSet2.add(v2e22);
            BarEntry v2e23 = new BarEntry(Float.parseFloat(twittersessionArray.get(3)), 1); // Feb
            valueSet2.add(v2e23);
            BarEntry v2e24 = new BarEntry(Float.parseFloat(twittersessionArray.get(2)), 2); // Feb
            valueSet2.add(v2e24);
            BarEntry v2e25 = new BarEntry(Float.parseFloat(twittersessionArray.get(1)), 3); // Feb
            valueSet2.add(v2e25);
            BarEntry v2e26 = new BarEntry(Float.parseFloat(twittersessionArray.get(0)), 4); // Feb
            valueSet2.add(v2e26);

            ArrayList<BarEntry> valueSet3 = new ArrayList<>();
            BarEntry v2e31 = new BarEntry(Float.parseFloat(linkedinsessionArray.get(4)), 0);
            valueSet3.add(v2e31);
            BarEntry v2e32 = new BarEntry(Float.parseFloat(linkedinsessionArray.get(3)), 1); // Feb
            valueSet3.add(v2e32);
            BarEntry v2e33 = new BarEntry(Float.parseFloat(linkedinsessionArray.get(2)), 2); // Feb
            valueSet3.add(v2e33);
            BarEntry v2e34 = new BarEntry(Float.parseFloat(linkedinsessionArray.get(1)), 3); // Feb
            valueSet3.add(v2e34);
            BarEntry v2e35 = new BarEntry(Float.parseFloat(linkedinsessionArray.get(0)), 4); // Feb
            valueSet3.add(v2e35);

            BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Facebook");
            BarDataSet barDataSet2 = new BarDataSet(valueSet2, "Twitter");
            BarDataSet barDataSet3 = new BarDataSet(valueSet3, "LinkedIn");


            barDataSet1.setColor(Color.rgb(81, 182, 231));
            barDataSet2.setColor(Color.rgb(255, 235, 59));
            barDataSet3.setColor(Color.rgb(238, 64, 129));

            dataSets = new ArrayList<>();
            dataSets.add(barDataSet1);
            dataSets.add(barDataSet2);
            dataSets.add(barDataSet3);


        } else if (counttext == 6) {

            ArrayList<BarEntry> valueSet1 = new ArrayList<>();
            BarEntry v1e11 = new BarEntry(Float.parseFloat(facebooksessionArray.get(5)), 0); // Jan
            valueSet1.add(v1e11);
            BarEntry v1e12 = new BarEntry(Float.parseFloat(facebooksessionArray.get(4)), 1); // Feb
            valueSet1.add(v1e12);
            BarEntry v1e13 = new BarEntry(Float.parseFloat(facebooksessionArray.get(3)), 2); // Feb
            valueSet1.add(v1e13);
            BarEntry v1e14 = new BarEntry(Float.parseFloat(facebooksessionArray.get(2)), 3); // Feb
            valueSet1.add(v1e14);
            BarEntry v1e15 = new BarEntry(Float.parseFloat(facebooksessionArray.get(1)), 4); // Feb
            valueSet1.add(v1e15);
            BarEntry v1e16 = new BarEntry(Float.parseFloat(facebooksessionArray.get(0)), 5); // Feb
            valueSet1.add(v1e16);


            ArrayList<BarEntry> valueSet2 = new ArrayList<>();
            BarEntry v2e22 = new BarEntry(Float.parseFloat(twittersessionArray.get(5)), 0);
            valueSet2.add(v2e22);
            BarEntry v2e23 = new BarEntry(Float.parseFloat(twittersessionArray.get(4)), 1); // Feb
            valueSet2.add(v2e23);
            BarEntry v2e24 = new BarEntry(Float.parseFloat(twittersessionArray.get(3)), 2); // Feb
            valueSet2.add(v2e24);
            BarEntry v2e25 = new BarEntry(Float.parseFloat(twittersessionArray.get(2)), 3); // Feb
            valueSet2.add(v2e25);
            BarEntry v2e26 = new BarEntry(Float.parseFloat(twittersessionArray.get(1)), 4); // Feb
            valueSet2.add(v2e26);
            BarEntry v2e27 = new BarEntry(Float.parseFloat(twittersessionArray.get(0)), 5); // Feb
            valueSet2.add(v2e27);

            ArrayList<BarEntry> valueSet3 = new ArrayList<>();
            BarEntry v2e31 = new BarEntry(Float.parseFloat(linkedinsessionArray.get(5)), 0);
            valueSet3.add(v2e31);
            BarEntry v2e32 = new BarEntry(Float.parseFloat(linkedinsessionArray.get(4)), 1); // Feb
            valueSet3.add(v2e32);
            BarEntry v2e33 = new BarEntry(Float.parseFloat(linkedinsessionArray.get(3)), 2); // Feb
            valueSet3.add(v2e33);
            BarEntry v2e34 = new BarEntry(Float.parseFloat(linkedinsessionArray.get(2)), 3); // Feb
            valueSet3.add(v2e34);
            BarEntry v2e35 = new BarEntry(Float.parseFloat(linkedinsessionArray.get(1)), 4); // Feb
            valueSet3.add(v2e35);
            BarEntry v2e36 = new BarEntry(Float.parseFloat(linkedinsessionArray.get(0)), 5); // Feb
            valueSet3.add(v2e36);

            BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Facebook");
            BarDataSet barDataSet2 = new BarDataSet(valueSet2, "Twitter");
            BarDataSet barDataSet3 = new BarDataSet(valueSet3, "LinkedIn");


            barDataSet1.setColor(Color.rgb(81, 182, 231));
            barDataSet2.setColor(Color.rgb(255, 235, 59));
            barDataSet3.setColor(Color.rgb(238, 64, 129));


            dataSets = new ArrayList<>();
            dataSets.add(barDataSet1);
            dataSets.add(barDataSet2);
            dataSets.add(barDataSet3);

        }
        return dataSets;
    }

    public String Decreasedate(String date) {
        Date date1 = null;
        try {
            date1 = dateFormatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String today = dateFormatter.format(date1);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date1);
        cal.add(cal.MONTH, -1);
        Date todate1 = cal.getTime();
        String Decrementedby1 = dateFormatter.format(todate1);


        return Decrementedby1;
    }

    private ProgressDialog createAuthDialog(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setIndeterminate(false);
        progressDialog.setMessage("Please wait");
        progressDialog.setCancelable(false);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                cancelSendTask();
            }

            private void cancelSendTask() {
                Log.i(ReachFragment.TAG, "Cancel Authenticated task!!");
            }
        });

        return progressDialog;
    }

    public void dismissDialog() {
        if (this.authDialog != null) {
            try {
                this.authDialog.dismiss();
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // getActivity().unregisterReceiver(broadcastReceiver);
        //
        // LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(broadcastReceiver);

    }

    @Override
    public void onResume() {
        super.onResume();
        // registerInternetCheckReceiver();
    }


    public String weekincrement(String date) {
        Date date1 = null;
        try {
            date1 = dateFormatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String today = dateFormatter.format(date1);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date1);
        cal.add(cal.DATE, +7);
        Date todate1 = cal.getTime();
        String Decrementedby1 = dateFormatter.format(todate1);


        return Decrementedby1;
    }

    public String weekDecrement(String date) {
        Date date1 = null;
        try {
            date1 = dateFormatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String today = dateFormatter.format(date1);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date1);
        cal.add(cal.DATE, -7);
        Date todate1 = cal.getTime();
        String Decrementedby1 = dateFormatter.format(todate1);


        return Decrementedby1;
    }

    public void week_month_custom_responsefilter(String result, String Edate, int size) {

        try {
            JSONObject j = new JSONObject(result);
            // String result = j.getString("rows");
            int monthnum = Integer.parseInt(Edate.substring(5, 7));
            int month = monthnum + 1;
            //JSONObject totalsForAllResults =j.get("totalsForAllResults");
            JSONObject arrayresult = j.getJSONObject("totalsForAllResults");
            String sessionscount = arrayresult.getString("ga:sessions");
        /*    if (sessionscount.contentEquals("0")) {
                ResSite = ResSite + "\n" + getMonthName(month) + "\n" + "Facebook" + "\n" + "LinkedIn" + "\n" + "Twitter" + "\n";
                ResCount = ResCount + "\n" + "\n" + "0" + "\n" + "0" + "\n" + "0" + "\n";
                ReshasSocialSourceReferral = ReshasSocialSourceReferral + "\n" + "\n" + "No" + "\n" + "No" + "\n" + "No" + "\n";
                strStartDate = "";
                strEndDate = "";

            } else {*/
               /* String strFb = "", strLind = "", strTwitter = "", strStartDate = "", strEndDate = "";*/


            JSONObject arrayQuery = j.getJSONObject("query");
            if (j.has("rows")) {
                JSONArray rows = j.getJSONArray("rows");

                for (int i = 0; i < rows.length(); i++) {

                    JSONArray array = rows.getJSONArray(i);

                    if (String.valueOf(array.get(0)).contentEquals("Facebook")) {
                        facebooksessionArray.add(String.valueOf(array.get(1)));
                        facebookPageviwesArray.add(String.valueOf(array.get(2)));
                        facebookAvgSessionArray.add(String.valueOf(array.get(3)));
                        facebookpageview_persessionArray.add(String.valueOf(array.get(4)));
                    } else if (String.valueOf(array.get(0)).contentEquals("LinkedIn")) {
                        linkedinsessionArray.add(String.valueOf(array.get(1)));
                        linkedinPageviwesArray.add(String.valueOf(array.get(2)));
                        linkedinAvgSessionArray.add(String.valueOf(array.get(3)));
                        linkedinpageview_persessionArray.add(String.valueOf(array.get(4)));
                    } else if (String.valueOf(array.get(0)).contentEquals("Twitter")) {
                        twittersessionArray.add(String.valueOf(array.get(1)));
                        twitterPageviwesArray.add(String.valueOf(array.get(2)));
                        twitterAvgSessionArray.add(String.valueOf(array.get(3)));
                        twitterpageview_persessionArray.add(String.valueOf(array.get(4)));
                    }
              /*      if (i == 0) {
                        if (String.valueOf(array.get(0)).contentEquals("Facebook")) {

                            String resfacebook = String.valueOf(array.get(2));
                            facebooksessionArray.add(resfacebook);
                           *//* resultcount.add(0, String.valueOf(array.get(2)));*//*


                        } else {
                            String resfacebook = "0";
                            facebooksessionArray.add(resfacebook);
                        }
                    } else if (i == 1) {
                        if (String.valueOf(array.get(0)).contentEquals("LinkedIn")) {
                            String reslinkedin = String.valueOf(array.get(2));
                            linkedinsessionArray.add(reslinkedin);
                            // resultcount.add(1, String.valueOf(array.get(2)));

                        } else {
                            String reslinkedin = "0";
                            linkedinsessionArray.add(reslinkedin);
                        }
                    } else if (i == 2) {
                        if (String.valueOf(array.get(0)).contentEquals("Twitter")) {
                            String restwitter = String.valueOf(array.get(2));
                            twittersessionArray.add(restwitter);
                            // resultcount.add(2, String.valueOf(array.get(2)));


                        } else {
                            String restwitter = "0";
                            twittersessionArray.add(restwitter);
                        }
                    }*/
                }


               /* if (rows.length() < 3) {
                    String restwitter = "0";
                    twittersessionArray.add(restwitter);
                }
                if (rows.length() < 2) {
                    String reslinkedin = "0";
                    linkedinsessionArray.add(reslinkedin);
                }
                if (rows.length() < 1) {
                    String resfacebook = "0";
                    facebooksessionArray.add(resfacebook);
                }*/

                if (facebooksessionArray.size() == size) {
                } else {
                    facebooksessionArray.add("0");
                }
                if (linkedinsessionArray.size() == size) {
                } else {
                    linkedinsessionArray.add("0");
                }
                if (twittersessionArray.size() == size) {
                } else {
                    twittersessionArray.add("0");
                }

                if (facebookPageviwesArray.size() == size) {
                } else {
                    facebookPageviwesArray.add("0");
                }
                if (linkedinPageviwesArray.size() == size) {
                } else {
                    linkedinPageviwesArray.add("0");
                }
                if (twitterPageviwesArray.size() == size) {
                } else {
                    twitterPageviwesArray.add("0");
                }

                if (facebookAvgSessionArray.size() == size) {
                } else {
                    facebookAvgSessionArray.add("0");
                }
                if (linkedinAvgSessionArray.size() == size) {
                } else {
                    linkedinAvgSessionArray.add("0");
                }
                if (twitterAvgSessionArray.size() == size) {
                } else {
                    twitterAvgSessionArray.add("0");
                }

                if (facebookpageview_persessionArray.size() == size) {
                } else {
                    facebookpageview_persessionArray.add("0");
                }
                if (linkedinpageview_persessionArray.size() == size) {
                } else {
                    linkedinpageview_persessionArray.add("0");
                }
                if (twitterpageview_persessionArray.size() == size) {
                } else {
                    twitterpageview_persessionArray.add("0");
                }


            } else {
                facebooksessionArray.add("0");
                facebookpageview_persessionArray.add("0");
                facebookAvgSessionArray.add("0");
                facebookPageviwesArray.add("0");

                linkedinsessionArray.add("0");
                linkedinpageview_persessionArray.add("0");
                linkedinAvgSessionArray.add("0");
                linkedinPageviwesArray.add("0");

                twitterPageviwesArray.add("0");
                twitterAvgSessionArray.add("0");
                twitterpageview_persessionArray.add("0");
                twittersessionArray.add("0");

            }
             /*   monthlist.put("Facebook", facebooksessionArray);
                monthlist.put("LinkedIn", linkedinsessionArray);
                monthlist.put("Twitter", twittersessionArray);
            }*/
        } catch (Exception e) {
            Log.d("Exception", e.getMessage());
        }
        monthlist.toString();

    }
/*
    public class MyValueFormatter implements ValueFormatter {

        @Override
        public String getFormattedValue(float value) {
            //return Math.round(value) + "";
//            int a = Math.round(value);
            return Math.round(value) + "";


        }
    }

    public class PieValueFormatter implements ValueFormatter {

        @Override
        public String getFormattedValue(float value) {

            return Math.abs(value / 100) + "%";
            // return  Math.round(value/100)+"";
        }
    }*/
}
