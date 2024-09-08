package gr.galeos.seniortracker.ui.locationHistory;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import gr.galeos.seniortracker.databinding.FragmentLocationHistoryBinding;

public class LocationHistoryFragment extends Fragment {

    private FragmentLocationHistoryBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        LocationHistoryViewModel locationHistoryViewModel =
                new ViewModelProvider(this).get(LocationHistoryViewModel.class);

        binding = FragmentLocationHistoryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.tvHistory;
        locationHistoryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}