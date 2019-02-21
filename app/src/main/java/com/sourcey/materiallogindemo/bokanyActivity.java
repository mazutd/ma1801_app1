package com.sourcey.materiallogindemo;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Date;


public class bokanyActivity extends AppCompatActivity {
    private static final String TAG = "bokanyActivity";
    DatabaseHelper mDatabaseHelper;
    private EditText editText;
    private Button btnAdd;
    private Button addbtn;
    public static final String userName = "com.sourcey.materiallogindemo.MESSAGE";
    private ImageView backbtn;
    private CalendarView datum;
    private EditText klassRumtext;
    public static final String PREFERENCES_FILE_NAME = "MyAppPreferences";
    public String selectedDates = "";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bokany);

        mDatabaseHelper = new DatabaseHelper(this);
        editText = (EditText) findViewById(R.id.antalElever);
        klassRumtext = (EditText) findViewById(R.id.klassRum);
        addbtn = (Button) findViewById(R.id.addbutton);
        datum = (CalendarView) findViewById(R.id.datumCalender);
        datum.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                selectedDates = sdf.format(new Date(year - 1900, month, dayOfMonth));
                Log.d("bokanyActivity", "valda datumet är " + selectedDates);
            }
        });

        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newEntry = editText.getText().toString();
                String klassRum = klassRumtext.getText().toString();
                SharedPreferences mysettings = getSharedPreferences(PREFERENCES_FILE_NAME, 0);
                String userNameInlogg = mysettings.getString("ususe", null);

                if (editText.length() != 0) {
                    AddData(newEntry, userNameInlogg, selectedDates, klassRum);
                    editText.setText("");
                } else {
                    toastMessage("Fyll i alla fält för att kunna skapa bokning");
                }

            }
        });


        backbtn = (ImageView) findViewById(R.id.backImage);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    public void AddData(String newEntry, String userNameInlogg, String datum, String klassRum) {
        boolean insertData = mDatabaseHelper.addData(newEntry, userNameInlogg, datum.toString(), klassRum);

        if (insertData) {
            toastMessage("Din bokning är klar");
            mDatabaseHelper.getData(userNameInlogg);

            Intent returnIntent = getIntent();
            returnIntent.putExtra("hahamomo", "Mazen");
            returnIntent.putExtra(userName, userNameInlogg);
            setResult(99, returnIntent);
            finish();


        } else {
            toastMessage("FEL FEL FEEEL ! ");
        }
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


}