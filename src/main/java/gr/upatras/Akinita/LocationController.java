package gr.upatras.Akinita;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class LocationController {
    private static final Logger log = LoggerFactory.getLogger(LocationController.class);

    @RequestMapping(value = "/locations/", produces = {"application/json;charset=utf-8"}, method = RequestMethod.GET)
    public List<Location> getLocations() {
        log.debug("Getting Locations");
        List<Location> locations = new ArrayList<>();
        DatabaseAccess.get("LOCATION", new DatabaseAccessCallback() {
            @Override
            public void run(ResultSet rs) {
                try {
                    while (rs.next()) {
                        Location newLocation = new Location(rs.getString("City"),
                                rs.getString("Area"),
                                rs.getString("County"),
                                Integer.parseInt(rs.getString("Area_code"))
                        );
                        locations.add(newLocation);
                    }
                } catch (SQLException e) {
                    log.error(e.getMessage());
                }
            }
        });
//        List<Location> locations = DatabaseAccess.get("");

        return locations;
    }
}
