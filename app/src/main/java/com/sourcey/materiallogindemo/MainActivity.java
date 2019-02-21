package com.sourcey.materiallogindemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.sourcey.materiallogindemo.LoginActivity.userName;


public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_SIGNUP = 0;
    @BindView(R.id.addbokning)
    ImageView _backicon;
    @BindView(R.id.logOutbtn)
    TextView _logout;
    private static final String TAG = "MainActivity";
    DatabaseHelper mDatabaseHelper;
    private HorizontalScrollView mListView;
    public static final String PREFERENCES_FILE_NAME = "MyAppPreferences";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences mysettings = getSharedPreferences(PREFERENCES_FILE_NAME, 0);
        String userNameInlogg = mysettings.getString("ususe", null);
        Log.d("LoginActivity", "Mianmian" + userNameInlogg);

        if (userNameInlogg != "Användare") {
            Intent intent = new Intent();
            intent.putExtra(userName, userNameInlogg);
            setResult(RESULT_OK, intent);
            Log.d("LoginActivity", "finns användare" + userNameInlogg);

        } else {
            Log.d("MainActivity", "sssss: INTE INLOGGAD");


        }
        mListView = (HorizontalScrollView) findViewById(R.id.bokningarView);
        mDatabaseHelper = new DatabaseHelper(this);
        ButterKnife.bind(this);
        Intent get = getIntent();
        String user = get.getStringExtra(userName);
        Intent intent = new Intent(this, LoginActivity.class);

        startActivityForResult(intent, 1);


        LinearLayout gallery = findViewById(R.id.gallery);
        LayoutInflater inflater = LayoutInflater.from(this);
        LayoutInflater inflater2 = LayoutInflater.from(this);


        for (int i = 1; i <= 3; i++) {
            View view2 = inflater.inflate(R.layout.items, gallery, false);

            if (i == 1) {
                TextView textView1 = view2.findViewById(R.id.textView3);
                textView1.setText("Nackademin ");
                ImageView imageView = view2.findViewById(R.id.imageView2);
                imageView.setImageResource(R.drawable.classroomone);

                gallery.addView(view2);

            }
            if (i == 2) {
                TextView textView1 = view2.findViewById(R.id.textView3);
                textView1.setText("BOKA NU");
                ImageView imageView = view2.findViewById(R.id.imageView2);
                imageView.setImageResource(R.drawable.classroomtow);

                gallery.addView(view2);

            }
            if (i == 3) {
                TextView textView1 = view2.findViewById(R.id.textView3);
                textView1.setText("VISSTE DU?");
                ImageView imageView = view2.findViewById(R.id.imageView2);
                imageView.setImageResource(R.drawable.classroomthree);

                gallery.addView(view2);

            }


        }
        _backicon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), bokanyActivity.class);
                startActivityForResult(intent, 99);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
        _logout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d("MainActivity", "loggarut");
                SharedPreferences settingsfile = getSharedPreferences(PREFERENCES_FILE_NAME, 0);
                SharedPreferences.Editor myeditor = settingsfile.edit();
                myeditor.putString("ususe", "");
                myeditor.apply();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                finish();
            }
        });


    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("MainActivity", "rullar in " + resultCode);


        if (resultCode == RESULT_OK || resultCode == 99) {
            Log.d("MainActivity", "rullar in ");
            String strEditText = data.getStringExtra(userName);

            TextView displayMessageView = findViewById(R.id.anvandaren);
            displayMessageView.setText(strEditText);
            LayoutInflater inflater2 = LayoutInflater.from(this);
            LinearLayout gallery2 = findViewById(R.id.gallery2);
            gallery2.removeAllViews();
            SharedPreferences mysettings = getSharedPreferences(PREFERENCES_FILE_NAME, 0);
            String userNameInlogg = mysettings.getString("ususe", null);
            Cursor data1 = mDatabaseHelper.getData(userNameInlogg);
            ArrayList<String> listData = new ArrayList<>();
            while (data1.moveToNext()) {
                //get the value from the database in column 1
                //then add it to the
                Log.d(TAG, data1.getString(1).toString() + "+++" + userNameInlogg.toString());

                listData.add(data1.getString(1));
                Log.d("MainActivity", "databas:" + data1.getString(1));
                View view3 = inflater2.inflate(R.layout.bokningar, gallery2, false);
                TextView textView = view3.findViewById(R.id.textView6);
                TextView datumView = view3.findViewById(R.id.textView7);
                TextView antalElever = view3.findViewById(R.id.textView11);
                final TextView skapadesAv = view3.findViewById(R.id.textView8);
                final TextView visaBokning = view3.findViewById(R.id.textView12);

                textView.setText(data1.getString(4));
                datumView.setText(data1.getString(3));
                antalElever.setText("PASSAR+ " + data1.getString(2));
                skapadesAv.setText("Bokningen skapades av " + data1.getString(1));
                visaBokning.setId(Integer.parseInt(data1.getString(0)));
                visaBokning.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        int id = visaBokning.getId();
                        Cursor datas = mDatabaseHelper.getItemID(id);
                        int itemID = -1;
                        String namn = "";
                        String datum = "";
                        String antalElever = "";
                        String klassRum = "";

                        while (datas.moveToNext()) {
                            itemID = datas.getInt(0);
                            namn = datas.getString(1);
                            antalElever = datas.getString(3);
                            datum = datas.getString(2);
                            klassRum = datas.getString(4);
                        }
                        if (itemID > -1) {
                            Intent editScreenIntent = new Intent(MainActivity.this, EditDataActivity.class);
                            editScreenIntent.putExtra("id", itemID);
                            editScreenIntent.putExtra("name", namn);
                            editScreenIntent.putExtra("datum", datum);
                            editScreenIntent.putExtra("antalElever", antalElever);
                            editScreenIntent.putExtra("klassRum", klassRum);
                            startActivityForResult(editScreenIntent, 99);
                        }
                    }
                });
                gallery2.addView(view3);


            }


        }
        if (resultCode == RESULT_CANCELED) {
            //Gör nåt om CANCELED
        }


    }
}

