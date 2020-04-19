package com.example.eceapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;

public class CreateAccountActivity extends AppCompatActivity {
    //code by Adam Brewer 30/01/2020 Last Edited by Adam 07/04/2020
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account_gui);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        final int adminDaycare = pref.getInt("daycare", 0); //pull admin's daycare id from shared pref
        //submit button
        Button btnSubmit = findViewById(R.id.btnSubmit);
        //back button
        Button btnBack = findViewById(R.id.buttonCreateBack);
        final RadioButton admin = findViewById(R.id.rdoAdmin);
        final EditText txtFirstName = findViewById(R.id.txtFirstName);
        final EditText txtLastName = findViewById(R.id.txtLastName);
        final EditText txtUsername = findViewById(R.id.txtUsername);
        final EditText txtPassword = findViewById(R.id.txtPassword);
        final EditText txtConfirm = findViewById(R.id.editText5);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(getApplicationContext(), AdminOptions.class);
                startActivity(startIntent);
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create instance of the TeacherClass
                TeacherClass t = new TeacherClass();
                //grab text from first name text input and apply to teacher first name
                t.firstName = txtFirstName.getText().toString().trim();
                //grab text from last name text input and apply to teacher last name
                t.lastName = txtLastName.getText().toString().trim();
                //grab text from username text input and and apply to teacher username
                t.username = txtUsername.getText().toString().trim();
                //grab text from password text input and apply to teacher password
                t.password = txtPassword.getText().toString().trim();
                //grab text from password confirm text input and create string variable
                String confirm = txtConfirm.getText().toString().trim();
                t.daycareID = adminDaycare; //admin can only create a user within their own daycare
                //isAdmin - set to true if admin radio button is selected
                if(admin.isChecked()) {
                    t.isAdmin = true;
                }
                //verify that all information has been provided and is correct
                if(VerifyInfo(t.firstName, t.lastName, t.username, t.password, confirm)) {
                    PasswordHash hash = new PasswordHash();
                    t.password = hash.generatedPassword(t.password);

                    InsertTeacher(t.firstName, t.lastName, t.username, t.password, t.daycareID, t.isAdmin);
                }
            }//end of onClick
        });
    }//end of onCreate

    public void ShowNotification(String message){
        Snackbar display = Snackbar.make(findViewById(R.id.textView), message, Snackbar.LENGTH_LONG);
        display.show();
    }
    public boolean IsEmpty(String string){
        return string.equals("");
    }//end of IsEmpty

    public boolean PasswordMatch(String password, String confirm){
        return password.equals(confirm);
    }//end of PasswordMatch

    public boolean VerifyInfo(String fName, String lName, String user, String pass, String conf){
        if(!IsEmpty(fName)){
            if(!IsEmpty(lName)){
                if(!IsEmpty(user)){
                    if(PasswordMatch(pass, conf)) {
                        return true;
                    } else {
                        ShowNotification("Password and password confirmation do not match");
                    }
                } else {
                    ShowNotification("Please enter a username");
                }
            } else {
                ShowNotification("Please enter a last name");
            }
        } else {
            ShowNotification("Please enter a first name");
        }
        return false;
    }//end of VerifyInfo

    public void InsertTeacher(String fname, String lname, String username, String password, int daycareID, boolean isAdmin){
        //instance of ConnectionClass
        TeacherClass t = new TeacherClass();
        //Attempt to insert new Teacher into database - Create Teacher calls the Create_Teacher stored procedure
        int result = t.CreateTeacher(fname, lname, username, password, daycareID, isAdmin);
        // 0 is returned by the stored procedure if the username already exists in the database
        if (result == 0){
            ShowNotification("Username already exists in the database");
        } else if (result == 1){ // 1 is returned by the stored procedure if the username already exists in the database

            Snackbar snackbar = Snackbar.make(findViewById(R.id.textView), "Account created successfully", Snackbar.LENGTH_LONG).
                    setAction("OKAY", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SharedPreferenceConfig preferenceConfig = new SharedPreferenceConfig(getApplicationContext());
                            preferenceConfig.writeLoginStatus(false);
                            //when user logs out all info stored in shared preferences are cleared (username, password, daycare id, admin status)
                            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                            SharedPreferences.Editor editor = pref.edit();
                            editor.clear();
                            editor.apply();
                            Intent LoginPage = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(LoginPage);
                        }
                    });
            snackbar.show();

        } else if (result == 2) {
            ShowNotification("Database Error");
        } else if (result == 3) {
            ShowNotification("Undefined Error");
        } else {
            ShowNotification("ERROR: account not created");
        }
    }//end of InsertTeacher
}
