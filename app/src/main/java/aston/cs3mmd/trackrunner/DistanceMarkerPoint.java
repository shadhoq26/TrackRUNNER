package aston.cs3mmd.trackrunner;

import com.google.android.gms.maps.model.LatLng;
//this class stores the data for a distance marker.
public class DistanceMarkerPoint {
    LatLng location;
    int time;

    public DistanceMarkerPoint(LatLng location, int time) {
        this.location = location;
        this.time = time;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
