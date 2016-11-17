package google.shwethasp.com.analytics_google.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.RectF;
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
//import com.github.mikephil.charting.utils.Highlight;
//import com.github.mikephil.charting.utils.ValueFormatter;

import org.achartengine.renderer.XYSeriesRenderer;

import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import google.shwethasp.com.analytics_google.Connectivity;
import google.shwethasp.com.analytics_google.R;
import google.shwethasp.com.analytics_google.fragment.ReachFragment;

/**
 * Created by shwethap on 29-09-2016.
 */
public class Reach_CustomAdapter extends RecyclerView.Adapter<Reach_CustomAdapter.ViewHolder> {

    private static final String TAG = "Reach_CustomAdapter";
    private ProgressDialog authDialog;
    private String[] mDataSet;
    private int[] mDataSetTypes;
    BarData alldata;
    ArrayList<String> month = new ArrayList<String>();
    private Context context;
    //    int[] reach_session;
    HashMap<String, String> resultHashmap;
    LinearLayout viatdup;
    int mycountpost;
    ArrayList<String> fb = new ArrayList<String>();
    ArrayList<String> link = new ArrayList<String>();
    ArrayList<String> twitter = new ArrayList<String>();

    ArrayList<String> facebookPageviwesArray = new ArrayList<String>();
    ArrayList<String> facebookAvgSessionArray = new ArrayList<String>();
    ArrayList<String> facebookpageview_persessionArray = new ArrayList<String>();


    ArrayList<String> linkedinAvgSessionArray = new ArrayList<String>();
    ArrayList<String> linkedinPageviwesArray = new ArrayList<String>();
    ArrayList<String> linkedinpageview_persessionArray = new ArrayList<String>();

    ArrayList<String> twitterpageview_persessionArray = new ArrayList<String>();
    ArrayList<String> twitterPageviwesArray = new ArrayList<String>();
    ArrayList<String> twitterAvgSessionArray = new ArrayList<String>();

    PieData piedata;
    int mycount;

//    int[] pieChartValues = new int[3];


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View v) {
            super(v);
        }
    }

    public class ReachSourceViewHolder extends ViewHolder {
        TextView start_date, end_date, nochart_text, reach_barchart_text;
        BarChart chart;
        XYSeriesRenderer xySeriesRenderer = new XYSeriesRenderer();


        //        public static final String TYPE = "type";
//        private int[] COLORS;
//        private CategorySeries mSeries;
//        private DefaultRenderer mRenderer;
//        private GraphicalView mChartView;
        PieChart pielayout;
        ProgressBar mProgressBar;


        public ReachSourceViewHolder(View v) {
            super(v);

            this.start_date = (TextView) v.findViewById(R.id.start_date);
            this.end_date = (TextView) v.findViewById(R.id.end_date);
            this.chart = (BarChart) v.findViewById(R.id.chart);
            Legend l = this.chart.getLegend();
            l.setFormSize(10);
            l.setTextSize(8);
            this.nochart_text = (TextView) v.findViewById(R.id.no_chart_data);
//            this.mRenderer = new DefaultRenderer();
//            this.mSeries = new CategorySeries("");
//            this.COLORS = new int[]{Color.rgb(81, 182, 231), Color.rgb(255, 235, 59), Color.rgb(106, 108, 178), Color.rgb(238, 64, 129)};
            this.pielayout = (PieChart) v.findViewById(R.id.reach_pie_chart);
            Legend l1 = this.pielayout.getLegend();
            l1.setFormSize(10);
            l1.setTextSize(8);
            //  this.pieChartValues = new int[3];
            this.mProgressBar = (ProgressBar) v.findViewById(R.id.reach_progressbar);
            mProgressBar.setVisibility(View.VISIBLE);

            //this.chart.setVisibility(View.INVISIBLE);
            // this.pielayout.setVisibility(View.INVISIBLE);
            this.reach_barchart_text = (TextView) v.findViewById(R.id.reach_barchart_text);


        }
    }

    public class ReachStatViewHolder extends ViewHolder {
        TextView score;
        LinearLayout reach_month_dynamiclayout, reach_dynamic_monthname, reach_week_custom_layout, reach_weekcustom_metrics_layout, reach_total_txt, reach_txt_layout;
        TextView monthname1, cusweekTW, cusweekFB, cusweekLI, custotal_count, total_text;
        View dummy_totalview, view3;

        public ReachStatViewHolder(View v) {
            super(v);
            //this.score = (TextView) v.findViewById(R.id.score);
            this.reach_month_dynamiclayout = (LinearLayout) v.findViewById(R.id.stat_layout);
            this.reach_week_custom_layout = (LinearLayout) v.findViewById(R.id.reach_weekmonth_dynamic_layout);
            this.reach_dynamic_monthname = (LinearLayout) v.findViewById(R.id.reach_monthname_layout);
            this.cusweekTW = (TextView) v.findViewById(R.id.cusweekTW);
            this.cusweekLI = (TextView) v.findViewById(R.id.cusweekLI);
            this.cusweekFB = (TextView) v.findViewById(R.id.cusweekFB);
            this.custotal_count = (TextView) v.findViewById(R.id.cusweekTotal);
            this.total_text = (TextView) v.findViewById(R.id.total_text);
            this.dummy_totalview = (View) v.findViewById(R.id.dummy_totalview);
            this.view3 = (View) v.findViewById(R.id.view3);
            this.reach_weekcustom_metrics_layout = (LinearLayout) v.findViewById(R.id.reach_weekcustom_metrics_layout);
            this.reach_total_txt = (LinearLayout) v.findViewById(R.id.reach_total_txt);

            this.reach_txt_layout = (LinearLayout) v.findViewById(R.id.reach_txt_layout);
        }
    }


    //    public Reach_CustomAdapter(Context mContexts, String[] dataSet, int[] dataSetTypes, BarData data, ArrayList<String> resultCount, ArrayList<String> resultsites) {
    public Reach_CustomAdapter(Context mContexts, String[] dataSet, int[] dataSetTypes, BarData data, ArrayList<String> fb,
                               ArrayList<String> link, ArrayList<String> twitter, ArrayList<String> month, int mycount,
                               ArrayList<String> fbpageviews, ArrayList<String> lipageviews, ArrayList<String> twpageviews,
                               ArrayList<String> fbavgsession, ArrayList<String> liavgsession, ArrayList<String> twavgsession,
                               ArrayList<String> fbpage_persession, ArrayList<String> lipage_persession, ArrayList<String> twpage_persession, PieData piedata) {
        mDataSet = dataSet;
        mDataSetTypes = dataSetTypes;
        alldata = data;
//        this.alldata.setDrawValues(true);
        this.context = mContexts;
        this.fb = fb;
        this.link = link;
        this.twitter = twitter;
        this.month = month;
        this.mycount = mycount;
//        this.reach_session = reach_sessions;
        this.facebookAvgSessionArray = fbavgsession;
        this.facebookpageview_persessionArray = fbpage_persession;
        this.facebookPageviwesArray = fbpageviews;
        this.linkedinAvgSessionArray = liavgsession;
        this.linkedinpageview_persessionArray = lipage_persession;
        this.linkedinPageviwesArray = lipageviews;
        this.twitterAvgSessionArray = twavgsession;
        this.twitterpageview_persessionArray = twpage_persession;
        this.twitterPageviwesArray = twpageviews;
        this.piedata = piedata;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v;

        if (viewType == ReachFragment.ReachSource) {
            v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.reach_source_card, viewGroup, false);

            return new ReachSourceViewHolder(v);
        } else if (viewType == ReachFragment.ReachStat) {
            v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.reach_stat_card, viewGroup, false);
            return new ReachStatViewHolder(v);
        } else {
            return null;
        }

    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        if (Connectivity.isConnected(context)) {
            if (viewHolder.getItemViewType() == ReachFragment.ReachSource) {
                final ReachSourceViewHolder holder = (ReachSourceViewHolder) viewHolder;

                holder.mProgressBar.setVisibility(View.VISIBLE);

                String[] startDatearray = mDataSet[0].split("-");
                String[] endDatearray = mDataSet[1].split("-");
                holder.start_date.setText(startDatearray[2] + "-" + getMonthName(Integer.parseInt(startDatearray[1])).substring(0, 3).toUpperCase() + "-" + startDatearray[0]);
                holder.end_date.setText(endDatearray[2] + "-" + getMonthName(Integer.parseInt(endDatearray[1])).substring(0, 3).toUpperCase() + "-" + endDatearray[0]);

                holder.chart.setData(alldata);

                holder.chart.setDescription(" ");
                // holder.chart.animateY(2000);
                holder.chart.setPinchZoom(false);
                //Disable zoom for chart
                //   holder.chart.setScaleEnabled(false);




                holder.chart.setScaleXEnabled(false);
                holder.chart.setScaleYEnabled(true);
                holder.chart.setExtraBottomOffset(10);
                holder.chart.animateXY(0, 1000);
                XAxis xAxis = holder.chart.getXAxis();
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                holder.chart.isDrawValueAboveBarEnabled();//returns true or false

                YAxis yAxisLeft = holder.chart.getAxisLeft();
                yAxisLeft.setValueFormatter(new YAxisValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, YAxis yAxis) {
                        return Math.round(value) + "";
                    }
                });
                // this.alldata.setDrawValues(true);
                YAxis yAxisRight = holder.chart.getAxisRight();
                yAxisRight.setEnabled(false);
           /* YAxis yAxisLeft = holder.chart.getAxisLeft();
            yAxisLeft.getAxisMaxValue();
            yAxisLeft.setValueFormatter(new MyValueFormatter());*/
                holder.chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                    //   protected RectF mOnValueSelectedRectF = new RectF();

                    /*@Override
                    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {


                    }*/

                    @Override
                    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                        final Toast toast = Toast.makeText(context, String.valueOf(e.getVal()).split("\\.\\d{1,2}?$")[0], Toast.LENGTH_SHORT);
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

                holder.chart.invalidate();
                holder.mProgressBar.setVisibility(View.GONE);
                XYSeriesRenderer xySeriesRenderer = new XYSeriesRenderer();
                //  holder.xySeriesRenderer.setsetFillPoints(true);

                // Setting event click listener for the button btn_chart of the MainActivity layout

//                holder.mRenderer.setApplyBackgroundColor(true);
//                holder.mRenderer.setBackgroundColor(Color.rgb(255, 255, 255));
//                holder.mRenderer.setChartTitleTextSize(20);
//                holder.mRenderer.setLabelsTextSize(15);
//                holder.mRenderer.setLegendTextSize(15);
//                holder.mRenderer.setMargins(new int[]{20, 30, 15, 0});
//                holder.mRenderer.setZoomButtonsVisible(true);
//                holder.mRenderer.setShowLabels(false);
                // holder.mRenderer.isShowLabels();
                //   holder.mRenderer.setStartAngle(90);
//            holder.chart.setVisibility(View.INVISIBLE);
                if (mycount == 0 || mycount == 1 || mycount == 7) {
                    holder.pielayout.setVisibility(View.VISIBLE);
                    holder.chart.setVisibility(View.INVISIBLE);
                } else {
                    holder.pielayout.setVisibility(View.INVISIBLE);
                    holder.chart.setVisibility(View.VISIBLE);
                    holder.nochart_text.setVisibility(View.GONE);
                    int checksum = 0;
                    for (int c = 0; c < fb.size(); c++) {
                        int ressum = Integer.parseInt(fb.get(c)) + Integer.parseInt(link.get(c)) + Integer.parseInt(twitter.get(c));
                        checksum = ressum + checksum;
                    }
                    if (checksum > 0) {
                        holder.reach_barchart_text.setVisibility(View.GONE);
                        holder.chart.setVisibility(View.VISIBLE);
                    } else {
                        holder.reach_barchart_text.setVisibility(View.VISIBLE);
                        holder.reach_barchart_text.setText("No chart data available.");
                        holder.chart.setVisibility(View.GONE);
                    }
                }
//                if (holder.mChartView == null) {
//
//                    holder.mChartView = ChartFactory.getPieChartView(context, holder.mSeries, holder.mRenderer);
//                    holder.mRenderer.setClickEnabled(true);
//                    holder.mRenderer.setSelectableBuffer(10);
//                    holder.layout.setUsePercentValues(true);

                // holder.layout.isDrawSliceTextEnabled();

                if (piedata != null) {
                    float i = piedata.getYValueSum();
                    Log.e(" piedata.getYValueSum ", "" + i);
                    if (i == 0.0) {
                        holder.nochart_text.setVisibility(View.VISIBLE);
                        holder.nochart_text.setText("No chart data available.");
                        holder.pielayout.setVisibility(View.GONE);
                    } else {
                        holder.pielayout.setData(piedata);
                        holder.pielayout.animateX(2000);

                        // enable hole and configure
                        holder.pielayout.setDrawHoleEnabled(true);
                       // holder.pielayout.setHoleColor(android.R.color.transparent);
                        holder.pielayout.setHoleColorTransparent(true);
                        holder.pielayout.setTransparentCircleRadius(10);
                        holder.pielayout.setRotationEnabled(false);
                        holder.pielayout.setDrawSliceText(false);

                        holder.pielayout.setDescription(" ");
                        holder.pielayout.setHoleRadius(35f);

                        // holder.pielayout.isDrawSliceTextEnabled();


                        //holder.nochart_text.setVisibility(View.GONE);
                    }

                } else {
                    holder.nochart_text.setVisibility(View.GONE);
                    holder.pielayout.setVisibility(View.GONE);

                }


                   /* holder.layout.addView(holder.mChartView, new RecyclerView.LayoutParams(RecyclerView.LayoutParams.FILL_PARENT,
                            RecyclerView.LayoutParams.FILL_PARENT));*/
//                } else {
//                    holder.mChartView.repaint();
//                }
//                ArrayList<String> listsites = new ArrayList<String>();
//                listsites.add("Facebook");
//                listsites.add("LinkedIn");
//                listsites.add("Twitter");
//                if (reach_session != null) {
//                    this.pieChartValues = reach_session;
//                    for (int i = 0; i < this.pieChartValues.length; i++) {
//                        holder.mSeries.add(listsites.get(i), this.pieChartValues[i]);
//                        SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
//                        renderer.setColor(holder.COLORS[(holder.mSeries.getItemCount() - 1) % holder.COLORS.length]);
//                        holder.mRenderer.addSeriesRenderer(renderer);
//                        if (holder.mChartView != null)
//                            holder.mChartView.repaint();
//                    }
//                }
                holder.pielayout.invalidate();
                holder.mProgressBar.setVisibility(View.GONE);
            } else if (viewHolder.getItemViewType() == ReachFragment.ReachStat) {
                ReachStatViewHolder holder = (ReachStatViewHolder) viewHolder;
                if (mycount == 0 || mycount == 1 || mycount == 7) {
                    // holder.headline.setText(mDataSet[position]);
                    // for (int i = 0; i < fb.size(); i++) {
                    for (int i = fb.size(); i > 0; i--) {
                        //size 3 change it according to requiremnt


                        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(
                                Context.LAYOUT_INFLATER_SERVICE);
                        View convertView = mInflater.inflate(R.layout.detailedstat_result, null,
                                false);
                        View convertView1 = mInflater.inflate(R.layout.reach_detailed_dynamic_monthname, null,
                                false);

                        View dynamic_weekcustom_view = mInflater.inflate(R.layout.reach_dynamic_statistics, null, false);

                        // TextView site_names = (TextView) convertView.findViewById(R.id.site_names);
                        TextView facebook_count = (TextView) convertView.findViewById(R.id.facebook_count);
                        TextView linkedin_count = (TextView) convertView.findViewById(R.id.linkedin_count);
                        TextView twitter_count = (TextView) convertView.findViewById(R.id.twitter_count);
                        TextView reach_totalcount = (TextView) convertView.findViewById(R.id.total_counts);

                        TextView facebook_session_count = (TextView) dynamic_weekcustom_view.findViewById(R.id.facebook_sessioncount);
                        TextView facebook_pageview_count = (TextView) dynamic_weekcustom_view.findViewById(R.id.facebook_pageviewcount);
                        // TextView facebook_avgsession_count = (TextView) dynamic_weekcustom_view.findViewById(R.id.facebook_avg_sessioncount);
                        TextView facebook_pagepersession_count = (TextView) dynamic_weekcustom_view.findViewById(R.id.facebook_page_persession_count);

                        TextView linkedin_session_count = (TextView) dynamic_weekcustom_view.findViewById(R.id.linkedin_sessioncount);
                        TextView linkedin_pageview_count = (TextView) dynamic_weekcustom_view.findViewById(R.id.linkedin_pageviewcount);
                        //  TextView linkedin_avgsession_count = (TextView) dynamic_weekcustom_view.findViewById(R.id.linkedin_avg_sessioncount);
                        TextView linkedin_pagepersession_count = (TextView) dynamic_weekcustom_view.findViewById(R.id.linkedin_page_persession_count);

                        TextView twitter_session_count = (TextView) dynamic_weekcustom_view.findViewById(R.id.twitter_sessioncount);
                        TextView twitter_pageview_count = (TextView) dynamic_weekcustom_view.findViewById(R.id.twitter_pageviewcount);
                        //  TextView twitter_avgsession_count = (TextView) dynamic_weekcustom_view.findViewById(R.id.twitter_avg_sessioncount);
                        TextView twitter_pagepersession_count = (TextView) dynamic_weekcustom_view.findViewById(R.id.twitter_page_persession_count);





                        /*facebook_count.setText(fb.get(i - 1));
                        linkedin_count.setText(link.get(i - 1));
                        twitter_count.setText(twitter.get(i - 1));
                        reach_totalcount.setText(String.valueOf(reach_sum));*/

                        facebook_session_count.setText(fb.get(0));
                        facebook_pageview_count.setText(facebookPageviwesArray.get(0));
                        //facebook_avgsession_count.setText(facebookAvgSessionArray.get(0).substring(0,2));
                        Double fb_sessionvalue = Double.valueOf(facebookpageview_persessionArray.get(0));
                        DecimalFormat df = new DecimalFormat("#.##");
                        String dx = df.format(fb_sessionvalue);
                        fb_sessionvalue = Double.valueOf(dx);
                        facebook_pagepersession_count.setText(fb_sessionvalue.toString());

                        linkedin_session_count.setText(link.get(0));
                        linkedin_pageview_count.setText(linkedinPageviwesArray.get(0));
                        //   linkedin_avgsession_count.setText(linkedinAvgSessionArray.get(0).substring(0,2));
                        Double link_sessionvalue = Double.valueOf(linkedinpageview_persessionArray.get(0));
                        DecimalFormat df1 = new DecimalFormat("#.##");
                        String dx1 = df1.format(link_sessionvalue);
                        link_sessionvalue = Double.valueOf(dx1);
                        linkedin_pagepersession_count.setText(link_sessionvalue.toString());

                        twitter_session_count.setText(twitter.get(0));
                        twitter_pageview_count.setText(twitterPageviwesArray.get(0));
                        //   twitter_avgsession_count.setText(twitterAvgSessionArray.get(0).substring(0,2));
                        Double twitter_sessionvalue = Double.valueOf(twitterpageview_persessionArray.get(0));
                        DecimalFormat df2 = new DecimalFormat("#.##");
                        String dx2 = df2.format(twitter_sessionvalue);
                        twitter_sessionvalue = Double.valueOf(dx2);
                        twitter_pagepersession_count.setText(twitter_sessionvalue.toString());


                       /* holder.cusweekFB.setText(fb.get(i - 1));
                        holder.cusweekLI.setText(link.get(i - 1));
                        holder.cusweekTW.setText(twitter.get(i - 1));
                        holder.custotal_count.setVisibility(View.GONE);*/


                        convertView.setVisibility(View.GONE);
                        //   holder.monthname1.setVisibility(View.VISIBLE);
                        //     holder.monthname1.setText(month.get(i - 1));

                        holder.reach_week_custom_layout.setVisibility(View.VISIBLE);
                        holder.reach_month_dynamiclayout.setVisibility(View.GONE);
                        holder.total_text.setVisibility(View.GONE);
                        holder.dummy_totalview.setVisibility(View.GONE);
                        holder.view3.setVisibility(View.GONE);

                        holder.reach_weekcustom_metrics_layout.setVisibility(View.VISIBLE);
                        holder.reach_total_txt.setVisibility(View.GONE);//changed now from visibility


                        convertView1.setVisibility(View.GONE);
                        holder.reach_week_custom_layout.addView(dynamic_weekcustom_view);
                        holder.reach_month_dynamiclayout.addView(convertView);
                    }
                } else {
                    for (int i = fb.size(); i > 0; i--) {
                        //size 3 change it according to requiremnt


                        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(
                                Context.LAYOUT_INFLATER_SERVICE);
                        View convertView = mInflater.inflate(R.layout.detailedstat_result, null,
                                false);
                        View convertView1 = mInflater.inflate(R.layout.reach_detailed_dynamic_monthname, null,
                                false);

                        // TextView site_names = (TextView) convertView.findViewById(R.id.site_names);
                        TextView facebook_count = (TextView) convertView.findViewById(R.id.facebook_count);
                        TextView linkedin_count = (TextView) convertView.findViewById(R.id.linkedin_count);
                        TextView twitter_count = (TextView) convertView.findViewById(R.id.twitter_count);
                        TextView reach_totalcount = (TextView) convertView.findViewById(R.id.total_counts);

                        TextView monthname = (TextView) convertView1.findViewById(R.id.month_name);
                        int reach_sum = Integer.parseInt(fb.get(i - 1)) + Integer.parseInt(link.get(i - 1))
                                + Integer.parseInt(twitter.get(i - 1));

                        facebook_count.setText(fb.get(i - 1));
                        linkedin_count.setText(link.get(i - 1));
                        twitter_count.setText(twitter.get(i - 1));
                        reach_totalcount.setText(String.valueOf(reach_sum));

                        holder.dummy_totalview.setVisibility(View.VISIBLE);
                        holder.total_text.setVisibility(View.VISIBLE);
                        holder.view3.setVisibility(View.VISIBLE);

                        holder.cusweekFB.setVisibility(View.INVISIBLE);
                        holder.cusweekLI.setVisibility(View.INVISIBLE);
                        holder.cusweekTW.setVisibility(View.INVISIBLE);

                        monthname.setText(month.get(i - 1));
                        //      holder.monthname1.setVisibility(View.GONE);

                        holder.reach_total_txt.setVisibility(View.VISIBLE);
                        holder.reach_txt_layout.setVisibility(View.VISIBLE);


                        convertView.setVisibility(View.VISIBLE);
                        holder.reach_weekcustom_metrics_layout.setVisibility(View.GONE);
                        holder.reach_month_dynamiclayout.setVisibility(View.VISIBLE);
                        holder.reach_week_custom_layout.setVisibility(View.GONE);
                        convertView1.setVisibility(View.VISIBLE);

                        holder.reach_month_dynamiclayout.addView(convertView);
                        holder.reach_dynamic_monthname.addView(convertView1);
                    }
                }

            }
        } else {
            Snackbar.make(viewHolder.itemView, "Lost Internet connection", Snackbar.LENGTH_LONG).show();
        }
    }

    String getMonthName(int monthNumber) {
        String[] months = new DateFormatSymbols().getMonths();
        int n = monthNumber - 1;
        return (n >= 0 && n <= 11) ? months[n] : "wrong number";
    }

    @Override
    public int getItemCount() {
        return mDataSetTypes.length;
    }

    @Override
    public int getItemViewType(int position) {
        return mDataSetTypes[position];
    }


   /* public class MyValueFormatter implements ValueFormatter {


        @Override
        public String getFormattedValue(float value) {
            return Math.round(value) + "";
        }
    }
*/

}

