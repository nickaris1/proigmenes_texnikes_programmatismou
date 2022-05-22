package gr.upatras.Akinita;

import java.util.HashMap;

public class Owner extends Entity{
    private String FName;
    private String LName;
    private Integer afm;
    private Integer phone;


    public Owner(String FName, String LName, Integer afm, Integer phone) {
        this.FName = FName;
        this.LName = LName;
        this.afm = afm;
        this.phone = phone;
        
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

    public Integer getPhone() {
        return phone;
    }

    public void setPhone(Integer phone) {
        this.phone = phone;
    }
}
