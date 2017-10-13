package team49.comfortfly;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.qpxExpress.QPXExpress;
import com.google.api.services.qpxExpress.QPXExpressRequestInitializer;
import com.google.api.services.qpxExpress.model.PassengerCounts;
import com.google.api.services.qpxExpress.model.SliceInput;
import com.google.api.services.qpxExpress.model.TripOption;
import com.google.api.services.qpxExpress.model.TripOptionsRequest;
import com.google.api.services.qpxExpress.model.TripsSearchRequest;
import com.google.api.services.qpxExpress.model.TripsSearchResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText Origin;
    EditText Destination;
    EditText Dt;
    Button Search;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Origin = (EditText) findViewById(R.id.editText1);
        Destination = (EditText) findViewById(R.id.editText2);
        Dt = (EditText) findViewById(R.id.editText3);
        Search = (Button) findViewById(R.id.button2);
        listView = (ListView) findViewById(R.id.listView1);

        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SliceInput slice = new SliceInput();
                slice.setOrigin(Origin.getText().toString());
                slice.setDestination(Destination.getText().toString());
                slice.setDate(Dt.getText().toString());

                AirlineReservation a = new AirlineReservation();
                a.execute(slice);
            }
        });

    }

    class AirlineReservation extends AsyncTask<Object, Boolean, Boolean> {

        private static final String APPLICATION_NAME = "MyFlightApplication";
        /**
         * Global instance of the JSON factory.
         */
        private final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
        /**
         * @param args
         */

        List<TripOption> tripResults;
        StringBuilder sb = new StringBuilder();
        /**
         * Global instance of the HTTP transport.
         */
        private HttpTransport httpTransport;

        @Override
        protected Boolean doInBackground(Object... params) {

            SliceInput slice = (SliceInput) params[0];
            slice.setMaxStops(1);

            try {
                //httpTransport = GoogleNetHttpTransport.newTrustedTransport();
                httpTransport = new ApacheHttpTransport();

                PassengerCounts passengers = new PassengerCounts();
                passengers.setAdultCount(1);

                List<SliceInput> slices = new ArrayList<SliceInput>();
                slices.add(slice);

                TripOptionsRequest request = new TripOptionsRequest();
                request.setSolutions(10);
                request.setPassengers(passengers);
                request.setSlice(slices);

                TripsSearchRequest parameters = new TripsSearchRequest();
                parameters.setRequest(request);
                QPXExpress qpXExpress = new QPXExpress.Builder(httpTransport, JSON_FACTORY, null).setApplicationName(APPLICATION_NAME)
                        .setGoogleClientRequestInitializer(new QPXExpressRequestInitializer(Credential.API_KEY)).build();

                TripsSearchResponse list = qpXExpress.trips().search(parameters).execute();
                this.tripResults = list.getTrips().getTripOption();

            } catch (IOException e) {
                System.err.println(e.getMessage());
                return false;
            } catch (Throwable t) {
                t.printStackTrace();
                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                SearchResultAdapter s = new SearchResultAdapter(getApplicationContext(), tripResults);
                listView.setAdapter(s);
            }
        }
    }
}

