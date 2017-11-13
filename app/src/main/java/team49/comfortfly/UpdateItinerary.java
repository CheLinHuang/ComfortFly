package team49.comfortfly;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
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

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
    String departDateInput;
    String returnDateInput;
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
        departDateInput = intent.getExtras().getString("departDate");
        returnDateInput = intent.getExtras().getString("returnDate");
        departTimeInput = intent.getExtras().getString("departTime");
        returnTimeInput = intent.getExtras().getString("returnTime");
        airlineInput = intent.getExtras().getString("airline");
        flightNumberInput = intent.getExtras().getString("flightNumber");
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");

        if (departDateInput != "") {
            try {
                Date d = f.parse(departDateInput);
                long milliseconds = d.getTime();
                StartDateView.setDate(milliseconds);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        StartDateView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                int d = dayOfMonth;
                StartDate = String.valueOf(year) + '-' + String.valueOf(month + 1) + '-' + String.valueOf(d);
            }
        });

        if (returnDateInput != "") {
            try {
                Date d = f.parse(returnDateInput);
                long milliseconds = d.getTime();
                ReturnDateView.setDate(milliseconds);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
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
                new SendItinerary().execute();
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
        if (originInput.equals("")){
            Origin.setHint("Departure");
            Search.setText("Add");
        }
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

        if (destinationInput.equals(""))
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

    class SendItinerary extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://fa17-cs411-49.cs.illinois.edu/api/trip?token=" + Home.token);
            Trip trip = new Trip();
            trip.Origin = "TPE";
            trip.Destination = "ORD";
            trip.DepartureDate = "2013-10-25";
            trip.DepartureTime = "11:00";
            trip.ArrivalDate = "2017-10-29";
            trip.ArrivalTime = "23:00";
            trip.Airline = "AA";
            trip.FlightNumber = "666";

            try {
                httppost.setEntity(new StringEntity("{\"action\":\"insert\"," + trip.toString() + "}"));
                HttpResponse response = httpclient.execute(httppost);
                System.out.println(response.getStatusLine().getStatusCode());
                if (response.getStatusLine().getStatusCode() == 200) {
                    String responseString = EntityUtils.toString(response.getEntity());
                    System.out.println(responseString);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return true;
        }
    }
}

