package team49.comfortfly;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class TripDetailAdapter extends ArrayAdapter<Trip> {

    List<Trip> trips;

    public TripDetailAdapter(Context context, List<Trip> trips) {
        super(context, 0, trips);
        this.trips = trips;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Holder holder;

        // Get the data item for this position
        Trip result = super.getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_trip_detail_adapter, parent, false);

            // Lookup view for data population
            TextView textViewTripDepartAirport = (TextView) convertView.findViewById(R.id.textViewTripDepartAirport);
            TextView textViewTripArrivalAirport = (TextView) convertView.findViewById(R.id.textViewTripArrivalAirport);
            TextView textViewTripDepartDate = (TextView) convertView.findViewById(R.id.textViewTripDepartDate);
            TextView textViewTripDepartTime = (TextView) convertView.findViewById(R.id.textViewTripDepartTime);
            TextView textViewTripArrivalDate = (TextView) convertView.findViewById(R.id.textViewTripArrivalDate);
            TextView textViewTripArrivalTime = (TextView) convertView.findViewById(R.id.textViewTripArrivalTime);
            TextView textViewTripDuration = (TextView) convertView.findViewById(R.id.textViewTripDuration);
            TextView textViewTripAirline = (TextView) convertView.findViewById(R.id.textViewTripAirline);
            TextView textViewTripFlightNum = (TextView) convertView.findViewById(R.id.textViewTripFlightNum);
            holder = new Holder();

            holder.textViewTripDepartAirport = textViewTripDepartAirport;
            holder.textViewTripArrivalAirport = textViewTripArrivalAirport;
            holder.textViewTripDepartDate = textViewTripDepartDate;
            holder.textViewTripDepartTime = textViewTripDepartTime;
            holder.textViewTripArrivalDate = textViewTripArrivalDate;
            holder.textViewTripArrivalTime = textViewTripArrivalTime;
            holder.textViewTripDuration = textViewTripDuration;
            holder.textViewTripAirline = textViewTripAirline;
            holder.textViewTripFlightNum = textViewTripFlightNum;


            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.textViewTripDepartAirport.setText(result.Origin);
        holder.textViewTripArrivalAirport.setText(result.Destination);
        holder.textViewTripDepartDate.setText(result.DepartureDate);
        holder.textViewTripDepartTime.setText(result.DepartureTime);
        holder.textViewTripArrivalDate.setText(result.ArrivalDate);
        holder.textViewTripArrivalTime.setText(result.ArrivalTime);
        holder.textViewTripDuration.setText(result.Duration);
        holder.textViewTripAirline.setText(result.Airline);
        holder.textViewTripFlightNum.setText(result.FlightNumber);

        // Return the completed view to render on screen
        return convertView;
    }

    public class Holder {
        TextView textViewTripDepartAirport;
        TextView textViewTripArrivalAirport;
        TextView textViewTripDepartDate;
        TextView textViewTripDepartTime;
        TextView textViewTripArrivalDate;
        TextView textViewTripArrivalTime;
        TextView textViewTripDuration;
        TextView textViewTripAirline;
        TextView textViewTripFlightNum;
    }
}
