package com.archilog.archi_log_demassue_piron.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.archilog.archi_log_demassue_piron.Presenter.MainActivityPresenter;
import com.archilog.archi_log_demassue_piron.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private MainActivityPresenter mainActivityPresenter;
    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.activity_main_signInButtonWithGoogle).setOnClickListener(this);
        findViewById(R.id.activity_main_signIn).setOnClickListener(this);

        this.mainActivityPresenter = new MainActivityPresenter(this, this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_main_signIn:
                startActivity(new Intent(MainActivity.this, SignInActivity.class));
                break;
            case R.id.activity_main_signInButtonWithGoogle:
                signIn();
                break;
        }
    }

    private void signIn() {
        Intent signInIntent = MainActivityPresenter.getGoogleSignInClient().getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mainActivityPresenter.checkIfConnectionIsPossible(requestCode, data);
    }

    public void printToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}