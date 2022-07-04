package gr.upatras.Akinita;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class Owner extends Entity{
    private Integer afm;
    private String fName;
    private String lName;
    private String phone;

    /**
     * Constructor method for Owner class
     * @param fName		Owner's first name
     * @param lName		Owner's last name
     * @param afm		Owner's afm
     * @param phone		Owner's phone number
     */
    public Owner(Integer afm, String fName, String lName, String phone) {
        this.afm = afm;
        this.fName = fName;
        this.lName = lName;
        if (phone == null)
        	this.phone = phone;
        else this.phone = String.format("%010d",Long.parseLong(phone));
        
        fieldsMap = new HashMap<>();
        fieldsMap.put("afm", "afm");
        fieldsMap.put("fName", "fname");
        fieldsMap.put("lName", "lname");
        fieldsMap.put("phone", "phone");
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
        return fName;
    }

    /**
     * Setter function for fname param
     * @param fName
     */
    public void setFName(String fName) {
        this.fName = fName;
    }

    /**
     * Getter function for lname param
     * @return
     */
    public String getLName() {
        return lName;
    }

    /**
     * Setter function for lname param
     * @param lName
     */
    public void setLName(String lName) {
        this.lName = lName;
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
                Integer.parseInt(rs.getString("AFM")),
                rs.getString("Fname"),
                rs.getString("Lname"),
                rs.getString("Phone"));
    }
}

