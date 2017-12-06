package team49.comfortfly;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ChatAdapter extends ArrayAdapter<Message> {

    private List<Message> groupChatList;

    public ChatAdapter(Context context, List<Message> groupChatList) {
        // context is Activity
        super(context, 0, groupChatList);
        this.groupChatList = groupChatList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Message msg = getItem(position);
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_chat_adapter, null);
        TextView chatTextViewContent = convertView.findViewById(R.id.chatTextViewContent);
        TextView chatTextViewUserName = convertView.findViewById(R.id.chatTextViewUserName);
        TextView chatTextViewTime = convertView.findViewById(R.id.chatTextViewTime);

        chatTextViewContent.setText(msg.content);
        chatTextViewUserName.setText(msg.sender);
        chatTextViewTime.setText(msg.time);

        return convertView;
    }
}

