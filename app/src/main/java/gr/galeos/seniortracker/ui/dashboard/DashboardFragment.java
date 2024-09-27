package gr.galeos.seniortracker.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import gr.galeos.seniortracker.R;
import gr.galeos.seniortracker.UserModel;
import gr.galeos.seniortracker.databinding.FragmentDashboardBinding;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private DashboardViewModel viewmodel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        loadUI();
        setupViewModel();
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

        if (UserModel.getInstance().user.getAccountType().equals("1")){
            binding.historyTile.getRoot().setAlpha(0.5f);
            binding.historyTile.getRoot().setEnabled(false);
            binding.viewFencesTile.getRoot().setAlpha(0.5f);
            binding.viewFencesTile.getRoot().setEnabled(false);
            binding.manageFencesTile.getRoot().setAlpha(0.5f);
            binding.manageFencesTile.getRoot().setEnabled(false);
            binding.manageSeniorsTile.getRoot().setAlpha(0.5f);
            binding.manageSeniorsTile.getRoot().setEnabled(false);
        } else if (UserModel.getInstance().user.getAccountType().equals("0")){
            binding.historyTile.getRoot().setAlpha(1f);
            binding.historyTile.getRoot().setEnabled(true);
            binding.viewFencesTile.getRoot().setAlpha(1f);
            binding.viewFencesTile.getRoot().setEnabled(true);
            binding.manageFencesTile.getRoot().setAlpha(1f);
            binding.manageFencesTile.getRoot().setEnabled(true);
            binding.manageSeniorsTile.getRoot().setAlpha(1f);
            binding.manageSeniorsTile.getRoot().setEnabled(true);
        }

    }

    private void setupViewModel() {

        viewmodel =
                new ViewModelProvider(this).get(DashboardViewModel.class);
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