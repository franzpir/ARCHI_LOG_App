package com.archilog.archi_log_demassue_piron.Presenter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import com.archilog.archi_log_demassue_piron.Model.Adapter.UserAdapter;
import com.archilog.archi_log_demassue_piron.Model.HTTPRequests.GetUserContactsRequest;
import com.archilog.archi_log_demassue_piron.Model.User;
import com.archilog.archi_log_demassue_piron.Views.MessagesActivity;
import com.archilog.archi_log_demassue_piron.Views.UsersFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class UsersFragmentPresenter implements Observer {
    private UsersFragment usersFragment;
    private Context context;
    private GetUserContactsRequest getUserContactsRequest;
    private UserAdapter userAdapter;
    private RecyclerView recyclerView;
    private List<User> contacts;

    public UsersFragmentPresenter(Context context, UsersFragment usersFragment) {
        this.context = context;
        this.getUserContactsRequest = new GetUserContactsRequest(context);
        this.getUserContactsRequest.addObserver(this);
        this.usersFragment = usersFragment;
        this.recyclerView = usersFragment.getRecyclerView();
        this.contacts = new ArrayList<>();
    }

    public void getUserContacts() {
        getUserContactsRequest.getUserContacts();
    }


    @Override
    public void update(Observable observable, Object o) {
        if(getUserContactsRequest.gotAnError() == false) {
            contacts = getUserContactsRequest.getContacts();
            userAdapter = new UserAdapter(context, this);
            recyclerView.setAdapter(userAdapter);
        } else {
            usersFragment.printToast("Erreur lors du chargement des contacts.");
        }
    }

    public int getListOfContactsSize() {
        return contacts.size();
    }

    public void onBindUserRowViewAtPosition(int position, UserRowView rowView) {
        User contact = contacts.get(position);
        rowView.setPicture();
        rowView.setUsername(contact.getFormattedName());
    }

    public void constructIntentToGoToMessagesActivity(int position) {
        User user = contacts.get(position);
        Intent intent = new Intent(context, MessagesActivity.class);
        intent.putExtra("userId", user.getUserId());
        intent.putExtra("formattedName", user.getFormattedName());
        context.startActivity(intent);
    }
}
