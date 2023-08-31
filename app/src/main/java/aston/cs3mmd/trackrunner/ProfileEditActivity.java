package aston.cs3mmd.trackrunner;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.google.android.material.textfield.TextInputLayout;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
//this class is for the profile edit activity
public class ProfileEditActivity extends AppCompatActivity {

    String name = "";
    String gender = "";
    String weight = "";
    String height = "";
    String weightGoal = "";

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getWeight() {
        return weight;
    }

    public String getHeight() {
        return height;
    }

    public String getWeightGoal() {
        return weightGoal;
    }

    //This runs when the activity is created.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        //This gets the extra information which is passed into the activity.
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("name");
            gender = extras.getString("gender");
            weight = extras.getString("weight");
            height = extras.getString("height");
            weightGoal = extras.getString("weightGoal");
        }
        //this changes the current fragment to the Profile page editor.
        fragmentManager.beginTransaction().replace(R.id.frameLayout3,new ProfilePageEditor()).commit();

    }



}