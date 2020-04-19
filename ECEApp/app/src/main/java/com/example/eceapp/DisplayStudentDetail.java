package com.example.eceapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eceapp.businessLayer.AddressBL;
import com.example.eceapp.businessLayer.ContactBL;
import com.example.eceapp.businessLayer.DaycareClassBL;
import com.example.eceapp.businessLayer.FamilyClassBL;
import com.example.eceapp.businessLayer.MedicalHistoryBL;
import com.example.eceapp.businessLayer.MedicationBL;
import com.example.eceapp.businessLayer.NonAuthorizedBL;
import com.example.eceapp.businessLayer.PractitionerBL;
import com.example.eceapp.businessLayer.StudentClassBL;

import java.util.ArrayList;

//Phoebe - did not use this page because adding and modifying student are sharing one page CreateStudentAcyivity
public class DisplayStudentDetail extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    ActionBar actionBar;
    Button btnAddStudent, btnBack, btnModifyStudent, btnSave, btnCancel;
    EditText fName, lName, dob, aptNo, strNo, street, city, postCode;
    Spinner prov, spPracProv;
    StudentClass stu = new StudentClass();
    AddressClass addr = new AddressClass();
    AddressClass pracAddress = new AddressClass();
    ArrayList provinces = addr.getProvinces();
    ListView lvContacts, lvNonAu;

    CreateStudentActivity createStudentActivity = new CreateStudentActivity();

    StudentClassBL studentClassBL = new StudentClassBL();
    AddressBL addressBL = new AddressBL();
    ContactBL contactBL = new ContactBL();
    NonAuthorizedBL nonAuthorizedBL = new NonAuthorizedBL();
    FamilyClassBL familyClassBL = new FamilyClassBL();
    MedicalHistory medicalHistory = new MedicalHistory();
    Medication medication = new Medication();
    MedicationBL medicationBL = new MedicationBL();
    MedicalHistoryBL medicalHistoryBL = new MedicalHistoryBL();
    Practitioner practitioner = new Practitioner();
    PractitionerBL practitionerBL = new PractitionerBL();

    RelativeLayout rlContacts, rlNonAuthorized;
    NonAuthorized nonAuthorized;
    ArrayList<NonAuthorized> nonAuthorizeds = new ArrayList<>();

    int medicareNo, isRoutineServices;
    String expiryDate, seriousAllergies, otherAllergies, isEmergencyTreatment;

    String pracFName, pracLName, pracPhone, pracAptNo, pracStrNo, pracStreet, pracCity, pracProv, pracPostalCode;

    int medical_history_id, proofId, medicationId, measles, mumps, meningitis, rubella, chicken_pox, pertussis, asthma,
            eczema, diabetes, epilepsy;
    String other, restricted_activities, dietary_restrictions;
    DaycareClass daycareClass = new DaycareClass();
    String medication1, condition1, dosage1, medication2, condition2, dosage2;

    int prev_attendance;
    String prev_attendance_long, prev_attendance_experience,
            allergy_management_plan, emergency_plan, routine_services_plan,
            help_dress, help_eat, help_toilet, help_wash, help_other, hints,
            like_to_do, extra_info;
    EditText et_medicareNo, et_expiryDate, et_seriousAllergies, et_otherAllergies;
    RadioButton rdoYesEmergencyTreatment, rdoNoEmergencyTreatment, rdoYesRoutineServices,
            rdoNoRoutineServices, rdoYesPrev_attendance, rdoNoPrev_attendance,
            rdo6months, rdo1year, rdo2years, rdoMorethan2years;
    EditText et_pracFName, et_pracLName, et_pracPhone, et_pracAptNo, et_pracStrNo, et_pracStreet, et_pracCity, et_pracPostalCode;
    CheckBox chk_measles, chk_mumps, chk_meningitis, chk_rubella, chk_chicken_pox, chk_pertussis, chk_asthma,
            chk_eczema, chk_diabetes, chk_epilepsy,
            chk6months, chk1year, chk2years, chkMorethan2years;
    EditText et_otherhealthStatusOther1, et_restricted_activities, et_dietary_restrictions,
            et_medication1, et_condition1, et_dosage1, et_medication2, et_condition2, et_dosage2,
            et_prev_attendance_experience, et_help_dress, et_help_eat, et_help_toilet, et_help_wash, et_help_other, et_hints,
            et_like_to_do, et_extra_info
    ;
    DaycareClassBL daycareClassBL = new DaycareClassBL();
    ArrayList<DaycareClass> daycareList = daycareClassBL.ListOfDaycares();

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_student_detail);

        findViewByIds();
        populateProvinces(prov);
        populateProvinces(spPracProv);

        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDefaultDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            //set in manifest parentActivity
        }

        //pull student detail
        Intent intent = getIntent();
        stu = intent.getParcelableExtra("Student");
        populateStudent();

        //Click on "Add new student" button
        btnAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoToCreateStudent();
            }
        });

        //Back to Student List
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoToStudentList();
            }
        });

        //Modify Student
        btnModifyStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //display modify screen
                ModifyScreen();
            }
        });

        btnSave.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ModifyStudent();
            }
        }));

        btnCancel.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                populateStudent();
                DisplayScreen();

            }
        }));


    }
    private void populateStudent(){
        actionBar.setTitle(stu.getFullname());

        //use method from CreateStudentActivity
        createStudentActivity.populateStudentInfo();//not include contact and authorized

        fName.setText(stu.getFname());
        lName.setText(stu.getLname());
        dob.setText(stu.getDob());
        aptNo.setText(stu.getAddressClass().getAptNo());
        strNo.setText(stu.getAddressClass().getStrNo());
        street.setText(stu.getAddressClass().getStreet());
        city.setText(stu.getAddressClass().getCity());
        postCode.setText(stu.getAddressClass().getPostCode());
        populateProvinces(prov);
        try{
            prov.setSelection(provinces.indexOf(stu.getAddressClass().getProv()));
        }catch(Exception ex) {prov.setSelection(0);}


    }
    private void GoToCreateStudent() {
        Intent intent = new Intent(this, CreateStudentActivity.class);
        startActivity(intent);
    }
    private void GoToStudentList() {
        Intent intent = new Intent(getApplicationContext(), StudentListView.class);
        startActivity(intent);
    }
    private void ModifyStudent() {
        Intent intent = getIntent();
        StudentClass stu = intent.getParcelableExtra("Student");
        //get new student info - use method from CreateStudentActivity
        createStudentActivity.getStudentInfo();//not include Contact and Authorized
        /*

        stu.setFname(fName.getText().toString().trim());
        stu.setLname(lName.getText().toString().trim());
        stu.setDob(dob.getText().toString().trim());
        stu.setAptNo(aptNo.getText().toString().trim());
        stu.setStrNo(strNo.getText().toString().trim());
        stu.setStreet(street.getText().toString().trim());
        stu.setCity(city.getText().toString().trim());
        stu.setProv(prov.getSelectedItem().toString());
        stu.setPostCode(postCode.getText().toString().trim());

*/
//call update student method
        int count = studentClassBL.updateStudent(stu);
        if (count > 0){
            Toast.makeText(DisplayStudentDetail.this, "Student is modified",Toast.LENGTH_SHORT).show();
            DisplayScreen();
        }
        else{
            Toast.makeText(DisplayStudentDetail.this, "There is an error, please try again",Toast.LENGTH_SHORT).show();
        }
    }
    private void findViewByIds(){
        btnAddStudent = findViewById(R.id.btnAddStu);
        btnBack = findViewById(R.id.btnBack);
        btnModifyStudent = findViewById(R.id.btnModifyStudent);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        //Edit Text
        fName = findViewById(R.id.txtStuFirstName);
        lName = findViewById(R.id.txtStuLastname);
        dob = findViewById(R.id.txtStuDOB);
        aptNo = findViewById(R.id.txtAptNo);
        strNo = findViewById(R.id.txtStrNo);
        street = findViewById(R.id.txtStreet);
        city = findViewById(R.id.txtCity);
        postCode = findViewById(R.id.txtPostCode);

        //ListView Contact
        lvContacts = findViewById(R.id.contactListView);
        lvNonAu = findViewById(R.id.nonAuthorizedListView);

        //Relative Layouts
        rlContacts = findViewById(R.id.rlContacts);
        rlNonAuthorized=findViewById(R.id.rlNonAuthorized);

        prov = findViewById(R.id.spProv);
        spPracProv = findViewById(R.id.spPracProv);


        et_medicareNo = findViewById(R.id.txtMedicareNo);
        et_expiryDate = findViewById(R.id.txtMedicareExpiryDate);
        et_seriousAllergies = findViewById(R.id.txtAllergyAlert);
        et_otherAllergies = findViewById(R.id.txtOtherAllergies);

        et_pracFName=findViewById(R.id.txtPracFName);
        et_pracLName=findViewById(R.id.txtPracLName);
        et_pracPhone=findViewById(R.id.txtPracPhone);
        et_pracAptNo=findViewById(R.id.txtPracAptNo);
        et_pracStrNo=findViewById(R.id.txtPracStrNo);
        et_pracStreet=findViewById(R.id.txtPracStreet);
        et_pracCity=findViewById(R.id.txtPracCity);
        et_pracPostalCode=findViewById(R.id.txtPracPostCode);

        et_otherhealthStatusOther1=findViewById(R.id.txthealthStatusOther1);
        et_restricted_activities=findViewById(R.id.txtrestrictedActivities);
        et_dietary_restrictions=findViewById(R.id.txtDietaryRestrictions);
        et_medication1=findViewById(R.id.txtmedication1);
        et_condition1=findViewById(R.id.txtCondition1);
        et_dosage1=findViewById(R.id.txtDosage1);
        et_medication2=findViewById(R.id.txtmedication2);
        et_condition2=findViewById(R.id.txtCondition2);
        et_dosage2=findViewById(R.id.txtDosage2);
        et_prev_attendance_experience=findViewById(R.id.txtChildExperience);
        et_help_dress=findViewById(R.id.txtDressingHelp);
        et_help_eat=findViewById(R.id.txtEatingHelp);
        et_help_toilet=findViewById(R.id.txtToiletingHelp);
        et_help_wash=findViewById(R.id.txtHandwashingToothbrushingHelp);
        et_help_other=findViewById(R.id.txtOtherHelp);
        et_hints=findViewById(R.id.txtHints);
        et_like_to_do=findViewById(R.id.txtLikeDo);
        et_extra_info=findViewById(R.id.txtExtraSharing);

        chk_measles=findViewById(R.id.chkMeasles);
        chk_mumps=findViewById(R.id.chkMumps);
        chk_meningitis=findViewById(R.id.chkMeningitis);
        chk_rubella=findViewById(R.id.chkRubella);
        chk_chicken_pox=findViewById(R.id.chkChickenPox);
        chk_pertussis=findViewById(R.id.chkPertussis);
        chk_asthma=findViewById(R.id.chkAsthma);
        chk_eczema=findViewById(R.id.chkEczemaPsoriasis);
        chk_diabetes=findViewById(R.id.chkDiabetes);
        chk_epilepsy=findViewById(R.id.chkEpilepsySeizures);

        rdo6months=findViewById(R.id.rdo_6months);
        rdo1year=findViewById(R.id.rdo_1year);
        rdo2years=findViewById(R.id.rdo_2years);
        rdoMorethan2years=findViewById(R.id.rdomorethan_2years);

        rdoYesEmergencyTreatment=findViewById(R.id.isEmergencyTreatment);
        rdoNoEmergencyTreatment=findViewById(R.id.isNotEmergencyTreatment);
        rdoYesRoutineServices=findViewById(R.id.isRoutineService);
        rdoNoRoutineServices=findViewById(R.id.isNotRoutineService);
        rdoYesPrev_attendance=findViewById(R.id.isPreAttendant);
        rdoNoPrev_attendance=findViewById(R.id.isNotPreAttendant);
    }
    private void ModifyScreen() {
        //display Save, Cancel Button
        btnSave.setVisibility(View.VISIBLE);
        btnCancel.setVisibility(View.VISIBLE);

        //hide Modify, Back, Add_New_Student button
        btnModifyStudent.setVisibility(View.INVISIBLE);
        btnBack.setVisibility(View.INVISIBLE);
        btnAddStudent.setVisibility(View.INVISIBLE);

        //enable editText
        fName.setEnabled(true);
        lName.setEnabled(true);
        dob.setEnabled(true);
        aptNo.setEnabled(true);
        strNo.setEnabled(true);
        street.setEnabled(true);
        city.setEnabled(true);
        prov.setEnabled(true);
        postCode.setEnabled(true);
    }
    private void DisplayScreen() {
        //hide Save, Cancel Button
        btnSave.setVisibility(View.INVISIBLE);
        btnCancel.setVisibility(View.INVISIBLE);

        //display Modify, Back, Add_New_Student button
        btnModifyStudent.setVisibility(View.VISIBLE);
        btnBack.setVisibility(View.VISIBLE);
        btnAddStudent.setVisibility(View.VISIBLE);

        //unenable editText
        fName.setEnabled(false);
        lName.setEnabled(false);
        dob.setEnabled(false);
        aptNo.setEnabled(false);
        strNo.setEnabled(false);
        street.setEnabled(false);
        city.setEnabled(false);
        prov.setEnabled(false);
        postCode.setEnabled(false);
    }

    public void populateProvinces(Spinner sp){
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, provinces);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter);
        sp.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
