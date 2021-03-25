package com.archilog.archi_log_demassue_piron.Views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.archilog.archi_log_demassue_piron.Presenter.DetailHousePresenter;
import com.archilog.archi_log_demassue_piron.R;

public class DetailHouseActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView textView_houseFrom, textView_address, textView_instructions;
    private Button button_iTakeCare, button_sendNote;
    private EditText editText_descriptionPassage, editText_noteDescription;
    private RatingBar ratingBar;

    private DetailHousePresenter detailHousePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_housedetails);

        textView_houseFrom = findViewById(R.id.activity_housedetails_houseFrom);
        textView_address = findViewById(R.id.activity_housedetails_houseFromAddress);
        textView_instructions = findViewById(R.id.activity_housedetails_instructions);
        button_iTakeCare = findViewById(R.id.activity_housedetails_btn_takeCare);
        button_sendNote = findViewById(R.id.activity_housedetails_sendNote);
        ratingBar = findViewById(R.id.activity_housedetails_ratingBar);
        editText_noteDescription = findViewById(R.id.activity_housedetails_noteDescription);
        button_iTakeCare.setOnClickListener(this);
        button_sendNote.setOnClickListener(this);
        editText_descriptionPassage = findViewById(R.id.activity_housedetails_descriptionPassage);

        detailHousePresenter = new DetailHousePresenter(this, this, getHouseIdFromIntentExtra());

        detailHousePresenter.getHousePublicProfile();
    }

    private String getHouseIdFromIntentExtra() {
        Intent intent = getIntent();
        return intent.getStringExtra("houseId");
    }

    public void setTextsForTheView(String houseAddress, String username, String instructions, String pictureURL) {
        textView_address.setText(houseAddress);
        textView_houseFrom.setText("Maison de " + username);
        textView_instructions.setText(instructions);
        setButtonDependingOnLocalisation();
    }

    private void setButtonDependingOnLocalisation() {
        if(detailHousePresenter.isCurrentUserNearbyTheServicingHouse()) {
            button_iTakeCare.setEnabled(false);
            button_iTakeCare.setBackgroundColor(R.color.colorDisabled);
            button_iTakeCare.setTextColor(R.color.colorTextDisabled);
        }
    }

    private void sendPassage() {
        String descriptionPassage = editText_descriptionPassage.getText().toString();
        String houseId = getHouseIdFromIntentExtra();

        detailHousePresenter.sendPassage(descriptionPassage, houseId);
    }

    private void sendNote() {
        String houseId = getHouseIdFromIntentExtra();
        int note = (int) ratingBar.getRating();
        String description = editText_noteDescription.getText().toString();

        detailHousePresenter.sendNote(houseId, description, note);
    }

    public void resetPassageDescription() {
        editText_descriptionPassage.setText("");
    }

    public void resetNoteRatingBarAndDescription() {
        editText_noteDescription.setText("");
        ratingBar.setRating(0);
    }

    public void printToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_housedetails_btn_takeCare:
                sendPassage();
                break;
            case R.id.activity_housedetails_sendNote:
                sendNote();
                break;
        }
    }
}
