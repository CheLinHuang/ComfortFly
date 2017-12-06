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
    EditText editText_oldPassword;
    EditText edit_password;
    EditText edit_password_confirm;
    Button newNameBtn;
    Button newPasswordBtn;
    Button logout_btn;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        textView_setting = (TextView) findViewById(R.id.textView_setting);

        editNickName = (EditText) findViewById(R.id.editNickName);
        editText_oldPassword = (EditText) findViewById(R.id.editText_oldPassword);
        edit_password = (EditText) findViewById(R.id.edit_password);
        edit_password_confirm = (EditText) findViewById(R.id.edit_password_confirm);

        newNameBtn = (Button) findViewById(R.id.newNameBtn);
        newNameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newNameBtn.setEnabled(false);
                new editNickNameTask().execute(editNickName.getText().toString());
            }
        });

        newPasswordBtn = (Button) findViewById(R.id.newPasswordBtn);
        newPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkPasswordMatch()) {
                    edit_password_confirm.setError("The password doesn't match!");
                    edit_password_confirm.requestFocus();
                } else if (!isPasswordValid(edit_password.getText().toString())) {
                    //edit_password.setError(getString(teamhardcoder.y_fi.R.string.error_invalid_password));
                } else {
                    newPasswordBtn.setEnabled(false);
                    String[] passwords = new String[2];
                    passwords[0] = editText_oldPassword.getText().toString();
                    passwords[1] = edit_password.getText().toString();
                    //new editPasswordTask(getApplicationContext()).execute(passwords);
                }
            }
        });

        logout_btn = (Button) findViewById(R.id.logout_btn);
        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout_btn.setEnabled(false);
                /*Intent intent = new Intent(Settings.this, Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);*/
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

    private boolean checkPasswordMatch() {
        return edit_password.getText().toString().equals(edit_password_confirm.getText().toString());
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    class getImageTask extends AsyncTask<Void, Void, Void> {

        Bitmap bitmap;

        @Override
        protected Void doInBackground(Void... params) {
            try {
                bitmap = BitmapFactory.decodeStream((InputStream) new URL("http://e1.mail.thecheesecakefactory.com/files/amf_cheesecake_factory/project_1/17-12-04_Day_of_10000_Slices_Teaser/promo1.jpg").getContent());

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
            System.out.println(bitmap.toString());
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
            newNameBtn.setEnabled(true);
        }
    }


    class editPasswordNameTask extends AsyncTask<String, Void, Boolean> {

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
            newNameBtn.setEnabled(true);
        }
    }

}
