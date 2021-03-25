package com.archilog.archi_log_demassue_piron.Model.HTTPRequests;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.archilog.archi_log_demassue_piron.Model.User;
import com.archilog.archi_log_demassue_piron.Model.Utils.UserParser;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.*;

import static android.content.Context.MODE_PRIVATE;
import static com.archilog.archi_log_demassue_piron.Model.Constants.SHARED_PREFERENCES;

public class GetUserContactsRequest extends Observable {
    private static final String TAG = "ContactsRequest/";

    private boolean gotAnError;
    private List<User> contacts;
    private Context context;
    private String userId;
    private String url = "https://porthos-intra.cg.helmo.be/groupe20003/User/Contacts/";

    public GetUserContactsRequest(Context context) {
        this.context = context;
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        userId = sharedPreferences.getString("userId", "noUserId");
        this.url += userId;
        contacts = new ArrayList<>();
        gotAnError = false;
    }

    public void getUserContacts() {
        Log.i(TAG + "sendRequest", "Tentative d'envoi de la requête.");
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for(int i = 0; i < response.length(); i++) {
                                contacts.add(UserParser.parseUserFromJSONObject(response.getJSONObject(i)));
                            }

                            gotAnError = false;

                            Log.i(TAG + "sendRequest", "Requête complète.");
                            setChanged();
                            notifyObservers();
                        } catch (JSONException e) {
                            Log.e(TAG + "sendRequest", "Problème lors du parsing en JSON.");

                            gotAnError = true;

                            setChanged();
                            notifyObservers();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
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

    public List<User> getContacts() {
        return contacts;
    }

    public boolean gotAnError() {
        return gotAnError;
    }
}
