package gr.galeos.seniortracker.ui.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import org.greenrobot.eventbus.EventBus;

import gr.galeos.seniortracker.R;
import gr.galeos.seniortracker.databinding.FragmentLoginBinding;
import gr.galeos.seniortracker.utils.Constants;
import gr.galeos.seniortracker.utils.MessageEvent;
import gr.galeos.seniortracker.utils.SharedPreferencesUtils;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private LoginViewModel viewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentLoginBinding.inflate(inflater, container, false);
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
                new ViewModelProvider(this).get(LoginViewModel.class);
    }

    private void setupObservers() {
        viewModel.getToken().observe(getViewLifecycleOwner(), token -> {
            if (token != null) {
                SharedPreferencesUtils.saveSessionId(token);
                SharedPreferencesUtils.saveEmail(binding.emailEditText.getText().toString());
                viewModel.getAccountType(binding.emailEditText.getText().toString());
            } else {
                binding.errorMessageTextView.setVisibility(View.VISIBLE);
                binding.errorMessageTextView.setText(R.string.login_failed);
            }
        });

        viewModel.getType().observe(getViewLifecycleOwner(), type -> {
            if (type != null) {
                SharedPreferencesUtils.saveAccountType(type);
                Navigation.findNavController(binding.getRoot()).navigate(R.id.action_navigate_from_login_to_home);
                EventBus.getDefault().post(new MessageEvent(Constants.USER_LOGGED_IN));
            } else {
                Navigation.findNavController(binding.getRoot()).navigate(R.id.action_navigate_from_login_to_account);

            }
        });


    }

    private void setClickListeners() {
        binding.loginButton.setOnClickListener(v -> {
            String email = binding.emailEditText.getText().toString();
            String password = binding.passwordEditText.getText().toString();
            if (!email.isEmpty() || !password.isEmpty()) {
                viewModel.login(email, password);
            }else{
                binding.errorMessageTextView.setVisibility(View.VISIBLE);
                binding.errorMessageTextView.setText(R.string.empty_fields);
            }
        });

        binding.signUpTextButton.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_navigate_from_login_to_signup);
        });
    }

}