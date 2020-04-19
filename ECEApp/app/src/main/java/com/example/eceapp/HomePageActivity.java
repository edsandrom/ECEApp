package com.example.eceapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
//Coded by Ethan Feb 3
public class HomePageActivity extends AppCompatActivity {

    private SharedPreferenceConfig preferenceConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page_gui);

        preferenceConfig = new SharedPreferenceConfig(getApplicationContext());
        //pulling username, password, daycare id, and admin status from shared preferences
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        String username = pref.getString("username", null);
        String password = pref.getString("password", null);
        int daycare = pref.getInt("daycare", -1);
        boolean admin = pref.getBoolean("admin", false);

        Button btnForm = findViewById(R.id.btnForms);
        Button btnManual = findViewById(R.id.btnManuals);
        Button btnStudent = findViewById(R.id.btnStudent);
        Button btnLogout = findViewById(R.id.btnLogout);
        Button btnViewStudentList = findViewById(R.id.btnViewStudentList);
        Button btnAdminOptions = findViewById(R.id.btnAdminOptions);
        Button btnEmergency = findViewById(R.id.btnEmergency);
        //if the user logged in is not an admin, the Edit Accounts button will be disabled
        if(admin == false){
            btnAdminOptions.setEnabled(false);
            View b = btnAdminOptions;
            b.setVisibility(View.GONE);
        }

        btnForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoToForms();
            }
        });

        btnManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoToManuals();
            }
        });

        btnStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoToCreateStudent();
            }
        });



        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout(); //this will have to be fixed to clear login information and not allow the user to hit the back button on application to return to previous page
            }
        });

        btnViewStudentList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewStudentList();
            }
        });

        btnAdminOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdminOptionsPage();
            }
        });

        btnEmergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EmergencyPage();
            }
        });
    }

    private void GoToForms(){
        Intent startIntent = new Intent(getApplicationContext(), FormAndTemplateSettings.class);
        startActivity(startIntent);
    }//redirect to forms page

    private void GoToManuals(){
        Intent startIntent = new Intent(getApplicationContext(), ManualPageActivity.class);
        startActivity(startIntent);
    }//go to manuals page

    public void GoToCreateStudent(){
        Intent startIntent = new Intent(getApplicationContext(), CreateStudentActivity.class);
        startActivity(startIntent);
    }//go to create student page

    private void logout(){
        preferenceConfig.writeLoginStatus(false);
        //when user logs out all info stored in shared preferences are cleared (username, password, daycare id, admin status)
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.apply();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    } //logs out goes to login page


    private void viewStudentList(){
        Intent startIntent = new Intent(getApplicationContext(), StudentListView.class);
        startActivity(startIntent);
    } //go to Student List displayed - Phoebe

    private void AdminOptionsPage(){
        Intent startIntent = new Intent(getApplicationContext(), AdminOptions.class);
        startActivity(startIntent);
    }//goes to the Edit Account page - Adam

    private void EmergencyPage(){
        Intent startIntent = new Intent(getApplicationContext(), EmergencyPageActivity.class);
        startActivity(startIntent);
    }
}
