package gr.upatras.Akinita;

import com.google.gson.Gson;
import gr.upatras.Database.DatabaseAccess;
import gr.upatras.Database.DatabaseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@RestController
public class CustomSearchController {

    private static final Logger log = LoggerFactory.getLogger(CustomSearchController.class);

    /**
     * HTTP post function to add a property location
     * @param search json Search object
     */
    @RequestMapping(value = "/search/", produces = {"application/json;charset=utf-8"}, consumes = {"application/json;charset=utf-8"}, method = RequestMethod.POST)
    public ResponseEntity<List<CustomSearch>> createLocation(@RequestBody CustomSearch search, HttpServletRequest request) {
        log.info("CustomSearch");
        log.info(Util.createRequestLogReport(request));
        AtomicReference<List<CustomSearch>> searchRes = new AtomicReference<>();


        String q = DatabaseUtil.searchQuery(search);

        boolean ret = DatabaseAccess.runCustomQuery(q, (rs, pk) -> searchRes.set(getSearchList(rs, pk)));

        return new ResponseEntity<>(searchRes.get(), (ret)? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    /**
     * Create CustomSearch List from database ResultSet
     * @param rs the resultSet from database
     * @param primaryKeys the resultSet from database
     * @return return List&lt;CustomSearch&gt;
     */
    @SuppressWarnings("unused")
    private List<CustomSearch> getSearchList(ResultSet rs, List<String> primaryKeys) {
        List<CustomSearch> search = new ArrayList<>();
        try {
            while (rs.next()) {
                Location loc = null;
                if (getFieldOrNull(rs, "City") != null) {
                    loc = Location.createLocationFromResultSet(rs);
                }

                Property prop = null;
                if (getFieldOrNull(rs, "listed_price") != null) {
                    prop = Property.createPropertyFromResultSet(rs);
                }

                Owner own = null;
                if (getFieldOrNull(rs, "Fname") != null) {
                    own = Owner.createOwnerFromResultSet(rs);
                }

                Sale sale = null;
                if (getFieldOrNull(rs, "price") != null) {
                    sale = Sale.createSaleFromResultSet(rs);
                }

                search.add(new CustomSearch(loc, own, prop, sale, new Modifiers()));
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return search;
    }

    /**
     * Get string from ResultSet(fieldname) if it exist else return null
     * @param rs Resultset
     * @param fieldName fieldname
     * @return String
     */
    private String getFieldOrNull(ResultSet rs, String fieldName) {
        try {
            return rs.getString(fieldName);
        } catch (SQLException e) {
            return null;
        }
    }
}
