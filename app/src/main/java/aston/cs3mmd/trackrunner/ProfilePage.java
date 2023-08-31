package aston.cs3mmd.trackrunner;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

//this class is for the profile page fragment.
public class ProfilePage extends Fragment {

    ProfileData profileData = new ProfileData();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_page, container, false);
    }
    //this is for when the view is created.
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //When the button is pressed it will open the profile edit page, send in the current profile data.
        Button editbutton = getView().findViewById(R.id.profilepage_EditButton);
        editbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (getContext(),ProfileEditActivity.class);
                intent.putExtra("name", profileData.getName());
                intent.putExtra("gender", profileData.getGender());
                intent.putExtra("weight", profileData.getWeight());
                intent.putExtra("height", profileData.getHeight());
                intent.putExtra("weightGoal", profileData.getWeightGoal());
                startActivity(intent);
            }
        });
        //this loads in the profile data.
        ((MainActivity)getActivity()).loadProfileData();
        profileData = ((MainActivity)getActivity()).getProfileData();

        //These variables are for the different text views on the profile page.
        TextView nameText = getView().findViewById(R.id.profilepage_profilename);
        TextView genderText = getView().findViewById(R.id.profilepage_gender);
        TextView weightText = getView().findViewById(R.id.profilepage_weight);
        TextView heightText = getView().findViewById(R.id.profilepage_height);
        TextView weightGoalText = getView().findViewById(R.id.profilepage_weightgoal);

        //sets the text for each text view.
        nameText.setText(profileData.getName());
        genderText.setText("Gender: " + profileData.getGender());
        weightText.setText("Weight: " + profileData.getWeight() + " kg");
        heightText.setText("Height: " + profileData.getHeight() + " cm");
        weightGoalText.setText("Weight Goal: " + profileData.getWeightGoal() + " kg");
    }



}