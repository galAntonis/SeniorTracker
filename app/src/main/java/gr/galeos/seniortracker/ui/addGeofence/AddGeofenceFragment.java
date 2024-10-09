package gr.galeos.seniortracker.ui.addGeofence;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import gr.galeos.seniortracker.R;
import gr.galeos.seniortracker.UserModel;
import gr.galeos.seniortracker.databinding.FragmentAddGeofenceBinding;
import gr.galeos.seniortracker.utils.PermissionUtils;

public class AddGeofenceFragment extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener {

    private FragmentAddGeofenceBinding binding;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private GoogleMap map;
    private boolean permissionDenied = false;
    private Marker centerMarker;  // Marker to track the center

    private String email;

    private AddGeofenceViewModel viewModel; // ViewModel to handle location data

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddGeofenceBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            email = bundle.getString("email");
        }

        setupViewModel();
        setupMap();
        setupButtonClick();  // Setup button click action
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(AddGeofenceViewModel.class);
    }

    private void setupMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    private void setupButtonClick() {
        // When the button is clicked, capture the center marker's position
        binding.markGeofenceButton.setOnClickListener(v -> {
            if (centerMarker != null) {
                LatLng markerPosition = centerMarker.getPosition();
                double lat = markerPosition.latitude;
                double lon = markerPosition.longitude;
                Bundle bundle = new Bundle();
                bundle.putDouble("lat", lat);
                bundle.putDouble("lon", lon);
                bundle.putString("email", email);
                Navigation.findNavController(v).navigate(R.id.action_navigate_from_add_geofence_to_geofence_details, bundle);
                // Do something with lat and lon, e.g., send to ViewModel or display
                // For now, let's just log it
                Log.d("AddGeofenceFragment", "Lat: " + lat + ", Lon: " + lon);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        LatLng lastKnownLatLng = new LatLng(37.9838877, 23.7279186);
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(lastKnownLatLng, 18));

        // Set the center marker at the initial position of the map's center
        centerMarker = map.addMarker(new MarkerOptions().position(map.getCameraPosition().target).draggable(false));

        if (!UserModel.getInstance().user.getAccountType().equals("0")) {
            map.setOnMyLocationButtonClickListener(this);
            map.setOnMyLocationClickListener(this);
            enableMyLocation();
        }

        // Update marker position when the map moves
        map.setOnCameraMoveListener(() -> {
            if (centerMarker != null) {
                // Set the marker at the new center of the map
                centerMarker.setPosition(map.getCameraPosition().target);
            }
        });
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
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        // You can handle location clicks here if needed
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
