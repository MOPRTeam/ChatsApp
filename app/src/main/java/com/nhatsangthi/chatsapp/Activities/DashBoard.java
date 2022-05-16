package com.nhatsangthi.chatsapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.nhatsangthi.chatsapp.Fragments.GroupFragment;
import com.nhatsangthi.chatsapp.Fragments.MainFragment;
import com.nhatsangthi.chatsapp.R;
import com.nhatsangthi.chatsapp.Utils.Util;

public class DashBoard extends AppCompatActivity {

    private ChipNavigationBar navigationBar;
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
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        fragmentTransaction.replace(R.id.dashboardContainer, fragment);
        fragmentTransaction.addToBackStack(fragment.toString());
        fragmentTransaction.commit();
    }
}