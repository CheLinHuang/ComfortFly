package team49.comfortfly;

public class Trip {
    String FRID;
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
        FRID = "!";
        Origin = "TPE";
        Destination = "ORD";
        DepartureDate = "10/25";
        DepartureTime = "11:00";
        ArrivalDate = "11/15";
        ArrivalTime = "23:00";
        Airline = "AA";
        FlightNumber = "666";
        Duration = "1h 10m";
    }
}
