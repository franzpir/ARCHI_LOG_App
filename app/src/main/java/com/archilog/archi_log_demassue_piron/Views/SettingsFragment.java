package com.archilog.archi_log_demassue_piron.Views;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.fragment.app.Fragment;
import com.archilog.archi_log_demassue_piron.Presenter.SettingsFragmentPresenter;
import com.archilog.archi_log_demassue_piron.R;

public class SettingsFragment extends Fragment implements View.OnClickListener {

    private Button disconnect;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        disconnect = view.findViewById(R.id.fragment_settings_disconnect);
        disconnect.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_settings_disconnect:
                logout();
                break;
        }

    }

    private void logout() {
        SettingsFragmentPresenter settingsFragmentPresenter = new SettingsFragmentPresenter(getContext());
        settingsFragmentPresenter.logOut();
        startActivity(new Intent(getActivity(), MainActivity.class));
    }
}
