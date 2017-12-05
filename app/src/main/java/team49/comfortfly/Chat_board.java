package team49.comfortfly;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Chat_board extends AppCompatActivity {

    List<Chatroom> list;
    ListView listViewChatBoard;
    ChatroomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_board);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabChat);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(Chat_board.this, Chat.class);
//                startActivity(i);
            }
        });
        listViewChatBoard = (ListView) findViewById(R.id.listViewChatrooms);
        list = new ArrayList<>();
        new PullChatRoomTask().execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        list = new ArrayList<>();
        new PullChatRoomTask().execute();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        list = new ArrayList<>();
        new PullChatRoomTask().execute();
    }

    class CreateChatroomTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://fa17-cs411-49.cs.illinois.edu/api/chatroom?token=" + Home.token);

            try {
                httppost.setEntity(new StringEntity("{\"action\":\"create\",\"addmember\":\"" + params[0] + "\"}"));
                HttpResponse response = httpclient.execute(httppost);
                System.out.println(response.toString());

                System.out.println(response.getStatusLine().getStatusCode());
                if (response.getStatusLine().getStatusCode() == 200) {
                    String responseString = EntityUtils.toString(response.getEntity());
                    System.out.println(responseString);
                    JSONObject obj = new JSONObject(responseString);
                    return obj.getString("chatroomid");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null && !result.equals("")) {
                Intent i = new Intent(Chat_board.this, Chat.class);
                i.putExtra("chatroomid", result);
                startActivity(i);
            }
        }
    }

    class DeleteChatroomTask extends AsyncTask<Chatroom, Void, Boolean> {

        Chatroom chatroom;

        @Override
        protected Boolean doInBackground(Chatroom... params) {

            this.chatroom = params[0];

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://fa17-cs411-49.cs.illinois.edu/api/chatroom?token=" + Home.token);

            try {
                httppost.setEntity(new StringEntity("{\"action\":\"delete\",\"chatroomid\":\"" + chatroom.ID + "\"}"));
                HttpResponse response = httpclient.execute(httppost);
                System.out.println(response.toString());
                System.out.println(response.getStatusLine().getStatusCode());
                if (response.getStatusLine().getStatusCode() == 200) {
                    String responseString = EntityUtils.toString(response.getEntity());
                    System.out.println(responseString);
                    JSONObject obj = new JSONObject(responseString);
                    if (obj.getString("result").equals("success"))
                        return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                list.remove(chatroom);
                adapter.notifyDataSetChanged();
            }
        }
    }

    private class PullChatRoomTask extends AsyncTask<Trip, Void, Void> {

        List<Chatroom> list = new ArrayList<>();

        @Override
        protected Void doInBackground(Trip... params) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://fa17-cs411-49.cs.illinois.edu/api/chatroom?token=" + Home.token);

            try {
                httppost.setEntity(new StringEntity("{\"action\":\"show\"}"));
                HttpResponse response = httpclient.execute(httppost);
                System.out.println(response.getStatusLine().getStatusCode());

                if (response.getStatusLine().getStatusCode() == 200) {
                    String responseString = EntityUtils.toString(response.getEntity());
                    System.out.println(responseString);

                    JSONObject obj = new JSONObject(responseString);

                    JSONArray arr = obj.getJSONArray("chatroom");
                    for (int i = 0; i < arr.length(); i++) {
                        String ID = arr.getJSONObject(i).getString("id");
                        StringBuilder member = new StringBuilder();

                        JSONArray members = arr.getJSONObject(i).getJSONArray("member");
                        for (int j = 0; j < members.length(); j++) {
                            member.append(members.get(j));
                            if (j != members.length() - 1)
                                member.append(",");
                        }

                        list.add(new Chatroom(ID, member.toString()));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            // TODO
            // Add adapter
            //listViewChatBoard.setAdapter();

            adapter = new ChatroomAdapter(getApplicationContext(), list);
            listViewChatBoard.setAdapter(adapter);

            listViewChatBoard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Chatroom chatroom = (Chatroom) parent.getItemAtPosition(position);
                    Intent i = new Intent(Chat_board.this, Chat.class);
                    i.putExtra("chatroomid", chatroom.ID);
                    startActivity(i);
                }
            });

            listViewChatBoard.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(
            ) {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    final Chatroom chatroom = (Chatroom) parent.getItemAtPosition(position);
                    final AlertDialog.Builder builder = new AlertDialog.Builder(Chat_board.this);
                    builder.setTitle("Alert");
                    builder.setMessage("Delete this chatroom?");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            new DeleteChatroomTask().execute(chatroom);
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    builder.create().show();
                    return true;
                }
            });
        }
    }

    class Chatroom {

        String ID;
        String member;

        Chatroom(String ID, String member) {
            this.ID = ID;
            this.member = member;
        }
    }
}
