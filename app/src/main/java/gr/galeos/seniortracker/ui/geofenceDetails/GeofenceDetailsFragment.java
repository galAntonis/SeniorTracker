package gr.galeos.seniortracker.ui.geofenceDetails;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import gr.galeos.seniortracker.databinding.FragmentGeofenceDetailsBinding;
import gr.galeos.seniortracker.models.GeofenceModel;
import gr.galeos.seniortracker.utils.GeofenceBroadcastReceiver;

public class GeofenceDetailsFragment extends Fragment {

    private FragmentGeofenceDetailsBinding binding;

    private static final String TAG = "GeofenceDetailsFragment";
    private static final long GEOFENCE_EXPIRATION_IN_MILLISECONDS = 5000;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private GeofencingClient geofencingClient;
    private PendingIntent geofencePendingIntent;
    private GeofenceDetailsViewModel viewModel;
    private double latitude;
    private double longitude;
    private String email;
    private int radius;
    private String geofenceId;
    private String geofenceName;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request permission
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentGeofenceDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            if (args.containsKey("geofenceId")) {
                geofenceId = args.getString("geofenceId");
                geofenceName = args.getString("geofenceName");
                latitude = args.getDouble("latitude");
                longitude = args.getDouble("longitude");
                email = args.getString("email");
                radius = args.getInt("radius");
                initPrefilledFields();
            } else {
                latitude = args.getDouble("lat");
                longitude = args.getDouble("lon");
                email = args.getString("email");
                initUI();
            }

        }

        setupViewModel();
        setupObservers();
        setClickListeners();
        setupSlider();
        checkEmptyFields();

    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(GeofenceDetailsViewModel.class);
    }

    private void initPrefilledFields() {
        binding.tvLat.setText(String.valueOf(latitude));
        binding.tvLon.setText(String.valueOf(longitude));
        binding.nameEditText.setText(geofenceName);
        binding.emailEditText.setText(email);
        binding.emailEditText.setEnabled(false);
        binding.nameEditText.setText(geofenceName);
        binding.depositSlider.setValue(radius);
        binding.depositSlider.setEnabled(false);
        binding.tvRadius.setText(String.format("%sm", radius));
        binding.saveButton.setEnabled(true);
    }

    private void initUI() {
        radius = (int) binding.depositSlider.getValue();
        binding.tvLat.setText(String.valueOf(latitude));
        binding.tvLon.setText(String.valueOf(longitude));
        binding.emailEditText.setText(email);
        binding.emailEditText.setEnabled(false);
        binding.saveButton.setEnabled(false);
    }

    private void setupObservers() {
        viewModel.findMySenior().observe(getViewLifecycleOwner(), found -> {
            if (found) {
                geofencingClient = LocationServices.getGeofencingClient(requireActivity());
                checkAndRequestLocationPermissions(radius);
                binding.errorMessageTextView.setVisibility(View.GONE);
                Toast.makeText(requireContext(), "Senior found!", Toast.LENGTH_SHORT).show();
            } else {
                binding.errorMessageTextView.setText("Senior not found!");
                binding.errorMessageTextView.setVisibility(View.VISIBLE);
            }
        });
        viewModel.storeGeofence().observe(getViewLifecycleOwner(), stored -> {
            if (stored) {
                //TODO: Go to success fragment
                Toast.makeText(requireContext(), "Geofence stored!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "Failed to store geofence!", Toast.LENGTH_SHORT).show();
            }
        });
        viewModel.updateGeofence().observe(getViewLifecycleOwner(), updated -> {
            if (updated) {
                Toast.makeText(requireContext(), "Geofence updated!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "Failed to update geofence!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setClickListeners() {
        // Set up click listeners
        binding.saveButton.setOnClickListener(v -> {
            if (geofenceId != null) {
                GeofenceModel geofenceModel = new GeofenceModel(binding.nameEditText.getText().toString(), latitude, longitude, (int) radius);
                viewModel.updateGeofence(email, geofenceId, geofenceModel);
            } else{
                viewModel.findSenior(email);
            }
        });
    }

    private void setupSlider() {
        binding.depositSlider.addOnChangeListener((slider, value, fromUser) -> {
            radius = (int) value;
            binding.tvRadius.setText(String.format("%sm", radius));
        });
    }

    private void checkEmptyFields() {
        binding.nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Do nothing
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    binding.saveButton.setEnabled(false);
                } else {
                    binding.saveButton.setEnabled(true);
                }
            }
        });
    }

    private static final int BACKGROUND_LOCATION_REQUEST_CODE = 2;

    private void checkAndRequestLocationPermissions(int radius) {
        // Check if we already have foreground location permission
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Request foreground location permission
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // We have foreground permission, now check for background location permission (Android 10+)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Request background location permission
                    requestPermissions(new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, BACKGROUND_LOCATION_REQUEST_CODE);
                } else {
                    // Permissions are granted, proceed with geofence setup
                    createGeofence(latitude, longitude, radius);
                }
            } else {
                // On Android 9 and below, background permission isn't needed, proceed with geofence setup
                createGeofence(latitude, longitude, radius);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Foreground location permission granted, now check for background permission if needed
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                        // Request background location permission
                        requestPermissions(new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, BACKGROUND_LOCATION_REQUEST_CODE);
                    } else {
                        // Background permission already granted, proceed with geofence setup
                        createGeofence(latitude, longitude, radius);
                    }
                } else {
                    // Android 9 or below, no need for background permission, proceed with geofence setup
                    createGeofence(latitude, longitude, radius);
                }
            } else {
                // Foreground location permission denied, handle it (show a message or disable feature)
                Toast.makeText(requireContext(), "Location permissions not granted", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == BACKGROUND_LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Background location permission granted, proceed with geofence setup
                createGeofence(latitude, longitude, radius);
            } else {
                // Background location permission denied, handle it (show a message or disable feature)
                Toast.makeText(requireContext(), "Background location permissions not granted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void createGeofence(double lat, double lon, float radius) {
        Geofence geofence = new Geofence.Builder()
                .setRequestId("Geofence_ID")  // Give the geofence an ID (you can use UUID or any identifier)
                .setCircularRegion(lat, lon, radius)  // Set the location and radius
                .setExpirationDuration(GEOFENCE_EXPIRATION_IN_MILLISECONDS)  // Geofence never expires
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)  // Trigger for enter and exit
                .build();

        GeofencingRequest geofencingRequest = new GeofencingRequest.Builder()
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)  // Trigger an enter event as soon as geofence is added
                .addGeofence(geofence)
                .build();

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // If permissions are not granted, handle accordingly
            Toast.makeText(requireContext(), "Location permissions not granted", Toast.LENGTH_SHORT).show();
            return;
        }

        geofencingClient.addGeofences(geofencingRequest, getGeofencePendingIntent())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        long expirationDuration = System.currentTimeMillis() + 5000; // Example expiration
                        GeofenceModel geofenceModel = new GeofenceModel(binding.nameEditText.getText().toString(), latitude, longitude, (int) radius);
                        viewModel.writeGeofenceToFirestore(binding.emailEditText.getText().toString(), binding.nameEditText.getText().toString(), geofenceModel);

                        Toast.makeText(requireContext(), "Geofence added successfully!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (e instanceof ApiException) {
                            ApiException apiException = (ApiException) e;
                            int statusCode = apiException.getStatusCode();
                            switch (statusCode) {
                                case GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE:
                                    Toast.makeText(requireContext(), "Geofence service is not available now. Try again later.", Toast.LENGTH_SHORT).show();
                                    break;
                                case GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES:
                                    Toast.makeText(requireContext(), "Your app has registered too many geofences.", Toast.LENGTH_SHORT).show();
                                    break;
                                case GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS:
                                    Toast.makeText(requireContext(), "You have provided too many PendingIntents to the addGeofences() call.", Toast.LENGTH_SHORT).show();
                                    break;
                                case GeofenceStatusCodes.GEOFENCE_INSUFFICIENT_LOCATION_PERMISSION:
                                    Toast.makeText(requireContext(), "Location permission is not granted.", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(requireContext(), "Unknown geofence error: " + statusCode, Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }
                    }
                });

    }

    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it
        if (geofencePendingIntent != null) {
            return geofencePendingIntent;
        }

        // Intent to launch when geofence transition happens
        Intent intent = new Intent(requireContext(), GeofenceBroadcastReceiver.class);

        // Use FLAG_IMMUTABLE as you're not modifying the PendingIntent after it's created
        geofencePendingIntent = PendingIntent.getBroadcast(
                requireContext(),
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        return geofencePendingIntent;
    }

}