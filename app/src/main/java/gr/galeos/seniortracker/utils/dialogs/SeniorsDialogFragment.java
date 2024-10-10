package gr.galeos.seniortracker.utils.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

import gr.galeos.seniortracker.databinding.FragmentSeniorsDialogListDialogBinding;
import gr.galeos.seniortracker.databinding.FragmentSeniorsDialogListDialogItemBinding;
import gr.galeos.seniortracker.models.User;
import gr.galeos.seniortracker.ui.maps.MapsViewModel;
import gr.galeos.seniortracker.utils.SharedPreferencesUtils;

/**
 * <p>A fragment that shows a list of items as a modal bottom sheet.</p>
 * <p>You can show this modal bottom sheet from your activity like this:</p>
 * <pre>
 *     SeniorsDialogFragment.newInstance(30).show(getSupportFragmentManager(), "dialog");
 * </pre>
 */
public class SeniorsDialogFragment extends BottomSheetDialogFragment {


    private FragmentSeniorsDialogListDialogBinding binding;
    private MapsViewModel viewModel; // ViewModel to handle location data


    // TODO: Customize parameters
    public static SeniorsDialogFragment newInstance() {
        return new SeniorsDialogFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentSeniorsDialogListDialogBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViewModel();
        loadData();
        getSeniors();
        expandModalBottomSheet(view);

    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(MapsViewModel.class);
    }

    private void loadData() {
        viewModel.getSeniors().observe(getViewLifecycleOwner(), seniors -> {
            if (seniors != null) {
                RecyclerView recyclerView = binding.list;
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(new ItemAdapter(seniors));
            } else {
                Toast.makeText(requireContext(), "No seniors found", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getSeniors() {
        viewModel.getMySeniors();
    }

    private void expandModalBottomSheet(View view) {
        ViewGroup.LayoutParams layoutParams = ((View) view.getParent()).getLayoutParams();
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView name;

        public ViewHolder(FragmentSeniorsDialogListDialogItemBinding binding) {
            super(binding.getRoot());
            this.name = binding.text;
        }

        public void bind(User senior, View.OnClickListener listener) {
            name.setText(senior.getFirstname() + " " + senior.getLastname());
            itemView.setOnClickListener(listener);
        }

    }

    private class ItemAdapter extends RecyclerView.Adapter<ViewHolder> {

        private ArrayList<User> seniors;

        public ItemAdapter(ArrayList<User> seniors) {
            this.seniors = seniors;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            return new ViewHolder(FragmentSeniorsDialogListDialogItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            User senior = seniors.get(position);
            holder.bind(senior, v -> {
                SharedPreferencesUtils.saveSelectedSenior(senior.getId());
                getDialog().dismiss();

                // Use the view with a NavController
                View navView = requireParentFragment().getView(); // Ensure this has a NavController
                if (navView != null) {
                    int id = Navigation.findNavController(navView).getCurrentDestination().getId();
                    Navigation.findNavController(navView).popBackStack(id, true);
                    Navigation.findNavController(navView).navigate(id);
                } else {
                    Toast.makeText(requireContext(), "Navigation view is missing", Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return seniors.size();
        }

    }
}