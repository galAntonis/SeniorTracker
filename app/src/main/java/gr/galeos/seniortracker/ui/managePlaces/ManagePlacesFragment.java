package gr.galeos.seniortracker.ui.managePlaces;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import gr.galeos.seniortracker.R;
import gr.galeos.seniortracker.databinding.FragmentManagePlacesBinding;
import gr.galeos.seniortracker.databinding.ItemGeofencesBinding;
import gr.galeos.seniortracker.models.GeofenceModel;

public class ManagePlacesFragment extends Fragment {

    private FragmentManagePlacesBinding binding;
    private ManagePlacesViewModel viewModel;
    private static String email;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentManagePlacesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViewModel();
        setupObservers();
        Bundle bundle = getArguments();
        if (bundle != null)
            email = bundle.getString("email");
        viewModel.fetchGeofences(email);
        setClickListeners();
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(ManagePlacesViewModel.class);
    }

    private void setupObservers() {
        viewModel.getGeofencesLiveData().observe(getViewLifecycleOwner(), geofencesAndIds -> {
            if (geofencesAndIds != null) {
                List<GeofenceModel> geofences = geofencesAndIds.first;
                List<String> documentIds = geofencesAndIds.second;

                // Update RecyclerView
                binding.list.setLayoutManager(new LinearLayoutManager(getContext()));
                binding.list.setAdapter(new GeofencesAdapter(geofences, documentIds));
            }
        });
    }

    private void setClickListeners() {
        binding.cvAddGeofence.setOnClickListener( v -> {
            Bundle bundle = new Bundle();
            bundle.putString("email", email);
            Log.d("ManagePlacesFragment", "email: " + email);
            Navigation.findNavController(v).navigate(R.id.action_navigate_from_manage_places_to_add_geofence,bundle);
        });
    }

    private static class GeofencesAdapter extends RecyclerView.Adapter<ViewHolder> {
        private final List<GeofenceModel> geofences;
        private final List<String> documentIds;

        public GeofencesAdapter(List<GeofenceModel> geofences, List<String> documentIds) {
            this.geofences = geofences;
            this.documentIds = documentIds;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(ItemGeofencesBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.bind(geofences.get(position), documentIds.get(position));
        }

        @Override
        public int getItemCount() {
            return geofences.size();
        }
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemGeofencesBinding binding;

        public ViewHolder(ItemGeofencesBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(GeofenceModel geofenceModel, String documentId) {
            binding.tvGeofenceName.setText(geofenceModel.getName());
            binding.ivSettings.setOnClickListener(v -> {
                // Pass the document ID and geofence data to the details fragment
                Bundle bundle = new Bundle();
                bundle.putString("geofenceId", documentId);
                bundle.putString("geofenceName", geofenceModel.getName());
                bundle.putString("email", email);
                bundle.putDouble("latitude", geofenceModel.getLatitude());
                bundle.putDouble("longitude", geofenceModel.getLongitude());
                bundle.putInt("radius", geofenceModel.getRadius());

                // Navigate to Geofence Details Fragment
                Navigation.findNavController(v).navigate(R.id.action_navigate_from_manage_places_to_geofence_details_fragment, bundle);
            });
        }
    }

}
