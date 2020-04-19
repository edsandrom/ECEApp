package com.example.eceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdminOptions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_options);
        Button btnCreateAccount = findViewById(R.id.buttonCreateAccount);
        Button btnEditAccounts = findViewById(R.id.buttonEditAccount);
        Button btnEditForms = findViewById(R.id.buttonEditForms);
        Button btnBack = findViewById(R.id.buttonBack);

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(getApplicationContext(), CreateAccountActivity.class);
                startActivity(startIntent);
            }
        });

        btnEditAccounts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(getApplicationContext(), EditAccount.class);
                startActivity(startIntent);
            }
        });

        btnEditForms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(getApplicationContext(), EditForms.class);
                startActivity(startIntent);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(getApplicationContext(), HomePageActivity.class);
                startActivity(startIntent);
            }
        });
    }
}
