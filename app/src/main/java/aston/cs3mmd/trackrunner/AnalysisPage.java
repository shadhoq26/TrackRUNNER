package aston.cs3mmd.trackrunner;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
//this is the class for the analysis page fragment.
public class AnalysisPage extends Fragment implements AnalysisRecyclerViewInterface{
    ArrayList<Run> runs = new ArrayList<>();
    //this runs when the fragment is created.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    //creates the views in the fragment.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_analysis_page, container, false);
    }
    //This function will run after the view is created.
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //loads up the run from files.
        loadRuns();
        //set recycler view.
        RecyclerView recyclerView = getView().findViewById(R.id.analysisRecyclerView);
        AnalysisRecyclerAdapter adapter = new AnalysisRecyclerAdapter(getContext(), runs, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
    //this function loads the run data from files.
    public void loadRuns() {
        FileInputStream fileInputStream = null;
        //try loading the data, if it crashes then it will give an exception.
        try {
            fileInputStream = ((MainActivity)getActivity()).openFileInput("runs.csv");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;

            runs = new ArrayList<>();
            while ((line = bufferedReader.readLine()) != null) {
                String[] data = line.split(",");
                String[] loc = data[4].split("@");
                runs.add(new Run(data[0], data[1], data[2], data[3], loc[0] + "," + loc[1], data[5]));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    //When an item is clicked, the page will be changed to the AnalysisViewPage.
    @Override
    public void onClick(int position) {
        ((MainActivity)getActivity()).changePage(new AnalysisViewPage(runs.get(position), position+1));
    }
    //When the delete button is clicked inside the recycler view the route will be deleted.
    @Override
    public void onDelete(int position) {
        new AlertDialog.Builder(getContext()).setTitle("Delete Run").setMessage("Are you sure?").setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                runs.remove(position);
                saveRun();
                ((MainActivity)getActivity()).changePage(new AnalysisPage());
            }
        }).setNegativeButton(android.R.string.no, null).show();
    }
    //This will save the run to the files.
    public void saveRun() {
        FileOutputStream fileOutputStream = null;
        //It will try to save the data, if it crashes then it will give an exception.
        try {
            String data = "";
            for (Run r : runs) {
                String[] fromLoc = r.getFrom().split(",");
                data += r.getTime() + "," + r.getMiles() + "," + r.getKCAL() + "," + r.getSpeed() + "," + fromLoc[0] + "@" + fromLoc[1] + "," + r.getTo() + "\n";
            }

            fileOutputStream = getActivity().openFileOutput("runs.csv", Context.MODE_PRIVATE);
            fileOutputStream.write(data.getBytes());
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