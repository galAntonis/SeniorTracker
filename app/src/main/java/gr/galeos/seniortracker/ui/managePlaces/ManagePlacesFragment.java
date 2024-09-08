package gr.galeos.seniortracker.ui.managePlaces;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import gr.galeos.seniortracker.databinding.FragmentManagePlacesBinding;

public class ManagePlacesFragment extends Fragment {

    private FragmentManagePlacesBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ManagePlacesViewModel managePlacesViewModel =
                new ViewModelProvider(this).get(ManagePlacesViewModel.class);

        binding = FragmentManagePlacesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.tvManagePlaces;
        managePlacesViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}