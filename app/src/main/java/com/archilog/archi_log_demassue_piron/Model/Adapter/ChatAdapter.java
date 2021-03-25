package com.archilog.archi_log_demassue_piron.Model.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.archilog.archi_log_demassue_piron.Presenter.ChatRowView;
import com.archilog.archi_log_demassue_piron.Presenter.MessagesActivityPresenter;
import com.archilog.archi_log_demassue_piron.R;

import static com.archilog.archi_log_demassue_piron.Model.Constants.MESSAGE_TYPE_RIGHT;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private Context context;
    private MessagesActivityPresenter messagesActivityPresenter;

    public ChatAdapter(Context context, MessagesActivityPresenter messagesActivityPresenter) {
        this.context = context;
        this.messagesActivityPresenter = messagesActivityPresenter;
    }

    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        if(viewType == MESSAGE_TYPE_RIGHT) {
            view = LayoutInflater.from(context).inflate(R.layout.chat_item_right, parent, false);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.chat_item_left, parent, false);
        }

        return new ChatAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChatAdapter.ViewHolder holder, int position) {

        messagesActivityPresenter.onBindChatRowViewAtPosition(position, holder);
    }

    @Override
    public int getItemCount() {
        return messagesActivityPresenter.getChatsSize();
    }

    @Override
    public int getItemViewType(int position) {
        return messagesActivityPresenter.setMessageType(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements ChatRowView {
        private TextView message_toShow;
        private ImageView profile_image;

        public ViewHolder(View view) {
            super(view);

            message_toShow = view.findViewById(R.id.chat_item_message_content);
            profile_image = view.findViewById(R.id.chat_item_profile_image);
        }

        @Override
        public void setText(String text) {
            message_toShow.setText(text);
        }

        @Override
        public void setImageResource() {
            profile_image.setImageResource(R.drawable.person);
        }
    }
}
