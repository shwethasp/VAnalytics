package google.shwethasp.com.analytics_google.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
/*import com.github.mikephil.charting.utils.Highlight;
import com.github.mikephil.charting.utils.ValueFormatter;*/

import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.util.ArrayList;

import google.shwethasp.com.analytics_google.Connectivity;
import google.shwethasp.com.analytics_google.R;
import google.shwethasp.com.analytics_google.fragment.VisitorsFragment;

/**
 * Created by shwethap on 30-09-2016.
 */
public class Visitors_CustomAdapter extends RecyclerView.Adapter<Visitors_CustomAdapter.ViewHolder> {

    private static final String TAG = "Visitors_CustomAdapter";

    private String[] mDataSet;
    private int[] mDataSetTypes;
    BarData alldata;

    ArrayList<String> month = new ArrayList<String>();
    private Context context;
    ArrayList<String> direct = new ArrayList<String>();
    ArrayList<String> organic = new ArrayList<String>();
    ArrayList<String> social = new ArrayList<String>();
    ArrayList<String> referral = new ArrayList<String>();

    ArrayList<String> directPageviwesArray = new ArrayList<String>();
    ArrayList<String> directbouncerateArray = new ArrayList<String>();

    ArrayList<String> organicPageviwesArray = new ArrayList<String>();
    ArrayList<String> organicbouncerateArray = new ArrayList<String>();

    ArrayList<String> socialPageviwesArray = new ArrayList<String>();
    ArrayList<String> socialbouncerateArray = new ArrayList<String>();

    ArrayList<String> referralPageviwesArray = new ArrayList<String>();
    ArrayList<String> referralbouncerateArray = new ArrayList<String>();

    int mycount;

    PieData piedata;
    int[] reach_session;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View v) {
            super(v);
        }
    }

    public class VisitorsSourceViewHolder extends ViewHolder {
        TextView start_date, end_date, piechart_text, visitor_barchart_text;
        BarChart chart;
        PieChart visitor_pechart;
        ProgressBar mProgressBar;

        public VisitorsSourceViewHolder(View v) {
            super(v);
            this.start_date = (TextView) v.findViewById(R.id.start_date_Visitors);
            this.end_date = (TextView) v.findViewById(R.id.end_date_Visitors);
            this.chart = (BarChart) v.findViewById(R.id.Chart_Visitors);
            this.visitor_barchart_text = (TextView) v.findViewById(R.id.visitor_barchart_text);
            this.visitor_pechart = (PieChart) v.findViewById(R.id.visitor_pie_chart);
            Legend l = this.visitor_pechart.getLegend();
            l.setFormSize(10);
            l.setTextSize(8);
            this.piechart_text = (TextView) v.findViewById(R.id.piechart_text);
            this.mProgressBar = (ProgressBar) v.findViewById(R.id.visitor_progressbar);
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    public class VisitorsStatViewHolder extends ViewHolder {
        TextView score;
        LinearLayout visitor_month_dynamiclayout, visitor_dynamic_monthname, visitor_week_custom_layout, visitor_weekcustom_metrics_layout, total_txt_layout, total_layout;
        TextView monthname1, cusweekSocial, cusweekOrganic, cusweekDirect, cusweekReferral, Total_Count, total_text;
        View dummy_totalview, view3;

        public VisitorsStatViewHolder(View v) {
            super(v);
            this.visitor_month_dynamiclayout = (LinearLayout) v.findViewById(R.id.stat_layout_Visitors);
            this.visitor_dynamic_monthname = (LinearLayout) v.findViewById(R.id.reach_monthlayout_Visitors);
            this.monthname1 = (TextView) v.findViewById(R.id.customweek_Visitors);
            this.cusweekSocial = (TextView) v.findViewById(R.id.cusweekTW_Visitors);
            this.cusweekOrganic = (TextView) v.findViewById(R.id.cusweekLI_Visitors);
            this.cusweekDirect = (TextView) v.findViewById(R.id.cusweekFB_Visitors);
            this.cusweekReferral = (TextView) v.findViewById(R.id.cusweekReferral_Visitors);
            this.Total_Count = (TextView) v.findViewById(R.id.cusweekTotal);

            this.visitor_week_custom_layout = (LinearLayout) v.findViewById(R.id.visitor_weekmonth_dynamic_layout);
            this.visitor_weekcustom_metrics_layout = (LinearLayout) v.findViewById(R.id.visitor_weekcustom_metrics_layout);
            this.view3 = (View) v.findViewById(R.id.visitor_view3);
            this.total_text = (TextView) v.findViewById(R.id.total_visitor_text);
            this.dummy_totalview = (View) v.findViewById(R.id.dummy_visitor_totalview);
            this.total_txt_layout = (LinearLayout) v.findViewById(R.id.total_txt_layout);
            this.total_layout = (LinearLayout) v.findViewById(R.id.total_layout);

        }
    }


    public Visitors_CustomAdapter(Context mContexts, String[] dataSet, int[] dataSetTypes, BarData data, ArrayList<String> direct,
                                  ArrayList<String> organic, ArrayList<String> social, ArrayList<String> referral,
                                  ArrayList<String> month, int mycount, int[] reach_sessions, ArrayList<String> directPageviwesArray,
                                  ArrayList<String> directbouncerateArray, ArrayList<String> organicPageviwesArray,
                                  ArrayList<String> organicbouncerateArray, ArrayList<String> socialPageviwesArray,
                                  ArrayList<String> socialbouncerateArray, ArrayList<String> referralPageviwesArray,
                                  ArrayList<String> referralbouncerateArray, PieData piedata) {

        mDataSet = dataSet;
        mDataSetTypes = dataSetTypes;
        alldata = data;

        this.context = mContexts;
        this.direct = direct;
        this.organic = organic;
        this.referral = referral;
        this.social = social;
        this.month = month;
        this.mycount = mycount;

        this.reach_session = reach_sessions;
        this.directPageviwesArray = directPageviwesArray;
        this.directbouncerateArray = directbouncerateArray;
        this.organicPageviwesArray = organicPageviwesArray;
        this.organicbouncerateArray = organicbouncerateArray;
        this.socialPageviwesArray = socialPageviwesArray;
        this.socialbouncerateArray = socialbouncerateArray;
        this.referralPageviwesArray = referralPageviwesArray;
        this.referralbouncerateArray = referralbouncerateArray;
        this.piedata = piedata;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v;

        if (viewType == VisitorsFragment.VisitorSource) {
            v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.visitors_source_card, viewGroup, false);
            return new VisitorsSourceViewHolder(v);
        } else if (viewType == VisitorsFragment.VisitorsStat) {
            v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.visitors_stat_card, viewGroup, false);
            return new VisitorsStatViewHolder(v);
        } else {
            return null;
        }

    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        if (Connectivity.isConnected(context)) {
            if (viewHolder.getItemViewType() == VisitorsFragment.VisitorSource) {
                VisitorsSourceViewHolder holder = (VisitorsSourceViewHolder) viewHolder;

                holder.mProgressBar.setVisibility(View.VISIBLE);

                String[] startDatearray = mDataSet[0].split("-");
                String[] endDatearray = mDataSet[1].split("-");
                //  holder.mProgressBar.setVisibility(View.VISIBLE);
                holder.start_date.setText(startDatearray[2] + "-" + getMonthName(Integer.parseInt(startDatearray[1])).substring(0, 3).toUpperCase() + "-" + startDatearray[0]);
                holder.end_date.setText(endDatearray[2] + "-" + getMonthName(Integer.parseInt(endDatearray[1])).substring(0, 3).toUpperCase() + "-" + endDatearray[0]);

                //  holder.chart.setVisibility(View.VISIBLE);//add this line
                Legend l = holder.chart.getLegend();
                l.setFormSize(10);
                l.setTextSize(8);
                holder.chart.animateXY(0, 1000);
                holder.chart.setExtraBottomOffset(10);

                holder.chart.setData(alldata);
                holder.chart.setDescription("");

                //Disable zoom for chart
                //   holder.chart.setScaleEnabled(false);
                holder.chart.setScaleXEnabled(false);
                holder.chart.setScaleYEnabled(true);
                //  holder.chart.setTouchEnabled(true);
                holder.chart.setClickable(true);

                holder.chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

                   /* @Override
                    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

                    }*/

                    @Override
                    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                        final Toast toast = android.widget.Toast.makeText(context, String.valueOf(e.getVal()).split("\\.\\d{1,2}?$")[0], android.widget.Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CLIP_HORIZONTAL, 0, 0);
                        View view = toast.getView();
                        toast.setView(view);
                        toast.show();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                toast.cancel();
                            }
                        }, 500);
                    }

                    @Override
                    public void onNothingSelected() {

                    }
                });


                  /*  holder.chart.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Log.d("TAG", "TOUCHED IN : " + v.getId());
                    }
                });*/

                /*DataTable data =([
                ['Element', 'Density', { role: 'style' }, { role: 'annotation' } ],
                ['Copper', 8.94, '#b87333', 'Cu' ],
                ['Silver', 10.49, 'silver', 'Ag' ],
                ['Gold', 19.30, 'gold', 'Au' ],
                ['Platinum', 21.45, 'color: #e5e4e2', 'Pt' ]
                ]);
*/
                /*DataTable dataTable=new DataTable();
                dataTable.addColumn(['Element', 'Density', { role: 'style' }, { role: 'annotation' } ],
                ['Copper', 8.94, '#b87333', 'Cu' ],
                ['Silver', 10.49, 'silver', 'Ag' ],
                ['Gold', 19.30, 'gold', 'Au' ],
                ['Platinum', 21.45, 'color: #e5e4e2', 'Pt' ]
                )*/

                holder.chart.isDrawValueAboveBarEnabled();
                //  this.alldata.setDrawValues(false);
                YAxis yAxisRight = holder.chart.getAxisRight();
                yAxisRight.setEnabled(false);
                YAxis yAxisLeft = holder.chart.getAxisLeft();
                yAxisLeft.setValueFormatter(new YAxisValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, YAxis yAxis) {
                        return Math.round(value) + "";
                    }
                });
                XAxis xAxis = holder.chart.getXAxis();
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

           /* LeftAxis YAxis = holder.chart.getAxisLeft ();
            RightAxis YAxis = chart.getAxisRight ();

            LeftAxis YAxis = holder.chart.getAxis (com.github.mikephil.charting.components.YAxis.AxisDependency.LEFT);

            YAxis YAxis = radarChart.getYAxis ();*/
                holder.chart.invalidate();
                holder.mProgressBar.setVisibility(View.GONE);

                if (mycount == 0 || mycount == 1 || mycount == 7) {
                    holder.visitor_pechart.setVisibility(View.VISIBLE);
                    holder.chart.setVisibility(View.INVISIBLE);
                } else {
                    holder.visitor_pechart.setVisibility(View.INVISIBLE);
                    holder.chart.setVisibility(View.VISIBLE);
                    holder.piechart_text.setVisibility(View.GONE);

                    int checksum = 0;
                    for (int c = 0; c < organic.size(); c++) {
                        int ressum = Integer.parseInt(direct.get(c)) + Integer.parseInt(organic.get(c)) + Integer.parseInt(social.get(c)) + Integer.parseInt(referral.get(c));
                        checksum = ressum + checksum;
                    }
                    if (checksum > 0) {
                        holder.visitor_barchart_text.setVisibility(View.GONE);
                        holder.chart.setVisibility(View.VISIBLE);
                    } else {
                        holder.visitor_barchart_text.setVisibility(View.VISIBLE);
                        holder.visitor_barchart_text.setText("No chart data available.");
                        holder.chart.setVisibility(View.GONE);
                    }


                }
                if (piedata != null) {
                    float i = piedata.getYValueSum();
                    Log.e(" piedata.getYValueSum ", "" + i);
                    if (i == 0.0) {
                        holder.piechart_text.setVisibility(View.VISIBLE);
                        holder.piechart_text.setText("No chart data available.");
                        holder.visitor_pechart.setVisibility(View.GONE);
                    } else {
                        holder.visitor_pechart.setData(piedata);
                        holder.visitor_pechart.animateX(2000);
                        // enable hole and configure
                        holder.visitor_pechart.setDrawHoleEnabled(true);
                        holder.visitor_pechart.setHoleColorTransparent(true);
                        holder.visitor_pechart.setTransparentCircleRadius(10);

                        holder.visitor_pechart.setDrawSliceText(false);
                        holder.visitor_pechart.isDrawSliceTextEnabled();

                        holder.visitor_pechart.setHoleRadius(35f);
                        holder.visitor_pechart.setDescription(" ");
                        holder.visitor_pechart.setRotationEnabled(false);
                        holder.visitor_pechart.invalidate();
                        holder.mProgressBar.setVisibility(View.GONE);
                        //  holder.piechart_text.setVisibility(View.GONE);
                    }

                } else {
                    //  holder.visitor_pechart.setVisibility(View.INVISIBLE);
                    holder.piechart_text.setVisibility(View.GONE);
                    holder.visitor_pechart.setVisibility(View.GONE);


                }

            } else if (viewHolder.getItemViewType() == VisitorsFragment.VisitorsStat) {
                VisitorsStatViewHolder holder = (VisitorsStatViewHolder) viewHolder;

                if (mycount == 0 || mycount == 1 || mycount == 7) {
                    // holder.headline.setText(mDataSet[position]);
                    for (int i = direct.size(); i > 0; i--) {
                        //size 3 change it according to requiremnt


                        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(
                                Context.LAYOUT_INFLATER_SERVICE);
                        View convertView = mInflater.inflate(R.layout.detailedstat_result_visitors, null,
                                false);
                        View convertView1 = mInflater.inflate(R.layout.reach_detailed_dynamic_monthname_visitors, null,
                                false);
                        View visitor_dynamic_weekcustom_view = mInflater.inflate(R.layout.visitor_dynamic_statistics, null, false);
                        // TextView site_names = (TextView) convertView.findViewById(R.id.site_names);
              /*      TextView direct_count = (TextView) convertView.findViewById(R.id.direct_count);
                    TextView organic_count = (TextView) convertView.findViewById(R.id.organic_count);
                    TextView social_count = (TextView) convertView.findViewById(R.id.social_count);
                    TextView referral_count = (TextView) convertView.findViewById(R.id.referral_count);
*/


                        TextView direct_session_count = (TextView) visitor_dynamic_weekcustom_view.findViewById(R.id.direct_sessioncount);
                        TextView direct_pageview_count = (TextView) visitor_dynamic_weekcustom_view.findViewById(R.id.direct_pageviewcount);
                        // TextView facebook_avgsession_count = (TextView) dynamic_weekcustom_view.findViewById(R.id.facebook_avg_sessioncount);
                        TextView direct_bouncearte_count = (TextView) visitor_dynamic_weekcustom_view.findViewById(R.id.direct_bouncerate_count);

                        TextView oraganic_session_count = (TextView) visitor_dynamic_weekcustom_view.findViewById(R.id.organic_sessioncount);
                        TextView oraganic_pageview_count = (TextView) visitor_dynamic_weekcustom_view.findViewById(R.id.organic_pageviewcount);
                        //  TextView linkedin_avgsession_count = (TextView) dynamic_weekcustom_view.findViewById(R.id.linkedin_avg_sessioncount);
                        TextView organic_bouncerate_count = (TextView) visitor_dynamic_weekcustom_view.findViewById(R.id.organic_bouncerate_count);

                        TextView social_session_count = (TextView) visitor_dynamic_weekcustom_view.findViewById(R.id.social_sessioncount);
                        TextView social_pageview_count = (TextView) visitor_dynamic_weekcustom_view.findViewById(R.id.social_pageviewcount);
                        //  TextView twitter_avgsession_count = (TextView) dynamic_weekcustom_view.findViewById(R.id.twitter_avg_sessioncount);
                        TextView social_bouncerate_count = (TextView) visitor_dynamic_weekcustom_view.findViewById(R.id.social_bouncerate_count);

                        TextView referral_session_count = (TextView) visitor_dynamic_weekcustom_view.findViewById(R.id.referral_sessioncount);
                        TextView referral_pageview_count = (TextView) visitor_dynamic_weekcustom_view.findViewById(R.id.referral_pageviewcount);
                        //  TextView twitter_avgsession_count = (TextView) dynamic_weekcustom_view.findViewById(R.id.twitter_avg_sessioncount);
                        TextView referral_bouncerate_count = (TextView) visitor_dynamic_weekcustom_view.findViewById(R.id.referral_bouncerate_count);


                 /*  direct_count.setText(direct.get(i - 1));
                    organic_count.setText(organic.get(i - 1));
                    social_count.setText(social.get(i - 1));
                    referral_count.setText(referral.get(i - 1));*/

                        direct_session_count.setText(direct.get(0));
                        direct_pageview_count.setText(directPageviwesArray.get(0));

                        Double direct_bounceratevalue = Double.valueOf(directbouncerateArray.get(0));
                        DecimalFormat df = new DecimalFormat("#.##");
                        String dx = df.format(direct_bounceratevalue);
                        direct_bounceratevalue = Double.valueOf(dx);
                        direct_bouncearte_count.setText(direct_bounceratevalue.toString() + "%");

                        oraganic_session_count.setText(organic.get((0)));
                        oraganic_pageview_count.setText(organicPageviwesArray.get(0));

                        Double organic_bounceratevalue = Double.valueOf(organicbouncerateArray.get(0));
                        DecimalFormat df1 = new DecimalFormat("#.##");
                        String dx1 = df1.format(organic_bounceratevalue);
                        organic_bounceratevalue = Double.valueOf(dx1);
                        organic_bouncerate_count.setText(organic_bounceratevalue.toString() + "%");

                        social_session_count.setText(social.get(0));
                        social_pageview_count.setText(socialPageviwesArray.get(0));

                        Double social_bounceratevalue = Double.valueOf(socialbouncerateArray.get(0));
                        DecimalFormat df2 = new DecimalFormat("#.##");
                        String dx2 = df2.format(social_bounceratevalue);
                        social_bounceratevalue = Double.valueOf(dx2);
                        social_bouncerate_count.setText(social_bounceratevalue.toString() + "%");

                        referral_session_count.setText(referral.get(0));
                        referral_pageview_count.setText(referralPageviwesArray.get(0));

                        Double referral_bounceratevalue = Double.valueOf(referralbouncerateArray.get(0));
                        DecimalFormat df3 = new DecimalFormat("#.##");
                        String dx3 = df3.format(referral_bounceratevalue);
                        referral_bounceratevalue = Double.valueOf(dx3);
                        referral_bouncerate_count.setText(referral_bounceratevalue.toString() + "%");


                  /*  holder.cusweekDirect.setText(direct.get(i - 1));
                    holder.cusweekOrganic.setText(organic.get(i - 1));
                    holder.cusweekSocial.setText(social.get(i - 1));
                    holder.cusweekReferral.setText(referral.get(i - 1));*/
                        //   int sum = Integer.parseInt(direct.get(i - 1)) + Integer.parseInt(organic.get(i - 1)) + Integer.parseInt(social.get(i - 1)) + Integer.parseInt(referral.get(i - 1));
                        //holder.Total_Count.setText(String.valueOf(sum));

                        convertView.setVisibility(View.GONE);
                        //   holder.monthname1.setVisibility(View.VISIBLE);
                        //     holder.monthname1.setText(month.get(i - 1));

                        holder.visitor_week_custom_layout.setVisibility(View.VISIBLE);
                        holder.visitor_month_dynamiclayout.setVisibility(View.GONE);
                        holder.total_text.setVisibility(View.GONE);
                        holder.dummy_totalview.setVisibility(View.GONE);
                        holder.view3.setVisibility(View.GONE);

                        convertView1.setVisibility(View.GONE);
                        holder.visitor_week_custom_layout.addView(visitor_dynamic_weekcustom_view);
                        holder.visitor_month_dynamiclayout.addView(convertView);
                        holder.visitor_weekcustom_metrics_layout.setVisibility(View.VISIBLE);
                        holder.total_txt_layout.setVisibility(View.VISIBLE);


                    }
                } else {
                    for (int i = direct.size(); i > 0; i--) {
                        //size 3 change it according to requiremnt


                        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(
                                Context.LAYOUT_INFLATER_SERVICE);
                        View convertView = mInflater.inflate(R.layout.detailedstat_result_visitors, null,
                                false);
                        View convertView1 = mInflater.inflate(R.layout.reach_detailed_dynamic_monthname_visitors, null,
                                false);

                        // TextView site_names = (TextView) convertView.findViewById(R.id.site_names);
                        TextView direct_count = (TextView) convertView.findViewById(R.id.direct_count);
                        TextView organic_count = (TextView) convertView.findViewById(R.id.organic_count);
                        TextView social_count = (TextView) convertView.findViewById(R.id.social_count);
                        TextView referral_count = (TextView) convertView.findViewById(R.id.referral_count);
                        TextView Total_Count_Visitors = (TextView) convertView.findViewById(R.id.total_count);


                        TextView monthname = (TextView) convertView1.findViewById(R.id.month_name_visitors);
                        int sum = Integer.parseInt(direct.get(i - 1)) + Integer.parseInt(organic.get(i - 1)) + Integer.parseInt(social.get(i - 1)) + Integer.parseInt(referral.get(i - 1));


                        direct_count.setText(direct.get(i - 1));
                        organic_count.setText(organic.get(i - 1));
                        social_count.setText(social.get(i - 1));
                        referral_count.setText(referral.get(i - 1));
                        Total_Count_Visitors.setText(String.valueOf(sum));

                        holder.dummy_totalview.setVisibility(View.VISIBLE);
                        holder.total_text.setVisibility(View.VISIBLE);
                        holder.view3.setVisibility(View.VISIBLE);

                        holder.cusweekDirect.setVisibility(View.INVISIBLE);
                        holder.cusweekOrganic.setVisibility(View.INVISIBLE);
                        holder.cusweekSocial.setVisibility(View.INVISIBLE);
                        holder.cusweekReferral.setVisibility(View.INVISIBLE);
                        holder.Total_Count.setVisibility(View.INVISIBLE);

                        holder.total_txt_layout.setVisibility(View.VISIBLE);
                        holder.total_layout.setVisibility(View.VISIBLE);


                        convertView.setVisibility(View.VISIBLE);
                        holder.visitor_weekcustom_metrics_layout.setVisibility(View.GONE);
                        holder.visitor_month_dynamiclayout.setVisibility(View.VISIBLE);
                        holder.visitor_week_custom_layout.setVisibility(View.GONE);
                        convertView1.setVisibility(View.VISIBLE);


                        monthname.setText(month.get(i - 1));
                        holder.monthname1.setVisibility(View.GONE);

                        holder.visitor_month_dynamiclayout.addView(convertView);
                        holder.visitor_dynamic_monthname.addView(convertView1);
                    }


                }
            }
        } else {
            Snackbar.make(viewHolder.itemView, "Lost Internet connection", Snackbar.LENGTH_LONG).show();
        }

    }

    @Override
    public int getItemCount() {
        return mDataSetTypes.length;
    }

    @Override
    public int getItemViewType(int position) {
        return mDataSetTypes[position];
    }

    String getMonthName(int monthNumber) {
        String[] months = new DateFormatSymbols().getMonths();
        int n = monthNumber - 1;
        return (n >= 0 && n <= 11) ? months[n] : "wrong number";
    }

  /*  public class MyValueFormatter implements ValueFormatter {

        @Override
        public String getFormattedValue(float value) {

            return Math.round(value) + "";
        }
    }*/
}

