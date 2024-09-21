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

import gr.galeos.seniortracker.databinding.ActivityMainBinding;

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
        // Set Toolbar
        setSupportActionBar(binding.toolbar);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.fragment_home,R.id.fragment_dashboard,R.id.fragment_maps, R.id.fragment_notifications)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        destinationListener(navController);
    }

    private void destinationListener(@NonNull NavController navController) {
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (getSupportActionBar() != null) {  // Check if the ActionBar is available
                if (destination.getId() == R.id.fragment_home) {
                    navView.setVisibility(View.VISIBLE);
                    getSupportActionBar().show();
                    getSupportActionBar().setTitle("Home");
                }else if (destination.getId() == R.id.fragment_maps) {
                    navView.setVisibility(View.VISIBLE);
                    getSupportActionBar().hide();
                } else if (destination.getId() == R.id.fragment_dashboard) {
                    navView.setVisibility(View.VISIBLE);
                    getSupportActionBar().show();
                    getSupportActionBar().setTitle("Dashboard");
                } else if (destination.getId() == R.id.fragment_notifications) {
                    navView.setVisibility(View.VISIBLE);
                    getSupportActionBar().show();
                    getSupportActionBar().setTitle("Notifications");
                } else if (destination.getId() == R.id.fragment_signup) {
                    navView.setVisibility(View.GONE);
                    getSupportActionBar().hide();
                } else if (destination.getId() == R.id.fragment_login) {
                    navView.setVisibility(View.GONE);
                    getSupportActionBar().hide();
                } else if (destination.getId() == R.id.fragment_account) {
                    navView.setVisibility(View.GONE);
                    getSupportActionBar().hide();

                }
            }
        });
    }



    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp();
    }
}