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
import com.archilog.archi_log_demassue_piron.Model.Utils.HouseParser;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import static android.content.Context.MODE_PRIVATE;
import static com.archilog.archi_log_demassue_piron.Model.Constants.SHARED_PREFERENCES;

public class GetHousePublicProfileRequest extends Observable {
    private static final String TAG = "PublicProfileRequest/";

    private Context context;
    private String url = "https://porthos-intra.cg.helmo.be/groupe20003/House/HousePublicProfile/";
    private House house;
    private String instructions, pictureURL;

    public GetHousePublicProfileRequest(Context context, String houseId) {
        this.context = context;
        this.url += houseId;
    }

    public void getHousePublicProfileRequest() {
        Log.i(TAG + "sendRequest", "Tentative d'envoi de la requête.");
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        parseHouseFromJSONObject(response);

                        Log.i(TAG + "sendRequest", "Requête complète.");
                        setChanged();
                        notifyObservers();
                    }
                }, new Response.ErrorListener() {
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

    private void parseHouseFromJSONObject(JSONObject jsonObject) {
        try {
            house = HouseParser.getHouseFromJSONObject(jsonObject);

            instructions = jsonObject.getString("instructions");
            pictureURL = jsonObject.getString("pictureUrl");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getInstructions() {
        return instructions;
    }

    public String getPictureURL() {
        return pictureURL;
    }

    public House getHouse() {
        return house;
    }
}
