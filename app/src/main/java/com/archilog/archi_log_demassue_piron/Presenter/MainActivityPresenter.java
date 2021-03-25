package com.archilog.archi_log_demassue_piron.Presenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import com.archilog.archi_log_demassue_piron.Model.HTTPRequests.GoogleAuthenticateRequest;
import com.archilog.archi_log_demassue_piron.R;
import com.archilog.archi_log_demassue_piron.Views.MainActivity;
import com.archilog.archi_log_demassue_piron.Views.MainPageActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

import static android.content.Context.MODE_PRIVATE;
import static com.archilog.archi_log_demassue_piron.Model.Constants.RC_SIGN_IN;
import static com.archilog.archi_log_demassue_piron.Model.Constants.SHARED_PREFERENCES;
import static com.archilog.archi_log_demassue_piron.Model.DateUtils.parseDate;

public class MainActivityPresenter implements Observer {
    private Context context;
    private MainActivity mainActivity;
    private GoogleAuthenticateRequest googleAuthenticateRequest;

    private static GoogleSignInOptions gso;
    private static GoogleSignInClient googleSignInClient;
    private static FirebaseAuth auth;
    private FirebaseUser firebaseUser;

    public MainActivityPresenter(Context context, MainActivity mainActivity) {
        this.context = context;
        this.mainActivity = mainActivity;

        this.auth = FirebaseAuth.getInstance();
        this.firebaseUser = auth.getCurrentUser();
        this.gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        this.googleSignInClient = GoogleSignIn.getClient(context, gso);
        this.googleAuthenticateRequest = new GoogleAuthenticateRequest(context);
        this.googleAuthenticateRequest.addObserver(this);

        if(isConnectionStillCorrect()) {
            startActivity(MainPageActivity.class);
        }

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(context);
    }

    public static GoogleSignInClient getGoogleSignInClient() {
        return googleSignInClient;
    }

    public boolean isConnectionStillCorrect() {
        if(firebaseUser == null || checkIfTokenIsExpired()) {
            return false;
        } else {
            return true;
        }
    }

    private boolean checkIfTokenIsExpired() {
        boolean isExpired = false;
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        String expires = sharedPreferences.getString("expires", "neverExpires");

        if(!expires.equals("neverExpires")) {
            if(compareTokenDateAndLocalDateTime(parseDate(expires))) {
                isExpired = true;
            }
        }

        return isExpired;
    }

    private boolean compareTokenDateAndLocalDateTime(String tokenDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date dateOfTheDay = new Date();
        Date tokenExpires = null;
        try {
            tokenExpires = formatter.parse(tokenDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateOfTheDay.after(tokenExpires);
    }

    public void firebaseAuthWithGoogle(final String idToken) {
        final AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(mainActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String tokenId = "{\"tokenId\":\"" + idToken + "\"}";
                            JSONObject postParameters = null;

                            try {
                                postParameters = new JSONObject(tokenId);
                            } catch (JSONException ex) {
                                System.err.println("Impossible d'ajouter le token.");
                                ex.printStackTrace();
                            }

                            googleAuthenticateRequest.googleAuthenticateRequest(postParameters);
                        }
                    }
                });
    }

    @Override
    public void update(Observable observable, Object o) {
        startActivity(MainPageActivity.class);
    }

    public void startActivity(Class toStart) {
        Intent intent = new Intent(context, toStart);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        mainActivity.startActivity(intent);
        mainActivity.finish();
    }

    public void checkIfConnectionIsPossible(int requestCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                mainActivity.printToast("Connexion impossible. Veuillez réessayer plus tard.");
            }
        }
    }
}
