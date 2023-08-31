package aston.cs3mmd.trackrunner;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
//this class is for the settings page fragment.
public class SettingsPage extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings_page, container, false);
    }
    //This for when the view is created.
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //this sets variables for the 2 spinners
        Spinner languagesSpinner = getView().findViewById(R.id.languagesSpinner);
        Spinner measurementSpinner = getView().findViewById(R.id.measurementSpinner);
        //this creates the variables for the lannguage and measurement data.
        ArrayAdapter<CharSequence> languagesAdapter = ArrayAdapter.createFromResource(getContext(), R.array.languages, android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> measurementAdapter = ArrayAdapter.createFromResource(getContext(), R.array.measurement, android.R.layout.simple_spinner_dropdown_item);
        //this sets the data for the language/measurement data options.
        languagesSpinner.setAdapter(languagesAdapter);
        measurementSpinner.setAdapter(measurementAdapter);

        //This will restart all the progress in the app/any data that is saved.
        Button restartProgressButton = getView().findViewById(R.id.restartProgressButton);
        restartProgressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //An alert dialogue is shown that asks the user if they want to delete all their data.
                new AlertDialog.Builder(getContext()).setTitle("Restart Progress").setMessage("Are you sure you want to restart your progress?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            //If the user presses yes, all the data will be deleted.
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                try {
                                    File dir = ((MainActivity) getActivity()).getFilesDir();
                                    File runsFile = new File(dir, "runs.csv");
                                    File profileFile = new File(dir, "profileData.csv");
                                    runsFile.delete();
                                    profileFile.delete();
                                    Toast toast = Toast.makeText(getContext(), "All Progress Deleted", Toast.LENGTH_SHORT);
                                    toast.show();

                                    ((MainActivity)getActivity()).profileData = new ProfileData();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).setNegativeButton(android.R.string.no, null).setIcon(android.R.drawable.ic_dialog_alert).show();
            }
        });
    }
}