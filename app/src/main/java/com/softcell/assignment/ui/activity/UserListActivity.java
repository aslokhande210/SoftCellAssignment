package com.softcell.assignment.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.softcell.assignment.R;
import com.softcell.assignment.api.presenter.ApplicantPresenter;
import com.softcell.assignment.api.view.AddApplicantView;
import com.softcell.assignment.base.BaseActivity;
import com.softcell.assignment.model.User;
import com.softcell.assignment.networking.NetworkError;
import com.softcell.assignment.ui.adapter.UserListAdapter;

import java.util.ArrayList;
import java.util.Collections;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserListActivity extends BaseActivity implements AddApplicantView {

    @BindView(R.id.users)
    RecyclerView users;
    @BindView(R.id.add_applicant)
    FloatingActionButton addApplicant;

    private ApplicantPresenter applicantPresenter;

    private final ArrayList<User> usersList = new ArrayList<>();

    private UserListAdapter userListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applicantPresenter = new ApplicantPresenter(this, this);
        setContentView(R.layout.activity_user_list);
        ButterKnife.bind(this);


        users.setLayoutManager(new LinearLayoutManager(this));

        userListAdapter = new UserListAdapter(this, usersList);
        users.setAdapter(userListAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        applicantPresenter.getUsers();
    }

    @OnClick(R.id.add_applicant)
    public void onViewClicked() {
        startActivity(new Intent(this, AddApplicantActivity.class));
    }

    @Override
    public void showProgress(boolean show) {

    }

    @Override
    public void onApplicantAdded(User user) {

    }

    @Override
    public void onApplicantsReceived(ArrayList<User> users) {
        if (users != null && !users.isEmpty()) {
            Collections.sort(users);
            usersList.clear();
            usersList.addAll(users);
            userListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void failed(NetworkError error) {

    }
}
