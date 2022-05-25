package gr.upatras.Akinita;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class Sale extends Entity{
	private Integer saleID;
	private String Date;
	private String Price;
	private String TM;
	private Boolean Rental;
	private Integer Warranty;
	private String StartDate;
	private String EndDate;
	private Integer PropId;
	
	public Sale(Integer saleID, String Date, String Price, String TM, Boolean Rental, Integer Warranty,
				String StartDate, String EndDate, Integer PropId) {
		this.saleID = saleID;
		this.Date = Date;
		this.Price = Price;
		this.TM = TM;
		this.Rental = Rental;
		this.Warranty = Warranty;
		this.StartDate = StartDate;
		this.EndDate = EndDate;
		this.PropId = PropId;
		
		fieldsMap = new HashMap<>();
		fieldsMap.put("saleID", "id");
		fieldsMap.put("Date", "date");
		fieldsMap.put("Price", "price");
		fieldsMap.put("TM", "tm");
		fieldsMap.put("Rental", "rental");
		fieldsMap.put("Warranty", "warranty");
		fieldsMap.put("StartDate", "startdate");
		fieldsMap.put("EndDate", "enddate");
		fieldsMap.put("PropId", "property_id");
		
	}
	
	public Integer getSaleID() {
		return saleID;
	}
	
	public void setSaleID(Integer saleID) {
		this.saleID = saleID;
	}
	
	public String getDate() {
		return Date;
	}
	
	public void setDate(String Date) {
		this.Date = Date;
	}
	public String getPrice() {
		return Price;
	}
	
	public void setPrice(String Price) {
		this.Price = Price;
	}
	public String getTM() {
		return TM;
	}
	
	public void setTM(String TM) {
		this.TM = TM;
	}
	public Boolean getRental() {
		return Rental;
	}
	
	public void setRental(Boolean Rental) {
		this.Rental = Rental;
	}
	public Integer getWarranty() {
		return Warranty;
	}
	
	public void setWarranty(Integer Warranty) {
		this.Warranty = Warranty;
	}
	public String getStartDate() {
		return StartDate;
	}
	
	public void setStartDate(String StartDate) {
		this.StartDate = StartDate;
	}
	public String getEndDate() {
		return EndDate;
	}
	
	public void setEndDate(String EndDate) {
		this.EndDate = EndDate;
	}
	
	public Integer getPropId() {
		return PropId;
	}
	
	public void setPropId(Integer PropId) {
		this.PropId = PropId;
	}


	public static Sale createSaleFromResultSet(ResultSet rs) throws SQLException {
		return new Sale(
				Integer.parseInt(rs.getString("ID")),
				rs.getString("Date"),
				rs.getString("Price"),
				rs.getString("TM"),
				Util.boolResultOrNull(rs.getString("Rental")),
				Util.intResultOrNull(rs.getString("Warranty")),
				rs.getString("StartDate"),
				rs.getString("EndDate"),
				Integer.parseInt(rs.getString("Property_id")));
	}

}




