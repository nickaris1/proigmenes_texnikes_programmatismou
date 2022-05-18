package gr.upatras.Akinita;

import gr.upatras.Database.DatabaseAccess;
import gr.upatras.Database.DatabaseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class PropertyController {
    private static final Logger log = LoggerFactory.getLogger(PropertyController.class);

    @RequestMapping(value = "/properties/", produces = {"application/json;charset=utf-8"}, method = RequestMethod.GET)
    public List<Property> getProperties() {
        log.info("Getting all properties");
        final List<Property>[] properties = new List[]{null};

        DatabaseAccess.get("PROPERTY", (rs, primaryKeys) -> {
            properties[0] = getPropertyList(rs, primaryKeys);
        });
        return properties[0];
    }

    @RequestMapping(value = "/properties/{id}", produces = {"application/json;charset=utf-8"}, method = RequestMethod.GET)
    public List<Property> getProperty(@PathVariable("id") int id) {
        log.info("Getting property¨" + id);
        final List<Property>[] properties = new List[]{null};

        DatabaseAccess.getById("PROPERTY", id, (rs, primaryKeys) -> {
            properties[0] = getPropertyList(rs, primaryKeys);
        });

        return properties[0];
    }

    @RequestMapping(value = "/properties/", produces = {"application/json;charset=utf-8"}, consumes = {"application/json;charset=utf-8"}, method = RequestMethod.POST)
    public ResponseEntity<Property> createLocation(@RequestBody Property property) {
        log.info("Will add a new property");
        String q = DatabaseUtil.queryInsertParamCreator(property);
        boolean res = DatabaseAccess.addEntry("PROPERTY", q);

        if (res) {
            return new ResponseEntity<>(property, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(property, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/properties/search", produces = {"application/json;charset=utf-8"}, consumes = {"application/json;charset=utf-8"}, method = RequestMethod.POST)
    public List<Property> searchProperty(@RequestBody Property property) {
        final List<Property>[] properties = new List[]{null};
        String query = DatabaseUtil.querySearchParamCreator(property);
        log.info(query);
        DatabaseAccess.getCustom("PROPERTY", query, ((rs, primaryKeys) -> {
            properties[0] = getPropertyList(rs, primaryKeys);
        }));
        return properties[0];
    }

    @RequestMapping(value = "/properties/{id}", produces = {"application/json;charset=utf-8"}, method = RequestMethod.DELETE)
    public ResponseEntity<Void> deletePropertyById(@PathVariable("id") int id) {
        boolean res = DatabaseAccess.deleteEntry("PROPERTY", id);
        if (res) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/properties/{id}", produces = {"application/json;charset=utf-8"}, consumes = {"application/json;charset=utf-8"}, method = RequestMethod.PATCH)
    ResponseEntity<Property> updateProperty(@RequestBody Property body, @PathVariable("id") int id) {
        Boolean[] res = {null};
        final List<Property>[] fProperties = new List[]{null};

        DatabaseAccess.getById("PROPERTY", id, ((rs, primaryKeys) -> {
            List<Property> properties = getPropertyList(rs, primaryKeys); // if entry exists
            fProperties[0] = properties;
            if (properties.isEmpty()) {
                res[0] = false;
            } else {
                String query = DatabaseUtil.queryUpdateParamCreator(body);
                res[0] = DatabaseAccess.updateEntry("PROPERTY", id, query);
                if (res[0]) {
                    int tempId = ((body.getId() == null) || (body.getId() == id) ? id : body.getId());

                    DatabaseAccess.getById("PROPERTY", tempId, ((rs2, primaryKeys2) -> {
                        fProperties[0] = getPropertyList(rs2, primaryKeys2);
                    }));
                }
            }
        }));

        if (fProperties[0] != null) {
            if (fProperties[0].size() > 0) {
                return new ResponseEntity<>(fProperties[0].get(0), ((res[0]) ? HttpStatus.OK : HttpStatus.BAD_REQUEST));
            } else {
                return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
        }
    }

    private List<Property> getPropertyList(ResultSet rs, List<String> primaryKeys) {
        List<Property> properties = new ArrayList<>();
        try {
            while (rs.next()) {
                Property newProperty = new Property(Integer.parseInt(rs.getString("Id")), Integer.parseInt(rs.getString("Listed_price")), Integer.parseInt(rs.getString("Tm")), Integer.parseInt(rs.getString("Type")), 
                		Integer.parseInt("Road"), Integer.parseInt("Address_num"), Integer.parseInt("Floor"), 
                		Boolean.parseBoolean("Availability"), Integer.parseInt("Owner_afm"), Integer.parseInt("Area_code"));
                properties.add(newProperty);
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return properties;
    }
}

