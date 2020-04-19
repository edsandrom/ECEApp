package com.example.eceapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContactActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private EditText etFirstName, etLastName, etHomePhone, etCellPhone, etWorkPhone, etAptNo, etStrNo, etStr, etCity, etPostalCode, etWorkPlace, etEmail;
    private Spinner spRelationship, spProvinces;
    private CheckBox chkSameAddress, chkAuthorizedPickup, chkEmergencyContact, chkActive;
    private Button btnAddContact, btnCancel, btnSaveModify;
    private TextView tvContactTitle;
    AddressClass address = new AddressClass();
    ArrayList provinces = address.getProvinces();
    FamilyRelationshipClass familyRel = new FamilyRelationshipClass();
    ArrayList relValues = familyRel.getRelationshipValues();
    ArrayList<ContactClass> contacts = new ArrayList<ContactClass>();
    ContactClass contact = new ContactClass();
    AddressClass contactAddress = new AddressClass();
    FamilyRelationshipClass familyRelationship = new FamilyRelationshipClass();
    StudentClass student = new StudentClass();
    int index;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        FindViewByIDs();//shorten number of lines on main method
        //Spinner: Relationships and provinces - Drop down List
        populateRelationships(spRelationship);
        populateProvinces(spProvinces);
        //Get Student object from Create Student Activity
        Intent intent = getIntent();
        student = intent.getParcelableExtra("student");
        contact = intent.getParcelableExtra("contact");//in Modify mode
        index = intent.getIntExtra("index",0);

        //get existed contacts array from student
        if (student !=null){
            contacts = student.getContacts();
            if(contact!=null){ //Modify mode
                PopulateContact();
                ModifyMode();
            }
            else{
                contact=new ContactClass();
            }
        }
        btnSaveModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetContact();
                if(ValidationContact()){
                    contacts.set(index, contact);
                    student.setContacts(contacts);
                    goToCreateStudent();
                }
            }
        });

        //Button Cancel - cancel adding contact - pass student object back to Create student for user  not to enter student info again
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToCreateStudent();
            }
        });

        //Button Add Contact
        btnAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetContact();
                if(ValidationContact()){
                    if(contacts == null) contacts = new ArrayList<>();
                    contacts.add(contact);
                    student.setContacts(contacts);
                    goToCreateStudent();
                }
            }
        });
    }
    private void FindViewByIDs(){
        //TextView
        tvContactTitle = findViewById(R.id.tvContactTitle);

        //Spinner
        spRelationship = findViewById(R.id.spRelationship);
        spProvinces = findViewById(R.id.spContactProv);

        //Button
        btnSaveModify=findViewById(R.id.btnModifyContact);
        btnCancel = findViewById(R.id.btnContactCancel);
        btnAddContact = findViewById(R.id.btnStudentContact);

        //EditText
        etFirstName = findViewById(R.id.txtContactFirstName);
        etLastName = findViewById(R.id.txtContactLastname);
        etHomePhone = findViewById(R.id.txtHomePhone);
        etCellPhone = findViewById(R.id.txtCellPhone);
        etWorkPhone = findViewById(R.id.txtWorkPhone);
        etAptNo = findViewById(R.id.txtContactAptNo);
        etStrNo = findViewById(R.id.txtContactStrNo);
        etStr = findViewById(R.id.txtContactStreet);
        etCity = findViewById(R.id.txtContactCity);
        etPostalCode = findViewById(R.id.txtContactPostCode);
        etEmail = findViewById(R.id.txtEmail);
        etWorkPlace = findViewById(R.id.txtWorkPlace);

        //CheckBox
        chkSameAddress = findViewById(R.id.chkSameAddress);
        chkAuthorizedPickup = findViewById(R.id.chkPickup);
        chkEmergencyContact = findViewById(R.id.chkEmergency);
        chkActive=findViewById(R.id.chkActiveContact);

    }
    private void ModifyMode(){
        btnSaveModify.setVisibility(View.VISIBLE);
        btnAddContact.setVisibility(View.GONE);
        tvContactTitle.setText("MODIFY CONTACT");
    }
    private void enableAddressFields(){
        etAptNo.setEnabled(true);
        etStrNo.setEnabled(true);
        etStr.setEnabled(true);
        etCity.setEnabled(true);
        etPostalCode.setEnabled(true);
        spProvinces.setEnabled(true);
    }
    private void unableAddressFields(){
        etAptNo.setEnabled(false);
        etStrNo.setEnabled(false);
        etStr.setEnabled(false);
        etCity.setEnabled(false);
        etPostalCode.setEnabled(false);
        spProvinces.setEnabled(false);

        etAptNo.setText("");
        etStrNo.setText("");
        etStr.setText("");
        etCity.setText("");
        etPostalCode.setText("");
        spProvinces.setSelection(0);
    }
    private void PopulateContact(){
        if(contact.familyRel.getIsEmergencyCOntact()==1) chkEmergencyContact.setChecked(true);
        else chkEmergencyContact.setChecked(false);
        if(contact.familyRel.getIsAuthorizedPickup()==1) chkAuthorizedPickup.setChecked(true);
        else chkAuthorizedPickup.setChecked(false);
        if(compareContactAndStudentAddress( student.getAddressClass(), contact.getAddress()) ) {
            chkSameAddress.setChecked(true);
            unableAddressFields();
        }
        else {
            chkSameAddress.setChecked(false);
            enableAddressFields();
        }
        if(contact.isActive==1) chkActive.setChecked(true);
        else chkActive.setChecked(false);

        etFirstName.setText(contact.getFirstName());
        etLastName.setText(contact.getLastName());
        etHomePhone.setText(contact.getHomePhone());
        etCellPhone.setText(contact.getCellPhone());
        etWorkPhone.setText(contact.getWorkPhone());
        etWorkPlace.setText(contact.getPlaceOfWork());
        etEmail.setText(contact.getEmail());
        etAptNo.setText(contact.getAddress().getAptNo());
        etStrNo.setText(contact.getAddress().getStrNo());
        etStr.setText(contact.getAddress().getStreet());
        etCity.setText(contact.getAddress().getCity());
        etPostalCode.setText(contact.getAddress().getPostCode());

        try {
            spProvinces.setSelection(provinces.indexOf(contact.getAddress().getProv()));
            spRelationship.setSelection(relValues.indexOf(contact.getFamilyRel().getRelationship()));
        } catch (Exception ex) {
            Log.e("Spinner error: ", ex.getMessage());
            spProvinces.setSelection(0);
            spRelationship.setSelection(0);
        }
    }
    private void GetContact(){
        if(contact==null) contact = new ContactClass();
        familyRelationship = contact.getFamilyRel();
        if(familyRelationship==null) familyRelationship=new FamilyRelationshipClass();
        contactAddress = contact.getAddress();
        if(contactAddress==null) contactAddress = new AddressClass();

        familyRelationship.setRelationship(spRelationship.getSelectedItem().toString());
        if(chkEmergencyContact.isChecked()) familyRelationship.setIsEmergencyCOntact(1);
        else familyRelationship.setIsEmergencyCOntact(0);
        if(chkAuthorizedPickup.isChecked()) familyRelationship.setIsAuthorizedPickup(1);
        else familyRelationship.setIsAuthorizedPickup(0);
        if(chkActive.isChecked()) {
            contact.setIsActive(1);
            familyRelationship.setIsActive(1);
        }
        else {
            contact.setIsActive(0);
            familyRelationship.setIsActive(0);
        }
        if(chkSameAddress.isChecked()) {
            contact.setIsSameAddress(1);
            contactAddress.setAddressId(student.getAddressClass().getAddressId());
            contactAddress.setAptNo(student.getAddressClass().getAptNo());
            contactAddress.setStrNo(student.getAddressClass().getStrNo());
            contactAddress.setStreet(student.getAddressClass().getStreet());
            contactAddress.setCity(student.getAddressClass().getCity());
            contactAddress.setPostCode(student.getAddressClass().getPostCode());
            contactAddress.setProv(student.getAddressClass().getProv());
        }
        else {
            contact.setIsSameAddress(0);
            contactAddress.setAptNo(etAptNo.getText().toString().trim());
            contactAddress.setStrNo(etStrNo.getText().toString().trim());
            contactAddress.setStreet(etStr.getText().toString().trim());
            contactAddress.setCity(etCity.getText().toString().trim());
            contactAddress.setPostCode(etPostalCode.getText().toString().trim());
            contactAddress.setProv(spProvinces.getSelectedItem().toString());
        }
        contact.setAddress(contactAddress);
        contact.setFamilyRel(familyRelationship);
        contact.setFirstName(etFirstName.getText().toString().trim());
        contact.setLastName(etLastName.getText().toString().trim());
        contact.setHomePhone(etHomePhone.getText().toString().trim());
        contact.setCellPhone(etCellPhone.getText().toString().trim());
        contact.setWorkPhone(etWorkPhone.getText().toString().trim());
        contact.setPlaceOfWork(etWorkPlace.getText().toString().trim());
        contact.setEmail(etEmail.getText().toString().trim());
    }
    public boolean compareContactAndStudentAddress(AddressClass s, AddressClass c){
        if(s.getAptNo().equalsIgnoreCase(c.getAptNo())
                && s.getStrNo().equalsIgnoreCase(c.getStrNo())
               && s.getStreet().equalsIgnoreCase(c.getStreet())
               && s.getCity().equalsIgnoreCase(c.getCity())
               && s.getPostCode().equalsIgnoreCase(c.getPostCode())
               && s.getProv().equalsIgnoreCase(c.getProv())
        ){
            return true;
        }
        else{
            return false;
        }

    }
    private boolean ValidationContact(){
        if(contact.getFirstName().isEmpty()){
            ShowNotification("Please enter a first name");
            return false;
        }
        if(contact.getLastName().isEmpty()){
            ShowNotification("Please enter a last name");
            return false;
        }
        if(contact.getCellPhone().isEmpty() || !ValidPhone(contact.getCellPhone())){
            ShowNotification("Please enter a valid cellphone number");
            return false;
        }
        if(contact.getPlaceOfWork().isEmpty()) {
            ShowNotification("Please enter the name of your work place");
            return false;
        }
        if(contact.getEmail().isEmpty() || !ValidEmail(contact.getEmail())){
            ShowNotification("Please enter a valid email address");
            return false;
        }
        if(contact.getIsSameAddress()==0){
            if(contact.getAddress().getStrNo().isEmpty()){
                ShowNotification("Please enter a street number");
                return false;
            }

            if(contact.getAddress().getStreet().isEmpty()){
                ShowNotification("Please enter a street name");
                return false;
            }

            if(contact.getAddress().getCity().isEmpty()){
                ShowNotification("Please enter a town or city");
                return false;
            }

            if(contact.getAddress().getPostCode().isEmpty() || !ValidPostal(contact.getAddress().getPostCode())){
                ShowNotification("Please enter a valid postal code");
                return false;
            }
        }
        return true;
    }
    private void ShowNotification(String message) {

        Snackbar display = Snackbar.make(findViewById(R.id.txtEmail), message, Snackbar.LENGTH_LONG);
        display.show();
    }//end of ShowNotification
        private boolean ValidPostal(String string){
        Pattern postal = Pattern.compile("^(?!.*[DFIOQU])[A-VXY][0-9][A-Z] ?[0-9][A-Z][0-9]$");
        Matcher matcher = postal.matcher(string);
        if(matcher.matches()){
            return true;
        }
        return false;
    }

    public boolean ValidEmail(String string){
        Pattern email = Patterns.EMAIL_ADDRESS;
        Matcher matcher = email.matcher(string);
        if(matcher.matches()){
            return true;
        }
        return false;
    }
    public boolean ValidPhone(String string){
        Pattern phone = Patterns.PHONE;
        Matcher matcher = phone.matcher(string);
        if(matcher.matches()){
            return true;
        }
        return false;
    }
    public void goToCreateStudent(){
        Intent intent = new Intent(ContactActivity.this, CreateStudentActivity.class);
        if(student != null){
            intent.putExtra("student", student);
            intent.putExtra("from", "contact");
        }
        startActivity(intent);
    }
    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();
        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.chkSameAddress:
                if(checked){
                    contact.setIsSameAddress(1);
                    unableAddressFields();
                }
                else{
                    contact.setIsSameAddress(0);
                    enableAddressFields();
                }
        }
    }
    public void populateRelationships(Spinner sp){
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, relValues);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter);
        sp.setOnItemSelectedListener(this);
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
