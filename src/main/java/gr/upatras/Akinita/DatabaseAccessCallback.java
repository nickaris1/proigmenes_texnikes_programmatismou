package gr.upatras.Akinita;

import java.sql.ResultSet;


/**
 * @author issaris nikolaos
 */
public interface DatabaseAccessCallback {
    void run(ResultSet rs);
}
