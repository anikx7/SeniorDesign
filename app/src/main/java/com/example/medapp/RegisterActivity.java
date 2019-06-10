package com.example.medapp;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {
    DatabaseHelper mydb;
    EditText username;
    EditText password;
    EditText cpassword;
    Button register;
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_register);

        mydb= new DatabaseHelper (this);
        username= findViewById (R.id.edittext_username);
        password= findViewById (R.id.edittext_password);
        cpassword= findViewById (R.id.cnf_password);
        login = findViewById (R.id.button_login);
        register= findViewById (R.id.button_registered);

        login.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent (RegisterActivity.this,MainActivity.class);
                startActivity (intent);
            }
        });

        register.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                String user = username.getText ().toString ().trim ();
                String pwd= password.getText ().toString ().trim ();
                String cnp= cpassword.getText ().toString ().trim ();
                if(pwd.equals (cnp))
                {
                    mydb.adduser (user,pwd);
                    Toast.makeText (RegisterActivity.this,"registration successful",Toast.LENGTH_SHORT).show ();

                    Intent intent= new Intent (RegisterActivity.this,MainActivity.class);
                    startActivity (intent);
                }

                else {
                    Toast.makeText (RegisterActivity.this,"password does not match",Toast.LENGTH_SHORT).show ();


                }


            }

        });

    }
}

