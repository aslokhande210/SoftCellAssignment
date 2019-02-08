package com.softcell.assignment.api.presenter;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.softcell.assignment.api.view.AddApplicantView;
import com.softcell.assignment.db.DatabaseHelper;
import com.softcell.assignment.model.User;
import com.softcell.assignment.networking.NetworkError;

import java.util.ArrayList;

public class ApplicantPresenter {
    private final AddApplicantView mAddApplicantView;
    private final DatabaseHelper databaseHelper;


    public ApplicantPresenter(Context context, AddApplicantView addApplicantView) {
        this.mAddApplicantView = addApplicantView;
        databaseHelper = OpenHelperManager.getHelper(context, DatabaseHelper.class);

    }

    public void addApplicant(User user) {
        mAddApplicantView.showProgress(true);

        try {
            databaseHelper.getUserDao().create(user);
            mAddApplicantView.onApplicantAdded(user);
        } catch (Exception e) {
            e.printStackTrace();
            mAddApplicantView.failed(new NetworkError(e));
        }

    }

    public void getUsers() {
        try {
            mAddApplicantView.onApplicantsReceived(new ArrayList<>(databaseHelper.getUserDao().queryForAll()));
        } catch (Exception e) {
            e.printStackTrace();
            mAddApplicantView.failed(new NetworkError(e));
        }
    }

}
