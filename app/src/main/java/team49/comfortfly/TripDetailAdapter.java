package team49.comfortfly;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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
            System.out.println(result.Companion.get(0));
            ImageButton friend1;
            ImageButton friend2;
            ImageButton friend3;



            if ( result.Companion.size() > 0) {
                friend1 = convertView.findViewById(R.id.friend1);
                friend1.setImageBitmap(result.CompanionPic.get(0));
                friend1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(TripDetailAdapter.this, "test", Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(TripDetailAdapter.super.getContext());
                        builder1.setMessage(result.Companion.get(0));
                        builder1.setCancelable(true);

                        builder1.setNeutralButton(
                                "Okay",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        final AlertDialog alert11 = builder1.create();
                    }
                });
            }
            if (result.Companion.size() > 1) {
                friend2 = convertView.findViewById(R.id.friend2);
                friend2.setImageBitmap(result.CompanionPic.get(1));
                friend2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(TripDetailAdapter.this, "test", Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(TripDetailAdapter.super.getContext());
                        builder1.setMessage(result.Companion.get(1));
                        builder1.setCancelable(true);

                        builder1.setNeutralButton(
                                "Okay",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        final AlertDialog alert11 = builder1.create();
                    }
                });
            }
            if (result.Companion.size() > 2) {
                friend3 = convertView.findViewById(R.id.friend3);
                friend3.setImageBitmap(result.CompanionPic.get(2));
                friend3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(TripDetailAdapter.this, "test", Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(TripDetailAdapter.super.getContext());
                        builder1.setMessage(result.Companion.get(2));
                        builder1.setCancelable(true);

                        builder1.setNeutralButton(
                                "Okay",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        final AlertDialog alert11 = builder1.create();
                    }
                });
            }
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

