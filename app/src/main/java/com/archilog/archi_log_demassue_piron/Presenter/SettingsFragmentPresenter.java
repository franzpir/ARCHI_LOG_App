package com.archilog.archi_log_demassue_piron.Presenter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import com.archilog.archi_log_demassue_piron.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import static android.content.Context.MODE_PRIVATE;
import static com.archilog.archi_log_demassue_piron.Model.Constants.SHARED_PREFERENCES;

public class SettingsFragmentPresenter {
    private FirebaseAuth auth;
    private Context context;

    public SettingsFragmentPresenter(Context context) {
        this.auth = FirebaseAuth.getInstance();
        this.context = context;
    }

    public void logOut() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(context, gso);

        auth.signOut();

        googleSignInClient.signOut().addOnCompleteListener((Activity) context,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();

                        editor.clear();
                        editor.commit();
                    }
                });
    }
}
