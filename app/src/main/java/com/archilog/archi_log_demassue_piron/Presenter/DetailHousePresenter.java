package com.archilog.archi_log_demassue_piron.Presenter;

import android.content.Context;
import android.content.SharedPreferences;
import com.archilog.archi_log_demassue_piron.Model.HTTPRequests.AddHouseNoteRequest;
import com.archilog.archi_log_demassue_piron.Model.HTTPRequests.AddPassageRequest;
import com.archilog.archi_log_demassue_piron.Model.HTTPRequests.GetHousePublicProfileRequest;
import com.archilog.archi_log_demassue_piron.Model.House;
import com.archilog.archi_log_demassue_piron.Model.Localisation.Localisation;
import com.archilog.archi_log_demassue_piron.Views.DetailHouseActivity;

import java.util.Observable;
import java.util.Observer;

import static android.content.Context.MODE_PRIVATE;
import static com.archilog.archi_log_demassue_piron.Model.Constants.SHARED_PREFERENCES;

public class DetailHousePresenter {

    private Context context;
    private DetailHouseActivity detailHouseActivity;
    private House house;
    private GetHousePublicProfileRequest getHousePublicProfileRequest;
    private AddPassageRequest addPassageRequest;
    private AddHouseNoteRequest addHouseNoteRequest;

    public DetailHousePresenter(Context context, DetailHouseActivity detailHouseActivity, String houseId) {
        this.context = context;
        this.detailHouseActivity = detailHouseActivity;
        this.getHousePublicProfileRequest = new GetHousePublicProfileRequest(context, houseId);
        this.getHousePublicProfileRequest.addObserver(new GetHousePublicProfileRequestObserver());
        this.addPassageRequest = new AddPassageRequest(context);
        this.addPassageRequest.addObserver(new AddPassageRequestObserver());
        this.addHouseNoteRequest = new AddHouseNoteRequest(context);
        this.addHouseNoteRequest.addObserver(new AddHouseNoteRequestObserver());
    }

    public boolean isCurrentUserNearbyTheServicingHouse() {
        Localisation localisation = new Localisation(context);
        double distance = localisation.getDistanceInMetersBetweenTwoLatLng(house);

        if(distance < 25) {
            return false;
        } else {
            return true;
        }
    }

    public void getHousePublicProfile() {
        getHousePublicProfileRequest.getHousePublicProfileRequest();
    }

    public void sendPassage(String description, String houseId) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", "test");

        addPassageRequest.sendPassageRequest(description, houseId, userId);

    }

    public void sendNote(String houseId, String description, int note) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", "test");

        addHouseNoteRequest.sendNoteRequest(houseId, userId, description, note);
    }

    private class GetHousePublicProfileRequestObserver implements Observer {
        @Override
        public void update(Observable observable, Object o) {
            house = getHousePublicProfileRequest.getHouse();
            if(house != null) {
                House house = getHousePublicProfileRequest.getHouse();
                String houseAddress = house.getFormattedName();
                String username = house.getUser().getFormattedName();
                detailHouseActivity.setTextsForTheView(houseAddress, username, getHousePublicProfileRequest.getInstructions(), getHousePublicProfileRequest.getPictureURL());
            }
        }
    }

    private class AddPassageRequestObserver implements Observer {
        @Override
        public void update(Observable observable, Object o) {
            if(addPassageRequest.gotAnError()) {
                detailHouseActivity.printToast("Erreur lors de l'envoi du passage.");
            } else {
                detailHouseActivity.printToast("Passage envoyé !");
                detailHouseActivity.resetPassageDescription();
            }
        }
    }

    private class AddHouseNoteRequestObserver implements Observer {
        @Override
        public void update(Observable observable, Object o) {
            if(addHouseNoteRequest.gotAnError()) {
                if(addHouseNoteRequest.getErrorNumber() == 404) {
                    detailHouseActivity.printToast("Vous avez déjà noté cette maison !");
                } else {
                    detailHouseActivity.printToast("Erreur lors de l'envoi de la note.");
                }
            } else {
                detailHouseActivity.printToast("Note envoyée !");
                detailHouseActivity.resetNoteRatingBarAndDescription();
            }
        }
    }
}


