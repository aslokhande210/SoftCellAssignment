package com.softcell.assignment.api.presenter;

import com.softcell.assignment.api.view.LoginView;
import com.softcell.assignment.model.LoginRequest;
import com.softcell.assignment.model.ResponseToken;
import com.softcell.assignment.networking.NetworkError;
import com.softcell.assignment.networking.Service;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class LoginPresenter {
    private final Service mService;
    private final LoginView mLoginView;
    private final CompositeSubscription mSubscriptions;


    public LoginPresenter(Service service, LoginView loginView) {
        this.mService = service;
        this.mLoginView = loginView;
        this.mSubscriptions = new CompositeSubscription();
    }

    public void login(String username, String password) {
        mLoginView.showProgress(true);

        Subscription subscription = mService.login(new Service.LoginCallback() {
            @Override
            public void onLoginSuccessful(ResponseToken token) {
                mLoginView.showProgress(false);
                mLoginView.loggedIn(token);
            }

            @Override
            public void onLoginFailed(NetworkError networkError) {
                mLoginView.showProgress(false);
                mLoginView.failed(networkError);
            }
        }, new LoginRequest(username, password));

        mSubscriptions.add(subscription);

    }

    public void onStop() {
        mSubscriptions.unsubscribe();
    }
}
