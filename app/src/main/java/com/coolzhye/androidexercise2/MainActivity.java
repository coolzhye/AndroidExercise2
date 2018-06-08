package com.coolzhye.androidexercise2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final int MAX_TEN_MINUTE = 143;
    private final int MAX_HALF_AN_HOUR = 47;
    private final int MAX_AN_HOUR = 23;

    private TextView textViewTime;
    private Switch switchRepeat;
    private LinearLayout layoutRepeat;
    ArrayList<CheckBox> checkBoxes = new ArrayList<>();

    private int hour = 0;
    private int min = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();
    }

    private void initialize() {
        this.textViewTime = findViewById(R.id.textViewTime);
        this.textViewTime.setText("00:00");

        this.switchRepeat = findViewById(R.id.switchRepeat);
        this.switchRepeat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                layoutRepeat.setVisibility(b ? View.VISIBLE : View.INVISIBLE);
            }
        });

        this.layoutRepeat = findViewById(R.id.layoutRepeat);
        this.layoutRepeat.setVisibility(View.INVISIBLE);

        int[] checkBoxIDs = { R.id.chkSun, R.id.chkMon, R.id.chkTue, R.id.chkWed, R.id.chkThu, R.id.chkFri, R.id.chkSat };

        for (int i = 0; i < checkBoxIDs.length; i++) {
            CheckBox checkBox = findViewById(checkBoxIDs[i]);
            checkBox.setTag(i+1);
            checkBoxes.add(checkBox);
        }

        SeekBar seekBar = findViewById(R.id.seekBar);
        seekBar.setMax(MAX_TEN_MINUTE);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                switch (seekBar.getMax()) {
                    case MAX_AN_HOUR:
                        hour = i;
                        min = 0;
                        break;
                    case MAX_HALF_AN_HOUR:
                        hour = i / 2;
                        min = (i % 2) * 30;
                        break;
                    case MAX_TEN_MINUTE:
                        hour = i / 6;
                        min = (i % 6) * 10;
                        break;
                }

                @SuppressLint("DefaultLocale")
                String formattedTime = String.format("%02d:%02d", hour, min);
                textViewTime.setText(formattedTime);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        Button buttonConfirm = findViewById(R.id.buttonConfirm);
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAlarm(hour);
            }
        });
    }

    public void createAlarm(int hour) {
//        ArrayList<Integer> days = new ArrayList<>(
//                Arrays.asList(Calendar.SUNDAY, Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY, Calendar.SATURDAY));

        ArrayList<Integer> days = new ArrayList<>();

        for (CheckBox checkBox : checkBoxes) {
            if (checkBox.isChecked()) {
                days.add(Integer.valueOf(checkBox.getTag().toString()));
            }
        }

        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM)
                .putExtra(AlarmClock.EXTRA_HOUR, hour)
                .putExtra(AlarmClock.EXTRA_MINUTES, min);

        if (switchRepeat.isChecked()) {
            intent.putExtra(AlarmClock.EXTRA_DAYS, days);
        }

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}