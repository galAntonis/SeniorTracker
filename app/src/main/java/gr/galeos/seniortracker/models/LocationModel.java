package gr.galeos.seniortracker.models;

public class LocationModel {
    public double latitude;
    public double longitude;
    public String email;
    public long timestamp;

    public LocationModel() {
        // Default constructor required for Firebase
    }

    public LocationModel(double latitude, double longitude, String email) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.email = email;
        this.timestamp = System.currentTimeMillis();
    }
}
