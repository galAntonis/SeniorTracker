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
import androidx.navigation.Navigation;

import gr.galeos.seniortracker.R;
import gr.galeos.seniortracker.databinding.FragmentAccountBinding;
import gr.galeos.seniortracker.utils.SharedPreferencesUtils;

public class AccountFragment extends Fragment {

    private FragmentAccountBinding binding;
    private AccountViewModel viewModel;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAccountBinding.inflate(inflater, container, false);
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
                new ViewModelProvider(this).get(AccountViewModel.class);
    }

    private void setupObservers() {
        viewModel.createdUserData().observe(getViewLifecycleOwner(), found -> {
            if (found) {
                Navigation.findNavController(binding.getRoot()).navigate(R.id.action_navigate_from_account_to_home);
            } else {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setClickListeners() {
        binding.signButton.setOnClickListener(v -> {
            String id = SharedPreferencesUtils.retrieveSessionId();
            String firstname = binding.nameEditText.getText().toString();
            String lastname = binding.surnameEditText.getText().toString();
            String email = binding.emailEditText.getText().toString();
            String phone = binding.phoneEditText.getText().toString();
            String accountType;
            if (binding.rbSupervisor.isChecked()) {
                accountType = "0"; //Supervisor
            } else {
                accountType = "1"; //Senior
            }

            viewModel.writeUserData(id, firstname, lastname, email, phone, accountType);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}