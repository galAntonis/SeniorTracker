package gr.galeos.seniortracker.ui.addSenior;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import gr.galeos.seniortracker.R;
import gr.galeos.seniortracker.databinding.FragmentAddSeniorBinding;

public class AddSeniorFragment extends Fragment {
    private FragmentAddSeniorBinding binding;
    private AddSeniorViewModel viewModel;

    private String seniorEmail;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAddSeniorBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        setupViewModel();
        setupObservers();
        setClickListeners();
    }

    private void setupViewModel() {
        viewModel =
                new ViewModelProvider(this).get(AddSeniorViewModel.class);
    }

    private void setupObservers() {
        viewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                Log.d("AddSeniorFragment", "User found: " + user);
                seniorEmail = user.getEmail();
                binding.errorMessageTextView.setVisibility(View.GONE);
                binding.cvResult.setVisibility(View.VISIBLE);
                binding.tvName.setText(user.getFirstname());
                binding.tvSurname.setText(user.getLastname());
            } else {
                Log.d("AddSeniorFragment", "User not found");
                binding.cvResult.setVisibility(View.GONE);
                binding.errorMessageTextView.setVisibility(View.VISIBLE);
                binding.errorMessageTextView.setText(R.string.user_not_found);
            }
        });

        viewModel.getUserAdded().observe(getViewLifecycleOwner(), userAdded -> {
            if (userAdded) {
                Log.d("AddSeniorFragment", "User added");

            } else {
                Log.d("AddSeniorFragment", "User not added");

            }
        });
    }

    private void setClickListeners() {
        binding.searchButton.setOnClickListener(v -> {
            viewModel.findUser(binding.emailEditText.getText().toString());
        });

        binding.ivAdd.setOnClickListener(v -> {
            viewModel.addSenior(seniorEmail);
        });
    }


}