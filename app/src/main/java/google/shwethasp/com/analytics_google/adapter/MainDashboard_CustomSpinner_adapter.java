package google.shwethasp.com.analytics_google.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import google.shwethasp.com.analytics_google.R;
import google.shwethasp.com.analytics_google.activity.WebSitePage;

/**
 * Created by shwethap on 05-10-2016.
 */
public class MainDashboard_CustomSpinner_adapter extends BaseAdapter {

    private Activity activity;
    ArrayList<String> WebsiteName, Websitetypename;
    public Resources res;
    LayoutInflater inflater;
    Context context;


    /*************
     * CustomAdapter Constructor
     *****************/
    public MainDashboard_CustomSpinner_adapter(Context applicationContext,
                                               ArrayList<String> WebsiteName, ArrayList<String> Websitetypename) {
//        super(activitySpinner, textViewResourceId, WebsiteName,WebsiteType,Websitetypename);

        /********** Take passed values **********/
        this.context = applicationContext;
        this.WebsiteName = WebsiteName;

        this.Websitetypename = Websitetypename;
//        res      = resLocal;

        /***********  Layout inflator to call external xml layout () **********************/
        //   inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater = (LayoutInflater.from(applicationContext));

    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View row = inflater.inflate(R.layout.simple_list_item_1, parent, false);
           /* View v = convertView;
            if (v == null) {
                Context mContext = this.getContext();
                LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.row, null);
            }

            TextView tv = (TextView) v.findViewById(R.id.spinnerTarget);
            tv.setText(testarray.get(position));
            tv.setTextColor(Color.BLACK);*/
        TextView website_name = (TextView) row.findViewById(R.id.websitename);
        TextView website_typename = (TextView) row.findViewById(R.id.website_type);//check indexout of bound exception
        website_name.setTextColor(Color.BLACK);
        website_typename.setTextColor(Color.BLACK);
       /* website_name.setEllipsize(TextUtils.TruncateAt.END);
        website_name.setSingleLine(true);*/
        ImageView globe = (ImageView) row.findViewById(R.id.globe_image);
       /* *//*if (website_name.getWidth()>80) {*//*
            website_name.setSingleLine();
            website_name.setEllipsize(TextUtils.TruncateAt.END);
       *//* } else {

        }*/
        globe.setVisibility(View.VISIBLE);
        LinearLayout linearLayout = (LinearLayout) row.findViewById(R.id.spinnerItemLayout);
        if (WebSitePage.website_clickedposition == position) {
            website_name.setTextColor(context.getResources().getColor(R.color.white));
            website_typename.setTextColor(context.getResources().getColor(R.color.white));
            linearLayout.setBackgroundColor(context.getResources().getColor(R.color.background_color));
        } else {
            /*website_name.setTextColor(context.getResources().getColor(R.color.black));
            website_typename.setTextColor(context.getResources().getColor(R.color.black));
            linearLayout.setBackgroundColor(context.getResources().getColor(R.color.white));*/
        }

        // Default selected Spinner item
        website_name.setText(WebsiteName.get(position));
        website_typename.setText(Websitetypename.get(position));
        if (website_typename.getText().toString().contentEquals("All Web Site Data")) {
            globe.setImageResource(R.drawable.ic_action_globe);
               /* globe.setImageResource(res.getIdentifier
                        ("com.androidexample.customspinner:drawable/"
                                + tempValues.getImage(), null, null));*/
        } else {
            globe.setImageResource(R.drawable.ic_action_phone);
        }

        return row;
    }

    @Override
    public int getCount() {
        return WebsiteName.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        /********** Inflate spinner_rows.xml file for each row ( Defined below ) ************/
        View row = inflater.inflate(R.layout.simple_list_item_1, parent, false);

        /***** Get each Model object from Arraylist ********/


        TextView website_name = (TextView) row.findViewById(R.id.websitename);
        TextView website_typename = (TextView) row.findViewById(R.id.website_type);

        ImageView globe = (ImageView) row.findViewById(R.id.globe_image);
        LinearLayout linearLayout = (LinearLayout) row.findViewById(R.id.spinnerItemLayout);
        if (WebSitePage.website_clickedposition == position) {
            website_name.setTextColor(context.getResources().getColor(R.color.white));
            website_typename.setTextColor(context.getResources().getColor(R.color.white));
            linearLayout.setBackgroundColor(context.getResources().getColor(R.color.background_color));
            website_name.setEllipsize(TextUtils.TruncateAt.END);
            website_name.setSingleLine(true);
        } else {
            /*website_name.setTextColor(context.getResources().getColor(R.color.black));
            website_typename.setTextColor(context.getResources().getColor(R.color.black));
            linearLayout.setBackgroundColor(context.getResources().getColor(R.color.white));*/
        }

        // Default selected Spinner item
        website_name.setText(WebsiteName.get(position));
        website_typename.setText(Websitetypename.get(position));
        if (website_typename.getText().toString().contentEquals("All Web Site Data")) {
            globe.setImageResource(R.drawable.ic_action_globe);
               /* globe.setImageResource(res.getIdentifier
                        ("com.androidexample.customspinner:drawable/"
                                + tempValues.getImage(), null, null));*/
        } else {
            globe.setImageResource(R.drawable.ic_action_phone);
        }


        return row;
    }

   /* // This funtion called for each row ( Called data.size() times )
    public View getCustomView(int position, View convertView, ViewGroup parent) {

        *//********** Inflate spinner_rows.xml file for each row ( Defined below ) ************//*
        View row = inflater.inflate(R.layout.simple_list_item_1, parent, false);

        *//***** Get each Model object from Arraylist ********//*
        tempValues = null;
        tempValues = (SpinnerModel) data.get(position);

        TextView website_name        = (TextView)row.findViewById(R.id.websitename);
        TextView website_type          = (TextView)row.findViewById(R.id.website_type);

            ImageView globe = (ImageView) row.findViewById(R.id.globe_image);

        if(position==0){

            // Default selected Spinner item
            website_name.setText("Please select company");
            website_type.setText("");
            if(website_type.getText()=="WEB") {
                globe.setImageResource(res.getIdentifier
                        ("com.androidexample.customspinner:drawable/"
                                + tempValues.getImage(),null,null));
            }else{
                globe.setImageResource(res.getIdentifier
                        ("com.androidexample.customspinner:drawable/"
                                + tempValues.getImage(),null,null));
            }
        }
        else
        {
           *//* // Set values for spinner each row
            website_name.setText(tempValues.getCompanyName());
            website_type.setText(tempValues.getUrl());
            globe.setImageResource(res.getIdentifier
                    ("com.androidexample.customspinner:drawable/"
                            + tempValues.getImage(),null,null));
*//*
        }

        return row;
    }*/

}
