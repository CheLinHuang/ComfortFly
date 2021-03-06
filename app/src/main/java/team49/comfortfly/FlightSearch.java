package team49.comfortfly;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FlightSearch extends AppCompatActivity {

    private static final String TAG = "FlightSearch";
    String curDate;
    String OriginLatLng;
    String DestinationLatLng;
    String StartDate;
    CalendarView StartDateView;
    CalendarView ReturnDateView;
    Button Search;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_search);

        StartDateView = (CalendarView) findViewById(R.id.startDate);
        ReturnDateView = (CalendarView) findViewById(R.id.returnDate);
        Search = (Button) findViewById(R.id.button2);
        listView = (ListView) findViewById(R.id.listView1);

        StartDateView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                int d = dayOfMonth;
                StartDate = String.valueOf(year) + '-' + String.valueOf(month + 1) + '-' + String.valueOf(d);
            }
        });

        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Missing information");
        builder1.setCancelable(true);

        builder1.setNeutralButton(
                "Okay",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert11 = builder1.create();

        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check valid
//                AirlineReservation a = new AirlineReservation();
//                a.execute(slice);
                Calendar date = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                curDate = sdf.format(date.getTime());
                if (OriginLatLng == null || DestinationLatLng == null || StartDate == null
                        || StartDate.compareTo(curDate) == -1 ) {
                    alert11.show();
                } else {

                    System.out.println((OriginLatLng.split("\\(")[1]).split("\\)")[0]);
                    System.out.println((DestinationLatLng.split("\\(")[1]).split("\\)")[0]);
                    System.out.println(StartDate);
                    Intent i = new Intent(FlightSearch.this, FlightSearchResult.class);
                    i.putExtra("originLatLng", (OriginLatLng.split("\\(")[1]).split("\\)")[0]);
                    i.putExtra("destinationLatLng", (DestinationLatLng.split("\\(")[1]).split("\\)")[0]);
                    i.putExtra("departDate", StartDate);
                    startActivity(i);
                }
            }
        });

        PlaceAutocompleteFragment Origin = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.origin);
        Origin.setHint("Departure");

        Origin.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName());
                Log.i(TAG, "Place: " + place.getLatLng());
                OriginLatLng = place.getLatLng().toString();
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });

        PlaceAutocompleteFragment Destination = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.destination);
        Destination.setHint("Destination");

        Destination.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Log.i(TAG, "Place: " + place.getName());
                Log.i(TAG, "Place: " + place.getLatLng());
                DestinationLatLng = place.getLatLng().toString();

            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });
    }
}

