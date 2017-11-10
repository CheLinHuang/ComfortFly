package team49.comfortfly;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Home extends AppCompatActivity {

    Button Search;
    Button Show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Search = (Button) findViewById(R.id.searchButton);
        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Home.this, FlightSearch.class);
                startActivity(i);
            }
        });

        Show = (Button) findViewById(R.id.showButton);
        Show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(System.currentTimeMillis());
                Intent i = new Intent(Home.this, TripManagement.class);
                /*
                i.putExtra("origin", "CMI");
                i.putExtra("destination", "ORD");
                i.putExtra("departDate", System.currentTimeMillis());
                i.putExtra("returnDate", System.currentTimeMillis());
                i.putExtra("departTime", "11:00AM");
                i.putExtra("returnTime", "12:00PM");
                i.putExtra("airline", "AA");
                i.putExtra("flightNumber", "1234"); */
                startActivity(i);
            }
        });

    }
}
