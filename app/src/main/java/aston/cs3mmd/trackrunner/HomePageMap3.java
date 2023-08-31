package aston.cs3mmd.trackrunner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;

//This is a class for the results page/Home page 3.
public class HomePageMap3 extends Fragment {
    int time;
    float distance;
    double KCAL;
    String start;
    String end;

    //This is a constructor that sets the variable data.
    public HomePageMap3(int time, float distance, double KCAL, String start, String end) {
        this.time = time;
        this.distance = distance;
        this.KCAL = KCAL;
        this.start = start;
        this.end = end;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_page_map3, container, false);
    }
    //This is for when the view is created, it will set the text.
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //This section is for the time, format the time and display in the text view.
        TextView timeText = getView().findViewById(R.id.resultsTimeView);
        String seconds = String.valueOf(time % 60);
        String minutes = String.valueOf(time / 60);
        String hours = String.valueOf((time / 60) / 60);

        //This will add a 0 to the beginning of each number if it is less then 9.
        seconds = (seconds.length() <= 1) ? "0" + seconds : seconds;
        minutes = (minutes.length() <= 1) ? "0" + minutes : minutes;
        hours = (hours.length() <= 1) ? "0" + hours : hours;

        //This sets the text view to the time.
        String timeString = hours + ":" + minutes + ":" + seconds;
        timeText.setText("Time: " + timeString);

        //This is for when converting distance to miles and display it in text view.
        Double miles = distance * 0.000621371192;
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        Double milesDistance = Double.parseDouble(decimalFormat.format(miles));
        TextView distanceText = getView().findViewById(R.id.resultsDistanceView);
        distanceText.setText("Distance: " + milesDistance + " miles");


        //This is the calculations for the calories, format and display it in the results.
        int met = 23;
        int bodyWeightInKg = Integer.parseInt(((RunActivity)getActivity()).getUserWeight());
        double KCALburned = met * 3.5 * bodyWeightInKg / 200 * (((double) time)/60);
        Double KCALformatted = Double.parseDouble(decimalFormat.format(KCALburned));

        TextView KCALtext = getView().findViewById(R.id.resultsKcalView);
        KCALtext.setText(String.valueOf("KCAL burned: " + KCALformatted));

        //This will calculate the average speed shown in the results page.
        double speed = miles/((double)time/60/60);
        TextView speedText = getView().findViewById(R.id.resultsSpeedView);
        double formattedSpeed = Double.parseDouble(decimalFormat.format(speed));
        speedText.setText(String.valueOf("Average Speed: " + formattedSpeed + " mph"));

        //This is the button for saving the run data into the files on the analysis page.
        Button saveButton = getView().findViewById(R.id.saveResultsButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveRun(timeString, String.valueOf(milesDistance), String.valueOf(KCALformatted), String.valueOf(formattedSpeed), start, end);
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.putExtra("analysis", true);
                startActivity(intent);
                ((RunActivity)getActivity()).finish();
            }
        });
        //this will restart the run again when clicked.
        Button restartButton = getView().findViewById(R.id.restartResultsButton);
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((RunActivity)getActivity()).changePage(new HomePageMap2(start, end));
            }
        });
    }
    //This is function for the run data being saved.
    public void saveRun(String time, String miles, String KCAL, String speed, String from, String to) {
        FileOutputStream fileOutputStream = null;
        try {
            String[] fromLoc = from.split(",");
            String run = time + "," + miles+ "," + KCAL+ "," + speed+ "," + fromLoc[0] + "@" + fromLoc[1] + "," + to + "\n";
            fileOutputStream = ((RunActivity)getActivity()).openFileOutput("runs.csv", Context.MODE_APPEND);
            fileOutputStream.write(run.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null){
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}