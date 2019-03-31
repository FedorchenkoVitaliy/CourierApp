package ru.startandroid.courierapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int CM_DELETE_ID = 1;
    private static final int CM_EDIT_ID = 2;
    DBHelper dbHelper;
    Button sum;
    FloatingActionButton add;
    Intent intent;
    SQLiteDatabase db;
    String selection, selectedDate;
    String[] selectionArgs;
    Cursor c;
    CalendarView calendarView;
    int year, month, day;
    Date dateNow;
    SimpleDateFormat formatForDateNow, formatForDateNowMonth, formatForDateNowYear;
    ArrayList<ClientsConstruct> clients = new ArrayList<>();
    ArrayList<String> id = new ArrayList<>();
    ShowClients adapter;
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sum = findViewById(R.id.sum);
        sum.setOnClickListener(this);
        add = findViewById(R.id.add);
        add.setOnClickListener(this);

        list = findViewById(R.id.list);

        dbHelper = new DBHelper(this);

        dateNow = new Date();
        formatForDateNow = new SimpleDateFormat("d-M-yyyy");
        formatForDateNowMonth = new SimpleDateFormat("M");
        formatForDateNowYear = new SimpleDateFormat("yyyy");
        selectedDate = new StringBuilder().append(formatForDateNow.format(dateNow))
                .append(" ").toString();


        /* БД показує всі записи з датою,що вибрана в календарі через методи readDB() і заповнює
        адаптер методом fillData()
         */
        calendarView = findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int Year, int Month, int DayOfMonth){

                year = Year;
                month = Month;
                day = DayOfMonth;

                selectedDate = new StringBuilder().append(day)
                        .append("-").append(month + 1).append("-").append(year)
                        .append(" ").toString();


                readBD();
            }
        });

        selection = null;
        selectionArgs = null;
        c = null;

        db = dbHelper.getWritableDatabase();

    }

    void  fillData() {
        selection = "date=?";
        db = dbHelper.getWritableDatabase();
        selectionArgs = new String[]{selectedDate};
        c = db.query("clients", null, selection, selectionArgs, null,
                null, null);
        if (c.moveToFirst()) {
            int idColIndex = c.getColumnIndex("id");
            int nameColIndex = c.getColumnIndex("name");
            int moneyColIndex = c.getColumnIndex("money");
            int notesColIndex = c.getColumnIndex("notes");
            do {
                for (int i = 0; i < selectionArgs.length; i++) {
                    clients.add(new ClientsConstruct(c.getString(nameColIndex),
                            c.getString(moneyColIndex), c.getString(notesColIndex)));
                    id.add(c.getString(idColIndex));
                }
            }while (c.moveToNext());
            c.close();
        }
    }

    void readBD() {
        clients.clear();
        fillData();
        adapter = new ShowClients(this, clients);
        list.setAdapter(adapter);
        registerForContextMenu(list);
    }

    //Контекстне меню для редагування і видалення записів із БД
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, CM_DELETE_ID, 0, "Удалити запис");
        menu.add(0, CM_EDIT_ID, 0, "Редагувати запис");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case CM_DELETE_ID:
                    AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo)
                            item.getMenuInfo();
                    db.delete("Clients", "id = " + id.get(acmi.position),
                            null);
                    id.remove(acmi.position);
                    clients.remove(acmi.position);
                    adapter.notifyDataSetChanged();
                    return true;
            case CM_EDIT_ID:
                AdapterView.AdapterContextMenuInfo acmiEdit = (AdapterView.AdapterContextMenuInfo)
                        item.getMenuInfo();
                intent = new Intent(this, EditForm.class);
                intent.putExtra("date", selectedDate);
                intent.putExtra("id", id.get(acmiEdit.position));
                startActivity(intent);
                break;
        }
        return super.onContextItemSelected(item);

    }

    //Натискання кнопок
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add:
                intent = new Intent(this, RecordForm.class);
                intent.putExtra("date", selectedDate);
                startActivity(intent);
                break;
            case R.id.sum:
                intent = new Intent(this, SumClients.class);
                startActivity(intent);
                break;
        }
    }

    //Клас БД
    static class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, "Clients", null, 1);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table clients ("
                    + "id integer primary key autoincrement,"
                    + "name text," + "money int," + "notes text," + "date text" + ");");
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }
    }
}