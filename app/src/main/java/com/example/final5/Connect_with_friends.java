package com.example.final5;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/* loaded from: classes3.dex */
public class Connect_with_friends extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private ViewPager viewPager;

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_with_friends);
        this.bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        getSupportActionBar().hide();
        this.bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() { // from class: com.example.final5.Connect_with_friends$$ExternalSyntheticLambda0
            @Override // com.google.android.material.navigation.NavigationBarView.OnItemSelectedListener
            public final boolean onNavigationItemSelected(MenuItem menuItem) {
                return Connect_with_friends.this.lambda$onCreate$0$Connect_with_friends(menuItem);
            }
        });
        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, new Friendlist());
            transaction.commit();
        }
    }

    public /* synthetic */ boolean lambda$onCreate$0$Connect_with_friends(MenuItem item) {
        Fragment selectedFragment = null;
        switch (item.getItemId()) {
            case R.id.navigation_dashboard /* 2131362334 */:
                selectedFragment = new Find_Friends();
                break;
            case R.id.navigation_home /* 2131362336 */:
                selectedFragment = new Friendlist();
                break;
            case R.id.navigation_notifications /* 2131362337 */:
                selectedFragment = new notification_fragment();
                break;
            case R.id.navigation_profile /* 2131362338 */:
                selectedFragment = new profile();
                break;
        }
        if (selectedFragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, selectedFragment);
            transaction.commit();
            return true;
        }
        return true;
    }
}