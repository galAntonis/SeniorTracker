package gr.galeos.seniortracker.ui.chooseSenior;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import gr.galeos.seniortracker.R;
import gr.galeos.seniortracker.databinding.FragmentChooseSeniorBinding;
import gr.galeos.seniortracker.databinding.ItemChooseSeniorBinding;

public class ChooseSeniorFragment extends Fragment {
    private FragmentChooseSeniorBinding binding;
    private ChooseSeniorViewModel viewModel;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentChooseSeniorBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        setupViewModel();
        setupObservers();
        viewModel.getMySeniors();
    }

    private void setupViewModel() {
        viewModel =
                new ViewModelProvider(this).get(ChooseSeniorViewModel.class);
    }

    private void setupObservers() {
        viewModel.getSeniors().observe(getViewLifecycleOwner(), seniors -> {
            if (seniors != null) {
                binding.seniorList.setLayoutManager(new LinearLayoutManager(getContext()));
                binding.seniorList.setAdapter(new ChooseSeniorFragment.SeniorsAdapter(seniors));
                for (int i = 0; i < seniors.size(); i++) {
                    Log.d("ManageSeniorsFragment", "Seniors found: " + seniors.get(i));
                }
            } else {
                Log.d("ManageSeniorsFragment", "Seniors not found");
            }
        });
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemChooseSeniorBinding binding;
        private final TextView name;

        public ViewHolder(ItemChooseSeniorBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.name = binding.tvSeniorName;
        }

        public void bind(String email, View.OnClickListener listener) {
            name.setText(email);
            binding.getRoot().setOnClickListener(listener);
        }
    }

    private static class SeniorsAdapter extends RecyclerView.Adapter<ViewHolder> {
        private List<String> seniors;

        public SeniorsAdapter(List<String> seniors) {
            this.seniors = seniors;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(
                    ItemChooseSeniorBinding.inflate(
                            LayoutInflater.from(parent.getContext()), parent, false)
            );
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.bind(seniors.get(position), v -> {
                Bundle bundle = new Bundle();
                bundle.putString("email", seniors.get(position));
                Log.d("ChooseSeniorFragment", "Email: " + seniors.get(position));
                Navigation.findNavController(v).navigate(R.id.action_navigate_from_choose_senior_to_manage_places, bundle);
            });
        }

        @Override
        public int getItemCount() {
            return seniors.size();
        }
    }


}