package team49.comfortfly;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ChatroomAdapter extends ArrayAdapter<Chat_board.Chatroom> {

    List<Chat_board.Chatroom> members;

    ChatroomAdapter(Context context, List<Chat_board.Chatroom> members) {
        super(context, 0, members);
        this.members = members;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Chat_board.Chatroom s = super.getItem(position);
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_chatroom_adapter, null);
        TextView textViewChatroomMember = convertView.findViewById(R.id.textViewChatroomMember);
        textViewChatroomMember.setText(s.member);

        return convertView;
    }
}