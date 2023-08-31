package aston.cs3mmd.trackrunner;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
//This class stores the route data.
public class RouteResults implements Serializable {
    @SerializedName("routes")
    private List <RouteStorage> results = new ArrayList<>();

    @SerializedName("status")
    private String status;

    public List<RouteStorage> getResults() {
        return results;
    }

    public void setResults(List<RouteStorage> results) {
        this.results = results;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
