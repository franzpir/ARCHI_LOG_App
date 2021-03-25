package com.archilog.archi_log_demassue_piron.Model.Utils;

import com.archilog.archi_log_demassue_piron.Model.House;
import com.archilog.archi_log_demassue_piron.Model.User;
import org.json.JSONException;
import org.json.JSONObject;

public class HouseParser {

    public static House getHouseFromJSONObject(JSONObject jsonObject) {
        House house = null;

        try {
            String street = jsonObject.getString("street");
            String number = jsonObject.getString("number");
            String locality = jsonObject.getString("locality");
            String country = jsonObject.getString("country");
            String houseId = jsonObject.getString("houseId");
            String postalCode = jsonObject.getString("postalCode");
            User owner = UserParser.parseUserFromJSONObject(jsonObject.getJSONObject("owner"));

            house = new House(houseId, country, locality, street, number, postalCode, owner);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return house;
    }
}
