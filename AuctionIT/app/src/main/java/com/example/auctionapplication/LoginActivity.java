package com.example.auctionapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener  {
    private TextInputEditText emailText;
    private TextInputEditText passwordText;

    private InputValidation inputValidation;
    private DatabaseHelper databaseHelper;
    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView title = (TextView) findViewById(R.id.titleTxtView);
        title.setText(Html.fromHtml("Auction<font color='#DC7633'>IT</font>"));

         emailText = (TextInputEditText) findViewById(R.id.email);
         textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
         textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);
         passwordText = (TextInputEditText) findViewById(R.id.password);



        Button cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
        Button signIn = (Button) findViewById(R.id.signIN);
        signIn.setOnClickListener(this);
        Button Register = (Button) findViewById(R.id.register);
        Register.setOnClickListener(this);
        Button forgotPassword = (Button) findViewById(R.id.forgotPassword);
        forgotPassword.setOnClickListener(this);
        emailText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                emailText.setSelection( emailText.getText().length(),0);
            }
        });
        passwordText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                passwordText.setSelection( passwordText.getText().length(),0);
            }
        });


        PreferenceUtils utils = new PreferenceUtils();
        if(utils.getEmail(this)!= null){
            Intent intent = new Intent(LoginActivity.this, LoginDisplayActivity.class);
            startActivity(intent);
        }
        databaseHelper = new DatabaseHelper(LoginActivity.this);
        inputValidation = new InputValidation(LoginActivity.this);

    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()){
            case R.id.cancel:
                intent = new Intent(this, AccountActivity.class);
                startActivity(intent);
                break;
            case R.id.signIN:
                verifyCredentials();
                break;
            case R.id.register:
                Intent intentRegister = new Intent(this, RegisterActivity.class);
                startActivity(intentRegister);
                break;
            case R.id.forgotPassword:
                intent = new Intent(this,ForgotPassword.class);
                startActivity(intent);
                break;
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }

    private void verifyCredentials(){
        if (!inputValidation.isInputEditTextFilled(emailText, textInputLayoutEmail, "Enter Valid Email")) {
            return;
        }
        if (!inputValidation.isInputEditTextEmail(emailText, textInputLayoutEmail, "Enter Valid Email")) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(passwordText, textInputLayoutPassword, "Enter Valid Password")) {
            return;
        }

        String email = emailText.getText().toString().trim();
        String password = passwordText.getText().toString().trim();

        if (databaseHelper.checkUser(email, password)) {
            PreferenceUtils.saveEmail(email, this);
            PreferenceUtils.savePassword(password, this);
            Intent accountsIntent = new Intent(LoginActivity.this, LoginDisplayActivity.class);
            accountsIntent.putExtra("EMAIL", emailText.getText().toString().trim());
            emptyInputEditText();
            startActivity(accountsIntent);
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "Wrong Email or Password", Toast.LENGTH_LONG).show();
        }
    }

    private void emptyInputEditText(){
        emailText.setText(null);
        passwordText.setText(null);
    }
}
