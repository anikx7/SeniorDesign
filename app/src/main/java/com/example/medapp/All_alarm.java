package com.example.medapp;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class All_alarm extends AppCompatActivity {
    private ArrayList<String> data = new ArrayList<String>();
    private ArrayList<Boolean> status_data = new ArrayList<Boolean>();
    DatabaseHelper mDatabaseHelper;
    static int temp=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_all_alarm);
        ListView lv = (ListView) findViewById(R.id.listview);
        mDatabaseHelper = new DatabaseHelper(this);
        generateListContent();


        lv.setAdapter(new MyListAdaper(this, R.layout.list_item, data));
        //lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        //  @Override
        //public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //Toast.makeText(MainActivity.this, "List item was clicked at " + position, Toast.LENGTH_SHORT).show();
        //}
        //});
    }

    private void generateListContent() {

        Cursor cursor_data = mDatabaseHelper.getData();
        while(cursor_data.moveToNext()){
            //get the value from the database in column 1
            //then add it to the ArrayList

            if(cursor_data.getString(3).equalsIgnoreCase("1") ){
                status_data.add(false);
                data.add(cursor_data.getString(1) +" \nAlarm Enable "+"\nAlarm time:"+cursor_data.getString(2) );
            }
            else {
                data.add(cursor_data.getString(1) + " \nAlarm Disable"+"\nAlarm time:"+cursor_data.getString(2));
                status_data.add(true);
            }
        }
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

    private class MyListAdaper extends ArrayAdapter<String> {
        private int layout;
        private List<String> mObjects;
        int index=0;
        private MyListAdaper(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
            mObjects = objects;
            layout = resource;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder mainViewholder = null;
            if(convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.thumbnail = (ImageView) convertView.findViewById(R.id.list_item_thumbnail);
                viewHolder.title = (TextView) convertView.findViewById(R.id.list_item_text);
                viewHolder.set_alarm = (Button) convertView.findViewById(R.id.list_item_set);
                viewHolder.cancel_alarm = (Button) convertView.findViewById(R.id.list_item_cancel);
                convertView.setTag(viewHolder);
            }
            mainViewholder = (ViewHolder) convertView.getTag();





            mainViewholder.set_alarm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String alarm_info = Integer.toString(position) + "," + Integer.toString(1);
                    Intent intent = new Intent(All_alarm.this, Alarm_set.class);
                    intent.putExtra("alarm_code",alarm_info);
                    Toast.makeText(getContext(), "Button was enabled for list item " + position, Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }
            });
            mainViewholder.cancel_alarm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String alarm_info = Integer.toString(position) + "," + Integer.toString(0);
                    Intent intent = new Intent(All_alarm.this, Alarm_set.class);
                    intent.putExtra("alarm_code",alarm_info);
                    Toast.makeText(getContext(), "Button was disabled for list item " + position, Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }
            });

            mainViewholder.title.setText(getItem(position));

            return convertView;
        }
    }
    public class ViewHolder {

        ImageView thumbnail;
        TextView title;
        Button set_alarm;
        Button cancel_alarm;
    }
}

