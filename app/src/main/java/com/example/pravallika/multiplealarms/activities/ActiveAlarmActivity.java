package com.example.pravallika.multiplealarms.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.example.pravallika.multiplealarms.R;

/**
 * Created by RitenVithlani on 4/29/17.
 */

public class ActiveAlarmActivity extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.active_alarm_activity);

        ImageView ivCancelAlarm = (ImageView) findViewById(R.id.iv_cancel_alarm);
        ivCancelAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int requestCode = getIntent().getIntExtra("requestCode", 0);
                //AlarmHelper.cancelAlarm();
                //off switch

                // return to the alarm activity
                Intent intent = new Intent(ActiveAlarmActivity.this, AlarmActivity.class);
                intent.putExtra("turnOffSwitch", true);
                startActivity(intent);
            }
        });
    }
}
