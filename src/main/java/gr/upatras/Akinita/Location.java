package gr.upatras.Akinita;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class Location extends Entity {
    private String city;
    private String area;
    private String county;
    private Integer postCode;



    /**
     * Constructor method for Location class
     * @param city		The city that the property is located
     * @param area		The area that the property is located
     * @param county	The country that the property is located
     * @param postCode	The postCode of the location
     */
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

    /**
     * Getter function for city param
     */
    public String getCity() {
        return city;
    }

    /**
     * Setter function for city param
     * @param city
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Getter function for area param
     * @return
     */
    public String getArea() {
        return area;
    }

    /**
     * Setter function for area param
     */
    public void setArea(String area) {
        this.area = area;
    }

    /**
     * Getter function for country param
     */
    public String getCounty() {
        return county;
    }

    /**
     * Getter function for country param
     */
    public void setCounty(String county) {
        this.county = county;
    }

    /**
     * Getter function for postCode param
     * @return
     */
    public Integer getPostCode() {
        return postCode;
    }

    /**
     * Getter function for postCode param
     * @param postCode Integer
     */
    public void setPostCode(Integer postCode) {
        this.postCode = postCode;
    }

    /**
	 * Create Location Object from database
	 * @param rs ResultSet from database
	 * @return Location object create from database query
	 * @throws SQLException
	 */
    public static Location createLocationFromResultSet(ResultSet rs) throws SQLException {
        return new Location(rs.getString("City"), rs.getString("Area"), rs.getString("County"), Integer.parseInt(rs.getString("Area_code")));
    }
}
