package team49.comfortfly;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class Home extends AppCompatActivity {

    Button Search;
    Button Show;
    Button Setting;
    public static String token = "wu0lwqwia06gpe0dgy8x9k1m98zh1pud";

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
                Intent i = new Intent(Home.this, TripManagement.class);
                startActivity(i);
            }
        });

        Setting = (Button) findViewById(R.id.showSetting);
        Setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Home.this, Setting.class);
                startActivity(i);
            }
        });
    }

    class LoginTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httpget = new HttpPost("http://fa17-cs411-49.cs.illinois.edu/api/account");
                httpget.setEntity(new StringEntity("{\"action\":\"login\",\"email\":\"123@456.edu\",\"password\":\"12345\"}"));
                HttpResponse response = httpclient.execute(httpget);

                System.out.println(response.toString());

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
    }
}

