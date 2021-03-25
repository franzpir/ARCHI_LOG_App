package com.archilog.archi_log_demassue_piron.Presenter;

import android.content.Context;
import android.content.Intent;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import com.archilog.archi_log_demassue_piron.Model.Adapter.CustomExpandableListAdapter;
import com.archilog.archi_log_demassue_piron.Model.ExpandableLists.ExpandableListForPassages;
import com.archilog.archi_log_demassue_piron.Model.ExpandableLists.ExpandableListForServants;
import com.archilog.archi_log_demassue_piron.Model.HTTPRequests.GetHousePersonalProfileRequest;
import com.archilog.archi_log_demassue_piron.Model.House;
import com.archilog.archi_log_demassue_piron.Model.Passage;
import com.archilog.archi_log_demassue_piron.Model.User;
import com.archilog.archi_log_demassue_piron.Views.InfosOfOwnHouseActivity;
import com.archilog.archi_log_demassue_piron.Views.RateServantActivity;

import java.util.*;

public class InfosOfOwnHousePresenter {
    private Context context;
    private String houseId;
    private House house;
    private List<User> servants;
    private GetHousePersonalProfileRequest getHousePersonalProfileRequest;
    private InfosOfOwnHouseActivity infosOfOwnHouseActivity;
    private ExpandableListView expandableListViewForPassages, expandableListViewForServants;

    public InfosOfOwnHousePresenter(Context context, String houseId, InfosOfOwnHouseActivity infosOfOwnHouseActivity) {
        this.context = context;
        this.houseId = houseId;
        servants = new ArrayList<>();
        getHousePersonalProfileRequest = new GetHousePersonalProfileRequest(houseId, context);
        getHousePersonalProfileRequest.addObserver(new GetHousePersonalProfileRequestObserver());
        getHousePersonalProfileRequest.getHousePersonalProfileRequest();
        this.infosOfOwnHouseActivity = infosOfOwnHouseActivity;

        this.expandableListViewForPassages = infosOfOwnHouseActivity.getExpandableListViewForPassages();
        ExpandableListForPassages expandableListForPassages = new ExpandableListForPassages(context, houseId);
        expandableListForPassages.addObserver(new GetPassagesForAHouseRequestObserver());

        this.expandableListViewForServants = infosOfOwnHouseActivity.getExpandableListViewForServants();
    }

    public House getHouse() {
        return house;
    }

    private class GetHousePersonalProfileRequestObserver implements Observer {
        @Override
        public void update(Observable observable, Object o) {
            servants = getHousePersonalProfileRequest.getServants();
            house = getHousePersonalProfileRequest.getHouse();
            infosOfOwnHouseActivity.setTextsForTheView(house.getFormattedName(), house.getUser().getFormattedName());
            constructExpandableListViewForServants();
        }
    }

    private class GetPassagesForAHouseRequestObserver implements Observer {
        @Override
        public void update(Observable observable, Object o) {
            constructExpandableListViewForPassages();
        }
    }

    public void constructExpandableListViewForPassages() {
        HashMap<String, List<Passage>> expandableListDetailForPassages = ExpandableListForPassages.getPassagesForAHouse();
        List<String> expandableListTitleForPassages = new ArrayList<>(expandableListDetailForPassages.keySet());
        ExpandableListAdapter expandableListAdapterForPassages = new CustomExpandableListAdapter(context, expandableListTitleForPassages, expandableListDetailForPassages);
        expandableListViewForPassages.setAdapter(expandableListAdapterForPassages);

        infosOfOwnHouseActivity.setExpandableListViewForPassages(expandableListViewForPassages);

    }

    public void constructExpandableListViewForServants() {
        ExpandableListForServants expandableListForServants = new ExpandableListForServants(context, servants);
        HashMap<String, List<User>> expandableListDetailForServants = ExpandableListForServants.getServantsFromHouse();
        List<String> expandableListTitleForServants = new ArrayList<>(expandableListDetailForServants.keySet());
        ExpandableListAdapter expandableListAdapterForServants = new CustomExpandableListAdapter(context, expandableListTitleForServants, expandableListDetailForServants);
        expandableListViewForServants.setAdapter(expandableListAdapterForServants);

        infosOfOwnHouseActivity.setExpandableListViewForServants(expandableListViewForServants);
    }

    public void handleClick(int childPosition) {
        User user = servants.get(childPosition);
        Intent intent = new Intent(context, RateServantActivity.class);
        intent.putExtra("userId", user.getUserId());
        intent.putExtra("firstname", user.getFirstname());
        intent.putExtra("lastname", user.getLastname());
        intent.putExtra("pictureLink", user.getPictureLink());

        context.startActivity(intent);
    }
}
