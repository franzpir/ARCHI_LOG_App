package com.archilog.archi_log_demassue_piron.Model;

import static com.archilog.archi_log_demassue_piron.Model.DateUtils.parseDate;

public class Passage implements IObject {
    private String datePassage;
    private String houseId;
    private User user;
    private String description;

    public Passage(String datePassage, String houseId, User user, String description) {
        this.datePassage = parseDate(datePassage);
        this.houseId = houseId;
        this.user = user;
        this.description = description;
    }

    public String getHouseId() {
        return houseId;
    }

    public User getUser() {
        return user;
    }

    @Override
    public String getFormattedName() {
        return datePassage + " : " + description;
    }
}
