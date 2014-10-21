package com.example.epital.tablettestapplication.dashboard.DailyMeasurement.Navigation;

import android.app.ListFragment;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.example.epital.tablettestapplication.R;
import com.example.epital.tablettestapplication.dashboard.DailyMeasurement.DailyMeasurementFragmentCommunication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oscarandersen on 03/10/14.
 */

public class DailyMeasurementListFragment extends ListFragment implements AdapterView.OnItemClickListener {

    String[] menuTitles;
    TypedArray menuIcons;
    DailyMeasurementListAdapter adapter;
    private List<DailyMeasurementListItem> rowItems;
    DailyMeasurementFragmentCommunication comm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.daily_measurement_list, container, false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        comm = (DailyMeasurementFragmentCommunication) getActivity();

        menuTitles = getResources().getStringArray(R.array.titles);
        menuIcons = getResources().obtainTypedArray(R.array.icons);

        rowItems = new ArrayList<DailyMeasurementListItem>();
        for (int i = 0; i < menuTitles.length; i++) {
            DailyMeasurementListItem item = new DailyMeasurementListItem(menuTitles[i], menuIcons.getResourceId(i, -1));
            rowItems.add(item);
        }
        adapter = new DailyMeasurementListAdapter(getActivity(), rowItems);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        //Toast.makeText(getActivity(), menuTitles[position], Toast.LENGTH_SHORT).show();

        //comm.changeDailyMeasurementContent(position);
    }

    public void focus(int position){
        //getListView().setSelection(position);
    }

    /*
        ADAPTER:
        An Adapter object acts as a bridge between an AdapterView and the underlying data for that view.
        The Adapter provides access to the data items.
        The Adapter is also responsible for making a View for each item in the data set.
        CURSOR:
        This interface provides random read-write access to the result set returned by a database query.
     */
}
