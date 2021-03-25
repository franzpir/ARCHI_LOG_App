package com.archilog.archi_log_demassue_piron.Views;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.archilog.archi_log_demassue_piron.Presenter.SignInPresenter;
import com.archilog.archi_log_demassue_piron.R;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText email, password;
    private SignInPresenter signInPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        email = findViewById(R.id.activity_signin_email);
        password = findViewById(R.id.activity_signin_password);
        findViewById(R.id.activity_signin_btn_login).setOnClickListener(this);

        signInPresenter = new SignInPresenter(this, this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_signin_btn_login:
                checkUsersData();
                break;
        }
    }

    private void checkUsersData() {
        String emailText = email.getText().toString();
        String passwordText = password.getText().toString();
        if(TextUtils.isEmpty(emailText) || TextUtils.isEmpty(passwordText)) {
            printToast("Veuillez vérifier les données du formulaire.");
        } else {
            signInPresenter.login(emailText, passwordText);

        }
    }

    public void printToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

}
