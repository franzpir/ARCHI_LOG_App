package com.archilog.archi_log_demassue_piron.Views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.archilog.archi_log_demassue_piron.Presenter.MessagesActivityPresenter;
import com.archilog.archi_log_demassue_piron.R;

import static com.archilog.archi_log_demassue_piron.Model.Constants.SHARED_PREFERENCES;

public class MessagesActivity extends AppCompatActivity {
    private ImageView user_image;
    private TextView user_name;
    private EditText message_toSend;
    private ImageButton btn_toSend;

    private RecyclerView recyclerView;

    private String userId;

    private MessagesActivityPresenter messagesActivityPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        Toolbar toolbar = findViewById(R.id.message_activity_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        recyclerView = findViewById(R.id.message_activity_all_messages);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        user_image = findViewById(R.id.message_activity_profile_image);
        user_name = findViewById(R.id.message_activity_profile_name);
        btn_toSend = findViewById(R.id.message_activity_btn_send);
        message_toSend = findViewById(R.id.message_activity_to_send);

        Intent intent = getIntent();
        final String contactId = intent.getStringExtra("userId");
        final String contactName = intent.getStringExtra("formattedName");

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        userId = sharedPreferences.getString("userId", "noUserId");

        btn_toSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = message_toSend.getText().toString();
                if(message.equals("")) {
                    Toast.makeText(MessagesActivity.this, R.string.messagesActivity_lengthMessage_FR, Toast.LENGTH_LONG).show();
                } else {
                    messagesActivityPresenter.sendMessage(contactId, message);
                }

                message_toSend.setText("");
            }
        });

        user_name.setText(contactName);
        user_image.setImageResource(R.drawable.person);

        messagesActivityPresenter = new MessagesActivityPresenter(this, this);
        messagesActivityPresenter.readMessages(contactId);
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

}
