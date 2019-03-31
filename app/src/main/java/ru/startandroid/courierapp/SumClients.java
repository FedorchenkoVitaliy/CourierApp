package ru.startandroid.courierapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

/* Актівіті для реалізації підрахунку кількості клієнтів з певного числа до певного числа, яке
ви оберете(працюэ тыльки в межах одного мысяця) */
public class SumClients extends AppCompatActivity  implements View.OnClickListener  {

    TextView textSum;
    EditText firstDay, firstMonth, firstYear, lastDay, lastMonth, lastYear;
    FloatingActionButton sumData;
    private CalendarView calendarView;
    int year, month, day;
    String[] selectionArgs;
    Cursor c;
    String selection, selectedDate, superDate1, superDate2, newSuperDate;
    SQLiteDatabase db;
    MainActivity.DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sum_clients);

        textSum = findViewById(R.id.textSum);
        firstDay = findViewById(R.id.firstDay);
        firstMonth = findViewById(R.id.firstMonth);
        firstYear = findViewById(R.id.firstYear);
        lastDay = findViewById(R.id.lastDay);
        lastMonth = findViewById(R.id.lastMonth);
        lastYear = findViewById(R.id.lastYear);

        sumData = findViewById(R.id.sumData);
        sumData.setOnClickListener(this);

        calendarView = findViewById(R.id.calendarView2);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int Year, int Month, int DayOfMonth) {

                year = Year;
                month = Month;
                day = DayOfMonth;

                selectedDate = new StringBuilder().append(day)
                        .append("-").append(month + 1).append("-").append(year)
                        .append(" ").toString();

                int selectDay = Integer.parseInt(selectedDate.substring(0,
                        selectedDate.indexOf("-")));
                int selectMonth = Integer.parseInt(selectedDate.substring(selectedDate.indexOf("-")
                        + 1, selectedDate.lastIndexOf("-")));
                int selectYear = Integer.parseInt(selectedDate.substring(selectedDate.
                        lastIndexOf("-") + 1, selectedDate.length() - 1));
                if(firstDay.getText().length() == 0) {
                    firstDay.setText(String.valueOf(selectDay));
                    firstMonth.setText(String.valueOf(selectMonth));
                    firstYear.setText(String.valueOf(selectYear));
                } else{
                    lastDay.setText(String.valueOf(selectDay));
                    lastMonth.setText(String.valueOf(selectMonth));
                    lastYear.setText(String.valueOf(selectYear));
                }

            }
        });
    }

    @Override
    public void onClick(View v) {

        String day1 = firstDay.getText().toString();
        String day2 = lastDay.getText().toString();
        String month1 = firstMonth.getText().toString();
        String month2 = lastMonth.getText().toString();
        String year1 = firstYear.getText().toString();
        String year2 = lastYear.getText().toString();

        superDate1=new StringBuilder().append(day1)
                .append("-").append(month1).append("-").append(year1)
                .append(" ").toString();
        superDate2=new StringBuilder().append(day2)
                .append("-").append(month2).append("-").append(year2)
                .append(" ").toString();

        dbHelper = new MainActivity.DBHelper(this);
        db = dbHelper.getWritableDatabase();

        ArrayList<String> listDate = new ArrayList<>();
        int Day1 = Integer.parseInt(superDate1.substring(0, superDate1.indexOf("-")));
        int Month1 = Integer.parseInt(superDate1.substring(superDate1.indexOf("-") + 1, superDate1.lastIndexOf("-")));
        int Year1 = Integer.parseInt(superDate1.substring(superDate1.lastIndexOf("-") + 1, superDate1.length() - 1));
        int Day2 = Integer.parseInt(superDate2.substring(0, superDate2.indexOf("-")));
        int Month2 = Integer.parseInt(superDate2.substring(superDate2.indexOf("-") + 1, superDate2.lastIndexOf("-")));
        int Year2 = Integer.parseInt(superDate2.substring(superDate2.lastIndexOf("-") + 1, superDate2.length() - 1));
        if (Month1 == Month2 && Year1 == Year2) {
            while (Day1 <= Day2) {
                newSuperDate = new StringBuilder().append(Integer.toString(Day1)).append("-").
                        append(Integer.toString(Month1)).append("-").
                        append(Integer.toString(Year1)).append(" ").toString();
                Day1++;
                listDate.add(newSuperDate);
            }
        }

        ArrayList<String> listName = new ArrayList<>();
        for (int i = 0; i < listDate.size(); i++) {
            selection = "date=?";
            selectionArgs = new String[]{listDate.get(i)};
            c = db.query("clients", null, selection, selectionArgs, null,
                    null, null);
            if (c.moveToFirst()) {
                int nameColIndex = c.getColumnIndex("name");
                do {
                    for (int j = 0; j < selectionArgs.length; j++) {
                        listName.add(c.getString(nameColIndex));
                    }
                } while (c.moveToNext());
            } else textSum.setText("");
            textSum.setText(String.valueOf(listName.size()));
            c.close();
        }
    }
}