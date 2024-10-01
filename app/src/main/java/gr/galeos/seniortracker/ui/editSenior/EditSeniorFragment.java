package gr.galeos.seniortracker.ui.editSenior;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import gr.galeos.seniortracker.SeniorModel;
import gr.galeos.seniortracker.databinding.FragmentEditSeniorBinding;

public class EditSeniorFragment extends Fragment {

    private FragmentEditSeniorBinding binding;
    private EditSeniorViewModel viewModel;
    private String seniorId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            seniorId = bundle.getString("seniorId");
        }
    }


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentEditSeniorBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        setupViewModel();
        setupObservers();
        setClickListeners();
        if (seniorId != null) {
            viewModel.fetchSeniorDetails(seniorId);
        }

    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(EditSeniorViewModel.class);
    }

    private void setupObservers() {
        viewModel.getSeniorData().observe(getViewLifecycleOwner(), senior -> {
            if (senior != null) {
                SeniorModel.getInstance().user.setId(senior.getId());
                SeniorModel.getInstance().user.setFirstname(senior.getFirstname());
                SeniorModel.getInstance().user.setLastname(senior.getLastname());
                SeniorModel.getInstance().user.setEmail(senior.getEmail());
                SeniorModel.getInstance().user.setPhone(senior.getPhone());
                SeniorModel.getInstance().user.setAccountType(senior.getAccountType());
                binding.nameEditText.setText(SeniorModel.getInstance().user.getFirstname());
                binding.surnameEditText.setText(SeniorModel.getInstance().user.getLastname());
                binding.emailEditText.setText(SeniorModel.getInstance().user.getEmail());
                binding.phoneEditText.setText(SeniorModel.getInstance().user.getPhone());
            } else {
                // Error
                Log.d("EditSeniorFragment", "Senior not found");
            }
        });

        viewModel.updateUserData().observe(getViewLifecycleOwner(), found -> {
            if (found) {
                Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setClickListeners() {
        binding.saveButton.setOnClickListener(v -> {
            String id = SeniorModel.getInstance().user.getId();
            String firstname = binding.nameEditText.getText().toString();
            String lastname = binding.surnameEditText.getText().toString();
            String email = binding.emailEditText.getText().toString();
            String phone = binding.phoneEditText.getText().toString();
            viewModel.updateUserData(id, firstname, lastname, email, phone,SeniorModel.getInstance().user.getAccountType());

        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}