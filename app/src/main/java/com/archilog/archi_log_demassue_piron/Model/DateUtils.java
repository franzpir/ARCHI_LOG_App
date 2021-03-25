package com.archilog.archi_log_demassue_piron.Model;

public class DateUtils {

    public static String parseDate(String datePassage) {
        String date = datePassage.substring(0, datePassage.indexOf("T"));
        String time = datePassage.substring(datePassage.indexOf("T") + 1, datePassage.indexOf("."));

        String year = date.substring(0, date.indexOf("-"));
        String month = date.substring(date.indexOf("-") + 1, date.lastIndexOf("-"));
        String day = date.substring(date.lastIndexOf("-") + 1);

        return parseDateInTheCorrectFormat(day, month, year, time);
    }

    private static String parseDateInTheCorrectFormat(String day, String month, String year, String time) {
        StringBuilder sb = new StringBuilder();
        sb.append(day);
        sb.append("/");
        sb.append(month);
        sb.append("/");
        sb.append(year);
        sb.append(" ");
        sb.append(time);

        return sb.toString();
    }
}
