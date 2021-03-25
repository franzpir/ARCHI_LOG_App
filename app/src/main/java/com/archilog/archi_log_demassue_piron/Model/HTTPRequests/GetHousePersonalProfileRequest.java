package com.archilog.archi_log_demassue_piron.Model.HTTPRequests;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.archilog.archi_log_demassue_piron.Model.House;
import com.archilog.archi_log_demassue_piron.Model.User;
import com.archilog.archi_log_demassue_piron.Model.Utils.HouseParser;
import com.archilog.archi_log_demassue_piron.Model.Utils.UserParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

import static android.content.Context.MODE_PRIVATE;
import static com.archilog.archi_log_demassue_piron.Model.Constants.SHARED_PREFERENCES;

public class GetHousePersonalProfileRequest extends Observable {

    private static final String TAG = "PersonalProfileRequest/";

    private String url = "https://porthos-intra.cg.helmo.be/groupe20003/House/HousePersonalProfile/";
    private List<User> servants;
    private Context context;
    private House house;

    public GetHousePersonalProfileRequest(String houseId, Context context) {
        url += houseId;
        this.servants = new ArrayList<>();
        this.context = context;
    }

    public void getHousePersonalProfileRequest() {
        Log.i(TAG + "sendRequest", "Tentative d'envoi de la requête.");
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        house = HouseParser.getHouseFromJSONObject(response);
                        try {
                            JSONArray jsonArray = response.getJSONArray("servants");
                            for(int i = 0; i < jsonArray.length(); i++) {
                                User user = UserParser.parseUserFromJSONObject(jsonArray.getJSONObject(i));
                                servants.add(user);
                            }
                            Log.i(TAG + "sendRequest", "Requête complète.");

                            setChanged();
                            notifyObservers();

                        } catch (JSONException e) {
                            Log.e(TAG + "sendRequest", "Problème lors du parsing en JSON.");
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG + "sendRequest", "Erreur lors de l'envoi de la requête : " + error.getMessage());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json; charset=utf-8");
                SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
                String token = sharedPreferences.getString("token", "token");
                params.put("Authorization", "Bearer " + token);

                return params;
            }
        };

        HTTPSingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public List<User> getServants() {
        return this.servants;
    }

    public House getHouse() {
        return house;
    }
}
