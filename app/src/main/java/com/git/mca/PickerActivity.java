package com.git.mca;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

public class PickerActivity extends AppCompatActivity {
    private DatePicker datePicker;
    private TimePicker timePicker;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private String preferencesName = "SETTINGS";
    public static String CHOSEN_DATE = "chosenDate";
    public static String CHOSEN_TIME = "chosenTime";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picker);

        preferences = this.getSharedPreferences(preferencesName, MODE_PRIVATE);
        editor = preferences.edit();

        datePicker = findViewById(R.id.datepicker);
        timePicker = findViewById(R.id.timepicker);
        timePicker.setIs24HourView(true);

        Button button = findViewById(R.id.submit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("PickerView", "Date is : " + datePicker.getYear() + "-" +
                        Integer.toString(datePicker.getMonth()+1) + "-" +
                        datePicker.getDayOfMonth());
                String text = "Date is : " + datePicker.getYear() + "-" +
                        Integer.toString(datePicker.getMonth()+1) + "-" +
                        datePicker.getDayOfMonth() + "\n";
                text += "Time is : " + timePicker.getCurrentHour() + ":" + timePicker.getCurrentMinute();
                Toast.makeText(getBaseContext(), text, Toast.LENGTH_SHORT).show();

                editor.putString(CHOSEN_DATE, datePicker.getYear() + "-" +
                        Integer.toString(datePicker.getMonth()+1) + "-" +
                        datePicker.getDayOfMonth());
                editor.putString(CHOSEN_TIME, timePicker.getCurrentHour()
                        + ":" + timePicker.getCurrentMinute());
                editor.commit();
            }
        });
    }
}
