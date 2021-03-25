package com.archilog.archi_log_demassue_piron.Model.HTTPRequests;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import static android.content.Context.MODE_PRIVATE;
import static com.archilog.archi_log_demassue_piron.Model.Constants.SHARED_PREFERENCES;

public class GoogleAuthenticateRequest extends Observable {

    private static final String TAG = "GoogleAuthenticateRequest/";
    private Context context;

    public GoogleAuthenticateRequest(Context context) {
        this.context = context;
    }

    public void googleAuthenticateRequest(JSONObject postParameters) {
        Log.i(TAG + "sendRequest", "Tentative d'envoi de la requête.");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                "https://porthos-intra.cg.helmo.be/groupe20003/Account/GoogleAuthenticate",
                postParameters,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String userId = response.getString("userId");
                            String email = response.getString("email");
                            String token = response.getString("token");
                            String expires = response.getString("expires");

                            setSharedPreferences(userId, email, token, expires);

                            Log.i(TAG + "sendRequest", "Requête complète.");

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

    private void setSharedPreferences(String userId, String email, String token, String expires) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("userId", userId);
        editor.putString("email", email);
        editor.putString("token", token);
        editor.putString("expires", expires);
        editor.commit();
    }
}
