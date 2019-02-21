package com.sourcey.materiallogindemo;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;


public class LoginActivity extends AppCompatActivity {
    public static final String  EXTRA_MESSAGE = "com.sourcey.materiallogindemo.MESSAGE";
    public static final String userName = "com.sourcey.materiallogindemo.MESSAGE";
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    public static final String PREFERENCES_FILE_NAME = "MyAppPreferences";

    @BindView(R.id.input_email) EditText _emailText;
    @BindView(R.id.input_password) EditText _passwordText;
    @BindView(R.id.btn_login) Button _loginButton;
    @BindView(R.id.link_signup) TextView _signupLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        SharedPreferences mysettings= getSharedPreferences(PREFERENCES_FILE_NAME, 0);
        String userNameInlogg = mysettings.getString("ususe", null);
        Log.d("LoginActivity","login_activity"+ userNameInlogg);
        if(userNameInlogg != "" || userNameInlogg != null){
            final EditText text1 = (EditText) findViewById(R.id.input_email);
            text1.setText(userNameInlogg);
        }else
        {
            final EditText text1 = (EditText) findViewById(R.id.input_email);
            text1.setText("");
        }

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }


    public void login() {
        Log.d(TAG, "Logga in");


        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(false);
        String email1 = _emailText.getText().toString();

        progressDialog.setMessage("Loggar in "+email1);
        progressDialog.show();


        // TODO: Implement your own authentication logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3500);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Skapa SignUp logiken här (ta ned username osv..)

                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        EditText editText = findViewById(R.id.input_email);
        String message = editText.getText().toString();
        SharedPreferences settingsfile= getSharedPreferences(PREFERENCES_FILE_NAME,0);
        SharedPreferences.Editor myeditor = settingsfile.edit();
        myeditor.putString("ususe", message);
        myeditor.apply();

        Intent intent = new Intent();
        intent.putExtra(userName, message);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Inlogging misslyckades", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("Skriv in gilltigt mejladress");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("Lösenordet måste vara mellan 4-10 bokstäver");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}
