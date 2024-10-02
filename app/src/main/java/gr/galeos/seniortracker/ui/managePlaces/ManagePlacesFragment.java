package gr.galeos.seniortracker.ui.managePlaces;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import gr.galeos.seniortracker.R;
import gr.galeos.seniortracker.databinding.FragmentManagePlacesBinding;

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
        setupViewModel();
        setClickListeners();
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(ManagePlacesViewModel.class);
    }

    private void setClickListeners() {
        binding.cvAddGeofence.setOnClickListener( v -> {
            Navigation.findNavController(v).navigate(R.id.action_navigate_from_manage_places_to_add_geofence);
        });
    }
}
