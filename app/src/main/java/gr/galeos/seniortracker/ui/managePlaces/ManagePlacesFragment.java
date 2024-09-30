package gr.galeos.seniortracker.ui.managePlaces;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import gr.galeos.seniortracker.databinding.FragmentManagePlacesBinding;
import gr.galeos.seniortracker.utils.LocationTrackingService;

public class ManagePlacesFragment extends Fragment {

    private FragmentManagePlacesBinding binding;
    private ManagePlacesViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentManagePlacesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ManagePlacesViewModel.class);

        requestLocationPermissions();
        setupClickListeners();
    }

    private void requestLocationPermissions() {
        String[] permissions;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION};
        } else {
            permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
        }
        requestPermissions(permissions, 1);
    }

    private void setupClickListeners() {
        binding.startTrackingButton.setOnClickListener(v -> {
            if (hasLocationPermission()) {
                startTrackingService();
            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        });

        binding.stopTrackingButton.setOnClickListener(v -> stopTrackingService());
    }

    private boolean hasLocationPermission() {
        return ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void startTrackingService() {
        Intent serviceIntent = new Intent(getContext(), LocationTrackingService.class);
        requireContext().startService(serviceIntent);
        Toast.makeText(requireContext(), "Location tracking started", Toast.LENGTH_SHORT).show();
    }

    private void stopTrackingService() {
        Intent serviceIntent = new Intent(getContext(), LocationTrackingService.class);
        requireContext().stopService(serviceIntent);
        Toast.makeText(requireContext(), "Location tracking stopped", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startTrackingService();
        } else {
            Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }
}
