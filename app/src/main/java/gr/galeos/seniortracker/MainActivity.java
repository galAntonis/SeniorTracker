package gr.galeos.seniortracker;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Objects;

import gr.galeos.seniortracker.databinding.ActivityMainBinding;
import gr.galeos.seniortracker.utils.Constants;
import gr.galeos.seniortracker.utils.MessageEvent;
import gr.galeos.seniortracker.utils.SharedPreferencesUtils;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private AppBarConfiguration appBarConfiguration;
    private NavController navController;
    BottomNavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        navView = binding.navView;
        setSupportActionBar(binding.toolbar);

        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.fragment_home, R.id.fragment_dashboard, R.id.fragment_maps, R.id.fragment_notifications)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        SharedPreferencesUtils.initSharedPreferences(this.getApplicationContext());

        destinationListener(navController);
    }

    private void destinationListener(@NonNull NavController navController) {
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (getSupportActionBar() != null) {
                // Your existing destination change logic
                if (destination.getId() == R.id.fragment_home) {
                    navView.setVisibility(View.VISIBLE);
                    getSupportActionBar().hide();
                } else if (destination.getId() == R.id.fragment_maps) {
                    navView.setVisibility(View.VISIBLE);
                    getSupportActionBar().hide();
                } else if (destination.getId() == R.id.fragment_dashboard) {
                    navView.setVisibility(View.VISIBLE);
                    getSupportActionBar().show();
                    getSupportActionBar().setTitle(R.string.dashboard_title);
                } else if (destination.getId() == R.id.fragment_notifications) {
                    navView.setVisibility(View.VISIBLE);
                    getSupportActionBar().show();
                    getSupportActionBar().setTitle(R.string.notifications_title);
                } else if (destination.getId() == R.id.fragment_signup) {
                    navView.setVisibility(View.GONE);
                    getSupportActionBar().hide();
                } else if (destination.getId() == R.id.fragment_login) {
                    navView.setVisibility(View.GONE);
                    getSupportActionBar().hide();
                } else if (destination.getId() == R.id.fragment_settings) {
                    navView.setVisibility(View.GONE);
                    getSupportActionBar().show();
                    getSupportActionBar().setTitle(R.string.settings_title);
                } else if (destination.getId() == R.id.fragment_account) {
                    navView.setVisibility(View.GONE);
                    getSupportActionBar().show();
                    getSupportActionBar().setTitle(R.string.my_account_title);
                } else if (destination.getId() == R.id.fragment_manage_seniors) {
                    navView.setVisibility(View.GONE);
                    getSupportActionBar().show();
                    getSupportActionBar().setTitle(R.string.manage_seniors_title);
                } else if (destination.getId() == R.id.fragment_add_senior) {
                    navView.setVisibility(View.GONE);
                    getSupportActionBar().show();
                    getSupportActionBar().setTitle(R.string.add_senior_title);
                } else if (destination.getId() == R.id.fragment_edit_senior) {
                    navView.setVisibility(View.GONE);
                    getSupportActionBar().show();
                    getSupportActionBar().setTitle(R.string.edit_senior_title);
                } else if (destination.getId() == R.id.fragment_history) {
                    navView.setVisibility(View.GONE);
                    getSupportActionBar().show();
                    getSupportActionBar().setTitle(R.string.location_history_title);
                } else if (destination.getId() == R.id.fragment_language) {
                    navView.setVisibility(View.GONE);
                    getSupportActionBar().show();
                    getSupportActionBar().setTitle(R.string.choose_language_title);
                } else if(destination.getId() == R.id.fragment_account_settings) {
                    navView.setVisibility(View.GONE);
                    getSupportActionBar().show();
                    getSupportActionBar().setTitle(R.string.my_account_title);
                } else if (destination.getId() == R.id.fragment_manage_places) {
                    navView.setVisibility(View.GONE);
                    getSupportActionBar().show();
                    getSupportActionBar().setTitle(R.string.title_manage_places);
                } else if (destination.getId() == R.id.fragment_choose_senior) {
                    navView.setVisibility(View.GONE);
                    getSupportActionBar().show();
                    getSupportActionBar().setTitle(R.string.choose_senior);
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (Objects.equals(event.message, Constants.USER_LOGGED_IN)) {
            showServices();
        } else {
            hideServices();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        // Check session validity once when resuming activity
        if (SharedPreferencesUtils.isSessionIdValid()) {
            EventBus.getDefault().post(new MessageEvent(Constants.USER_LOGGED_IN));
        } else {
            EventBus.getDefault().post(new MessageEvent(Constants.USER_LOGOUT));
        }
    }

    private void showServices() {
        navView.getMenu().findItem(R.id.fragment_dashboard).setEnabled(true);
        navView.getMenu().findItem(R.id.fragment_maps).setEnabled(true);
        navView.getMenu().findItem(R.id.fragment_notifications).setEnabled(true);
        navView.setVisibility(View.VISIBLE);
    }

    private void hideServices() {
        navView.getMenu().findItem(R.id.fragment_dashboard).setEnabled(false);
        navView.getMenu().findItem(R.id.fragment_maps).setEnabled(false);
        navView.getMenu().findItem(R.id.fragment_notifications).setEnabled(false);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this); // Unregister EventBus to prevent memory leaks
    }
}
