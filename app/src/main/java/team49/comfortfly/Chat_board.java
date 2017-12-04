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

import java.util.ArrayList;
import java.util.List;

public class Chat_board extends AppCompatActivity {

    ListView listViewChat;
    List<Trip> list;
    TripDetailAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_board);

        // TODO action
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Chat_board.this, Chat.class);
                startActivity(i);
            }
        });
        listViewChat = (ListView) findViewById(R.id.listViewTripManage);
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
                        list.add(trip);
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
            listViewChat.setAdapter(adapter);

            listViewChat.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Trip trip = (Trip) parent.getItemAtPosition(position);
                    Intent i = new Intent(Chat_board.this, UpdateItinerary.class);
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

            listViewChat.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(
            ) {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    final Trip trip = (Trip) parent.getItemAtPosition(position);
                    final AlertDialog.Builder builder = new AlertDialog.Builder(Chat_board.this);
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

    class PullChatRoomTask extends AsyncTask<Trip, Void, Void> {

        Trip trip;

        @Override
        protected Void doInBackground(Trip... params) {
            this.trip = params[0];
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://fa17-cs411-49.cs.illinois.edu/api/chatroom?token=" + Home.token);

            try {
                httppost.setEntity(new StringEntity("{\"action\":\"delete\",\"fsid\":\"" + trip.fsid + "\"}"));
                HttpResponse response = httpclient.execute(httppost);
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
                        list.add(trip);
                    }
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
