package gr.upatras.Akinita;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.crypto.Data;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

@RestController
public class LocationController {
    private static final Logger log = LoggerFactory.getLogger(LocationController.class);

    @RequestMapping(value = "/locations/", produces = {"application/json;charset=utf-8"}, method = RequestMethod.GET)
    public List<Location> getLocations() {
        log.info("Getting all locations");
        List<Location> locations = new ArrayList<>();

        DatabaseAccess.get("LOCATION", (rs, primaryKeys) -> {
            try {
                while (rs.next()) {
                    Location newLocation = new Location(rs.getString("City"), rs.getString("Area"), rs.getString("County"), Integer.parseInt(rs.getString("Area_code")));
                    locations.add(newLocation);
                }
            } catch (SQLException e) {
                log.error(e.getMessage());
            }
        });
        return locations;
    }

    @RequestMapping(value = "/locations/{id}", produces = {"application/json;charset=utf-8"}, method = RequestMethod.GET)
    public Location getLocation(@PathVariable("id") int id) {
        log.info("Getting Location¨" + id);
        final Location[] newLocation = {null};

        DatabaseAccess.getById("LOCATION", id, (rs, primaryKeys) -> {
            try {
                while (rs.next()) {
                    newLocation[0] = new Location(rs.getString("City"), rs.getString("Area"), rs.getString("County"), Integer.parseInt(rs.getString("Area_code")));
                }
            } catch (SQLException e) {
                log.error(e.getMessage());
            }
        });

        return newLocation[0];
    }

    @RequestMapping(value = "/locations", produces = {"application/json;charset=utf-8"}, consumes = {"application/json;charset=utf-8"}, method = RequestMethod.POST)
    public ResponseEntity<Location> createLocation(@RequestBody Location location) {
        log.info("Will add a new location");
        String q;
        if (location.getPostCode() != null) {
            q = "(\"Area_code\", \"City\", \"Area\", \"County\") VALUES (\"" + location.getPostCode() + "\", \"" + location.getCity() + "\", \"" + location.getArea() + "\", \"" + location.getCounty() + "\")";
        } else {
            q = "(\"City\", \"Area\", \"County\") VALUES (\"" + location.getCity() + "\", \"" + location.getArea() + "\", \"" + location.getCounty() + "\")";
        }
        boolean res = DatabaseAccess.addEntry("LOCATION", q);
        if (res) {
            return new ResponseEntity<>(location, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(location, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/locations/search", produces = {"application/json;charset=utf-8"}, consumes = {"application/json;charset=utf-8"}, method = RequestMethod.POST)
    public ResponseEntity<Location> searchLocation(@RequestBody Location location) {
//        List<String> query = new ArrayList<>();
        log.info(DatabaseAccess.queryCreator(location));
        return new ResponseEntity<>(location, HttpStatus.BAD_REQUEST);
    }
}
