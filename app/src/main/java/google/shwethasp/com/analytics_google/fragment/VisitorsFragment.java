package google.shwethasp.com.analytics_google.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

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
import google.shwethasp.com.analytics_google.adapter.Visitors_CustomAdapter;
import google.shwethasp.com.analytics_google.activity.WebSitePage;
import google.shwethasp.com.analytics_google.activity.BaseLoginActivity;
import google.shwethasp.com.analytics_google.activity.MainDashboard;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VisitorsFragment extends Fragment implements MyAccountManager2.MyAccountManagerListener {
    public static final int VisitorSource = 0;
    public static final int VisitorsStat = 1;
    private RecyclerView mRecyclerView;
    private ProgressDialog authDialog;
    private static final String TAG = VisitorsFragment.class.getCanonicalName();
    private Visitors_CustomAdapter mVisitorsAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String[] mDataset = new String[2];
    private int mDatasetTypes[] = {VisitorSource, VisitorsStat}; //view types
    private SimpleDateFormat dateFormatter;
    BarData data;
    String response, mStartDate, mEndDate, cal_startdate, cal_enddate, Id;
    int myCount = 0, counttext;
    int i, weekcount;
    ArrayList<String> organucArray = new ArrayList<String>();
    ArrayList<String> directArray = new ArrayList<String>();
    ArrayList<String> socialArray = new ArrayList<String>();
    ArrayList<String> referralArray = new ArrayList<String>();

    ArrayList<String> directPageviewsArray = new ArrayList<String>();
    ArrayList<String> directBouncerateArray = new ArrayList<String>();

    ArrayList<String> oraganicPageviewsArray = new ArrayList<String>();
    ArrayList<String> organicBouncerateArray = new ArrayList<String>();

    ArrayList<String> socialPageviewsArray = new ArrayList<String>();
    ArrayList<String> socialBouncerateArray = new ArrayList<String>();

    ArrayList<String> referralPageviewsArray = new ArrayList<String>();
    ArrayList<String> referralBouncerateArray = new ArrayList<String>();

    ArrayList<String> dyanamicMonthlist = new ArrayList<String>();
    LinearLayout stat_layout;
    private OnFragmentInteractionListener mListener;
    int[] reachsessions;
    PieData piedatas;

    ProgressBar mProgressBar;


   /* public ReachFragment() {
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
        View view = inflater.inflate(R.layout.fragment_visitors, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewvisitor);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        stat_layout = (LinearLayout) view.findViewById(R.id.stat_layout_Visitors);

        mProgressBar = (ProgressBar) view.findViewById(R.id.visitor_progressbar);
//        mProgressBar.setScaleY(10f);
       // mProgressBar.setVisibility(View.VISIBLE);

        HashMap<String, String> CustomorWeek = new HashMap<String, String>();
        //Adapter is created in the last step

        Id = getArguments().getString("ID");

        mVisitorsAdapter = new Visitors_CustomAdapter(getActivity(), mDataset, mDatasetTypes, data, directArray, organucArray, socialArray, referralArray, dyanamicMonthlist,
                counttext, reachsessions, directPageviewsArray, directBouncerateArray,
                oraganicPageviewsArray, organicBouncerateArray, socialPageviewsArray, socialBouncerateArray, referralPageviewsArray, referralBouncerateArray, piedatas);
        mRecyclerView.setAdapter(mVisitorsAdapter);

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        counttext = getArguments().getInt("monthcount");
        cal_startdate = getArguments().getString("start_date");
        cal_enddate = getArguments().getString("end_date");

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


        return view;

    }

    public void callcalenders(int counttext, View view, String websiteid) {

        if (counttext == 0) {
            Log.d("week", String.valueOf(counttext));
//            if (Connectivity.isConnected(getActivity())) {
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
            String decesedate1 = Decreasedate(Enddate);
            mStartDate = decesedate1;
            this.authDialog = createAuthDialog(getActivity());
            this.authDialog.show();
            i = 1;
            weekcount = 7;
            Week(MyAccountManager2.restoken, websiteid, StartDate, Enddate);
          /*  } else {
                Snackbar.make(view, "Lost Internet Connection", Snackbar.LENGTH_LONG).show();
            }*/

        } else if (counttext == 1) {
            Log.d("one month", String.valueOf(counttext));
//            if (Connectivity.isConnected(getActivity())) {
            Date date = new Date();
            String today = dateFormatter.format(date);
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, -1);
            Date todate1 = cal.getTime();
            String fromdate = dateFormatter.format(todate1);
            String StartDate = fromdate;
            String Enddate = today;
            Calendar cal1 = Calendar.getInstance();
            cal1.add(Calendar.MONTH, -1);
            Date udptdStartdate = cal1.getTime();
            String updated_Stratdate = dateFormatter.format(udptdStartdate);
            mDataset[0] = StartDate;
            mDataset[1] = Enddate;
            this.authDialog = createAuthDialog(getActivity());
            this.authDialog.show();
            // Week(MyAccountManager2.restoken, websiteid, updated_Stratdate, Increaseeweek(updated_Stratdate));
            Week(MyAccountManager2.restoken, websiteid, StartDate, Enddate);
//            } else {
//                Snackbar.make(view, "Lost Internet Connection", Snackbar.LENGTH_LONG).show();
//            }
        } else if (counttext == 2) {
//            if (Connectivity.isConnected(getActivity())) {
               /* Date date = new Date();
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
                mDataset[0] = StartDate;*/

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
            //   if (Connectivity.isConnected(getActivity())) {
            Date date = new Date();
            String today = dateFormatter.format(date);
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, -3);
            Date todate1 = cal.getTime();
            String fromdate = dateFormatter.format(todate1);
            String StartDate = fromdate;
            String Enddate = today;
            String decesedate1 = DecreaseMonth(Enddate);
            mStartDate = decesedate1;
            mEndDate = Enddate;
            myCount = 3;
            this.authDialog = createAuthDialog(getActivity());
            this.authDialog.show();
            mDataset[1] = mEndDate;
            mDataset[0] = StartDate;
            i = 1;
            MonthResultResponse(MyAccountManager2.restoken, websiteid, mStartDate, mEndDate, counttext);
          /*  } else {
                Snackbar.make(view, "Lost Internet Connection", Snackbar.LENGTH_LONG).show();
            }*/
        } else if (counttext == 4) {
//          if (Connectivity.isConnected(getActivity())) {
            Date date = new Date();
            String today = dateFormatter.format(date);
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, -4);
            Date todate1 = cal.getTime();
            String fromdate = dateFormatter.format(todate1);
            String StartDate = fromdate;
            String Enddate = today;
            String decesedate1 = DecreaseMonth(Enddate);
            mStartDate = decesedate1;
            mEndDate = Enddate;
            myCount = 4;
            this.authDialog = createAuthDialog(getActivity());
            this.authDialog.show();
            mDataset[1] = mEndDate;
            mDataset[0] = StartDate;
            i = 1;
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
            String decesedate1 = DecreaseMonth(Enddate);
            mStartDate = decesedate1;
            mEndDate = Enddate;
            myCount = 5;
            this.authDialog = createAuthDialog(getActivity());
            this.authDialog.show();
            mDataset[1] = mEndDate;
            mDataset[0] = StartDate;
            i = 1;
            MonthResultResponse(MyAccountManager2.restoken, websiteid, mStartDate, mEndDate, counttext);
//            } else {
//                Snackbar.make(view, "Lost Internet Connection", Snackbar.LENGTH_LONG).show();
//            }
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
            String decesedate1 = DecreaseMonth(Enddate);
            mStartDate = decesedate1;
            mEndDate = Enddate;
            myCount = 6;
            this.authDialog = createAuthDialog(getActivity());
            this.authDialog.show();
            mDataset[1] = mEndDate;
            mDataset[0] = StartDate;
            i = 1;
            MonthResultResponse(MyAccountManager2.restoken, websiteid, mStartDate, mEndDate, counttext);
            /*} else {
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
          /*  } else {
                Snackbar.make(view, "Lost Internet Connection", Snackbar.LENGTH_LONG).show();
            }*/
        }
    }

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
        final Vector<String> dimensions = new Vector();
        Vector<String> filters = new Vector();
        metrics.add("ga:sessions,ga:organicSearches,ga:newUsers,ga:bounceRate");
        dimensions.add("ga:medium");
        Call<JsonElement> mService = mInterfaceService.getResult("Bearer " + token, ids, sdate, edate, metrics, dimensions, filters);
        mService.enqueue(new Callback<JsonElement>() {

            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response1) {
                Log.e("visW_response code", response1.code() + "");
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


                        ArrayList<Entry> entries = new ArrayList<Entry>();
                        entries.add(new Entry(Integer.parseInt(directArray.get(0)), 0));
                        entries.add(new Entry(Integer.parseInt(organucArray.get(0)), 1));
                        entries.add(new Entry(Integer.parseInt(socialArray.get(0)), 2));
                        entries.add(new Entry(Integer.parseInt(referralArray.get(0)), 3));

                        // creating labels<br />
                        ArrayList<String> labels = new ArrayList<String>();
                        labels.add("Direct");
                        labels.add("Organic");
                        labels.add("Social");
                        labels.add("Referral");


                        PieDataSet dataset = new PieDataSet(entries, "");
                        dataset.setSliceSpace(2f);
                        dataset.setSelectionShift(5f);
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
                        int[] colors = {getResources().getColor(R.color.skyblue), getResources().getColor(R.color.yellow), getResources().getColor(R.color.violet), getResources().getColor(R.color.pink)};

                        dataset.setColors(colors);
                        //   dataset.setColors(ColorTemplate.COLORFUL_COLORS); // set the color


                        piedatas = new PieData(labels, dataset); // initialize Piedata
                        //   pieChart.setData(data);

                        dismissDialog();
                        MainDashboard.setRefreshActionButtonState(false);
                        reachsessions = new int[]{Integer.parseInt(directArray.get(0)), Integer.parseInt(organucArray.get(0)), Integer.parseInt(socialArray.get(0)), Integer.parseInt(referralArray.get(0))};
                        mVisitorsAdapter = new Visitors_CustomAdapter(getActivity(), mDataset, mDatasetTypes, data, directArray, organucArray, socialArray, referralArray,
                                dyanamicMonthlist, counttext, reachsessions, directPageviewsArray, directBouncerateArray,
                                oraganicPageviewsArray, organicBouncerateArray, socialPageviewsArray, socialBouncerateArray, referralPageviewsArray, referralBouncerateArray, piedatas);
                        mRecyclerView.setAdapter(mVisitorsAdapter);



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
                    Log.d("vistor week fragment ","token expire");
                    Snackbar.make(getView(), "Refresh and try again", Snackbar.LENGTH_LONG).show();
                    MainDashboard.tokengenerate(BaseLoginActivity.accountName,VisitorsFragment.this);
                } else if(response1.code()==400){
                    dismissDialog();
                    MainDashboard.setRefreshActionButtonState(false);
                  /*  if (!accountManager.startAuthActivity(BaseLoginActivity.this, 12, accountName, BaseLoginActivity.this)) {
                        Toast.makeText(BaseLoginActivity.this, "Account name error", 1).show();
                    }*/
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

    @Override
    public void authToken(String str) {

    }

  /*  public class MyValueFormatter implements ValueFormatter {

        @Override
        public String getFormattedValue(float value) {
            // return Math.round(value) + "";
            return Math.abs(value / 100) + "%";


        }
    }

    public class PieValueFormatter implements ValueFormatter {

        @Override
        public String getFormattedValue(float value) {
            // return Math.round(value) + "";
            return Math.round(value) + "";


        }
    }*/
    public String Month(String token, String id, final String sdate, final String edate) {

        String BASE_URL = "https://www.googleapis.com";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        IGoogleAnalyticsApi mInterfaceService = retrofit.create(IGoogleAnalyticsApi.class);
        String ids = "ga:" + id;
        Vector<String> metrics = new Vector();
        Vector<String> dimensions = new Vector();
        metrics.add("ga:sessions,ga:organicSearches");
        dimensions.add("ga:medium");
        Call<JsonElement> mService = mInterfaceService.getVisitors("Bearer " + token, ids, sdate, edate, metrics, dimensions);
        mService.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response1) {
                try {
                    response = response1.body().toString();

                    SEPres(response, edate, i);
                    i++;

                    String[] daterepresentation = sdate.split("-");
                    dyanamicMonthlist.add(daterepresentation[2] + "-" + daterepresentation[1]);
                    if (weekcount > 1) {

                        mEndDate = Increaseeweek(edate);
                        mStartDate = Decreaseweek(mEndDate);
                        weekcount--;
                        Month(MyAccountManager2.restoken, String.valueOf(BaseLoginActivity.Hashmap1.get("bootcamplab")), mStartDate, mEndDate);

                    } else {

                        data = new BarData(setXaxisData_for_MonthWise(sdate), getDataSet());
                        data.setDrawValues(false);
                        mVisitorsAdapter = new Visitors_CustomAdapter(getActivity(), mDataset, mDatasetTypes, data,
                                directArray, organucArray, socialArray, referralArray, dyanamicMonthlist, counttext, reachsessions, directPageviewsArray, directBouncerateArray,
                                oraganicPageviewsArray, organicBouncerateArray, socialPageviewsArray, socialBouncerateArray, referralPageviewsArray, referralBouncerateArray, piedatas);
                        mRecyclerView.setAdapter(mVisitorsAdapter);
                        dismissDialog();
                    }

                    /*data = new BarData(getXAxisValues(sdate), setyaxisData_for_Custonm_Week());
                    data.setDrawValues(false);
                    mVisitorsAdapter = new Visitors_CustomAdapter(getActivity(), mDataset, mDatasetTypes, data, directArray, organucArray, socialArray, referralArray, dyanamicMonthlist, counttext);
                    mRecyclerView.setAdapter(mVisitorsAdapter);
                    dismissDialog();*/

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
    }

    private ArrayList<String> getXAxisValues(String sdate) {
        ArrayList<String> xAxis = new ArrayList<>();

        for (i = dyanamicMonthlist.size(); i > 0; i--) {
            xAxis.add(dyanamicMonthlist.get(i - 1).substring(0, 3));
        }

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


    private ArrayList<BarDataSet> setyaxisData_for_Custonm_Week() {
        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();


        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        BarEntry v1e1 = new BarEntry(Float.parseFloat(directArray.get(0)), 0); // Jan
        valueSet1.add(v1e1);


        ArrayList<BarEntry> valueSet2 = new ArrayList<>();
        BarEntry v1e2 = new BarEntry(Float.parseFloat(organucArray.get(0)), 0); // Feb
        valueSet2.add(v1e2);


        ArrayList<BarEntry> valueSet3 = new ArrayList<>();
        BarEntry v1e3 = new BarEntry(Float.parseFloat(socialArray.get(0)), 0); // Mar
        valueSet3.add(v1e3);

        ArrayList<BarEntry> valueSet4 = new ArrayList<>();
        BarEntry v1e4 = new BarEntry(Float.parseFloat(referralArray.get(0)), 0); // Apr
        valueSet4.add(v1e4);

        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Direct");
        BarDataSet barDataSet2 = new BarDataSet(valueSet2, "Organic");
        BarDataSet barDataSet3 = new BarDataSet(valueSet3, "Social");
        BarDataSet barDataSet4 = new BarDataSet(valueSet4, "Referral");

        barDataSet1.setColor(Color.rgb(81, 182, 231));
        barDataSet2.setColor(Color.rgb(255, 235, 59));
        barDataSet3.setColor(Color.rgb(106, 108, 178));
        barDataSet4.setColor(Color.rgb(238, 64, 129));


        dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);
        dataSets.add(barDataSet2);
        dataSets.add(barDataSet3);
        dataSets.add(barDataSet4);

        return dataSets;

    }

    private ArrayList<BarDataSet> setyaxisData_for_Week() {
        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();


        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        BarEntry v1e1 = new BarEntry(Float.parseFloat(directArray.get(6)), 0); // Jan
        valueSet1.add(v1e1);
        BarEntry v1e11 = new BarEntry(Float.parseFloat(directArray.get(5)), 1); // Jan
        valueSet1.add(v1e11);
        BarEntry v1e12 = new BarEntry(Float.parseFloat(directArray.get(4)), 2); // Jan
        valueSet1.add(v1e12);
        BarEntry v1e13 = new BarEntry(Float.parseFloat(directArray.get(3)), 3); // Jan
        valueSet1.add(v1e13);
        BarEntry v1e14 = new BarEntry(Float.parseFloat(directArray.get(2)), 4); // Jan
        valueSet1.add(v1e14);
        BarEntry v1e15 = new BarEntry(Float.parseFloat(directArray.get(1)), 5); // Jan
        valueSet1.add(v1e15);
        BarEntry v1e16 = new BarEntry(Float.parseFloat(directArray.get(0)), 6); // Jan
        valueSet1.add(v1e16);


        ArrayList<BarEntry> valueSet2 = new ArrayList<>();
        BarEntry v1e2 = new BarEntry(Float.parseFloat(organucArray.get(6)), 0); // Feb
        valueSet2.add(v1e2);
        BarEntry v1e21 = new BarEntry(Float.parseFloat(organucArray.get(5)), 1); // Feb
        valueSet2.add(v1e21);
        BarEntry v1e22 = new BarEntry(Float.parseFloat(organucArray.get(4)), 2); // Feb
        valueSet2.add(v1e22);
        BarEntry v1e23 = new BarEntry(Float.parseFloat(organucArray.get(3)), 3); // Feb
        valueSet2.add(v1e23);
        BarEntry v1e24 = new BarEntry(Float.parseFloat(organucArray.get(2)), 4); // Feb
        valueSet2.add(v1e24);
        BarEntry v1e25 = new BarEntry(Float.parseFloat(organucArray.get(1)), 5); // Feb
        valueSet2.add(v1e25);
        BarEntry v1e26 = new BarEntry(Float.parseFloat(organucArray.get(0)), 6); // Feb
        valueSet2.add(v1e26);

        ArrayList<BarEntry> valueSet3 = new ArrayList<>();
        BarEntry v1e3 = new BarEntry(Float.parseFloat(socialArray.get(6)), 0); // Mar
        valueSet3.add(v1e3);
        BarEntry v1e31 = new BarEntry(Float.parseFloat(socialArray.get(5)), 1); // Mar
        valueSet3.add(v1e31);
        BarEntry v1e32 = new BarEntry(Float.parseFloat(socialArray.get(4)), 2); // Mar
        valueSet3.add(v1e32);
        BarEntry v1e33 = new BarEntry(Float.parseFloat(socialArray.get(3)), 3); // Mar
        valueSet3.add(v1e33);
        BarEntry v1e34 = new BarEntry(Float.parseFloat(socialArray.get(2)), 4); // Mar
        valueSet3.add(v1e34);
        BarEntry v1e35 = new BarEntry(Float.parseFloat(socialArray.get(1)), 5); // Mar
        valueSet3.add(v1e35);
        BarEntry v1e36 = new BarEntry(Float.parseFloat(socialArray.get(0)), 6); // Mar
        valueSet3.add(v1e36);

        ArrayList<BarEntry> valueSet4 = new ArrayList<>();
        BarEntry v1e4 = new BarEntry(Float.parseFloat(referralArray.get(6)), 0); // Apr
        valueSet4.add(v1e4);
        BarEntry v1e41 = new BarEntry(Float.parseFloat(referralArray.get(5)), 1); // Apr
        valueSet4.add(v1e41);
        BarEntry v1e42 = new BarEntry(Float.parseFloat(referralArray.get(4)), 2); // Apr
        valueSet4.add(v1e42);
        BarEntry v1e43 = new BarEntry(Float.parseFloat(referralArray.get(3)), 3); // Apr
        valueSet4.add(v1e43);
        BarEntry v1e44 = new BarEntry(Float.parseFloat(referralArray.get(2)), 4); // Apr
        valueSet4.add(v1e44);
        BarEntry v1e45 = new BarEntry(Float.parseFloat(referralArray.get(1)), 5); // Apr
        valueSet4.add(v1e45);
        BarEntry v1e46 = new BarEntry(Float.parseFloat(referralArray.get(0)), 6); // Apr
        valueSet4.add(v1e46);

        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Direct");
        BarDataSet barDataSet2 = new BarDataSet(valueSet2, "Organic");
        BarDataSet barDataSet3 = new BarDataSet(valueSet3, "Social");
        BarDataSet barDataSet4 = new BarDataSet(valueSet4, "Referral");

        barDataSet1.setColor(Color.rgb(81, 182, 231));
        barDataSet2.setColor(Color.rgb(255, 235, 59));
        barDataSet3.setColor(Color.rgb(106, 108, 178));
        barDataSet4.setColor(Color.rgb(238, 64, 129));

        dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);
        dataSets.add(barDataSet2);
        dataSets.add(barDataSet3);
        dataSets.add(barDataSet4);

        return dataSets;

    }

    private ArrayList<BarDataSet> setyaxisData_for_Month() {
        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();


        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
       /* BarEntry v1e1 = new BarEntry(Float.parseFloat(directArray.get(5)), 0); // Jan
        valueSet1.add(v1e1);*/
        BarEntry v1e11 = new BarEntry(Float.parseFloat(directArray.get(4)), 0); // Jan
        valueSet1.add(v1e11);
        BarEntry v1e12 = new BarEntry(Float.parseFloat(directArray.get(3)), 1); // Jan
        valueSet1.add(v1e12);
        BarEntry v1e13 = new BarEntry(Float.parseFloat(directArray.get(2)), 2); // Jan
        valueSet1.add(v1e13);
        BarEntry v1e14 = new BarEntry(Float.parseFloat(directArray.get(1)), 3); // Jan
        valueSet1.add(v1e14);
        BarEntry v1e15 = new BarEntry(Float.parseFloat(directArray.get(0)), 4); // Jan
        valueSet1.add(v1e15);


        ArrayList<BarEntry> valueSet2 = new ArrayList<>();
        /*BarEntry v1e2 = new BarEntry(Float.parseFloat(organucArray.get(5)), 0); // Feb
        valueSet2.add(v1e2);*/
        BarEntry v1e21 = new BarEntry(Float.parseFloat(organucArray.get(4)), 0); // Feb
        valueSet2.add(v1e21);
        BarEntry v1e22 = new BarEntry(Float.parseFloat(organucArray.get(3)), 1); // Feb
        valueSet2.add(v1e22);
        BarEntry v1e23 = new BarEntry(Float.parseFloat(organucArray.get(2)), 2); // Feb
        valueSet2.add(v1e23);
        BarEntry v1e24 = new BarEntry(Float.parseFloat(organucArray.get(1)), 3); // Feb
        valueSet2.add(v1e24);
        BarEntry v1e25 = new BarEntry(Float.parseFloat(organucArray.get(0)), 4); // Feb
        valueSet2.add(v1e25);


        ArrayList<BarEntry> valueSet3 = new ArrayList<>();
        /*BarEntry v1e3 = new BarEntry(Float.parseFloat(socialArray.get(5)), 0); // Mar
        valueSet3.add(v1e3);*/
        BarEntry v1e31 = new BarEntry(Float.parseFloat(socialArray.get(4)), 0); // Mar
        valueSet3.add(v1e31);
        BarEntry v1e32 = new BarEntry(Float.parseFloat(socialArray.get(3)), 1); // Mar
        valueSet3.add(v1e32);
        BarEntry v1e33 = new BarEntry(Float.parseFloat(socialArray.get(2)), 2); // Mar
        valueSet3.add(v1e33);
        BarEntry v1e34 = new BarEntry(Float.parseFloat(socialArray.get(1)), 3); // Mar
        valueSet3.add(v1e34);
        BarEntry v1e35 = new BarEntry(Float.parseFloat(socialArray.get(0)), 4); // Mar
        valueSet3.add(v1e35);


        ArrayList<BarEntry> valueSet4 = new ArrayList<>();
      /*  BarEntry v1e4 = new BarEntry(Float.parseFloat(referralArray.get(5)), 0); // Apr
        valueSet4.add(v1e4);*/
        BarEntry v1e41 = new BarEntry(Float.parseFloat(referralArray.get(4)), 0); // Apr
        valueSet4.add(v1e41);
        BarEntry v1e42 = new BarEntry(Float.parseFloat(referralArray.get(3)), 1); // Apr
        valueSet4.add(v1e42);
        BarEntry v1e43 = new BarEntry(Float.parseFloat(referralArray.get(2)), 2); // Apr
        valueSet4.add(v1e43);
        BarEntry v1e44 = new BarEntry(Float.parseFloat(referralArray.get(1)), 3); // Apr
        valueSet4.add(v1e44);
        BarEntry v1e45 = new BarEntry(Float.parseFloat(referralArray.get(0)), 4); // Apr
        valueSet4.add(v1e45);


        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Direct");
        BarDataSet barDataSet2 = new BarDataSet(valueSet2, "Organic");
        BarDataSet barDataSet3 = new BarDataSet(valueSet3, "Social");
        BarDataSet barDataSet4 = new BarDataSet(valueSet4, "Referral");

        barDataSet1.setColor(Color.rgb(81, 182, 231));
        barDataSet2.setColor(Color.rgb(255, 235, 59));
        barDataSet3.setColor(Color.rgb(106, 108, 178));
        barDataSet4.setColor(Color.rgb(238, 64, 129));


        dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);
        dataSets.add(barDataSet2);
        dataSets.add(barDataSet3);
        dataSets.add(barDataSet4);

        return dataSets;

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

    OkHttpClient okHttpClient = new OkHttpClient();

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
        metrics.add("ga:sessions,ga:organicSearches");
        dimensions.add("ga:medium");
        Call<JsonElement> mService = mInterfaceService.getResult("Bearer " + token, ids, sdate, edate, metrics, dimensions, filters);
        mService.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response1) {
                Log.e("visM_response code", response1.code() + "");
                if (response1.code() == 200) {
                    try {
                        String responsemn1 = response1.body().toString();
                        SEPres(responsemn1, sdate, i);
                        i++;
                        int monthnum = Integer.parseInt(sdate.substring(5, 7));
                        int month = monthnum + 1;
                        String monthname = getMonthName(month);
                        dyanamicMonthlist.add(monthname.substring(0, 3).toUpperCase() + "-" + sdate.substring(2, 4));
                        if (myCount > 1) {
                            mEndDate = mStartDate;
                            mStartDate = DecreaseMonth(mEndDate);
                            myCount--;
                            MonthResultResponse(MyAccountManager2.restoken, id, mStartDate, mEndDate, monthcount);
                        } else {
                            data = new BarData(setXaxisData_for_MonthWise(sdate), getDataSet());
                            data.setDrawValues(false);
                            dismissDialog();
                            MainDashboard.setRefreshActionButtonState(false);
                            mVisitorsAdapter = new Visitors_CustomAdapter(getActivity(), mDataset, mDatasetTypes, data, directArray, organucArray, socialArray, referralArray,
                                    dyanamicMonthlist, counttext, reachsessions, directPageviewsArray, directBouncerateArray,
                                    oraganicPageviewsArray, organicBouncerateArray, socialPageviewsArray, socialBouncerateArray, referralPageviewsArray, referralBouncerateArray, piedatas);
                            mRecyclerView.setAdapter(mVisitorsAdapter);

                        }
                    } catch (Exception e) {
                        Log.d("Exception", String.valueOf(e));
                    }


                    Log.d("Result", String.valueOf(response));

                }else if (response1.code() == 401) {
                    //TODO: Token expired here
                    dismissDialog();
                    MainDashboard.setRefreshActionButtonState(false);
                    Log.d("visitor month fragment","vistors token expired");
                    Snackbar.make(getView(), " Refresh and try again", Snackbar.LENGTH_LONG).show();
                    MainDashboard.tokengenerate(BaseLoginActivity.accountName,VisitorsFragment.this);
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

    public String SEPres(String result, String Edate, int size) {

        try {
            JSONObject j = new JSONObject(result);
            // String result = j.getString("rows");
            int monthnum = Integer.parseInt(Edate.substring(5, 7));
            int month = monthnum + 1;
            JSONObject arrayresult = j.getJSONObject("totalsForAllResults");
            if (j.has("rows")) {
                JSONArray rows = j.getJSONArray("rows");
                String Organicsearchcount = arrayresult.getString("ga:organicSearches");
                for (int i = 0; i < rows.length(); i++) {

                    JSONArray array = rows.getJSONArray(i);
//                    if (i == 0) {
                    if (String.valueOf(array.get(0)).contentEquals("(none)")) {
                        directArray.add(String.valueOf(array.get(1)));
                    } else if (String.valueOf(array.get(0)).contentEquals("organic")) {
                        organucArray.add(Organicsearchcount);
                    } else if (String.valueOf(array.get(0)).contentEquals("referral")) {
                        referralArray.add(String.valueOf(array.get(1)));
                    } else if (String.valueOf(array.get(0)).contentEquals("social")) {
                        socialArray.add(String.valueOf(array.get(1)));
                    }


                }
                if (directArray.size() == size) {
                } else {
                    directArray.add("0");
                }
                if (organucArray.size() == size) {
                } else {
                    organucArray.add("0");
                }
                if (socialArray.size() == size) {
                } else {
                    socialArray.add("0");
                }
                if (referralArray.size() == size) {
                } else {
                    referralArray.add("0");
                }
            } else {
                directArray.add("0");
                organucArray.add("0");
                socialArray.add("0");
                referralArray.add("0");
            }
        } catch (Exception e) {
            Log.d("Exception", e.getMessage());
        }
        directArray.toString();
        return null;
    }


    private ArrayList<BarDataSet> getDataSet() {
        ArrayList<BarDataSet> dataSets = null;
        if (counttext == 2) {

            ArrayList<BarEntry> valueSet1 = new ArrayList<>();

            BarEntry v1e12 = new BarEntry(Float.parseFloat(directArray.get(1)), 0); // Feb
            valueSet1.add(v1e12);
            BarEntry v1e13 = new BarEntry(Float.parseFloat(directArray.get(0)), 1); // Feb
            valueSet1.add(v1e13);

            ArrayList<BarEntry> valueSet2 = new ArrayList<>();

            BarEntry v2e23 = new BarEntry(Float.parseFloat(organucArray.get(1)), 0); // Feb
            valueSet2.add(v2e23);
            BarEntry v2e24 = new BarEntry(Float.parseFloat(organucArray.get(0)), 1); // Feb
            valueSet2.add(v2e24);

            ArrayList<BarEntry> valueSet3 = new ArrayList<>();

            BarEntry v2e32 = new BarEntry(Float.parseFloat(socialArray.get(1)), 0); // Feb
            valueSet3.add(v2e32);
            BarEntry v2e33 = new BarEntry(Float.parseFloat(socialArray.get(0)), 1); // Feb
            valueSet3.add(v2e33);

            ArrayList<BarEntry> valueSet4 = new ArrayList<>();

            BarEntry v2e42 = new BarEntry(Float.parseFloat(referralArray.get(1)), 0); // Feb
            valueSet4.add(v2e42);
            BarEntry v2e43 = new BarEntry(Float.parseFloat(referralArray.get(0)), 1); // Feb
            valueSet4.add(v2e43);


            BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Direct");
            BarDataSet barDataSet2 = new BarDataSet(valueSet2, "Organic");
            BarDataSet barDataSet3 = new BarDataSet(valueSet3, "Social");
            BarDataSet barDataSet4 = new BarDataSet(valueSet4, "Referral");

            barDataSet1.setColor(Color.rgb(81, 182, 231));
            barDataSet2.setColor(Color.rgb(255, 235, 59));
            barDataSet3.setColor(Color.rgb(106, 108, 178));
            barDataSet4.setColor(Color.rgb(238, 64, 129));
            dataSets = new ArrayList<>();

            dataSets.add(barDataSet1);
            dataSets.add(barDataSet2);
            dataSets.add(barDataSet3);
            dataSets.add(barDataSet4);
        } else if (counttext == 3) {

            ArrayList<BarEntry> valueSet1 = new ArrayList<>();
            BarEntry v1e11 = new BarEntry(Float.parseFloat(directArray.get(2)), 0); // Jan
            valueSet1.add(v1e11);
            BarEntry v1e12 = new BarEntry(Float.parseFloat(directArray.get(1)), 1); // Feb
            valueSet1.add(v1e12);
            BarEntry v1e13 = new BarEntry(Float.parseFloat(directArray.get(0)), 2); // Feb
            valueSet1.add(v1e13);

            ArrayList<BarEntry> valueSet2 = new ArrayList<>();
            BarEntry v2e22 = new BarEntry(Float.parseFloat(organucArray.get(2)), 0);
            valueSet2.add(v2e22);
            BarEntry v2e23 = new BarEntry(Float.parseFloat(organucArray.get(1)), 1); // Feb
            valueSet2.add(v2e23);
            BarEntry v2e24 = new BarEntry(Float.parseFloat(organucArray.get(0)), 2); // Feb
            valueSet2.add(v2e24);

            ArrayList<BarEntry> valueSet3 = new ArrayList<>();
            BarEntry v2e31 = new BarEntry(Float.parseFloat(socialArray.get(2)), 0);
            valueSet3.add(v2e31);
            BarEntry v2e32 = new BarEntry(Float.parseFloat(socialArray.get(1)), 1); // Feb
            valueSet3.add(v2e32);
            BarEntry v2e33 = new BarEntry(Float.parseFloat(socialArray.get(0)), 2); // Feb
            valueSet3.add(v2e33);

            ArrayList<BarEntry> valueSet4 = new ArrayList<>();
            BarEntry v2e41 = new BarEntry(Float.parseFloat(referralArray.get(2)), 0);
            valueSet4.add(v2e41);
            BarEntry v2e42 = new BarEntry(Float.parseFloat(referralArray.get(1)), 1); // Feb
            valueSet4.add(v2e42);
            BarEntry v2e43 = new BarEntry(Float.parseFloat(referralArray.get(0)), 2); // Feb
            valueSet4.add(v2e43);


            BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Direct");
            BarDataSet barDataSet2 = new BarDataSet(valueSet2, "Organic");
            BarDataSet barDataSet3 = new BarDataSet(valueSet3, "Social");
            BarDataSet barDataSet4 = new BarDataSet(valueSet4, "Referral");

            barDataSet1.setColor(Color.rgb(81, 182, 231));
            barDataSet2.setColor(Color.rgb(255, 235, 59));
            barDataSet3.setColor(Color.rgb(106, 108, 178));
            barDataSet4.setColor(Color.rgb(238, 64, 129));
            dataSets = new ArrayList<>();

            dataSets.add(barDataSet1);
            dataSets.add(barDataSet2);
            dataSets.add(barDataSet3);
            dataSets.add(barDataSet4);

        } else if (counttext == 4) {


            ArrayList<BarEntry> valueSet1 = new ArrayList<>();
            BarEntry v1e11 = new BarEntry(Float.parseFloat(directArray.get(3)), 0); // Jan
            valueSet1.add(v1e11);
            BarEntry v1e12 = new BarEntry(Float.parseFloat(directArray.get(2)), 1); // Feb
            valueSet1.add(v1e12);
            BarEntry v1e13 = new BarEntry(Float.parseFloat(directArray.get(1)), 2); // Feb
            valueSet1.add(v1e13);
            BarEntry v1e14 = new BarEntry(Float.parseFloat(directArray.get(0)), 3); // Feb
            valueSet1.add(v1e14);

            ArrayList<BarEntry> valueSet2 = new ArrayList<>();
            BarEntry v2e22 = new BarEntry(Float.parseFloat(organucArray.get(3)), 0);
            valueSet2.add(v2e22);
            BarEntry v2e23 = new BarEntry(Float.parseFloat(organucArray.get(2)), 1); // Feb
            valueSet2.add(v2e23);
            BarEntry v2e24 = new BarEntry(Float.parseFloat(organucArray.get(1)), 2); // Feb
            valueSet2.add(v2e24);
            BarEntry v2e25 = new BarEntry(Float.parseFloat(organucArray.get(0)), 3); // Feb
            valueSet2.add(v2e25);

            ArrayList<BarEntry> valueSet3 = new ArrayList<>();
            BarEntry v2e31 = new BarEntry(Float.parseFloat(socialArray.get(3)), 0);
            valueSet3.add(v2e31);
            BarEntry v2e32 = new BarEntry(Float.parseFloat(socialArray.get(2)), 1); // Feb
            valueSet3.add(v2e32);
            BarEntry v2e33 = new BarEntry(Float.parseFloat(socialArray.get(1)), 2); // Feb
            valueSet3.add(v2e33);
            BarEntry v2e34 = new BarEntry(Float.parseFloat(socialArray.get(0)), 3); // Feb
            valueSet3.add(v2e34);

            ArrayList<BarEntry> valueSet4 = new ArrayList<>();
            BarEntry v2e41 = new BarEntry(Float.parseFloat(referralArray.get(3)), 0);
            valueSet4.add(v2e41);
            BarEntry v2e42 = new BarEntry(Float.parseFloat(referralArray.get(2)), 1); // Feb
            valueSet4.add(v2e42);
            BarEntry v2e43 = new BarEntry(Float.parseFloat(referralArray.get(1)), 2); // Feb
            valueSet4.add(v2e43);
            BarEntry v2e44 = new BarEntry(Float.parseFloat(referralArray.get(0)), 3); // Feb
            valueSet4.add(v2e44);


            BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Direct");
            BarDataSet barDataSet2 = new BarDataSet(valueSet2, "Organic");
            BarDataSet barDataSet3 = new BarDataSet(valueSet3, "Social");
            BarDataSet barDataSet4 = new BarDataSet(valueSet4, "Referral");

            barDataSet1.setColor(Color.rgb(81, 182, 231));
            barDataSet2.setColor(Color.rgb(255, 235, 59));
            barDataSet3.setColor(Color.rgb(106, 108, 178));
            barDataSet4.setColor(Color.rgb(238, 64, 129));

            dataSets = new ArrayList<>();

            dataSets.add(barDataSet1);
            dataSets.add(barDataSet2);
            dataSets.add(barDataSet3);
            dataSets.add(barDataSet4);

        } else if (counttext == 5) {

            ArrayList<BarEntry> valueSet1 = new ArrayList<>();
            BarEntry v1e11 = new BarEntry(Float.parseFloat(directArray.get(4)), 0); // Jan
            valueSet1.add(v1e11);
            BarEntry v1e12 = new BarEntry(Float.parseFloat(directArray.get(3)), 1); // Feb
            valueSet1.add(v1e12);
            BarEntry v1e13 = new BarEntry(Float.parseFloat(directArray.get(2)), 2); // Feb
            valueSet1.add(v1e13);
            BarEntry v1e14 = new BarEntry(Float.parseFloat(directArray.get(1)), 3); // Feb
            valueSet1.add(v1e14);
            BarEntry v1e15 = new BarEntry(Float.parseFloat(directArray.get(0)), 4); // Feb
            valueSet1.add(v1e15);

            ArrayList<BarEntry> valueSet2 = new ArrayList<>();
            BarEntry v2e22 = new BarEntry(Float.parseFloat(organucArray.get(4)), 0);
            valueSet2.add(v2e22);
            BarEntry v2e23 = new BarEntry(Float.parseFloat(organucArray.get(3)), 1); // Feb
            valueSet2.add(v2e23);
            BarEntry v2e24 = new BarEntry(Float.parseFloat(organucArray.get(2)), 2); // Feb
            valueSet2.add(v2e24);
            BarEntry v2e25 = new BarEntry(Float.parseFloat(organucArray.get(1)), 3); // Feb
            valueSet2.add(v2e25);
            BarEntry v2e26 = new BarEntry(Float.parseFloat(organucArray.get(0)), 4); // Feb
            valueSet2.add(v2e26);

            ArrayList<BarEntry> valueSet3 = new ArrayList<>();
            BarEntry v2e31 = new BarEntry(Float.parseFloat(socialArray.get(4)), 0);
            valueSet3.add(v2e31);
            BarEntry v2e32 = new BarEntry(Float.parseFloat(socialArray.get(3)), 1); // Feb
            valueSet3.add(v2e32);
            BarEntry v2e33 = new BarEntry(Float.parseFloat(socialArray.get(2)), 2); // Feb
            valueSet3.add(v2e33);
            BarEntry v2e34 = new BarEntry(Float.parseFloat(socialArray.get(1)), 3); // Feb
            valueSet3.add(v2e34);
            BarEntry v2e35 = new BarEntry(Float.parseFloat(socialArray.get(0)), 4); // Feb
            valueSet3.add(v2e35);

            ArrayList<BarEntry> valueSet4 = new ArrayList<>();
            BarEntry v2e41 = new BarEntry(Float.parseFloat(referralArray.get(4)), 0);
            valueSet4.add(v2e41);
            BarEntry v2e42 = new BarEntry(Float.parseFloat(referralArray.get(3)), 1); // Feb
            valueSet4.add(v2e42);
            BarEntry v2e43 = new BarEntry(Float.parseFloat(referralArray.get(2)), 2); // Feb
            valueSet4.add(v2e43);
            BarEntry v2e44 = new BarEntry(Float.parseFloat(referralArray.get(1)), 3); // Feb
            valueSet4.add(v2e44);
            BarEntry v2e45 = new BarEntry(Float.parseFloat(referralArray.get(0)), 4); // Feb
            valueSet4.add(v2e45);


            BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Direct");
            BarDataSet barDataSet2 = new BarDataSet(valueSet2, "Organic");
            BarDataSet barDataSet3 = new BarDataSet(valueSet3, "Social");
            BarDataSet barDataSet4 = new BarDataSet(valueSet4, "Referral");

            barDataSet1.setColor(Color.rgb(81, 182, 231));
            barDataSet2.setColor(Color.rgb(255, 235, 59));
            barDataSet3.setColor(Color.rgb(106, 108, 178));
            barDataSet4.setColor(Color.rgb(238, 64, 129));

            dataSets = new ArrayList<>();

            dataSets.add(barDataSet1);
            dataSets.add(barDataSet2);
            dataSets.add(barDataSet3);
            dataSets.add(barDataSet4);

        } else if (counttext == 6) {

            ArrayList<BarEntry> valueSet1 = new ArrayList<>();
            BarEntry v1e11 = new BarEntry(Float.parseFloat(directArray.get(5)), 0); // Jan
            valueSet1.add(v1e11);
            BarEntry v1e12 = new BarEntry(Float.parseFloat(directArray.get(4)), 1); // Feb
            valueSet1.add(v1e12);
            BarEntry v1e13 = new BarEntry(Float.parseFloat(directArray.get(3)), 2); // Feb
            valueSet1.add(v1e13);
            BarEntry v1e14 = new BarEntry(Float.parseFloat(directArray.get(2)), 3); // Feb
            valueSet1.add(v1e14);
            BarEntry v1e15 = new BarEntry(Float.parseFloat(directArray.get(1)), 4); // Feb
            valueSet1.add(v1e15);
            BarEntry v1e16 = new BarEntry(Float.parseFloat(directArray.get(0)), 5); // Feb
            valueSet1.add(v1e16);


            ArrayList<BarEntry> valueSet2 = new ArrayList<>();
            BarEntry v2e22 = new BarEntry(Float.parseFloat(organucArray.get(5)), 0);
            valueSet2.add(v2e22);
            BarEntry v2e23 = new BarEntry(Float.parseFloat(organucArray.get(4)), 1); // Feb
            valueSet2.add(v2e23);
            BarEntry v2e24 = new BarEntry(Float.parseFloat(organucArray.get(3)), 2); // Feb
            valueSet2.add(v2e24);
            BarEntry v2e25 = new BarEntry(Float.parseFloat(organucArray.get(2)), 3); // Feb
            valueSet2.add(v2e25);
            BarEntry v2e26 = new BarEntry(Float.parseFloat(organucArray.get(1)), 4); // Feb
            valueSet2.add(v2e26);
            BarEntry v2e27 = new BarEntry(Float.parseFloat(organucArray.get(0)), 5); // Feb
            valueSet2.add(v2e27);

            ArrayList<BarEntry> valueSet3 = new ArrayList<>();
            BarEntry v2e31 = new BarEntry(Float.parseFloat(socialArray.get(5)), 0);
            valueSet3.add(v2e31);
            BarEntry v2e32 = new BarEntry(Float.parseFloat(socialArray.get(4)), 1); // Feb
            valueSet3.add(v2e32);
            BarEntry v2e33 = new BarEntry(Float.parseFloat(socialArray.get(3)), 2); // Feb
            valueSet3.add(v2e33);
            BarEntry v2e34 = new BarEntry(Float.parseFloat(socialArray.get(2)), 3); // Feb
            valueSet3.add(v2e34);
            BarEntry v2e35 = new BarEntry(Float.parseFloat(socialArray.get(1)), 4); // Feb
            valueSet3.add(v2e35);
            BarEntry v2e36 = new BarEntry(Float.parseFloat(socialArray.get(0)), 5); // Feb
            valueSet3.add(v2e36);


            ArrayList<BarEntry> valueSet4 = new ArrayList<>();
            BarEntry v2e41 = new BarEntry(Float.parseFloat(referralArray.get(5)), 0);
            valueSet4.add(v2e41);
            BarEntry v2e42 = new BarEntry(Float.parseFloat(referralArray.get(4)), 1); // Feb
            valueSet4.add(v2e42);
            BarEntry v2e43 = new BarEntry(Float.parseFloat(referralArray.get(3)), 2); // Feb
            valueSet4.add(v2e43);
            BarEntry v2e44 = new BarEntry(Float.parseFloat(referralArray.get(2)), 3); // Feb
            valueSet4.add(v2e44);
            BarEntry v2e45 = new BarEntry(Float.parseFloat(referralArray.get(1)), 4); // Feb
            valueSet4.add(v2e45);
            BarEntry v2e46 = new BarEntry(Float.parseFloat(referralArray.get(0)), 5); // Feb
            valueSet4.add(v2e46);


            BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Direct");
            BarDataSet barDataSet2 = new BarDataSet(valueSet2, "Organic");
            BarDataSet barDataSet3 = new BarDataSet(valueSet3, "Social");
            BarDataSet barDataSet4 = new BarDataSet(valueSet4, "Referral");

            barDataSet1.setColor(Color.rgb(81, 182, 231));
            barDataSet2.setColor(Color.rgb(255, 235, 59));
            barDataSet3.setColor(Color.rgb(106, 108, 178));
            barDataSet4.setColor(Color.rgb(238, 64, 129));

            dataSets = new ArrayList<>();

            dataSets.add(barDataSet1);
            dataSets.add(barDataSet2);
            dataSets.add(barDataSet3);
            dataSets.add(barDataSet4);

        }
        return dataSets;
    }

    public String DecreaseMonth(String date) {
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

    public String Decreaseweek(String date) {
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

    public String Increaseeweek(String date) {
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
                Log.i(VisitorsFragment.TAG, "Cancel Authenticated task!!");
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

                    if (String.valueOf(array.get(0)).contentEquals("(none)")) {
                        directArray.add(String.valueOf(array.get(1)));
                        directPageviewsArray.add(String.valueOf(array.get(3)));
                        directBouncerateArray.add(String.valueOf(array.get(4)));
                        // facebookpageview_persessionArray.add(String.valueOf(array.get(4)));
                    } else if (String.valueOf(array.get(0)).contentEquals("organic")) {
                        organucArray.add(String.valueOf(array.get(1)));
                        oraganicPageviewsArray.add(String.valueOf(array.get(3)));
                        organicBouncerateArray.add(String.valueOf(array.get(4)));
                        // linkedinpageview_persessionArray.add(String.valueOf(array.get(4)));
                    } else if (String.valueOf(array.get(0)).contentEquals("social")) {
                        socialArray.add(String.valueOf(array.get(1)));
                        socialPageviewsArray.add(String.valueOf(array.get(3)));
                        socialBouncerateArray.add(String.valueOf(array.get(4)));

                    } else if (String.valueOf(array.get(0)).contentEquals("referral")) {
                        referralArray.add(String.valueOf(array.get(1)));
                        referralPageviewsArray.add(String.valueOf(array.get(3)));
                        referralBouncerateArray.add(String.valueOf(array.get(4)));

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

                if (directArray.size() == size) {
                } else {
                    directArray.add("0");
                }
                if (organucArray.size() == size) {
                } else {
                    organucArray.add("0");
                }
                if (socialArray.size() == size) {
                } else {
                    socialArray.add("0");
                }
                if (referralArray.size() == size) {
                } else {
                    referralArray.add("0");
                }


                if (directPageviewsArray.size() == size) {
                } else {
                    directPageviewsArray.add("0");
                }
                if (oraganicPageviewsArray.size() == size) {
                } else {
                    oraganicPageviewsArray.add("0");
                }
                if (socialPageviewsArray.size() == size) {
                } else {
                    socialPageviewsArray.add("0");
                }
                if (referralPageviewsArray.size() == size) {
                } else {
                    referralPageviewsArray.add("0");
                }


                if (directBouncerateArray.size() == size) {
                } else {
                    directBouncerateArray.add("0");
                }
                if (organicBouncerateArray.size() == size) {
                } else {
                    organicBouncerateArray.add("0");
                }
                if (socialBouncerateArray.size() == size) {
                } else {
                    socialBouncerateArray.add("0");
                }
                if (referralBouncerateArray.size() == size) {
                } else {
                    referralBouncerateArray.add("0");
                }


            } else {
                directArray.add("0");
                directPageviewsArray.add("0");
                directBouncerateArray.add("0");

                organucArray.add("0");
                oraganicPageviewsArray.add("0");
                organicBouncerateArray.add("0");

                socialArray.add("0");
                socialPageviewsArray.add("0");
                socialBouncerateArray.add("0");

                referralArray.add("0");
                referralPageviewsArray.add("0");
                referralBouncerateArray.add("0");

            }
             /*   monthlist.put("Facebook", facebooksessionArray);
                monthlist.put("LinkedIn", linkedinsessionArray);
                monthlist.put("Twitter", twittersessionArray);
            }*/
        } catch (Exception e) {
            Log.d("Exception", e.getMessage());
        }

    }
}




