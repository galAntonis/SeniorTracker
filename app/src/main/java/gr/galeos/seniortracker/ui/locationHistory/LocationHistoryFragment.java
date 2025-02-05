package gr.galeos.seniortracker.ui.locationHistory;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

import gr.galeos.seniortracker.databinding.FragmentLocationHistoryBinding;
import gr.galeos.seniortracker.models.LocationModel;

public class LocationHistoryFragment extends Fragment {


    private FragmentLocationHistoryBinding binding;
    private LocationHistoryViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLocationHistoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViewModel();
    }

    private void setupViewModel() {
        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(LocationHistoryViewModel.class);

        // Observe the location data LiveData using getViewLifecycleOwner()
        viewModel.getLocationsLiveData().observe(getViewLifecycleOwner(), locations -> {
            // Ensure that we update the UI only when the binding is non-null
            if (binding != null) {
                displayLocations(locations);
            }
        });
    }

    private void displayLocations(List<LocationModel> locations) {
        // Check if binding is null to avoid NullPointerException
        if (binding == null) return;

        StringBuilder locationData = new StringBuilder();

        for (LocationModel location : locations) {
            locationData.append("Lat: ")
                    .append(location.latitude)
                    .append(", Lng: ")
                    .append(location.longitude)
                    .append("\n")
                    .append("Email: ")
                    .append(location.email)
                    .append("\n");
        }

        // Set the location data to the TextView
        binding.locationDataTextView.setText(locationData.toString());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;  // Clear the binding when the view is destroyed
    }
}