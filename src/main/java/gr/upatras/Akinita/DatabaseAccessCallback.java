package gr.upatras.Akinita;

import java.sql.ResultSet;
import java.util.List;


/**
 * @author issaris nikolaos
 */
public interface DatabaseAccessCallback {
    void run(ResultSet rs, List<String> primaryKeys);
}
