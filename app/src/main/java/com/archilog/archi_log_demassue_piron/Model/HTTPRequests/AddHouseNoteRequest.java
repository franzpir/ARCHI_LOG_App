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

public class AddHouseNoteRequest extends Observable {
    private static final String TAG = "HouseNoteRequest/";
    private String url = "https://porthos-intra.cg.helmo.be/groupe20003/HouseNote";
    private Context context;
    private boolean gotAnError;
    private int errorNumber;

    public AddHouseNoteRequest(Context context) {
        this.context = context;
        this.gotAnError = false;
    }

    public void sendNoteRequest(String houseId, String userId, String description, int note) {
        RequestQueue queue = Volley.newRequestQueue(context);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("HouseReceiverId", houseId);
            jsonObject.put("NoteGiverId", userId);
            jsonObject.put("Note", note);
            jsonObject.put("Description", description);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String requestBody = jsonObject.toString();
        Log.i(TAG + "sendRequest", "Tentative d'envoi de la requête.");
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
                        errorNumber = error.networkResponse.statusCode;
                        setChanged();
                        notifyObservers();

                    }
                }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
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

    public int getErrorNumber() {
        return errorNumber;
    }
}
