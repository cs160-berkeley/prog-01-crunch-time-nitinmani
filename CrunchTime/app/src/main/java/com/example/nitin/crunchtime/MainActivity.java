package com.example.nitin.crunchtime;

import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    EditText repsOrMinsEdit;
    EditText weightEdit;
    EditText calorieGoalEdit;
    TextView repsOrMins;
    TextView caloriesBurned;
    Spinner exerciseSpinner;
    Button showResultsNow;
    RadioButton calGoalWorkout, calBurned;
    RadioGroup radioGroup;
    ListView workouts;
    List<String> exercisesAndRepsOrMins;


    public String[] exercises = {"Pushup", "Situp", "Squats", "Leg-lift", "Plank", "Jumping Jacks", "Pullup", "Cycling", "Walking", "Jogging", "Swimming", "Stair-Climbing"};
    public int[] timeForExercises = {350, 200, 225, 25, 25, 10, 100, 12, 20, 12, 13, 15};

    //mandatory implementation part 1 + EC
    //linear scale is assumed for weight
    public float getCaloriesBurned(int weight, String exercise, int durationOrReps) {
        if(exercise.equals("None Selected")) {
            return 0;
        }
        float weightScale = (float) weight/150;
        int exerciseReps = 1;
        for(int i = 0; i < exercises.length; i++) {
            if (exercises[i].equals(exercise)) {
                exerciseReps = timeForExercises[i];
            }
        }
        float repRatio = (float) durationOrReps / exerciseReps;
        return repRatio * 100 * weightScale;
    }

    //mandatory implementation part 2
    public HashMap<String, Integer> getEquivalentExercises(int duration, String exercise) {
        if(exercise.equals("None Selected")) {
            return null;
        }
        int originalRepsOrMins = 1;
        for(int i = 0; i < exercises.length; i++) {
            if (exercises[i].equals(exercise)) {
                originalRepsOrMins = timeForExercises[i];
                break;
            }
        }
        float scalingFactor = (float) duration / originalRepsOrMins;
        HashMap<String, Integer> exerciseRepsAndMinsScaled = new HashMap<String, Integer>();
        for (int i = 0; i < exercises.length; i++) {
            if (exercises[i].equals(exercise)) {
                continue;
            } else {
                exerciseRepsAndMinsScaled.put(exercises[i], (int) (scalingFactor * timeForExercises[i]));
            }
        }
        return exerciseRepsAndMinsScaled;
    }

    //Find out if reps or minutes
    public boolean isReps(String exercise) {
        if(exercise.equals("Pushup") || exercise.equals("Situp") || exercise.equals("Squats")|| exercise.equals("Pullup")) {
            return true;
        } else {
            return false;
        }
    }

    //EC part 2
    public HashMap<String, Integer> getExercisesForCalorieGoal(int calorieGoal, int weight) {
        float calorieScale = (float) calorieGoal/100;
        float weightScale = (float) 150/weight;
        HashMap<String, Integer> exercisesToMeetGoal = new HashMap<String, Integer>();
        for(int i = 0; i < exercises.length; i++) {
            exercisesToMeetGoal.put(exercises[i], (int)(timeForExercises[i] * calorieScale * weightScale));
        }
        return exercisesToMeetGoal;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        repsOrMinsEdit = (EditText)findViewById(R.id.editText);
        weightEdit = (EditText)findViewById(R.id.editText2);
        calorieGoalEdit = (EditText)findViewById(R.id.editText3);
        exerciseSpinner = (Spinner)findViewById(R.id.spinner);
        repsOrMins = (TextView)findViewById(R.id.textView3);
        caloriesBurned = (TextView)findViewById(R.id.textView4);
        showResultsNow = (Button)findViewById(R.id.button);
        calGoalWorkout = (RadioButton) findViewById(R.id.radioButton);
        calBurned = (RadioButton) findViewById(R.id.radioButton2);
        radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
        workouts = (ListView)findViewById(R.id.listView);

        exercisesAndRepsOrMins = new ArrayList<>();
        exercisesAndRepsOrMins.add("Pushups: 0 Reps");
        exercisesAndRepsOrMins.add("Situp: 0 Reps");
        exercisesAndRepsOrMins.add("Squats: 0 Reps");
        exercisesAndRepsOrMins.add("Leg-lift: 0 Reps");
        exercisesAndRepsOrMins.add("Plank: 0 Mins");
        exercisesAndRepsOrMins.add("Jumping Jacks: 0 Mins");
        exercisesAndRepsOrMins.add("Pullup: 0 Reps");
        exercisesAndRepsOrMins.add("Cycling: 0 Mins");
        exercisesAndRepsOrMins.add("Walking: 0 Mins");
        exercisesAndRepsOrMins.add("Jogging: 0 Mins");
        exercisesAndRepsOrMins.add("Swimming: 0 Mins");
        exercisesAndRepsOrMins.add("Stair-Climbing: 0 Mins");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                exercisesAndRepsOrMins );

        workouts.setAdapter(arrayAdapter);
        exerciseSpinner.setOnItemSelectedListener((new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedExercise = exerciseSpinner.getSelectedItem().toString();
                if (selectedExercise.equals("None Selected")) {
                    repsOrMins.setText("Reps or Mins");
                } else if (isReps(selectedExercise)) {
                    repsOrMins.setText("Reps");
                } else {
                    repsOrMins.setText("Mins");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                //do nothing
            }
        }));

        showResultsNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrayAdapter.clear();
                int selectedId = radioGroup.getCheckedRadioButtonId();
                HashMap<String, Integer> mappingToUse = null;
                if(selectedId == calBurned.getId()) { //not goal mode
                    int weight = 150;
                    calorieGoalEdit.setText("");
                    if(weightEdit.getText().toString().length() == 0 || weightEdit.getText().toString() == null) {
                        Toast.makeText(getApplicationContext(), "Please input your weight", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        weight = Integer.parseInt(weightEdit.getText().toString());
                    }
                    String selectedExercise = exerciseSpinner.getSelectedItem().toString();
                    if(selectedExercise.length() == 0 || selectedExercise.equals("None Selected") || selectedExercise == null ) {
                        Toast.makeText(getApplicationContext(), "Please select an exercise", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(repsOrMinsEdit.getText().toString().length() == 0 || repsOrMinsEdit.getText().toString() == null) {
                        if(isReps(selectedExercise)) {
                            Toast.makeText(getApplicationContext(), "Please input the number of repetitions", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            Toast.makeText(getApplicationContext(), "Please input the number of minutes", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    int durationOrReps = Integer.parseInt(repsOrMinsEdit.getText().toString());
                    float calorieLossFromWorkout = getCaloriesBurned(weight, selectedExercise, durationOrReps);
                    double roundedCalorieLoss = Math.round(100.0*calorieLossFromWorkout)/100.0;
                    caloriesBurned.setText(Double.toString(roundedCalorieLoss));
                    mappingToUse = getEquivalentExercises(durationOrReps, selectedExercise);
                    String workoutPlusRepsOrMins;
                    //exercisesAndRepsOrMins.removeAll(exercisesAndRepsOrMins);
                    for(String workout : mappingToUse.keySet()) {
                        if(isReps(workout)) {
                            workoutPlusRepsOrMins = workout + ": " + mappingToUse.get(workout) + " Reps";
                        } else {
                            workoutPlusRepsOrMins = workout + ": " + mappingToUse.get(workout) + " Mins";
                        }
                        arrayAdapter.add(workoutPlusRepsOrMins);
                    }

                } else {
                    exerciseSpinner.setSelection(0);
                    repsOrMinsEdit.setText("");
                    int weight = 150;
                    if(weightEdit.getText().toString().length() == 0 || weightEdit.getText().toString() == null) {
                        Toast.makeText(getApplicationContext(), "Please input your weight", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        weight = Integer.parseInt(weightEdit.getText().toString());
                    }
                    if(calorieGoalEdit.getText().toString() == null || calorieGoalEdit.getText().toString().length() == 0) {
                        Toast.makeText(getApplicationContext(), "Please input your calorie goal", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    int workoutCalorieLossGoal = Integer.parseInt(calorieGoalEdit.getText().toString());
                    mappingToUse = getExercisesForCalorieGoal(workoutCalorieLossGoal, weight);
                    String workoutPlusRepsOrMins;
                    //exercisesAndRepsOrMins.removeAll(exercisesAndRepsOrMins);
                    for(String workout : mappingToUse.keySet()) {
                        if(isReps(workout)) {
                            workoutPlusRepsOrMins = workout + ": " + mappingToUse.get(workout) + " Reps";
                        } else {
                            workoutPlusRepsOrMins = workout + ": " + mappingToUse.get(workout) + " Mins";
                        }
                        arrayAdapter.add(workoutPlusRepsOrMins);
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
