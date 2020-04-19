package com.example.eceapp.businessLayer;

import com.example.eceapp.AddressClass;
import com.example.eceapp.dataLayer.AddressDL;

public class AddressBL {
    AddressDL addressDL = AddressDL.getInstance();
    public int[] addAddress(AddressClass address){
        return addressDL.addAddress(address);
    }
    public int editAddress(AddressClass address){
        return addressDL.editAddress(address);
    }
    public AddressClass getAddressById(int id){
        return addressDL.fetchAddressById(id);
    }
}
