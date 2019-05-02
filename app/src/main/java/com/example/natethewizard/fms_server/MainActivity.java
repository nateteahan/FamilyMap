package com.example.natethewizard.fms_server;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //To make drawable icons
        Iconify.with(new FontAwesomeModule());

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = new LoginFragment();
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
        else {
            // Leave blank intentionally
        }
    }

    protected void onLogin() {
        //Remove login fragment
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = (Fragment) fm.findFragmentById(R.id.fragment_container);

        if (fragment != null) {
            fm.beginTransaction().remove(fragment).commit();
        }

        Fragment mapFragment = fm.findFragmentById(R.id.fragment_container);
            mapFragment = new MapFragment();
            fm.beginTransaction().add(R.id.fragment_container, mapFragment).commit();
    }
}
