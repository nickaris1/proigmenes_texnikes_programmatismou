package gr.upatras.Akinita;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

/**
 *
 */
public class Location extends Entity {
    private String city;
    private String area;
    private String county;
    private Integer postCode;


//    public Location() {}

    public Location(String city, String area, String county, Integer postCode) {
        this.city = city;
        this.area = area;
        this.county = county;
        this.postCode = postCode;

        fieldsMap = new HashMap<>();
        fieldsMap.put("city", "City");
        fieldsMap.put("postCode", "Area_code");
        fieldsMap.put("county", "County");
        fieldsMap.put("area", "Area");
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

    public Integer getPostCode() {
        return postCode;
    }

    public void setPostCode(Integer postCode) {
        this.postCode = postCode;
    }


    public static Location createLocationFromResultSet(ResultSet rs) throws SQLException {
        return new Location(rs.getString("City"), rs.getString("Area"), rs.getString("County"), Integer.parseInt(rs.getString("Area_code")));
    }
}
