package team49.comfortfly;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

public class Trip {
    String fsid;
    String Origin;
    String Destination;
    String DepartureTime;
    String DepartureDate;
    String ArrivalTime;
    String ArrivalDate;
    String Airline;
    String FlightNumber;
    String Duration;
    String Price;
    String Delay;
    List<String> Companion;
    List<Bitmap> CompanionPic;

    public Trip() {
        fsid = "";
        Origin = "";
        Destination = "";
        DepartureDate = "";
        DepartureTime = "";
        ArrivalDate = "";
        ArrivalTime = "";
        Airline = "";
        FlightNumber = "";
        Duration = "";
        Price = "";
        Delay = "";
        Companion = new ArrayList<>();
        CompanionPic = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "\"origin\":\"" + Origin + "\",\"dest\":\"" + Destination + "\",\"depart_time\":\"" + DepartureDate
                + " " + DepartureTime + "\",\"arrival_time\":\"" + ArrivalDate + " " + ArrivalTime + "\",\"airline\":\"" +
                Airline + "\",\"flight_num\":\"" + FlightNumber + "\"";
    }
}
