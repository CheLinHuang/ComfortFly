package team49.comfortfly;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.api.services.qpxExpress.model.LegInfo;
import com.google.api.services.qpxExpress.model.SliceInfo;
import com.google.api.services.qpxExpress.model.TripOption;

import java.util.List;

public class SearchResultAdapter extends ArrayAdapter<TripOption> {

    List<TripOption> tripResults;

    public SearchResultAdapter(Context context, List<TripOption> tripResults) {
        super(context, 0, tripResults);
        this.tripResults = tripResults;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        TripOption result = super.getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_search_result_adapter, parent, false);
        }

        // Lookup view for data population
        TextView textViewFlightTime = (TextView) convertView.findViewById(R.id.textViewFlightTime);
        TextView textViewDuration = (TextView) convertView.findViewById(R.id.textViewDuration);
        TextView textViewFlightNum = (TextView) convertView.findViewById(R.id.textViewFlightNum);
        TextView textViewPrice = (TextView) convertView.findViewById(R.id.textViewPrice);

        // Populate the data into the template view
        SliceInfo sliceInfo = result.getSlice().get(0);
        LegInfo leg = sliceInfo.getSegment().get(0).getLeg().get(0);

        StringBuilder sb = new StringBuilder();

        int duration = sliceInfo.getDuration();
        if (duration > 60) {
            sb.append(duration / 60);
            sb.append("h ");
        }
        sb.append(duration % 60);
        sb.append("m");

        textViewDuration.setText(sb.toString());

        String departTime = leg.getDepartureTime();
        String arrivalTime = leg.getArrivalTime();

        sb = new StringBuilder();
        sb.append(departTime.substring(11, 16) + " - ");
        sb.append(arrivalTime.substring(11, 16));

        textViewFlightTime.setText(sb.toString());

        textViewFlightNum.setText(sliceInfo.getSegment().get(0).getFlight().getCarrier() + " " +
                sliceInfo.getSegment().get(0).getFlight().getNumber());

        textViewPrice.setText(result.getPricing().get(0).getSaleTotal());

        // Return the completed view to render on screen
        return convertView;
    }
}
