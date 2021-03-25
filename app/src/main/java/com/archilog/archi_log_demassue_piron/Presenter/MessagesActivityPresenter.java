package com.archilog.archi_log_demassue_piron.Presenter;

import android.content.Context;
import android.content.SharedPreferences;
import com.archilog.archi_log_demassue_piron.Model.Adapter.ChatAdapter;
import com.archilog.archi_log_demassue_piron.Model.Chat;
import com.archilog.archi_log_demassue_piron.Views.MessagesActivity;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.archilog.archi_log_demassue_piron.Model.Constants.*;

public class MessagesActivityPresenter {

    private DatabaseReference reference;
    private List<Chat> messages;
    private Context context;
    private MessagesActivity messagesActivity;
    private String userId;

    public MessagesActivityPresenter(Context context, MessagesActivity messagesActivity) {
        this.context = context;
        this.messagesActivity = messagesActivity;

        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        userId = sharedPreferences.getString("userId", "noUserId");
    }

    public void sendMessage(String receiver, String message) {
        reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> messageInformations = new HashMap<>();
        messageInformations.put("sender", userId);
        messageInformations.put("receiver", receiver);
        messageInformations.put("message", message);

        reference.child("Chats").push().setValue(messageInformations);
    }

    public void readMessages(final String userID) {
        final MessagesActivityPresenter messagesActivityPresenter = this;
        messages = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                messages.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    if(chat.getReceiver().equals(userId) && chat.getSender().equals(userID) ||
                            chat.getReceiver().equals(userID) && chat.getSender().equals(userId) ) {
                        messages.add(chat);
                    }

                    ChatAdapter chatAdapter = new ChatAdapter(context, messagesActivityPresenter);
                    messagesActivity.getRecyclerView().setAdapter(chatAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {}
        });


    }

    public int setMessageType(int position) {
        if(messages.get(position).getSender().equals(userId)) {
            return MESSAGE_TYPE_RIGHT;
        } else {
            return MESSAGE_TYPE_LEFT;
        }
    }

    public int getChatsSize() {
        return this.messages.size();
    }

    public void onBindChatRowViewAtPosition(int position, ChatRowView chatRowView) {
        Chat chat = messages.get(position);
        chatRowView.setText(chat.getMessage());
        chatRowView.setImageResource();
    }
}
