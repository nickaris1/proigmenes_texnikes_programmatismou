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
import java.util.concurrent.atomic.AtomicReference;


@RestController
public class PropertyController {
    private static final Logger log = LoggerFactory.getLogger(PropertyController.class);

    @RequestMapping(value = "/properties/", produces = {"application/json;charset=utf-8"}, method = RequestMethod.GET)
    public List<Property> getProperties() {
        log.info("Getting all properties");
        AtomicReference<List<Property>> properties = new AtomicReference<>();

        DatabaseAccess.get("PROPERTY", (rs, primaryKeys) -> properties.set(getPropertyList(rs, primaryKeys)));
        return properties.get();
    }

    @RequestMapping(value = "/properties/{id}", produces = {"application/json;charset=utf-8"}, method = RequestMethod.GET)
    public List<Property> getProperty(@PathVariable("id") int id) {
        log.info("Getting property¨" + id);
        AtomicReference<List<Property>> properties = new AtomicReference<>();

        DatabaseAccess.getById("PROPERTY", id, (rs, primaryKeys) -> properties.set(getPropertyList(rs, primaryKeys)));

        return properties.get();
    }

    @RequestMapping(value = "/properties/", produces = {"application/json;charset=utf-8"}, consumes = {"application/json;charset=utf-8"}, method = RequestMethod.POST)
    public ResponseEntity<Property> createProperty(@RequestBody Property property) {
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
        AtomicReference<List<Property>> properties = new AtomicReference<>();
        String query = DatabaseUtil.querySearchParamCreator(property);
        log.info(query);
        DatabaseAccess.getCustom("PROPERTY", query, ((rs, primaryKeys) -> properties.set(getPropertyList(rs, primaryKeys))));
        return properties.get();
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
        AtomicReference<List<Property>> fProperties = new AtomicReference<>();

        DatabaseAccess.getById("PROPERTY", id, ((rs, primaryKeys) -> {
            List<Property> properties = getPropertyList(rs, primaryKeys); // if entry exists
            fProperties.set(properties);
            if (properties.isEmpty()) {
                res[0] = false;
            } else {
                String query = DatabaseUtil.queryUpdateParamCreator(body);
                res[0] = DatabaseAccess.updateEntry("PROPERTY", id, query);
                if (res[0]) {
                    int tempId = ((body.getId() == null) || (body.getId() == id) ? id : body.getId());

                    DatabaseAccess.getById("PROPERTY", tempId, ((rs2, primaryKeys2) -> fProperties.set(getPropertyList(rs2, primaryKeys2))));
                }
            }
        }));

        if (fProperties.get() != null) {
            if (fProperties.get().size() > 0) {
                return new ResponseEntity<>(fProperties.get().get(0), ((res[0]) ? HttpStatus.OK : HttpStatus.BAD_REQUEST));
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
                Property newProperty = new Property(
                        Integer.parseInt(rs.getString("id")),
                        Util.intResultOrNull(rs.getString("listed_price")),
                        Util.intResultOrNull(rs.getString("tm")),
                        rs.getString("type"),
                        rs.getString("Road"),
                        Util.intResultOrNull(rs.getString("Address_num")),
                        Util.intResultOrNull(rs.getString("Floor")),
                        Util.boolResultOrNull(rs.getString("Availability")),
                        Util.intResultOrNull(rs.getString("owner_afm")),
                        Util.intResultOrNull(rs.getString("area_code")));
                properties.add(newProperty);
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return properties;
    }
}

