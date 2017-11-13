package team49.comfortfly;

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

    public Trip() {
        fsid = "";
        Origin = "TPE";
        Destination = "ORD";
        DepartureDate = "2013-10-25";
        DepartureTime = "11:00";
        ArrivalDate = "2017-10-29";
        ArrivalTime = "23:00";
        Airline = "AA";
        FlightNumber = "666";
        Duration = "1h 10m";
    }

    @Override
    public String toString() {
        return "\"origin\":\"" + Origin + "\",\"dest\":\"" + Destination + "\",\"depart_time\":\"" + DepartureDate
                + " " + DepartureTime + "\",\"arrival_time\":\"" + ArrivalDate + " " + ArrivalTime + "\",\"airline\":\"" +
                Airline + "\",\"flight_num\":\"" + FlightNumber + "\"";
    }
}
