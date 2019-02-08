package com.softcell.assignment.api.view;

import com.softcell.assignment.model.User;
import com.softcell.assignment.networking.NetworkError;

import java.util.ArrayList;

public interface AddApplicantView {

    void showProgress(boolean show);

    void onApplicantAdded(User user);

    void onApplicantsReceived(ArrayList<User> users);

    void failed(NetworkError error);
}
