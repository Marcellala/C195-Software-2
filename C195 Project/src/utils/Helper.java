package utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.time.*;

public class Helper {

    private static final LocalTime START_BUSINESS_HOURS_EST = LocalTime.of(8,0);
    private static final LocalTime END_BUSINESS_HOURS_EST = LocalTime.of(22,0);

    private static ObservableList<LocalTime> localStartTimeList = FXCollections.observableArrayList();
    private static ObservableList<LocalTime> localEndTimeList = FXCollections.observableArrayList();

    private static void generateStartEndTimes(){
        ZonedDateTime estStartZDT = ZonedDateTime.of(LocalDate.now(),START_BUSINESS_HOURS_EST, ZoneId.of("America/New_York"));
        ZonedDateTime estEndZDT = ZonedDateTime.of(LocalDate.now(),END_BUSINESS_HOURS_EST, ZoneId.of("America/New_York"));
        LocalTime localStart = estStartZDT.withZoneSameInstant(ZoneId.systemDefault()).toLocalTime();
        //System.out.println("localStart = " + localStart);
        LocalTime localEnd = estEndZDT.withZoneSameInstant(ZoneId.systemDefault()).toLocalTime();

        if(localStartTimeList.size() == 0 || localEndTimeList.size() == 0){
            while (localStart.isBefore(localEnd)){
                localStartTimeList.add(localStart);
                localStart = localStart.plusMinutes(15);
                localEndTimeList.add(localStart);
            }
        }

    }

    public static ObservableList<LocalTime> getLocalStartTimeList() {
        generateStartEndTimes();
        return localStartTimeList;
    }

    public static ObservableList<LocalTime> getLocalEndTimeList() {
        generateStartEndTimes();
        return localEndTimeList;
    }
}
