package com.archilog.archi_log_demassue_piron.Presenter;

import android.content.Context;
import android.content.Intent;
import com.archilog.archi_log_demassue_piron.Model.HTTPRequests.LoginRequest;
import com.archilog.archi_log_demassue_piron.Views.MainPageActivity;
import com.archilog.archi_log_demassue_piron.Views.SignInActivity;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Observable;
import java.util.Observer;

public class SignInPresenter implements Observer {

    private Context context;
    private SignInActivity signInActivity;
    private LoginRequest loginRequest;

    public SignInPresenter(Context context, SignInActivity signInActivity) {
        this.context = context;
        this.signInActivity = signInActivity;
        this.loginRequest = new LoginRequest(context);
        this.loginRequest.addObserver(this);
    }

    public void login(String email, String password) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Email", email);
            jsonObject.put("Password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        loginRequest.loginRequest(jsonObject);
    }

    @Override
    public void update(Observable observable, Object o) {
        if(loginRequest.gotAnError() == true) {
            signInActivity.printToast("Connexion impossible. Veuillez réessayer plus tard.");
        } else {
            context.startActivity(new Intent(signInActivity, MainPageActivity.class));
        }
    }
}
