package com.archilog.archi_log_demassue_piron.Model.HTTPRequests;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.archilog.archi_log_demassue_piron.Model.Passage;
import com.archilog.archi_log_demassue_piron.Model.Utils.PassageParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

import static android.content.Context.MODE_PRIVATE;
import static com.archilog.archi_log_demassue_piron.Model.Constants.SHARED_PREFERENCES;

public class GetAllPassagesRequest extends Observable {
    private static final String TAG = "AllPassages/";

    private String url = "https://porthos-intra.cg.helmo.be/groupe20003/Passage/";
    private Context context;
    private List<Passage> passages;

    public GetAllPassagesRequest(Context context, String houseId) {
        this.passages = new ArrayList<>();
        this.context = context;
        this.url += houseId;
    }

    public void sendRequestGetAllPassages() {
        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for(int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                Passage passage = PassageParser.parsePassageFromJSONObject(jsonObject);
                                passages.add(passage);
                            }

                            Log.i(TAG + "sendRequest", "Requête complète.");

                            setChanged();
                            notifyObservers();

                        } catch (JSONException e) {
                            Log.e(TAG + "sendRequest", "Problème lors du parsing en JSON.");
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG + "sendRequest", "Erreur lors de l'envoi de la requête : " + error.getMessage());
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

        HTTPSingleton.getInstance(context).addToRequestQueue(jsonArrayRequest);
    }

    public List<Passage> getPassages() {
        return this.passages;
    }
}
