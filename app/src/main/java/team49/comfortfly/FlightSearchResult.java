package team49.comfortfly;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FlightSearchResult extends AppCompatActivity {

    ListView resultListView;
    String originLatLng;
    String destinationLatLng;
    String departDate;
    String returnDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_search_result);
        resultListView = (ListView) findViewById(R.id.resultListView);

        Intent intent = getIntent();
        originLatLng = intent.getExtras().getString("originLatLng");
        destinationLatLng = intent.getExtras().getString("destinationLatLng");
        departDate = intent.getExtras().getString("departDate");
        returnDate = intent.getExtras().getString("returnDate");

        GoogleFlightSearch a = new GoogleFlightSearch();
        a.execute();
    }

    class GoogleFlightSearch extends AsyncTask<Void, Boolean, Boolean> {

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
        protected Boolean doInBackground(Void... params) {


            String originAirport;
            String destinationAirport;

            try {

                /* Get departure airport code */
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet httpget = new HttpGet("http://iatageo.com/getCode/" + originLatLng.split(",")[0] +
                        "/" + originLatLng.split(",")[1]);
                HttpResponse response = httpclient.execute(httpget);

                if (response.getStatusLine().getStatusCode() == 200) {
                    String responseString = EntityUtils.toString(response.getEntity());
                    originAirport = responseString.split("\"")[7];
                } else {
                    return false;
                }

                /* Get destination airport code */
                httpget = new HttpGet("http://iatageo.com/getCode/" + destinationLatLng.split(",")[0] +
                        "/" + destinationLatLng.split(",")[1]);
                response = httpclient.execute(httpget);
                if (response.getStatusLine().getStatusCode() == 200) {
                    String responseString = EntityUtils.toString(response.getEntity());
                    destinationAirport = responseString.split("\"")[7];
                } else {
                    return false;
                }

                //httpTransport = GoogleNetHttpTransport.newTrustedTransport();
                httpTransport = new ApacheHttpTransport();

                SliceInput slice1 = new SliceInput();
                slice1.setMaxStops(1);
                slice1.setOrigin(originAirport);
                slice1.setDestination(destinationAirport);
                slice1.setDate(departDate);

                SliceInput slice2 = new SliceInput();
                slice2.setMaxStops(1);
                slice2.setOrigin(destinationAirport);
                slice2.setDestination(originAirport);
                slice2.setDate(returnDate);

                List<SliceInput> slices = new ArrayList<>();
                slices.add(slice1);
                slices.add(slice2);

                TripOptionsRequest request = new TripOptionsRequest();
                request.setSolutions(20);
                request.setPassengers(new PassengerCounts().setAdultCount(1));
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
                resultListView.setAdapter(s);
            }
        }
    }
}