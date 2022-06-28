package gr.upatras.Akinita;

import java.util.HashMap;

public class CustomSearch extends Entity{
	
	/**
	 * Constructor method for CustomSearch class
	 * @param location		The location of the property
	 * @param owner			The owner of the property
	 * @param property		The property
	 * @param sale			
	 * @param modifiers
	 */
    public CustomSearch(Location location, Owner owner, Property property, Sale sale, Modifiers modifiers) {
        this.location = location;
        this.owner = owner;
        this.property = property;
        this.sale = sale;
        this.modifiers = modifiers;

        if (modifiers == null) {
            this.modifiers = new Modifiers(null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null);
        }

        this.fieldsMap = new HashMap<>();

        this.fieldsMap.put("location", "LOCATION as l");
        this.fieldsMap.put("owner", "OWNER as o");
        this.fieldsMap.put("property", "PROPERTY as p");
        this.fieldsMap.put("sale", "SALE as s");

    }
    
    /**
     * Getter function for location param
     */
    public Location getLocation() {
        return location;
    }

    /** 
     * Setter function for location param
     * @param location
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * Getter function for owner param
     */
    public Owner getOwner() {
        return owner;
    }

    /**
     * Setter function for owner param
     * @param owner
     */
    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    /**
     * Getter function for property param
     */
    public Property getProperty() {
        return property;
    }

    /**
     * Setter function for property param
     * @param property
     */
    public void setProperty(Property property) {
        this.property = property;
    }

    /**
     * Getter function for sale param
     */
    public Sale getSale() {
        return sale;
    }

    /**
     * Setter function for sale param
     * @param sale
     */
    public void setSale(Sale sale) {
        this.sale = sale;
    }

    private Location location;
    private Owner owner;
    private Property property;
    private Sale sale;

    /**
     * Getter function for modifiers param
     */
    public Modifiers getModifiers() {
        return modifiers;
    }

    /**
     * Setter function for modifiers param
     * @param modifiers
     */
    public void setModifiers(Modifiers modifiers) {
        this.modifiers = modifiers;
    }

    private Modifiers modifiers;
}
