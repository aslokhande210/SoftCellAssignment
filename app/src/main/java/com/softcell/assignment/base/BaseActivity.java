package com.softcell.assignment.base;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.softcell.assignment.dependency.DaggerDependencies;
import com.softcell.assignment.dependency.Dependencies;
import com.softcell.assignment.networking.NetworkModule;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity {
    protected Dependencies dependencies;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dependencies = DaggerDependencies.builder().networkModule(new NetworkModule()).build();
    }

    protected Dependencies getDependencies() {
        return dependencies;
    }
}
