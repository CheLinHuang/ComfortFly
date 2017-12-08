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
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class Chat extends AppCompatActivity {

    ListView lView;
    List<Message> chatList;
    Button mSubmitMsgButton;
    ChatAdapter apt;
    EditText message_editText;
    SimpleDateFormat formatterChicago = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private String chatroomid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Intent intent = getIntent();
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        chatroomid = intent.getStringExtra("chatroomid");

        System.out.println(chatroomid);

        chatList = new ArrayList<>();
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
            message_editText.setText("");
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
                        ",\"time_from\":\"" + formatter.format(new Date(System.currentTimeMillis() + Integer.MAX_VALUE)) + "\",\"number\":30}"));
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
                                formatterChicago.format(formatter.parse(arr.getJSONObject(i).getString("time"))));
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
            apt = new ChatAdapter(getApplicationContext(), chatList);
            lView.setAdapter(apt);
            mSubmitMsgButton.setEnabled(true);
            new WebSocketThread(chatList).start();
        }
    }

    class WebSocketThread extends Thread {

        WebSocketClient websocket;
        List<Message> chatList;

        WebSocketThread(List<Message> chatList) {
            this.chatList = chatList;
        }

        @Override
        public void run() {
            try {
                URI url = new URI("ws://fa17-cs411-49.cs.illinois.edu:8888/ws");

                websocket = new WebSocketClient(url) {
                    @Override
                    public void onOpen(ServerHandshake serverHandshake) {
                        websocket.send(Home.email);
                    }

                    public void onMessage(String message) {
                        String[] msg = message.split(",");
                        if (msg[1].equals(chatroomid)) {
                            Message m = new Message(msg[2], msg[0], formatterChicago.format(new Date(System.currentTimeMillis())));
                            chatList.add(m);
                            apt.notifyDataSetChanged();
                        }
                        System.out.println("Got msg: " + message);
                    }

                    @Override
                    public void onClose(int i, String s, boolean b) {

                    }

                    @Override
                    public void onError(Exception e) {

                    }
                };

                // Establish WebSocket Connection
                websocket.connect();

                while (true) {
                    Thread.sleep(100);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}