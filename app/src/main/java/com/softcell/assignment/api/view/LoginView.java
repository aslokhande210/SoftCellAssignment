package com.softcell.assignment.api.view;

import com.softcell.assignment.model.ResponseToken;
import com.softcell.assignment.networking.NetworkError;

public interface LoginView {

    void showProgress(boolean show);

    void loggedIn(ResponseToken token);

    void failed(NetworkError error);
}
