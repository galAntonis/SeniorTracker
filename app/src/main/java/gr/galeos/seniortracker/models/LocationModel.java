package gr.galeos.seniortracker.models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LocationModel {
    public double latitude;
    public double longitude;
    public String email;
    public long timestamp;
    public String name = null;
    public String firstName = null;
    public String lastName = null;

    public LocationModel() {
        // Default constructor required for Firebase
    }

    public LocationModel(double latitude, double longitude, String email, String name, String firstName, String lastName) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.email = email;
        this.timestamp = System.currentTimeMillis();
        this.name = name;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }

}
