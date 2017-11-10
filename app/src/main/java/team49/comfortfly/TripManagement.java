package team49.comfortfly;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class TripManagement extends AppCompatActivity {

    ListView listViewTripManage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_management);

        // TODO action
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(this, UpdateItinerary.class);
                i.putExtra("origin", "");
                i.putExtra("destination", "");
                i.putExtra("departDate", 0L);
                i.putExtra("returnDate", 0L);
                i.putExtra("departTime", "");
                i.putExtra("returnTime", "");
                i.putExtra("airline", "");
                i.putExtra("flightNumber", "");
                startActivity(i);
            }
        });

        listViewTripManage = (ListView) findViewById(R.id.listViewTripManage);

        List<Trip> list = new ArrayList<>();

        list.add(new Trip());
        list.add(new Trip());
        list.add(new Trip());
        list.add(new Trip());
        list.add(new Trip());


        TripDetailAdapter s = new TripDetailAdapter(getApplicationContext(), list);
        listViewTripManage.setAdapter(s);

        listViewTripManage.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Trip trip = (Trip) parent.getItemAtPosition(position);
                Intent i = new Intent(this, UpdateItinerary.class);
                i.putExtra("origin", trip.Origin);
                i.putExtra("destination", trip.Destination);
                i.putExtra("departDate", System.currentTimeMillis());
                i.putExtra("returnDate", System.currentTimeMillis());
                i.putExtra("departTime", trip.DepartureTime);
                i.putExtra("returnTime", trip.ArrivalTime);
                i.putExtra("airline", trip.Airline);
                i.putExtra("flightNumber", trip.FlightNumber);
                startActivity(i);
            }
        });
    }
}
