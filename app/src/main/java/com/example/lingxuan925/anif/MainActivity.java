package com.example.lingxuan925.anif;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private Fragment fragment1, fragment2, fragment3;
    private Fragment[] fragments;
    private int lastFragment;
    private BottomNavigationView navigation;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_events:
                    if(lastFragment != 0){
                        switchFragment(lastFragment,0);
                        lastFragment = 0;
                    }
                    return true;
                case R.id.navigation_friends:
                    if(lastFragment != 1){
                        switchFragment(lastFragment,1);
                        lastFragment = 1;
                    }
                    return true;
                case R.id.navigation_user:
                    if(lastFragment != 2){
                        switchFragment(lastFragment,2);
                        lastFragment = 2;
                    }
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFragment();
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }


    private void initFragment() {
        fragment1 = new Events();
        fragment2 = new Friends();
        fragment3 = new User();
        fragments = new Fragment[]{fragment1, fragment2, fragment3};
        lastFragment = 0;
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_frame,fragment1).show(fragment1).commit();
    }

    private void switchFragment(int last, int index) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.hide(fragments[last]);
        if (!fragments[index].isAdded())
            transaction.add(R.id.fragment_frame, fragments[index]);
        transaction.show(fragments[index]).commitAllowingStateLoss();
    }
}
