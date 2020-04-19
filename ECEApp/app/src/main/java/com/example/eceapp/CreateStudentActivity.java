package com.example.eceapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateStudentActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    //Phoebe feb 5, 2020 - Modified Feb 18, Mar 12
    RadioGroup rgHowlong;
    TextView tvTitle;
    ScrollView scrollview;
    EditText fName, lName, dob, aptNo, strNo, street, city, postCode;
    Spinner prov, spDaycare, spPracProv;
    Button btnAddStudent, btnAddContact, btnAddNonAu, btnCancel, btnModifyStudent, btnSaveAdding, btnSaveModifying, btnCancelModify;
    AddressClass addr = new AddressClass();
    AddressClass pracAddress = new AddressClass();
    StudentClass student = new StudentClass();
    ArrayList<ContactClass> contacts = new ArrayList<>();
    String txtFName, txtLName, txtDob, txtStreetNo, txtaptNo, txtstreet, txtcity, txtprov, txtpostCode;
    ArrayList provinces = addr.getProvinces();
    ListView lvContacts, lvNonAu;
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
    int[] addressID_isExist = {0,0};
    int[] studentID_isExist = {0,0};
    int[] contactID_isExist = {0,0};
    int[] familyID_isExist = {0,0};
    int[] nonAuthID_isExist ={-1,0};
    int[] nonAuthIDRel_isExist = {-1,0};
    int[] pracId_isExist ={0,0};

    RelativeLayout rlContacts, rlNonAuthorized;
    NonAuthorized nonAuthorized;
    ArrayList<NonAuthorized> nonAuthorizeds = new ArrayList<>();

    String medicareNo, expiryDate, seriousAllergies, otherAllergies, isEmergencyTreatment;
    int medical_history_id, medicationId;

    DaycareClass daycareClass = new DaycareClass();
    EditText et_medicareNo, et_expiryDate, et_seriousAllergies, et_otherAllergies;
    RadioButton rdoYesEmergencyTreatment, rdoNoEmergencyTreatment, rdoYesRoutineServices,
            rdoNoRoutineServices, rdoYesPrev_attendance, rdoNoPrev_attendance,
            rdo6months, rdo1year, rdo2years, rdoMorethan2years;
    EditText et_pracFName, et_pracLName, et_pracPhone, et_pracAptNo, et_pracStrNo, et_pracStreet, et_pracCity, et_pracPostalCode;
    CheckBox chk_measles, chk_mumps, chk_meningitis, chk_rubella, chk_chicken_pox, chk_pertussis, chk_asthma,
            chk_eczema, chk_diabetes, chk_epilepsy    ;
    EditText et_otherhealthStatusOther1, et_restricted_activities, et_dietary_restrictions,
            et_medication1, et_condition1, et_dosage1, et_medication2, et_condition2, et_dosage2,
            et_prev_attendance_experience, et_help_dress, et_help_eat, et_help_toilet, et_help_wash, et_help_other, et_hints,
            et_like_to_do, et_extra_info
    ;
    DaycareClassBL daycareClassBL = new DaycareClassBL();
    ArrayList<DaycareClass> daycareList = daycareClassBL.ListOfDaycares();
    String mode="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_student);
        findViewByIds();
        //populate provinces
        populateProvinces(prov);
        populateProvinces(spPracProv);
        //populate daycare
        populateDayCare(spDaycare);

        //Get Student from ContactActivity
        Intent intent = getIntent();
        student = intent.getParcelableExtra("student");
        mode=intent.getStringExtra("from");
        if(student !=null){
            //if student is not null, populate student from Contact, Student List, and Non-Authorized
            populateStudentInfo();
            populateContacts();
            populateNonAuthorizeds();
            //System.out.println(contacts);
            if(mode!=null && mode.equalsIgnoreCase("StudentListView")){//Modify mode ==> student is not null
                DisplayMode();
            }
            else if(student.getStudentId()>0){
                ModifyMode();
            }
            else{
                AddMode();
            }
        }
        btnModifyStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModifyMode();
            }
        });

        //Button Add Contact - Pass current info to Contact and then turn it back
        btnAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GotoContact();
            }
        });
        btnAddNonAu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GotoNonAuthorized();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GotoStudentList();
            }
        });
        btnSaveModifying.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getStudentInfo();
                if (Validation()) {

                    int result = 0;
                    int newStuAddessId = 0, newPractitionerAddressId =0, newPractitionerId=0, newMedicationId =0,
                            newMedicalHistoryId=0;
                    newStuAddessId = addressBL.addAddress(student.getAddressClass())[0];
                    if(newStuAddessId>0) student.setAddressId(newStuAddessId);

                    practitioner = student.getPractitioner();
                    newPractitionerAddressId=addressBL.addAddress(practitioner.getAddressClass())[0];
                    if(newPractitionerAddressId>0) practitioner.setAddressId(newPractitionerAddressId);

                    newPractitionerId = practitionerBL.addPractitioner(practitioner)[0];
                   // System.out.println(practitioner.getfName());
                    //System.out.println(newPractitionerId);
                    if(newPractitionerId>0) student.setPractitionerId(newPractitionerId);

                    medicalHistory=student.getMedicalHistory();
                    newMedicationId = medicationBL.addMedication(medicalHistory.getMedication());

                    if(newMedicationId >0 ) medicalHistory.setMedicationId(newMedicationId);

                    newMedicalHistoryId = medicalHistoryBL.addMedicalHistory(medicalHistory);
                    if(newMedicalHistoryId>0) student.setMedicalHistoryId(newMedicalHistoryId);
                    contacts = student.getContacts();
                    if (contacts != null) {
                        for (ContactClass c : contacts) {
                            if (c.getContactId() > 0) {//Modify Contact, keep Contact Id, Address Id and Family ID

                                if(c.getIsSameAddress()==0){
                                   c.setAddressId( addressBL.addAddress(c.getAddress())[0]);
                                }
                                else{
                                    c.setAddressId(student.getAddressId());
                                }
                                result += contactBL.editContact(c);
                                result+=familyClassBL.editFamilyRealtionship(c.getFamilyRel());
                            } else {//add new contact
                                if (c.isSameAddress == 1) {
                                    c.setAddressId(student.getAddressId());
                                } else {
                                    c.setAddressId(addressBL.addAddress(c.getAddress())[0]);
                                }
                                int id = 0; //contact id
                                id = contactBL.addContact(c)[0];
                                if (id > 0) {
                                    c.setContactId(id);
                                    FamilyRelationshipClass f = c.getFamilyRel();
                                    f.setContactId(id);
                                    f.setStuId(student.getStudentId());
                                    familyClassBL.addFamilyRealtionship(f);
                                }
                            }
                        }
                    }
                    if (student.getNonAuthorizeds() != null) {
                        for (NonAuthorized n : student.getNonAuthorizeds()) {
                            if (n.getNonAuId() > 0)
                                result += nonAuthorizedBL.editNonAuthorized(n);
                            else {
                                int id = 0;
                                id = nonAuthorizedBL.addNonAuthorized(n)[0];
                                if (id > 0) {
                                    n.setNonAuId(id);
                                    n.setStuId(student.getStudentId());
                                    nonAuthorizedBL.addNonAuthorizedRealtionship(n);
                                }
                            }
                        }
                    }
                    result += studentClassBL.updateStudent(student);
                    if (result > 0) {
                        Toast.makeText(CreateStudentActivity.this, "Student information is saved",Toast.LENGTH_LONG).show();
                    }
                    GotoStudentList();
                }
            }
        });
        btnCancelModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                populateStudentInfo();
                populateContacts();
                populateNonAuthorizeds();
                DisplayMode();
            }
        });
        //Adding new Student
        btnSaveAdding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            getStudentInfo();
            if(Validation()){
            //add address, student, contact, and Family Relationship to DB
                if(student != null){
                    //add Student Address
                    addressID_isExist = addressBL.addAddress(student.getAddressClass());

                    if(addressID_isExist[0]==0){
                        showAlertDialog(CreateStudentActivity.this, "Error", "Please enter student address information");
                    }
                    else{
                        //add student
                        student.setAddressId(addressID_isExist[0]);//set address ID to student obj
                        practitioner = student.getPractitioner();
                        int newPractitionerAddressId=addressBL.addAddress(practitioner.getAddressClass())[0];
                        if(newPractitionerAddressId>0) practitioner.setAddressId(newPractitionerAddressId);

                        int newPractitionerId = practitionerBL.addPractitioner(practitioner)[0];

                        if(newPractitionerId>0) student.setPractitionerId(newPractitionerId);
                        //add Practitioner address
                        addressID_isExist = addressBL.addAddress(practitioner.getAddressClass());
                        if(addressID_isExist[0]!=0) practitioner.setAddressId(addressID_isExist[0]);
                        //add Practitioner
                        pracId_isExist = practitionerBL.addPractitioner(practitioner);
                        if(pracId_isExist[0]!=0) student.setPractitionerId(pracId_isExist[0]);

                        //Add medication
                        medicationId = medicationBL.addMedication(student.getMedicalHistory().getMedication());
                        if(medicationId!=0) medicalHistory.setMedicationId(medicationId);

                        //add medical history
                        medical_history_id = medicalHistoryBL.addMedicalHistory(student.getMedicalHistory());
                        if(medical_history_id!=0) student.setMedicalHistoryId(medical_history_id);

                        studentID_isExist = studentClassBL.addStudentInfo(student);

                        if(studentID_isExist[0]==0){
                            showAlertDialog(CreateStudentActivity.this, "Error", "Please enter student information");
                        }
                        else {
                            student.setStudentId(studentID_isExist[0]);
                            //add Non-Authorized Pickup
                            if(nonAuthorizeds!=null){
                                for(NonAuthorized n: nonAuthorizeds){
                                    //add non_authorized to DB, get Id to add non_authorized - student relationship
                                    //reset non-Authorized pickup result
                                    nonAuthID_isExist[0] = -1;
                                    nonAuthID_isExist[1] = 0;

                                    nonAuthID_isExist = nonAuthorizedBL.addNonAuthorized(n);
                                    if(nonAuthID_isExist[0]<0){
                                        showAlertDialog(CreateStudentActivity.this, "Error", "Please enter Non_authorized pickup person " );
                                    }
                                    else{
                                        n.setNonAuId(nonAuthID_isExist[0]);
                                        n.setStuId(student.getStudentId());

                                        //add non-authorized relationship
                                        //reset non-authorized relationship result
                                        nonAuthIDRel_isExist[0]=-1;
                                        nonAuthIDRel_isExist[1]=0;
                                        nonAuthIDRel_isExist = nonAuthorizedBL.addNonAuthorizedRealtionship(n);
                                        if(nonAuthIDRel_isExist[0]<0){
                                            showAlertDialog(CreateStudentActivity.this, "Error", "Please enter Non_authorized pickup person information" );
                                        }
                                        if(nonAuthIDRel_isExist[1]==1){
                                            showAlertDialog(CreateStudentActivity.this, "Saved", "This Non_authorized pickup information is linked to student");
                                        }
                                    }

                                }
                            }
                            //add Contact
                            contacts=student.getContacts();
                            if (contacts != null) {
                                for (ContactClass c : contacts) {
                                    //add Contact Address and set to contact obj
                                    if (c.isSameAddress == 1) {
                                        c.setAddressId(student.getAddressId());
                                    } else {
                                        //reset addressID_Exist
                                        addressID_isExist[0] = 0;
                                        addressID_isExist[1] = 0;

                                        //add Contact Address to DB
                                        addressID_isExist = addressBL.addAddress(c.getAddress());
                                        c.setAddressId(addressID_isExist[0]);
                                    }
                                    if (c.getAddressId() == 0) {
                                        showAlertDialog(CreateStudentActivity.this, "Error", "Please enter address information of " + c.getFirstName() + " " + c.getLastName());
                                    }
                                    else{
                                        //reset contactID_Exist
                                        contactID_isExist[0] = 0;
                                        contactID_isExist[1] = 1;

                                        //add Contact to DB and set ID
                                        contactID_isExist = contactBL.addContact(c);
                                        c.setContactId(contactID_isExist[0]);
                                        if(c.getContactId()==0){
                                            showAlertDialog(CreateStudentActivity.this, "Error", "Please enter information of " + c.getFirstName() + " " + c.getLastName());
                                        }
                                        else{
                                            //reset family relationship
                                            familyID_isExist[0]=0;
                                            familyID_isExist[1]=0;

                                            //add Family relationship
                                            FamilyRelationshipClass fam = c.getFamilyRel();
                                            fam.setStuId(student.getStudentId());
                                            fam.setContactId(c.getContactId());

                                            familyID_isExist=familyClassBL.addFamilyRealtionship(fam);
                                            fam.setFamilyRelId(familyID_isExist[0]);
                                            if(familyID_isExist[0]==0){
                                                showAlertDialog(CreateStudentActivity.this, "Error", "Please enter information of " + c.getFirstName() + " " + c.getLastName());
                                            }
                                        }
                                    }
                                }
                            }
                            if(studentID_isExist[1]==1){
                                Toast.makeText(CreateStudentActivity.this, "Student information is saved",Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(CreateStudentActivity.this, "Student is added",Toast.LENGTH_LONG).show();
                            }
                            GotoStudentList();
                        }
                    }
                }
                else{
                    showAlertDialog(CreateStudentActivity.this, "Error", "Please enter student information");
                }
            }
            }
        });
    }
    public void populateProvinces(Spinner sp){
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, provinces);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter);
        sp.setOnItemSelectedListener(this);
    }
    public void populateDayCare(Spinner sp){
        ArrayAdapter<DaycareClass> adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, daycareList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter);
        sp.setOnItemSelectedListener(this);
    }
    public void getStudentInfo(){
        //set student info
        if(student == null){
            student = new StudentClass();
        }
        //get student info
        txtFName = fName.getText().toString().trim();
        txtLName = lName.getText().toString().trim();
        txtDob = dob.getText().toString().trim();

        txtaptNo = aptNo.getText().toString().trim();
        txtStreetNo = strNo.getText().toString().trim();
        txtstreet = street.getText().toString().trim();
        txtcity = city.getText().toString().trim();
        txtprov = prov.getSelectedItem().toString();
        txtpostCode = postCode.getText().toString().trim().toUpperCase();

        medicareNo = et_medicareNo.getText().toString().trim();

        expiryDate=et_expiryDate.getText().toString().trim();
        seriousAllergies=et_seriousAllergies.getText().toString().trim();
        otherAllergies=et_otherAllergies.getText().toString().trim();

        //practitioner
        practitioner = student.getPractitioner();
        if(practitioner==null) practitioner=new Practitioner();
        practitioner.setfName(et_pracFName.getText().toString().trim());
        practitioner.setlName(et_pracLName.getText().toString().trim());
        practitioner.setPhone(et_pracPhone.getText().toString().trim());

        pracAddress = practitioner.getAddressClass();
        if(pracAddress==null) pracAddress = new AddressClass();
        pracAddress.setAptNo(et_pracAptNo.getText().toString().trim());
        pracAddress.setStrNo(et_pracStrNo.getText().toString().trim());
        pracAddress.setStreet(et_pracStreet.getText().toString().trim());
        pracAddress.setCity(et_pracCity.getText().toString().trim());
        pracAddress.setProv(spPracProv.getSelectedItem().toString());
        pracAddress.setPostCode(et_pracPostalCode.getText().toString().trim());
        practitioner.setAddressClass(pracAddress);
        student.setPractitioner(practitioner);
        ////Medical History - medication
        medicalHistory= student.getMedicalHistory();
        if (medicalHistory==null) medicalHistory = new MedicalHistory();
        medication = medicalHistory.getMedication();
        if (medication==null) medication = new Medication();
        medication.setMedication1(et_medication1.getText().toString().trim());
        medication.setCondition1(et_condition1.getText().toString().trim());
        medication.setDosage1(et_dosage1.getText().toString().trim());
        medication.setMedication2(et_medication2.getText().toString().trim());
        medication.setCondition2(et_condition2.getText().toString().trim());
        medication.setDosage2(et_dosage2.getText().toString().trim());

        medicalHistory.setMedication(medication);
        medicalHistory.setOther1(et_otherhealthStatusOther1.getText().toString().trim());
        medicalHistory.setRestricted_activities(et_restricted_activities.getText().toString().trim());
        medicalHistory.setDietary_restrictions(et_dietary_restrictions.getText().toString().trim());
        getCheckboxValues();
        getRadioButtonValues();

        //student info input starts here
        student.setMedicalHistory(medicalHistory);
        student.setDaycareId(getDaycareId());
        student.setFname(txtFName);
        student.setLname(txtLName);
        student.setDob(txtDob);
        addr = student.getAddressClass();
        if(addr == null){
            addr = new AddressClass();
        }
        addr.setAptNo(txtaptNo);
        addr.setStrNo(txtStreetNo);
        addr.setStreet(txtstreet);
        addr.setCity(txtcity);
        addr.setProv(txtprov);
        addr.setPostCode(txtpostCode);
        student.setAddressClass(addr);
        student.setMedicare(medicareNo);
        student.setMedicareExpiry(expiryDate);
        student.setSeriousAllergies(seriousAllergies);
        student.setOtherAllergies(otherAllergies);
        student.setPrev_attendance_experience(et_prev_attendance_experience.getText().toString().trim());

        student.setHelp_dress(et_help_dress.getText().toString().trim());
        student.setHelp_eat(et_help_eat.getText().toString().trim());
        student.setHelp_toilet(et_help_toilet.getText().toString().trim());
        student.setHelp_wash(et_help_wash.getText().toString().trim());
        student.setHelp_other(et_help_other.getText().toString().trim());
        student.setHints(et_hints.getText().toString().trim());
        student.setLike_to_do(et_like_to_do.getText().toString().trim());
        student.setExtra_info(et_extra_info.getText().toString().trim());
    }
    public void getCheckboxValues() {
        if (chk_measles.isChecked()) medicalHistory.setMeasles(1);
        else medicalHistory.setMeasles(0);
        if (chk_mumps.isChecked()) medicalHistory.setMumps(1);
        else medicalHistory.setMumps(0);
        if (chk_meningitis.isChecked()) medicalHistory.setMeningitis(1);
        else medicalHistory.setMeningitis(0);
        if (chk_rubella.isChecked()) medicalHistory.setRubella(1);
        else medicalHistory.setRubella(0);
        if (chk_chicken_pox.isChecked()) medicalHistory.setChicken_pox(1);
        else medicalHistory.setChicken_pox(0);
        if (chk_pertussis.isChecked()) medicalHistory.setPertussis(1);
        else medicalHistory.setPertussis(0);
        if (chk_asthma.isChecked()) medicalHistory.setAsthma(1);
        else medicalHistory.setAsthma(0);
        if (chk_eczema.isChecked()) medicalHistory.setEczema(1);
        else medicalHistory.setEczema(0);
        if (chk_diabetes.isChecked()) medicalHistory.setDiabetes(1);
        else medicalHistory.setDiabetes(0);
        if (chk_epilepsy.isChecked()) medicalHistory.setEpilepsy(1);
        else medicalHistory.setEpilepsy(0);
    }
    public void getRadioButtonValues(){
        if (rdoYesEmergencyTreatment.isChecked()) student.setEmergencyTreatment(1);
        else student.setEmergencyTreatment(0);
        if (rdoYesRoutineServices.isChecked()) student.setRoutine_services(1);
        else student.setRoutine_services(0);
        if (rdoYesPrev_attendance.isChecked()) student.setPrev_attendance(1);
        else student.setPrev_attendance(0);
        if(rdo6months.isChecked()) student.setPrev_attendance_long("6 months");
        else  if(rdo1year.isChecked()) student.setPrev_attendance_long("1 year");
        else  if(rdo2years.isChecked()) student.setPrev_attendance_long("2 years");
        else  if(rdoMorethan2years.isChecked()) student.setPrev_attendance_long("more than 2 years");
        else  student.setPrev_attendance_long("");
    }
    public void populateStudentInfo(){
        //get student info from Student object passed from Contact
        if(student !=null) {
            txtFName = student.getFname();
            txtLName = student.getLname();
            txtDob = student.getDob();
            txtaptNo = student.getAddressClass().getAptNo();
            txtStreetNo = student.getAddressClass().getStrNo();
            txtstreet = student.getAddressClass().getStreet();
            txtcity = student.getAddressClass().getCity();
            txtprov = student.getAddressClass().getProv();
            txtpostCode = student.getAddressClass().getPostCode();

            //set student info to Edittext and province
            fName.setText(txtFName);
            lName.setText(txtLName);
            dob.setText(txtDob);
            aptNo.setText(txtaptNo);
            street.setText(txtstreet);
            strNo.setText(txtStreetNo);
            city.setText(txtcity);
            postCode.setText(txtpostCode);

            try {
                prov.setSelection(provinces.indexOf(txtprov));
                //spDaycare.setSelection(daycareList.indexOf(student.getDaycareClass()));
            } catch (Exception ex) {
                Log.e("Spinner error: ", ex.getMessage());
                prov.setSelection(0);

            }
            et_medicareNo.setText(student.getMedicare());
            et_expiryDate.setText(student.getMedicareExpiry());
            et_seriousAllergies.setText(student.getSeriousAllergies());
            et_otherAllergies.setText(student.getOtherAllergies());

            //RadioButton
            if(student.getEmergencyTreatment()==1) rdoYesEmergencyTreatment.setChecked(true);
            else if(student.getEmergencyTreatment()==0) rdoNoEmergencyTreatment.setChecked(true);

            if(student.getRoutine_services()==1) rdoYesRoutineServices.setChecked(true);
            else if (student.getRoutine_services()==0) rdoNoRoutineServices.setChecked(true);
            if(student.getPrev_attendance()==1) {
                rdoYesPrev_attendance.setChecked(true);
                enablePreAttendance();

                String howLong =student.getPrev_attendance_long();
                if(howLong.equalsIgnoreCase("6 month")) rdo6months.setChecked(true);
                else if (howLong.equalsIgnoreCase("1 year")) rdo1year.setChecked(true);
                else if (howLong.equalsIgnoreCase("2 years")) rdo2years.setChecked(true);
                else if (howLong.equalsIgnoreCase("more than 2 years")) rdoMorethan2years.setChecked(true);
            }
            else if (student.getPrev_attendance()==0) {
                rdoNoPrev_attendance.setChecked(true);
                unablePreAttendance();
            }


            if(student.getPractitioner()!=null){
                et_pracFName.setText(student.getPractitioner().getfName());
                et_pracLName.setText(student.getPractitioner().getlName());
                et_pracPhone.setText(student.getPractitioner().getPhone());
                et_pracAptNo.setText(student.getPractitioner().getAddressClass().getAptNo());
                et_pracStrNo.setText(student.getPractitioner().getAddressClass().getStrNo());
                et_pracStreet.setText(student.getPractitioner().getAddressClass().getStreet());
                et_pracCity.setText(student.getPractitioner().getAddressClass().getCity());
                et_pracPostalCode.setText(student.getPractitioner().getAddressClass().getPostCode());
                try{
                    spPracProv.setSelection(provinces.indexOf(student.getPractitioner().getAddressClass().getProv()));
                }
                catch(Exception ex){
                    spPracProv.setSelection(0);
                }
            }
            if(student.getMedicalHistory()!=null){
                //CheckBox
                if(student.getMedicalHistory().getMeasles()==1) chk_measles.setChecked(true);
                if(student.getMedicalHistory().getMumps()==1) chk_mumps.setChecked(true);
                if(student.getMedicalHistory().getMeningitis()==1) chk_meningitis.setChecked(true);
                if(student.getMedicalHistory().getRubella()==1) chk_rubella.setChecked(true);
                if(student.getMedicalHistory().getChicken_pox()==1) chk_chicken_pox.setChecked(true);
                if(student.getMedicalHistory().getPertussis()==1) chk_pertussis.setChecked(true);
                if(student.getMedicalHistory().getAsthma()==1) chk_asthma.setChecked(true);
                if(student.getMedicalHistory().getEczema()==1) chk_eczema.setChecked(true);
                if(student.getMedicalHistory().getDiabetes()==1) chk_diabetes.setChecked(true);
                if(student.getMedicalHistory().getEpilepsy()==1) chk_epilepsy.setChecked(true);

                et_otherhealthStatusOther1.setText(student.getMedicalHistory().getOther1());
                et_restricted_activities.setText(student.getMedicalHistory().getRestricted_activities());
                et_dietary_restrictions.setText(student.getMedicalHistory().getDietary_restrictions());
                if(student.getMedicalHistory().getMedication()!=null){
                    et_medication1.setText(student.getMedicalHistory().getMedication().getMedication1());
                    et_condition1.setText(student.getMedicalHistory().getMedication().getCondition1());
                    et_dosage1.setText(student.getMedicalHistory().getMedication().getDosage1());
                    et_medication2.setText(student.getMedicalHistory().getMedication().getMedication2());
                    et_condition2.setText(student.getMedicalHistory().getMedication().getCondition2());
                    et_dosage2.setText(student.getMedicalHistory().getMedication().getDosage2());
                }
            }
            
            et_prev_attendance_experience.setText(student.getPrev_attendance_experience());
            et_help_dress.setText(student.getHelp_dress());
            et_help_eat.setText(student.getHelp_eat());
            et_help_toilet.setText(student.getHelp_toilet());
            et_help_wash.setText(student.getHelp_wash());
            et_help_other.setText(student.getHelp_other());
            et_hints.setText(student.getHints());
            et_like_to_do.setText(student.getLike_to_do());
            et_extra_info.setText(student.getExtra_info());
        }
    }
    private void findViewByIds(){
        rgHowlong=findViewById(R.id.rgHowlong);
        tvTitle= findViewById(R.id.title);
        scrollview = findViewById(R.id.rootAddstudent);
        btnAddContact = findViewById(R.id.btnAddContact);
        btnCancel = findViewById(R.id.btnCancelAddStudent);
        btnAddNonAu = findViewById(R.id.btnAddNonAuthorized);
        btnAddStudent = findViewById(R.id.btnAddStudent);
        btnSaveAdding = findViewById(R.id.btnSaveAddingStudent);
        btnModifyStudent= findViewById(R.id.btnModifyStudent);
        btnSaveModifying= findViewById(R.id.btnSaveModifyingStudent);
        btnCancelModify = findViewById(R.id.btnCancelModifyingStudent);
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
        spDaycare = findViewById(R.id.spDaycare);

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
    private void DisplayMode(){
        btnModifyStudent.setVisibility(View.VISIBLE);
        btnCancel.setVisibility(View.VISIBLE);
        btnSaveModifying.setVisibility(View.GONE);
        btnSaveAdding.setVisibility((View.GONE));
        btnAddStudent.setVisibility(View.GONE);
        btnCancelModify.setVisibility(View.GONE);

        btnAddContact.setText("Contacts");
        btnAddNonAu.setText("Non-Authorized Pick up");
        btnAddContact.setEnabled(false);
        btnAddNonAu.setEnabled(false);
        tvTitle.setText("Student Information");


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

        prov.setEnabled(false);
        spPracProv.setEnabled(false);
        spDaycare.setEnabled(false);

        et_medicareNo.setEnabled(false);
        et_expiryDate.setEnabled(false);
        et_seriousAllergies.setEnabled(false);
        et_otherAllergies.setEnabled(false);

        et_pracFName.setEnabled(false);
        et_pracLName.setEnabled(false);
        et_pracPhone.setEnabled(false);
        et_pracAptNo.setEnabled(false);
        et_pracStrNo.setEnabled(false);
        et_pracStreet.setEnabled(false);
        et_pracCity.setEnabled(false);
        et_pracPostalCode.setEnabled(false);

        et_otherhealthStatusOther1.setEnabled(false);
        et_restricted_activities.setEnabled(false);
        et_dietary_restrictions.setEnabled(false);
        et_medication1.setEnabled(false);
        et_condition1.setEnabled(false);
        et_dosage1.setEnabled(false);
        et_medication2.setEnabled(false);
        et_condition2.setEnabled(false);
        et_dosage2.setEnabled(false);
        et_prev_attendance_experience.setEnabled(false);
        et_help_dress.setEnabled(false);
        et_help_eat.setEnabled(false);
        et_help_toilet.setEnabled(false);
        et_help_wash.setEnabled(false);
        et_help_other.setEnabled(false);
        et_hints.setEnabled(false);
        et_like_to_do.setEnabled(false);
        et_extra_info.setEnabled(false);

        chk_measles.setEnabled(false);
        chk_mumps.setEnabled(false);
        chk_meningitis.setEnabled(false);
        chk_rubella.setEnabled(false);
        chk_chicken_pox.setEnabled(false);
        chk_pertussis.setEnabled(false);
        chk_asthma.setEnabled(false);
        chk_eczema.setEnabled(false);
        chk_diabetes.setEnabled(false);
        chk_epilepsy.setEnabled(false);

        rdo6months.setEnabled(false);
        rdo1year.setEnabled(false);
        rdo2years.setEnabled(false);
        rdoMorethan2years.setEnabled(false);

        rdoYesEmergencyTreatment.setEnabled(false);
        rdoNoEmergencyTreatment.setEnabled(false);
        rdoYesRoutineServices.setEnabled(false);
        rdoNoRoutineServices.setEnabled(false);
        rdoYesPrev_attendance.setEnabled(false);
        rdoNoPrev_attendance.setEnabled(false);

    }
    private void ModifyMode(){
        btnModifyStudent.setVisibility(View.GONE);
        btnSaveModifying.setVisibility(View.VISIBLE);
        btnCancelModify.setVisibility(View.VISIBLE);
        btnAddStudent.setVisibility(View.GONE);
        btnSaveAdding.setVisibility(View.GONE);
        btnAddContact.setText("Contacts");
        btnAddNonAu.setText("Non-Authorized Pick up");
        btnAddContact.setEnabled(true);
        btnAddNonAu.setEnabled(true);
        tvTitle.setText("Modify Student Information");
        btnCancel.setVisibility(View.GONE);

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

        prov.setEnabled(true);
        spPracProv.setEnabled(true);
        spDaycare.setEnabled(true);

        et_medicareNo.setEnabled(true);
        et_expiryDate.setEnabled(true);
        et_seriousAllergies.setEnabled(true);
        et_otherAllergies.setEnabled(true);

        et_pracFName.setEnabled(true);
        et_pracLName.setEnabled(true);
        et_pracPhone.setEnabled(true);
        et_pracAptNo.setEnabled(true);
        et_pracStrNo.setEnabled(true);
        et_pracStreet.setEnabled(true);
        et_pracCity.setEnabled(true);
        et_pracPostalCode.setEnabled(true);

        et_otherhealthStatusOther1.setEnabled(true);
        et_restricted_activities.setEnabled(true);
        et_dietary_restrictions.setEnabled(true);
        et_medication1.setEnabled(true);
        et_condition1.setEnabled(true);
        et_dosage1.setEnabled(true);
        et_medication2.setEnabled(true);
        et_condition2.setEnabled(true);
        et_dosage2.setEnabled(true);
        et_prev_attendance_experience.setEnabled(true);
        et_help_dress.setEnabled(true);
        et_help_eat.setEnabled(true);
        et_help_toilet.setEnabled(true);
        et_help_wash.setEnabled(true);
        et_help_other.setEnabled(true);
        et_hints.setEnabled(true);
        et_like_to_do.setEnabled(true);
        et_extra_info.setEnabled(true);

        chk_measles.setEnabled(true);
        chk_mumps.setEnabled(true);
        chk_meningitis.setEnabled(true);
        chk_rubella.setEnabled(true);
        chk_chicken_pox.setEnabled(true);
        chk_pertussis.setEnabled(true);
        chk_asthma.setEnabled(true);
        chk_eczema.setEnabled(true);
        chk_diabetes.setEnabled(true);
        chk_epilepsy.setEnabled(true);

        if(rdoYesPrev_attendance.isChecked()){
            enablePreAttendance();
        }
        else{
            unablePreAttendance();
        }
        rdoYesEmergencyTreatment.setEnabled(true);
        rdoNoEmergencyTreatment.setEnabled(true);
        rdoYesRoutineServices.setEnabled(true);
        rdoNoRoutineServices.setEnabled(true);
        rdoYesPrev_attendance.setEnabled(true);
        rdoNoPrev_attendance.setEnabled(true);
    }
    private void AddMode(){
        btnModifyStudent.setVisibility(View.GONE);
        btnSaveModifying.setVisibility(View.GONE);
        btnCancelModify.setVisibility(View.GONE);
        btnAddStudent.setVisibility(View.GONE);
        btnSaveAdding.setVisibility(View.VISIBLE);
        btnAddContact.setText("Add Contacts");
        btnAddNonAu.setText("Add Non-Authorized Pickup");
        btnAddContact.setEnabled(true);
        btnAddNonAu.setEnabled(true);
        tvTitle.setText("Add New Student");
        btnCancel.setVisibility(View.VISIBLE);
    }
    private void enablePreAttendance(){
        rgHowlong.setEnabled(true);
        et_prev_attendance_experience.setEnabled(true);
        rdo1year.setEnabled(true);
        rdo6months.setEnabled(true);
        rdo2years.setEnabled(true);
        rdoMorethan2years.setEnabled(true);
    }
    private void unablePreAttendance(){
        rgHowlong.setEnabled(false);
        et_prev_attendance_experience.setEnabled(false);
        rdo1year.setEnabled(false);
        rdo6months.setEnabled(false);
        rdo2years.setEnabled(false);
        rdoMorethan2years.setEnabled(false);
        rdo1year.setChecked(false);
        rdo6months.setChecked(false);
        rdo2years.setChecked(false);
        rdoMorethan2years.setChecked(false);
    }
    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.isPreAttendant:
                if (checked)
                    enablePreAttendance();
                    break;
            case R.id.isNotPreAttendant:
                if (checked)
                    unablePreAttendance();
                    break;
        }
    }
    public int getDaycareId(){
        final SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        int daycareId = pref.getInt("daycare", -1);
        return daycareId;
    }

    void populateContacts(){
        contacts = student.getContacts();
        if (contacts != null){

            //Modify layout height
            rlContacts.getLayoutParams().height=(contacts.size()*200);

            //create adapter
            CreateStudentActivity.MyAdapter adapter = new CreateStudentActivity.MyAdapter(this, contacts);
            lvContacts.setAdapter(adapter);
            //set item click on list view
            lvContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    //Toast.makeText(StudentListView.this, studentList1.get(i).getFullname(),Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CreateStudentActivity.this, ContactActivity.class);
                    intent.putExtra("contact", contacts.get(i));
                    getStudentInfo();
                    intent.putExtra("student", student);
                    //System.out.println("Put student from Create activity");
                    //System.out.println(student);
                    intent.putExtra("index", i);
                    startActivity(intent);
                }
            });
        }
    }
    class MyAdapter extends ArrayAdapter<ContactClass> {
        Context context;
        ArrayList<ContactClass> contacts;

        MyAdapter(Context c, ArrayList<ContactClass> myContacts) {
            super(c, R.layout.contact_row, R.id.fullName, myContacts);
            this.context = c;
            this.contacts = myContacts;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate((R.layout.contact_row), parent, false);
            TextView myName = row.findViewById(R.id.fullName);
            TextView myDesc = row.findViewById(R.id.desc);

            //set resources on views
            myName.setText(contacts.get(position).getFirstName()+" "+contacts.get(position).getLastName());
            String emergency_contact ="";
            if(contacts.get(position).getFamilyRel().getIsEmergencyCOntact()==1){
                emergency_contact = "Emergency contact";
            }

            myDesc.setText(contacts.get(position).getFamilyRel().getRelationship()+" - Cellphone:"+ contacts.get(position).getCellPhone()+" - "+emergency_contact);
            return row;
        }
    }
    void populateNonAuthorizeds(){
        //list view for Non-Authorized
        nonAuthorizeds = student.getNonAuthorizeds();
        if(nonAuthorizeds!=null){
            //Modify layout height
            rlNonAuthorized.getLayoutParams().height = nonAuthorizeds.size()*130;

            //create adapter
            CreateStudentActivity.NonAuthorizedAdapter adapter = new CreateStudentActivity.NonAuthorizedAdapter(this, nonAuthorizeds);
            lvNonAu.setAdapter(adapter);
            //set item click on list view
            lvNonAu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(CreateStudentActivity.this, NonAuthorizedActivity.class);
                    intent.putExtra("nonAthorized", nonAuthorizeds.get(i));
                    intent.putExtra("student",student);
                    intent.putExtra("index",i);
                    startActivity(intent);
                }
            });
        }
        }
    class NonAuthorizedAdapter extends ArrayAdapter<NonAuthorized> {
        Context context;
        ArrayList<NonAuthorized> myNonAuthorized;

        NonAuthorizedAdapter(Context c, ArrayList<NonAuthorized> nonAu) {
            super(c, R.layout.contact_row, R.id.fullName, nonAu);
            this.context = c;
            this.myNonAuthorized = nonAu;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate((R.layout.contact_row), parent, false);
            TextView myName = row.findViewById(R.id.fullName);
            TextView myDesc = row.findViewById(R.id.desc);

            //set resources on views
            myName.setText(myNonAuthorized.get(position).getFirstName()+" "+myNonAuthorized.get(position).getLastName());
            myDesc.setVisibility(View.GONE);
            return row;
        }
    }
    public void GotoNonAuthorized(){
        Intent intent = new Intent(CreateStudentActivity.this, NonAuthorizedActivity.class);
        getStudentInfo();
        intent.putExtra("student", student);
        startActivity(intent);
    }
    public void GotoContact(){
        Intent intent = new Intent(CreateStudentActivity.this, ContactActivity.class);
        getStudentInfo();
        intent.putExtra("student", student);
        startActivity(intent);
    }
    public void GotoHomePage(){
        Intent startIntent = new Intent(getApplicationContext(), HomePageActivity.class);
        startActivity(startIntent);
        finish();
    }
    private boolean Validation(){
        if(student.getFname().isEmpty()){
            ShowNotification("Please enter student's first name");
            return false;
        }
        else if(student.getLname().isEmpty()){
            ShowNotification("Please enter student's last name");
            return false;
        }
        else if(student.getDob().isEmpty() || !ValidDate(student.getDob())){
            ShowNotification("Please enter student's birthday YYYY-MM-DD");
            return false;
        }
        else if(student.getMedicare().isEmpty()){
            ShowNotification("Please enter Medicare Number");
            return false;
        }
        else if(student.getMedicareExpiry().isEmpty() || !ValidDate(student.getMedicareExpiry())){
            ShowNotification("Please enter Medicare Expiry Date- YYYY-MM-DD");
            return false;
        }
        else if(IsBlank(student.getAddressClass().getStrNo())){
            ShowNotification("Please enter student's street number");
            return false;
        }
        else if(IsBlank(student.getAddressClass().getStreet())){
            ShowNotification("Please enter student's street name");
            return false;
        }
        else if(IsBlank(student.getAddressClass().getCity())){
            ShowNotification("Please enter student's town or city");
            return false;
        }
        else if(IsBlank(student.getAddressClass().getPostCode()) || !ValidPostal(student.getAddressClass().getPostCode())){
            ShowNotification("Please enter student's valid postal code");
            return false;
        }
        else if(IsBlank(student.getPractitioner().getfName())){
            ShowNotification("Please enter practitioner's first name");
            return false;
        }
        else if(IsBlank(student.getPractitioner().getlName())){
            ShowNotification("Please enter practitioner's last name");
            return false;
        }
        else if(IsBlank(student.getPractitioner().getPhone())){
            ShowNotification("Please enter practitioner's phone number");
            return false;
        }
        return true;
    }

    private boolean IsBlank(String string){
        if(string.equals("")){
            return true;
        }
        return  false;
    }//end of IsBlank

    private void ShowNotification(String message) {
        Snackbar display = Snackbar.make(findViewById(R.id.tvAddContact), message, Snackbar.LENGTH_LONG);
        display.show();
    }//end of ShowNotification

    private boolean ValidDate(String string){
        Pattern date = Pattern.compile("^((19|2[0-9])[0-9]{2})-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$");
        Matcher matcher = date.matcher(string);
        if(matcher.matches()){
            return true;
        }
        return false;
    }
    private boolean ValidPostal(String string){
        Pattern postal = Pattern.compile("^(?!.*[DFIOQU])[A-VXY][0-9][A-Z] ?[0-9][A-Z][0-9]$");
        Matcher matcher = postal.matcher(string);
        if(matcher.matches()){
            return true;
        }
        return false;
    }//end of ValidPostal


    public void GotoStudentList(){
        Intent startIntent = new Intent(getApplicationContext(), StudentListView.class);
        startActivity(startIntent);
    }
    @Override
    public void onClick(View view) {

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    //Phoebe copied from LoginActivity (Ethan) 02/06/2020
    public static void showAlertDialog(Context context, String title, String message){  //creates a new alertbox (might create a class for this so less repeat code if it gets used in the future
        //This alert box only has an OK button on it that just closes
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle(title);
        alert.setMessage(message);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //on click method required to setButton name no code is required in here for the OK button it will just close the alertbox
            }
        });
        alert.create().show();
    }

}
