package gr.upatras.Akinita;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class Sale extends Entity{
	private Integer saleID;
	private String date;
	private String price;
	private String tm;
	private Boolean rental;
	private Integer warranty;
	private String startDate;
	private String endDate;
	private Integer propId;

	/**
     * Constructor method for Sale class
     * @param saleID		Sale's id
     * @param date			Sale's date
     * @param price			Sale's price
     * @param tm			Sale's area in square meters
	 * @param rental		Sale is a Rental instead
	 * @param warranty		Rental's amount paid in advance
	 * @param startDate		Rental's starting date
	 * @param endDate		Rental's ending date
	 * @param propId		The id of the property the sale relates to
     */
	
	public Sale(Integer saleID, String date, String price, String tm, Boolean rental, Integer warranty,
				String startDate, String endDate, Integer propId) {
		this.saleID = saleID;
		this.date = date;
		this.price = price;
		this.tm = tm;
		this.rental = rental;
		this.warranty = warranty;
		this.startDate = startDate;
		this.endDate = endDate;
		this.propId = propId;
		
		fieldsMap = new HashMap<>();
		fieldsMap.put("saleID", "Id");
		fieldsMap.put("date", "Date");
		fieldsMap.put("price", "Price");
		fieldsMap.put("tm", "TM");
		fieldsMap.put("rental", "Rental");
		fieldsMap.put("warranty", "Warranty");
		fieldsMap.put("startDate", "StartDate");
		fieldsMap.put("endDate", "Enddate");
		fieldsMap.put("propId", "Property_id");
		
	}
	
	public Integer getSaleID() {
		return saleID;
	}
	
	public void setSaleID(Integer saleID) {
		this.saleID = saleID;
	}
	
	public String getDate() {
		return date;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	public String getPrice() {
		return price;
	}
	
	public void setPrice(String price) {
		this.price = price;
	}
	public String getTM() {
		return tm;
	}
	
	public void setTM(String tm) {
		this.tm = tm;
	}
	public Boolean getRental() {
		return rental;
	}
	
	public void setRental(Boolean rental) {
		this.rental = rental;
	}
	public Integer getWarranty() {
		return warranty;
	}
	
	public void setWarranty(Integer warranty) {
		this.warranty = warranty;
	}
	public String getStartDate() {
		return startDate;
	}
	
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	public Integer getPropId() {
		return propId;
	}
	
	public void setPropId(Integer propId) {
		this.propId = propId;
	}


	/**
	 * Create Sale Object from database
	 * @param rs ResultSet from database
	 * @return Sale object create from database query
	 * @throws SQLException
	 */
	public static Sale createSaleFromResultSet(ResultSet rs) throws SQLException {
		return new Sale(
				Integer.parseInt(rs.getString("id")),
				rs.getString("date"),
				rs.getString("price"),
				rs.getString("tm"),
				Util.boolResultOrNull(rs.getString("rental")),
				Util.intResultOrNull(rs.getString("warranty")),
				rs.getString("startdate"),
				rs.getString("enddate"),
				Integer.parseInt(rs.getString("property_id")));
	}

}




