package gr.upatras.Akinita;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.HashMap;

public class Modifiers extends Entity{
    private static final Logger log = LoggerFactory.getLogger(Modifiers.class);
    private String locationPostCode;
    private String locationCity;
    private String locationCounty;
    private String locationArea;
    private String ownerFName;
    private String ownerLName;
    private String ownerAfm;
    private String ownerPhone;
    private String propertyId;
    private String propertyListedPrice;
    private String propertyTm;
    private String propertyType;
    private String propertyRoad;
    private String propertyAddressNum;
    private String propertyFloor;
    private String propertyAvailability;
    private String propertyOwnerAfm;
    private String propertyAreaCode;
    private String saleID;
    private String saleDate;
    private String salePrice;
    private String saleTM;
    private String saleRental;
    private String saleWarranty;
    private String saleStartDate;
    private String saleEndDate;
    private String salePropId;

    private void init() {
        for (Field field : this.getClass().getDeclaredFields()) {
            try {
                if (field.get(this) == null) {
                    if (String.valueOf(field.getType()).equals("class java.lang.String")) {
                        field.set(this, "=");
                    } else {
                        field.set(this, true);
                    }
                }
            } catch (IllegalAccessException e) {
                log.error(e.getMessage());
            }
        }

        this.fieldsMap = new HashMap<>();
        this.fieldsMap.put("city", "locationCity");
        this.fieldsMap.put("area", "locationPostCode");
        this.fieldsMap.put("county", "locationCounty");
        this.fieldsMap.put("postCode", "locationPostCode");
        this.fieldsMap.put("FName", "ownerFName");
        this.fieldsMap.put("LName", "ownerLName");
        this.fieldsMap.put("afm", "ownerAfm");
        this.fieldsMap.put("phone", "ownerPhone");
        this.fieldsMap.put("id", "propertyId");
        this.fieldsMap.put("listedPrice", "propertyListedPrice");
        this.fieldsMap.put("tm", "propertyTm");
        this.fieldsMap.put("type", "propertyType");
        this.fieldsMap.put("road", "propertyRoad");
        this.fieldsMap.put("addressNum", "propertyAddressNum");
        this.fieldsMap.put("floor", "propertyFloor");
        this.fieldsMap.put("availability", "propertyAvailability");
        this.fieldsMap.put("ownerAfm", "propertyOwnerAfm");
        this.fieldsMap.put("areaCode", "propertyAreaCode");
        this.fieldsMap.put("saleID", "saleID");
        this.fieldsMap.put("Date", "saleDate");
        this.fieldsMap.put("Price", "salePrice");
        this.fieldsMap.put("TM", "saleTM");
        this.fieldsMap.put("Rental", "saleRental");
        this.fieldsMap.put("Warranty", "saleWarranty");
        this.fieldsMap.put("StartDate", "saleStartDate");
        this.fieldsMap.put("EndDate", "saleEndDate");
        this.fieldsMap.put("PropId", "salePropId");
    }

    public Modifiers() { init(); }
    public Modifiers(String locationPostCode, String locationCity, String locationArea, String locationCounty, String ownerFName, String ownerLName, String ownerAfm, String ownerPhone, String propertyId, String propertyListedPrice, String propertyTm, String propertyType, String propertyRoad, String propertyAddressNum, String propertyFloor, String propertyAvailability, String propertyOwnerAfm, String propertyAreaCode, String saleID, String saleDate, String salePrice, String saleTM, String saleRental, String saleWarranty, String saleStartDate, String saleEndDate, String salePropId) {
        this.locationPostCode = locationPostCode;
        this.locationCity = locationCity;
        this.locationArea = locationArea;
        this.locationCounty = locationCounty;
        this.ownerFName = ownerFName;
        this.ownerLName = ownerLName;
        this.ownerAfm = ownerAfm;
        this.ownerPhone = ownerPhone;
        this.propertyId = propertyId;
        this.propertyListedPrice = propertyListedPrice;
        this.propertyTm = propertyTm;
        this.propertyType = propertyType;
        this.propertyRoad = propertyRoad;
        this.propertyAddressNum = propertyAddressNum;
        this.propertyFloor = propertyFloor;
        this.propertyAvailability = propertyAvailability;
        this.propertyOwnerAfm = propertyOwnerAfm;
        this.propertyAreaCode = propertyAreaCode;
        this.saleID = saleID;
        this.saleDate = saleDate;
        this.salePrice = salePrice;
        this.saleTM = saleTM;
        this.saleRental = saleRental;
        this.saleWarranty = saleWarranty;
        this.saleStartDate = saleStartDate;
        this.saleEndDate = saleEndDate;
        this.salePropId = salePropId;

        init();
    }

    public String getLocationPostCode() {
        return locationPostCode;
    }

    public void setLocationPostCode(String locationPostCode) {
        this.locationPostCode = locationPostCode;
    }

    public String getLocationCity() {
        return locationCity;
    }

    public void setLocationCity(String locationCity) {
        this.locationCity = locationCity;
    }

    public String getLocationArea() {
        return locationArea;
    }

    public void setLocationArea(String locationArea) {
        this.locationArea = locationArea;
    }

    public String getLocationCounty() {
        return locationCounty;
    }

    public void setLocationCounty(String locationCounty) {
        this.locationCounty = locationCounty;
    }

    public String getOwnerFName() {
        return ownerFName;
    }

    public void setOwnerFName(String ownerFName) {
        this.ownerFName = ownerFName;
    }

    public String getOwnerLName() {
        return ownerLName;
    }

    public void setOwnerLName(String ownerLName) {
        this.ownerLName = ownerLName;
    }

    public String getOwnerAfm() {
        return ownerAfm;
    }

    public void setOwnerAfm(String ownerAfm) {
        this.ownerAfm = ownerAfm;
    }

    public String getOwnerPhone() {
        return ownerPhone;
    }

    public void setOwnerPhone(String ownerPhone) {
        this.ownerPhone = ownerPhone;
    }

    public String getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }

    public String getPropertyListedPrice() {
        return propertyListedPrice;
    }

    public void setPropertyListedPrice(String propertyListedPrice) {
        this.propertyListedPrice = propertyListedPrice;
    }

    public String getPropertyTm() {
        return propertyTm;
    }

    public void setPropertyTm(String propertyTm) {
        this.propertyTm = propertyTm;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public String getPropertyRoad() {
        return propertyRoad;
    }

    public void setPropertyRoad(String propertyRoad) {
        this.propertyRoad = propertyRoad;
    }

    public String getPropertyAddressNum() {
        return propertyAddressNum;
    }

    public void setPropertyAddressNum(String propertyAddressNum) {
        this.propertyAddressNum = propertyAddressNum;
    }

    public String getPropertyFloor() {
        return propertyFloor;
    }

    public void setPropertyFloor(String propertyFloor) {
        this.propertyFloor = propertyFloor;
    }

    public String getPropertyAvailability() {
        return propertyAvailability;
    }

    public void setPropertyAvailability(String propertyAvailability) {
        this.propertyAvailability = propertyAvailability;
    }

    public String getPropertyOwnerAfm() {
        return propertyOwnerAfm;
    }

    public void setPropertyOwnerAfm(String propertyOwnerAfm) {
        this.propertyOwnerAfm = propertyOwnerAfm;
    }

    public String getPropertyAreaCode() {
        return propertyAreaCode;
    }

    public void setPropertyAreaCode(String propertyAreaCode) {
        this.propertyAreaCode = propertyAreaCode;
    }

    public String getSaleID() {
        return saleID;
    }

    public void setSaleID(String saleID) {
        this.saleID = saleID;
    }

    public String getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(String saleDate) {
        this.saleDate = saleDate;
    }

    public String getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(String salePrice) {
        this.salePrice = salePrice;
    }

    public String getSaleTM() {
        return saleTM;
    }

    public void setSaleTM(String saleTM) {
        this.saleTM = saleTM;
    }

    public String getSaleRental() {
        return saleRental;
    }

    public void setSaleRental(String saleRental) {
        this.saleRental = saleRental;
    }

    public String getSaleWarranty() {
        return saleWarranty;
    }

    public void setSaleWarranty(String saleWarranty) {
        this.saleWarranty = saleWarranty;
    }

    public String getSaleStartDate() {
        return saleStartDate;
    }

    public void setSaleStartDate(String saleStartDate) {
        this.saleStartDate = saleStartDate;
    }

    public String getSaleEndDate() {
        return saleEndDate;
    }

    public void setSaleEndDate(String saleEndDate) {
        this.saleEndDate = saleEndDate;
    }

    public String getSalePropId() {
        return salePropId;
    }

    public void setSalePropId(String salePropId) {
        this.salePropId = salePropId;
    }
}
