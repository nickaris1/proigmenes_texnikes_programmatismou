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
public class SaleController {
	private static final Logger log = LoggerFactory.getLogger(SaleController.class);
	
    @RequestMapping(value = "/sales/", produces = {"application/json;charset=utf-8"}, method = RequestMethod.GET)
    public List<Sale> getSales(HttpServletRequest request) {
        log.info("Getting all Sales");
        log.info(Util.createRequestLogReport(request));
        AtomicReference<List<Sale>> sales = new AtomicReference<>();

        DatabaseAccess.get("SALE", (rs, primaryKeys) -> sales.set(getSaleList(rs, primaryKeys)));
        return sales.get();
    }

    @RequestMapping(value = "/sales/{id}", produces = {"application/json;charset=utf-8"}, method = RequestMethod.GET)
    public List<Sale> getSale(@PathVariable("id") int id, HttpServletRequest request) {
        log.info("Getting Sale¨" + id);
        log.info(Util.createRequestLogReport(request));
        AtomicReference<List<Sale>> sales = new AtomicReference<>();

        DatabaseAccess.getById("SALE", id, (rs, primaryKeys) -> sales.set(getSaleList(rs, primaryKeys)));

        return sales.get();
    }
    
    @RequestMapping(value = "/sales/", produces = {"application/json;charset=utf-8"}, consumes = {"application/json;charset=utf-8"}, method = RequestMethod.POST)
    public ResponseEntity<Sale> createSale(@RequestBody Sale sale, HttpServletRequest request) {
        log.info("Will add a new Sale");
        log.info(Util.createRequestLogReport(request));
        String q = DatabaseUtil.queryInsertParamCreator(sale);
        boolean res = DatabaseAccess.addEntry("SALE", q);

        if (res) {
            return new ResponseEntity<>(sale, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(sale, HttpStatus.BAD_REQUEST);
        }
    }
    
    @RequestMapping(value = "/sales/search", produces = {"application/json;charset=utf-8"}, consumes = {"application/json;charset=utf-8"}, method = RequestMethod.POST)
    public List<Sale> searchSale(@RequestBody Sale sale, HttpServletRequest request) {
        log.info(Util.createRequestLogReport(request));
        AtomicReference<List<Sale>> sales = new AtomicReference<>();
        String query = DatabaseUtil.querySearchParamCreator(sale);
        log.info(query);
        DatabaseAccess.getCustom("Sale", query, ((rs, primaryKeys) -> sales.set(getSaleList(rs, primaryKeys))));
        return sales.get();
    }

    @RequestMapping(value = "/sales/{id}", produces = {"application/json;charset=utf-8"}, method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteSaleById(@PathVariable("id") int id, HttpServletRequest request) {
        log.info("Deleting Sale: " + id);
        log.info(Util.createRequestLogReport(request));
        boolean res = DatabaseAccess.deleteEntry("SALE", id);
        if (res) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    @RequestMapping(value = "/sales/{id}", produces = {"application/json;charset=utf-8"}, consumes = {"application/json;charset=utf-8"}, method = RequestMethod.PATCH)
    ResponseEntity<Sale> updateSale(@RequestBody Sale body, @PathVariable("id") int id, HttpServletRequest request) {
        log.info("Update Sale: " + id);
        log.info(Util.createRequestLogReport(request));
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
                	int tempId = ((body.getSaleID() == null) || (body.getSaleID() == id) ? id : body.getSaleID());

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

    /**
     * Create Sales List from database ResultSet
     * @param rs the resultSet from database
     * @param primaryKeys the resultSet from database
     * @return return List&lt;Sale&gt;
     */
    private List<Sale> getSaleList(ResultSet rs, List<String> primaryKeys) {
        List<Sale> sales = new ArrayList<>();
        try {
            while (rs.next()) {
                Sale newSale = Sale.createSaleFromResultSet(rs);
                sales.add(newSale);
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return sales;
    }
	

}
