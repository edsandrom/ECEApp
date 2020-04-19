package com.example.eceapp.dataLayer;

import android.util.Log;

import com.example.eceapp.ConnectionClass;
import com.example.eceapp.ContactClass;
import com.example.eceapp.FamilyRelationshipClass;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

public class ContactDL {
    private static ContactDL instance;

    //Database Connection
    private static Connection conn;
    //

    public static ContactDL getInstance() {
        if (instance == null) {
            ConnectionClass connection = new ConnectionClass();
            instance = new ContactDL();
            conn = connection.Connect();
            //System.out.println(conn);
        }
        return instance;
    }


    public int[] addContact(ContactClass contact){
        int contactId=0;
        int isExist=0;
        int[] contactID_isExist = new int[2];
        try {
            CallableStatement mystmt = conn.prepareCall("call add_contact(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            mystmt.setInt(1, contact.getAddressId());
            mystmt.setString(2, contact.getFirstName());
            mystmt.setString(3, contact.getLastName());
            mystmt.setString(4, contact.getHomePhone());
            mystmt.setString(5, contact.getWorkPhone());
            mystmt.setString(6, contact.getCellPhone());
            mystmt.setString(7, contact.getPlaceOfWork());
            mystmt.setString(8, contact.getEmail());
            //check duplicate student record
            mystmt.registerOutParameter(9, Types.INTEGER);
            mystmt.registerOutParameter(10, Types.INTEGER);
            mystmt.executeUpdate();
            contactId = mystmt.getInt(9);
            isExist = mystmt.getInt(10);
            contactID_isExist[0] =contactId;
            contactID_isExist[1] = isExist;

            return contactID_isExist;
        } catch (SQLException ex) {
            //RepeatMethods.showAlertDialog(CreateStudentActivity.this, "Error", "Something wrong happened. Please try again");
            //Log.e("SQL ERROR", ex.getMessage());

        } catch (Exception ex) {
            Log.e("Other ERROR", ex.getMessage());

        }
        return contactID_isExist;
    }
    public int editContact(ContactClass contact) {
        int isUpdate = -1;
        try {
            CallableStatement mystmt = conn.prepareCall("call edit_contact(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            mystmt.setInt(1, contact.getContactId());
            mystmt.setString(2, contact.getFirstName());
            mystmt.setString(3, contact.getLastName());
            mystmt.setString(4, contact.getHomePhone());
            mystmt.setString(5, contact.getWorkPhone());
            mystmt.setString(6, contact.getCellPhone());
            mystmt.setString(7, contact.getPlaceOfWork());
            mystmt.setString(8, contact.getEmail());
            mystmt.setInt(9, contact.getIsActive());
            mystmt.setInt(10, contact.getAddressId());
            isUpdate=mystmt.executeUpdate();


            return isUpdate;
        } catch (SQLException ex) {

            //Log.e("SQL ERROR", ex.getMessage());

        } catch (Exception ex) {
            Log.e("Other ERROR", ex.getMessage());

        }
        return isUpdate;
    }
    public ArrayList<ContactClass> fetchAllContacts() {
        int addressId = 0;
        ArrayList<ContactClass> contactList = new ArrayList<>();
        CallableStatement fetchStatement;
        try {
            fetchStatement = conn.prepareCall("call fetchAllContacts");
            ResultSet rs = fetchStatement.executeQuery();

            rs.first();
            do {
                ContactClass contact = new ContactClass();
                contact.setContactId(rs.getInt(1));
                contact.setAddressId(rs.getInt(2));//will add addressClass after - Phoebe
                contact.setFirstName(rs.getString(3));
                contact.setLastName(rs.getString(4));
                contact.setHomePhone(rs.getString(5));
                contact.setWorkPhone(rs.getString(6));
                contact.setCellPhone(rs.getString(7));
                contact.setPlaceOfWork(rs.getString(8));
                contact.setEmail(rs.getString(9));
                contactList.add(contact);
            } while (rs.next());

            return contactList;

        } catch (SQLException ex) {
            //Log.e("SQL error", ex.getMessage());

        }
        //only gets here if an error occurs
        return null;
    }
    public ArrayList<ContactClass> fetchContactsByStudentId (int id){
        ArrayList<ContactClass> contactList = new ArrayList<>();
        try {
            CallableStatement stmt;
            stmt = conn.prepareCall("call fetchContactsByStudentId("+id+")");
            ResultSet rs = stmt.executeQuery();
            rs.first();
            do {
                ContactClass contact = new ContactClass();

                FamilyRelationshipClass familyRelationship= new FamilyRelationshipClass();
                //int familyRelId = rs.getInt(1);
                familyRelationship.setFamilyRelId(rs.getInt(1));
                familyRelationship.setRelationship(rs.getString(2));
                familyRelationship.setIsEmergencyCOntact(rs.getInt(3));
                familyRelationship.setIsAuthorizedPickup(rs.getInt(4));
                familyRelationship.setIsActive(rs.getInt(5));

                contact.setFamilyRel(familyRelationship);

                contact.setAddressId(rs.getInt(6));//will add addressClass after - Phoebe
                contact.setFirstName(rs.getString(7));
                contact.setLastName(rs.getString(8));
                contact.setHomePhone(rs.getString(9));
                contact.setWorkPhone(rs.getString(10));
                contact.setCellPhone(rs.getString(11));
                contact.setPlaceOfWork(rs.getString(12));
                contact.setEmail(rs.getString(13));
                contact.setContactId(rs.getInt(14));

                contactList.add(contact);

            } while (rs.next());

            return contactList;
        } catch (SQLException ex) {
            //Log.e("SQL Contact ERROR", ex.getMessage());
        } catch (Exception ex) {
            Log.e("Other Contact ERROR", ex.getMessage());
        }
        return null;
    }
}
