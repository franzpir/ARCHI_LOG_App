package com.archilog.archi_log_demassue_piron.Model.HTTPRequests;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.android.volley.*;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import static android.content.Context.MODE_PRIVATE;
import static com.archilog.archi_log_demassue_piron.Model.Constants.SHARED_PREFERENCES;

public class AddPassageRequest extends Observable {
    private static final String TAG = "PassageRequest/";

    private Context context;
    private String url = "https://porthos-intra.cg.helmo.be/groupe20003/Passage";
    private boolean gotAnError;

    public AddPassageRequest(Context context) {
        this.context = context;
        this.gotAnError = false;
    }

    public void sendPassageRequest(String description, String houseId, String userId) {
        RequestQueue queue = Volley.newRequestQueue(context);
        JSONObject passage = new JSONObject();
        try {
            passage.put("UserId", userId);
            passage.put("HouseId", houseId);
            passage.put("Description", description);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String requestBody = passage.toString();

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(TAG + "sendRequest", "Requête complète.");
                        gotAnError = false;
                        setChanged();
                        notifyObservers();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG + "sendRequest", "Erreur lors de l'envoi de la requête : " + error.getMessage());
                        gotAnError = true;
                    }
                }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    return null;
                }
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String responseString = "";
                if (response != null) {
                    responseString = String.valueOf(response.statusCode);
                }
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }

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

        queue.add(request);

    }

    public boolean gotAnError() {
        return gotAnError;
    }
}
