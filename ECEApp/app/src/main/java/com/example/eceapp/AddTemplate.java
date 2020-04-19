package com.example.eceapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.example.eceapp.businessLayer.TemplateClassBL;
import com.example.eceapp.dataLayer.FileUtil;
import com.example.eceapp.dataLayer.connectS3Class;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;

public class AddTemplate extends AppCompatActivity {

    private File uploadFile;
    private TextView uploadTextView;
    private String fileName = "";
    private String filePath = "";
    private File tempDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_template);
        //Creating Buttons
        Button saveButton = (Button) findViewById(R.id.btnSaveTemp);
        ImageButton uploadButton = (ImageButton) findViewById(R.id.btnUpload);
        Button backButton = (Button) findViewById(R.id.btnBack);
        //End Buttons creation
        //Creating EditText and TextViews Field(s)
        final EditText tempDescriptionEditText = (EditText) findViewById(R.id.editTextTemplateDescription);
        uploadTextView = (TextView) findViewById(R.id.uploadTextView);
        //End EditText and TextViews Creation
        //Creating RadioButtons
        final RadioButton rdoTeacherAllowedYes = (RadioButton) findViewById(R.id.rdoTeacherAllowedYes);
        final RadioButton rdoTeacherAllowedNo = (RadioButton) findViewById(R.id.rdoTeacherAllowedNo);
        final RadioButton rdoStudentAllowedYes = (RadioButton) findViewById(R.id.rdoStudentAllowedYes);
        final RadioButton rdoStudentAllowedNo = (RadioButton) findViewById(R.id.rdoStudentAllowedNo);
        final RadioButton rdoActiveFormYes = (RadioButton) findViewById(R.id.rdoActiveFormYes);
        final RadioButton rdoActiveFormNo = (RadioButton) findViewById(R.id.rdoActiveFormNo);
        //End Radiobuttons creation
        //Back Button
        backButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //Back Button END

        //Upload button
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent fileChooser = new Intent(Intent.ACTION_GET_CONTENT);
                fileChooser.setType("application/pdf");
                startActivityForResult(Intent.createChooser(fileChooser, "Choose one pdf file"), 2);
            }
        });
        //Upload Button END
        //Save Button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean rdoTeacher, rdoStudent, rdoActive;
                String tempDesc, uploadedFileName;
                if (rdoTeacherAllowedYes.isChecked() || rdoTeacherAllowedNo.isChecked()) {//Teach Form RadioGroup is checked
                    rdoTeacher = rdoTeacherAllowedYes.isChecked();
                    if (rdoStudentAllowedYes.isChecked() || rdoStudentAllowedNo.isChecked()) {//Student Form RadioGroup is checked
                        rdoStudent = rdoStudentAllowedYes.isChecked();
                        if (rdoActiveFormYes.isChecked() || rdoActiveFormNo.isChecked()) {//Template Active RadioGroup is checked
                            rdoActive = rdoActiveFormYes.isChecked();
                            if (!tempDescriptionEditText.getText().toString().trim().equalsIgnoreCase("")) {//Template Description is not blank
                                tempDesc = tempDescriptionEditText.getText().toString().trim();
                                if (!fileName.trim().equalsIgnoreCase("")) {//File to be upload was chosen
                                    //Uploading file to S3 Bucket
                                    CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                                            getApplicationContext(),
                                            BuildConfig.COGNITO_ID, // Identity pool ID
                                            Regions.US_EAST_1 // Region
                                    );
                                    //
                                    connectS3Class s3client = new connectS3Class();
                                    String extension = "." + FilenameUtils.getExtension(uploadFile.getAbsolutePath());
                                    tempDesc += extension;
                                    s3client.uploadFile(credentialsProvider.getCredentials(), "Templates/" + tempDesc, uploadFile);
                                    //End Upload file to S3 Bucket process
                                    //Add info to DB
                                    TemplateClass template = new TemplateClass();
                                    template.setDescription(tempDesc);
                                    template.setStudent_allowed(rdoStudent);
                                    template.setTeacher_allowed(rdoTeacher);
                                    template.setActive(rdoActive);
                                    template.setLocation("https://" + BuildConfig.S3_BUCKETNAME + ".s3.amazonaws.com/" + "Templates/" + tempDesc);
                                    TemplateClassBL tempClassBL = new TemplateClassBL();
                                    Boolean flag = tempClassBL.saveNewTemplate(template);
                                    if (flag) {
                                        Toast.makeText(AddTemplate.this, "The template was successfully uploaded.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        s3client.deleteFile(credentialsProvider.getCredentials(), "Templates/" + template.getDescription());
                                        System.out.println(template.getDescription() + " was deleted from S3 Bucket due to error while writing info to DB.");
                                        Toast.makeText(AddTemplate.this, "Error uploading template. Please try again.", Toast.LENGTH_SHORT).show();

                                    }
                                    //End add info to DB
                                } else {
                                    Toast.makeText(AddTemplate.this, "Please select a file to be uploaded and try again", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(AddTemplate.this, "Please enter the new template description and try again", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(AddTemplate.this, "Please select active form option and try again", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(AddTemplate.this, "Please select student form option and try again", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AddTemplate.this, "Please select teach form option and try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //Save Button END
    }

    //After user select a file to be uploaded
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            // The result data contains a URI for the document or directory that
            // the user selected.
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                uploadFile = new File(uri.getPath());
                filePath = uploadFile.getAbsolutePath();
                System.out.println(filePath);
//                final String[] split = uploadFile.getPath().split(":");//split the path.
//                filePath = split[1];
                if (uri.getScheme().equals("content")) {
                    DocumentFile docFile = DocumentFile.fromSingleUri(this, uri);
                    if (docFile != null) {
                        fileName = docFile.getName();
                        System.out.println("Filename: " + fileName);
                        filePath = docFile.getUri().getPath();
                    }
                }
                uploadTextView.setText("File to be uploaded: " + fileName);
                System.out.println("FilePath: " + filePath);
                try {
                    uploadFile = FileUtil.from(this, uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("UPLOAD FILE: " + uploadFile);
            }
        }
    }
}
