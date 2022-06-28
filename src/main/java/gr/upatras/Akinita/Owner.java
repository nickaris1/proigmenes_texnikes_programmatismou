package gr.upatras.Akinita;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class Owner extends Entity{
    private String FName;
    private String LName;
    private Integer afm;
    private String phone;

    /**
     * Constructor method for Owner class
     * @param FName		Owner's first name
     * @param LName		Owner's last name
     * @param afm		Owner's afm
     * @param phone		Owner's phone number
     */
    public Owner(String FName, String LName, Integer afm, String phone) {
        this.FName = FName;
        this.LName = LName;
        this.afm = afm;
        this.phone = String.format("%010d",Long.parseLong(phone));
        
        fieldsMap = new HashMap<>();
        fieldsMap.put("afm", "AFM");
        fieldsMap.put("FName", "Fname");
        fieldsMap.put("LName", "Lname");
        fieldsMap.put("phone", "Phone");
    }
    
    /**
     * Getter function for afm param
     * @return
     */
    public Integer getAFM() {
        return afm;
    }

    /**
     * Setter function for afm param
     * @param afm
     */
    public void setAFM(Integer afm) {
        this.afm = afm;
    }

    /**
     * Getter function for fname param
     * @return
     */
    public String getFName() {
        return FName;
    }

    /**
     * Setter function for fname param
     * @param FName
     */
    public void setFName(String FName) {
        this.FName = FName;
    }

    /**
     * Getter function for lname param
     * @return
     */
    public String getLName() {
        return LName;
    }

    /**
     * Setter function for lname param
     * @param LName
     */
    public void setLName(String LName) {
        this.LName = LName;
    }

    /**
     * Getter function for phone param
     * @return
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Setter function for phone param
     * @param phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Creates an owner from a given ResultSet param
     * @param rs
     * @return
     * @throws SQLException
     */
    public static Owner createOwnerFromResultSet(ResultSet rs) throws SQLException {
        return new Owner(
                rs.getString("Fname"),
                rs.getString("Lname"),
                Integer.parseInt(rs.getString("AFM")),
                rs.getString("Phone"));
    }
}

