package com.archilog.archi_log_demassue_piron.Model.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.archilog.archi_log_demassue_piron.Presenter.UserRowView;
import com.archilog.archi_log_demassue_piron.Presenter.UsersFragmentPresenter;
import com.archilog.archi_log_demassue_piron.R;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context context;
    private UsersFragmentPresenter usersFragmentPresenter;

    public UserAdapter(Context context, UsersFragmentPresenter usersFragmentPresenter) {
        this.context = context;
        this.usersFragmentPresenter = usersFragmentPresenter;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item, parent, false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserAdapter.ViewHolder holder, final int position) {
        usersFragmentPresenter.onBindUserRowViewAtPosition(position, holder);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usersFragmentPresenter.constructIntentToGoToMessagesActivity(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return usersFragmentPresenter.getListOfContactsSize();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements UserRowView {
        private TextView username;
        private ImageView profile_image;

        public ViewHolder(View view) {
            super(view);

            username = view.findViewById(R.id.fragment_user_profile_name);
            profile_image = view.findViewById(R.id.fragment_user_profile_image);
        }

        @Override
        public void setUsername(String name) {
            username.setText(name);
        }

        @Override
        public void setPicture() {
            profile_image.setImageResource(R.drawable.person);
        }
    }
}
