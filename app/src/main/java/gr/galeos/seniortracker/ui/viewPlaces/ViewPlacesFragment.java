package gr.galeos.seniortracker.ui.viewPlaces;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import gr.galeos.seniortracker.databinding.FragmentViewPlacesBinding;

public class ViewPlacesFragment extends Fragment {

    private FragmentViewPlacesBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ViewPlacesViewModel viewPlacesViewModel =
                new ViewModelProvider(this).get(ViewPlacesViewModel.class);

        binding = FragmentViewPlacesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.tvViewPlaces;
        viewPlacesViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}