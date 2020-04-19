package com.example.eceapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ActionMenuView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.example.eceapp.businessLayer.DaycareClassBL;
import com.example.eceapp.businessLayer.FormClassBL;
import com.example.eceapp.businessLayer.StudentClassBL;
import com.example.eceapp.businessLayer.TeacherClassBL;
import com.example.eceapp.businessLayer.TemplateClassBL;
import com.example.eceapp.dataLayer.connectS3Class;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.github.barteksc.pdfviewer.util.ArrayUtils;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FormAndTemplateSettings extends AppCompatActivity {

    private int userType = 1; //Setting user type to Both
    private int dataType = 0; //Setting data type to Template
    CheckBox allFormsChkBox;
    CheckBox inactiveFormsAndTemplateschkbox;
    private Boolean allFormsBool;
    private Boolean inactiveFormsAndTemplatesBool;
    private String fileNameDB;
    private String role = "", firstname = "", lastname = "", templateDesc = "", formDate = "", activeStatus = "";
    private int templateId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_and_template_settings);
        //Getting the session teacher info
        final TextView formOrTemplateNameLabel = findViewById(R.id.formOrTemplateNameTxtVw);
        final Button openFileButton = findViewById(R.id.btnOpenFile);
        openFileButton.setEnabled(false);
        //
        //Automatically check "Teacher" radio button as user type on create activity
        final RadioButton rdoTeacherRole = (RadioButton) findViewById(R.id.rdoTeacherRole);
        final RadioButton rdoStudentRole = (RadioButton) findViewById(R.id.rdoStudentRole);
        rdoStudentRole.setChecked(false);
        rdoTeacherRole.setChecked(true);
        //
        //Automatically check "template" radio button as data type on create activity
        final RadioButton rdoTemplateSelect = (RadioButton) findViewById(R.id.rdoTemplateSelect);
        final RadioButton rdoFormSelect = (RadioButton) findViewById(R.id.rdoFormSelect);
        rdoTemplateSelect.setChecked(true);
        rdoFormSelect.setChecked(false);
        //

        //All forms checkbox
        allFormsChkBox = (CheckBox) findViewById(R.id.everyoneChkBox);
        allFormsChkBox.setChecked(false);
        allFormsChkBox.setEnabled(false);
        allFormsBool = allFormsChkBox.isChecked();
        //
        //Inactive Templates Checkbox
        inactiveFormsAndTemplateschkbox = (CheckBox) findViewById(R.id.InactiveItems);
        inactiveFormsAndTemplateschkbox.setChecked(false);
        inactiveFormsAndTemplateschkbox.setEnabled(true);
        inactiveFormsAndTemplatesBool = inactiveFormsAndTemplateschkbox.isChecked();
        //
        //Populating Student/Teacher Spinner On Create
        final Spinner userSpinner = (Spinner) findViewById(R.id.spinnerUser);
        populateUserSpinner(userSpinner, userType, dataType, allFormsBool);
        //
        //Populating Forms/Template Spinner On Create
        final Spinner formsAndTemplateSpinner = (Spinner) findViewById(R.id.spinnerTemplates);
        populateFormsAndTemplateSpinner(userSpinner, formsAndTemplateSpinner, userType, dataType, inactiveFormsAndTemplatesBool);
        //
        //Writing Form/Template on Textview after selection
        formsAndTemplateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String labelText = formsAndTemplateSpinner.getSelectedItem().toString().trim();
                formOrTemplateNameLabel.setText(labelText);
                if (!labelText.equalsIgnoreCase("")) {
                    openFileButton.setEnabled(true);
                    if (labelText.substring(0, labelText.indexOf(" ")).equalsIgnoreCase("Student")) { //Student Form
                        int[] blankIndexesOldArray = new int[labelText.toCharArray().length];

                        int index = 0;
                        for (int k = 0; k < labelText.toCharArray().length; k++) {
                            if (Character.isWhitespace(labelText.charAt(k)))
                                blankIndexesOldArray[index] = k;
                            index++;
                        }
                        int targetIndex = 0;
                        int sourceIndex = 0;
                        for (sourceIndex = 0; sourceIndex < blankIndexesOldArray.length; sourceIndex++) {
                            if (blankIndexesOldArray[sourceIndex] != 0)
                                blankIndexesOldArray[targetIndex++] = blankIndexesOldArray[sourceIndex];
                        }
                        int[] blankIndexesArray = new int[targetIndex];
                        System.arraycopy(blankIndexesOldArray, 0, blankIndexesArray, 0, targetIndex);

                        int limit = blankIndexesArray.length + 1;
                        String[] results = new String[limit];
                        for (int j = 0; j < limit; j++) {
                            if (j == 0) {
                                results[j] = labelText.substring(0, blankIndexesArray[0]);
                            } else if (j < limit - 1) {
                                results[j] = labelText.substring(blankIndexesArray[j - 1], blankIndexesArray[j]);
                            } else {
                                results[j] = labelText.substring(blankIndexesArray[j - 1] + 1, labelText.toCharArray().length);
                            }
                        }

                        role = results[0].trim();
                        firstname = results[1].trim();
                        lastname = results[2].trim();
                        templateDesc = results[4].trim();
                        formDate = results[6].trim();
                        activeStatus = results[8].trim();

                    } else if (labelText.substring(0, labelText.indexOf(" ")).equalsIgnoreCase("Teacher")) { //Teacher Form
                        int[] blankIndexesOldArray = new int[labelText.toCharArray().length];
                        int index = 0;
                        for (int k = 0; k < labelText.toCharArray().length; k++) {
                            if (Character.isWhitespace(labelText.charAt(k)))
                                blankIndexesOldArray[index] = k;
                            index++;
                        }
                        int targetIndex = 0;
                        int sourceIndex = 0;
                        for (sourceIndex = 0; sourceIndex < blankIndexesOldArray.length; sourceIndex++) {
                            if (blankIndexesOldArray[sourceIndex] != 0)
                                blankIndexesOldArray[targetIndex++] = blankIndexesOldArray[sourceIndex];
                        }
                        int[] blankIndexesArray = new int[targetIndex];
                        System.arraycopy(blankIndexesOldArray, 0, blankIndexesArray, 0, targetIndex);
                        int limit = blankIndexesArray.length + 1;
                        String[] results = new String[limit];
                        for (int j = 0; j < limit; j++) {
                            if (j == 0) {
                                results[j] = labelText.substring(0, blankIndexesArray[0]);
                            } else if (j < limit - 1) {
                                results[j] = labelText.substring(blankIndexesArray[j - 1], blankIndexesArray[j]);
                            } else {
                                results[j] = labelText.substring(blankIndexesArray[j - 1] + 1, labelText.toCharArray().length);
                            }
                        }

                        role = results[0].trim();
                        firstname = results[1].trim();
                        lastname = results[2].trim();
                        templateDesc = results[4].trim();
                        formDate = results[6].trim();
                        activeStatus = results[8].trim();

                    } else { //Template
                        String stringNameAndId = formsAndTemplateSpinner.getSelectedItem().toString().trim();
                        String stringId = stringNameAndId.substring(0, stringNameAndId.indexOf("."));
                        templateId = Integer.parseInt(stringId);
                        TemplateClassBL templateClassBL = new TemplateClassBL();
                        TemplateClass template = templateClassBL.getTemplateById(templateId);
                        templateDesc = template.getDescription();
                        if (template.isActive()) activeStatus = "Active";
                        else activeStatus = "Inactive";
                    }
                } else {
                    openFileButton.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //
        //Populating Spinners On Radio Button Checked Change
        rdoStudentRole.setOnCheckedChangeListener(new RadioButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (rdoStudentRole.isChecked()) {
                    userType = 0;
                    allFormsChkBox.setChecked(false);
                } else if (rdoTeacherRole.isChecked()) {
                    userType = 1;
                    allFormsChkBox.setChecked(false);
                }
                if (rdoFormSelect.isChecked()) dataType = 1;
                else dataType = 0;
                populateUserSpinner(userSpinner, userType, dataType, allFormsBool);
                populateFormsAndTemplateSpinner(userSpinner, formsAndTemplateSpinner, userType, dataType, inactiveFormsAndTemplatesBool);
            }
        });
        rdoTeacherRole.setOnCheckedChangeListener(new RadioButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (rdoStudentRole.isChecked()) {
                    userType = 0;
                    allFormsChkBox.setChecked(false);
                } else if (rdoTeacherRole.isChecked()) {
                    userType = 1;
                    allFormsChkBox.setChecked(false);
                }
                if (rdoFormSelect.isChecked()) dataType = 1;
                else dataType = 0;
                populateUserSpinner(userSpinner, userType, dataType, allFormsBool);
                populateFormsAndTemplateSpinner(userSpinner, formsAndTemplateSpinner, userType, dataType, inactiveFormsAndTemplatesBool);
            }
        });
        rdoTemplateSelect.setOnCheckedChangeListener(new RadioButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                role = "";
                if (rdoStudentRole.isChecked()) {
                    userType = 0;
                    allFormsChkBox.setChecked(false);
                } else if (rdoTeacherRole.isChecked()) {
                    userType = 1;
                    allFormsChkBox.setChecked(false);
                }
                if (rdoFormSelect.isChecked()) dataType = 1;
                else dataType = 0;
                populateUserSpinner(userSpinner, userType, dataType, allFormsBool);
                populateFormsAndTemplateSpinner(userSpinner, formsAndTemplateSpinner, userType, dataType, inactiveFormsAndTemplatesBool);
            }
        });
        rdoFormSelect.setOnCheckedChangeListener(new RadioButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (rdoStudentRole.isChecked()) {
                    userType = 0;
                    allFormsChkBox.setChecked(false);
                } else if (rdoTeacherRole.isChecked()) {
                    userType = 1;
                    allFormsChkBox.setChecked(false);
                }
                if (rdoFormSelect.isChecked()) dataType = 1;
                else dataType = 0;
                populateUserSpinner(userSpinner, userType, dataType, allFormsBool);
                populateFormsAndTemplateSpinner(userSpinner, formsAndTemplateSpinner, userType, dataType, inactiveFormsAndTemplatesBool);
            }
        });
        //If All Forms checkbox is checked/unchecked
        allFormsChkBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                allFormsBool = allFormsChkBox.isChecked();
                populateUserSpinner(userSpinner, userType, dataType, allFormsBool);
                populateFormsAndTemplateSpinner(userSpinner, formsAndTemplateSpinner, userType, dataType, inactiveFormsAndTemplatesBool);
            }
        });
        //If inactive checkbox is checked/unchecked
        inactiveFormsAndTemplateschkbox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                inactiveFormsAndTemplatesBool = inactiveFormsAndTemplateschkbox.isChecked();
                populateUserSpinner(userSpinner, userType, dataType, allFormsBool);
                populateFormsAndTemplateSpinner(userSpinner, formsAndTemplateSpinner, userType, dataType, inactiveFormsAndTemplatesBool);
            }
        });
        //

        //Add Teacher Form Button
        Button addTeacherFormButton = (Button) findViewById(R.id.btnAddTeacherForm);
        addTeacherFormButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Recreating the intent before go to another screen
                Intent thisIntent = getIntent();
                finish();
                startActivity(thisIntent);
                //
                //Calling new activity
                Intent newIntent = new Intent(getApplicationContext(), FormTeacherActivity.class);
                startActivity(newIntent);

            }
        });
        //
        //Add Student Form Button
        Button addStuFormButton = (Button) findViewById(R.id.btnAddStuForm);
        addStuFormButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Recreating the intent before go to another screen
                Intent thisIntent = getIntent();
                finish();
                startActivity(thisIntent);
                //
                //Calling new activity
                Intent newIntent = new Intent(getApplicationContext(), FormSelectionActivity.class);
                startActivity(newIntent);
                //
            }
        });
        //
        //Back Button
        Button backButton = (Button) findViewById(R.id.btnBack);
        backButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //Back Button END

        //Open File button
        openFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Initializing the Amazon Cognito credentials provider
                CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                        getApplicationContext(),
                        BuildConfig.COGNITO_ID, // Identity pool ID
                        Regions.fromName(BuildConfig.COGNITO_REGION)// Region
                );
                //
                connectS3Class s3client = new connectS3Class();
                File downloadedFile;
                File mediaStorageDir;
                String dir;
                String description;
                if (!role.equalsIgnoreCase("")) {
                    if (role.equalsIgnoreCase("Student")) { //Student Role Selected
                        TemplateClass temp = new TemplateClass();
                        TemplateClassBL templateClassBL = new TemplateClassBL();
                        templateDesc = templateDesc.replaceAll("_", " ");
                        temp = templateClassBL.getTemplateByDescription(templateDesc);
                        int tempId = temp.getTemplateId();
                        StudentClass student = new StudentClass();
                        StudentClassBL studentClassBL = new StudentClassBL();
                        student = studentClassBL.getStudentByName(firstname, lastname);
                        int studentId = student.getStudentId();
                        if (formDate.toCharArray()[formDate.length() - 2] == '.') {
                            formDate = formDate.substring(0, formDate.length() - 2);
                        }
                        formDate = formDate.replace(":", "-");
                        String extension = ".pdf";
                        Boolean isActive;
                        if (activeStatus.equalsIgnoreCase("Active")) isActive = true;
                        else isActive = false;
                        FormClass form = new FormClass();
                        FormClassBL formClassBL = new FormClassBL();
                        String filename = "Form_" + tempId + "_STU_" + studentId + "_" + formDate + extension;
                        form = formClassBL.getStudentFormByNameAndStatus(filename, isActive);

                        s3client.downloadFile(credentialsProvider.getCredentials(), "StudentForms/" + form.getFilename(), "StudentForms/" + form.getFilename());
                        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                        StrictMode.setVmPolicy(builder.build());
                        mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "StudentForms"); //Directory
                        downloadedFile = new File(mediaStorageDir, form.getFilename());
                        description = form.getFilename();
                        dir = "StudentForms";

                    } else { //Teacher Role selected
                        TemplateClass temp = new TemplateClass();
                        TemplateClassBL templateClassBL = new TemplateClassBL();
                        System.out.println("BEFORE : " + templateDesc);
                        templateDesc = templateDesc.replaceAll("_", " ");
                        System.out.println("AFTER : " + templateDesc);
                        temp = templateClassBL.getTemplateByDescription(templateDesc);
                        int tempId = temp.getTemplateId();
                        TeacherClass teacher = new TeacherClass();
                        TeacherClassBL teacherClassBL = new TeacherClassBL();
                        teacher = teacherClassBL.getTeacherByName(firstname, lastname);
                        int teacherId = teacher.getTeacherID();
                        if (formDate.toCharArray()[formDate.length() - 2] == '.') {
                            formDate = formDate.substring(0, formDate.length() - 2);
                        }
                        formDate = formDate.replace(":", "-");
                        String extension = ".pdf";
                        Boolean isActive;
                        System.out.println("ACTIVE STATUS " + activeStatus);
                        if (activeStatus.equalsIgnoreCase("Active")) isActive = true;
                        else isActive = false;
                        System.out.println(isActive);
                        FormClass form = new FormClass();
                        FormClassBL formClassBL = new FormClassBL();
                        String filename = "Form_" + tempId + "_TEACH_" + teacherId + "_" + formDate + extension;
                        System.out.println("FILE NAME: " + filename);
                        System.out.println(isActive);
                        form = formClassBL.getTeacherFormByNameAndStatus(filename, isActive);
                        System.out.println("FILE NAME: " + form.getFilename());
                        s3client.downloadFile(credentialsProvider.getCredentials(), "TeacherForms/" + form.getFilename(), "TeacherForms/" + form.getFilename());
                        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                        StrictMode.setVmPolicy(builder.build());
                        mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "TeacherForms"); //Directory
                        downloadedFile = new File(mediaStorageDir, form.getFilename());
                        description = form.getFilename();
                        dir = "TeacherForms";
                    }
                } else {//Template Selected
                    TemplateClass temp = new TemplateClass();
                    TemplateClassBL templateClassBL = new TemplateClassBL();
                    templateDesc = templateDesc.replaceAll("_", " ");
                    temp = templateClassBL.getTemplateByDescription(templateDesc);
                    String extension = ".pdf";
                    s3client.downloadFile(credentialsProvider.getCredentials(), "Templates/" + temp.getDescription() + extension, "Template/" + temp.getDescription() + extension);
                    System.out.println();
                    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                    StrictMode.setVmPolicy(builder.build());
                    mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Template"); //Directory
                    downloadedFile = new File(mediaStorageDir, temp.getDescription());
                    description = temp.getDescription() + extension;
                    dir = "Template";
                }
                System.out.println("BEFORE FILE: " + downloadedFile.toString());
                Intent i = new Intent(getBaseContext(), viewDownloadedPdf.class);
                String downloadedFileString = downloadedFile.toString();
                //Set the Data to pass
                i.putExtra("dir", dir);
                i.putExtra("desc", description);
                startActivity(i);
            }
        });
    }

    private void populateFormsAndTemplateSpinner(Spinner userSpinner, Spinner formsAndTemplateSpinner, int userType, int dataType, Boolean inactiveFormsAndTemplatesBool) {
        String selectedUser;
        if (dataType != 0) {
            selectedUser = userSpinner.getSelectedItem().toString().trim();
        } else {
            selectedUser = " ";
        }
        FormClassBL formClassBL = new FormClassBL();
        TemplateClassBL templateClassBL = new TemplateClassBL();
        StudentClassBL studentClassBL = new StudentClassBL();
        TeacherClassBL teacherClassBL = new TeacherClassBL();
        List<String> resultArrayList = new ArrayList();
        if (selectedUser.equalsIgnoreCase("Students")) { //All student Forms
            ArrayList<FormClass> resultList = new ArrayList();
            if (inactiveFormsAndTemplatesBool == false) { //Don't show inactive forms
                resultList = formClassBL.getActiveStudentForms();
            } else { //Show inactive forms
                resultList = formClassBL.getAllStudentForms();
            }
            String[] resultArray = new String[resultList.size()];
            for (int i = 0; i < resultList.size(); i++) {
                FormClass form = formClassBL.getStudentFormById(resultList.get(i).getId()); //Getting Form Info
                TemplateClass template = templateClassBL.getTemplateById(form.getTemplate_id()); // Getting Template Info
                StudentClass student = studentClassBL.getStudentById(form.getStudent_id());
                String templateDesc = template.getDescription();
                String formDate = form.getDate_create();
                Boolean formActive = form.getActive();
                String activeStatus;
                if (formActive) activeStatus = "Active";
                else activeStatus = "Inactive";

                String studentFullName = student.getFname() + " " + student.getLname();
                resultArray[i] = "Student " + studentFullName + " - " + templateDesc.replaceAll(" ", "_") + " - " + formDate.replaceAll(" ", "_") + " - " + activeStatus.replaceAll(" ", "_");
//                resultArray[i] = resultList.get(i).getId() + ". " + resultList.get(i).getFilename();
            }
            resultArrayList = Arrays.asList(resultArray);
        } else if (selectedUser.equalsIgnoreCase("Teachers")) { //All Teachers Forms
            ArrayList<FormClass> resultList = new ArrayList();
            if (!inactiveFormsAndTemplatesBool) { //Don't show inactive forms
                resultList = formClassBL.getActiveTeacherForms();
            } else { //Show inactive forms
                resultList = formClassBL.getAllTeacherForms();
            }
            if (resultList != null) {
                String[] resultArray = new String[resultList.size()];
                for (int i = 0; i < resultList.size(); i++) {
                    FormClass form = formClassBL.getTeacherFormById(resultList.get(i).getId()); //Getting Form Info
                    TemplateClass template = templateClassBL.getTemplateById(form.getTemplate_id()); // Getting Template Info
                    TeacherClass teacher = teacherClassBL.getTeacherById(form.getTeacher_id());
                    String templateDesc = template.getDescription();
                    String formDate = form.getDate_create();
                    Boolean formActive = form.getActive();
                    String activeStatus;
                    if (formActive) activeStatus = "Active";
                    else activeStatus = "Inactive";
                    String teacherFullName = teacher.getFirstName() + " " + teacher.getLastName();
                    resultArray[i] = "Teacher " + teacherFullName + " - " + templateDesc.replaceAll(" ", "_") + " - " + formDate.replaceAll(" ", "_") + " - " + activeStatus.replaceAll(" ", "_");
                }
                resultArrayList = Arrays.asList(resultArray);
            } else {

                resultArrayList = Arrays.asList(" ");
            }
        } else if (selectedUser.equalsIgnoreCase("All Forms")) { //All Students + Teachers Forms
            ArrayList<FormClass> resultList = new ArrayList();

            if (!inactiveFormsAndTemplatesBool) { //Don't show inactive forms
                ArrayList studentList = formClassBL.getActiveStudentForms();
                ArrayList teacherList = formClassBL.getActiveTeacherForms();
                if (studentList != null) {
                    resultList.addAll(studentList);
                }
                if (teacherList != null) {
                    resultList.addAll(teacherList);
                }
            } else { //Show inactive forms
                ArrayList studentList = formClassBL.getAllStudentForms();
                ArrayList teacherList = formClassBL.getAllTeacherForms();
                if (studentList != null) {
                    resultList.addAll(studentList);
                }
                if (teacherList != null) {
                    resultList.addAll(teacherList);
                }
            }
            if (resultList != null) {
                String[] resultArray = new String[resultList.size()];
                for (int i = 0; i < resultList.size(); i++) {
                    FormClass form = formClassBL.getTeacherFormById(resultList.get(i).getId()); //Getting Form Info (Teacher)
                    FormClass form2 = formClassBL.getStudentFormById(resultList.get(i).getId()); //Getting Form Info (Student)
                    if (form2 != null && form2.getStudent_id() > 0) {
                        TemplateClass template = templateClassBL.getTemplateById(form2.getTemplate_id()); // Getting Template Info
                        StudentClass student = studentClassBL.getStudentById(form2.getStudent_id());
                        String templateDesc = template.getDescription();
                        String formDate = form2.getDate_create();
                        Boolean formActive = form2.getActive();
                        String activeStatus;
                        if (formActive) activeStatus = "Active";
                        else activeStatus = "Inactive";
                        String studentFullName = student.getFname() + " " + student.getLname();
                        resultArray[i] = "Student " + studentFullName + " - " + templateDesc.replaceAll(" ", "_") + " - " + formDate.replaceAll(" ", "_") + " - " + activeStatus.replaceAll(" ", "_");
                    } else {
                        TemplateClass template = templateClassBL.getTemplateById(form.getTemplate_id()); // Getting Template Info
                        TeacherClass teacher = teacherClassBL.getTeacherById(form.getTeacher_id());
                        String templateDesc = template.getDescription();
                        String formDate = form.getDate_create();
                        Boolean formActive = form.getActive();
                        String activeStatus;
                        if (formActive) activeStatus = "Active";
                        else activeStatus = "Inactive";
                        String teacherFullName = teacher.getFirstName() + " " + teacher.getLastName();
                        resultArray[i] = "Teacher " + teacherFullName + " - " + templateDesc.replaceAll(" ", "_") + " - " + formDate.replaceAll(" ", "_") + " - " + activeStatus.replaceAll(" ", "_");
                    }
//                resultArray[i] = resultList.get(i).getId() + ". " + resultList.get(i).getFilename();
                }
                resultArrayList = Arrays.asList(resultArray);
            } else {
                resultArrayList = Arrays.asList(" ");
            }
        } else if (selectedUser.equalsIgnoreCase(" ")) { //Condition where TEMPLATE data type is selected
            ArrayList<TemplateClass> resultList = new ArrayList();
            if (resultList != null) {
                if (userType == 0) {    //Student
                    if (inactiveFormsAndTemplatesBool == false) { //Don't show inactive templates
                        resultList = templateClassBL.getActiveStudentsTemplates();
                    } else { //Show inactive templates
                        resultList = templateClassBL.getAllStudentsTemplates();
                    }
                } else {    // User type == 1 -> Teacher
                    if (inactiveFormsAndTemplatesBool == false) { //Don't show inactive templates
                        resultList = templateClassBL.getActiveTeachersTemplates();
                    } else { //Show inactive templates
                        resultList = templateClassBL.getAllTeachersTemplates();
                    }
                }
                String[] resultArray = new String[resultList.size()];
                for (int i = 0; i < resultList.size(); i++) {
                    resultArray[i] = resultList.get(i).getTemplateId() + ". " + resultList.get(i).getDescription().replaceAll(" ", "_");
                }
                resultArrayList = Arrays.asList(resultArray);
            } else {

                resultArrayList = Arrays.asList(" ");
            }
        } else { //Forms Condition where a specific user was selected
            String userString = "";
            ArrayList<FormClass> resultList = new ArrayList();
            if (resultList != null) {
                if (userType == 0) {    //Student
                    userString = selectedUser.substring(0, selectedUser.indexOf("_"));
                    int user_id = Integer.parseInt(userString);
                    if (inactiveFormsAndTemplatesBool == false) { //Don't show inactive forms
                        resultList = formClassBL.getActiveFormsByStudentId(user_id);
                    } else { //Show inactive forms
                        resultList = formClassBL.getAllFormsByStudentId(user_id);
                    }
                    String[] resultArray = new String[resultList.size()];
                    for (int i = 0; i < resultList.size(); i++) {
                        resultArray[i] = resultList.get(i).getId() + ". " + resultList.get(i).getFilename().replaceAll(" ", "_");
                    }
                    resultArrayList = Arrays.asList(resultArray);
                } else {    // User type == 1 -> Teacher
                    userString = selectedUser.substring(0, selectedUser.indexOf("_"));
                    int user_id = Integer.parseInt(userString);
                    if (inactiveFormsAndTemplatesBool == false) { //Don't show inactive forms
                        resultList = formClassBL.getActiveFormsByTeacherId(user_id);
                    } else { //Show inactive forms
                        resultList = formClassBL.getAllFormsByTeacherId(user_id);
                    }
                    if (resultList != null) {
                        String[] resultArray = new String[resultList.size()];
                        for (int i = 0; i < resultList.size(); i++) {
                            resultArray[i] = resultList.get(i).getId() + ". " + resultList.get(i).getFilename().replaceAll(" ", "_");
                        }
                        resultArrayList = Arrays.asList(resultArray);
                    }
                }
            } else {

                resultArrayList = Arrays.asList(" ");
            }
        }
        Object[] resultArray = resultArrayList.toArray();
        if (resultArray.length > 0) {
            ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, resultArray);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //Setting the ArrayAdapter data on the Spinner
            formsAndTemplateSpinner.setAdapter(aa);
        }
    }

    private void populateUserSpinner(Spinner userSpinner, int userType, int dataType, Boolean
            allFormsBool) {//User type 0 = Student / User type 1 = Teacher /  >-< Data type Template = 0 / Data Type Form != 0

        if (dataType == 0) { //Specific Student/Teacher Selection is not allowed - The user wants to retrieve templates
            String[] blankArray = new String[1];
            blankArray[0] = " ";
            ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, blankArray);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //Setting the ArrayAdapter data on the Spinner
            userSpinner.setAdapter(aa);
            userSpinner.setEnabled(false);
            //
            allFormsChkBox.setChecked(false);
            allFormsChkBox.setEnabled(false);
        } else {
            if (allFormsBool) {
                allFormsChkBox.setChecked(true);
                String[] blankArray = new String[1];
                blankArray[0] = "All Forms";
                ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, blankArray);
                aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //Setting the ArrayAdapter data on the Spinner
                userSpinner.setAdapter(aa);
                userSpinner.setEnabled(false);
            } else {
                userSpinner.setEnabled(true);
                allFormsChkBox.setChecked(false);
                allFormsChkBox.setEnabled(true);
                //Student Array
                String[] studentArray;
                StudentClassBL studentClassBL = new StudentClassBL();
                ArrayList<StudentClass> studentList = studentClassBL.getStudents();
                studentArray = new String[studentList.size() + 1];
                studentArray[0] = "Students";
                for (int i = 0; i < studentList.size(); i++) {
                    studentArray[i + 1] = studentList.get(i).getStudentId() + ". " + studentList.get(i).getFullname();
                }
                //Teacher Array
                String[] teachersArray;
                TeacherClassBL teacherClassBL = new TeacherClassBL();
                ArrayList<TeacherClass> teacherList = teacherClassBL.getTeachers();
                teachersArray = new String[teacherList.size() + 1];
                teachersArray[0] = "Teachers";
                for (int i = 0; i < teacherList.size(); i++) {
                    teachersArray[i + 1] = teacherList.get(i).getTeacherID() + ". " + teacherList.get(i).getFirstName() + " " + teacherList.get(i).getLastName();
                }
                if (userType == 0) { //Student

                    //Creating the ArrayAdapter instance having the students list
                    ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, studentArray);
                    aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    //Setting the ArrayAdapter data on the Spinner
                    userSpinner.setAdapter(aa);

                } else if (userType == 1) { //Teacher
                    //Creating the ArrayAdapter instance having the students list
                    ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, teachersArray);
                    aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    //Setting the ArrayAdapter data on the Spinner
                    userSpinner.setAdapter(aa);
                } else { //Both
                    //Concatenate Student and Teacher Array
                    int newArrayLenght = studentArray.length + teachersArray.length;
                    String[] bothArray = new String[newArrayLenght];
                    int i = 0;
                    for (String s : studentArray) {
                        bothArray[i] = s;
                        i++;
                    }
                    for (String t : teachersArray) {
                        bothArray[i] = t;
                        i++;
                    }
                    ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, bothArray);
                    aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    //Setting the ArrayAdapter data on the Spinner
                    userSpinner.setAdapter(aa);
                }
            }
        }
    }
}
