package com.example.medapp;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText username;
    EditText password;
    ImageButton login;
    ImageButton register;
    DatabaseHelper mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);

        setContentView (R.layout.activity_main);
        mydb= new DatabaseHelper (this);
        username= findViewById (R.id.edittext_username);
        password= findViewById (R.id.edittext_password);
        login= findViewById (R.id.button_login);

        register= findViewById (R.id.button_register);


        register.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (MainActivity.this,RegisterActivity.class);
                startActivity (intent);
            }
        });


        login.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                String user= username.getText ().toString ().trim ();
                String pwd = password.getText ().toString ().trim ();
                Boolean res = mydb.check (user,pwd);
                if(res==true){
                    Toast.makeText (MainActivity.this,"login successful",Toast.LENGTH_SHORT).show ();
                    Intent intent = new Intent (MainActivity.this,homeactivity.class);
                    startActivity (intent);
                }
                else {
                    Toast.makeText (MainActivity.this,"login error",Toast.LENGTH_SHORT).show ();

                }

            }
        });

    }
}

