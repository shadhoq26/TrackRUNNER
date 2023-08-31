package aston.cs3mmd.trackrunner;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
//This class stores the direction path data.
public class RouteStorage implements Serializable {
    @SerializedName("bounds")
    private MultiBound bounds;

    @SerializedName("overview_polyline")
    private DirectionLineData lineData;

    public MultiBound getBounds() {
        return bounds;
    }

    public void setBounds(MultiBound bounds) {
        this.bounds = bounds;
    }

    public DirectionLineData getLineData() {
        return lineData;
    }

    public void setLineData(DirectionLineData lineData) {
        this.lineData = lineData;
    }
}

