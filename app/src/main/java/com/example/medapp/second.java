package com.example.medapp;



import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class second extends AppCompatActivity {

    DatabaseHelper db;
    ListView userlist;
    ArrayList<String> listItem;
    ArrayAdapter adapter;
    EditText add_name;
    Button delete;
    Button update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_second);
        db= new DatabaseHelper (this);
        listItem = new ArrayList<> ();
        add_name= findViewById (R.id.add_name);
        userlist = findViewById (R.id.users_list);
        delete = findViewById (R.id.delete_data);
        update= findViewById (R.id.update);
        viewData ();

        //viewData();
        userlist.setOnItemClickListener (new AdapterView.OnItemClickListener () {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String text = userlist.getItemAtPosition (position).toString ();
                Toast.makeText (second.this, " "+ text, Toast.LENGTH_SHORT).show ();
                add_name.setText (text);




                update.setOnClickListener (new View.OnClickListener () {
                    @Override
                    public void onClick(View v) {

                        Cursor data = db.medID (text);//
                        int itemid= -1;//
                        while (data.moveToNext ())//
                        {
                            itemid= data.getInt (0);//

                        }
                        if(itemid>-1)

                        {
                            String i= new Integer (itemid).toString ();
                            boolean up= db.updatemed (i,add_name.getText ().toString ());
                            // adapter.notifyDataSetChanged ();
                            Intent intent = new Intent (second.this,second.class);
                            startActivity (intent);
                            //  finishActivity (second.this);



                        }



                    }
                });










                delete.setOnClickListener (new View.OnClickListener () {
                    @Override
                    public void onClick(View v) {
                        db.deletedata(text);
                        add_name.setText ("");
                        Intent intent = new Intent (second.this,second.class);
                        startActivity (intent);

                        //viewData ();
                    }
                });


            }
        });














    }



    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent (second.this,medicationactivity.class);
        startActivity (intent);

    }


    private void viewData(){
        Cursor cursor = db.viewData ();
        if(cursor.getCount ()==0){
            Toast.makeText (this, "not show", Toast.LENGTH_SHORT).show ();
        }
        else {
            while (cursor.moveToNext ()){
                listItem.add (cursor.getString (1));
            }
            adapter = new ArrayAdapter<> (this,android.R.layout.simple_list_item_1,listItem);
            userlist.setAdapter (adapter);


        }
    }
}

