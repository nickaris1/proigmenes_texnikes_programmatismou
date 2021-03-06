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
public class OwnerController {
	private static final Logger log = LoggerFactory.getLogger(OwnerController.class);
	
	/**
	 * HTTP get function to find all owners
	 * @return
	 */
    @RequestMapping(value = "/owners/", produces = {"application/json;charset=utf-8"}, method = RequestMethod.GET)
    public List<Owner> getOwners(HttpServletRequest request) {
        log.info("Getting all owners");
        log.info(Util.createRequestLogReport(request));
        AtomicReference<List<Owner>> owners = new AtomicReference<>();

        DatabaseAccess.get("OWNER", (rs, primaryKeys) -> owners.set(getOwnerList(rs, primaryKeys)));
        return owners.get();
    }

    /** 
     * HTTP get function to find a specific owner through his id
     * @param id
     * @return
     */
    @RequestMapping(value = "/owners/{id}", produces = {"application/json;charset=utf-8"}, method = RequestMethod.GET)
    public List<Owner> getOwner(@PathVariable("id") int id, HttpServletRequest request) {
        log.info("Getting Owner¨" + id);
        log.info(Util.createRequestLogReport(request));
        AtomicReference<List<Owner>> owners = new AtomicReference<>();

        DatabaseAccess.getById("OWNER", id, (rs, primaryKeys) -> owners.set(getOwnerList(rs, primaryKeys)));

        return owners.get();
    }
    
    /**
     * HTTP post function to add a new owner
     * @param owner
     * @return
     */
    @RequestMapping(value = "/owners/", produces = {"application/json;charset=utf-8"}, consumes = {"application/json;charset=utf-8"}, method = RequestMethod.POST)
    public ResponseEntity<Owner> createOwner(@RequestBody Owner owner, HttpServletRequest request) {
        log.info("Will add a new owner");
        log.info(Util.createRequestLogReport(request));
        String q = DatabaseUtil.queryInsertParamCreator(owner);
        boolean res = DatabaseAccess.addEntry("OWNER", q);

        if (res) {
            return new ResponseEntity<>(owner, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(owner, HttpStatus.BAD_REQUEST);
        }
    }
    
    /**
     * 
     * @param owner
     * @return
     */
    @RequestMapping(value = "/owners/search", produces = {"application/json;charset=utf-8"}, consumes = {"application/json;charset=utf-8"}, method = RequestMethod.POST)
    public List<Owner> searchOwner(@RequestBody Owner owner, HttpServletRequest request) {
        log.info(Util.createRequestLogReport(request));
        AtomicReference<List<Owner>> owners = new AtomicReference<>();
        String query = DatabaseUtil.querySearchParamCreator(owner);
        log.info(query);
        DatabaseAccess.getCustom("OWNER", query, ((rs, primaryKeys) -> owners.set(getOwnerList(rs, primaryKeys))));
        return owners.get();
    }

    /**
     * HTTP delete function to delete a specific owner through his id
     * @param id
     * @return
     */
    @RequestMapping(value = "/owners/{id}", produces = {"application/json;charset=utf-8"}, method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteOwnerById(@PathVariable("id") int id, HttpServletRequest request) {
        log.info("Deleting owner: " + id);
        log.info(Util.createRequestLogReport(request));
        boolean res = DatabaseAccess.deleteEntry("OWNER", id);
        if (res) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    /**
     * HTTP patch function to update the owner params
     * @param body
     * @param id
     * @return
     */
    @RequestMapping(value = "/owners/{id}", produces = {"application/json;charset=utf-8"}, consumes = {"application/json;charset=utf-8"}, method = RequestMethod.PATCH)
    ResponseEntity<Owner> updateOwner(@RequestBody Owner body, @PathVariable("id") int id, HttpServletRequest request) {
        log.info("Update Owner: " + id);
        log.info(Util.createRequestLogReport(request));
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
    
    /**
     * Return a list with every owner
     * @param rs
     * @param primaryKeys
     * @return
     */
    private List<Owner> getOwnerList(ResultSet rs, List<String> primaryKeys) {
        List<Owner> owners = new ArrayList<>();
        try {
            while (rs.next()) {
                Owner newOwner = Owner.createOwnerFromResultSet(rs);
                owners.add(newOwner);
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return owners;
    }
	

}
