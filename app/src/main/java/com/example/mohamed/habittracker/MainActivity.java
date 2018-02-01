package com.example.mohamed.habittracker;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.mohamed.habittracker.data.HabitContract.HabitEntry;
import com.example.mohamed.habittracker.data.HabitDbHelper;

public class MainActivity extends AppCompatActivity {
    private HabitDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                startActivity(intent);
            }
        });

        mDbHelper = new HabitDbHelper(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabase();
    }

    private Cursor read() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                HabitEntry._ID,
                HabitEntry.COLUMN_NAME,
                HabitEntry.COLUMN_START_DATE,
                HabitEntry.COLUMN_NUMBER_OF_TIMES
        };

        Cursor cursor = db.query(HabitEntry.TABLE_NAME, projection, null, null, null, null, null);
        return cursor;
    }

    private void displayDatabase() {
        Cursor cursor = read();

        TextView displayView = (TextView) findViewById(R.id.text_view_habit);

        try {
            displayView.setText("The habit table contains " + cursor.getCount() + " pets.\n\n");
            displayView.append(HabitEntry._ID + " - " +
                    HabitEntry.COLUMN_NAME + " - " +
                    HabitEntry.COLUMN_START_DATE + " - " +
                    HabitEntry.COLUMN_NUMBER_OF_TIMES + " - " + "\n");

            int idColumnIndex = cursor.getColumnIndex(HabitEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(HabitEntry.COLUMN_NAME);
            int dateColumnIndex = cursor.getColumnIndex(HabitEntry.COLUMN_START_DATE);
            int numberOfTimesColumnIndex = cursor.getColumnIndex(HabitEntry.COLUMN_NUMBER_OF_TIMES);

            while (cursor.moveToNext()) {
                int currentID = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                String currentDate = cursor.getString(dateColumnIndex);
                int currentNumberOfTimes = cursor.getInt(numberOfTimesColumnIndex);

                displayView.append(("\n" + currentID + " - " +
                        currentName + " - " +
                        currentDate + " - " +
                        currentNumberOfTimes));
            }
        } finally {
            cursor.close();
        }
    }

    private void insertHabit() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values1 = new ContentValues();
        values1.put(HabitEntry.COLUMN_NAME, "Drink water");
        values1.put(HabitEntry.COLUMN_NUMBER_OF_TIMES, 2);
        values1.put(HabitEntry.COLUMN_NAME, "Walk my dog");
        values1.put(HabitEntry.COLUMN_NUMBER_OF_TIMES, 1);
        db.insert(HabitEntry.TABLE_NAME, null, values1);
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
        if (id == R.id.action_insert_dummy_data) {
            insertHabit();
            displayDatabase();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
