package gr.galeos.seniortracker.ui.home;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import gr.galeos.seniortracker.R;
import gr.galeos.seniortracker.UserModel;
import gr.galeos.seniortracker.databinding.FragmentHomeBinding;
import gr.galeos.seniortracker.databinding.ItemYourSeniorsBinding;
import gr.galeos.seniortracker.models.User;
import gr.galeos.seniortracker.utils.Constants;
import gr.galeos.seniortracker.utils.LocationTrackingService;
import gr.galeos.seniortracker.utils.MessageEvent;
import gr.galeos.seniortracker.utils.SharedPreferencesUtils;


public class HomeFragment extends Fragment {


    private FragmentHomeBinding binding;
    private HomeViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        SharedPreferencesUtils.initSharedPreferences(requireContext());

        setupViewModel();
        initUi();
        setupObservers();
        setClickListeners();

    }

    private void setupViewModel() {

        viewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
    }

    private void initUi() {
        if (SharedPreferencesUtils.isSessionIdValid()) {
            viewModel.getMe(SharedPreferencesUtils.retrieveSessionId());
        } else {
            initLogoutUI();
        }

    }

    private void initSupervisorUI() {
        EventBus.getDefault().post(new MessageEvent(Constants.USER_LOGGED_IN));
        binding.tvTitle.setVisibility(View.VISIBLE);
        binding.tvSubtitle.setVisibility(View.VISIBLE);
        binding.includeLatestActivity.getRoot().setVisibility(View.VISIBLE);
        binding.includeYourSeniors.getRoot().setVisibility(View.VISIBLE);
        binding.loggedOutLayout.getRoot().setVisibility(View.GONE);
        binding.seniorsLayout.getRoot().setVisibility(View.GONE);
        viewModel.getMySeniors();
    }

    private void initSeniorUI() {
        EventBus.getDefault().post(new MessageEvent(Constants.USER_LOGGED_IN));
        binding.tvTitle.setVisibility(View.VISIBLE);
        binding.tvSubtitle.setVisibility(View.VISIBLE);
        binding.includeLatestActivity.getRoot().setVisibility(View.GONE);
        binding.includeYourSeniors.getRoot().setVisibility(View.GONE);
        binding.loggedOutLayout.getRoot().setVisibility(View.GONE);
        binding.seniorsLayout.getRoot().setVisibility(View.VISIBLE);
        viewModel.getMyGeofences();
    }

    private void initLogoutUI() {
        EventBus.getDefault().post(new MessageEvent(Constants.USER_LOGOUT));
        binding.tvTitle.setVisibility(View.INVISIBLE);
        binding.tvSubtitle.setVisibility(View.INVISIBLE);
        binding.includeLatestActivity.getRoot().setVisibility(View.GONE);
        binding.includeYourSeniors.getRoot().setVisibility(View.GONE);
        binding.loggedOutLayout.getRoot().setVisibility(View.VISIBLE);
        binding.seniorsLayout.getRoot().setVisibility(View.GONE);
    }

    private void setupObservers() {
        viewModel.findAccount().observe(getViewLifecycleOwner(), found -> {
            if (found) {
                if (UserModel.getInstance().user.getAccountType().equals("0")) {
                    initSupervisorUI();
                } else if (UserModel.getInstance().user.getAccountType().equals("1")) {
                    initSeniorUI();
                }
            } else {
                initLogoutUI();
            }
        });

        viewModel.getSeniors().observe(getViewLifecycleOwner(), seniors -> {
            if (seniors != null) {
                binding.includeYourSeniors.list.setLayoutManager(new LinearLayoutManager(getContext()));
                binding.includeYourSeniors.list.setAdapter(new HomeFragment.SeniorsAdapter(seniors));
                for (int i = 0; i < seniors.size(); i++) {
                    Log.d("ManageSeniorsFragment", "Seniors found: " + seniors.get(i).getEmail());
                }
            } else {
                Log.d("ManageSeniorsFragment", "Seniors not found");
            }
        });

        viewModel.getGeofences().observe(getViewLifecycleOwner(), geofences -> {
            if (geofences != null) {
                for (int i = 0; i < geofences.size(); i++) {
                    Log.d("ManageGeofencesFragment", "Geofences found: " + geofences.get(i).getName());
                }
            } else {
                Log.d("ManageGeofencesFragment", "Geofences not found");
            }
        });
    }

    private void setClickListeners() {
        binding.seniorsLayout.broadcastButton.setOnClickListener(v -> {
            if (hasLocationPermission()) {
                startTrackingService();
            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        });

        binding.seniorsLayout.stopBroadcastButton.setOnClickListener(v -> {
            stopTrackingService();
        });

        binding.loggedOutLayout.loginButton.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_navigate_from_home_to_login);
        });
    }

    private boolean hasLocationPermission() {
        return ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void startTrackingService() {
        Intent serviceIntent = new Intent(getContext(), LocationTrackingService.class);
        requireContext().startService(serviceIntent);
        Toast.makeText(requireContext(), "Location tracking started", Toast.LENGTH_SHORT).show();
    }

    private void stopTrackingService() {
        Intent serviceIntent = new Intent(getContext(), LocationTrackingService.class);
        requireContext().stopService(serviceIntent);
        Toast.makeText(requireContext(), "Location tracking stopped", Toast.LENGTH_SHORT).show();
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView name;
        private final TextView surname;
        private final ImageView ivSettings;

        public ViewHolder(ItemYourSeniorsBinding binding) {
            super(binding.getRoot());
            this.name = binding.tvName;
            this.surname = binding.tvSurname;
            this.ivSettings = binding.ivSettings;
        }

        public void bind(User senior, View.OnClickListener listener) {
            name.setText(senior.getFirstname());
            surname.setText(senior.getLastname());
            ivSettings.setOnClickListener(listener);
        }
    }

    private static class SeniorsAdapter extends RecyclerView.Adapter<HomeFragment.ViewHolder> {
        private ArrayList<User> seniors;

        public SeniorsAdapter(ArrayList<User> seniors) {
            this.seniors = seniors;
        }

        @Override
        public HomeFragment.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new HomeFragment.ViewHolder(
                    ItemYourSeniorsBinding.inflate(
                            LayoutInflater.from(parent.getContext()), parent, false
                    ));
        }

        @Override
        public void onBindViewHolder(HomeFragment.ViewHolder holder, int position) {
            User senior = seniors.get(position);
            holder.bind(senior, v -> {
                Bundle bundle = new Bundle();
                bundle.putString("seniorId", senior.getId());
                Navigation.findNavController(v).navigate(R.id.action_nagigate_from_home_to_edit_senior, bundle);
            });
        }

        @Override
        public int getItemCount() {
            return seniors.size();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}