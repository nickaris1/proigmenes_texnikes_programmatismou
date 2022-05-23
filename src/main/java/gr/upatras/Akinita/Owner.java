package gr.upatras.Akinita;

import java.util.HashMap;

public class Owner extends Entity{
    private String FName;
    private String LName;
    private Integer afm;
    private String phone;


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
    
    public Integer getAFM() {
        return afm;
    }

    public void setAFM(Integer afm) {
        this.afm = afm;
    }

    public String getFName() {
        return FName;
    }

    public void setFName(String FName) {
        this.FName = FName;
    }

    public String getLName() {
        return LName;
    }

    public void setLName(String LName) {
        this.LName = LName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
