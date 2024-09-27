package gr.galeos.seniortracker.ui.home;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.greenrobot.eventbus.EventBus;

import gr.galeos.seniortracker.R;
import gr.galeos.seniortracker.UserModel;
import gr.galeos.seniortracker.databinding.FragmentHomeBinding;
import gr.galeos.seniortracker.utils.Constants;
import gr.galeos.seniortracker.utils.MessageEvent;
import gr.galeos.seniortracker.utils.PermissionUtils;
import gr.galeos.seniortracker.utils.SharedPreferencesUtils;


public class HomeFragment extends Fragment {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean permissionDenied = false;
    private FusedLocationProviderClient fusedLocationClient;

    private double lat, lon;

    private FragmentHomeBinding binding;
    private HomeViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        setupViewModel();
        initUi();
        setupObservers();
        setClickListeners();

    }

    private void setupViewModel() {

        viewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
    }

    private void initUi() {

        SharedPreferencesUtils.initSharedPreferences(requireContext());
        if (SharedPreferencesUtils.isSessionIdValid() && SharedPreferencesUtils.retrieveAccountType().equals("0")) {
            initSupervisorUI();
            viewModel.getLocationData();
        } else if (SharedPreferencesUtils.isSessionIdValid() && SharedPreferencesUtils.retrieveAccountType().equals("1")) {
            initSeniorUI();
        } else {
            initLogoutUI();
        }
    }

    private void initSupervisorUI() {
        EventBus.getDefault().post(new MessageEvent(Constants.USER_LOGGED_IN));
        binding.tvTitle.setVisibility(View.VISIBLE);
        binding.tvSubtitle.setVisibility(View.VISIBLE);
        binding.includeLatestActivity.getRoot().setVisibility(View.VISIBLE);
        binding.includeYourSeniors.getRoot().setVisibility(View.VISIBLE);
        binding.loggedOutLayout.getRoot().setVisibility(View.GONE);
        binding.seniorsLayout.getRoot().setVisibility(View.GONE);
    }

    private void initSeniorUI() {
        EventBus.getDefault().post(new MessageEvent(Constants.USER_LOGGED_IN));
        binding.tvTitle.setVisibility(View.VISIBLE);
        binding.tvSubtitle.setVisibility(View.VISIBLE);
        binding.includeLatestActivity.getRoot().setVisibility(View.GONE);
        binding.includeYourSeniors.getRoot().setVisibility(View.GONE);
        binding.loggedOutLayout.getRoot().setVisibility(View.GONE);
        binding.seniorsLayout.getRoot().setVisibility(View.VISIBLE);
    }

    private void initLogoutUI() {
        EventBus.getDefault().post(new MessageEvent(Constants.USER_LOGOUT));
        binding.tvTitle.setVisibility(View.INVISIBLE);
        binding.tvSubtitle.setVisibility(View.INVISIBLE);
        binding.includeLatestActivity.getRoot().setVisibility(View.GONE);
        binding.includeYourSeniors.getRoot().setVisibility(View.GONE);
        binding.loggedOutLayout.getRoot().setVisibility(View.VISIBLE);
        binding.seniorsLayout.getRoot().setVisibility(View.GONE);
    }

    private void setupObservers() {

    }

    private void setClickListeners() {
        binding.seniorsLayout.broadcastButton.setOnClickListener(v -> {
            enableMyLocation();
            broadcastLocation();
        });

        binding.loggedOutLayout.loginButton.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_navigate_from_home_to_login);
        });
    }


    // [START maps_check_location_permission]
    @SuppressLint("MissingPermission")
    private void enableMyLocation() {
        // [START maps_check_location_permission]
        // 1. Check if permissions are granted, if so, enable the my location layer
        if (ContextCompat.checkSelfPermission(this.requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this.requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            return;
        }

        // 2. Otherwise, request location permissions from the user.
        PermissionUtils.requestLocationPermissions(this.requireActivity(), LOCATION_PERMISSION_REQUEST_CODE, true);
        // [END maps_check_location_permission]
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION) || PermissionUtils
                .isPermissionGranted(permissions, grantResults,
                        Manifest.permission.ACCESS_COARSE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Permission was denied. Display an error message
            // [START_EXCLUDE]
            // Display the missing permission error dialog when the fragments resume.
            permissionDenied = true;
            // [END_EXCLUDE]
        }
    }


    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getChildFragmentManager(), "dialog");
    }

    private void getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Permission is granted, get the last known location
            fusedLocationClient.getLastLocation()
                    .addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            Location location = task.getResult();
                            if (location != null) {
                                // Display the location coordinates in the TextView
                                lat = location.getLatitude();
                                lon = location.getLongitude();
                                String locationText = "Latitude: " + lat + "\nLongitude: " + lon;
                                Toast.makeText(requireContext(), locationText, Toast.LENGTH_LONG).show();

                            } else {
                                Toast.makeText(requireContext(), "Location not found", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        } else {
            // Handle case when permission is denied
            Toast.makeText(requireContext(), "Location permission denied", Toast.LENGTH_LONG).show();
        }
    }

    private void broadcastLocation() {
        getCurrentLocation();
        viewModel.writeLocationData(UserModel.getInstance().user.getId(), lon, lat);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}