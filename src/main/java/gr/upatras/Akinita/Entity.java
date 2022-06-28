package gr.upatras.Akinita;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Map;

public class Entity {

    @JsonIgnore
    public transient Map<String, String> fieldsMap;

    /**
     * Getter function for FieldsMap param
     * @return
     */
    @JsonIgnore
    public Map<String, String> getFieldsMap() {
        return fieldsMap;
    }

    /**
     * Setter function for FieldsMap param
     * @param fieldsMap
     */
    public void setFieldsMap(Map<String, String> fieldsMap) {
        this.fieldsMap = fieldsMap;
    }
}
