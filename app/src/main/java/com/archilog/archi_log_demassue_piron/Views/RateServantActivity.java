package com.archilog.archi_log_demassue_piron.Views;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.archilog.archi_log_demassue_piron.Presenter.RateServantPresenter;
import com.archilog.archi_log_demassue_piron.R;

public class RateServantActivity extends AppCompatActivity implements View.OnClickListener {

    private Button sendNote;
    private EditText noteDescription;
    private RatingBar ratingBar;
    private TextView name;
    private RateServantPresenter rateServantPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rateservant);

        name = findViewById(R.id.activity_rateservant_servantName);
        sendNote = findViewById(R.id.activity_rateservant_sendNote);
        sendNote.setOnClickListener(this);
        noteDescription = findViewById(R.id.activity_rateservant_edittext);
        ratingBar = findViewById(R.id.activity_rateservant_ratingBar);

        rateServantPresenter = new RateServantPresenter(this, this, getIntent());

    }

    public void setTextView(String servantName) {
        name.setText(servantName);
    }

    private void sendNote() {
        int note = (int) ratingBar.getRating();
        String description = noteDescription.getText().toString();

        rateServantPresenter.sendNote(note, description);
    }

    public void resetView() {
        noteDescription.setText("");
        ratingBar.setRating(0);
    }

    public void printToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_rateservant_sendNote:
                sendNote();
                break;
        }
    }
}
