package gr.galeos.seniortracker.ui.account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import gr.galeos.seniortracker.UserModel;
import gr.galeos.seniortracker.databinding.FragmentAccountSettingsBinding;

public class AccountSettingsFragment extends Fragment {

    private FragmentAccountSettingsBinding binding;
    private AccountSettingsViewModel viewModel;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAccountSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        setupViewModel();
        setupObservers();
        setClickListeners();
        if (UserModel.getInstance().user.getId() != null) {
            viewModel.fetchAccountDetails(UserModel.getInstance().user.getId());
        }


    }

    private void setupViewModel() {
        viewModel =
                new ViewModelProvider(this).get(AccountSettingsViewModel.class);
    }

    private void setupObservers() {
        viewModel.getAccountData().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                UserModel.getInstance().user.setId(user.getId());
                UserModel.getInstance().user.setFirstname(user.getFirstname());
                UserModel.getInstance().user.setLastname(user.getLastname());
                UserModel.getInstance().user.setEmail(user.getEmail());
                UserModel.getInstance().user.setPhone(user.getPhone());
                UserModel.getInstance().user.setAccountType(user.getAccountType());
                binding.nameEditText.setText(UserModel.getInstance().user.getFirstname());
                binding.surnameEditText.setText(UserModel.getInstance().user.getLastname());
                binding.emailEditText.setText(UserModel.getInstance().user.getEmail());
                binding.phoneEditText.setText(UserModel.getInstance().user.getPhone());
            } else {
                // Error
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.updateUserData().observe(getViewLifecycleOwner(), success -> {
            if (success) {
                Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setClickListeners() {
        binding.saveButton.setOnClickListener(v -> {
            String id = UserModel.getInstance().user.getId();
            String firstname = binding.nameEditText.getText().toString();
            String lastname = binding.surnameEditText.getText().toString();
            String email = binding.emailEditText.getText().toString();
            String phone = binding.phoneEditText.getText().toString();
            String accountType = UserModel.getInstance().user.getAccountType().toString();

            viewModel.updateUserData(id, firstname, lastname, email, phone, accountType);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}