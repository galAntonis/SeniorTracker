package gr.galeos.seniortracker.ui.language;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import java.util.Locale;

import gr.galeos.seniortracker.databinding.FragmentLanguageBinding;

public class LanguageFragment extends Fragment {

    private FragmentLanguageBinding binding;
    private String languageCode;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentLanguageBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        checkLanguage();
        setupRadioButtons();
        setClickListeners();
    }

    private void checkLanguage(){
        if (Locale.getDefault().getLanguage().equals("el") ){
            binding.radioEnglish.setChecked(false);
            binding.radioGreek.setChecked(true);
        } else if (Locale.getDefault().getLanguage().equals("en")){
            binding.radioEnglish.setChecked(true);
            binding.radioGreek.setChecked(false);
        }
    }

    private void setupRadioButtons(){
        binding.radioGreek.setOnClickListener(v -> {
            languageCode = "el";
        });
        binding.radioEnglish.setOnClickListener(v -> {
            languageCode = "en";
        });
    }

    private void setClickListeners(){
        binding.saveButton.setOnClickListener(v -> {
            if (languageCode != null){
                setLocal(requireActivity(), languageCode);
            }
        });
    }
    private void setLocal(FragmentActivity activity, String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        activity.getBaseContext().getResources().updateConfiguration(
                config,
                activity.getBaseContext().getResources().getDisplayMetrics()
        );
        activity.recreate();
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}