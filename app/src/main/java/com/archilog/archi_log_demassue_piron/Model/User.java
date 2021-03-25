package com.archilog.archi_log_demassue_piron.Model;

public class User implements IObject {
    private String userId;
    private String firstname;
    private String lastname;
    private String pictureLink;

    public User(String userId, String firstname, String lastname, String pictureLink) {
        this.userId = userId;
        this.firstname = firstname;
        this.lastname = lastname;
        this.pictureLink = pictureLink;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getPictureLink() {
        return pictureLink;
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public String getFormattedName() {
        return firstname + " " + lastname;
    }
}
