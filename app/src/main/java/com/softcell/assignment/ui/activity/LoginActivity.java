package com.softcell.assignment.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.softcell.assignment.R;
import com.softcell.assignment.api.presenter.LoginPresenter;
import com.softcell.assignment.api.view.LoginView;
import com.softcell.assignment.base.BaseActivity;
import com.softcell.assignment.model.ResponseToken;
import com.softcell.assignment.networking.NetworkError;
import com.softcell.assignment.networking.Service;

import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements LoginView {

    @BindView(R.id.username)
    TextInputEditText username;
    @BindView(R.id.username_layout)
    TextInputLayout usernameLayout;
    @BindView(R.id.password)
    TextInputEditText password;
    @BindView(R.id.password_layout)
    TextInputLayout passwordLayout;
    @BindView(R.id.login_layout)
    LinearLayout loginLayout;
    @BindView(R.id.login)
    Button login;
    @BindView(R.id.text_forgot_password)
    TextView textForgotPassword;

    @Inject
    public Service service;
    private LoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDependencies().inject(this);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        loginPresenter = new LoginPresenter(service, this);
    }

    @OnClick({R.id.login, R.id.text_forgot_password})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login:
                if (isFormValid()) {
                    loginPresenter.login(Objects.requireNonNull(username.getText()).toString(), Objects.requireNonNull(password.getText()).toString());
                }
                break;
            case R.id.text_forgot_password:
                //Reset the password.
                break;
        }
    }

    private boolean isFormValid() {

        boolean valid = true;

        if (TextUtils.isEmpty(Objects.requireNonNull(username.getText())) /*||
                !Patterns.EMAIL_ADDRESS.matcher(Objects.requireNonNull(username.getText()).toString()).matches()*/) {
            usernameLayout.setError(getString(R.string.enter_valid_email));
            valid = false;
        }

        if (TextUtils.isEmpty(Objects.requireNonNull(username.getText()))) {
            passwordLayout.setError(getString(R.string.password_cannot_be_empty));
            valid = false;
        }
        if (valid) {
            usernameLayout.setErrorEnabled(false);
            passwordLayout.setErrorEnabled(false);
        }
        return valid;
    }

    private ProgressDialog mProgressDialog = null;

    @Override
    public void showProgress(boolean show) {
        if (show) {
            if (mProgressDialog == null) {
                mProgressDialog = new ProgressDialog(this);
                mProgressDialog.setMessage(getString(R.string.logging_in));
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mProgressDialog.show();
            }
        } else {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
                mProgressDialog = null;
            }
        }
    }

    @Override
    public void loggedIn(ResponseToken token) {
        Toast.makeText(this, R.string.login_successful, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, UserListActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void failed(NetworkError error) {
        Toast.makeText(this, error.getAppErrorMessage(), Toast.LENGTH_SHORT).show();
    }
}
