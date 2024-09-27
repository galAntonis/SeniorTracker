package gr.galeos.seniortracker.ui.manageSeniors;

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
import gr.galeos.seniortracker.databinding.FragmentManageSeniorsBinding;

public class ManageSeniorsFragment extends Fragment {

    private FragmentManageSeniorsBinding binding;
    private ManageSeniorsViewModel viewModel;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentManageSeniorsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        setupViewModel();
        setClickListeners();
    }

    private void setupViewModel() {
        viewModel =
                new ViewModelProvider(this).get(ManageSeniorsViewModel.class);
    }

    private void setClickListeners() {
        binding.cvAddSeniors.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_navigate_from_manage_seniors_to_add_senior);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}