package team49.comfortfly;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Chat extends AppCompatActivity {

    private static String groupId;

    ListView lView;
    List<Message> groupChatList;
    List<String> groupChatUserName;
    Button mSubmitMsgButton;
    EditText message_editText;

    public Chat() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_chat, container, false);
        groupId = getArguments().getString("groupId");
        lView = (ListView) rootView.findViewById(R.id.groupChatListView);

        new GroupChatDownloadTask(getContext()).execute();

        message_editText = (EditText) rootView.findViewById(R.id.message_editText);
        mSubmitMsgButton = (Button) rootView.findViewById(R.id.submit_msg_btn);
        mSubmitMsgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = message_editText.getText().toString();
                if (!msg.equals("")) {
                    mSubmitMsgButton.setEnabled(false);
                    new SendGroupChatTask(getContext()).execute(msg);
                } else {
                    message_editText.setError("Say something!");
                }
            }
        });

        return rootView;
    }


    public void setUpListView() {
        GroupChatAdapter apt = new GroupChatAdapter(getContext(), groupChatList, groupChatUserName);
        lView.setAdapter(apt);
        lView.setSelection(apt.getCount() - 1);
        lView.setDivider(null);
        lView.setDividerHeight(0);

    }

    public void sortListViewByTime() {
        groupChatList = new ArrayList<>(groupChatList);
        Collections.sort(groupChatList, new Comparator<Message>() {
            @Override
            public int compare(Message lhs, Message rhs) {
                return lhs.getCreatedDate().compareTo(rhs.getCreatedDate());
            }
        });
    }

    class ChatDownloadTask extends AsyncTask<Void, Void, Boolean> {

        private Context context;

        @Override
        protected Boolean doInBackground(Void... params) {
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {

        }
    }
}

