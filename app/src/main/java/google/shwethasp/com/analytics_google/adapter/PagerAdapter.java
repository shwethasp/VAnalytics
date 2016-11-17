package google.shwethasp.com.analytics_google.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import google.shwethasp.com.analytics_google.activity.MainDashboard;
import google.shwethasp.com.analytics_google.fragment.ReachFragment;
import google.shwethasp.com.analytics_google.fragment.VisitorsFragment;

/**
 * Created by Pavan.Chunchula on 28-09-2016.
 */
public class PagerAdapter extends FragmentStatePagerAdapter {

    //integer to count number of tabs
    int tabCount;


    SharedPreferences sharedpreferences;
    Context mContext;


    //Constructor to the class

    public PagerAdapter(FragmentManager fm, int tabCount) {

        super(fm);
        //Initializing tab count
        this.tabCount = tabCount;


    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                VisitorsFragment tab2 = new VisitorsFragment();
                Bundle bundle1 = new Bundle();
                bundle1.putInt("monthcount", MainDashboard.adpMyMessageCount);

                bundle1.putString("start_date", MainDashboard.adpStartDate);
                bundle1.putString("end_date", MainDashboard.adpEndDate);
                bundle1.putString("ID", MainDashboard.adpId);

               /* sharedpreferences =mContext.getSharedPreferences("mypreference", Context.MODE_PRIVATE);
                SharedPreferences.Editor visitor_calcount = sharedpreferences.edit();
                visitor_calcount.putInt("count", intcount);
                visitor_calcount.commit();*/
                // set Fragmentclass Arguments
                tab2.setArguments(bundle1);

                return tab2;
            case 1:
                ReachFragment tab1 = new ReachFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("monthcount", MainDashboard.adpMyMessageCount);

                bundle.putString("start_date", MainDashboard.adpStartDate);
                bundle.putString("end_date", MainDashboard.adpEndDate);
                bundle.putString("ID", MainDashboard.adpId);
                /*sharedpreferences =mContext.getSharedPreferences("mypreference", Context.MODE_PRIVATE);
                SharedPreferences.Editor calcount = sharedpreferences.edit();
                calcount.putInt("count", intcount);
                calcount.commit();*/
                // set Fragmentclass Arguments
                tab1.setArguments(bundle);
                return tab1;

            default:
                return null;
        }
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return tabCount;
    }

    public CharSequence getPageTitle(int position) {
//        return super.getPageTitle(position);
        switch (position) {
            case 0:
                return "Visitors";
            case 1:
                return "Reach";
        }
        return null;
    }

    @Override
    public int getItemPosition(Object object){
        return PagerAdapter.POSITION_NONE;
    }
}
