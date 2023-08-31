package aston.cs3mmd.trackrunner;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

//This is a class for the profile page editor fragment.
public class ProfilePageEditor extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_page_editor, container, false);
    }
    //this for when the view is created.
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //This is when the cancel button is clicked, it will close the activity.
        Button cancelbutton = getView().findViewById(R.id.profilePage_cancel_button);
        cancelbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ProfileEditActivity)getActivity()).finish();
            }
        });
        //These will set a variable for each text box.
        TextInputLayout nameTextbox = getView().findViewById(R.id.profilePage_editor_name_textBox);
        TextInputLayout genderTextbox = getView().findViewById(R.id.profilePage_editor_gender_textBox);
        TextInputLayout weightTextbox = getView().findViewById(R.id.profilePage_editor_weight_textBox);
        TextInputLayout heightTextbox = getView().findViewById(R.id.profilePage_editor_height_textBox);
        TextInputLayout weightGoalTextbox = getView().findViewById(R.id.profilePage_editor_weight_goal_textBox);
        //Sets variables for profile data that is passed into activity.
        String name = ((ProfileEditActivity)getActivity()).getName();
        String gender = ((ProfileEditActivity)getActivity()).getGender();
        String weight = ((ProfileEditActivity)getActivity()).getWeight();
        String height = ((ProfileEditActivity)getActivity()).getHeight();
        String weightGoal = ((ProfileEditActivity)getActivity()).getWeightGoal();
        //Sets the text inside text box to profile data.
        nameTextbox.getEditText().setText(name);
        genderTextbox.getEditText().setText(gender);
        weightTextbox.getEditText().setText(weight);
        heightTextbox.getEditText().setText(height);
        weightGoalTextbox.getEditText().setText(weightGoal);

        //This will save the data for the profile when clicked and close activity.
        Button saveButton = getView().findViewById(R.id.profilePage_save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.putExtra("profilePage", true);
                startActivity(intent);
                ((ProfileEditActivity)getActivity()).finish();
                Log.i("shad","Data saved");
            }
        });
    }
    //function to save all the profile data.
    private void saveData() {
        //Sets variables for all the text boxes.
        TextInputLayout nameTextbox = getView().findViewById(R.id.profilePage_editor_name_textBox);
        TextInputLayout genderTextbox = getView().findViewById(R.id.profilePage_editor_gender_textBox);
        TextInputLayout weightTextbox = getView().findViewById(R.id.profilePage_editor_weight_textBox);
        TextInputLayout heightTextbox = getView().findViewById(R.id.profilePage_editor_height_textBox);
        TextInputLayout weightGoalTextbox = getView().findViewById(R.id.profilePage_editor_weight_goal_textBox);

        //This will get all the text from text boxes.
        String name = nameTextbox.getEditText().getText().toString();
        String gender = genderTextbox.getEditText().getText().toString();
        String weight = weightTextbox.getEditText().getText().toString();
        String height = heightTextbox.getEditText().getText().toString();
        String weightGoal = weightGoalTextbox.getEditText().getText().toString();
        //This will format the profile data and save it to files.
        String data = name + "," + gender + "," + weight + "," + height + "," + weightGoal;
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = ((ProfileEditActivity)getActivity()).openFileOutput("profileData.csv", MODE_PRIVATE);
            fileOutputStream.write(data.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}