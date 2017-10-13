package team49.comfortfly;
import android.os.AsyncTask;
import android.widget.TextView;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.qpxExpress.QPXExpressRequestInitializer;
import com.google.api.services.qpxExpress.QPXExpress;
import com.google.api.services.qpxExpress.model.FlightInfo;
import com.google.api.services.qpxExpress.model.LegInfo;
import com.google.api.services.qpxExpress.model.PassengerCounts;
import com.google.api.services.qpxExpress.model.PricingInfo;
import com.google.api.services.qpxExpress.model.SegmentInfo;
import com.google.api.services.qpxExpress.model.SliceInfo;
import com.google.api.services.qpxExpress.model.TripOption;
import com.google.api.services.qpxExpress.model.TripOptionsRequest;
import com.google.api.services.qpxExpress.model.TripsSearchRequest;
import com.google.api.services.qpxExpress.model.SliceInput;
import com.google.api.services.qpxExpress.model.TripsSearchResponse;

import java.io.IOException;
import java.util.*;

public class AirlineReservation extends AsyncTask<Object, Boolean, Boolean> {

    /**
     * @param args
     */

    TextView result;

    AirlineReservation(TextView result) {
        this.result = result;
    }

    private static final String APPLICATION_NAME = "MyFlightApplication";

    /** Global instance of the HTTP transport. */
    private static HttpTransport httpTransport;

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    StringBuilder sb = new StringBuilder();

    @Override
    protected Boolean doInBackground(Object... params) {
        // TODO Auto-generated method stub

        SliceInput slice = (SliceInput) params[0];
        slice.setMaxStops(1);

        try {
            //httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            httpTransport = new ApacheHttpTransport();

            PassengerCounts passengers= new PassengerCounts();
            passengers.setAdultCount(1);

            List<SliceInput> slices = new ArrayList<SliceInput>();
            slices.add(slice);

            TripOptionsRequest request= new TripOptionsRequest();
            request.setSolutions(5);
            request.setPassengers(passengers);
            request.setSlice(slices);

            TripsSearchRequest parameters = new TripsSearchRequest();
            parameters.setRequest(request);
            QPXExpress qpXExpress= new QPXExpress.Builder(httpTransport, JSON_FACTORY, null).setApplicationName(APPLICATION_NAME)
                    .setGoogleClientRequestInitializer(new QPXExpressRequestInitializer(Credential.API_KEY)).build();

            TripsSearchResponse list= qpXExpress.trips().search(parameters).execute();
            List<TripOption> tripResults=list.getTrips().getTripOption();

            String id;

/*
            for(int i=0; i<tripResults.size(); i++){
                //Trip Option ID
                id= tripResults.get(i).getId();
                System.out.println("id "+id);

                //Slice
                List<SliceInfo> sliceInfo= tripResults.get(i).getSlice();
                for(int j=0; j<sliceInfo.size(); j++){
                    int duration= sliceInfo.get(j).getDuration();
                    System.out.println("duration "+duration);
                    List<SegmentInfo> segInfo= sliceInfo.get(j).getSegment();
                    for(int k=0; k<segInfo.size(); k++){
                        String bookingCode= segInfo.get(k).getBookingCode();
                        System.out.println("bookingCode "+bookingCode);
                        FlightInfo flightInfo=segInfo.get(k).getFlight();
                        String flightNum= flightInfo.getNumber();
                        System.out.println("flightNum "+flightNum);
                        String flightCarrier= flightInfo.getCarrier();
                        System.out.println("flightCarrier "+flightCarrier);

                        sb.append(flightCarrier + " ");
                        sb.append(flightNum + " ");

                        List<LegInfo> leg=segInfo.get(k).getLeg();
                        for(int l=0; l<leg.size(); l++){
                            String aircraft= leg.get(l).getAircraft();
                            System.out.println("aircraft "+aircraft);
                            String departTime=leg.get(l).getDepartureTime();
                            System.out.println("departTime "+departTime);
                            String arrivalTime= leg.get(l).getArrivalTime();
                            System.out.println("arrivalTime "+arrivalTime);

                            String dest=leg.get(l).getDestination();
                            System.out.println("Destination "+dest);

                            sb.append(departTime.substring(11,16) + "-");
                            sb.append(arrivalTime.substring(11,16) + " ");

                            String destTer= leg.get(l).getDestinationTerminal();
                            System.out.println("DestTer "+destTer);
                            String origin=leg.get(l).getOrigin();
                            System.out.println("origun "+origin);
                            String originTer=leg.get(l).getOriginTerminal();
                            System.out.println("OriginTer "+originTer);
                            int durationLeg= leg.get(l).getDuration();
                            System.out.println("durationleg "+durationLeg);
                            int mil= leg.get(l).getMileage();
                            System.out.println("Milleage "+mil);

                        }

                    }
                }

                //Pricing
                List<PricingInfo> priceInfo= tripResults.get(i).getPricing();
                for(int p=0; p<priceInfo.size(); p++){
                    String price= priceInfo.get(p).getSaleTotal();
                    System.out.println("Price "+price);
                    sb.append(price);
                }
                sb.append('\n');
            }
            */
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
        if (success)
            result.setText(sb.toString());
    }
}