package ru.startandroid.courierapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/* Актівіті для запису даних у БД */
public class RecordForm extends AppCompatActivity implements View.OnClickListener {

    final String LOG_TAG = "myLogs";
    Button write;
    EditText name, price, notes;
    Intent intent;
    String date;
    MainActivity.DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_form);

        name = findViewById(R.id.name);
        price = findViewById(R.id.price);
        notes = findViewById(R.id.notes);
        write = findViewById(R.id.write);
        write.setOnClickListener(this);

        intent = getIntent();
        date = intent.getStringExtra("date");
    }

    @Override
    public void onClick(View v) {
        String formName = name.getText().toString();
        String formPrice = price.getText().toString();
        String formNotes = notes.getText().toString();

        dbHelper = new MainActivity.DBHelper(this);
        ContentValues cv = new ContentValues();
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        if(formName.trim().length() == 0 || formPrice.trim().length() == 0)
            Toast.makeText(this, "Заповніть поле \"Ім'я\" та \"Оплачнео\"",
                    Toast.LENGTH_LONG).show();
        else {
            cv.put("name", formName);
            cv.put("money", formPrice);
            cv.put("notes", formNotes);
            cv.put("date", date);
            db.insert("clients", null, cv);
            Log.d(LOG_TAG, "row inserted");
            finish();
        }
    }
}