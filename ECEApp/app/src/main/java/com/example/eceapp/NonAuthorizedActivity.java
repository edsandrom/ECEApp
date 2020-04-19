package com.example.eceapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class NonAuthorizedActivity extends AppCompatActivity {
    private EditText etFirstName, etLastName;
    private String firstName, lastName;
    private Button btnAddNonAu, btnCancel, btnModifyNonAu;
    ArrayList<NonAuthorized> nonAuthorizeds = new ArrayList<>();
    NonAuthorized nonAuthorized ;
    StudentClass student = new StudentClass();
    boolean ready=true;
    int index=-1;
    TextView nonAuTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_non_authorized);

        nonAuTitle = findViewById(R.id.tvNonAuTitle);

        btnCancel = findViewById(R.id.btnNonAuthorizedCancel);
        btnAddNonAu = findViewById(R.id.btnAddNonAuthorized);
        btnModifyNonAu= findViewById(R.id.btnModifyNonAu);

        //Edit Text
        etFirstName = findViewById(R.id.txtNonAuthorizedFirstName);
        etLastName = findViewById(R.id.txtNonAuthorizedLastname);


        //Get Student object from Create Student Activity
        Intent intent = getIntent();
        student=intent.getParcelableExtra("student");

        nonAuthorized = intent.getParcelableExtra("nonAthorized");
        index = intent.getIntExtra("index",0);

        if (student !=null){
            nonAuthorizeds = student.getNonAuthorizeds();
            if(nonAuthorized!=null){//Modify nonAuthorized mode ==> nonAuthorizeds is not null
                populateAuthorized();
                btnModifyNonAu.setVisibility(View.VISIBLE);
                btnAddNonAu.setVisibility(View.GONE);
                nonAuTitle.setText("MODIFY NON-AUTHORIZED PICKUP");
                btnModifyNonAu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(Validations()){
                            nonAuthorizeds.set(index, nonAuthorized);
                            student.setNonAuthorizeds(nonAuthorizeds);
                            GotoCreateStudent();
                        }
                    }
                });
            }
        }

        btnAddNonAu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nonAuthorized == null){
                    nonAuthorized = new NonAuthorized();
                }
                if(Validations()){
                    if(nonAuthorizeds ==null){
                        nonAuthorizeds = new ArrayList<>();
                    }
                    nonAuthorizeds.add(nonAuthorized);
                    if(student==null){
                        student = new StudentClass();
                    }
                    student.setNonAuthorizeds(nonAuthorizeds);
                    GotoCreateStudent();
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GotoCreateStudent();
            }
        });
    }
    public void GotoCreateStudent(){
        Intent intent = new Intent(NonAuthorizedActivity.this, CreateStudentActivity.class);
        if(student!=null){
            intent.putExtra("student",student);
        }
        startActivity(intent);
    }
    private void populateAuthorized(){
        etFirstName.setText(nonAuthorized.getFirstName());
        etLastName.setText(nonAuthorized.getLastName());
    }
    private boolean Validations(){
        lastName = etLastName.getText().toString().trim();
        firstName = etFirstName.getText().toString().trim();
        if(firstName.isEmpty()) {
            ShowNotification("Please enter first name");
            return false;
        }
        else if(lastName.isEmpty()){
             ShowNotification("Please enter last name");
            return false;
        }
        else {
            nonAuthorized.setFirstName(firstName);
            nonAuthorized.setLastName(lastName);
            return true;
        }

    }
    private void ShowNotification(String message) {
        Snackbar display = Snackbar.make(findViewById(R.id.txtNonAuthorizedLastname), message, Snackbar.LENGTH_LONG);
        display.show();
    }//end of ShowNotification
}
