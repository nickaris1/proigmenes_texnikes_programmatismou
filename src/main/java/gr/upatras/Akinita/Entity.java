package gr.upatras.Akinita;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Map;

public class Entity {
    @JsonIgnore
    public transient Map<String, String> fieldsMap;

    @JsonIgnore
    public Map<String, String> getFieldsMap() {
        return fieldsMap;
    }

    public void setFieldsMap(Map<String, String> fieldsMap) {
        this.fieldsMap = fieldsMap;
    }
}
