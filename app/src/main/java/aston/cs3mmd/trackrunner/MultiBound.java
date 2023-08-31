package aston.cs3mmd.trackrunner;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
//this class stores the bounds data.
public class MultiBound implements Serializable {
    @SerializedName("northeast")
    private StoreBound northeast;

    @SerializedName("southwest")
    private StoreBound southwest;

    public StoreBound getNortheast() {
        return northeast;
    }

    public void setNortheast(StoreBound northeast) {
        this.northeast = northeast;
    }

    public StoreBound getSouthwest() {
        return southwest;
    }

    public void setSouthwest(StoreBound southwest) {
        this.southwest = southwest;
    }
}
