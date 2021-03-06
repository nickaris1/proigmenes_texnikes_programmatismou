package gr.upatras.Akinita;

import java.util.HashMap;

/**
* Custom search entity.
*/

public class CustomSearch extends Entity{
	private Location location;
    private Owner owner;
    private Property property;
    private Sale sale;
    private Modifiers modifiers;


	/**
	 * Constructor method for CustomSearch class
	 * @param location		location of the property
	 * @param owner			owner of the property
	 * @param property		property
	 * @param sale			sale object
	 * @param modifiers     modifiers object
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
    

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public Sale getSale() {
        return sale;
    }


    public void setSale(Sale sale) {
        this.sale = sale;
    }

    


    public Modifiers getModifiers() {
        return modifiers;
    }

    public void setModifiers(Modifiers modifiers) {
        this.modifiers = modifiers;
    }

}
