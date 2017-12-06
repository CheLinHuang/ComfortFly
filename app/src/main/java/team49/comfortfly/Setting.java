package team49.comfortfly;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;


public class Setting extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 1888;
    TextView textView_setting;
    EditText editNickName;
    Button newNameBtn;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        textView_setting = (TextView) findViewById(R.id.textView_setting);
        editNickName = (EditText) findViewById(R.id.editNickName);
        newNameBtn = (Button) findViewById(R.id.newNameBtn);
        newNameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newNameBtn.setEnabled(false);
                new editNickNameTask().execute(editNickName.getText().toString());
            }
        });

        imageView = (ImageView) this.findViewById(R.id.imageView);
        new getImageTask().execute();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);
        }
    }

    class getImageTask extends AsyncTask<Void, Void, Void> {

        Bitmap bitmap;

        @Override
        protected Void doInBackground(Void... params) {
            try {
                bitmap = BitmapFactory.decodeStream((InputStream) new URL("https://ws.engr.illinois.edu/directory/viewphoto.aspx?id=4960&s=215&type=portrait").getContent());

                ByteArrayOutputStream bao = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bao);
                byte[] ba = bao.toByteArray();
                String baImage = new String(ba, "UTF-8");

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://fa17-cs411-49.cs.illinois.edu/api/picture?token=" + Home.token);

                httppost.setEntity(new StringEntity(baImage));

                httppost.setHeader("Content-type", "image/jpg");
                HttpResponse response = httpclient.execute(httppost);
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

        @Override
        protected void onPostExecute(Void result) {
            if (bitmap != null)
                imageView.setImageBitmap(bitmap);
        }
    }

    class editNickNameTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://fa17-cs411-49.cs.illinois.edu/api/account?token=" + Home.token);
            try {
                httppost.setEntity(new StringEntity("{\"action\":\"edit\",\"name\":\"" + params[0] + "\"}"));
                HttpResponse response = httpclient.execute(httppost);
                System.out.println(response.toString());

                System.out.println(response.getStatusLine().getStatusCode());
                if (response.getStatusLine().getStatusCode() == 200) {
                    String responseString = EntityUtils.toString(response.getEntity());
                    System.out.println(responseString);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            editNickName.setText("");
            newNameBtn.setEnabled(true);
        }
    }
}
