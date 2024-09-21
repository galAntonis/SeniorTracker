package gr.galeos.seniortracker.ui.signup;

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
import gr.galeos.seniortracker.databinding.FragmentSignUpBinding;
import gr.galeos.seniortracker.utils.SharedPreferencesUtils;

public class SignUpFragment extends Fragment {

    private FragmentSignUpBinding binding;
    private SignUpViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferencesUtils.initSharedPreferences(requireContext());

        if (SharedPreferencesUtils.isSessionIdValid()){
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_navigate_from_signup_to_home);
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSignUpBinding.inflate(inflater, container, false);
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
                new ViewModelProvider(this).get(SignUpViewModel.class);
    }

    private void setupObservers() {

        viewModel.getSignup().observe(getViewLifecycleOwner(), signup -> {
            if (signup) {
                Navigation.findNavController(binding.getRoot()).navigate(R.id.action_navigate_from_signup_to_login);
            } else {
                Toast.makeText(getContext(), "Sign up failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setClickListeners() {

        binding.signButton.setOnClickListener(v -> {
            String email = binding.emailEditText.getText().toString();
            String password = binding.passwordEditText.getText().toString();
            String retypePassword = binding.retypePasswordEditText.getText().toString();

            if (!email.isEmpty() && !password.isEmpty() && !retypePassword.isEmpty()) {
                if (password.equals(retypePassword)) {
                    viewModel.signUp(email, password);
                } else {
                    binding.errorMessageTextView.setVisibility(View.VISIBLE);
                    binding.errorMessageTextView.setText("Passwords do not match");
                }
            } else {
                binding.errorMessageTextView.setVisibility(View.VISIBLE);
                binding.errorMessageTextView.setText("You have some empty fields");
            }
        });

        binding.loginTextButton.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_navigate_from_signup_to_login);
        });
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
        binding = null;
    }
}