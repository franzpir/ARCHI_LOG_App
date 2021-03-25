package com.archilog.archi_log_demassue_piron.Views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import androidx.fragment.app.Fragment;
import com.archilog.archi_log_demassue_piron.Presenter.HomeFragmentPresenter;
import com.archilog.archi_log_demassue_piron.R;

public class HomeFragment extends Fragment {

    private ExpandableListView expandableListView;
    private HomeFragmentPresenter homeFragmentPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        expandableListView = view.findViewById(R.id.fragment_home_expandableListView);

        homeFragmentPresenter = new HomeFragmentPresenter(getContext(), this);
        homeFragmentPresenter.constructExpandableListView();

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                homeFragmentPresenter.handleClick(groupPosition, childPosition);

                return false;
            }
        });

        return view;
    }

    public ExpandableListView getExpandableListView() {
        return expandableListView;
    }

    public void setExpandableListView(ExpandableListView expandableListView) {
        this.expandableListView = expandableListView;
    }
}
