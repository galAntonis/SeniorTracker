package gr.galeos.seniortracker.ui.manageSeniors;

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

import java.util.ArrayList;

import gr.galeos.seniortracker.R;
import gr.galeos.seniortracker.databinding.FragmentManageSeniorsBinding;
import gr.galeos.seniortracker.databinding.ItemYourSeniorsBinding;
import gr.galeos.seniortracker.models.User;

public class ManageSeniorsFragment extends Fragment {

    private FragmentManageSeniorsBinding binding;
    private ManageSeniorsViewModel viewModel;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentManageSeniorsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        setupViewModel();
        setupAdapter();
        setupObservers();
        setClickListeners();
        viewModel.getMySeniors();
    }

    private void setupViewModel() {
        viewModel =
                new ViewModelProvider(this).get(ManageSeniorsViewModel.class);
    }

    private void setupAdapter() {

    }

    private void setupObservers(){
        viewModel.getSeniors().observe(getViewLifecycleOwner(), seniors -> {
            if (seniors != null) {
                binding.list.setLayoutManager(new LinearLayoutManager(getContext()));
                binding.list.setAdapter(new SeniorsAdapter(seniors));
                for (int i=0;i<seniors.size();i++){

                    Log.d("ManageSeniorsFragment", "Seniors found: " + seniors.get(i).getEmail());
                }
            } else {
               Log.d("ManageSeniorsFragment", "Seniors not found");
            }
        });
    }

    private void setClickListeners() {
        binding.cvAddSeniors.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_navigate_from_manage_seniors_to_add_senior);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView name;
        private final TextView surname;

        public ViewHolder(ItemYourSeniorsBinding binding) {
            super(binding.getRoot());
            this.name = binding.tvName;
            this.surname = binding.tvSurname;

            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        public interface OnClickListener {
            void onClick();
        }
    }



    private static class SeniorsAdapter extends RecyclerView.Adapter<ViewHolder> {
        private ArrayList<User> seniors;

        public SeniorsAdapter(ArrayList<User> seniors) {
            this.seniors = seniors;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(
                    ItemYourSeniorsBinding.inflate(
                            LayoutInflater.from(parent.getContext()), parent, false
                    ));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.name.setText(seniors.get(position).getFirstname());
            holder.surname.setText(seniors.get(position).getLastname());
        }

        @Override
        public int getItemCount() {
            return seniors.size();
        }

        public interface OnClickListener {
            void onClick();
        }
    }




}