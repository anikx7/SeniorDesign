package com.example.medapp;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class homeactivity extends AppCompatActivity {
    Button medication;
    Button reminder;
    Button history;
    Button notes
            ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_homeactivity);
        medication = findViewById (R.id.button_med);
        reminder= findViewById (R.id.button_reminder);
        history= findViewById (R.id.button_history);
        notes= findViewById (R.id.button_notes);


        medication.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent (homeactivity.this,medicationactivity.class);
                startActivity (intent);
            }
        });
        reminder.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (homeactivity.this,All_alarm.class);
                startActivity (intent);
            }
        });
        notes.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (homeactivity.this,notesactivity.class);
                startActivity (intent);
            }
        });




    }
}

