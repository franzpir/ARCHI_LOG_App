package com.archilog.archi_log_demassue_piron.Views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.archilog.archi_log_demassue_piron.Presenter.InfosOfOwnHousePresenter;
import com.archilog.archi_log_demassue_piron.R;

public class InfosOfOwnHouseActivity extends AppCompatActivity {

    private TextView textView_houseFrom, textView_address;

    private InfosOfOwnHousePresenter infosOfOwnHousePresenter;

    private ExpandableListView expandableListViewForPassages, expandableListViewForServants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infosownhouse);

        textView_houseFrom = findViewById(R.id.activity_infosownhouse_houseFrom);
        textView_address = findViewById(R.id.activity_infosownhouse_houseFromAddress);
        expandableListViewForPassages = findViewById(R.id.activity_infosownhouse_expandableListViewForPassages);
        expandableListViewForServants = findViewById(R.id.activity_infosownhouse_expandableListViewForServants);

        infosOfOwnHousePresenter = new InfosOfOwnHousePresenter(getApplicationContext(), getHouseIdFromIntentExtra(), this);

        infosOfOwnHousePresenter.constructExpandableListViewForPassages();

        expandableListViewForServants.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                infosOfOwnHousePresenter.handleClick(childPosition);

                return false;
            }
        });
    }

    public ExpandableListView getExpandableListViewForPassages() {
        return expandableListViewForPassages;
    }

    public ExpandableListView getExpandableListViewForServants() {
        return expandableListViewForServants;
    }

    public void setExpandableListViewForPassages(ExpandableListView expandableListViewForPassages) {
        this.expandableListViewForPassages = expandableListViewForPassages;
    }

    public void setExpandableListViewForServants(ExpandableListView expandableListViewForServants) {
        this.expandableListViewForServants = expandableListViewForServants;
    }

    private String getHouseIdFromIntentExtra() {
        Intent intent = getIntent();
        return intent.getStringExtra("houseId");
    }

    public void setTextsForTheView(String houseAddress, String userName) {
        textView_address.setText(houseAddress);
        textView_houseFrom.setText("Maison de " + userName);
    }


}
