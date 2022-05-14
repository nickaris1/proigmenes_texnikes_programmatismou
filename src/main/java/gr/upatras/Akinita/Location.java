package gr.upatras.Akinita;

/**
 *
 */
public class Location {
    private String city;
    private String area;
    private String county;
    private int postCode;


    public Location() {}

    public Location(String city, String area, String county, int postCode) {
        this.city = city;
        this.area = area;
        this.county = county;
        this.postCode = postCode;
    }


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public int getPostCode() {
        return postCode;
    }

    public void setPostCode(int postCode) {
        this.postCode = postCode;
    }
}
