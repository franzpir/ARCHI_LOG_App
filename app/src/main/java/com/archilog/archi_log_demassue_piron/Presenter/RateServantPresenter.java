package com.archilog.archi_log_demassue_piron.Presenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import com.archilog.archi_log_demassue_piron.Model.HTTPRequests.RateServantRequest;
import com.archilog.archi_log_demassue_piron.Model.User;
import com.archilog.archi_log_demassue_piron.Views.RateServantActivity;

import java.util.Observable;
import java.util.Observer;

import static android.content.Context.MODE_PRIVATE;
import static com.archilog.archi_log_demassue_piron.Model.Constants.SHARED_PREFERENCES;

public class RateServantPresenter implements Observer {
    private RateServantActivity rateServant;
    private Context context;
    private Intent intent;
    private User servant;
    private RateServantRequest rateServantRequest;

    public RateServantPresenter(Context context, RateServantActivity rateServant, Intent intent) {
        this.context = context;
        this.rateServant = rateServant;
        this.intent = intent;
        this.servant = getUserFromIntentExtras();
        this.rateServantRequest = new RateServantRequest(context);
        rateServantRequest.addObserver(this);

        rateServant.setTextView(servant.getFormattedName());
    }

    public void sendNote(int note, String description) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        String giverID = sharedPreferences.getString("userId", "test");
        String receiverID = servant.getUserId();

        rateServantRequest.sendNoteRequest(description, note, giverID, receiverID);
    }

    private User getUserFromIntentExtras() {
        String userId = intent.getStringExtra("userId");
        String firstName = intent.getStringExtra("firstname");
        String lastName = intent.getStringExtra("lastname");
        String pictureLink = intent.getStringExtra("pictureLink");

        return new User(userId, firstName, lastName, pictureLink);
    }

    @Override
    public void update(Observable observable, Object o) {
        if(rateServantRequest.gotAnError()) {
            rateServant.printToast("Erreur lors de l'envoi de la note.");
        } else {
            rateServant.printToast("Note envoyée !");
            rateServant.resetView();
        }
    }
}
