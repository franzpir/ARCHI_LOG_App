package com.archilog.archi_log_demassue_piron.Model.Utils;

import com.archilog.archi_log_demassue_piron.Model.User;
import org.json.JSONException;
import org.json.JSONObject;

public class UserParser {
    public static User parseUserFromJSONObject(JSONObject jsonObject) {
        User user = null;

        try {
            String userId = jsonObject.getString("userId");
            String firstname = jsonObject.getString("firstName");
            String lastname = jsonObject.getString("lastName");
            String pictureLink = jsonObject.getString("pictureLink");

            user = new User(userId, firstname, lastname, pictureLink);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return user;
    }
}
