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
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class EditForms extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_forms);
        final SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        Button btnNew = findViewById(R.id.btnNewForm);
        Button btnBack = findViewById(R.id.btnBackForm);
        Button btnSave = findViewById(R.id.btnSaveForm);
        final EditText formDescription = findViewById(R.id.editTextFormDescription);
        final RadioButton rdoTeacherYes = findViewById(R.id.rdoTeacherAllowedYes);
        final RadioButton rdoTeacherNo = findViewById(R.id.rdoTeacherAllowedNo);
        final RadioButton rdoStudentYes = findViewById(R.id.rdoStudentAllowedYes);
        final RadioButton rdoStudentNo = findViewById(R.id.rdoStudentAllowedNo);
        final RadioButton rdoActiveYes = findViewById(R.id.rdoActiveFormYes);
        final RadioButton rdoActiveNo = findViewById(R.id.rdoActiveFormNo);
        final TextView tvFormDescription = findViewById(R.id.textViewFormDisplay);
        final Spinner templateSpinner = findViewById(R.id.formSpinner);
        FormClass fc = new FormClass();
        ArrayList<FormClass> templates = fc.getAllFormTemplates();
        ArrayList<String> formNames = new ArrayList<String>();
        for(FormClass f: templates) {
            formNames.add(f.description);
        }
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, formNames);
        // Drop down layout style - list view
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        templateSpinner.setAdapter(dataAdapter);

        templateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String)templateSpinner.getSelectedItem();
                FormClass selectedForm = FindForm(selection);
                SharedPreferences.Editor editor = pref.edit();
                editor.putInt("template_id", selectedForm.id);
                editor.putString("description", selectedForm.description);
                editor.putBoolean("teacher_allowed", selectedForm.teacher_allowed);
                editor.putBoolean("student_allowed", selectedForm.student_allowed);
                editor.putString("location", selectedForm.location);
                editor.putBoolean("active", selectedForm.active);
                editor.apply();
                tvFormDescription.setText(selectedForm.description);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //do nothing
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FormClass updateForm = new FormClass(pref.getInt("template_id", 0), "", false,
                        false, pref.getString("location", ""), false);
                if(formDescription.getText().toString().trim().equals("")) {
                    updateForm.description = pref.getString("description", "");
                } else {
                    updateForm.description = formDescription.getText().toString().trim();
                }
                if(rdoTeacherYes.isChecked()){
                    updateForm.teacher_allowed = true;
                } else if (rdoTeacherNo.isChecked()) {
                    updateForm.teacher_allowed = false;
                } else {
                    updateForm.teacher_allowed = pref.getBoolean("teacher_allowed", false);
                }
                if(rdoStudentYes.isChecked()){
                    updateForm.student_allowed = true;
                } else if (rdoStudentNo.isChecked()) {
                    updateForm.student_allowed = false;
                } else {
                    updateForm.student_allowed = pref.getBoolean("student_allow", false);
                }
                if(rdoActiveYes.isChecked()){
                    updateForm.active = true;
                } else if (rdoActiveNo.isChecked()) {
                    updateForm.active = false;
                } else {
                    updateForm.active = pref.getBoolean("active", false);
                }
                UpdateSelectedForm(updateForm);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadAdminOptionsPage();
            }
        });
        btnNew.setOnClickListener(




                new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Recreating the intent before go to another screen
                Intent thisIntent = getIntent();
                finish();
                startActivity(thisIntent);
                //
                //Calling new activity
                Intent newIntent = new Intent(getApplicationContext(), AddTemplate.class);
                startActivity(newIntent);
                //
            }
        });
    } //end of onCreate method
    public void ShowNotification(String message) {
        Snackbar display = Snackbar.make(findViewById(R.id.textViewStudentAllowed), message, Snackbar.LENGTH_LONG);
        display.show();
    }//end of ShowNotification
    public FormClass FindForm(String description) {
        FormClass fc = new FormClass();
        ArrayList<FormClass> formList = fc.getAllFormTemplates();
        for(FormClass f : formList){
            if(f.description.equals(description)) {
                fc = f;
            }
        }
        return fc;
    }//end of FindForm method

    public void CreateNewForm(FormClass fc){
        FormClass formClass = new FormClass();
        int result = formClass.InsertForm(fc);
        if (result == 0) {
            ShowNotification("Form with that description alread exists");
        } else if (result == 1) {
            Snackbar snackbar = Snackbar.make(findViewById(R.id.textViewStudentAllowed), "Form updated successfully", Snackbar.LENGTH_LONG).
                    setAction("OKAY", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LoadEditFormsPage();
                        }
                    });
            snackbar.show();
        } else if (result == 2) {
            ShowNotification("Database Error");
        } else if (result == 3) {
            ShowNotification("Undefined Error");
        }
    }

    public void UpdateSelectedForm(FormClass fc) {
        FormClass formClass = new FormClass();
        int result = formClass.UpdateForm(fc);
        if (result == 0) {
            ShowNotification("Form could not be updated");
        } else if (result == 1) {
            Snackbar snackbar = Snackbar.make(findViewById(R.id.textViewStudentAllowed), "Form updated successfully", Snackbar.LENGTH_LONG).
                    setAction("OKAY", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LoadEditFormsPage();
                        }
                    });
            snackbar.show();
        } else if (result == 2) {
            ShowNotification("Database Error");
        } else if (result == 3) {
            ShowNotification("Undefined Error");
        }
    }
    public void LoadEditFormsPage(){
        Intent startIntent = new Intent(getApplicationContext(), EditForms.class);
        startActivity(startIntent);
    }

    public void LoadAdminOptionsPage(){
        Intent startIntent = new Intent(getApplicationContext(), AdminOptions.class);
        startActivity(startIntent);
    }
}
