package com.archilog.archi_log_demassue_piron.Presenter;

import android.content.Context;
import android.content.Intent;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import com.archilog.archi_log_demassue_piron.Model.Adapter.CustomExpandableListAdapter;
import com.archilog.archi_log_demassue_piron.Model.ExpandableLists.ExpandableListForHouses;
import com.archilog.archi_log_demassue_piron.Model.House;
import com.archilog.archi_log_demassue_piron.Views.DetailHouseActivity;
import com.archilog.archi_log_demassue_piron.Views.HomeFragment;
import com.archilog.archi_log_demassue_piron.Views.InfosOfOwnHouseActivity;

import java.util.*;

public class HomeFragmentPresenter implements Observer {
    private Context context;
    private HomeFragment homeFragment;
    private ExpandableListView expandableListView;
    private List<String> expandableListTitle;
    private HashMap<String, List<House>> expandableListDetail;

    public HomeFragmentPresenter(Context context, HomeFragment homeFragment) {
        this.context = context;
        this.homeFragment = homeFragment;

        this.expandableListView = homeFragment.getExpandableListView();
        ExpandableListForHouses expandableListForHouses = new ExpandableListForHouses(context);
        expandableListForHouses.addObserver(this);
    }

    public void constructExpandableListView() {
        expandableListDetail = ExpandableListForHouses.getEstateForFirstPage();
        expandableListTitle = new ArrayList<>(expandableListDetail.keySet());
        ExpandableListAdapter expandableListAdapter = new CustomExpandableListAdapter<>(context, expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);

        this.homeFragment.setExpandableListView(expandableListView);
    }

    public void handleClick(int groupPosition, int childPosition) {
        Intent intent;
        if(groupPosition == 0) {
            intent = new Intent(context, InfosOfOwnHouseActivity.class);
        } else {
            intent = new Intent(context, DetailHouseActivity.class);
        }

        House currentHouse = expandableListDetail.get(expandableListTitle.get(groupPosition)).get(
                childPosition);
        intent.putExtra("houseId", currentHouse.getHouseId());
        if(groupPosition == 1) {
            intent.putExtra("isMyEstate", true);
        } else {
            intent.putExtra("isMyEstate", false);
        }

        context.startActivity(intent);
    }

    @Override
    public void update(Observable observable, Object o) {
        constructExpandableListView();
    }
}
