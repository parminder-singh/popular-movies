package com.example.parmindr.popularmovies.activity;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.parmindr.popularmovies.R;
import com.example.parmindr.popularmovies.fragment.SettingsFragment;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getFragmentManager().beginTransaction().replace(R.id.activity_settings, new SettingsFragment()).commit();
    }
}
