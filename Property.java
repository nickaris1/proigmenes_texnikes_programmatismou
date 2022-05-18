package gr.upatras.Akinita;

import java.util.HashMap;

public class Property extends Entity {

	private Integer id;
	private Integer listedPrice;
	private Integer tm;
	private Integer type;
	private Integer road;
	private Integer addressNum;
	private Integer floor;
	private Boolean availability;
	private Integer ownerAfm;
	private Integer areaCode;

	public Property(Integer id, Integer listedPrice, Integer tm, Integer type, Integer road, Integer addressNum, 
			Integer floor, Boolean availability, Integer ownerAfm, Integer areaCode) {
		
		this.setId(id);
		this.setListedPrice(listedPrice);
		this.setTm(tm);
		this.setType(type);
		this.setRoad(road);
		this.setAddressNum(addressNum);
		this.setFloor(floor);
		this.setAvailability(availability);
		this.setOwnerAfm(ownerAfm);
		this.setAreaCode(areaCode);
		
		fieldsMap = new HashMap<>();
        fieldsMap.put("id", "Id");
        fieldsMap.put("listedPrice", "Listed_price");
        fieldsMap.put("tm", "Tm");
        fieldsMap.put("type", "Type");
        fieldsMap.put("road", "Road");
        fieldsMap.put("addressNum", "Address_num");
        fieldsMap.put("floor", "Floor");
        fieldsMap.put("availability", "Availability");
        fieldsMap.put("ownerAfm", "Owner_afm");
        fieldsMap.put("areaCode", "Area_code");
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getListedPrice() {
		return listedPrice;
	}

	public void setListedPrice(Integer listedPrice) {
		this.listedPrice = listedPrice;
	}

	public Integer getTm() {
		return tm;
	}

	public void setTm(Integer tm) {
		this.tm = tm;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getRoad() {
		return road;
	}

	public void setRoad(Integer road) {
		this.road = road;
	}

	public Integer getAddressNum() {
		return addressNum;
	}

	public void setAddressNum(Integer addressNum) {
		this.addressNum = addressNum;
	}

	public Integer getFloor() {
		return floor;
	}

	public void setFloor(Integer floor) {
		this.floor = floor;
	}

	public Boolean getAvailability() {
		return availability;
	}

	public void setAvailability(Boolean availability) {
		this.availability = availability;
	}

	public Integer getOwnerAfm() {
		return ownerAfm;
	}

	public void setOwnerAfm(Integer ownerAfm) {
		this.ownerAfm = ownerAfm;
	}

	public Integer getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(Integer areaCode) {
		this.areaCode = areaCode;
	}
}
