package team49.comfortfly;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class Chat extends AppCompatActivity {

    private String chatroomid;

    ListView lView;
    List<Message> chatList;
    Button mSubmitMsgButton;
    EditText message_editText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Intent intent = getIntent();
        chatroomid = intent.getStringExtra("chatroomid");

        lView = (ListView) findViewById(R.id.groupChatListView);

        new ChatDownloadTask().execute();

        message_editText = (EditText) findViewById(R.id.message_editText);
        mSubmitMsgButton = (Button) findViewById(R.id.submit_msg_btn);
        mSubmitMsgButton.setEnabled(false);
        mSubmitMsgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = message_editText.getText().toString();
                if (!msg.equals("")) {
                    mSubmitMsgButton.setEnabled(false);
                    new SendMsgTask().execute(msg);
                } else {
                    message_editText.setError("Say something!");
                }
            }
        });
    }

    public void setUpListView() {
        ChatAdapter apt = new ChatAdapter(getApplicationContext(), chatList);
        lView.setAdapter(apt);
    }

//    public void sortListViewByTime() {
//        chatList = new ArrayList<>(chatList);
//        Collections.sort(chatList, new Comparator<Message>() {
//            @Override
//            public int compare(Message lhs, Message rhs) {
//                return lhs.getCreatedDate().compareTo(rhs.getCreatedDate());
//            }
//        });
//    }

    class SendMsgTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://fa17-cs411-49.cs.illinois.edu/api/chatmessage?token=" + Home.token);

            try {
                httppost.setEntity(new StringEntity("{\"action\":\"add\",\"chatroomid\":\"" + chatroomid + "\"" +
                        ",\"content\":\"" + params[0] + "\"}"));
                HttpResponse response = httpclient.execute(httppost);

                System.out.println(response.toString());

                System.out.println(response.getStatusLine().getStatusCode());
                if (response.getStatusLine().getStatusCode() == 200) {
                    String responseString = EntityUtils.toString(response.getEntity());
                    System.out.println(responseString);

                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                message_editText.setText("");
            }
            new ChatDownloadTask().execute();
        }
    }

    class ChatDownloadTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://fa17-cs411-49.cs.illinois.edu/api/chatmessage?token=" + Home.token);

            try {
                httppost.setEntity(new StringEntity("{\"action\":\"search\",\"chatroomid\":\"" + chatroomid + "\"" +
                        ",\"time_from\":\"2017-12-1 00:00\",\"number\":10}"));
                HttpResponse response = httpclient.execute(httppost);

                System.out.println(response.toString());

                System.out.println(response.getStatusLine().getStatusCode());
                if (response.getStatusLine().getStatusCode() == 200) {
                    String responseString = EntityUtils.toString(response.getEntity());
                    System.out.println(responseString);
                    JSONObject obj = new JSONObject(responseString);

                    chatList.clear();

                    JSONArray arr = obj.getJSONArray("message");
                    for (int i = 0; i < arr.length(); i++) {
                        Message msg = new Message(arr.getJSONObject(i).getString("content"),
                                arr.getJSONObject(i).getString("sender"),
                                arr.getJSONObject(i).getString("time"));
                        chatList.add(msg);
                    }
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            setUpListView();
            mSubmitMsgButton.setEnabled(true);
        }
    }
}

