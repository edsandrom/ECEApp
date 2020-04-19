package com.example.eceapp;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.example.eceapp.businessLayer.DaycareClassBL;
import com.example.eceapp.businessLayer.FormClassBL;
import com.example.eceapp.businessLayer.StudentClassBL;
import com.example.eceapp.businessLayer.TemplateClassBL;
import com.example.eceapp.dataLayer.connectS3Class;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FormSelectionActivity extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<String> pdfList = new ArrayList<>(); //List of pictures that will be merged into a pdf file.
    private ImageButton uploadButton;
    private int formPage = 1;
    //    ViewFlipper imageFlipper;
    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Forms"); //Directory
    private TextView daycareNameTxt, parent1NameTxt, parent2NameTxt, fullNameStudentTxt, formDescriptionTxt, uploadText;
    private Button submitButton, resetButton;
    private String fileName;
    private String timeStamp;
    private int template_id = -1; //Template ID
    private int user_id = -1; //Student ID
    private Spinner studentDDL;
    private Spinner formsDDL;
    private int clickedNo = -1; //Flag to check if the user has taken pictures and decided to take more (it will delete all previous pictures)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_selection);
        Button backButton = (Button) findViewById(R.id.btnBack);
        submitButton = (Button) findViewById(R.id.btnSubmit);
        submitButton.setEnabled(false);
        uploadButton = (ImageButton) findViewById(R.id.btnUpload);
        uploadButton.setEnabled(false);
        resetButton = (Button) findViewById(R.id.btnReset);
        resetButton.setVisibility(View.INVISIBLE);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            uploadButton.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }

        //TextViews
        daycareNameTxt = (TextView) findViewById((R.id.txtViewDaycareName));
        parent1NameTxt = (TextView) findViewById((R.id.txtViewParentName1));
        parent2NameTxt = (TextView) findViewById((R.id.txtViewParentName2));
        fullNameStudentTxt = (TextView) findViewById(R.id.studentNametxt);
        formDescriptionTxt = (TextView) findViewById((R.id.formDescTxt));
        uploadText = (TextView) findViewById(R.id.txtViewAttachment);

        //Student and Forms Spinners - Drop down lists
        studentDDL = (Spinner) findViewById(R.id.spinnerStudents);
        formsDDL = (Spinner) findViewById(R.id.spinnerForms);
        populateStudentsInfo(studentDDL);
        populateTemplatesDDL(formsDDL);

        //Student drop down list on select action
        studentDDL.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String stringNameAndId = studentDDL.getSelectedItem().toString().trim();

                //Displaying the Student Name on Screen
                String stringName = stringNameAndId.substring(stringNameAndId.indexOf(".") + 1);
                fullNameStudentTxt.setText(stringName.trim());

                if (fullNameStudentTxt.getText() == "") {
                    daycareNameTxt.setText("");
                } else {
                    //Getting the student_id
                    String stringId = stringNameAndId.substring(0, stringNameAndId.indexOf("."));
                    System.out.println(stringId);
                    user_id = Integer.parseInt(stringId);

                    //Displaying daycare info on screen
                    DaycareClass daycare;
                    DaycareClassBL daycareClassBL = new DaycareClassBL();
                    StudentClass student = new StudentClass();
                    StudentClassBL studentClassBL = new StudentClassBL();
                    student = studentClassBL.getStudentById(user_id);
                    daycare = daycareClassBL.getDaycareById(student.getDaycareId());
                    daycareNameTxt.setText(daycare.getName());

                    //Displaying parents info on screen
                    ArrayList<String> parentsList = studentClassBL.getParentsByStudentId(user_id);
                    if (parentsList.size() > 2) {
                        System.out.println("More than 2 parents/guardians");
                    } else if (parentsList.size() > 1) {
                        parent1NameTxt.setText(parentsList.get(0));
                        parent2NameTxt.setText(parentsList.get(1));
                    } else if (parentsList.size() == 1) {
                        parent1NameTxt.setText(parentsList.get(0));
                        parent2NameTxt.setText("N/A");
                    } else {
                        System.out.println("This student has no parent.");
                    }
                }
                //Checking if both student and forms were selected
                if (!formsDDL.getSelectedItem().toString().trim().equals("") && !studentDDL.getSelectedItem().toString().trim().equals("")) {
                    uploadButton.setEnabled(true);
                    resetButton.setVisibility(View.VISIBLE);
                } else {
                    uploadButton.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                daycareNameTxt.setText("");
            }
        });

        //Form drop down list on select action
        formsDDL.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String formNameAndId = formsDDL.getSelectedItem().toString().trim();

                //Displaying text Desc
                String formName = formNameAndId.substring(formNameAndId.indexOf(".") + 1);
                formDescriptionTxt.setText(formName.trim());
                if (formDescriptionTxt.getText() != "") {
                    //Getting the template_id
                    String formId = formNameAndId.substring(0, formNameAndId.indexOf("."));
                    template_id = Integer.parseInt(formId);
                }
                //Checking if both student and forms were selected
                if (!formsDDL.getSelectedItem().toString().trim().equals("") && !studentDDL.getSelectedItem().toString().trim().equals("")) {
                    uploadButton.setEnabled(true);
                    resetButton.setVisibility(View.VISIBLE);
                } else {
                    uploadButton.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        uploadButton.setOnClickListener(this);

        //Submit Button BEGIN
        submitButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!formsDDL.getSelectedItem().toString().trim().equals("") && !studentDDL.getSelectedItem().toString().trim().equals("")) {
                    createForm();

                    //Deleting all jpg files
                    deleteJPGFiles();
                    //Initializing the Amazon Cognito credentials provider
                    CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                            getApplicationContext(),
                            "us-east-1:ba61a866-f8ff-48cd-960b-d14b1bbc95d3", // Identity pool ID
                            Regions.US_EAST_1 // Region
                    );
                    //
                    connectS3Class s3client = new connectS3Class();
                    File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Forms/" + fileName);
                    s3client.uploadFile(credentialsProvider.getCredentials(), "StudentForms/" + fileName, file);
                    try {
                        file.delete();
                        System.out.println("file deleted from android storage: " + fileName);
                    } catch (Exception ex) {
                        System.out.println("Error on deleting pdf file.");
                    }

                    if (FormClassBL.saveForm(template_id, "STU", user_id, timeStamp, fileName)) {
                        Toast.makeText(FormSelectionActivity.this, "Your file was successfully uploaded on our server.", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(getIntent());
                    } else {
                        Toast.makeText(FormSelectionActivity.this, "Your file wasn't uploaded on our server. Please try again.", Toast.LENGTH_SHORT).show();
                        System.out.println("Deleting the file: " + fileName);
                        s3client.deleteFile(credentialsProvider.getCredentials(), fileName); //Delete file from S3 since we get an error writing the filename into DB
                    }

                } else {
                    Toast.makeText(FormSelectionActivity.this, "Please select a student, a form template, and upload a picture in order to submit the form.", Toast.LENGTH_LONG).show();
                }
            }

        });


        //Submit Button END

        //Back Button Begin
        backButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //Back Button END

        //Reset Button Begin
        resetButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                finish();
                startActivity(getIntent());
            }
        });
        //Back Button END
    }


    public void onClick(View v) { //Upload Button
        if (clickedNo >= 0) {
            deleteJPGFiles();
        }
        Uri file;
        Intent takePictureIntent;
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //Intent responsible for taking picture
        file = Uri.fromFile(saveNewFile(mediaStorageDir));                       //Picture's Uri
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, file);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this); //Dialog Box that will ensure that all form's pages will be photographed
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) { //Returns the first activity component that can handle the intent (prevents app of crashing)
            startActivityForResult(takePictureIntent, 100);
        }
        //Calling the Dialog Box
        alertBuilder.setTitle("More Pages?").setCancelable(false).setMessage("More Pages?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }


    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE: //YES
                    if (formPage - 1 > 0 && !formsDDL.getSelectedItem().toString().trim().equals("") && !studentDDL.getSelectedItem().toString().trim().equals("")) {
                        submitButton.setEnabled(true);
                    } else {
                        submitButton.setEnabled(false);
                    }
                    uploadButton.callOnClick(); //Calling the onClick Method
                    break;
                case DialogInterface.BUTTON_NEGATIVE: //NO
                    //No button clicked
                    if (formPage - 1 > 0 && !formsDDL.getSelectedItem().toString().trim().equals("") && !studentDDL.getSelectedItem().toString().trim().equals("")) {
                        submitButton.setEnabled(true);
                    } else {
                        submitButton.setEnabled(false);
                        Toast.makeText(FormSelectionActivity.this, "Please select a student, a form template, and upload a picture in order to submit the form.", Toast.LENGTH_LONG).show();
                    }
                    uploadText.setText("You have " + (formPage - 1) + " page(s) to be uploaded.");
                    clickedNo++;
                    break;
            }
        }
    };

    //Checking if the software is allowed to work properly
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                uploadButton.setEnabled(true);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            if (new File(mediaStorageDir.getPath() + File.separator + "FORM" + formPage + ".jpg").exists()) {
                pdfList.add("FORM" + formPage + ".jpg"); //Adding the picture name to pdfList
                formPage++; //Incrementing the number of the page
            }
        }
    }

    //Method responsible for saving the pictures inside "FORM" folder
    private File saveNewFile(File dir) {

        ArrayList<Bitmap> imagesList = new ArrayList<>();

        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                System.out.println("Cannot create directory");
                Log.d("Forms", "failed to create directory");
                return null;
            }
        }
        File newFile = new File(dir.getPath() + File.separator +
                "FORM" + formPage + ".jpg");

        Bitmap newFileBitmap = BitmapFactory.decodeFile(newFile.getAbsolutePath());
        imagesList.add(newFileBitmap);

        return newFile;
    }

    private void createForm() {
        //Letter size
        int pageWidth = 611;
        int pageHeight = 791;

        //Creating pdf document
        PdfDocument document = new PdfDocument();

        //Loop that will generate the pdf pages
        for (String s : pdfList) {

            //Creating page properties
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create();

            //Starting the page
            PdfDocument.Page page = document.startPage(pageInfo);

            //Writing pages
            File image = new File(mediaStorageDir, (s));
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            final Bitmap tmp = BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions);
            int width = tmp.getWidth();
            int height = tmp.getHeight();
            float scaleWidth = ((float) pageWidth) / width;
            float scaleHeight = ((float) pageHeight) / height;
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeight); // resize the bit map
            Canvas canvas = page.getCanvas();
            canvas.setMatrix(matrix);
            canvas.drawBitmap(tmp, 0, 0, new Paint(Paint.FILTER_BITMAP_FLAG));
            document.finishPage(page);
        }
        //Saving the pdf
        try {
            if (template_id != -1 && user_id != -1) {
                timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
                fileName = "Form_" + template_id + "_STU_" + user_id + "_" + timeStamp + ".pdf";
            }
            File outputFile = new File(mediaStorageDir, fileName);

            if (outputFile.exists()) {
                outputFile.delete();
            }
            FileOutputStream outputStream = new FileOutputStream(outputFile);
            document.writeTo(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Closing the pdf document
        document.close();
    }

    private void populateStudentsInfo(Spinner studentDDL) {

        String[] studentArray;
        StudentClassBL studentClassBL = new StudentClassBL();
        ArrayList<StudentClass> studentList = studentClassBL.getStudents();
        studentArray = new String[studentList.size() + 1];
        studentArray[0] = "";
        for (int i = 0; i < studentList.size(); i++) {
            studentArray[i + 1] = studentList.get(i).getStudentId() + ". " + studentList.get(i).getFullname();
        }
        //Creating the ArrayAdapter instance having the students list
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, studentArray);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        studentDDL.setAdapter(aa);
    }

    private void populateTemplatesDDL(Spinner formsDDL) {
        String[] formsArray;
        TemplateClassBL templateClassBL = new TemplateClassBL();
        ArrayList<TemplateClass> templateList = templateClassBL.getActiveStudentsTemplates();
        formsArray = new String[templateList.size() + 1];
        formsArray[0] = "";
        for (int i = 0; i < templateList.size(); i++) {
            formsArray[i + 1] = templateList.get(i).getTemplateId() + ". " + templateList.get(i).getDescription();
        }
        //Creating the ArrayAdapter instance having the forms list
        ArrayAdapter bb = new ArrayAdapter(this, android.R.layout.simple_spinner_item, formsArray);
        bb.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        formsDDL.setAdapter(bb);
    }

    private void deleteJPGFiles() {
        //Deleting all jpg files
        for (int i = 1; i <= formPage; i++) {
            File delFile = new File(mediaStorageDir, "FORM" + (i - 1) + ".jpg");
            System.out.println("DELETE " + delFile.toString());
            try {
                delFile.delete();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("File Inexistent");
            }
            uploadText.setText("Take Form Pictures");
            formPage = 1; //Resetting formPages to the initial state
        }
    }
}
