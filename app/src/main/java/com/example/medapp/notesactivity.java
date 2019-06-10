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

public class notesactivity extends AppCompatActivity {
    DatabaseHelper db;
    Button add_note;
    EditText add_n;
    Button delete;
    Button update;
    ListView notes_list;

    ArrayList<String> noteItem;
    ArrayAdapter ad1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_notesactivity);


        db= new DatabaseHelper (this);
        noteItem = new ArrayList<> ();

        add_note = findViewById (R.id.add_note);
        add_n = findViewById (R.id.add_n);
        notes_list = findViewById (R.id.notes_list);
        delete = findViewById (R.id.delete_note);
        update= findViewById (R.id.update);

        viewNotes();


        notes_list.setOnItemClickListener (new AdapterView.OnItemClickListener () {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String text = notes_list.getItemAtPosition (position).toString ();
                Toast.makeText (notesactivity.this, " "+ text, Toast.LENGTH_SHORT).show ();
                add_n.setText (text);


                update.setOnClickListener (new View.OnClickListener () {
                    @Override
                    public void onClick(View v) {

                        Cursor data = db.noteID (text);//
                        int itemid= -1;//
                        while (data.moveToNext ())//
                        {
                            itemid= data.getInt (0);//

                        }
                        if(itemid>-1)

                        {
                            String i= new Integer (itemid).toString ();
                            boolean up= db.updatenote (i,add_n.getText ().toString ());
                            // adapter.notifyDataSetChanged ();
                            Intent intent = new Intent (notesactivity.this,notesactivity.class);
                            startActivity (intent);
                            //  finishActivity (second.this);



                        }



                    }
                });









                delete.setOnClickListener (new View.OnClickListener () {
                    @Override
                    public void onClick(View v) {
                        db.deletenote (text);
                        add_n.setText ("");

                        //viewData ();
                        Intent intent = new Intent (notesactivity.this,notesactivity.class);
                        startActivity (intent);
                    }
                });


            }
        });

        add_note.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                String name = add_n.getText ().toString ();

                //////messed up here
                if(name.equals ("farzana"))
                {
                    Toast.makeText (notesactivity.this, "its me", Toast.LENGTH_SHORT).show ();
                }
                if(!name.equals ("") && db.insertNote (name) )
                {
                    Toast.makeText (notesactivity.this, "ADDED",Toast.LENGTH_SHORT ).show ();
                    add_n.setText ("");
                    noteItem.clear ();
                    viewNotes ();
                }
                else
                    Toast.makeText (notesactivity.this,"not added",Toast.LENGTH_SHORT).show ();
            }
        });



    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent (notesactivity.this,homeactivity.class);
        startActivity (intent);

    }






    private void viewNotes() {
        Cursor cursor = db.viewNotes ();
        if(cursor.getCount ()==0){
            Toast.makeText (this, "not show", Toast.LENGTH_SHORT).show ();       //if nothinf is in database
        }
        ////if there is data this is moving the cursor to each one of the data
        else {
            while (cursor.moveToNext ()){
                noteItem.add (cursor.getString (1));
            }
            ad1 = new ArrayAdapter<> (this,android.R.layout.simple_list_item_1,noteItem);     //showing data in to list view
            notes_list.setAdapter (ad1);


        }
    }


}

