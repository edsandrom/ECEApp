package com.example.eceapp;

//Phoebe added implements Parcelable to pass Student object between activities - Feb20
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class StudentClass implements Parcelable {
    private String parent1Name, parent2Name;
    private String fname;
    private String lname;
    private String fullname;
    private String address;
    private String dob;
    private int studentId;
    private int daycareId;
    private int familyId;

    //Phoebe added more fields - Feb20
    private int addressId;
    private String aptNo;
    private String strNo;
    private String street;
    private String city;
    private String prov;
    private String postCode;
    private AddressClass addressClass;

    //Phoebe added Contact _Feb25
    private ArrayList<ContactClass> contacts;
    FamilyRelationshipClass familyRel;

    //Ethan added March 3
    private String medicare; //medicare number --Phoebe changed String to int - but it was not apply for maximum int value ->Phoebe changed the data type to String
    private String practioner; //full Name
    private int emergencyTreatment; //this might be a int or bool --Phoebe changed String to int
    private String seriousAllergies;
    private String otherAllergies;

    //Non-Authorized pickup
    private ArrayList<NonAuthorized> nonAuthorizeds;

    //Other fields in student obj
    private String medicareExpiry; //Date
    private int routine_services,prev_attendance;
    private String prev_attendance_long, prev_attendance_experience,
            allergy_management_plan, emergency_plan, routine_services_plan,
            help_dress, help_eat, help_toilet, help_wash, help_other, hints,
            like_to_do, extra_info;

    //Practitioner
    private int practitionerId;
    Practitioner practitioner;

    //Medical History
    private int medicalHistoryId;
    MedicalHistory medicalHistory;

    //Daycare
    DaycareClass daycareClass;


    public StudentClass() {
    }

    public StudentClass(String fullname, int daycareId) {
        this.fullname = fullname;
        this.daycareId = daycareId;
    }


    protected StudentClass(Parcel in) {
        parent1Name = in.readString();
        parent2Name = in.readString();
        fname = in.readString();
        lname = in.readString();
        fullname = in.readString();
        address = in.readString();
        dob = in.readString();
        studentId = in.readInt();
        daycareId = in.readInt();
        familyId = in.readInt();
        addressId = in.readInt();
        aptNo = in.readString();
        strNo = in.readString();
        street = in.readString();
        city = in.readString();
        prov = in.readString();
        postCode = in.readString();
        addressClass = in.readParcelable(AddressClass.class.getClassLoader());
        contacts = in.createTypedArrayList(ContactClass.CREATOR);
        familyRel = in.readParcelable(FamilyRelationshipClass.class.getClassLoader());
        medicare = in.readString();
        practioner = in.readString();
        emergencyTreatment = in.readInt();
        seriousAllergies = in.readString();
        otherAllergies = in.readString();
        nonAuthorizeds = in.createTypedArrayList(NonAuthorized.CREATOR);
        medicareExpiry = in.readString();
        routine_services = in.readInt();
        prev_attendance = in.readInt();
        prev_attendance_long = in.readString();
        prev_attendance_experience = in.readString();
        allergy_management_plan = in.readString();
        emergency_plan = in.readString();
        routine_services_plan = in.readString();
        help_dress = in.readString();
        help_eat = in.readString();
        help_toilet = in.readString();
        help_wash = in.readString();
        help_other = in.readString();
        hints = in.readString();
        like_to_do = in.readString();
        extra_info = in.readString();
        practitionerId = in.readInt();
        practitioner = in.readParcelable(Practitioner.class.getClassLoader());
        medicalHistoryId = in.readInt();
        medicalHistory = in.readParcelable(MedicalHistory.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(parent1Name);
        dest.writeString(parent2Name);
        dest.writeString(fname);
        dest.writeString(lname);
        dest.writeString(fullname);
        dest.writeString(address);
        dest.writeString(dob);
        dest.writeInt(studentId);
        dest.writeInt(daycareId);
        dest.writeInt(familyId);
        dest.writeInt(addressId);
        dest.writeString(aptNo);
        dest.writeString(strNo);
        dest.writeString(street);
        dest.writeString(city);
        dest.writeString(prov);
        dest.writeString(postCode);
        dest.writeParcelable(addressClass, flags);
        dest.writeTypedList(contacts);
        dest.writeParcelable(familyRel, flags);
        dest.writeString(medicare);
        dest.writeString(practioner);
        dest.writeInt(emergencyTreatment);
        dest.writeString(seriousAllergies);
        dest.writeString(otherAllergies);
        dest.writeTypedList(nonAuthorizeds);
        dest.writeString(medicareExpiry);
        dest.writeInt(routine_services);
        dest.writeInt(prev_attendance);
        dest.writeString(prev_attendance_long);
        dest.writeString(prev_attendance_experience);
        dest.writeString(allergy_management_plan);
        dest.writeString(emergency_plan);
        dest.writeString(routine_services_plan);
        dest.writeString(help_dress);
        dest.writeString(help_eat);
        dest.writeString(help_toilet);
        dest.writeString(help_wash);
        dest.writeString(help_other);
        dest.writeString(hints);
        dest.writeString(like_to_do);
        dest.writeString(extra_info);
        dest.writeInt(practitionerId);
        dest.writeParcelable(practitioner, flags);
        dest.writeInt(medicalHistoryId);
        dest.writeParcelable(medicalHistory, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<StudentClass> CREATOR = new Creator<StudentClass>() {
        @Override
        public StudentClass createFromParcel(Parcel in) {
            return new StudentClass(in);
        }

        @Override
        public StudentClass[] newArray(int size) {
            return new StudentClass[size];
        }
    };

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getDaycareId() {
        return daycareId;
    }

    public void setDaycareId(int daycareId) {
        this.daycareId = daycareId;
    }

    public int getFamilyId() {
        return familyId;
    }

    public void setFamilyId(int familyId) {
        this.familyId = familyId;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFullname() {
        if(fullname == null){
            return fname + " " +lname;
        }
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public String getAptNo() {
        return aptNo;
    }

    public void setAptNo(String aptNo) {
        this.aptNo = aptNo;
    }

    public String getStrNo() {
        return strNo;
    }

    public void setStrNo(String strNo) {
        this.strNo = strNo;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProv() {
        return prov;
    }

    public void setProv(String prov) {
        this.prov = prov;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public ArrayList<ContactClass> getContacts() {
        return contacts;
    }

    public void setContacts(ArrayList<ContactClass> contacts) {
        this.contacts = contacts;
    }

    public FamilyRelationshipClass getFamilyRel() {
        return familyRel;
    }

    public void setFamilyRel(FamilyRelationshipClass familyRel) {
        this.familyRel = familyRel;
    }




    public String getMedicare() {
        return medicare;
    }

    public void setMedicare(String medicare) {
        this.medicare = medicare;
    }

    public String getOtherAllergies() {
        return otherAllergies;
    }

    public void setOtherAllergies(String otherAllergies) {
        this.otherAllergies = otherAllergies;
    }

    public String getPractioner() {
        return practioner;
    }

    public void setPractioner(String practioner) {
        this.practioner = practioner;
    }

    public String getSeriousAllergies() {
        return seriousAllergies;
    }

    public void setSeriousAllergies(String seriousAllergies) {
        this.seriousAllergies = seriousAllergies;
    }

    public int getEmergencyTreatment() {
        return emergencyTreatment;
    }

    public void setEmergencyTreatment(int emergencyTreatment) {
        this.emergencyTreatment = emergencyTreatment;
    }

    public String getMedicareExpiry() {
        return medicareExpiry;
    }

    public void setMedicareExpiry(String medicareExpiry) {
        this.medicareExpiry = medicareExpiry;
    }

    public int getRoutine_services() {
        return routine_services;
    }

    public void setRoutine_services(int routine_services) {
        this.routine_services = routine_services;
    }

    public int getPrev_attendance() {
        return prev_attendance;
    }

    public void setPrev_attendance(int prev_attendance) {
        this.prev_attendance = prev_attendance;
    }

    public String getPrev_attendance_long() {
        return prev_attendance_long;
    }

    public void setPrev_attendance_long(String prev_attendance_long) {
        this.prev_attendance_long = prev_attendance_long;
    }

    public String getPrev_attendance_experience() {
        return prev_attendance_experience;
    }

    public void setPrev_attendance_experience(String prev_attendance_experience) {
        this.prev_attendance_experience = prev_attendance_experience;
    }

    public String getAllergy_management_plan() {
        return allergy_management_plan;
    }

    public void setAllergy_management_plan(String allergy_management_plan) {
        this.allergy_management_plan = allergy_management_plan;
    }

    public String getEmergency_plan() {
        return emergency_plan;
    }

    public void setEmergency_plan(String emergency_plan) {
        this.emergency_plan = emergency_plan;
    }

    public String getRoutine_services_plan() {
        return routine_services_plan;
    }

    public void setRoutine_services_plan(String routine_services_plan) {
        this.routine_services_plan = routine_services_plan;
    }

    public String getHelp_dress() {
        return help_dress;
    }

    public void setHelp_dress(String help_dress) {
        this.help_dress = help_dress;
    }

    public String getHelp_eat() {
        return help_eat;
    }

    public void setHelp_eat(String help_eat) {
        this.help_eat = help_eat;
    }

    public String getHelp_toilet() {
        return help_toilet;
    }

    public void setHelp_toilet(String help_toilet) {
        this.help_toilet = help_toilet;
    }

    public String getHelp_wash() {
        return help_wash;
    }

    public void setHelp_wash(String help_wash) {
        this.help_wash = help_wash;
    }

    public String getHelp_other() {
        return help_other;
    }

    public void setHelp_other(String help_other) {
        this.help_other = help_other;
    }

    public String getHints() {
        return hints;
    }

    public void setHints(String hints) {
        this.hints = hints;
    }

    public String getLike_to_do() {
        return like_to_do;
    }

    public void setLike_to_do(String like_to_do) {
        this.like_to_do = like_to_do;
    }

    public String getExtra_info() {
        return extra_info;
    }

    public void setExtra_info(String extra_info) {
        this.extra_info = extra_info;
    }

    public int getPractitionerId() {
        return practitionerId;
    }

    public void setPractitionerId(int practitionerId) {
        this.practitionerId = practitionerId;
    }

    public Practitioner getPractitioner() {
        return practitioner;
    }

    public void setPractitioner(Practitioner practitioner) {
        this.practitioner = practitioner;
    }

    public int getMedicalHistoryId() {
        return medicalHistoryId;
    }

    public void setMedicalHistoryId(int medicalHistoryId) {
        this.medicalHistoryId = medicalHistoryId;
    }

    public MedicalHistory getMedicalHistory() {
        return medicalHistory;
    }

    public void setMedicalHistory(MedicalHistory medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    public DaycareClass getDaycareClass() {
        return daycareClass;
    }

    public void setDaycareClass(DaycareClass daycareClass) {
        this.daycareClass = daycareClass;
    }

    public ArrayList<NonAuthorized> getNonAuthorizeds() {
        return nonAuthorizeds;

    }

    public void setNonAuthorizeds(ArrayList<NonAuthorized> nonAuthorizeds) {
        this.nonAuthorizeds = nonAuthorizeds;
    }

    public String getParent1Name() {
        return parent1Name;
    }

    public void setParent1Name(String parent1Name) {
        this.parent1Name = parent1Name;
    }

    public String getParent2Name() {
        return parent2Name;
    }

    public void setParent2Name(String parent2Name) {
        this.parent2Name = parent2Name;
    }


    @Override
    public String toString(){
        return fullname;
    }

    public AddressClass getAddressClass() {
        return addressClass;
    }

    public void setAddressClass(AddressClass addressClass) {
        this.addressClass = addressClass;
    }



}
