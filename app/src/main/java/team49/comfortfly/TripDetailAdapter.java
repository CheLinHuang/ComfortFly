package team49.comfortfly;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TripDetailAdapter extends ArrayAdapter<Trip> {

    List<Trip> trips;

    TripDetailAdapter(Context context, List<Trip> trips) {
        super(context, 0, trips);
        this.trips = trips;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Trip result = super.getItem(position);

        if (result.Price.equals("") && result.Destination.equals("")) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_trip_companion, null);
            System.out.println(result.Companion.get(0));
            ImageButton friend1;
            ImageButton friend2;
            ImageButton friend3;
            if ( result.Companion.size() > 0) {
                friend1 = convertView.findViewById(R.id.friend1);
                // http://icons.iconarchive.com/icons/iconarchive/red-orb-alphabet/48/Letter-Z-icon.png

//                friend1.setImageResource(map.get(result.Companion.get(0).toLowerCase().charAt(0)));
            }
            if ( result.Companion.size() > 1)
                friend2 = convertView.findViewById(R.id.friend2);
            if ( result.Companion.size() > 2)
                friend3 = convertView.findViewById(R.id.friend3);

        } else if(result.Price.equals("")) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_trip_detail_adapter, null);
            TextView textViewTripDepartAirport = (TextView) convertView.findViewById(R.id.textViewTripDepartAirport);
            TextView textViewTripArrivalAirport = (TextView) convertView.findViewById(R.id.textViewTripArrivalAirport);
            TextView textViewTripDepartDate = (TextView) convertView.findViewById(R.id.textViewTripDepartDate);
            TextView textViewTripDepartTime = (TextView) convertView.findViewById(R.id.textViewTripDepartTime);
            TextView textViewTripArrivalDate = (TextView) convertView.findViewById(R.id.textViewTripArrivalDate);
            TextView textViewTripArrivalTime = (TextView) convertView.findViewById(R.id.textViewTripArrivalTime);
            TextView textViewTripAirline = (TextView) convertView.findViewById(R.id.textViewTripAirline);
            TextView textViewTripFlightNum = (TextView) convertView.findViewById(R.id.textViewTripFlightNum);

            textViewTripDepartAirport.setText(result.Origin);
            textViewTripArrivalAirport.setText(result.Destination);
            textViewTripDepartDate.setText(result.DepartureDate);
            textViewTripDepartTime.setText(result.DepartureTime);
            textViewTripArrivalDate.setText(result.ArrivalDate);
            textViewTripArrivalTime.setText(result.ArrivalTime);
            textViewTripAirline.setText(result.Airline);
            textViewTripFlightNum.setText(result.FlightNumber);

        } else {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_trip_summary, null);
            TextView textViewSummaryPrice = (TextView) convertView.findViewById(R.id.textViewSummaryPrice);
            TextView textViewSummaryDuration = (TextView) convertView.findViewById(R.id.textViewSummaryDuration);
            TextView textViewDelay = convertView.findViewById(R.id.textViewDelay);
            textViewSummaryPrice.setText(result.Price);
            textViewSummaryDuration.setText(result.Duration);
            textViewDelay.setText(result.Delay);
        }
        // Return the completed view to render on screen
        return convertView;
    }
}

