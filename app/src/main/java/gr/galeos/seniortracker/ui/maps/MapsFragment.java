package gr.galeos.seniortracker.ui.maps;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import gr.galeos.seniortracker.R;
import gr.galeos.seniortracker.UserModel;
import gr.galeos.seniortracker.databinding.FragmentMapsBinding;
import gr.galeos.seniortracker.models.LocationModel;
import gr.galeos.seniortracker.models.User;
import gr.galeos.seniortracker.utils.PermissionUtils;
import gr.galeos.seniortracker.utils.SharedPreferencesUtils;
import gr.galeos.seniortracker.utils.dialogs.SeniorsDialogFragment;

public class MapsFragment extends Fragment implements
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback {

    private FragmentMapsBinding binding;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean permissionDenied = false;
    private GoogleMap map;

    private MapsViewModel viewModel; // ViewModel to handle location data
    private Marker currentMarker;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMapsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupViewModel();
        setupMap();
        setClickListeners();
        setupObservers();

        // Trigger fetch of the last location
        if (UserModel.getInstance().user.getAccountType().equals("0")) {
            viewModel.getMySeniors();
        }
    }

    private void setupViewModel(){
        viewModel = new ViewModelProvider(this).get(MapsViewModel.class);
    }

    private void setupMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    private void setClickListeners() {
        binding.seniorsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SeniorsDialogFragment.newInstance().show(getChildFragmentManager(), "dialog");
            }
        });
    }

    private void setupObservers() {
        viewModel.getSeniors().observe(getViewLifecycleOwner(), new Observer<ArrayList<User>>() {
            @Override
            public void onChanged(ArrayList<User> seniors) {
                if (seniors != null) {
                    loadData();
                } else {
                    Toast.makeText(requireContext(), "No seniors found", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Fetch the last known location and observe changes
        viewModel.getLastLocationsLiveData().observe(getViewLifecycleOwner(), new Observer<LocationModel>() {
            @Override
            public void onChanged(LocationModel lastLocation) {
                if (lastLocation != null && map != null && UserModel.getInstance().user.getAccountType().equals("0")) {
                    LatLng lastKnownLatLng = new LatLng(lastLocation.latitude, lastLocation.longitude);

                    // Remove the previous marker if it exists
                    if (currentMarker != null) {
                        currentMarker.remove();
                    }
                    // Add a new marker for the last known location and move the camera
                    currentMarker = map.addMarker(new MarkerOptions()
                            .position(lastKnownLatLng)
                            .title("Last Location")
                            .icon( BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                    map.moveCamera(CameraUpdateFactory.newLatLng(lastKnownLatLng));
                }
            }
        });
    }

    private void loadData() {
        if(SharedPreferencesUtils.isSessionIdValid() && SharedPreferencesUtils.seniorSelected()){
            Log.d("MapsFragment", "Selected senior: " + SharedPreferencesUtils.retrieveSelectedSenior());
            Log.d("MapsFragment", "Seniors: " + UserModel.getInstance().seniors);
            UserModel.getInstance().seniors.stream().filter( senior -> senior.getId().equals(SharedPreferencesUtils.retrieveSelectedSenior())).findFirst().ifPresent(senior -> {
                binding.tvName.setText(senior.getFirstname());
                binding.tvSurname.setText(senior.getLastname());
                viewModel.fetchLastLocationData(senior);
            });
        } else if (SharedPreferencesUtils.isSessionIdValid() && !SharedPreferencesUtils.seniorSelected()) {
            User senior = UserModel.getInstance().seniors.get(0);
            SharedPreferencesUtils.saveSelectedSenior(senior.getId());
            binding.tvName.setText(senior.getFirstname());
            binding.tvSurname.setText(senior.getLastname());
            viewModel.fetchLastLocationData(senior);
        } else if (!SharedPreferencesUtils.isSessionIdValid()) {
            Toast.makeText(requireContext(), "No session id found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        LatLng lastKnownLatLng = new LatLng(37.9838877, 23.7279186);
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(lastKnownLatLng, 18));
        if (!UserModel.getInstance().user.getAccountType().equals("0")) {
            map.setOnMyLocationButtonClickListener(this);
            map.setOnMyLocationClickListener(this);
            enableMyLocation();
        }
    }

    @SuppressLint("MissingPermission")
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this.requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this.requireContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true); // Enable blue dot on map
            return;
        }
        PermissionUtils.requestLocationPermissions(this.requireActivity(), LOCATION_PERMISSION_REQUEST_CODE, true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this.requireContext(), "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this.requireContext(), "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                android.Manifest.permission.ACCESS_FINE_LOCATION) || PermissionUtils
                .isPermissionGranted(permissions, grantResults,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION)) {
            enableMyLocation();
        } else {
            permissionDenied = true;
            showMissingPermissionError();
        }
    }

    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getChildFragmentManager(), "dialog");
    }
}

