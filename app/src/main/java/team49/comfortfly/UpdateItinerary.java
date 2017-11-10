package team49.comfortfly;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UpdateItinerary extends AppCompatActivity {

    String curDate;
    String OriginLatLng;
    String DestinationLatLng;
    String StartDate;
    String ReturnDate;
    CalendarView StartDateView;
    CalendarView ReturnDateView;
    Button Search;
    ListView listView;
    EditText airline;
    EditText flightNumber;

    String originInput;
    String destinationInput;
    Long departDateInput;
    Long returnDateInput;
    String departTimeInput;
    String returnTimeInput;
    String airlineInput;
    String flightNumberInput;

    private static final String TAG = "UpdateItinerary";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_itinerary);

        StartDateView = (CalendarView) findViewById(R.id.startDate);
        ReturnDateView = (CalendarView) findViewById(R.id.returnDate);
        Search = (Button) findViewById(R.id.button2);
        listView = (ListView) findViewById(R.id.listView1);

        Intent intent = getIntent();
        originInput = intent.getExtras().getString("origin");
        destinationInput = intent.getExtras().getString("destination");
        departDateInput = intent.getExtras().getLong("departDate");
        returnDateInput = intent.getExtras().getLong("returnDate");
        departTimeInput = intent.getExtras().getString("departTime");
        returnTimeInput = intent.getExtras().getString("returnTime");
        airlineInput = intent.getExtras().getString("airline");
        flightNumberInput = intent.getExtras().getString("flightNumber");

        if (departDateInput != 0L) StartDateView.setDate(departDateInput);
        StartDateView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                int d = dayOfMonth;
                StartDate = String.valueOf(year) + '-' + String.valueOf(month + 1) + '-' + String.valueOf(d);
            }
        });

        if (departDateInput != 0L) StartDateView.setDate(departDateInput);
        ReturnDateView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                int d = dayOfMonth;
                ReturnDate = String.valueOf(year) + '-' + String.valueOf(month + 1) + '-' + String.valueOf(d);
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
                if (OriginLatLng == null || DestinationLatLng == null || StartDate == null || ReturnDate == null
                        || StartDate.compareTo(curDate) == -1 || ReturnDate.compareTo(StartDate) == -1) {
                    alert11.show();
                } else {
//                    Intent i = new Intent(UpdateItinerary.this, FlightSearchResult.class);
//                    i.putExtra("originLatLng", (OriginLatLng.split("\\(")[1]).split("\\)")[0]);
//                    i.putExtra("destinationLatLng", (DestinationLatLng.split("\\(")[1]).split("\\)")[0]);
//                    i.putExtra("departDate", StartDate);
//                    i.putExtra("returnDate", ReturnDate);
//                    startActivity(i);
                }
            }
        });

        PlaceAutocompleteFragment Origin = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.origin);
        if (originInput == "")
            Origin.setHint("Departure");
        else {
            Origin.setText(originInput);
        }

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

        if (originInput == "")
            Destination.setHint("Destination");
        else {
            Destination.setText(destinationInput);
        }

        Destination.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
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

        final EditText startTime = (EditText) findViewById(R.id.startTime);
        if (departTimeInput != "") startTime.setText(departTimeInput);
        startTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(UpdateItinerary.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        startTime.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });
        final EditText endTime = (EditText) findViewById(R.id.endTime);
        if (returnTimeInput != "") endTime.setText(returnTimeInput);
        endTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(UpdateItinerary.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        endTime.setText(selectedHour + ":" + (selectedMinute < 10 ? "0" + selectedMinute : selectedMinute));
                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        airline = (EditText) findViewById(R.id.airline);
        flightNumber = (EditText) findViewById(R.id.flightNumber);

        if (airlineInput != "") {
            airline.setText(airlineInput);
        }
        if (flightNumberInput != "") {
            flightNumber.setText(flightNumberInput);
        }
    }
}

