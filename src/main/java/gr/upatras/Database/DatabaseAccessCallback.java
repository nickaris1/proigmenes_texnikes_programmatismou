package gr.upatras.Database;

import java.sql.ResultSet;
import java.util.List;


/**
 * Callback interface for database.
 * <br>
 *
 * @author issaris nikolaos
 */
public interface DatabaseAccessCallback {
    void run(ResultSet rs, List<String> primaryKeys);
}
