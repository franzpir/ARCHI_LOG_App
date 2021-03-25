package com.archilog.archi_log_demassue_piron.Views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.archilog.archi_log_demassue_piron.Presenter.UsersFragmentPresenter;
import com.archilog.archi_log_demassue_piron.R;

public class UsersFragment extends Fragment {

    private RecyclerView recyclerView;
    private UsersFragmentPresenter usersFragmentPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users, container, false);

        LinearLayoutManager manager = new LinearLayoutManager(view.getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView = view.findViewById(R.id.fragment_users_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);

        usersFragmentPresenter = new UsersFragmentPresenter(getContext(), this);

        loadContacts();

        return view;
    }

    private void loadContacts() {
        usersFragmentPresenter.getUserContacts();
    }

    public void printToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }
}
