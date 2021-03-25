package com.archilog.archi_log_demassue_piron.Model;

public class MapObject {

    private String username;
    private double[] latlng;

    public MapObject(String username, double[] latlng) {
        this.username = username;
        this.latlng = latlng;
    }

    public String getUsername() {
        return username;
    }

    public double[] getLatLng() {
        return latlng;
    }
}
