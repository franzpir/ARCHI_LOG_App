package com.archilog.archi_log_demassue_piron.Views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.archilog.archi_log_demassue_piron.Presenter.MapFragmentPresenter;
import com.archilog.archi_log_demassue_piron.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private List<Marker> markers;
    private MapFragmentPresenter mapFragmentPresenter;
    private GoogleMap googleMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mapFragmentPresenter = new MapFragmentPresenter(getContext(), this);
        markers = new ArrayList<>();

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.fragment_map_map);
        mapFragment.getMapAsync(this);

        mapFragmentPresenter.getServicingHouses();

        return view;
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;
        LatLng currentLocalisation = mapFragmentPresenter.getLatLng();
        if(currentLocalisation == null) {
            Toast.makeText(getApplicationContext(), R.string.mapFragment_errorLocation_FR, Toast.LENGTH_LONG).show();
        } else {
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocalisation));

            setMarkersOnTheMap(currentLocalisation, googleMap);

            googleMap.setMinZoomPreference(8.0f);
            googleMap.setMaxZoomPreference(20.0f);
        }
    }

    private void setMarkersOnTheMap(LatLng currentLocalisation, final GoogleMap googleMap) {
        markers.add(googleMap.addMarker(new MarkerOptions()
                .position(currentLocalisation)
                .icon(mapFragmentPresenter.convertVectorImageToBitmapDescriptor(R.drawable.my_location))
                .title("Vous êtes ici")));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        mapFragmentPresenter.sendGoogleMapsIntent(markers, marker);

        return true;
    }

    public void setServicingHouses(double[] coordinates, String username) {
        System.out.println("coordinates[0] = " + coordinates[0]);
        System.out.println("coordinates[1] = " + coordinates[1]);
        System.out.println("username = " + username);
        try {
            markers.add(googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(coordinates[0], coordinates[1]))
                    .title("Maison de " + username)
                    .icon(mapFragmentPresenter.convertVectorImageToBitmapDescriptor(R.drawable.home_other))));

        } catch(NullPointerException ex) {
            mapFragmentPresenter.startActivity(MainActivity.class);
        }
    }

}
