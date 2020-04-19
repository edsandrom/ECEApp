package com.example.eceapp.dataLayer;

import android.util.Log;

import com.example.eceapp.ConnectionClass;
import com.example.eceapp.MedicalHistory;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class MedicalHistoryDL {
    private static MedicalHistoryDL instance;
    private static Connection conn;
    public static MedicalHistoryDL getInstance(){
        if(instance==null){
            ConnectionClass connection = new ConnectionClass();
            instance = new MedicalHistoryDL();
            conn=connection.Connect();
            //System.out.println(conn);
        }
        return instance;
    }
    public int addMedicalHistory(MedicalHistory m){
        int mediId =0;
        try{
            CallableStatement stmt=conn.prepareCall("Call add_medical_history(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            stmt.setInt(1, m.getMedicationId());
            stmt.setInt(2, m.getMeasles());
            stmt.setInt(3, m.getMumps());
            stmt.setInt(4, m.getMeningitis());
            stmt.setInt(5, m.getRubella());
            stmt.setInt(6, m.getChicken_pox());
            stmt.setInt(7, m.getPertussis());
            stmt.setInt(8, m.getAsthma());
            stmt.setInt(9, m.getEczema());
            stmt.setInt(10, m.getDiabetes());
            stmt.setInt(11, m.getEpilepsy());
            stmt.setString(12, m.getOther1());
            stmt.setString(13, m.getRestricted_activities());
            stmt.setString(14, m.getDietary_restrictions());
            stmt.registerOutParameter(15, Types.INTEGER);

            stmt.executeUpdate();
            mediId=stmt.getInt(15);
            return mediId;
        }catch(SQLException ex){
            //Log.e("SQL ERROR", ex.getMessage());
        }catch(Exception ex){
            Log.e("Other error: ", ex.getMessage());
        }
        return mediId;
    }
    public int editMedicalHistory(MedicalHistory m) {
        int isUpdate = -1;
        try {
            CallableStatement stmt=conn.prepareCall("Call edit_medical_history(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            stmt.setInt(1, m.getMedical_history_id());
            stmt.setInt(2, m.getMeasles());
            stmt.setInt(3, m.getMumps());
            stmt.setInt(4, m.getMeningitis());
            stmt.setInt(5, m.getRubella());
            stmt.setInt(6, m.getChicken_pox());
            stmt.setInt(7, m.getPertussis());
            stmt.setInt(8, m.getAsthma());
            stmt.setInt(9, m.getEczema());
            stmt.setInt(10, m.getDiabetes());
            stmt.setInt(11, m.getEpilepsy());
            stmt.setString(12, m.getOther1());
            stmt.setString(13, m.getRestricted_activities());
            stmt.setString(14, m.getDietary_restrictions());
            isUpdate=stmt.executeUpdate();
            return isUpdate;
        } catch (SQLException ex) {
            //Log.e("SQL MHistory ERROR", ex.getMessage());
        } catch (Exception ex) {
            Log.e("Other MHistory ERROR", ex.getMessage());
        }
        return isUpdate;
    }
    public MedicalHistory fetchMedicalHistoryById (int id){
        try {
            CallableStatement stmt;
            stmt = conn.prepareCall("call fetchMedicalHistoryById("+id+")");
            ResultSet rs = stmt.executeQuery();
            rs.first();
            MedicalHistory n = new MedicalHistory();
            n.setMedical_history_id(rs.getInt(1));
            n.setProofId(rs.getInt(2));
            n.setMedicationId(rs.getInt(3));
            n.setMeasles(rs.getInt(4));
            n.setMumps(rs.getInt(5));
            n.setMeningitis(rs.getInt(6));
            n.setRubella(rs.getInt(7));
            n.setChicken_pox(rs.getInt(8));
            n.setPertussis(rs.getInt(9));
            n.setAsthma(rs.getInt(10));
            n.setEczema(rs.getInt(11));
            n.setDiabetes(rs.getInt(12));
            n.setEpilepsy(rs.getInt(13));
            n.setOther1(rs.getString(14));
            n.setOther2(rs.getString(15));
            n.setRestricted_activities(rs.getString(16));
            n.setDietary_restrictions(rs.getString(17));

            //System.out.println(n);
            return n;
        } catch (SQLException ex) {
            //Log.e("SQL MHistory ERROR", ex.getMessage());
        } catch (Exception ex) {
            Log.e("Other MHistory ERROR", ex.getMessage());
        }
        return null;
    }
}
