package team49.comfortfly;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class TripManagement extends AppCompatActivity {

    ListView listViewTripManage;
    List<Trip> list;
    TripDetailAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_management);
        
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TripManagement.this, UpdateItinerary.class);
                i.putExtra("fsid", "");
                i.putExtra("origin", "");
                i.putExtra("destination", "");
                i.putExtra("departDate", "");
                i.putExtra("returnDate", "");
                i.putExtra("departTime", "");
                i.putExtra("returnTime", "");
                i.putExtra("airline", "");
                i.putExtra("flightNumber", "");
                startActivity(i);
            }
        });
        listViewTripManage = (ListView) findViewById(R.id.listViewTripManage);
        //list = new ArrayList<>();
        //new GetTripsTask().execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        list = new ArrayList<>();
        new GetTripsTask().execute();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //list = new ArrayList<>();
        //new GetTripsTask().execute();
    }

    String duration(String time1, String time2) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        StringBuilder sb = new StringBuilder();
        try {
            long t1 = f.parse(time1).getTime();
            long t2 = f.parse(time2).getTime();
            long diff = (t2 - t1) / 1000 / 60;
            if (diff > 24 * 60) {
                sb.append(diff / 24 / 60);
                sb.append("d");
            }
            if (diff > 60) {
                sb.append(diff % (24 * 60) / 60);
                sb.append("h");
            }
            sb.append(diff % 60);
            sb.append("m");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    class GetTripsTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet("http://fa17-cs411-49.cs.illinois.edu/api/trip?token=" + Home.token);
            try {
                HttpResponse response = httpclient.execute(httpget);

                System.out.println(response.toString());

                System.out.println(response.getStatusLine().getStatusCode());
                if (response.getStatusLine().getStatusCode() == 200) {
                    String responseString = EntityUtils.toString(response.getEntity());
                    System.out.println(responseString);
                    JSONObject obj = new JSONObject(responseString);

                    JSONArray arr = obj.getJSONArray("result");
                    for (int i = 0; i < arr.length(); i++) {
                        Trip trip = new Trip();
                        trip.fsid = arr.getJSONObject(i).getString("fsid");
                        trip.Origin = arr.getJSONObject(i).getString("origin");
                        trip.Destination = arr.getJSONObject(i).getString("dest");
                        trip.FlightNumber = arr.getJSONObject(i).getString("flight_num");
                        trip.Airline = arr.getJSONObject(i).getString("airline");
                        String depart_time = arr.getJSONObject(i).getString("depart_time");
                        trip.DepartureDate = depart_time.split(" ")[0];
                        trip.DepartureTime = depart_time.split(" ")[1];
                        String arrival_time = arr.getJSONObject(i).getString("arrival_time");
                        trip.ArrivalDate = arrival_time.split(" ")[0];
                        trip.ArrivalTime = arrival_time.split(" ")[1];
                        trip.Duration = duration(depart_time, arrival_time);
                        list.add(trip);

                        String companionString = arr.getJSONObject(i).getString("companion");
                        String[] companionList = companionString.substring(1, companionString.length() - 1).split(",");
                        Trip companionTrip = new Trip();
                        for (int j = 0; j < companionList.length; j++) {
                            System.out.println(companionList[j]);
                            companionTrip.Companion.add(companionList[j]);
                        }
                        list.add(companionTrip);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            adapter = new TripDetailAdapter(getApplicationContext(), list);
            listViewTripManage.setAdapter(adapter);

            listViewTripManage.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Trip trip = (Trip) parent.getItemAtPosition(position);
                    Intent i = new Intent(TripManagement.this, UpdateItinerary.class);
                    i.putExtra("fsid", trip.fsid);
                    i.putExtra("origin", trip.Origin);
                    i.putExtra("destination", trip.Destination);
                    i.putExtra("departDate", trip.DepartureDate);
                    i.putExtra("returnDate", trip.ArrivalDate);
                    i.putExtra("departTime", trip.DepartureTime);
                    i.putExtra("returnTime", trip.ArrivalTime);
                    i.putExtra("airline", trip.Airline);
                    i.putExtra("flightNumber", trip.FlightNumber);
                    startActivity(i);
                }
            });

            listViewTripManage.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(
            ) {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    final Trip trip = (Trip) parent.getItemAtPosition(position);
                    final AlertDialog.Builder builder = new AlertDialog.Builder(TripManagement.this);
                    builder.setTitle("Alert");
                    builder.setMessage("Delete this trip?");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            new DeleteTripTask().execute(trip);
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    builder.create().show();
                    return true;
                }
            });
        }
    }

    class DeleteTripTask extends AsyncTask<Trip, Void, Void> {

        Trip trip;

        @Override
        protected Void doInBackground(Trip... params) {
            this.trip = params[0];
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://fa17-cs411-49.cs.illinois.edu/api/trip?token=" + Home.token);

            try {
                httppost.setEntity(new StringEntity("{\"action\":\"delete\",\"fsid\":\"" + trip.fsid + "\"}"));
                HttpResponse response = httpclient.execute(httppost);
                System.out.println(response.getStatusLine().getStatusCode());

                if (response.getStatusLine().getStatusCode() == 200) {
                    String responseString = EntityUtils.toString(response.getEntity());
                    System.out.println(responseString);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            list.remove(trip);
            adapter.notifyDataSetChanged();
        }
    }
}
