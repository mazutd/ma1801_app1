package com.sourcey.materiallogindemo;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class EditDataActivity extends AppCompatActivity {

    private static final String TAG = "EditDataActivity";
    private ImageView backButton;
    private Button avBokaBtn;
    private TextView editable_item, datum_item, antalElevertxt,klassrumtxt;
    public static final String userName = "com.sourcey.materiallogindemo.MESSAGE";
    DatabaseHelper mDatabaseHelper;
    public static final String PREFERENCES_FILE_NAME = "MyAppPreferences";
    private String selectedName, antalElever, klassRum, datum;
    private int selectedID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_data_layout);
        backButton = (ImageView) findViewById(R.id.backButton);
        avBokaBtn = (Button) findViewById(R.id.avBokaBtn);

        //btnDelete = (Button) findViewById(R.id.btnDelete);
        editable_item = (TextView) findViewById(R.id.editable_item2);
        datum_item = (TextView) findViewById(R.id.datumBokning);
        antalElevertxt = (TextView) findViewById(R.id.editable_item4);
        klassrumtxt = (TextView) findViewById(R.id.editable_item3);
        mDatabaseHelper = new DatabaseHelper(this);


        Intent receivedIntent = getIntent();

        //hämta ID för klickade item
        selectedID = receivedIntent.getIntExtra("id", -1); //NOTE: -1 is just the default value

        //hämta namnet (Matcha namn och id  i databasen för att inte radera fel
        selectedName = receivedIntent.getStringExtra("name");
        datum = receivedIntent.getStringExtra("datum");
        antalElever = receivedIntent.getStringExtra("antalElever");
        klassRum = receivedIntent.getStringExtra("klassRum");

        editable_item.setText(antalElever);
        datum_item.setText(antalElever);
        antalElevertxt.setText(klassRum);
        klassrumtxt.setText(selectedName);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        avBokaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("EditDataActivity", "deleteknappen" + selectedID);
                mDatabaseHelper.deleteName(selectedID, selectedName);
                editable_item.setText("");
                toastMessage("Din bokninga har avbokats!");
                Intent returnIntent = getIntent();
                SharedPreferences mysettings = getSharedPreferences(PREFERENCES_FILE_NAME, 0);
                String userNameInlogg = mysettings.getString("ususe", null);
                returnIntent.putExtra(userName, userNameInlogg);
                setResult(99, returnIntent);
                finish();
            }
        });

    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
























