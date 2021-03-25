package com.archilog.archi_log_demassue_piron.Model;

public class House implements IObject {
    private String houseId;
    private String country;
    private String locality;
    private String street;
    private String number;
    private String postalCode;
    private User user;

    public House(String houseId, String country, String locality, String street, String number, String postalCode, User user) {
        this.houseId = houseId;
        this.country = country;
        this.locality = locality;
        this.street = street;
        this.number = number;
        this.postalCode = postalCode;
        this.user = user;
    }

    public String getHouseId() {
        return houseId;
    }

    public User getUser() {
        return user;
    }

    @Override
    public String getFormattedName() {
        return locality + " - " + street + ", " + number;
    }

    public String getLongFormattedAddress() {
        return street + ", " + number + " " + locality + " " + country;
    }
}
