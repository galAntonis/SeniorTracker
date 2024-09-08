package gr.galeos.seniortracker.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import gr.galeos.seniortracker.R;
import gr.galeos.seniortracker.databinding.FragmentDashboardBinding;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        loadUI();
        setClickListeners();
    }

    private void loadUI() {

        binding.historyTile.title.setText(getString(R.string.dashboard_history));
        binding.historyTile.image.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_location_history, null));
        binding.viewFencesTile.title.setText(getString(R.string.dashboard_view_places));
        binding.viewFencesTile.image.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_view_fences, null));
        binding.manageFencesTile.title.setText(getString(R.string.dashboard_manage_places));
        binding.manageFencesTile.image.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_manage_fences, null));
        binding.manageSeniorsTile.title.setText(getString(R.string.dashboard_manage_seniors));
        binding.manageSeniorsTile.image.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_manage_seniors, null));
        binding.settingsTile.title.setText(getString(R.string.dashboard_settings));
        binding.settingsTile.image.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_settings, null));
    }

    private void setClickListeners() {

        binding.historyTile.getRoot().setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_navigation_dashboard_to_navigation_history));
        binding.viewFencesTile.getRoot().setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_navigation_dashboard_to_navigation_view_places));
        binding.manageFencesTile.getRoot().setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_navigation_dashboard_to_navigation_manage_places));
        binding.manageSeniorsTile.getRoot().setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_navigation_dashboard_to_navigation_manage_seniors));
        binding.settingsTile.getRoot().setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_navigation_dashboard_to_navigation_settings));
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
        binding = null;
    }
}