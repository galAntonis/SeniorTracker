package gr.galeos.seniortracker.ui.locationHistory;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import gr.galeos.seniortracker.databinding.FragmentLocationHistoryBinding;
import gr.galeos.seniortracker.databinding.ItemLocationHistoryBinding;
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;  // Clear the binding when the view is destroyed
    }

    private void setupViewModel() {
        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(LocationHistoryViewModel.class);
        // Initialize Adapter
        LocationHistoryAdapter adapter = new LocationHistoryAdapter();
        binding.locationList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.locationList.setAdapter(adapter);
        // Observe the location data LiveData using getViewLifecycleOwner()
        viewModel.getLocationsLiveData().observe(getViewLifecycleOwner(), locations -> {
            // Ensure that we update the UI only when the binding is non-null
            if (binding != null) {
                adapter.updateList(locations);
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
                    .append("\n")
                    .append("Name: ")
                    .append(location.name)
                    .append("\n");
        }

    }

    private static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView name;
        private final TextView place;
        private final TextView date;

        public ViewHolder(ItemLocationHistoryBinding binding) {
            super(binding.getRoot());
            this.name = binding.tvName;
            this.place = binding.tvPlace;
            this.date = binding.tvDate;
        }

        public void bind(String name, String place, String date) {
            this.name.setText(name);
            this.place.setText(place);
            this.date.setText(date);
        }
    }

    private static class LocationHistoryAdapter extends RecyclerView.Adapter<LocationHistoryFragment.ViewHolder> {
        private List<LocationModel> locations = new ArrayList<>(); // Initialize with empty list

        public void updateList(List<LocationModel> newLocations) {
            this.locations.clear();
            this.locations.addAll(newLocations);
            notifyDataSetChanged(); // Notify adapter about the changes
        }

        @NonNull
        @Override
        public LocationHistoryFragment.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new LocationHistoryFragment.ViewHolder(
                    ItemLocationHistoryBinding.inflate(
                            LayoutInflater.from(parent.getContext()), parent, false)
            );
        }

        @Override
        public void onBindViewHolder(LocationHistoryFragment.ViewHolder holder, int position) {
            LocationModel location = locations.get(position);
            String username = location.firstName + " " + location.lastName;
            holder.bind(username, location.name, location.getDate()); // Assuming date is missing
        }

        @Override
        public int getItemCount() {
            return locations.size();
        }
    }

}