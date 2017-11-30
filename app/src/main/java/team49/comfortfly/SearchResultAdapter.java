package team49.comfortfly;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class SearchResultAdapter extends ArrayAdapter<Trip> {

    List<Trip> tripResults;

    public SearchResultAdapter(Context context, List<Trip> tripResults) {
        super(context, 0, tripResults);
        this.tripResults = tripResults;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        Trip trip = super.getItem(position);

        if (!trip.Duration.equals("")) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_trip_summary, parent, false);
            }
            TextView textViewPrice = (TextView) convertView.findViewById(R.id.textViewSummaryPrice);
            TextView textViewDuration = (TextView) convertView.findViewById(R.id.textViewSummaryDuration);
            textViewPrice.setText(trip.Price);
            textViewDuration.setText(trip.Duration);
        } else {

            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_search_result_adapter, parent, false);
            }

            // Lookup view for data population
            TextView textViewFlightTime = (TextView) convertView.findViewById(R.id.textViewFlightTime);
            TextView textViewFlightNum = (TextView) convertView.findViewById(R.id.textViewFlightNum);
            TextView textViewDuration = (TextView) convertView.findViewById(R.id.textViewDuration);
            textViewFlightTime.setText(trip.DepartureTime + " - " + trip.ArrivalTime);
            textViewFlightNum.setText(trip.Airline + " " + trip.FlightNumber);
            textViewDuration.setText("");
        }
        // Return the completed view to render on screen
        return convertView;
    }
}
