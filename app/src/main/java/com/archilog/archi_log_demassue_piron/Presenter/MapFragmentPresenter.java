package com.archilog.archi_log_demassue_piron.Presenter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import com.archilog.archi_log_demassue_piron.Model.HTTPRequests.GetUserHousesAndServicingHousesRequest;
import com.archilog.archi_log_demassue_piron.Model.Localisation.Localisation;
import com.archilog.archi_log_demassue_piron.Model.MapObject;
import com.archilog.archi_log_demassue_piron.Views.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class MapFragmentPresenter implements Observer {
    private GetUserHousesAndServicingHousesRequest userHousesAndServicingHousesRequest;
    private Localisation localisation;
    private Context context;
    private MapFragment mapFragment;

    public MapFragmentPresenter(Context context, MapFragment mapFragment) {
        this.userHousesAndServicingHousesRequest = new GetUserHousesAndServicingHousesRequest(context);
        this.context = context;
        this.localisation = new Localisation(context);
        this.userHousesAndServicingHousesRequest.addObserver(this);
        this.mapFragment = mapFragment;
    }

    public BitmapDescriptor convertVectorImageToBitmapDescriptor(int vectorImage) {
        Drawable vector = ContextCompat.getDrawable(this.context, vectorImage);
        vector.setBounds(0, 0, vector.getIntrinsicWidth(), vector.getIntrinsicHeight());
        Bitmap bitmapImage = Bitmap.createBitmap(vector.getIntrinsicWidth(), vector.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmapImage);
        vector.draw(canvas);

        return BitmapDescriptorFactory.fromBitmap(bitmapImage);
    }

    public LatLng getLatLng() {
        Double latitude = localisation.getLatitude();
        Double longitude = localisation.getLongitude();
        if (!latitude.isNaN() || !longitude.isNaN()) {
            return new LatLng(localisation.getLatitude(), localisation.getLongitude());
        } else {
            return null;
        }
    }

    public void getServicingHouses() {
        userHousesAndServicingHousesRequest.getUserHousesAndServicingHousesRequest();
    }

    public void sendGoogleMapsIntent(List<Marker> markers, Marker marker) {
        for(int i = 0; i < markers.size(); i++) {
            if(markers.get(i).equals(marker)) {
                Uri gmmIntentUri = Uri.parse("google.navigation:geo=" + markers.get(i).getPosition().toString());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");

                Log.i("MapFragmentPresenter/sendGoogleMapsIntent", "Démarrage de Google Maps.");
                context.startActivity(mapIntent);
            }
        }
    }

    public void startActivity(Class toStart) {
        Toast.makeText(context, "Erreur lors du chargement de la carte. Merci de réessayer.", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(context, toStart);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public void update(Observable observable, Object o) {
        List<MapObject> houses = userHousesAndServicingHousesRequest.getServicingHousesAsMapObjects();
        if(houses.size() != 0) {
            for(int i = 0; i < houses.size(); i++) {
                double[] coordinates = houses.get(i).getLatLng();
                String username = houses.get(i).getUsername();

                mapFragment.setServicingHouses(coordinates, username);
            }
        }
    }
}