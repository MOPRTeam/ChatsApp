package com.nhatsangthi.chatsapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.nhatsangthi.chatsapp.Constants.AllConstants;
import com.nhatsangthi.chatsapp.Fragments.GroupFragment;
import com.nhatsangthi.chatsapp.Fragments.MainFragment;
import com.nhatsangthi.chatsapp.Models.User;
import com.nhatsangthi.chatsapp.R;
import com.nhatsangthi.chatsapp.Utils.Util;

public class DashBoard extends AppCompatActivity {

    private ChipNavigationBar navigationBar;
    Fragment mainFragment = new MainFragment();
    Fragment groupFragment = new GroupFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        navigationBar = findViewById(R.id.navigationChip);

        if (savedInstanceState == null) {
            navigationBar.setItemSelected(R.id.chat, true);
            replaceFragment(mainFragment);
        }

        navigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                switch (i) {

                    case R.id.chat:
                        replaceFragment(mainFragment);
                        break;
                    case R.id.group:
                        replaceFragment(groupFragment);
                        break;
                    case R.id.profile:
//                        fragment = new ProfileFragment();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        Util.updateOnlineStatus("Online");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Util.updateOnlineStatus("Offline");
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