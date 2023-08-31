package aston.cs3mmd.trackrunner;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

//This class is for the input for location/direction.
public class HomePageMap1 extends Fragment {

    String route;
    //this sets the route data.
    public HomePageMap1(String route) {
        this.route = route;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_page_map1, container, false);
    }
    //this runs when the view is created.
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //if the route variable contains text it will set the destination text box to that text.
        if (route != null) {
            TextInputLayout destinationTextBox = getView().findViewById(R.id.destinationInput);
            destinationTextBox.getEditText().setText(route);
        }
        //This will close the activity.
        Button backButton = getView().findViewById(R.id.runCancelButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((RunActivity)getActivity()).finish();
            }
        });
        //This will take you to Home Page 2/ The map page and set destination.
        Button goButton = getView().findViewById(R.id.runGoButton);
        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextInputLayout startLocationTextBox = getView().findViewById(R.id.locationInput);
                TextInputLayout destinationTextBox = getView().findViewById(R.id.destinationInput);
                String start = startLocationTextBox.getEditText().getText().toString();
                String end = destinationTextBox.getEditText().getText().toString();
                //If From(Your Location) or To(Destination) is empty then a message will appear.
                //This message will tell you to enter destination or location to continue.
                if (start.isEmpty()||end.isEmpty()){
                    Toast toast = new Toast(getContext());
                    toast.setText("Enter Location/Destination to continue");
                    toast.show();
                }else{
                    ((RunActivity)getActivity()).changePage(new HomePageMap2(start,end));
                }



            }
        });
        //This will set the location to the current location when pressed.
        FloatingActionButton currentLocationButton = getView().findViewById(R.id.currentLocationButton);
        currentLocationButton.setOnClickListener(new View.OnClickListener() {
            //The text viewed will be "Current Location" when the current location button is clicked.
            @Override
            public void onClick(View view) {
                TextInputLayout yourLocationTextBox = getView().findViewById(R.id.locationInput);
                yourLocationTextBox.getEditText().setText("Current Location");
            }
        });
    }
}