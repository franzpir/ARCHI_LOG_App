package com.archilog.archi_log_demassue_piron.Model.ExpandableLists;

import android.content.Context;
import com.archilog.archi_log_demassue_piron.Model.HTTPRequests.GetAllPassagesRequest;
import com.archilog.archi_log_demassue_piron.Model.Passage;

import java.util.*;

public class ExpandableListForPassages extends Observable implements Observer  {
    private static List<Passage> listOfVisits;
    private static Context context;
    private static GetAllPassagesRequest getAllPassagesRequest;

    public ExpandableListForPassages(Context context, String houseId) {
        this.listOfVisits = new ArrayList<>();
        this.context = context;
        this.getAllPassagesRequest  = new GetAllPassagesRequest(context, houseId);
        this.getAllPassagesRequest.addObserver(this);

        getAllPassagesRequest.sendRequestGetAllPassages();
    }

    public static HashMap<String, List<Passage>> getPassagesForAHouse() {
        final HashMap<String, List<Passage>> expandableListDetail = new HashMap<String, List<Passage>>();
        expandableListDetail.put("Liste des passages", listOfVisits);

        return expandableListDetail;
    }

    @Override
    public void update(Observable observable, Object o) {
        listOfVisits = getAllPassagesRequest.getPassages();

        setChanged();
        notifyObservers();
    }
}
