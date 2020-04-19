package com.example.eceapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private SharedPreferenceConfig preferenceConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_gui);

        //Coded By Ethan Steeves Jan 22/20             Edited By Ethan Feb 11/2020

        preferenceConfig = new SharedPreferenceConfig(getApplicationContext());
        if(preferenceConfig.readLoginStatus()){
            Intent startIntent = new Intent(getApplicationContext(), HomePageActivity.class);
           // startIntent.putExtra("org.example.eceapp.USERNAME", username);
            startActivity(startIntent);
            finish();
        }

        Button btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText txtUsername = findViewById(R.id.txtUsername);
                EditText txtPassword = findViewById(R.id.txtPassword);
               // TextView test = findViewById(R.id.testtxt);
                String username = txtUsername.getText().toString().trim();
                String password = txtPassword.getText().toString().trim();
                if(!username.equals("")){
                   // test.setText(username);
                    if(!password.equals("")) {
                        TeacherClass teacher = new TeacherClass();
                        boolean IsActive = teacher.IsTeacherActive(username);
                        if (IsActive) {
                            LoginClass lc = new LoginClass();
                            LoginClass login = lc.login(username, password);
                            int status = login.status;

                            if(status == 1) {
                                preferenceConfig.writeLoginStatus(true);
                                //shared preferences being used to store username, password, daycare id, and admin status for the session - Adam
                                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("username", login.username);
                                editor.putString("password", login.password);
                                editor.putInt("daycare", login.daycareID);
                                editor.putBoolean("admin", login.isAdmin);
                                editor.apply(); //commit needed to save shared preferences - Adam
                                Intent startIntent = new Intent(getApplicationContext(), HomePageActivity.class);
                                startIntent.putExtra("org.example.eceapp.USERNAME", username);
                                startActivity(startIntent);
                                finish();
                            }
                            else if(status == 0 || status == -2){
                                showAlertDialog("Incorrect Credentials", "Incorrect Username or Password was entered");
                            }
                            else{
                                showAlertDialog("Connection Error", "Connection error, please check your internet if problems persist please contact system administrator");
                            }
                        } else {
                            showAlertDialog("Account Inactive", "Cannot sign in. Please contact your administrator.");
                        }
                    }
                    else{
                        showAlertDialog("Invalid Password", "Please enter a valid password");
                    }
                }
                else{
                    showAlertDialog("Invalid Username", "Please enter a valid username");
                }
            }
        });//end of Login button click event
    } //end of on create

    public void showAlertDialog(String title, String message){  //creates a new alertbox (might create a class for this so less repeat code if it gets used in the future
                                                                //This alert box only has an OK button on it that just closes
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle(title);
            alert.setMessage(message);
            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //on click method required to setButton name no code is required in here for the OK button it will just close the alertbox
                }
            });
            alert.create().show();
    }
}
