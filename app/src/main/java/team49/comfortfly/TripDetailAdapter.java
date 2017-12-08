package team49.comfortfly;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

public class TripDetailAdapter extends ArrayAdapter<Trip> {

    List<Trip> trips;

    TripDetailAdapter(Context context, List<Trip> trips) {
        super(context, 0, trips);
        this.trips = trips;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final Trip result = super.getItem(position);

        if (result.Price.equals("") && result.Destination.equals("")) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_trip_companion, null);
            ImageButton friend1;
            ImageButton friend2;
            ImageButton friend3;

            if (result.CompanionPic.size() > 0) {
                friend1 = convertView.findViewById(R.id.friend1);
                friend1.setImageBitmap(result.CompanionPic.get(0));
            }
            if (result.CompanionPic.size() > 1) {
                friend2 = convertView.findViewById(R.id.friend2);
                friend2.setImageBitmap(result.CompanionPic.get(1));
            }
            if (result.CompanionPic.size() > 2) {
                friend3 = convertView.findViewById(R.id.friend3);
                friend3.setImageBitmap(result.CompanionPic.get(2));
            }
        } else if (result.Price.equals("")) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_trip_detail_adapter, null);
            TextView textViewTripDepartAirport = convertView.findViewById(R.id.textViewTripDepartAirport);
            TextView textViewTripArrivalAirport = convertView.findViewById(R.id.textViewTripArrivalAirport);
            TextView textViewTripDepartDate = convertView.findViewById(R.id.textViewTripDepartDate);
            TextView textViewTripDepartTime = convertView.findViewById(R.id.textViewTripDepartTime);
            TextView textViewTripArrivalDate = convertView.findViewById(R.id.textViewTripArrivalDate);
            TextView textViewTripArrivalTime = convertView.findViewById(R.id.textViewTripArrivalTime);
            TextView textViewTripAirline = convertView.findViewById(R.id.textViewTripAirline);
            TextView textViewTripFlightNum = convertView.findViewById(R.id.textViewTripFlightNum);

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
            TextView textViewSummaryPrice = convertView.findViewById(R.id.textViewSummaryPrice);
            TextView textViewSummaryDuration = convertView.findViewById(R.id.textViewSummaryDuration);
            TextView textViewDelay = convertView.findViewById(R.id.textViewDelay);
            textViewSummaryPrice.setText(result.Price);
            textViewSummaryDuration.setText(result.Duration);
            textViewDelay.setText(result.Delay);
        }
        // Return the completed view to render on screen
        return convertView;
    }
}

