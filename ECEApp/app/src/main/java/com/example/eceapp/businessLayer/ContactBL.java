package com.example.eceapp.businessLayer;

import com.example.eceapp.ContactClass;
import com.example.eceapp.dataLayer.ContactDL;

import java.util.ArrayList;

public class ContactBL {
    ContactDL contactDL = ContactDL.getInstance();
    public int[] addContact(ContactClass contact){
        return contactDL.addContact(contact);
    }
    public int editContact(ContactClass contact){
        return contactDL.editContact(contact);
    }
    public ArrayList<ContactClass> getContactsByStudentId(int id){
        return contactDL.fetchContactsByStudentId(id);
    }
}
