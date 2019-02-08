package com.softcell.assignment.networking;


import com.softcell.assignment.model.LoginRequest;
import com.softcell.assignment.model.ResponseToken;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class Service {
    private final NetworkService networkService;

    Service(NetworkService networkService) {
        this.networkService = networkService;
    }



    public Subscription login(final LoginCallback callback, LoginRequest loginRequest) {
        return networkService.login(loginRequest).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                onErrorResumeNext(Observable::error).subscribe(new Subscriber<ResponseToken>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                callback.onLoginFailed(new NetworkError(e));
            }

            @Override
            public void onNext(ResponseToken responseToken) {
                callback.onLoginSuccessful(responseToken);
            }
        });
    }

    public interface LoginCallback {

        void onLoginSuccessful(ResponseToken token);

        void onLoginFailed(NetworkError networkError);
    }
}
