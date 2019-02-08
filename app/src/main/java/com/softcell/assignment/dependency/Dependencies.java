package com.softcell.assignment.dependency;

import com.softcell.assignment.networking.NetworkModule;
import com.softcell.assignment.ui.activity.AddApplicantActivity;
import com.softcell.assignment.ui.activity.LoginActivity;
import com.softcell.assignment.ui.activity.UserListActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {NetworkModule.class,})
public interface Dependencies {

    void inject(LoginActivity loginActivity);


}