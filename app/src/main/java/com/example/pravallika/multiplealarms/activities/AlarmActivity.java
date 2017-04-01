package com.example.pravallika.multiplealarms.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.pravallika.multiplealarms.R;
import com.example.pravallika.multiplealarms.fragments.TimePickerFragment2;

public class AlarmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        sunday();
        monday();
        tuesday();
        wednesday();
        thursday();
        friday();
        saturday();

        LinearLayout setAlarm = (LinearLayout) findViewById(R.id.setAlarm);

        setAlarm.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                TimePickerFragment2 newFragment = new TimePickerFragment2();
                newFragment.show(getFragmentManager(), "TimePicker");
            }
        });
    }

    private void sunday() {
        Button sunday = (Button) findViewById(R.id.sunday);
        sunday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button sunday = (Button) findViewById(R.id.sunday);
                sunday.setBackgroundResource(R.drawable.rectangle);
            }
        });

    }

    private void monday() {
        Button sunday = (Button) findViewById(R.id.monday);
        sunday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button sunday = (Button) findViewById(R.id.monday);
                sunday.setBackgroundResource(R.drawable.rectangle);
            }
        });

    }

    private void tuesday() {
        Button sunday = (Button) findViewById(R.id.tuesday);
        sunday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button sunday = (Button) findViewById(R.id.tuesday);
                sunday.setBackgroundResource(R.drawable.rectangle);
            }
        });

    }

    private void wednesday() {
        Button sunday = (Button) findViewById(R.id.wednesday);
        sunday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button sunday = (Button) findViewById(R.id.wednesday);
                sunday.setBackgroundResource(R.drawable.rectangle);
            }
        });

    }

    private void thursday() {
        Button sunday = (Button) findViewById(R.id.thursday);
        sunday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button sunday = (Button) findViewById(R.id.thursday);
                sunday.setBackgroundResource(R.drawable.rectangle);
            }
        });

    }

    private void friday() {
        Button sunday = (Button) findViewById(R.id.friday);
        sunday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button sunday = (Button) findViewById(R.id.friday);
                sunday.setBackgroundResource(R.drawable.rectangle);
            }
        });

    }

    private void saturday() {
        Button sunday = (Button) findViewById(R.id.saturday);
        sunday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button sunday = (Button) findViewById(R.id.saturday);
                sunday.setBackgroundResource(R.drawable.rectangle);
            }
        });

    }

}

