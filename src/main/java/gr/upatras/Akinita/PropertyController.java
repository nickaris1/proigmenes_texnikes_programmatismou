package gr.upatras.Akinita;

import gr.upatras.Database.DatabaseAccess;
import gr.upatras.Database.DatabaseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;


@RestController
public class PropertyController {
    private static final Logger log = LoggerFactory.getLogger(PropertyController.class);

    @RequestMapping(value = "/properties/", produces = {"application/json;charset=utf-8"}, method = RequestMethod.GET)
    public List<Property> getProperties(HttpServletRequest request) {
        log.info("Getting all properties");
        log.info(Util.createRequestLogReport(request));
        AtomicReference<List<Property>> properties = new AtomicReference<>();

        DatabaseAccess.get("PROPERTY", (rs, primaryKeys) -> properties.set(getPropertyList(rs, primaryKeys)));
        return properties.get();
    }

    @RequestMapping(value = "/properties/{id}", produces = {"application/json;charset=utf-8"}, method = RequestMethod.GET)
    public List<Property> getProperty(@PathVariable("id") int id, HttpServletRequest request) {
        log.info("Getting property¨" + id);
        log.info(Util.createRequestLogReport(request));
        AtomicReference<List<Property>> properties = new AtomicReference<>();

        DatabaseAccess.getById("PROPERTY", id, (rs, primaryKeys) -> properties.set(getPropertyList(rs, primaryKeys)));

        return properties.get();
    }

    @RequestMapping(value = "/properties/", produces = {"application/json;charset=utf-8"}, consumes = {"application/json;charset=utf-8"}, method = RequestMethod.POST)
    public ResponseEntity<Property> createProperty(@RequestBody Property property, HttpServletRequest request) {
        log.info("Will add a new property");
        log.info(Util.createRequestLogReport(request));
        String q = DatabaseUtil.queryInsertParamCreator(property);
        boolean res = DatabaseAccess.addEntry("PROPERTY", q);

        if (res) {
            return new ResponseEntity<>(property, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(property, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/properties/search", produces = {"application/json;charset=utf-8"}, consumes = {"application/json;charset=utf-8"}, method = RequestMethod.POST)
    public List<Property> searchProperty(@RequestBody Property property, HttpServletRequest request) {
        log.info(Util.createRequestLogReport(request));
        AtomicReference<List<Property>> properties = new AtomicReference<>();
        String query = DatabaseUtil.querySearchParamCreator(property);
        log.info(query);
        DatabaseAccess.getCustom("PROPERTY", query, ((rs, primaryKeys) -> properties.set(getPropertyList(rs, primaryKeys))));
        return properties.get();
    }

    @RequestMapping(value = "/properties/{id}", produces = {"application/json;charset=utf-8"}, method = RequestMethod.DELETE)
    public ResponseEntity<Void> deletePropertyById(@PathVariable("id") int id, HttpServletRequest request) {
        log.info("Deleting Property: " + id);
        log.info(Util.createRequestLogReport(request));
        boolean res = DatabaseAccess.deleteEntry("PROPERTY", id);
        if (res) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/properties/{id}", produces = {"application/json;charset=utf-8"}, consumes = {"application/json;charset=utf-8"}, method = RequestMethod.PATCH)
    ResponseEntity<Property> updateProperty(@RequestBody Property body, @PathVariable("id") int id, HttpServletRequest request) {
        log.info("Update Location: " + id);
        log.info(Util.createRequestLogReport(request));
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

    /**
     * Create Property List from database ResultSet
     * @param rs the resultSet from database
     * @param primaryKeys the resultSet from database
     * @return return List&lt;Property&gt;
     */
    private List<Property> getPropertyList(ResultSet rs, List<String> primaryKeys) {
        List<Property> properties = new ArrayList<>();
        try {
            while (rs.next()) {
                Property newProperty = Property.createPropertyFromResultSet(rs);
                properties.add(newProperty);
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return properties;
    }
}

