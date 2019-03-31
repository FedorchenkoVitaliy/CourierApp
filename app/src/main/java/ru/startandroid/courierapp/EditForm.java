package ru.startandroid.courierapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//Актівіті для реалізації редагування записів
public class EditForm extends AppCompatActivity implements View.OnClickListener {

    Button write;
    EditText name, price, notes;
    Intent intent;
    String date, position;
    MainActivity.DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_form);

        name = findViewById(R.id.name);
        price = findViewById(R.id.price);
        notes = findViewById(R.id.notes);
        write = findViewById(R.id.write);
        write.setOnClickListener(this);

        intent = getIntent();
        date = intent.getStringExtra("date");
        position = intent.getStringExtra("id");
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
            db.update("Clients", cv, "id = ?",
                    new String[] { position });
            finish();
        }
    }
}
