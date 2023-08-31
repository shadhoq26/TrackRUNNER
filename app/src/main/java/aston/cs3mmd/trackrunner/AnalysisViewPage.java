package aston.cs3mmd.trackrunner;

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
//this is for the analysis view page fragment
public class AnalysisViewPage extends Fragment {
    Run run;
    int number;

    //this sets the run information for the analysis view page.
    public AnalysisViewPage(Run run, int number) {
        this.run = run;
        this.number = number;
    }

    //this runs when the fragment is created.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //this creates the view for the fragment.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_analysis_view_page, container, false);
    }
    //This runs when the view is created
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //This sets a variable for each text view.
        TextView timeText = getView().findViewById(R.id.analysisTimeText);
        TextView distanceText = getView().findViewById(R.id.analysisDistanceText);
        TextView KCALText = getView().findViewById(R.id.analysisKCALText);
        TextView speedText = getView().findViewById(R.id.analysisSpeedText);
        TextView title = getView().findViewById(R.id.routeText);
        //this sets the data for each text view.
        timeText.setText("Time: "+ run.getTime());
        distanceText.setText("Distance: "+ run.getMiles() +"miles");
        KCALText.setText("KCAL Burned: " + run.getKCAL());
        speedText.setText("Average Speed: "+ run.getSpeed() +" mph");
        title.setText("Route " + number);
        //this is for when the back button is pressed on the AnalysisViewPage.
        Button backButton = getView().findViewById(R.id.analysisBackButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).changePage(new AnalysisPage());
            }
        });
        //This is for when the "run route again" button is clicked.
        Button runRouteAgainButton = getView().findViewById(R.id.runRouteAgainButton);
        runRouteAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), RunActivity.class);
                intent.putExtra("weight", ((MainActivity)getActivity()).profileData.getWeight());
                intent.putExtra("route", run.getTo());
                startActivity(intent);
            }
        });
    }
}