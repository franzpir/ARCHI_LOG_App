package com.archilog.archi_log_demassue_piron.Views;

import android.os.Bundle;

import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import com.archilog.archi_log_demassue_piron.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainPageActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    public MainPageActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);

        switchFragment(new HomeFragment());

        bottomNavigationView = findViewById(R.id.activity_mainpage_bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        Fragment selectedFragment = null;

                        switch (item.getItemId()) {
                            case R.id.navigation_home:
                                selectedFragment = new HomeFragment();
                                break;
                            case R.id.navigation_mail:
                                selectedFragment = new UsersFragment();
                                break;
                            case R.id.navigation_map:
                                selectedFragment = new MapFragment();
                                break;
                            case R.id.navigation_settings:
                                selectedFragment = new SettingsFragment();
                                break;
                        }

                        switchFragment(selectedFragment);

                        return true;
                    }
                });
    }

    public void switchFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_mainpage_fragment_container, fragment).commit();

    }
}
