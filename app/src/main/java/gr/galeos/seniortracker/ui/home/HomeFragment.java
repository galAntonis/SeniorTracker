package gr.galeos.seniortracker.ui.home;


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
import gr.galeos.seniortracker.databinding.FragmentHomeBinding;
import gr.galeos.seniortracker.utils.SharedPreferencesUtils;


public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomeViewModel viewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        setupViewModel();
        initUi();
        setupObservers();
        setClickListeners();


    }

    private void setupViewModel() {

        viewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
    }

    private void initUi() {
            SharedPreferencesUtils.initSharedPreferences(requireContext());

            if (SharedPreferencesUtils.isSessionIdValid()){
                binding.tvTitle.setVisibility(View.VISIBLE);
                binding.tvSubtitle.setVisibility(View.VISIBLE);
                binding.includeLatestActivity.getRoot().setVisibility(View.VISIBLE);
                binding.includeYourSeniors.getRoot().setVisibility(View.VISIBLE);
                binding.loggedOutLayout.getRoot().setVisibility(View.GONE);
            }else if (!SharedPreferencesUtils.isSessionIdValid()){
                binding.tvTitle.setVisibility(View.INVISIBLE);
                binding.tvSubtitle.setVisibility(View.INVISIBLE);
                binding.includeLatestActivity.getRoot().setVisibility(View.GONE);
                binding.includeYourSeniors.getRoot().setVisibility(View.GONE);
                binding.loggedOutLayout.getRoot().setVisibility(View.VISIBLE);
            }
    }

    private void setupObservers() {

    }

    private void setClickListeners() {
        binding.loggedOutLayout.loginButton.setOnClickListener(v -> {
                Navigation.findNavController(v).navigate(R.id.action_navigate_from_home_to_login);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}