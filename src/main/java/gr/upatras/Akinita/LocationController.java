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

import static gr.upatras.Akinita.Location.createLocationFromResultSet;

@RestController
public class LocationController {
    private static final Logger log = LoggerFactory.getLogger(LocationController.class);

    /**
     * HTTP get function to find all locations
     * @return
     */
    @RequestMapping(value = "/locations/", produces = {"application/json;charset=utf-8"}, method = RequestMethod.GET)
    public List<Location> getLocations() {
        log.info("Getting all locations");
        AtomicReference<List<Location>> locations = new AtomicReference<>();

        DatabaseAccess.get("LOCATION", (rs, primaryKeys) -> locations.set(getLocationList(rs, primaryKeys)));
        return locations.get();
    }

    /**
     * HTTP get function to find a specific location through its id
     * @param id
     * @return
     */
    @RequestMapping(value = "/locations/{id}", produces = {"application/json;charset=utf-8"}, method = RequestMethod.GET)
    public List<Location> getLocation(@PathVariable("id") int id) {
        log.info("Getting Location¨" + id);
        AtomicReference<List<Location>> locations = new AtomicReference<>();

        DatabaseAccess.getById("LOCATION", id, (rs, primaryKeys) -> locations.set(getLocationList(rs, primaryKeys)));

        return locations.get();
    }

    /**
     * HTTP post function to add a new location
     * @param location
     * @return
     */
    @RequestMapping(value = "/locations/", produces = {"application/json;charset=utf-8"}, consumes = {"application/json;charset=utf-8"}, method = RequestMethod.POST)
    public ResponseEntity<Location> createLocation(@RequestBody Location location) {
        log.info("Will add a new location");
        String q = DatabaseUtil.queryInsertParamCreator(location);
        boolean res = DatabaseAccess.addEntry("LOCATION", q);

        if (res) {
            return new ResponseEntity<>(location, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(location, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 
     * @param location
     * @return
     */
    @RequestMapping(value = "/locations/search", produces = {"application/json;charset=utf-8"}, consumes = {"application/json;charset=utf-8"}, method = RequestMethod.POST)
    public List<Location> searchLocation(@RequestBody Location location) {
        AtomicReference<List<Location>> locations = new AtomicReference<>();
        String query = DatabaseUtil.querySearchParamCreator(location);
        log.info(query);
        DatabaseAccess.getCustom("LOCATION", query, ((rs, primaryKeys) -> locations.set(getLocationList(rs, primaryKeys))));
        return locations.get();
    }

    /**
     * HTTP delete function to delete a specific location through its id
     * @param id
     * @return
     */
    @RequestMapping(value = "/locations/{id}", produces = {"application/json;charset=utf-8"}, method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteLocationById(@PathVariable("id") int id) {
        boolean res = DatabaseAccess.deleteEntry("LOCATION", id);
        if (res) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * HTTP patch function to update the location params
     * @param body
     * @param id
     * @return
     */
    @RequestMapping(value = "/locations/{id}", produces = {"application/json;charset=utf-8"}, consumes = {"application/json;charset=utf-8"}, method = RequestMethod.PATCH)
    ResponseEntity<Location> updateLocation(@RequestBody Location body, @PathVariable("id") int id) {
        Boolean[] res = {null};
        AtomicReference<List<Location>> fLocations = new AtomicReference<>();
        DatabaseAccess.getById("LOCATION", id, ((rs, primaryKeys) -> {
            List<Location> locations = getLocationList(rs, primaryKeys); // if entry exists
            fLocations.set(locations);
            if (locations.isEmpty()) {
                res[0] = false;
            } else {
                String query = DatabaseUtil.queryUpdateParamCreator(body);
                res[0] = DatabaseAccess.updateEntry("LOCATION", id, query);
                if (res[0]) {
                    int tempId = ((body.getPostCode() == null) || (body.getPostCode() == id) ? id : body.getPostCode());

                    DatabaseAccess.getById("LOCATION", tempId, ((rs2, primaryKeys2) -> fLocations.set(getLocationList(rs2, primaryKeys2))));
                }
            }
        }));

        if (fLocations.get() != null) {
            if (fLocations.get().size() > 0) {
                return new ResponseEntity<>(fLocations.get().get(0), ((res[0]) ? HttpStatus.OK : HttpStatus.BAD_REQUEST));
            } else {
                return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Return a list with every location
     * @param rs
     * @param primaryKeys
     * @return
     */
    @SuppressWarnings("unused")
    private List<Location> getLocationList(ResultSet rs, List<String> primaryKeys) {
        List<Location> locations = new ArrayList<>();
        try {
            while (rs.next()) {
                Location newLocation = createLocationFromResultSet(rs);
                locations.add(newLocation);
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return locations;
    }
}

