package gr.galeos.seniortracker.models;

import java.util.HashMap;
import java.util.Map;

public class GeofenceModel {

    private String name;
    private double latitude;
    private double longitude;
    private int radius;

    // Constructor
    public GeofenceModel(String name, double latitude, double longitude, int radius) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
    }

    // Method to convert GeofenceModel to a Map for Firestore storage
    public Map<String, Object> toMap() {
        Map<String, Object> geofenceData = new HashMap<>();
        geofenceData.put("name", name);
        geofenceData.put("latitude", latitude);
        geofenceData.put("longitude", longitude);
        geofenceData.put("radius", radius);
        return geofenceData;
    }

    // Getters and Setters
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
