package com.example.epital.tablettestapplication.dashboard.DailyMeasurement.Navigation;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.epital.tablettestapplication.R;

import java.util.List;

/**
 * Created by oscarandersen on 04/10/14.
 */
public class DailyMeasurementListAdapter extends BaseAdapter {

    Context context;
    List<DailyMeasurementListItem> rowItem;

    public DailyMeasurementListAdapter(Context context, List<DailyMeasurementListItem> rowItem) {
        this.context = context;
        this.rowItem = rowItem;
    }

    @Override
    public int getCount() {
        return rowItem.size();
    }

    @Override
    public Object getItem(int i) {
        return rowItem.get(i);
    }

    @Override
    public long getItemId(int i) {
        return rowItem.indexOf(getItem(i));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.daily_measurement_list_item, null);
        }

        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.daily_measurement_list_item_icon);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.daily_measurement_list_item_title);

        DailyMeasurementListItem listItem = rowItem.get(position);
        imgIcon.setImageResource(listItem.getIcon());
        txtTitle.setText(listItem.getTitle());

        return convertView;
    }

    @Override
    public boolean isEnabled(int position) {
        //disable the list view (now its not clickable)
        return true;
    }

}
