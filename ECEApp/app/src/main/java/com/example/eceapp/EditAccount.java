package com.example.eceapp;


import androidx.appcompat.app.AppCompatActivity;
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
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;


public class EditAccount extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);
        TeacherClass t = new TeacherClass();
        final SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        String username = pref.getString("username", "");
        String password = pref.getString("password", "");
        int daycare = pref.getInt("daycare", -1);
        boolean admin = pref.getBoolean("admin", false);
        final TextView tvRole = findViewById(R.id.textViewDisplayRole);
        final RadioButton rdoTeacherSelect = findViewById(R.id.rdoTeacherRole);
        final RadioButton rdoAdminSelect = findViewById(R.id.rdoAdminSelect);
        //spinner which will contain accounts for the admin to edit (their site only)
        final Spinner accountSpinner = findViewById(R.id.accountSpinner);
        //"Save Changes" button - calls the UpdateTeacher method in ConnectionClass to save changes to teacher account
        Button btnSave = findViewById(R.id.buttonUpdate);
        final RadioButton rdoTeacherActive = findViewById(R.id.rdoTeacherActive);
        final RadioButton rdoTeacherInactive = findViewById(R.id.rdoTeacherInactive);
        final EditText editPassword = findViewById(R.id.editTextPassword);
        //pull all teachers from the database and put them in a list
        final ArrayList<TeacherClass> teachers = t.getAllTeachers();
        final ArrayList<String> editList = new ArrayList<String>();
        for (TeacherClass teach : teachers) {
            if (teach.daycareID == daycare) {
                editList.add(teach.username + " - " + teach.firstName + " " + teach.lastName);
            }
        }//end of for loop

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, editList);
        // Drop down layout style - list view
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        accountSpinner.setAdapter(dataAdapter);
        accountSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //take the selected option from the spinner, split the username, first and last names
                //into separate strings and turn each piece into a variable
                String spinnerChoice = (String) accountSpinner.getSelectedItem();
                String[] spinnerArray = spinnerChoice.split(" ");
                String selectedUsername = spinnerArray[0];
                TeacherClass selectedTeacher = FindTeacher(selectedUsername);
                //textview to display the name of the user being edited
                TextView textView = findViewById(R.id.textViewUser);
                textView.setText("Account information for " + selectedTeacher.firstName + " " + selectedTeacher.lastName);
                TextView tvUsername = findViewById(R.id.textViewDisplayUsername);
                tvUsername.setText(selectedUsername);
                tvRole.setText(AdminStatus(selectedTeacher.isAdmin));
                SharedPreferences.Editor editor = pref.edit();
                editor.putInt("teacher_id", selectedTeacher.teacherID);
                editor.putInt("daycare_id", selectedTeacher.daycareID);
                editor.putInt("address_id", selectedTeacher.addressID);
                editor.putString("first_name", selectedTeacher.firstName);
                editor.putString("last_name", selectedTeacher.lastName);
                editor.putString("username", selectedTeacher.username);
                editor.putString("password", selectedTeacher.password);
                editor.putBoolean("admin", selectedTeacher.isAdmin);
                editor.putBoolean("active", selectedTeacher.isActive);
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //do nothing
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TeacherClass selectedTeacher = new TeacherClass(pref.getInt("teacher_id", -1), pref.getInt("address_id", -1),
                        pref.getString("first_name", ""), pref.getString("last_name", ""), pref.getString("username", ""),
                        pref.getString("password",""), pref.getInt("daycare_id", -1), pref.getBoolean("admin", false), pref.getBoolean("active", false));

                if (rdoAdminSelect.isChecked()) {
                    selectedTeacher.setAdmin(true);
                } else if (rdoTeacherSelect.isChecked()) {
                    selectedTeacher.setAdmin(false);
                }
                if(rdoTeacherActive.isChecked()){
                    selectedTeacher.setActive(true);
                } else if(rdoTeacherInactive.isChecked()){
                    selectedTeacher.setActive(false);
                }
                String updatePassword = "";
                //boolean updateAdmin = pref.getBoolean("editRole", false);
                if (!editPassword.getText().toString().trim().equals("")) {
                    //hash the password
                    PasswordHash hash = new PasswordHash();
                    updatePassword = editPassword.getText().toString().trim();
                    updatePassword = hash.generatedPassword(updatePassword);
                } else {
                    //updatePassword = pref.getString("editPassword", "");
                    updatePassword = selectedTeacher.password;
                }
                UpdateTeacher(selectedTeacher.teacherID, updatePassword, selectedTeacher.isAdmin, selectedTeacher.isActive);
            }
        });
        //back button to return the user to the home page
        Button btnBack = findViewById(R.id.buttonEditBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(getApplicationContext(), AdminOptions.class);
                startActivity(startIntent);
            }
        });
    }//end of onCreate

    public void ShowNotification(String message) {
        Snackbar display = Snackbar.make(findViewById(R.id.textView13), message, Snackbar.LENGTH_LONG);
        display.show();
    }

    public TeacherClass FindTeacher(String username){
        TeacherClass tc = new TeacherClass();
        ArrayList<TeacherClass> teacherList = tc.getAllTeachers();
        for(TeacherClass t : teacherList) {
            if(t.username.equals(username)) {
                tc = t;
            }
        }
        return tc;
    }

    public void UpdateTeacher(int id, String password, boolean isAdmin, boolean isActive) {
        TeacherClass tc = new TeacherClass();
        int result = tc.UpdateTeacher(id, password, isAdmin, isActive);
        if (result == 0) {
            ShowNotification("User could not be updated");
        } else if (result == 1) {
            Snackbar snackbar = Snackbar.make(findViewById(R.id.textView13), "Account updated successfully", Snackbar.LENGTH_LONG).
                    setAction("OKAY", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent CreateAccount = new Intent(getApplicationContext(), EditAccount.class);
                            startActivity(CreateAccount);
                        }
                    });
            snackbar.show();
        } else if (result == 2) {
            ShowNotification("Database Error");
        } else if (result == 3) {
            ShowNotification("Undefined Error");
        }
    }

    public String AdminStatus(Boolean isAdmin) {
        if (isAdmin) {
            return "Admin";
        } else {
            return "Teacher";
        }
    }
}