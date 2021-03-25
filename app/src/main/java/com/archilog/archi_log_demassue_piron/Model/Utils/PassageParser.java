package com.archilog.archi_log_demassue_piron.Model.Utils;

import com.archilog.archi_log_demassue_piron.Model.Passage;
import com.archilog.archi_log_demassue_piron.Model.User;
import org.json.JSONException;
import org.json.JSONObject;

public class PassageParser {
    public static Passage parsePassageFromJSONObject(JSONObject jsonObject) {
        Passage passage = null;

        try {
            User user = UserParser.parseUserFromJSONObject(jsonObject.getJSONObject("user"));
            String houseId = jsonObject.getString("housePassedId");
            String description = jsonObject.getString("description");
            String date = jsonObject.getString("date");

            passage = new Passage(date, houseId, user, description);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return passage;

    }
}
