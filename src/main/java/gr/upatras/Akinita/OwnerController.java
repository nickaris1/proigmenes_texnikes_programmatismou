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
public class OwnerController {
	private static final Logger log = LoggerFactory.getLogger(OwnerController.class);
	
    @RequestMapping(value = "/owners/", produces = {"application/json;charset=utf-8"}, method = RequestMethod.GET)
    public List<Owner> getOwners() {
        log.info("Getting all owners");
        AtomicReference<List<Owner>> owners = new AtomicReference<>();

        DatabaseAccess.get("OWNER", (rs, primaryKeys) -> owners.set(getOwnerList(rs, primaryKeys)));
        return owners.get();
    }

    @RequestMapping(value = "/owners/{id}", produces = {"application/json;charset=utf-8"}, method = RequestMethod.GET)
    public List<Owner> getOwner(@PathVariable("id") int id) {
        log.info("Getting Owner¨" + id);
        AtomicReference<List<Owner>> owners = new AtomicReference<>();

        DatabaseAccess.getById("OWNER", id, (rs, primaryKeys) -> owners.set(getOwnerList(rs, primaryKeys)));

        return owners.get();
    }
    
    @RequestMapping(value = "/owners/", produces = {"application/json;charset=utf-8"}, consumes = {"application/json;charset=utf-8"}, method = RequestMethod.POST)
    public ResponseEntity<Owner> createOwner(@RequestBody Owner owner) {
        log.info("Will add a new owner");
        String q = DatabaseUtil.queryInsertParamCreator(owner);
        boolean res = DatabaseAccess.addEntry("OWNER", q);

        if (res) {
            return new ResponseEntity<>(owner, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(owner, HttpStatus.BAD_REQUEST);
        }
    }
    
    @RequestMapping(value = "/owners/search", produces = {"application/json;charset=utf-8"}, consumes = {"application/json;charset=utf-8"}, method = RequestMethod.POST)
    public List<Owner> searchOwner(@RequestBody Owner owner) {
        AtomicReference<List<Owner>> owners = new AtomicReference<>();
        String query = DatabaseUtil.querySearchParamCreator(owner);
        log.info(query);
        DatabaseAccess.getCustom("OWNER", query, ((rs, primaryKeys) -> owners.set(getOwnerList(rs, primaryKeys))));
        return owners.get();
    }

    @RequestMapping(value = "/owners/{id}", produces = {"application/json;charset=utf-8"}, method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteOwnerById(@PathVariable("id") int id) {
        boolean res = DatabaseAccess.deleteEntry("OWNER", id);
        if (res) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    @RequestMapping(value = "/owners/{id}", produces = {"application/json;charset=utf-8"}, consumes = {"application/json;charset=utf-8"}, method = RequestMethod.PATCH)
    ResponseEntity<Owner> updateOwner(@RequestBody Owner body, @PathVariable("id") int id) {
        Boolean[] res = {null};
        AtomicReference<List<Owner>> fOwners = new AtomicReference<>();
        DatabaseAccess.getById("OWNER", id, ((rs, primaryKeys) -> {
            List<Owner> owners = getOwnerList(rs, primaryKeys); // if entry exists
            fOwners.set(owners);
            if (owners.isEmpty()) {
                res[0] = false;
            } else {
                String query = DatabaseUtil.queryUpdateParamCreator(body);
                res[0] = DatabaseAccess.updateEntry("OWNER", id, query);
                if (res[0]) {
                	int tempId = ((body.getAFM() == null) || (body.getAFM() == id) ? id : body.getAFM());

                    DatabaseAccess.getById("OWNER", tempId, ((rs2, primaryKeys2) -> fOwners.set(getOwnerList(rs2, primaryKeys2))));
                }
            }
        }));

        if (fOwners.get() != null) {
            if (fOwners.get().size() > 0) {
                return new ResponseEntity<>(fOwners.get().get(0), ((res[0]) ? HttpStatus.OK : HttpStatus.BAD_REQUEST));
            } else {
                return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
        }
    }
    
    @SuppressWarnings("unused")
    private List<Owner> getOwnerList(ResultSet rs, List<String> primaryKeys) {
        List<Owner> owners = new ArrayList<>();
        try {
            while (rs.next()) {
                Owner newOwner = new Owner(rs.getString("Fname"), rs.getString("Lname"), Integer.parseInt(rs.getString("Phone")), Integer.parseInt(rs.getString("AFM")));
                owners.add(newOwner);
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return owners;
    }
	

}
