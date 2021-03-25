package com.archilog.archi_log_demassue_piron.Model.ExpandableLists;

import android.content.Context;
import com.archilog.archi_log_demassue_piron.Model.HTTPRequests.GetUserHousesAndServicingHousesRequest;
import com.archilog.archi_log_demassue_piron.Model.House;

import java.util.*;

public class ExpandableListForHouses extends Observable implements Observer {
    private static List<House> myEstate;
    private static List<House> estateFromOthers;
    private static GetUserHousesAndServicingHousesRequest getUserHousesAndServicingHousesRequest;

    public ExpandableListForHouses(Context context) {
        myEstate = new ArrayList<>();
        estateFromOthers = new ArrayList<>();

        getUserHousesAndServicingHousesRequest = new GetUserHousesAndServicingHousesRequest(context);
        getUserHousesAndServicingHousesRequest.addObserver(this);

        getUserHousesAndServicingHousesRequest.getUserHousesAndServicingHousesRequest();
    }

    public static HashMap<String, List<House>> getEstateForFirstPage() {
        final HashMap<String, List<House>> expandableListDetail = new HashMap<String, List<House>>();

        expandableListDetail.put("Mes biens", myEstate);
        expandableListDetail.put("Les biens dont je m'occupe", estateFromOthers);

        return expandableListDetail;
    }

    @Override
    public void update(Observable observable, Object o) {
        myEstate = getUserHousesAndServicingHousesRequest.getUserHouses();
        estateFromOthers = getUserHousesAndServicingHousesRequest.getServicingHousesAsHouses();

        setChanged();
        notifyObservers();
    }
}
