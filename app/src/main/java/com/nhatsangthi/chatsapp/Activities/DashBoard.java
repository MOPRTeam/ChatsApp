package com.nhatsangthi.chatsapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.nhatsangthi.chatsapp.Fragments.GroupFragment;
import com.nhatsangthi.chatsapp.Fragments.MainFragment;
import com.nhatsangthi.chatsapp.R;
import com.nhatsangthi.chatsapp.Utils.Util;

public class DashBoard extends AppCompatActivity {

    private ChipNavigationBar navigationBar;
    Fragment fragment = null;
    Fragment mainFragment = new MainFragment();
    Fragment groupFragment = new GroupFragment();
    private Util util;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        util = new Util();
        navigationBar = findViewById(R.id.navigationChip);

        if (savedInstanceState == null) {
            navigationBar.setItemSelected(R.id.chat, true);
            getSupportFragmentManager().beginTransaction().replace(R.id.dashboardContainer, new MainFragment()).commit();
        }

        navigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                switch (i) {

                    case R.id.chat:
//                        fragment = new MainFragment();
                        replaceFragment(mainFragment);
                        break;
                    case R.id.group:
//                        fragment = new GroupFragment();
                        replaceFragment(groupFragment);
                        break;
                    case R.id.profile:
//                        fragment = new ProfileFragment();
                }

//                if (fragment != null)
//                    getSupportFragmentManager().beginTransaction().replace(R.id.dashboardContainer, fragment).commit();
            }
        });


    }

    @Override
    protected void onResume() {
        util.updateOnlineStatus("Online");
        super.onResume();
    }

    @Override
    protected void onPause() {
        util.updateOnlineStatus("Offline");
        super.onPause();
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        fragmentTransaction.replace(R.id.dashboardContainer, fragment);
        fragmentTransaction.addToBackStack(fragment.toString());
        fragmentTransaction.commit();
    }
}