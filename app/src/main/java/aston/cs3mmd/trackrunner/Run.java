package aston.cs3mmd.trackrunner;
//This class stores the run data.
public class Run {
    String time;
    String miles;
    String KCAL;
    String speed;
    String from;
    String to;

    //Constructor that sets all the variable data.
    public Run(String time, String miles, String KCAL, String speed, String from, String to) {
        this.time = time;
        this.miles = miles;
        this.KCAL = KCAL;
        this.speed = speed;
        this.from = from;
        this.to = to;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMiles() {
        return miles;
    }

    public void setMiles(String miles) {
        this.miles = miles;
    }

    public String getKCAL() {
        return KCAL;
    }

    public void setKCAL(String KCAL) {
        this.KCAL = KCAL;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
