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
public class SaleController {
	private static final Logger log = LoggerFactory.getLogger(SaleController.class);
	
    @RequestMapping(value = "/sales/", produces = {"application/json;charset=utf-8"}, method = RequestMethod.GET)
    public List<Sale> getSales() {
        log.info("Getting all Sales");
        AtomicReference<List<Sale>> sales = new AtomicReference<>();

        DatabaseAccess.get("SALE", (rs, primaryKeys) -> sales.set(getSaleList(rs, primaryKeys)));
        return sales.get();
    }

    @RequestMapping(value = "/sales/{id}", produces = {"application/json;charset=utf-8"}, method = RequestMethod.GET)
    public List<Sale> getSale(@PathVariable("id") int id) {
        log.info("Getting Sale¨" + id);
        AtomicReference<List<Sale>> sales = new AtomicReference<>();

        DatabaseAccess.getById("SALE", id, (rs, primaryKeys) -> sales.set(getSaleList(rs, primaryKeys)));

        return sales.get();
    }
    
    @RequestMapping(value = "/sales/", produces = {"application/json;charset=utf-8"}, consumes = {"application/json;charset=utf-8"}, method = RequestMethod.POST)
    public ResponseEntity<Sale> createSale(@RequestBody Sale sale) {
        log.info("Will add a new Sale");
        String q = DatabaseUtil.queryInsertParamCreator(sale);
        boolean res = DatabaseAccess.addEntry("SALE", q);

        if (res) {
            return new ResponseEntity<>(sale, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(sale, HttpStatus.BAD_REQUEST);
        }
    }
    
    @RequestMapping(value = "/sales/search", produces = {"application/json;charset=utf-8"}, consumes = {"application/json;charset=utf-8"}, method = RequestMethod.POST)
    public List<Sale> searchSale(@RequestBody Sale sale) {
        AtomicReference<List<Sale>> sales = new AtomicReference<>();
        String query = DatabaseUtil.querySearchParamCreator(sale);
        log.info(query);
        DatabaseAccess.getCustom("Sale", query, ((rs, primaryKeys) -> sales.set(getSaleList(rs, primaryKeys))));
        return sales.get();
    }

    @RequestMapping(value = "/sales/{id}", produces = {"application/json;charset=utf-8"}, method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteSaleById(@PathVariable("id") int id) {
        boolean res = DatabaseAccess.deleteEntry("SALE", id);
        if (res) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    @RequestMapping(value = "/sales/{id}", produces = {"application/json;charset=utf-8"}, consumes = {"application/json;charset=utf-8"}, method = RequestMethod.PATCH)
    ResponseEntity<Sale> updateSale(@RequestBody Sale body, @PathVariable("id") int id) {
        Boolean[] res = {null};
        AtomicReference<List<Sale>> fSales = new AtomicReference<>();
        
        DatabaseAccess.getById("SALE", id, ((rs, primaryKeys) -> {
            List<Sale> sales = getSaleList(rs, primaryKeys); // if entry exists
            fSales.set(sales);
            if (sales.isEmpty()) {
                res[0] = false;
            } else {
                String query = DatabaseUtil.queryUpdateParamCreator(body);
                res[0] = DatabaseAccess.updateEntry("SALE", id, query);
                if (res[0]) {
                	int tempId = ((body.getID() == null) || (body.getID() == id) ? id : body.getID());

                    DatabaseAccess.getById("SALE", tempId, ((rs2, primaryKeys2) -> fSales.set(getSaleList(rs2, primaryKeys2))));
                }
            }
        }));

        if (fSales.get() != null) {
            if (fSales.get().size() > 0) {
                return new ResponseEntity<>(fSales.get().get(0), ((res[0]) ? HttpStatus.OK : HttpStatus.BAD_REQUEST));
            } else {
                return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
        }
    }
    
    private List<Sale> getSaleList(ResultSet rs, List<String> primaryKeys) {
        List<Sale> sales = new ArrayList<>();
        try {
            while (rs.next()) {
                Sale newSale = new Sale(
                		Integer.parseInt(rs.getString("ID")),
                		rs.getString("Date"),
                		rs.getString("Price"),
                		rs.getString("TM"),
                		Util.boolResultOrNull(rs.getString("Rental")),
                		Util.intResultOrNull(rs.getString("Warranty")),
                		rs.getString("StartDate"),
                		rs.getString("EndDate"),
                		Integer.parseInt(rs.getString("Property_id")));
                sales.add(newSale);
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return sales;
    }
	

}
