package gr.galeos.seniortracker.ui.manageSeniors;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import gr.galeos.seniortracker.databinding.FragmentManageSeniorsBinding;

public class ManageSeniorsFragment extends Fragment {

    private FragmentManageSeniorsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        ManageSeniorsViewModel manageSeniorsViewModel =
                new ViewModelProvider(this).get(ManageSeniorsViewModel.class);

        binding = FragmentManageSeniorsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.tvManageSeniors;
        manageSeniorsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}