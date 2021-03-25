package com.archilog.archi_log_demassue_piron.Model.HTTPRequests;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.archilog.archi_log_demassue_piron.Model.MapObject;
import com.archilog.archi_log_demassue_piron.Model.Utils.HouseParser;
import com.archilog.archi_log_demassue_piron.Model.House;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

import static android.content.Context.MODE_PRIVATE;
import static com.archilog.archi_log_demassue_piron.Model.Constants.SHARED_PREFERENCES;

public class GetUserHousesAndServicingHousesRequest extends Observable {

    private static final String TAG = "UserHousesAndServicingHousesRequest/";

    private String url = "https://porthos-intra.cg.helmo.be/groupe20003/User/UserHousesAndServicingHouses/";
    private Context context;
    private List<MapObject> servicingHousesAsMapObjects;
    private List<House> servicingHousesAsHouses;
    private List<House> userHouses;

    public GetUserHousesAndServicingHousesRequest(Context context) {
        this.context = context;
        this.userHouses = new ArrayList<>();
        this.servicingHousesAsHouses = new ArrayList<>();
        this.servicingHousesAsMapObjects = new ArrayList<>();

        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        this.url += sharedPreferences.getString("userId", "test");
    }

    public void getUserHousesAndServicingHousesRequest() {
        Log.i(TAG + "getServicingHousesRequest", "Tentative d'envoi de la requête.");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            parseServicingHouses(response.getJSONArray("servicingHouses"));
                            parseUserHouses(response.getJSONArray("userHouses"));

                            Log.i(TAG + "getServicingHousesRequest", "Requête complète.");
                            setChanged();
                            notifyObservers();
                        } catch (JSONException e) {
                            Log.e(TAG + "getServicingHousesRequest", "Problème lors du parsing en JSON.");
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG + "getServicingHousesRequest", "Erreur lors de l'envoi de la requête : " + error.getMessage());
            }
        }
        ) {
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

    private void parseUserHouses(JSONArray userHouses) {
        for (int i = 0; i < userHouses.length(); i++) {
            JSONObject jsonObject = null;
            try {
                jsonObject = userHouses.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            House house = HouseParser.getHouseFromJSONObject(jsonObject);
            this.userHouses.add(house);
        }
    }

    private void parseServicingHouses(JSONArray servicingHouses) {
        for (int i = 0; i < servicingHouses.length(); i++) {
            JSONObject jsonObject = null;
            try {
                jsonObject = servicingHouses.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            House house = HouseParser.getHouseFromJSONObject(jsonObject);
            servicingHousesAsHouses.add(house);

            double[] coordinates = getCoordinatesFromAddress(house.getLongFormattedAddress());
            String userName = house.getUser().getFormattedName();

            servicingHousesAsMapObjects.add(new MapObject(userName, coordinates));
        }
    }

    private double[] getCoordinatesFromAddress(String address) {
        double[] coordinates = new double[2];
        Geocoder geocoder = new Geocoder(context);
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocationName(address, 1);
            if (addresses.size() > 0) {
                coordinates[0] = addresses.get(0).getLatitude();
                coordinates[1] = addresses.get(0).getLongitude();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return coordinates;
    }

    public List<MapObject> getServicingHousesAsMapObjects() {
        return servicingHousesAsMapObjects;
    }

    public List<House> getServicingHousesAsHouses() {
        return servicingHousesAsHouses;
    }

    public List<House> getUserHouses() {
        return userHouses;
    }
}