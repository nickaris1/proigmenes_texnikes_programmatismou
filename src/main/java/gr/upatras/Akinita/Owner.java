package gr.upatras.Akinita;

public class Owner {
    private String FName;
    private String LName;
    private int AFM;
    private String address;
    private int phone;

    public Owner() {}

    public Owner(String FName, String LName, int AFM, String address, int phone) {
        this.FName = FName;
        this.LName = LName;
        this.AFM = AFM;
        this.address = address;
        this.phone = phone;
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

    public int getAFM() {
        return AFM;
    }

    public void setAFM(int AFM) {
        this.AFM = AFM;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }
}
