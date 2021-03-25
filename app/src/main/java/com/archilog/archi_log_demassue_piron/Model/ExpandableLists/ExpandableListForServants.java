package com.archilog.archi_log_demassue_piron.Model.ExpandableLists;

import android.content.Context;
import com.archilog.archi_log_demassue_piron.Model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Observable;

public class ExpandableListForServants extends Observable {
    private static List<User> servants;
    private static Context context;

    public ExpandableListForServants(Context context, List<User> servants) {
        this.context = context;
        this.servants = servants;
    }

    public static HashMap<String, List<User>> getServantsFromHouse() {
        final HashMap<String, List<User>> expandableListDetails = new HashMap<>();
        expandableListDetails.put("Liste des personnes qui s'occupent de votre bien", servants);

        return expandableListDetails;
    }

    public static List<User> getServants() {
        return servants;
    }
}
