package aston.cs3mmd.trackrunner;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
//This stores the weather information/icon data.
public class WeatherInformation implements Serializable {

    @SerializedName("icon")
    String icon;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
