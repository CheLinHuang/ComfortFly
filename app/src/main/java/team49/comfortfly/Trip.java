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
    String Price;

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
    }

    @Override
    public String toString() {
        return "\"origin\":\"" + Origin + "\",\"dest\":\"" + Destination + "\",\"depart_time\":\"" + DepartureDate
                + " " + DepartureTime + "\",\"arrival_time\":\"" + ArrivalDate + " " + ArrivalTime + "\",\"airline\":\"" +
                Airline + "\",\"flight_num\":\"" + FlightNumber + "\"";
    }
}
