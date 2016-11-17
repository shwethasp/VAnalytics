package google.shwethasp.com.analytics_google.adapter;

import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import google.shwethasp.com.analytics_google.R;
import google.shwethasp.com.analytics_google.WebsiteModel;
import google.shwethasp.com.analytics_google.activity.BaseLoginActivity;
import google.shwethasp.com.analytics_google.activity.WebSitePage;

/**
 * Created by shwethap on 03-10-2016.
 */
public class Website_CustomAdapter extends RecyclerView.Adapter<Website_CustomAdapter.DataObjectHolder> implements Filterable {
    private static String LOG_TAG = "MyRecyclerViewAdapter";
    /*  private ArrayList<String> websitenames;
      private ArrayList<String> response_timeZoneArrays;
      private ArrayList<String> WebsiteTypeNames;*/
    private ArrayList<WebsiteModel> mArrayListModel;
    private ArrayList<WebsiteModel> orgData;
    private MyClickListener myClickListener;
    ContactsFilter mContactsFilter;

    @Override
    public Filter getFilter() {

        if (mContactsFilter == null)
            mContactsFilter = new ContactsFilter(this, mArrayListModel);

        return mContactsFilter;
    }

    // Filter

    private class ContactsFilter extends Filter {

        private Website_CustomAdapter adapter;

        private ArrayList<WebsiteModel> filteredData;

        public ContactsFilter(Website_CustomAdapter customAdapter, ArrayList<WebsiteModel> websiteModels) {
            adapter = customAdapter;
            orgData = websiteModels;
            filteredData = new ArrayList<WebsiteModel>();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            /*// Create a FilterResults object
            FilterResults results = new FilterResults();

            // If the constraint (search string/pattern) is null
            // or its length is 0, i.e., its empty then
            // we just set the `values` property to the
            // original contacts list which contains all of them
            if (constraint == null || constraint.length() == 0) {
                results.values = WebSitePage.modelArrayList;
                results.count = WebSitePage.modelArrayList.size();
            } else {
                // Some search copnstraint has been passed
                // so let's filter accordingly
                ArrayList<WebsiteModel> filteredContacts = new ArrayList<WebsiteModel>();

                // We'll go through all the contacts and see
                // if they contain the supplied string

                for (WebsiteModel websiteModel : mArrayListModel) {
                    if (websiteModel.getWebSiteName().contains(constraint.toString())) {
                        // if `contains` == true then add it
                        // to our filtered list
                        filteredContacts.add(websiteModel);
                    }
                    // Finally set the filtered values and size/count
                    results.values = filteredContacts;
                    results.count = filteredContacts.size();

                }
                if (filteredContacts.size() == 0) {
                    // Finally set the filtered values and size/count
                    results.values = filteredContacts;
                    results.count = filteredContacts.size();
                }
            }
            // Return our FilterResults object
            return results;*/
            filteredData.clear();
            final FilterResults results = new FilterResults();

            Log.d("performFiltering: ", constraint.toString());

            if (TextUtils.isEmpty(constraint.toString())) {
                filteredData.addAll(orgData);
            } else {
                final String filterPattern = constraint.toString().toLowerCase().trim();

                for (final WebsiteModel user : orgData) {
                    // set condition for filter here
                    if (user.getWebSiteName().toLowerCase().contains(filterPattern)) {
                        filteredData.add(user);
                    }
                }
            }

            results.values = filteredData;
            results.count = filteredData.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mArrayListModel = (ArrayList<WebsiteModel>) results.values;
            notifyDataSetChanged();
            //listAdapter.getData().clear();

        }

    }

    public class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView label, webtypename, timezone;
        ImageView globe, dropimage;
        TextView dateTime;
        View viewline;
        int count = 0;

        public DataObjectHolder(View itemView) {
            super(itemView);
            label = (TextView) itemView.findViewById(R.id.website_name);

            RelativeLayout dropdown_arrow = (RelativeLayout) itemView.findViewById(R.id.dropdownarrow);
            dropimage = (ImageView) itemView.findViewById(R.id.dropdownimage);
            webtypename = (TextView) itemView.findViewById(R.id.webtype_name);
            viewline = (View) itemView.findViewById(R.id.viewline);
            timezone = (TextView) itemView.findViewById(R.id.timezone);
            globe = (ImageView) itemView.findViewById(R.id.image_globe);

            final LinearLayout mLinearLayout = (LinearLayout) itemView.findViewById(R.id.alldata_layout);
            dropdown_arrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (count == 0) {
                        mLinearLayout.setVisibility(View.GONE);
                        viewline.setVisibility(View.GONE);
                        dropimage.setImageResource(R.drawable.dropleft);
                        count++;
                    } else {
                        mLinearLayout.setVisibility(View.VISIBLE);
                        viewline.setVisibility(View.VISIBLE);
                        dropimage.setImageResource(R.drawable.orange_dropdown_arrow);
                        count = 0;
                    }
                }
            });
            Log.i(LOG_TAG, "Adding Listener");
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.i(LOG_TAG, "adapter name " + mArrayListModel.get(getAdapterPosition()).getWebSiteName());
            Log.i(LOG_TAG, "adapter id" + mArrayListModel.get(getAdapterPosition()).getWebSiteId());
            for (int i = 0; i < orgData.size(); i++) {
                if (orgData.get(i).getWebSiteId().contentEquals(mArrayListModel.get(getAdapterPosition()).getWebSiteId())) {
                    WebSitePage.website_clickedposition = i;
                    Log.i(LOG_TAG, "Org position " +  WebSitePage.website_clickedposition );
                }
            }
            myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public Website_CustomAdapter(ArrayList<WebsiteModel> websiteModels) {
       /* ,ArrayList<String>WebTimezone*/
       /* websitenames = websitename;
        WebsiteTypeNames = WebsiteTypeName;
        response_timeZoneArrays = response_timeZoneArray;*/
        //  WebTimezones=WebTimezone;

        this.mArrayListModel = websiteModels;
        this.orgData = websiteModels;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.websitepage_card_view, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.label.setText(mArrayListModel.get(position).getWebSiteName());
        holder.webtypename.setText(mArrayListModel.get(position).getWebSiteType());
        holder.timezone.setText("TimeZone: " + mArrayListModel.get(position).getTimeZone());

        if (holder.webtypename.getText().toString().contentEquals("All Web Site Data")) {
            holder.globe.setImageResource(R.drawable.ic_action_globe);
        } else {
            holder.globe.setImageResource(R.drawable.ic_action_phone);
        }

    }

    /*public void addItem(DataObject dataObj, int index) {
        mDataset.add(index, dataObj);
        notifyItemInserted(index);
    }*/

    public void deleteItem(int index) {
        mArrayListModel.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mArrayListModel.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}
