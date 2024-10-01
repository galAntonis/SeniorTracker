package gr.galeos.seniortracker.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import org.greenrobot.eventbus.EventBus;

import gr.galeos.seniortracker.R;
import gr.galeos.seniortracker.databinding.FragmentSettingsBinding;
import gr.galeos.seniortracker.utils.Constants;
import gr.galeos.seniortracker.utils.MessageEvent;
import gr.galeos.seniortracker.utils.SharedPreferencesUtils;

public class SettingsFragment extends Fragment {
    private FragmentSettingsBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        setClickListeners();
    }

    private void setClickListeners() {

        binding.accountOption.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_navigation_settings_to_account);
        });
        binding.languageOption.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_navigation_settings_to_language);
        });
        binding.exitOption.setOnClickListener(v -> {
            SharedPreferencesUtils.invalidateSessionId();
            EventBus.getDefault().post(new MessageEvent(Constants.USER_LOGOUT));
            Navigation.findNavController(v).navigate(R.id.action_navigation_settings_to_home);
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}